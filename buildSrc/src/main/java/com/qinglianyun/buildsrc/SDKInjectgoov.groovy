package com.qinglianyun.buildsrc

import com.qinglianyun.buildsrc.utils.InjectUtils
import javassist.CannotCompileException;
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.NotFoundException

public class SDKInjectgoov {

    private String mPath;
    private SDKConfig mSDKConfig;
    private InjectUtils mInjectUtils;

    SDKInjectgoov(String mPath, SDKConfig mSDKConfig) {
        this.mPath = mPath
        this.mSDKConfig = mSDKConfig
        mInjectUtils = InjectUtils.getInstance();
        try {
            // Classpool 默认情况下只搜索JVM同路径下的class。
            mInjectUtils.appendPath(filePath);
            mInjectUtils.getClassPool().importPackage("java.lang");
            mInjectUtils.getClassPool().importPackage(mSDKConfig.getPackageName());

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

    public void dt(String path) {
        File dir = new File(path);
        dir.eachFileRecurse { File file ->
            String absolutePath = file1.getAbsolutePath()
            if (absolutePath.endsWith(".class") && !absolutePath.contains("R.class")
                    && !absolutePath.contains("BuildConfig.class")
                    && !absolutePath.contains(mSDKConfig.getApplicationName())) {

                // 对指定包名下的文件插入代码
                int index = absolutePath.indexOf(mSDKConfig.getPackagePath())
                System.out.println("-------------- class 构造方法插入方法 index=" + index + "---------------")
                if (index != -1) {
                    int end = absolutePath.length() - 6;  // .class
                    String className = absolutePath.substring(index, end)
                            .replace("\\", ".")
                            .replace("/", ".")
                    injectToClass(className)
                }
            }
        }
    }

}