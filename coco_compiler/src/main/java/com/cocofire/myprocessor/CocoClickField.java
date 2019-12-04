package com.cocofire.myprocessor;

import com.cocofire.my_annotation.CocoClick;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import static javax.lang.model.element.ElementKind.METHOD;

public class CocoClickField {
    private int[] mResId;
    private String methodName;

    public CocoClickField(Element element) throws IllegalArgumentException {
        if (!(element instanceof ExecutableElement) || element.getKind() != METHOD) {
            throw new IllegalStateException(
                    String.format("@%s annotation must be on a method.", CocoClick.class.getSimpleName()));
        }

        //从目标文件中获取注解标记的变量
        CocoClick bindView = element.getAnnotation(CocoClick.class);
        mResId = bindView.value();

        ExecutableElement executableElement = (ExecutableElement) element;
        methodName = executableElement.getSimpleName().toString();
    }


    int[] getmResId() {
        return mResId;
    }


    String getMethodName() {
        return methodName;
    }

}
