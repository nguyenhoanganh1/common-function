package com.tech.common.repository;

import com.tech.common.entity.User;
import io.micrometer.tracing.annotation.ContinueSpan;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

    @ContinueSpan
    @Override
    List<User> findAll();
}
