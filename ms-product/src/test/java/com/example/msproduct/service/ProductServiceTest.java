package com.example.msproduct.service;

import com.example.msproduct.error.ProductAlreadyExistsException;
import com.example.msproduct.error.ProductHasEmptyStockException;
import com.example.msproduct.error.ProductNotFoundException;
import com.example.msproduct.model.Product;
import com.example.msproduct.model.ProductDto;
import com.example.msproduct.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository mockProductRepository;

    private ProductService productService;

    private ProductDto productDto;
    private Product product;


    @BeforeEach
    void setUp() {
        productService = new ProductService(mockProductRepository);

        productDto = new ProductDto("productName", new BigInteger("100"), BigDecimal.ONE);

        product = Product.builder()
                .productName("productName")
                .stock(new BigInteger("100"))
                .price(BigDecimal.ONE)
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .build();

    }

    @Test
    void testAddProductThrowsProductAlreadyExistsException() {

        when(mockProductRepository.findProductByProductName("productName")).thenReturn(Optional.ofNullable(product));

        assertThrows(ProductAlreadyExistsException.class, () -> productService.addProduct(productDto));
    }

    @Test
    void testAddProduct() {

        when(mockProductRepository.findProductByProductName("productName")).thenReturn(Optional.empty());

        productService.addProduct(productDto);

        verify(mockProductRepository).save(any(Product.class));
    }


    @Test
    void testBuyProduct() {

        when(mockProductRepository.findProductByProductName("productName")).thenReturn(Optional.ofNullable(product));

        assertEquals(new BigInteger("99"), product.getStock());
    }

    @Test
    void testBuyProductThrowsProductNotFoundException() {

        when(mockProductRepository.findProductByProductName("productName")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.buyProduct("productName"));
    }

    @Test
    void testBuyProductThrowsProductHasEmptyStockException() {

        product.setStock(BigInteger.ZERO);
        when(mockProductRepository.findProductByProductName("productName")).thenReturn(Optional.ofNullable(product));

        assertThrows(ProductHasEmptyStockException.class, () -> productService.buyProduct("productName"));
    }

    @Test
    void testBuyProductThrowsOptimisticLockingFailureException() {

        when(mockProductRepository.findProductByProductName("productName")).thenReturn(Optional.ofNullable(product));

        when(mockProductRepository.save(any(Product.class))).thenThrow(OptimisticLockingFailureException.class);

        assertThrows(OptimisticLockingFailureException.class, () ->
                productService.buyProduct("productName"));

    }

    @Test
    void testGetAvailableProducts() {

        final Page<Product> products = new PageImpl<>(List.of(Product.builder()
                .productName("productName1")
                .stock(new BigInteger("73"))
                .price(BigDecimal.TEN)
                .createdAt(LocalDateTime.of(2023, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2023, 1, 1, 0, 0, 0))
                .build(), product));
        when(mockProductRepository.findAllByStockGreaterThan(BigInteger.ZERO,
                PageRequest.of(0, 10))).thenReturn(products);

        final List<ProductDto> result = productService.getAvailableProducts(0, 10);

        assertEquals(2, result.size());
    }

    @AfterEach
    void tearDown() {
        productDto = null;
        product = null;
    }
}