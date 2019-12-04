package com.cocofire.myprocessor;

import com.cocofire.my_annotation.CocoBindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class CocoBindViewField {
    private VariableElement mVariableElement;
    private int mResId;

    public CocoBindViewField(Element element) throws IllegalArgumentException {
        if (!(element instanceof VariableElement) || element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s",
                    CocoBindView.class.getSimpleName()));
        }
        mVariableElement = (VariableElement) element;
        //从目标文件中获取注解标记的变量
        CocoBindView bindView = element.getAnnotation(CocoBindView.class);
        mResId = bindView.value();
        if (mResId < 0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", CocoBindView.class.getSimpleName(),
                            mVariableElement.getSimpleName()));
        }
    }

    Name getFieldName() {
        return mVariableElement.getSimpleName();
    }

    int getResId() {
        return mResId;
    }

    TypeMirror getFieldType() {
        return mVariableElement.asType();
    }
}
