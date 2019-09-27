package com.yida.cloud.pligin.restools

import com.yida.cloud.pligin.restools.folder.*
import com.yida.cloud.pligin.restools.values.ValuesReplace
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created on 2019/9/5
 *
 * @author: gongziyi* Description:
 */
class ReplaceResName extends DefaultTask {


    ReplaceResName() {
        group = "powerUploadPlugin"
    }

    @TaskAction
    void replace() {
        if (!project.android) {
            throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!')
        }

        if (project.resConfig == null) {       // check config
            throw new IllegalArgumentException(
                    'ResTools gradle plugin "resConfig DSL" config can not be null.')
        }

        // === System default
        String sourceFolder = project.android.sourceSets.main.java.srcDirs[0].getAbsolutePath()
        String resFolder =    project.android.sourceSets.main.res.srcDirs[0].getAbsolutePath()
        String manifestFilePath = project.android.sourceSets.main.manifest.srcFile.getAbsolutePath()

        long startTime = System.currentTimeMillis()     // startTime

        // === User settings
        def config = project.resConfig
        if (config.new_prefix == null || config.new_prefix.trim().length() == 0) {
            throw new IllegalArgumentException(
                    'the [new_prefix] can not be null (必须配置新的前缀)')
        }
        if (config.srcFolderPath != null && config.srcFolderPath.trim().length() > 0) {
            sourceFolder = config.srcFolderPath
        }
        if (config.resFolderPath != null && config.resFolderPath.trim().length() > 0) {
            resFolder = config.resFolderPath
        }
        if (config.manifestFilePath != null && config.manifestFilePath.trim().length() > 0) {
            manifestFilePath = config.manifestFilePath
        }

        // === print all settings

        println(">>>>>> old_prefix: ${config.old_prefix}")
        println(">>>>>> new_prefix: ${config.new_prefix}")
        println(">>>>>> srcFolder : ${sourceFolder}")
        println(">>>>>> resFolder : ${resFolder}")
        println(">>>>>> AndroidManifest.xml file path : ${manifestFilePath}")

        // === do work
        println "++++++++++++++++++++++ Start replace Android resources..."

        ResToolsConfiguration workConfig = new ResToolsConfiguration(
                config.new_prefix,
                config.old_prefix,
                sourceFolder,
                resFolder,
                manifestFilePath
        )
        doWork(workConfig)

        println("++++++++++++++++++++++ Finish replace resouces name, Total time: ${(System.currentTimeMillis() - startTime) / 1000} ")
    }


    private void doWork(ResToolsConfiguration config) throws IOException {
        // 1. layout
        def layoutReplace = new LayoutReplace(config)
        layoutReplace.replaceThis()

        // 2. drawable
        def drawableReplace = new DrawableReplace(config)
        drawableReplace.replaceThis()

        // 3.  color
        def colorReplace = new ColorReplace(config)
        colorReplace.replaceThis()


        // 4.  Anim
        def anim = new AnimReplace(config)
        anim.replaceThis()

        // 5.  menu
        def menuReplace = new MenuReplace(config)
        menuReplace.replaceThis()

        // 6.  mipmap
        def mipmapReplace = new MipmapReplace(config)
        mipmapReplace.replaceThis()

        // 7. raw
        def rawReplace = new RawReplace(config)
        rawReplace.replaceThis()

        // 8. xml
        def xmlReplace = new XmlReplace(config)
        xmlReplace.replaceThis()

        ////////////// all values  types //////////////////
        // === 9. values test not support attrs
        def valuesReplace = new ValuesReplace(config)
        valuesReplace.replaceValues(ValuesReplace.ALL_VALUES_TYPES)
    }
}
