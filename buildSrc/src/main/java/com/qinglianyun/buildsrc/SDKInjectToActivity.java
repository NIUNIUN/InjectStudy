package com.qinglianyun.buildsrc;

import com.qinglianyun.buildsrc.utils.InjectUtils;
import com.qinglianyun.buildsrc.utils.Utils;


import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * 将配置信息插入到项目代码中
 * Created by tang_xqing on 2020/8/21.
 */
public class SDKInjectToActivity {
    private String mPath;
    private SDKConfig mSDKConfig;
    private InjectUtils mInjectUtils;

    /**
     * @param filePath 文件目录
     */
    public SDKInjectToActivity(Project project, String filePath, SDKConfig config) {
        mPath = filePath;
        mSDKConfig = config;
        mInjectUtils = InjectUtils.getInstance();
        try {
            // Classpool 默认情况下只搜索JVM同路径下的class。所以将当前路径添加到ClassPool中
            mInjectUtils.appendPath(filePath);

            String path = "D:\\03_WorkSpace\\TestApplication\\trancelib\\build\\intermediates\\javac\\debug\\compileDebugJavaWithJavac\\classes";
            mInjectUtils.appendPath(path);

//            String annoPath = "D:/03_WorkSpace/TestApplication/annolib/build/libs/annolib.jar";
//            String annoPath = "D:\\03_WorkSpace\\TestApplication\\annolib\\build\\classes\\java\\main";
//            mInjectUtils.appendPath(annoPath);

//            "${System.getenv('ANDROID_SDK_HOME')}/platforms/android-26/android.jar"
            //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
//            mInjectUtils.appendPath(project.android.bootClasspath[0].toString());
            mInjectUtils.appendPath(System.getenv("ANDROID_SDK_HOME") + "/platforms/android-26/android.jar");

            mInjectUtils.getClassPool().importPackage("java.lang");

            //引入android.os.Bundle包，因为onCreate方法参数有Bundle
            mInjectUtils.getClassPool().importPackage("android.os.Bundle");
            mInjectUtils.getClassPool().importPackage("android.support.annotation");


            mInjectUtils.getClassPool().importPackage(mSDKConfig.getPackageName());
            mInjectUtils.getClassPool().importPackage("com.qinglianyun.tracelib");

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
     * 循环遍历所有class文件，在Activity生命周期方法中插入代码
     *
     * @param path
     */
    public void iteratorClass(String path) {
        File[] files = new File(path).listFiles();
        for (File file : files) {
            String absolutePath = file.getAbsolutePath();
            if (file.isDirectory()) {
                iteratorClass(file.getAbsolutePath());
            } else {
                if (!absolutePath.endsWith(".class") || absolutePath.equals("BuildConfig.class")
                        || absolutePath.equals("R.class") || absolutePath.contains("R$")) {
                    continue;
                }
                String srcClassDir = mSDKConfig.getPackagePath();
                int index = absolutePath.indexOf(srcClassDir);
                if (index != -1) {
                    String className = absolutePath.substring(index, absolutePath.length() - 6);

                    className = className.replace("\\", ".")
                            .replace("/", ",");
                    injectToActivity(className);
                }
            }
        }
    }

    private void injectToActivity(String className) {
        try {
            if (!className.contains("Activity")) {
                return;
            }
            System.out.println("--------------  className =" + className + "---------------");

            CtClass ctClass = mInjectUtils.gClass(className);
            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
            for (CtMethod declaredMethod : declaredMethods) {
                String name = declaredMethod.getName();
                if (name.equals("onCreate") || name.equals("onDestory")) {
                    System.out.println("--------------    |_______ method =" + name + "---------------");
                    // 插入代码
                    String str = ctClass.getName() + " call with " + name + "()";
//                    declaredMethod.insertBefore("System.out.println(System.currentTimeMillis()+\"" + name + "\");");
                    declaredMethod.insertBefore("TracePrint.print(\"" + str + "\");");
                }
            }
            write(ctClass, mPath);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            CtClass ctClass = mInjectUtils.gClass(className);

            CtMethod[] methods = ctClass.getDeclaredMethods();
            for (CtMethod method : methods) {
                String annotationClass = Utils.getAd2Extension().getAnnotationClass();
                if (Utils.isEmpty(Utils.getAd2Extension().getAnnotationClass())) {
                    System.out.println("------------- 未设置注解类 --------------");
                    return;
                }

                ConstPool constPool = method.getMethodInfo().getConstPool();
                AnnotationsAttribute attribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.invisibleTag);
                Annotation[] annotations = attribute.getAnnotations();
                if (null == annotations) {
                    System.out.println("------------- 未获取到方法注解 --------------");
                } else {
                    System.out.println("------------- 获取到方法注解 size = " + annotations.length + "--------------");
                    for (Annotation annotation : annotations) {
                        String unitName = ((StringMemberValue) annotation.getMemberValue("unitName")).getValue();
                        System.out.println("--------------  annotation typeName = " + annotation.getTypeName() + "  unitName = " + unitName + "---------------");
                    }
                }


//                Annotation annotation1 = attribute.getAnnotation("com.qinglianyun.tracelib.TraceLog");
//                if(null == annotation1){
//                    System.out.println("--------------  annotationClass = null ---------------");
//                }else{
//                    System.out.println("--------------  annotationClass =" + annotationClass + "---------------");
//                }

/*
                Class<?> aClass = mInjectUtils.getClassPool().getClassLoader().loadClass("com.qinglianyun.tracelib.TraceLog");
//                VisitableURLClassLoader loader = new VisitableURLClassLoader("", ctClass.getClassPool().getClassLoader(), urls);
//                Object annotation = method.getAnnotation(ctClass1.toClass(loader));
                Object annotation = method.getAnnotation(aClass);
//                Object[] annotations = method.getAnnotations();
//                for (Object o : annotations) {
                // TODO: 2020/8/26 暂时没有想到，如何判断指定注解类
                if (null != annotation) {
                    System.out.println("-------------       |________注解" + ctClass.getName() + " method = " + method + " --------------");
                    // 插入代码
                    String src = "System.out.println(System.currentTimeMillis()+\"  " + className + "   " + method.getName() + "\");";
                    if ((method.getModifiers() & Modifier.FINAL) != Modifier.FINAL) {
                        method.insertAfter(src);
                    }
//                }
                }*/
            }

            // 所有内部类（包含匿名内部类、内部类）
//            iteratorNestedClass(ctClass);

            // 获取类实现的接口
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
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iteratorNestedClass(CtClass ctClass) throws NotFoundException, CannotCompileException, IOException {
        // 所有内部类（包含匿名内部类、内部类）
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
