package com.util.crud.service.util;

import java.util.List;

public interface Mapper<T> {

    void setTargetClass(Class<T> targetClass);

    List<T> mapList(List sourceObj);

    T map(Object sourceObj);
}
