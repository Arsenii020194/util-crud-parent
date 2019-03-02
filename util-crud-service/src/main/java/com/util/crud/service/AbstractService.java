package com.util.crud.service;

import com.util.crud.dto.AbstractDto;
import com.util.crud.params.AbstractParams;

import java.util.List;

public interface AbstractService<D extends AbstractDto, P extends AbstractParams> {

    List<D> filter(P params);

    D getById(P id);

    void remove(P id);

    void remove(List<P> id);

    D update(P id);

    List<D> update(List<P> id);

    D create(P id);

    List<D> create(List<P> id);
}
