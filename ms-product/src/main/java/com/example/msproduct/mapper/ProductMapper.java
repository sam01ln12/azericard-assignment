package com.example.msproduct.mapper;

import com.example.msproduct.model.Product;
import com.example.msproduct.model.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toProductDto(Product product);

    List<ProductDto> productDtos(List<Product> products);
}
