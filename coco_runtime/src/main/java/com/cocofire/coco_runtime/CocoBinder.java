package com.cocofire.coco_runtime;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.VisibleForTesting;

public class CocoBinder {

    @VisibleForTesting
    private static final Map<Class<?>, Constructor<? extends CocoViewFinder>> BINDINGS = new LinkedHashMap<>();

    /**
     * Activity注解绑定 Activity
     */
    @NonNull
    @UiThread
    public static void bind(Activity target) {
        View sourceView = target.getWindow().getDecorView();
        bind(target, sourceView);
    }

    @NonNull
    @UiThread
    public static CocoViewFinder bind(Activity target, View sourceView) {
        Class<?> targetClass = target.getClass();
        Constructor<? extends CocoViewFinder> constructor = findBindingConstructorForClass(targetClass);

        if (constructor == null) {
            return null;
        }

        //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
        try {
            return constructor.newInstance(target, sourceView);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }

    @Nullable
    @CheckResult
    @UiThread
    private static Constructor<? extends CocoViewFinder> findBindingConstructorForClass(Class<?> cls) {
        Constructor<? extends CocoViewFinder> bindingCtor = BINDINGS.get(cls);
        if (bindingCtor != null || BINDINGS.containsKey(cls)) {
            return bindingCtor;
        }
        String clsName = cls.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")
                || clsName.startsWith("androidx.")) {
            return null;
        }
        try {
            Class<?> bindingClass = cls.getClassLoader().loadClass(clsName + "_ViewBinding");
            //noinspection unchecked
            bindingCtor = (Constructor<? extends CocoViewFinder>) bindingClass.getConstructor(cls, View.class);
        } catch (ClassNotFoundException e) {
            bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
        }
        BINDINGS.put(cls, bindingCtor);
        return bindingCtor;
    }
}
