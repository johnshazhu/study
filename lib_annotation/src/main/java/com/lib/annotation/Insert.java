package com.lib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Insert {
    Class target() default EmptyInsertClass.class;
    String classPath() default "";
    String name() default "";
    String addCatch() default "";
    boolean replace() default false;
    boolean before() default false;
    boolean after() default true;
}
