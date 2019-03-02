package com.util.crud.dao.impl;

import com.util.crud.dao.AbstractDao;
import com.util.crud.dao.util.ParamsProcessor;
import com.util.crud.entity.AbstractEntity;
import com.util.crud.params.AbstractParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractDaoImpl<E extends AbstractEntity, P extends AbstractParams> implements AbstractDao<E, P> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDaoImpl.class);
    @PersistenceContext
    private EntityManager em;
    private Class<E> entityClass;

    @PostConstruct
    public void init() {
        Class[] generics = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractDaoImpl.class);
        if (generics != null) {
            entityClass = generics[0];
        }
    }

    @Override
    public List<E> filter(P params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> q = cb.createQuery(entityClass);
        Root<E> c = q.from(entityClass);
        q.select(c);

        ParamsProcessor<P> paramsProcessor = new ParamsProcessor<>(cb, c);
        paramsProcessor.processParams(params);

        List<Predicate> predicates = paramsProcessor.getPredicates();

        TypedQuery<E> t = em.createQuery(q.where(predicates.toArray(new Predicate[predicates.size()])));
        paramsProcessor.getParams().forEach(t::setParameter);

        return t.getResultList();
    }

    @Override
    public E getById(P id) {
        List<E> result = this.filter(id);
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void remove(P id) {
        E entity = this.getById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    @Override
    public void remove(List<P> id) {
        id.forEach(this::remove);
    }

    @Override
    public E update(P id) {
        E entity = this.getById(id);
        return em.merge(entity);
    }

    @Override
    public List<E> update(List<P> id) {
        return id.stream().map(this::update).collect(Collectors.toList());
    }

    @Override
    public E create(P id) {
        E newEntity = null;
        try {
            newEntity = entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error(null, e);
        }
        return em.merge(newEntity);
    }

    @Override
    public List<E> create(List<P> id) {
        return id.stream().map(this::create).collect(Collectors.toList());
    }
}
