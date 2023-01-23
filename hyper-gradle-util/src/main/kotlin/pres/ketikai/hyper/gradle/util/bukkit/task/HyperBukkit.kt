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

package pres.ketikai.hyper.gradle.util.bukkit.task

import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.result.ResolvedVariantResult
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.diagnostics.internal.ConfigurationDetails
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableDependency
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableDependencyResult
import org.gradle.api.tasks.diagnostics.internal.graph.nodes.RenderableModuleResult
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import pres.ketikai.hyper.gradle.util.bukkit.asm.life.HyperBukkitVisitor
import pres.ketikai.hyper.gradle.util.bukkit.entity.Hyper
import pres.ketikai.hyper.gradle.util.bukkit.entity.PluginYaml
import pres.ketikai.hyper.gradle.util.bukkit.LibraryScope
import pres.ketikai.hyper.gradle.util.bukkit.entity.Libraries
import java.io.File
import java.io.FileNotFoundException
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.*

/**
 * <p>增强 Bukkit 插件任务</p>
 *
 * <p>Created on 2023/1/3 12:18</p>
 * @author ketikai
 * @since 0.0.1
 * @version 0.0.1
 */
abstract class HyperBukkit : DefaultTask() {

    private val objectFactory = project.objects

    @Internal
    val charset: Property<Charset> = objectFactory.property(Charset::class.java)

    private val pluginYaml = PluginYaml(project)

    private val hyper = Hyper(project)

    private val libraries = Libraries(project)

    init {
        super.setGroup("hyper")
        super.setDescription("为基于 hyper 的 bukkit 插件开发提供简化和增强")
        super.dependsOn(project.tasks.named("classes"))

        charset.set(Charsets.UTF_8)
    }

    @TaskAction
    fun execute() {
        generateYamlFile(charset.get())
        enhance()
    }

