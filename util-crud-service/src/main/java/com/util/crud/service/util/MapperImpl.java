package com.util.crud.service.util;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperImpl<T> implements Mapper<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperImpl.class);

    private Class<T> targetClass;

    public void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public T map(Object sourceObj) {
        T target = null;
        try {
            target = targetClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOGGER.error(null, e);
        }
        assert target != null;
        try {
            BeanUtils.copyProperties(target, sourceObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(null, e);
        }
        return target;
    }

    public List<T> mapList(List sourceObj) {
        return (List<T>) sourceObj.stream().map(s -> map(s)).collect(Collectors.toList());
    }
}
