package com.tech.common.repository;

import com.tech.common.entity.User;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

    @QueryHints(
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
    )
    @ContinueSpan
    @Override
    List<User> findAll();
}
