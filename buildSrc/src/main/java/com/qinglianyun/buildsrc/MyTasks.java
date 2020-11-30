package com.qinglianyun.buildsrc;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

/**
 * 自定义task
 *   任务中的属性？
 *
 * Created by tang_xqing on 2020/8/21.
 */
public class MyTasks extends DefaultTask {

    /**
     * Input注解，gradle会自定生成get、set方法
     */
    @Input
    String taskIDD;

    @Input
    String taskName;

    public MyTasks() {

    }

    /**
     * TaskAction注解，表明执行任务时所执行的方法
     */
    @TaskAction
    void start() {

    }

}
