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

package pres.ketikai.hyper.gradle.plugin.configurer

import org.gradle.api.Action
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import pres.ketikai.hyper.api.HyperPlugin
import pres.ketikai.hyper.commons.asm.AsmUtils
import pres.ketikai.hyper.commons.asm.metadata.AnnotationMetadata
import pres.ketikai.hyper.commons.asm.metadata.AnnotationMetadata.MethodMetadata.ArrayMetadata
import pres.ketikai.hyper.commons.asm.metadata.ClassMetadata
import pres.ketikai.hyper.commons.yaml.YamlUtils
import java.io.File
import java.io.FileNotFoundException
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

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

    private val librariesConfigurer = LibrariesConfigurer(project)

    private var name: String? = null
    private var main: String? = null
    private var version: String? = null
    private var api: String? = null
    private var description: String? = null
    private val authors: MutableSet<String> = linkedSetOf()
    private var website: String? = null
    private var load: String? = null
    private val depend: MutableSet<String> = linkedSetOf()
    private val softDepend: MutableSet<String> = linkedSetOf()
    private val loadBefore: MutableSet<String> = linkedSetOf()
    private val libraries: MutableSet<String> = linkedSetOf()

    fun generateYamlFile(charset: Charset) {
        scan()

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
        val yaml = YamlUtils.getYaml()
        val map = if (source != null) yaml.load<MutableMap<String, Any>>(source) else mutableMapOf()
        map["name"] = promisePluginName()
        map["main"] = promiseMain()
        map["version"] = promiseVersion()

        promiseApiVersion()?.apply { map["api-version"] = this }
        description?.apply { map["description"] = this }
        promiseAuthors()?.apply { map["authors"] = this.toList() }
        promiseWebsite()?.apply { map["website"] = this }
        load?.apply { map["load"] = this }

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

    private fun promiseApiVersion(): String? {
        val api = this.api
        if (!api.isNullOrBlank() &&
            !Regex(
                "^[1-9][0-9]*.[0-9.]*[0-9]$"
            ).matches(api)
        ) {
            throw IllegalArgumentException("Illegal api version \"$api\".")
        }
        return api
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
        val libraries = librariesConfigurer.dependencies.map { it.module.id.toString() }.toMutableSet()
        libraries.addAll(this.libraries)
        if (libraries.isEmpty()) {
            return null
        }
        return libraries
    }

    private fun scan() {
        val classesDir = File(project.buildDir, "/classes/")
        if (!classesDir.exists() || classesDir.isFile) {
            throw FileNotFoundException(classesDir.absolutePath)
        }

        var walk: Stream<Path>? = null
        var methods: Array<AnnotationMetadata.MethodMetadata>? = null
        try {
            walk = Files.walk(classesDir.toPath())
            val iterator = walk.iterator()
            var next: File?
            var metadata: ClassMetadata?
            var temp: AnnotationMetadata?
            while (iterator.hasNext()) {
                next = iterator.next().toFile()
                if (next.isDirectory || !next.toString().endsWith(".class")) {
                    continue
                }

                metadata = AsmUtils.getClassMetadata(next.readBytes(), Opcodes.ASM9, ClassReader.SKIP_FRAMES)
                if (metadata == null || !AsmUtils.isExtends(metadata, "org/bukkit/plugin/java/JavaPlugin")) {
                    continue
                }

                temp = AsmUtils.hasAnnotation(metadata, Type.getDescriptor(HyperPlugin::class.java))
                if (methods != null && temp != null) {
                    throw RuntimeException("The plugin main class must be unique.")
                }
                main = metadata.name.replace('/', '.')
                name = main!!.split('.').last()
                methods = temp.methods
            }
        } finally {
            walk?.close()
        }

        methods ?: throw RuntimeException("The annotation @HyperPlugin was not found.")

        var methodName: String?
        var methodValue: Any?
        var valueArray: Array<out Any>?
        for (method in methods) {
            methodName = method.name ?: continue
            methodValue = method.value
            if (methodValue is String) {
                when (methodName) {
                    "name" -> name = methodValue
                    "version" -> version = methodValue
                    "api" -> api = methodValue
                    "description" -> description = methodValue
                    "website" -> website = methodValue
                    "when" -> load = methodValue
                }
            } else if (methodValue is ArrayMetadata) {
                valueArray = methodValue.array
                if (valueArray.isNullOrEmpty()) {
                    continue
                }
                when (methodName) {
                    "authors" -> {
                        valueArray.forEach {
                            if (it is String) {
                                authors.add(it)
                            }
                        }
                    }

                    "libraries" -> {
                        valueArray.forEach {
                            if (it is String) {
                                libraries.add(it)
                            }
                        }
                    }

                    "dependencies" -> {
                        valueArray.forEach {
                            if (it is String) {
                                when (it[0]) {
                                    '?' -> softDepend.add(it)
                                    '>' -> loadBefore.add(it)
                                    else -> depend.add(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun libraries(action: Action<LibrariesConfigurer>): PluginConfigurer {
        action.execute(librariesConfigurer)

        return this
    }
}