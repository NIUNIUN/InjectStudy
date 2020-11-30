package com.qinglianyun.buildsrc;


import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.qinglianyun.buildsrc.utils.JarZipUtil;
import com.qinglianyun.buildsrc.utils.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Transform用来在class到dex阶段，修改class文件的API。
 * 修改字节码文件的框架：javassist,ASM
 * <p>
 * class编译dex之前，proguard操作之前。（考虑到混淆问题）
 * <p>
 * Created by tang_xqing on 2020/8/21.
 */
public class MyTrans extends Transform {

    private Project mProject;
    private SDKConfig mConfig;

    public MyTrans(Project mProject) {
        this.mProject = mProject;
    }

    private void importProperties() {
        if (null != mConfig) {
            return;
        }
        String propertiesPath = Utils.getAd2Extension().getPropertiesPath();
        System.out.println("-----------  propertiesPath " + propertiesPath + " -----------");
//        File file = mProject.getRootProject().file("TransformConfig/qly_security_scan_2.properties");
        File file = new File(propertiesPath);

        DataInputStream dis = null;
        Properties properties = null;
        try {
            dis = new DataInputStream(new FileInputStream(file));
            properties = new Properties();
            properties.load(dis);
            mConfig = new SDKConfig(properties);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != dis) {
                    dis.close();
                    properties.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * transform 的名字
     * <p>
     * transformClassesWithDexBuilderForDebug ( class转换到dex任务 )
     * transform + getInputTypes() + With + getName() +debug/release
     * <p>
     * 最后生成 transformClassesWithMyTranssForDebug
     *
     * @return
     */
    @Override
    public String getName() {
        return "MyTranss";
    }

    /**
     * 处理数据的类型，最终生成的trans任务不同
     * class --java的class
     * resource --java的源码
     *
     * @return
     */
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    /**
     * 要操作内容的作用域，官网提供7中类型
     * <p>
     * 一般是全部class文件
     *
     * @return
     */
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY;
    }

    /**
     * 是否开启增量编译
     * 开启增量编译，input包含change、add、remove、nochange
     *
     * @return
     */
    @Override
    public boolean isIncremental() {
        return false;
    }

    /**
     * Task中有input、output。
     * 一个任务的input是上个任务的output。
     *
     * @param transformInvocation
     * @throws TransformException
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        importProperties();
//        transformInvocation.getInputs();   // 当前transform的输出作为下个transform的输入

        //输出流：class文件的输出目录。将修改的文件，保存在指定目录
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        // 输入流:java文件的保存目录。两种形式：jar和文件目录。需要分开遍历
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        Iterator<TransformInput> iterator = inputs.iterator();
        while (iterator.hasNext()) {
            TransformInput next = iterator.next();

            /************** 遍历java文件目录 **************/
            iteratorDirector(next.getDirectoryInputs(), outputProvider);

            /************** 遍历jar目录 **************/
            iteratorJar(next.getJarInputs(), outputProvider);
        }
        super.transform(transformInvocation);
    }


    /**
     * 循环遍历文件目录
     *
     * @param directoryInputs
     * @param outputProvider
     */
    private void iteratorDirector(Collection<DirectoryInput> directoryInputs, TransformOutputProvider outputProvider) throws IOException {
        /************** 遍历java文件目录 **************/
        Iterator<DirectoryInput> directIter = directoryInputs.iterator();
        while (directIter.hasNext()) {
            DirectoryInput input = directIter.next();

            System.out.println("\n****************  interator director start  ********************");
            System.out.println(input.getFile().getAbsoluteFile());
            // TODO: 2020/8/24 在这里对class文件进行修改，例如字节码插桩
            SDKInject sdkInject = new SDKInject(input.getFile().getAbsolutePath(), mConfig);
//            sdkInject.startInject();
//            sdkInject.iteratorPath(input.getFile().getAbsolutePath());
            sdkInject.injectToAnnotation(input.getFile().getAbsolutePath());

//            SDKInjectToActivity sdkInject = new SDKInjectToActivity(mProject, input.getFile().getAbsolutePath(), mConfig);
//            sdkInject.iteratorClass(input.getFile().getAbsolutePath());
//            sdkInject.injectToAnnotation(input.getFile().getAbsolutePath());

            // 获取output文件目录
            File dest = outputProvider.getContentLocation(input.getName(), input.getContentTypes(), input.getScopes(), Format.DIRECTORY);

            // input文件修改完成后，复制给output

            FileUtils.copyDirectory(input.getFile(), dest);
        }
    }

    /**
     * 循环遍历jar
     *
     * @param jarInputs
     * @param outputProvider
     */
    private void iteratorJar(Collection<JarInput> jarInputs, TransformOutputProvider outputProvider) throws IOException {
        Iterator<JarInput> iterator = jarInputs.iterator();
        while (iterator.hasNext()) {
            JarInput input = iterator.next();

            File jarFile = input.getFile();
            System.out.println("--------- jar name = " + jarFile.getName());

            // 这里遍历循环jar文件，只对其重命名
            String jarName = input.getName();
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4);
                List<String> list = JarZipUtil.unzipJar(jarFile.getAbsolutePath(), jarName);
                for (String className : list) {
                    // 循环遍历jar中所有class文件
                }
            }

            String md5File = DigestUtils.md5Hex(jarFile.getAbsolutePath());
            File dest = outputProvider.getContentLocation(jarName + md5File, input.getContentTypes(), input.getScopes(), Format.JAR);

            FileUtils.copyFile(jarFile, dest);
        }
    }
}
