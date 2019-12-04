package com.cocofire.myprocessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static javax.lang.model.element.Modifier.PUBLIC;

public class AnnotatedSet {
    private static class TypeUtil {
        static final ClassName PROVIDER = ClassName.get("com.cocofire.coco_runtime", "CocoViewFinder");
        private static final ClassName UI_THREAD =
                ClassName.get("androidx.annotation", "UiThread");
        private static final ClassName VIEW = ClassName.get("android.view", "View");
        private static final ClassName Utils = ClassName.get("com.cocofire.coco_runtime", "AnnotatedUtils");

        private static final ClassName OnClick = ClassName.get("com.cocofire.coco_runtime", "CocoOnClickListener");
    }


    /**
     * 类或者接口元素
     */
    private TypeElement mTypeElement;

    /**
     * 绑定的view对象
     */
    private List<CocoBindViewField> mFields;
    /**
     * 绑定的点击事件
     */
    private List<CocoClickField> methods;

    private Elements mElements;

    AnnotatedSet(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
        mFields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    void addMethod(CocoClickField method) {
        methods.add(method);
    }

    void addField(CocoBindViewField field) {
        mFields.add(field);
    }

    JavaFile generateFile() {
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addAnnotation(TypeUtil.UI_THREAD)
                .addModifiers(PUBLIC)
//                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "target")
                .addParameter(TypeUtil.VIEW, "sourceView");
//                .addStatement("this(target, target.getWindow().getDecorView())");
        for (CocoBindViewField field : mFields) {
            // find views
//            constructor.addStatement("target.$N = target.findViewById($L)", field.getFieldName(), field.getResId());

            constructor.addStatement("target.$N = ($T)$T.findRequiredView(sourceView, $L, $S)",
                    field.getFieldName(),
                    ClassName.get(field.getFieldType()),
                    TypeUtil.Utils,
                    field.getResId(), " coco bind ");
        }
        for (CocoClickField method : methods) {
            for (int id : method.getmResId()) {
                String fieldName = "view" + Integer.toHexString(id);
                constructor.addStatement("View $N = $T.findRequiredView(sourceView, $L, $S)",
                        fieldName,
                        TypeUtil.Utils,
                        id,
                        " coco click ");

                TypeSpec.Builder callback = TypeSpec.anonymousClassBuilder("")
                        .superclass(TypeUtil.OnClick);

                MethodSpec.Builder callbackMethod = MethodSpec.methodBuilder("cocoOnClick")
                        .addAnnotation(Override.class)
                        .addParameter(TypeUtil.VIEW, "view")
                        .addModifiers(PUBLIC)
                        .returns(TypeName.VOID);
//                callbackMethod.addParameter(TypeName.);

                CodeBlock.Builder builder = CodeBlock.builder();
                builder.add("target.$L(view);\n", method.getMethodName());
                callbackMethod.addCode(builder.build());

                callback.addMethod(callbackMethod.build());

                constructor.addStatement("$N.setOnClickListener($L)", fieldName, callback.build());
//                        .endControlFlow();
            }

        }

        //generaClass 生成类
        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "_ViewBinding")//类名字
                .addSuperinterface(TypeUtil.PROVIDER)
                .addModifiers(Modifier.PUBLIC)
                //再加入我们的构造方法
                .addMethod(constructor.build())
//                .addMethod(bindViewMethod.build())
                .build();

        String packageName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
        return JavaFile.builder(packageName, injectClass)
                .build();
    }

}