    private fun generateYamlFile(charset: Charset) {
        val file = pluginYaml.defaultPluginYamlFile

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText(generateYamlStr(), charset)
        } else {
            file.writeText(generateYamlStr(file.readText(charset)), charset)
        }
    }

    private fun generateYamlStr(): String {
        return generateYamlStr(null)
    }

    private fun generateYamlStr(source: String?): String {
        val map = if (source != null) pluginYaml.yaml.load<MutableMap<String, Any>>(source) else mutableMapOf()
        map["name"] = promisePluginName()
        map["main"] = promiseMain()
        map["version"] = promiseVersion()
        map["api-version"] = promiseApiVersion()

        pluginYaml.description.orNull?.apply { map["description"] = this }
        promiseAuthors()?.apply { map["authors"] = this }
        promiseWebsite()?.apply { map["website"] = this }
        pluginYaml.prefix.orNull?.apply { map["prefix"] = this }
        pluginYaml.load.orNull?.apply { map["load"] = this }
        pluginYaml.database.orNull?.apply { map["database"] = this }

        promisePluginNames(pluginYaml.depend)?.apply { map["depend"] = this }
        promisePluginNames(pluginYaml.softDepend)?.apply { map["softDepend"] = this }
        promisePluginNames(pluginYaml.loadBefore)?.apply { map["loadBefore"] = this }
        promiseLibraries()?.apply { map["libraries"] = this }

        return pluginYaml.yaml.dump(map)
    }

    private fun checkPluginName(pluginName: String) {
        if (!Regex("^[a-zA-Z0-9_.-]+$").matches(pluginName)) {
            throw IllegalArgumentException("Illegal plugin name \"$pluginName\".")
        }
    }

    private fun promisePluginName(): String {
        val pluginName = pluginYaml.name.getOrElse(project.name)
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
        val mainClass = pluginYaml.main.get()
        if (!Regex("^(?!org.bukkit.)([a-zA-Z_$][a-zA-Z0-9_$]*.)*[a-zA-Z_$][a-zA-Z0-9_$]*$")
                .matches(mainClass)
        ) {
            throw IllegalArgumentException("Illegal plugin main class \"$mainClass\".")
        }
        return mainClass
    }

    private fun promiseVersion(): String {
        return pluginYaml.version.getOrElse(project.version.toString())
    }

    private fun promiseApiVersion(): String {
        val apiVersion = pluginYaml.apiVersion.get()
        if (!Regex("^[1-9][0-9]*.[0-9.]*[0-9]$").matches(apiVersion)) {
            throw IllegalArgumentException("Illegal api version \"$apiVersion\".")
        }
        return apiVersion
    }

    private fun promiseAuthors(): List<String>? {
        val authors = pluginYaml.authors.orNull
        if (authors.isNullOrEmpty()) {
            return null
        }
        return authors
    }

    private fun promiseWebsite(): String? {
        val website = pluginYaml.website.orNull ?: return null
        try {
            URL(website)
        } catch (e: MalformedURLException) {
            return null
        }
        return website
    }

    private fun promiseLibraries(): List<String>? {
        val libraries: MutableSet<String> = HashSet(pluginYaml.libraries.getOrElse(mutableListOf()))
        libraries.addAll(generateLibraries())
        if (libraries.isEmpty()) {
            return null
        }
        libraries.forEach {
            DefaultArtifact(it)
        }
        return libraries.toList()
    }

    private fun enhance() {
        if (!hyper.moreLifeEvents.get()) {
            return
        }

        val mainClass = pluginYaml.main.get()
        val mainClassFile = File(hyper.baseClassesFile, mainClass.replace('.', '/') + ".class")
        if (!mainClassFile.exists()) {
            throw FileNotFoundException("Plugin's main class \"$mainClass\" does not exists.")
        }

        val bytes = Files.readAllBytes(mainClassFile.toPath())
        val reader = ClassReader(bytes)
        val writer = ClassWriter(reader, ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS)
        reader.accept(HyperBukkitVisitor(Opcodes.ASM9, writer), 0)
        mainClassFile.writeBytes(writer.toByteArray())
    }

    private fun generateLibraries(): List<String> {
        libraries.dependencies.clear()
        generateReport(calculateConfigurationDetails(), libraries.scope.get())
        return libraries.dependencies.values.map {
            var version = it.baseVersion
            if (it.isSnapshot && libraries.reserveSnapshotVersion.get()) {
                version = it.version
            }
            return@map "${it.groupId}:${it.artifactId}:${version}"
        }.toList()
    }

    private fun calculateConfigurationDetails(): List<ConfigurationDetails> {
        val sortedConfigurations = TreeSet(Comparator.comparing(Configuration::getName))
        sortedConfigurations.addAll(project.configurations)

        val configurationDetails = ArrayList<ConfigurationDetails>(sortedConfigurations.size)
        sortedConfigurations.forEach {
            configurationDetails.add(ConfigurationDetails.of(it))
        }

        return configurationDetails
    }

    private fun generateReport(configurationDetails: List<ConfigurationDetails>, scope: LibraryScope) {
        configurationDetails.forEach {
            if (it.isCanBeResolved) {
                val resolvedComponentResult = it.resolutionResultRoot!!.get()

                val renderableModuleResult = RenderableModuleResult(resolvedComponentResult)

                if (renderableModuleResult.allVariants.stream().map(ResolvedVariantResult::getDisplayName)
                        .anyMatch(scope.fullName::equals)
                ) {
                    mapping(renderableModuleResult)
                }
            }
        }
    }

    private fun mapping(dependency: RenderableDependency) {
        val isRenderableDependencyResult = dependency is RenderableDependencyResult
        if (isRenderableDependencyResult) {
            if (!addDependency(dependency)) {
                return
            }
        }

        if (isRenderableDependencyResult && libraries.visitChildren.get()) {
            dependency.children.forEach(this::mapping)
        } else if (!isRenderableDependencyResult) {
            dependency.children.forEach(this::mapping)
        }
    }

    private fun addDependency(dependency: RenderableDependency): Boolean {
        var id = dependency.id.toString()
        var current = parseLocal(id)
        if (current == null) {
            val args = id.split(':')
            val argsSize = args.size
            if (argsSize == 3) {
                current = Dependency(args[0], args[1], args[2])
            } else if (argsSize == 4) {
                current = Dependency(args[0], args[1], args[2])
                current.version = args[3]
            } else if (argsSize < 3) {
                throw IllegalArgumentException(
                    "Invalid id $id. " +
                            "Dependency id must contain at least groupId, artifactId, and version."
                )
            } else {
                throw IllegalArgumentException(
                    "Invalid id $id. " +
                            "This id has unknown fields. Please report the problem to the author (482194973@qq.com)."
                )
            }
        }

        val key = "${current.group}:${current.name}"
        id = "$key:${current.baseVersion}"
        if (isExclude(id)) {
            return false
        }

        if (!libraries.dependencies.containsKey(key)) {
            var artifact: Artifact = DefaultArtifact(id)
            if (current.isSnapshot() && artifact.isSnapshot && libraries.reserveSnapshotVersion.get()) {
                val version = artifact.version
                artifact = artifact.setVersion(
                    "${current.baseVersion.substring(0, version.length - 8)}${current.version}"
                )
            }

            libraries.dependencies[key] = artifact
            return true
        }
        return false
    }

    private fun parseLocal(id: String): Dependency? {
        if (id.startsWith("project :")) {
            val local = project.project(id.removeRange(0, 8))
            return Dependency(local.group.toString(), local.name.toString(), local.version.toString())
        }

        return null
    }

    private fun isExclude(id: String): Boolean {
        val excludes = libraries.excludes.get()
        for (regex in excludes) {
            if (Regex(regex).matches(id)) {
                return true
            }
        }
        return false
    }

    fun pluginYaml(action: Action<PluginYaml>) {
        action.execute(pluginYaml)
    }

    fun hyper(action: Action<Hyper>) {
        action.execute(hyper)
    }

    fun libraries(action: Action<Libraries>) {
        action.execute(libraries)
    }

    private class Dependency(val group: String, val name: String, var version: String) {

        val baseVersion: String = version

        fun isSnapshot() = version != baseVersion
    }
}