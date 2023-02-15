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

package pres.ketikai.hyper.gradle.util.dependency.impl

import org.gradle.api.artifacts.ResolvedDependency
import pres.ketikai.hyper.gradle.util.dependency.DependencyFilter
import pres.ketikai.hyper.gradle.util.dependency.DependencyResolver

/**
 * <p>默认的依赖解析器实现</p>
 *
 * <p>Created on 2023/2/13 22:58</p>
 *
 * @author ketikai
 * @since 1.0.6
 * @version 1.0.6
 */
class DefaultDependencyResolver(private val dependencyFilter: DependencyFilter) : DependencyResolver {

    override fun resolve(dependencies: Set<ResolvedDependency>): Set<ResolvedDependency> {
        val includedDependencies: MutableSet<ResolvedDependency> = linkedSetOf()
        val excludedDependencies: MutableSet<ResolvedDependency> = linkedSetOf()

        resolve(dependencies, includedDependencies, excludedDependencies)

        return includedDependencies
    }

    private fun resolve(
        dependencies: Set<ResolvedDependency>,
        includedDependencies: MutableSet<ResolvedDependency>,
        excludedDependencies: MutableSet<ResolvedDependency>
    ) {
        dependencies.forEach {
            if (dependencyFilter.isIncluded(it)) {
                includedDependencies.add(it)
            } else {
                excludedDependencies.add(it)
            }
            resolve(it.children, includedDependencies, excludedDependencies)
        }
    }
}