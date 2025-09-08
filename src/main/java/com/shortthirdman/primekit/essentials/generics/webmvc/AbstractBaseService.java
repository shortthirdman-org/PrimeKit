package com.shortthirdman.primekit.essentials.generics.webmvc;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
@RequiredArgsConstructor
public abstract class AbstractBaseService<T extends AbstractBaseEntity, D, ID extends Serializable> {

    @Autowired
    AbstractBaseRepository<T, ID> repository;

    Class<D> dtoClass;

    Class<T> entityClass;
}
