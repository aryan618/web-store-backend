package com.aryan.store.controllers;

import com.aryan.store.dtos.ProductDto;
import com.aryan.store.entities.Category;
import com.aryan.store.entities.Product;
import com.aryan.store.mappers.ProductMapper;
import com.aryan.store.repositories.CategoryRepository;
import com.aryan.store.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping()
    public ResponseEntity
            <List<ProductDto>> getProducts(@RequestParam(required = false) Byte category_id) {

        List<ProductDto> productDtos = new ArrayList<>();
        if(category_id!=null){
           List<Product> product1= productRepository.findByCategory_id(category_id);
           if(product1==null){
               return ResponseEntity.notFound().build();
           }
            for(Product product: product1) {
                //  productDtos.add(new ProductDto(product.getId(),product.getName(),product.getDescription(),product.getPrice(),product.getCategory().getId()));
                ProductDto productDto= productMapper.toDto(product);
                productDtos.add(productDto);
            }
             return ResponseEntity.ok(productDtos);
        }
        List<Product> products= productRepository.findAll();


        for(Product product: products) {
          //  productDtos.add(new ProductDto(product.getId(),product.getName(),product.getDescription(),product.getPrice(),product.getCategory().getId()));
            ProductDto productDto= productMapper.toDto(product);
            productDtos.add(productDto);
        }

        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getParticularProduct(@PathVariable Long id){
        Optional<Product> product= productRepository.findById(id);
        if(product.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(product.get()));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        Category category= categoryRepository.findById(productDto.getCategory_id()).orElse(null);
        if(category==null){
            return ResponseEntity.notFound().build();
        }
        Product product= productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping("/{id}/update-product")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        Product product= productRepository.findById(id).orElse(null);
        if(product==null){
            return ResponseEntity.notFound().build();
        }
        Category category= categoryRepository.findById(productDto.getCategory_id()).orElse(null);
        if(category==null){
            return ResponseEntity.notFound().build();
        }
        productMapper.updateEntity(productDto,product);

        product.setCategory(category);

        productRepository.save(product);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        Product product= productRepository.findById(id).orElse(null);
        if(product==null){
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);

        return ResponseEntity.noContent().build();
    }
}
