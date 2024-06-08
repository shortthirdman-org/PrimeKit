package com.shortthirdman.primekit.essentials.generics.webmvc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public class AbstractBaseDTO {

    private Long id;

    private int version;

    @JsonIgnoreProperties
    private LocalDateTime createdAt;

    @JsonIgnoreProperties
    private LocalDateTime updatedAt;
}
