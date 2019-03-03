package com.util.crud.service.impl;

import com.util.crud.dao.AbstractDao;
import com.util.crud.dto.AbstractDto;
import com.util.crud.entity.AbstractEntity;
import com.util.crud.params.AbstractParams;
import com.util.crud.service.AbstractService;
import com.util.crud.service.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public abstract class AbstractServiceImpl<D extends AbstractDto, P extends AbstractParams> implements AbstractService<D, P> {

    private Mapper<D> mapper;
    private Class<D> dtoClass;

    @PostConstruct
    public void init() {
        Class[] generics = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractServiceImpl.class);
        if (generics != null) {
            dtoClass = generics[0];
        }
        if (mapper != null) {
            mapper.setTargetClass(dtoClass);
        }
    }

    @Override
    public List<D> filter(P params) {
        return mapper.mapList(getDao().filter(params));
    }

    @Override
    public D getById(P id) {
        return mapper.map(getDao().getById(id));
    }

    @Override
    public void remove(P id) {
        getDao().remove(id);
    }

    @Override
    public void remove(List<P> id) {
        id.forEach(this::remove);
    }

    @Override
    public D update(P id) {
        return mapper.map(getDao().update(id));
    }

    @Override
    public List<D> update(List<P> id) {
        return id.stream().map(this::update).collect(Collectors.toList());
    }

    @Override
    public D create(P id) {
        return mapper.map(getDao().create(id));
    }

    @Override
    public List<D> create(List<P> id) {
        return id.stream().map(this::create).collect(Collectors.toList());
    }

    public abstract AbstractDao<? extends AbstractEntity, P> getDao();

    @Autowired
    public void setMapper(Mapper<D> mapper) {
        this.mapper = mapper;
    }
}
