package com.qinglianyun.buildsrc;

import com.qinglianyun.buildsrc.utils.InjectUtils;
import com.qinglianyun.buildsrc.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Loader;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * 将配置信息插入到项目代码中
 * Created by tang_xqing on 2020/8/21.
 */
public class SDKInject {
    private String mPath;
    private SDKConfig mSDKConfig;
    private InjectUtils mInjectUtils;

    /**
     * @param filePath 文件目录
     */
    public SDKInject(String filePath, SDKConfig config) {
        mPath = filePath;
        mSDKConfig = config;
        mInjectUtils = InjectUtils.getInstance();
        try {
            // Classpool 默认情况下只搜索JVM同路径下的class。
            mInjectUtils.appendPath(filePath);
//            String path = "D:\\03_WorkSpace\\TestApplication\\trancelib\\build\\intermediates\\javac\\debug\\compileDebugJavaWithJavac\\classes";
            String path = "D:\\03_WorkSpace\\TestApplication\\annolib\\build\\classes\\java\\main";
            mInjectUtils.appendPath(path);

            mInjectUtils.getClassPool().importPackage("java.lang");
            mInjectUtils.getClassPool().importPackage(mSDKConfig.getPackageName());
            mInjectUtils.getClassPool().importPackage("com.qinglianyun.annolib");

//            mInjectUtils.getClassPool().importPackage(mSDKConfig.getPackageName() + ".db");

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void write(CtClass ctClass, String path) throws CannotCompileException, IOException {
        if (ctClass.isFrozen()) {
            // 判断类是否被冻结。冻结则解冻
            ctClass.defrost();
        }

        // 根据ctclass，将生成的字节码文件保存在自定目录
        ctClass.writeFile(path);

        // 从ClassPool中释放缓存
        ctClass.detach();
    }

    /**
     * 修改指定field、method的值
     */
    public void startInject() {
        try {
            String sdkClassName = mSDKConfig.getSdkClassName();
            System.out.println("className = " + sdkClassName);
            CtClass ctClass = mInjectUtils.gClass(mSDKConfig.getPackageName() + ".db." + mSDKConfig.getSdkClassName());
            CtField field = ctClass.getDeclaredField("DATABASE_NAME");

            // 删除SDK_VERSION 属性字段
            ctClass.removeField(field);

            /**
             * 新增field
             * 方式一： CtField.make(filedDesc, ctClass)
             * 方式二：  CtField nField = new CtField(mInjectUtils.getClassPool().get("java.lang.String"), "SDK_VERSION", ctClass);
             */
            CtField nField = new CtField(mInjectUtils.getClassPool().get("java.lang.String"), "DATABASE_NAME", ctClass);
            nField.setModifiers(Modifier.PRIVATE | Modifier.STATIC);  // 访问范围
            ctClass.addField(nField, CtField.Initializer.constant(mSDKConfig.getSdkVersion()));  // 设置属性初始值

            /*//创建新的SDK_VERSION属性字段
            String filedDesc = "private final static String DATABASE_NAME = \"" + mSDKConfig.getSdkVersion() + "\";";
            ctClass.addField(CtField.make(filedDesc, ctClass));
            */

            /**
             * 新增方法
             * 方式一：CtMethod.make()
             * 方式二：CtMethod method  = new CtMethod(mInjectUtils.getClassPool().get("java.lang.String"),"getDName",new CtClass[]{CtClass.voidType},ctClass);
             */
            /*
            String methodDesc = "public String getDName(){return DATABASE_NAME;}";
            CtMethod make = CtMethod.make(methodDesc, ctClass);
            ctClass.addMethod(make);
            */

            String mBody = "return DATABASE_NAME;";
            CtMethod method = new CtMethod(mInjectUtils.getClassPool().get("java.lang.String"), "getDName", new CtClass[]{CtClass.voidType}, ctClass);
//            method.setModifiers(Modifier.PUBLIC);
            method.setBody(mBody);
            ctClass.addMethod(method);

            // 插入完成后，重新写入文件
            write(ctClass, mPath);

            CtNewMethod.copy(method, method.getName(), ctClass, null);

            CtClass returnType = method.getReturnType();
            if (returnType == CtClass.voidType) {

            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环遍历所有class文件，并插入代码
     */
    public void iteratorPath(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                iteratorPath(file1.getAbsolutePath());
            } else {
                String absolutePath = file1.getAbsolutePath();
                if (!absolutePath.endsWith(".class") || absolutePath.contains("R$")
                        || absolutePath.contains("R.class") || absolutePath.contains("BuildConfig.class")
                        || absolutePath.contains(mSDKConfig.getApplicationName())) {
                    continue;
                }

                // 对指定包名下的文件插入代码
                int index = absolutePath.indexOf(mSDKConfig.getPackagePath());
                if (index != -1) {
                    int end = absolutePath.length() - 6;  // .class
                    String className = absolutePath.substring(index, end)
                            .replace("\\", ".")
                            .replace("/", ".");

                    injectToClass(className);
                }
            }
        }
    }

    private void injectToClass(String className) {
        System.out.println("-------------- class 构造方法插入方法 className =" + className + "---------------");
        try {
            CtClass ctClass = mInjectUtils.gClass(className);
            CtConstructor[] declaredConstructors = ctClass.getConstructors();
            for (CtConstructor ctConstructor : declaredConstructors) {
                String name = ctClass.getName();
                // 插入return之前
                ctConstructor.insertAfter("System.out.println(System.currentTimeMillis()+\"" + name + "\");");
            }

            if (ctClass.isFrozen()) {
                ctClass.defrost();
            }

            ctClass.writeFile(mPath);
            ctClass.detach();

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String pattern = "*Activity";
//        String target = "com.jj.act.jj.github.MainActivity";
//        boolean b = wildcardMatch(pattern, target);
//        System.out.println("测试匹配 " + b);
//
//        b = wildcardMatch("com.**.act.*.github.*Activity", target);
//        System.out.println("\n测试匹配 " + b);
//
//        b = wildcardMatch("com.**.a*t.*.github.*Activity", target);
//        System.out.println("\n测试匹配 " + b);
    }

    /**
     * 循环遍历所有class文件，获取method的注解，然后在方法体插入代码
     */
    public void injectToAnnotation(String path) {
        File[] files = new File(path).listFiles();
        for (File file : files) {
            String absolutePath = file.getAbsolutePath();
            if (file.isDirectory()) {
                injectToAnnotation(file.getAbsolutePath());
            } else {
                if (!absolutePath.endsWith(".class") || absolutePath.contains("BuildConfig.class") || absolutePath.contains("R.class")) {
                    continue;
                }

                // 截取包名，因为类名需要全路径  *javac/ **/ classes/* 获取文件的编译路径
                if (Utils.isEmpty(Utils.getAd2Extension().getSrcClassDir())) {
                    System.out.println("------------- 未获取到class资源集 --------------");
                    return;
                }
                int srcIndex = absolutePath.indexOf(Utils.getAd2Extension().getSrcClassDir());
                if (srcIndex != -1) {
                    String className = absolutePath.substring(srcIndex + Utils.getAd2Extension().getSrcClassDir().length() + 1, absolutePath.length() - 6);
                    className = className.replace("\\", ".").replace("/", ".");
                    iteratorAnnotation(className);
                } else {
                    System.out.println("------------- 未获取到class --------------");
                }
            }
        }
    }

    private void iteratorAnnotation(String className) {
        System.out.println("--------------  className =" + className + "---------------");
        try {
            CtClass ctClass = mInjectUtils.gClass(className);

            CtMethod[] methods = ctClass.getDeclaredMethods();
            for (CtMethod method : methods) {
                String annotationClass = Utils.getAd2Extension().getAnnotationClass();
                if (Utils.isEmpty(Utils.getAd2Extension().getAnnotationClass())) {
                    System.out.println("------------- 未设置注解类 --------------");
                    return;
                }

                /*  // 通过获取方法的methodInfo()信息，还是找不到指定的注解
                ConstPool constPool = method.getMethodInfo().getConstPool();
                AnnotationsAttribute attribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                Annotation[] annotations = attribute.getAnnotations();
                System.out.println("-----------  注解 length = " + annotations.length);
                for (Annotation annotation : annotations) {
                    String value = ((StringMemberValue) annotation.getMemberValue("unitName = ")).getValue();
                    System.out.println("-----------  注解名 " + value);
                }*/

                /*
                String classesPath = "D:\\03_WorkSpace\\TestApplication\\annolib\\build\\libs\\annolib.jar";
                URL[] urls = new URL[1];
                urls[0] = new URL("file", null, classesPath);
                URLClassLoader loader = new URLClassLoader(urls);     // 加载指定路径下的jar/aar/dex文件
                Class<?> aClass1 = loader.loadClass("com.qinglianyun.annolib.TraceLog");
                Object annotation = method.getAnnotation(aClass1);  // 问题：就是获取不到指定的注解类！！！
                System.out.println("-----------  注解 name = " + aClass1.getName());
                if (null != annotation) {
                    System.out.println("-----------  注解不为空 ");
                } else {
                    System.out.println("-----------  注解为空 ");
                }
                */

                // 为什么ClassPool能找到class，toClass()却失败提示ClassNotFoundException()
//                Object annotation = method.getAnnotation(Class.forName("com.qinglianyun.tracelib.TraceLog",false,ctClass.getClassPool().getClassLoader()));
//                Loader loader = new Loader();
//                Class<?> aClass = loader.loadClass("com.qinglianyun.annolib.TraceLog");

                /**
                 * 判断是否是指定注解（通过Annotation.getTypeName去判断）
                 *
                 */
                MethodInfo methodInfo = method.getMethodInfo();
                AnnotationsAttribute ainfo = (AnnotationsAttribute) methodInfo.getAttribute(AnnotationsAttribute.invisibleTag);
//                Annotation annotation1 = ainfo.getAnnotation("com.qinglianyun.annolib.TraceLog");   // 不太清楚，为什么这样出现空指针异常
//                System.out.println("-----------  注解名 指定 " + annotation1.getTypeName());
                if (null != ainfo) {
                    Annotation[] annotations = ainfo.getAnnotations();
                    for (Annotation annotation : annotations) {
                        String typeName = annotation.getTypeName();  // 注解名称
                        if (annotationClass.equals(typeName)) {
                            CtClass ctClass1 = mInjectUtils.gClass(typeName);
                            int code = ((IntegerMemberValue) annotation.getMemberValue("code")).getValue();
                            String msg = ((StringMemberValue) annotation.getMemberValue("msg")).getValue();
                            System.out.println("-----------  注解名 " + annotation.getTypeName());
                            System.out.println("-----------    |____ code = " + code + "  msg = " + msg);

                            // 插入指定代码
//                            System.out.println(className + "  call with " + method.getName() + "() code = " + code + " msg = " + msg);
//                            String src = "System.out.println(System.currentTimeMillis()+\"  " + className + "   " + method.getName() + "\");";
                            String src = "System.out.println(\"" + className + "  call with " + method.getName() + "() code = " + code + " msg = " + msg + "\");";
                            method.insertBefore(src);
                        }
                    }
                }

                // 热更新： classloader、native， instat run
                Object[] annotations2 = method.getAnnotations();  // 获取所有的注解

            }

            // 所有内部类（包含匿名内部类、内部类）
//            injectNestClass(ctClass);

            // 获取类实现的接口，获取而不是内部类
            /*      CtClass[] interfaces = ctClass.getInterfaces();
            for (CtClass anInterface : interfaces) {
                System.out.println("--   -----------       |_____接口  " + anInterface.getName() + " --------------");
                CtMethod[] declaredMethods = anInterface.getMethods();
                for (CtMethod method : declaredMethods) {
                    if (!method.isEmpty()) {

                        System.out.println("-------------         |_____方法 " + method.getName() + " --------------");
                        String src = "System.out.println(System.currentTimeMillis()+\" 接口 " + anInterface.getName() + "   " + method.getName() + "\");";
                        method.insertAfter(src);

                        *//**
             * 获取类实现了某个接口，需要在调用接口方法的地方插入指定代码。
             * 实现思路：获取class类实现的接口，然后获取方法。
             * 判断方法名是否为指定的方法名？是，则一行一行读取class类找到实现方法的地方，插入指定代码
             *
             *
             *  存在的问题：可以得到实现的接口，但是无法得到实现的方法。
             *
             *//*
                    }
                }
            }
*/
            write(ctClass, mPath);

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void injectNestClass(CtClass ctClass) throws NotFoundException, CannotCompileException, IOException {
        CtClass[] nestedClasses = ctClass.getNestedClasses();
        for (CtClass nestedClass : nestedClasses) {
            System.out.println("-------------       |_____内部类  " + nestedClass.getName() + " --------------");
            CtMethod[] declaredMethods = nestedClass.getDeclaredMethods();
            for (CtMethod method : declaredMethods) {
                if (!method.isEmpty()) {
                    System.out.println("-------------         |_____方法 " + method.getName() + " --------------");
                    String src = "System.out.println(System.currentTimeMillis()+\"   " + nestedClass.getName() + "   " + method.getName() + "\");";
                    method.insertBefore(src);
                }
            }
            write(nestedClass, mPath);
        }
    }
}
