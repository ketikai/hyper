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
import org.gradle.api.artifacts.ResolvedDependency
import pres.ketikai.hyper.gradle.util.dependency.DependencyFilter
import pres.ketikai.hyper.gradle.util.dependency.DependencyResolver
import pres.ketikai.hyper.gradle.util.dependency.impl.DefaultDependencyFilter
import pres.ketikai.hyper.gradle.util.dependency.impl.DefaultDependencyResolver

/**
 * <p>依赖库配置器</p>
 *
 * <p>Created on 2022-12-24 00:40</p>
 *
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.6
 */
class LibrariesConfigurer(private val project: Project) {

    var enabled = true
    var scope: String? = null

    private val dependencyFilter: DependencyFilter = DefaultDependencyFilter()
    private val dependencyResolver: DependencyResolver = DefaultDependencyResolver(dependencyFilter)

    val dependencies: Set<ResolvedDependency>
        get() {
            return if (enabled) dependencyResolver.resolve(
                project.configurations
                    .getByName(scope ?: "runtimeClasspath")
                    .resolvedConfiguration
                    .firstLevelModuleDependencies
            ) else emptySet()
        }

    fun filter(action: Action<DependencyFilter>): LibrariesConfigurer {
        action.execute(dependencyFilter)

        return this
    }
}