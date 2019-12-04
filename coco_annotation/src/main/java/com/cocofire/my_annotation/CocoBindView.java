package com.cocofire.my_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.IdRes;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CocoBindView {
    @IdRes int value();
}
