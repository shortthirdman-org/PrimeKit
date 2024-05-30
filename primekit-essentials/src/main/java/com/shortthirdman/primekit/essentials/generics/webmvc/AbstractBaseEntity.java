package com.shortthirdman.primekit.essentials.generics.webmvc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EntityListeners(AuditingEntityListener.class)
public class AbstractBaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private int version;

    @JsonIgnoreProperties
    private LocalDateTime createdAt;

    @JsonIgnoreProperties
    private LocalDateTime updatedAt;

    @PrePersist
    public void onPrePersist() {
    }

    @PreUpdate
    public void onPreUpdate() {
    }
}
