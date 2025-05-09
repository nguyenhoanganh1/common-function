package com.tech.common.repository.redis;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface BaseKeyValueRepository<T, ID> extends KeyValueRepository<T, ID> {

}
