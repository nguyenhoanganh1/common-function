package com.tech.common.test.controller;

import com.tech.common.test.redis.Product;
import com.tech.common.test.redis.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProducerController {

    private final ProductService productService;

    @GetMapping("/{id}/{name}")
    public ResponseEntity<Product> getProductResponseEntity(
            @PathVariable("id") String id,
            @PathVariable("name") String name
    ) {
        Product product = productService.getProduct(id, name);
        return ResponseEntity.ok(product);
    }


}
