package com.tech.common.test.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct(String id, String name) {
        return productRepository.findByIdAndName(id, name)
                .orElseGet(() -> {
                    Product product = new Product();
                    product.setId(id);
                    product.setName(name);
                    product.setTimeToLive(1000L);
                    return productRepository.save(product);
                });
    }
}
