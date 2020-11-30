package com.qinglianyun.buildsrc.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Created by tang_xqing on 2020/8/24.
 */
public class InjectUtils {
    private ClassPool classPool;

    private static InjectUtils mInject;

    private InjectUtils() {
        classPool = ClassPool.getDefault();
    }

    public static InjectUtils getInstance() {
        if (null == mInject) {
            mInject = new InjectUtils();
        }
        return mInject;
    }

    public ClassPool getClassPool() {
        return classPool;
    }

    public void appendPath(String path) throws NotFoundException {
        classPool.appendClassPath(path);
    }

    public CtClass gClass(String className) throws NotFoundException {
        return classPool.getCtClass(className);
    }

    public CtField gFileld(CtClass ctClass, String fieldName) throws NotFoundException {
        return ctClass.getField(fieldName);
    }

    public CtMethod gMethod(CtClass ctClass, String methodName, String desc) throws NotFoundException {
        return ctClass.getMethod(methodName, desc);
    }
}
