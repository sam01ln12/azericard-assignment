package com.example.msproduct.service;

import com.example.msproduct.error.ProductAlreadyExistsException;
import com.example.msproduct.error.ProductHasEmptyStockException;
import com.example.msproduct.error.ProductNotFoundException;
import com.example.msproduct.mapper.ProductMapper;
import com.example.msproduct.model.Product;
import com.example.msproduct.model.ProductDto;
import com.example.msproduct.repository.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private static final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Transactional
    public ProductDto addProduct (ProductDto productDto) {

        Optional<Product> product = productRepository.findProductByProductName(productDto.getProductName());

        if (product.isPresent()) {
            throw new ProductAlreadyExistsException("Product with name " +  productDto.getProductName()
                    + " already exists");
        }

        Product newProduct = Product.builder()
                .productName(productDto.getProductName())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return productMapper.toProductDto(productRepository.save(newProduct));
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public ProductDto buyProduct(String productName) {

        Product product = productRepository.findProductByProductName(productName).orElseThrow(
                () -> new ProductNotFoundException("No product with name " + productName + " was found"));

        BigInteger stock = product.getStock();
        if (stock.compareTo(BigInteger.ZERO) == 0) {

            throw new ProductHasEmptyStockException("Stock is empty for product " + productName);
        }

        log.info("Buying product : {}", product);
        product.setStock(stock.subtract(BigInteger.ONE));
        product.setUpdatedAt(LocalDateTime.now());

        Product boughtProduct = productRepository.save(product);
        log.info("Product was bought : {}", product);

        return productMapper.toProductDto(boughtProduct);
    }

    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public ProductDto reverseProduct(String productName) {

        Product product = productRepository.findProductByProductName(productName).orElseThrow(
                () -> new ProductNotFoundException("No product with name " + productName + " was found"));

        log.info("Reverse product : {}", product);
        product.setStock(product.getStock().add(BigInteger.ONE));
        product.setUpdatedAt(LocalDateTime.now());

        return productMapper.toProductDto(productRepository.save(product));
    }

    @Transactional
    public List<ProductDto> getAvailableProducts(int pageNumber, int pageSize) {

        List<Product> availableProducts = productRepository.findAllByStockGreaterThan(
                BigInteger.ZERO, PageRequest.of(pageNumber, pageSize)).getContent();

        return productMapper.productDtos(availableProducts);
    }
}
