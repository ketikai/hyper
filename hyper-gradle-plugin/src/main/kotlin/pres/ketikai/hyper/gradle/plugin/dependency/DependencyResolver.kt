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

package pres.ketikai.hyper.gradle.plugin.dependency

import org.gradle.api.artifacts.ResolvedDependency

/**
 * <p>提供依赖解析器接口</p>
 *
 * <p>Created on 2023/2/13 19:37</p>
 *
 * @author ketikai
 * @since 1.0.6
 * @version 1.0.6
 */
interface DependencyResolver {

    fun resolve(dependencies: Set<ResolvedDependency>): Set<ResolvedDependency>
}