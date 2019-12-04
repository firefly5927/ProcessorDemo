package com.cocofire.myprocessor;

import com.cocofire.my_annotation.CocoBindView;
import com.cocofire.my_annotation.CocoClick;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
public class CocoProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Messager mMessager;

    private Map<String, AnnotatedSet> mAnnotatedSetMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //初始化我们需要的基础工具
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
        mAnnotatedSetMap = new TreeMap<>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(CocoBindView.class.getCanonicalName());
        annotations.add(CocoClick.class.getCanonicalName());
        return annotations;
    }

    // 支持的java版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        mAnnotatedSetMap.clear();
        processBindView(roundEnvironment);
        processBindClick(roundEnvironment);
        for (AnnotatedSet annotatedClass : mAnnotatedSetMap.values()) {
            try {
                //输出文件
                annotatedClass.generateFile().writeTo(filer);
            } catch (IOException e) {
//                error(e, "Only classes can be annotated with @%s",
//                        CocoAnnotation.class.getSimpleName());
            }
        }

        return false;
    }

    private void processBindClick(RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(CocoClick.class)) {
            AnnotatedSet annotatedClass = getAnnotatedClass(element);
            CocoClickField clickField = new CocoClickField(element);
            annotatedClass.addMethod(clickField);
        }
    }

    private void processBindView(RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(CocoBindView.class)) {
            AnnotatedSet annotatedClass = getAnnotatedClass(element);
            CocoBindViewField bindViewField = new CocoBindViewField(element);
            annotatedClass.addField(bindViewField);
        }
    }

    /**获取注解所在文件对应的生成类*/
    private AnnotatedSet getAnnotatedClass(Element element) {
        //typeElement表示类或者接口元素
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String fullName = typeElement.getQualifiedName().toString();
        //这里其实就是变相获得了注解的类名（完全限定名称，这里是这么说的）
        AnnotatedSet annotatedSet = mAnnotatedSetMap.get(fullName);
        if (annotatedSet == null) {
            annotatedSet = new AnnotatedSet(typeElement, elementUtils);
            mAnnotatedSetMap.put(fullName, annotatedSet);
        }
        return annotatedSet;
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

}
