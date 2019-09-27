package com.yida.cloud.pligin.restools


import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * zhaoyu1
 */
class ResToolsPlugin implements Plugin<Project> {

    static final String APP = "com.android.application"
    static final String LIBRARY = "com.android.library"

    @Override
    void apply(Project project) {
        if (!(project.plugins.hasPlugin(APP) || project.plugins.hasPlugin(LIBRARY))) {
            throw new IllegalArgumentException(
                    'ResTools gradle plugin can only be applied to android projects.')
        }

        // config
        project.extensions.create('resConfig', ResToolsConfiguration.class)

        // === Create Task
        project.tasks.create("replaceResName",ReplaceResName.class)
    }

}