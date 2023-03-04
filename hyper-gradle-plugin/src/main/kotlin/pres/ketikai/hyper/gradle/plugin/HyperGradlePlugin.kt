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

package pres.ketikai.hyper.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import pres.ketikai.hyper.gradle.plugin.configurer.LibrariesConfigurer
import pres.ketikai.hyper.gradle.plugin.task.Hyper

/**
 * <p>Hyper Gradle 工具</p>
 *
 * <p>Created on 2022-12-24 00:11</p>
 *
 * @see LibrariesConfigurer
 * @author ketikai
 * @since 0.0.1
 * @version 1.0.6
 */
class HyperGradlePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.withType(JavaCompile::class.java) {
            it.options.compilerArgs.add("-parameters")
        }
        target.tasks.create("hyper", Hyper::class.java)
    }
}