package com.shortthirdman.primekit.essentials.generics.webmvc;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public class AbstractBaseController<T extends AbstractBaseEntity, D extends AbstractBaseDTO, ID extends Serializable> {
}
