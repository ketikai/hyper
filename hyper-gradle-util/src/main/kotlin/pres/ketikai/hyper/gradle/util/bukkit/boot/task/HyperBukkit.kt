/*
 *    Copyright 2023 ketikai
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pres.ketikai.hyper.gradle.util.bukkit.boot.task

import org.eclipse.aether.artifact.DefaultArtifact
import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.yaml.snakeyaml.Yaml
import pres.ketikai.hyper.gradle.util.bukkit.boot.BukkitLoadType
import pres.ketikai.hyper.gradle.util.bukkit.boot.asm.life.HyperBukkitVisitor
import java.io.File
import java.io.FileNotFoundException
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.nio.file.Files

/**
 * <p>增强 Bukkit 插件任务</p>
 *
 * <p>Created on 2023/1/3 12:18</p>
 * @author ketikai
 * @since 0.0.1
 * @version 0.0.1
 */
abstract class HyperBukkit : DefaultTask() {

    private val defaultPluginYamlFileName = "plugin.yml"
    private val defaultPluginYamlFile = File("${project.buildDir}/resources/main", defaultPluginYamlFileName)
    private val baseClassesFile = File("${project.buildDir}/classes/java/main")

    private val objectFactory = project.objects
    private val yaml = Yaml()

    @Internal
    val hyper: Property<Boolean> = objectFactory.property(Boolean::class.java)

    @Internal
    val name: Property<String> = objectFactory.property(String::class.java)

    @Internal
    val main: Property<String> = objectFactory.property(String::class.java)

    @Internal
    val version: Property<String> = objectFactory.property(String::class.java)

    @Internal
    val apiVersion: Property<String> = objectFactory.property(String::class.java)

    @Internal
    val description: Property<String> = objectFactory.property(String::class.java)

    @Internal
    val authors: ListProperty<String> = objectFactory.listProperty(String::class.java)

    @Internal
    val website: Property<String> = objectFactory.property(String::class.java)

    @Internal
    val prefix: Property<String> = objectFactory.property(String::class.java)

    @Internal
    val load: Property<BukkitLoadType> = objectFactory.property(BukkitLoadType::class.java)

    @Internal
    val database: Property<Boolean> = objectFactory.property(Boolean::class.java)

    @Internal
    val depend: ListProperty<String> = objectFactory.listProperty(String::class.java)

    @Internal
    val softDepend: ListProperty<String> = objectFactory.listProperty(String::class.java)

    @Internal
    val loadBefore: ListProperty<String> = objectFactory.listProperty(String::class.java)

    @Internal
    val libraries: ListProperty<String> = objectFactory.listProperty(String::class.java)

    @Internal
    val charset: Property<Charset> = objectFactory.property(Charset::class.java)

    init {
        super.setGroup("hyper")
        super.setDescription("为基于 hyper 的 bukkit 插件开发提供简化和增强")
        super.setEnabled(false)

        hyper.set(false)
        charset.set(Charsets.UTF_8)
    }

    @TaskAction
    fun execute() {
        generatePluginYamlFile()
        generateHyperPluginMainClass()
    }

    private fun generateHyperPluginMainClass() {
        if (!hyper.get()) {
            return
        }

        val mainClass = main.get()
        val mainClassFile = File(baseClassesFile, mainClass.replace('.', '/') + ".class")
        if (!mainClassFile.exists()) {
            throw FileNotFoundException("Plugin's main class \"$mainClass\" does not exists.")
        }

        val bytes = Files.readAllBytes(mainClassFile.toPath())
        val reader = ClassReader(bytes)
        val writer = ClassWriter(reader, ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS)
        reader.accept(HyperBukkitVisitor(Opcodes.ASM9, writer), 0)
        mainClassFile.writeBytes(writer.toByteArray())
    }

    private fun generatePluginYamlFile() {
        val file = defaultPluginYamlFile

        val encoding = charset.get()
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText(generateYaml(), encoding)
        } else {
            file.writeText(generateYaml(file.readText(encoding)), encoding)
        }
    }

    private fun generateYaml(): String {
        return generateYaml(null)
    }

    private fun generateYaml(source: String?): String {
        val map = if (source != null) yaml.load<MutableMap<String, Any>>(source) else mutableMapOf()
        map["name"] = promisePluginName()
        map["main"] = promiseMain()
        map["version"] = promiseVersion()
        map["api-version"] = promiseApiVersion()

        description.orNull?.apply { map["description"] = this }
        promiseAuthors()?.apply { map["authors"] = this }
        promiseWebsite()?.apply { map["website"] = this }
        prefix.orNull?.apply { map["prefix"] = this }
        load.orNull?.apply { map["load"] = this.name }
        database.orNull?.apply { map["database"] = this }

        promisePluginNames(depend)?.apply { map["depend"] = this }
        promisePluginNames(softDepend)?.apply { map["softDepend"] = this }
        promisePluginNames(loadBefore)?.apply { map["loadBefore"] = this }
        promiseLibraries()?.apply { map["libraries"] = this }

        return yaml.dump(map)
    }

    private fun checkPluginName(pluginName: String) {
        if (!Regex("^[a-zA-Z0-9_.-]+$").matches(pluginName)) {
            throw IllegalArgumentException("Illegal plugin name \"$pluginName\".")
        }
    }

    private fun promisePluginName(): String {
        val pluginName = name.getOrElse(project.name)
        checkPluginName(pluginName)
        return pluginName
    }

    private fun promisePluginNames(pluginNames: ListProperty<String>): List<String>? {
        val list = pluginNames.orNull
        if (list.isNullOrEmpty()) {
            return null
        }
        list.forEach(this::checkPluginName)
        return list
    }

    private fun promiseMain(): String {
        val mainClass = main.get()
        if (!Regex("^(?!org.bukkit.)([a-zA-Z_$][a-zA-Z0-9_$]*.)*[a-zA-Z_$][a-zA-Z0-9_$]*$")
                .matches(mainClass)
        ) {
            throw IllegalArgumentException("Illegal plugin main class \"$mainClass\".")
        }
        return mainClass
    }

    private fun promiseVersion(): String {
        return version.getOrElse(project.version.toString())
    }

    private fun promiseApiVersion(): String {
        val apiVersion = apiVersion.get()
        if (!Regex("^[1-9][0-9]*.[0-9.]*[0-9]$").matches(apiVersion)) {
            throw IllegalArgumentException("Illegal api version \"$apiVersion\".")
        }
        return apiVersion
    }

    private fun promiseAuthors(): List<String>? {
        val authors = authors.orNull
        if (authors.isNullOrEmpty()) {
            return null
        }
        return authors
    }

    private fun promiseWebsite(): String? {
        val website = website.orNull ?: return null
        try {
            URL(website)
        } catch (e: MalformedURLException) {
            return null
        }
        return website
    }

    private fun promiseLibraries(): List<String>? {
        val libraries = libraries.orNull
        if (libraries.isNullOrEmpty()) {
            return null
        }
        libraries.forEach {
            DefaultArtifact(it)
        }
        return libraries
    }
}