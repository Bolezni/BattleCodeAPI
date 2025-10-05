package com.bolezni.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}
