package com.util.crud.dao.util;

import com.util.crud.params.AbstractParams;
import com.util.crud.params.Cause;
import com.util.crud.params.QueryParam;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamsProcessor<T extends AbstractParams> {

    private List<Predicate> predicates;
    private Map<ParameterExpression, Object> params;
    private CriteriaBuilder builder;
    private Root root;

    public ParamsProcessor(CriteriaBuilder builder, Root root) {
        this.builder = builder;
        this.root = root;
        params = new HashMap<>();
        predicates = new ArrayList<>();
    }

    public void processParams(T params) {
        List<Field> fieldsForQuery = FieldUtils.getFieldsListWithAnnotation(params.getClass(), QueryParam.class);

        for (Field f : fieldsForQuery) {
            ParameterExpression p = builder.parameter(f.getDeclaringClass());
            QueryParam queryParam = f.getDeclaredAnnotation(QueryParam.class);

            Cause cause = queryParam.cause();
            String col = queryParam.column();
            Expression path = root.get(col);
            f.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = FieldUtils.readField(f, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (fieldValue != null) {
                switch (cause) {
                    case EQ:
                        predicates.add(builder.equal(path, p));
                        break;
                    case ILIKE:
                        predicates.add(builder.lessThan(path, p));
                        break;
                    case LESS:
                        predicates.add(builder.lessThan(path, p));
                        break;
                    case GREATER:
                        predicates.add(builder.greaterThan(path, p));
                        break;
                    case LESS_EQ:
                        predicates.add(builder.lessThanOrEqualTo(path, p));
                        break;
                    case MORE_EQ:
                        predicates.add(builder.greaterThanOrEqualTo(path, p));
                }
                this.params.put(p, fieldValue);
            }
        }
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<Predicate> predicates) {
        this.predicates = predicates;
    }

    public Map<ParameterExpression, Object> getParams() {
        return params;
    }

    public void setParams(Map<ParameterExpression, Object> params) {
        this.params = params;
    }
}
