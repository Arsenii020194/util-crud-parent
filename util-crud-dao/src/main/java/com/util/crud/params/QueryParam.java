package com.util.crud.params;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.util.crud.params.Cause.EQ;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryParam {
    String column();

    Cause cause() default EQ;
}
