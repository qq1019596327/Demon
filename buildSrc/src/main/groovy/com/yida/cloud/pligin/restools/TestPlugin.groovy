package com.yida.cloud.pligin.restools

import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        /**
         * 创建拓展,这里创建拓展之后,可在引用的project中的gradle对其中的参数进行赋值
         */
        project.extensions.create('extConfig', ResToolsConfiguration.class)
        /**
         * 创建tasks任务
         * 任务类需要继承DefaultTask类并实现并标记@TaskAction方法
         */
        project.tasks.create("replaceResName",ReplaceResName.class)
    }
}