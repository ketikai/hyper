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

package pres.ketikai.hyper.gradle.util.report.dependency.task

import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
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
import pres.ketikai.hyper.common.util.JSONUtils
import pres.ketikai.hyper.gradle.util.report.dependency.DependencyScope
import java.io.File
import java.nio.charset.Charset
import java.util.*

/**
 * <p>依赖报告任务</p>
 * 提取项目中的依赖信息汇总并解析成 json 字符串<br>
 * 生成的报告是被版本冲突仲裁自动处理过的<br>
 *
 * <p>Created on 2022-12-24 00:40</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.5
 */
abstract class DependencyReport : DefaultTask() {

    private val defaultReportFileName = "dependency-report.json"
    private val defaultReportFile = File("${project.buildDir}/resources/main", defaultReportFileName)

    private val objectFactory = project.objects

    @Internal
    val scope: Property<DependencyScope> = objectFactory.property(DependencyScope::class.java)

    @Internal
    val dependencies: MutableMap<String, Artifact> = linkedMapOf()

    @Internal
    val visitChildren: Property<Boolean> = objectFactory.property(Boolean::class.java)

    @Internal
    val reserveSnapshotVersion: Property<Boolean> = objectFactory.property(Boolean::class.java)

    @Internal
    val excludes: ListProperty<String> = objectFactory.listProperty(String::class.java)

    @Internal
    val encoding: Property<Charset> = objectFactory.property(Charset::class.java)

    init {
        super.setGroup("hyper")
        super.setDescription("为项目生成 Json 格式的依赖报告")

        scope.set(DependencyScope.RUNTIME)
        visitChildren.set(false)
        reserveSnapshotVersion.set(false)
        encoding.set(Charsets.UTF_8)
    }

    @TaskAction
    fun execute() {
        dependencies.clear()
        val scope = scope.get()
        generateReport(calculateConfigurationDetails(), scope)
        dependencies.keys.forEach(::println)
        println("\nscope: ${scope.fullName}  amount: ${dependencies.size}")
        writeReport()
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

    private fun generateReport(configurationDetails: List<ConfigurationDetails>, scope: DependencyScope) {
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

        if (isRenderableDependencyResult && visitChildren.get()) {
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

        if (!dependencies.containsKey(key)) {
            var artifact: Artifact = DefaultArtifact(id)
            if (current.isSnapshot() && artifact.isSnapshot && reserveSnapshotVersion.get()) {
                val version = artifact.version
                artifact = artifact.setVersion(
                    "${current.baseVersion.substring(0, version.length - 8)}${current.version}"
                )
            }

            dependencies[key] = artifact
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
        val excludes = excludes.get()
        for (regex in excludes) {
            if (Regex(regex).matches(id)) {
                return true
            }
        }
        return false
    }

    private fun writeReport() {
        val report = defaultReportFile
        report.parentFile.mkdirs()
        if (report.exists() || report.createNewFile()) {
            try {
                JSONUtils.toFormatJson(dependencies)?.let { report.writeText(it, encoding.get()) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private class Dependency(val group: String, val name: String, var version: String) {

        val baseVersion: String = version

        fun isSnapshot() = version != baseVersion
    }
}