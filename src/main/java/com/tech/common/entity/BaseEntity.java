package com.tech.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity<T> implements Serializable {

    @CreatedBy
    private T createdBy;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedBy
    private T updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedTime;

}
