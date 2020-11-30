package com.qinglianyun.buildsrc.utils;

import org.gradle.internal.impldep.com.esotericsoftware.kryo.io.Input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * 对jar文件进行遍历
 * Created by tang_xqing on 2020/8/24.
 */
public class JarZipUtil {

    /**
     * 解压Jar包
     */
    public static List<String> unzipJar(String path, String destDirPath) throws IOException {
        List list = new ArrayList();
        if (path.endsWith(".jar")) {
            JarFile jarFile = new JarFile(path);
            Enumeration<JarEntry> entries = jarFile.entries();

            //  循环遍历解压后的文件
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.isDirectory()) {
                    continue;
                }

                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    String className = entryName.replace("\\", ".")
                            .replace("/", ".");
                    list.add(className);
                }
                // 将压缩包中的文件重写在指定目录，实现解压
                String outFileName = destDirPath + "/" + entryName;
                File outFile = new File(outFileName);
                outFile.getParentFile().mkdir();
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                FileOutputStream fileOutputStream = new FileOutputStream(outFile);
                byte[] line = new byte[1024];
                while (inputStream.read(line) != -1) {
                    fileOutputStream.write(line);
                }

                fileOutputStream.close();
                inputStream.close();
            }
            jarFile.close();
        }
        return list;
    }

    /**
     * 打包成Jar
     *
     * @param packagePath
     * @param path
     */
    public static void zipJar(String packagePath, String path) throws IOException {
        File file = new File(packagePath);
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(file));
        File[] files = file.listFiles();
        for (File file1 : files) {
            String entryName = file1.getAbsolutePath().substring(packagePath.length() + 1);
            // 创建了一个压缩文件的子项，并往里面写入数据
            outputStream.putNextEntry(new ZipEntry(entryName));
            if (!file1.isDirectory()) {
                InputStream inputStream = new FileInputStream(file1);
                byte[] bytes = new byte[1024];
                while (inputStream.read(bytes) != -1) {
                    outputStream.write(bytes);
                }

                inputStream.close();
            }
            outputStream.close();
        }
    }


}
