package com.aryan.store.mappers;

import com.aryan.store.dtos.ProductDto;
import com.aryan.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category_id", source = "category.id")
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);

    @Mapping(target="id", ignore = true)
    void updateEntity(ProductDto productDto, @MappingTarget Product product);
}
