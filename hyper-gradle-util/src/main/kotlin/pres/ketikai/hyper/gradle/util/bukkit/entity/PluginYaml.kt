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

package pres.ketikai.hyper.gradle.util.bukkit.entity

import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File

/**
 * <p>bukkit plugin.yml 对应实体</p>
 *
 * <p>Created on 2023/1/23 19:18</p>
 * @author ketikai
 * @since 0.0.1
 * @version 0.0.1
 */
class PluginYaml(project: Project) {

    private val defaultPluginYamlFileName = "plugin.yml"
    val defaultPluginYamlFile = File("${project.buildDir}/resources/main", defaultPluginYamlFileName)

    private val objectFactory = project.objects
    val yaml: Yaml

    val name: Property<String> = objectFactory.property(String::class.java)

    val main: Property<String> = objectFactory.property(String::class.java)

    val version: Property<String> = objectFactory.property(String::class.java)

    val apiVersion: Property<String> = objectFactory.property(String::class.java)

    val description: Property<String> = objectFactory.property(String::class.java)

    val authors: ListProperty<String> = objectFactory.listProperty(String::class.java)

    val website: Property<String> = objectFactory.property(String::class.java)

    val prefix: Property<String> = objectFactory.property(String::class.java)

    val load: Property<String> = objectFactory.property(String::class.java)

    val database: Property<Boolean> = objectFactory.property(Boolean::class.java)

    val depend: ListProperty<String> = objectFactory.listProperty(String::class.java)

    val softDepend: ListProperty<String> = objectFactory.listProperty(String::class.java)

    val loadBefore: ListProperty<String> = objectFactory.listProperty(String::class.java)

    val libraries: ListProperty<String> = objectFactory.listProperty(String::class.java)

    init {
        val options = DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.isPrettyFlow = true
        options.indicatorIndent = 2
        options.indent = 4
        yaml = Yaml(options)
    }
}