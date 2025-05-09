package com.tech.common.test.redis;

import com.tech.common.repository.redis.BaseKeyValueRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends BaseKeyValueRepository<Product, String> {

    Optional<Product> findByIdAndName(String id, String name);
}
