package com.qinglianyun.buildsrc;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension;
import com.qinglianyun.buildsrc.utils.Utils;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MyPlugg implements Plugin<Project> {
    @Override
    public void apply(Project o) {
        System.out.println("-----------  plugin start -----------");

//        addTask(o);

        // 判断是否是app
        boolean appPluginFlag = o.getPlugins().hasPlugin(AppPlugin.class);
//        if (!appPluginFlag) {
//            throw new GradleException("must be applicationplugin");
//        }

        // 自定义扩展函数
        final Ad2 addExt = addExtendion(o);

        Utils.setProject(o);

        MyTrans myTrans = new MyTrans(o);
        BaseExtension android = o.getExtensions().getByType(BaseExtension.class);
        // 注册Transform
        if (android instanceof LibraryExtension || android instanceof AppExtension) {
            android.registerTransform(myTrans);
        }

        /**
         * 不能直接获取到build.graldew中设扩展函数属性值（因为代码顺序执行），所以要在module加载插件后执行
         */
        o.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                System.out.println("-----------  extendion " + addExt.toString() + " -----------");
            }
        });

//        getTaskByName(o, "assemble");


       /* try {
            Properties properties = loadProjectFile(o, "TransformConfig\\qly_security_scan.properties");
            SDKConfig sdkConfig = new SDKConfig(properties);
            String sdkVersion = sdkConfig.getSdkVersion();
            System.out.println("test properties get sdkversion = " + sdkVersion);

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("-----------  plugin end -----------");
    }

    /**
     * 根据task名称获取对象。
     * 不知道task名称，可以通过getAllTasks()获得task名
     *
     * @param project
     */
    private void getTaskByName(Project project, String taskName) {
//        Map<Project, Set<Task>> allTasks = project.getAllTasks(true);
//        Iterator<Map.Entry<Project, Set<Task>>> iterator = allTasks.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<Project, Set<Task>> next = iterator.next();
//            Iterator<Task> iterator1 = next.getValue().iterator();
//
//            while (iterator1.hasNext()) {
//                Task next1 = iterator1.next();
//                System.out.println(" name = " + next1.getName() +" path = "+ next1.getPath());
//            }
//        }

        project.getTasks().getByName(taskName).doLast(
                task -> System.out.println(" compileTestJava do finish!!! "));
    }

    /**
     * 增加自定义扩展函数
     *
     * @param project
     * @return
     */
    private Ad2 addExtendion(Project project) {
        // 第一个参数：扩展函数名；第二个参数：将扩展函数映射到Ad2.class类上。build.gradle可以设置Ad2.class中的各个属性
        return project.getExtensions().create("addExt", Ad2.class);
    }

    /**
     * 增加任务
     *
     * @param project
     */
    private void addTask(Project project) {
        project.getTasks().create("myTaskk", MyTasks.class, new Action<MyTasks>() {
            @Override
            public void execute(MyTasks myTasks) {
                // 这里可以相当于task被加载时，做一些初始化的操作。

            }
        });
    }

    /**
     * 加载指定文件路径
     *
     * @param project
     * @throws IOException
     */
    private Properties loadProjectFile(Project project, String filePath) throws IOException {
        Properties properties = new Properties();
        File file = project.getRootProject().file(filePath);
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        properties.load(dis);

        return properties;
    }

    /**
     * 将配置信息插入到项目代码中
     *
     * @param project
     * @param sdkConfig
     */
    private void iinjectCode(Project project, SDKConfig sdkConfig) {
        String sdkVersion = sdkConfig.getSdkVersion();

    }
}

/**
 * 自定义插件最终生成其实是一个jar包
 * <p>
 * 自定义插件三种方法：
 * 方法一：在project中创建java library类型的module，module名必须为buildSrc。
 * <p>
 * <p>
 * 引用自定义插件两种方法：
 * 方法一：在build.gradle中，直接使用 (apply plugin MyPlugging)
 * <p>
 * 方法二：配置META_INFO，在build.gradle中引用配置文件名称 (apply plugin "mygradleeee")
 * 配置meta-info方式：resources/META-INF/gradle-plugins/xxxx.properties，在xxx.properties中添加implementation-class=自定义插件类名
 */
