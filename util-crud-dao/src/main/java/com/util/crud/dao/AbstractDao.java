package com.util.crud.dao;

import com.util.crud.entity.AbstractEntity;
import com.util.crud.params.AbstractParams;

import java.util.List;

public interface AbstractDao<E extends AbstractEntity, P extends AbstractParams> {

    List<E> filter(P params);

    E getById(P id);

    void remove(P id);

    void remove(List<P> id);

    E update(P id);

    List<E> update(List<P> id);

    E create(P id);

    List<E> create(List<P> id);
}
