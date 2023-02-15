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

package pres.ketikai.hyper.gradle.util.configurer.bukkit

import org.gradle.api.Action
import org.gradle.api.Project
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

/**
 * <p>bukkit plugin.yml 配置器</p>
 *
 * <p>Created on 2023/1/23 19:18</p>
 *
 * @author ketikai
 * @since 0.0.1
 * @version 1.0.6
 */
class PluginConfigurer(private val project: Project) {

    private val yaml: Yaml
    private val librariesConfigurer = LibrariesConfigurer(project)

    var name: String? = null
    var main: String? = null
    var version: String? = null
    var apiVersion: String? = null
    var description: String? = null
    val authors: MutableSet<String> = linkedSetOf()
    var website: String? = null
    var prefix: String? = null
    var load: String? = null
    var database: Boolean? = null
    val depend: MutableSet<String> = linkedSetOf()
    val softDepend: MutableSet<String> = linkedSetOf()
    val loadBefore: MutableSet<String> = linkedSetOf()

    init {
        val options = DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.isPrettyFlow = true
        options.indicatorIndent = 2
        options.indent = 4
        yaml = Yaml(options)
    }

    fun generateYamlFile(charset: Charset) {
        val dest = File("${project.buildDir}/resources/main/plugin.yml")

        if (!dest.exists()) {
            dest.parentFile.mkdirs()
            dest.writeText(generateYamlStr(), charset)
        } else {
            dest.writeText(generateYamlStr(dest.readText(charset)), charset)
        }
    }

    private fun generateYamlStr(): String {
        return generateYamlStr(null)
    }

    private fun generateYamlStr(source: String?): String {
        val map = if (source != null) yaml.load<MutableMap<String, Any>>(source) else mutableMapOf()
        map["name"] = promisePluginName()
        map["main"] = promiseMain()
        map["version"] = promiseVersion()
        map["api-version"] = promiseApiVersion()

        description?.apply { map["description"] = this }
        promiseAuthors()?.apply { map["authors"] = this.toList() }
        promiseWebsite()?.apply { map["website"] = this }
        prefix?.apply { map["prefix"] = this }
        load?.apply { map["load"] = this }
        database?.apply { map["database"] = this }

        promisePluginNames(depend)?.apply { map["depend"] = this.toList() }
        promisePluginNames(softDepend)?.apply { map["softDepend"] = this.toList() }
        promisePluginNames(loadBefore)?.apply { map["loadBefore"] = this.toList() }
        promiseLibraries()?.apply { map["libraries"] = this.toList() }

        return yaml.dump(map)
    }

    private fun checkPluginName(pluginName: String) {
        if (!Regex("^[a-zA-Z0-9_.-]+$").matches(pluginName)) {
            throw IllegalArgumentException("Illegal plugin name \"$pluginName\".")
        }
    }

    private fun promisePluginName(): String {
        val pluginName = name ?: project.name
        checkPluginName(pluginName)
        return pluginName
    }

    private fun promisePluginNames(pluginNames: Set<String>): Set<String>? {
        if (pluginNames.isEmpty()) {
            return null
        }
        pluginNames.forEach(this::checkPluginName)
        return pluginNames
    }

    private fun promiseMain(): String {
        val mainClass = main
        if (mainClass.isNullOrBlank() ||
            !Regex(
                "^(?!org.bukkit.)([a-zA-Z_$][a-zA-Z0-9_$]*.)*[a-zA-Z_$][a-zA-Z0-9_$]*$"
            ).matches(mainClass)
        ) {
            throw IllegalArgumentException("Illegal plugin main class \"$mainClass\".")
        }
        return mainClass
    }

    private fun promiseVersion(): String {
        return version ?: project.version.toString()
    }

    private fun promiseApiVersion(): String {
        val apiVersion = this.apiVersion
        if (apiVersion.isNullOrBlank() ||
            !Regex(
                "^[1-9][0-9]*.[0-9.]*[0-9]$"
            ).matches(apiVersion)
        ) {
            throw IllegalArgumentException("Illegal api version \"$apiVersion\".")
        }
        return apiVersion
    }

    private fun promiseAuthors(): Set<String>? {
        if (authors.isEmpty()) {
            return null
        }
        return authors
    }

    private fun promiseWebsite(): String? {
        val website = website ?: return null
        try {
            URL(website)
        } catch (e: MalformedURLException) {
            throw e
        }
        return website
    }

    private fun promiseLibraries(): Set<String>? {
        val libraries = librariesConfigurer.dependencies.map { it.module.id.toString() }.toSet()
        if (libraries.isEmpty()) {
            return null
        }
        return libraries
    }

    fun libraries(action: Action<LibrariesConfigurer>): PluginConfigurer {
        action.execute(librariesConfigurer)

        return this
    }
}