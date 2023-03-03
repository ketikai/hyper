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

package pres.ketikai.hyper.gradle.plugin.task

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import pres.ketikai.hyper.gradle.plugin.configurer.bukkit.PluginConfigurer
import java.nio.charset.Charset

/**
 * <p>简化 Bukkit 插件开发</p>
 *
 * <p>Created on 2023/1/3 12:18</p>
 *
 * @author ketikai
 * @since 0.0.1
 * @version 1.0.6
 */
abstract class HyperBukkit : DefaultTask() {

    @Internal
    var charset: Charset? = null

    private val pluginConfigurer = PluginConfigurer(project)

    init {
        super.setGroup("hyper")
        super.setDescription("简化 bukkit 插件开发")
        super.dependsOn(project.tasks.named("classes"))
    }

    @TaskAction
    fun execute() {
        pluginConfigurer.generateYamlFile(charset ?: Charsets.UTF_8)
    }

    fun pluginYaml(action: Action<PluginConfigurer>): HyperBukkit {
        action.execute(pluginConfigurer)

        return this
    }
}