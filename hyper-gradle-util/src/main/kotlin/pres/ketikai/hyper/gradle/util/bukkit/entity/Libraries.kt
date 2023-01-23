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

import org.eclipse.aether.artifact.Artifact
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import pres.ketikai.hyper.gradle.util.bukkit.LibraryScope

/**
 * <p>依赖库配置对应实体</p>
 * 提取项目中的依赖信息汇总并解析<br>
 * 生成的依赖库集合是被版本冲突仲裁自动处理过的<br>
 *
 * <p>Created on 2022-12-24 00:40</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.5
 */
class Libraries(project: Project) {

    private val objectFactory = project.objects

    val scope: Property<LibraryScope> = objectFactory.property(LibraryScope::class.java)

    val dependencies: MutableMap<String, Artifact> = linkedMapOf()

    val visitChildren: Property<Boolean> = objectFactory.property(Boolean::class.java)

    val reserveSnapshotVersion: Property<Boolean> = objectFactory.property(Boolean::class.java)

    val excludes: ListProperty<String> = objectFactory.listProperty(String::class.java)

    init {
        scope.set(LibraryScope.RUNTIME)
        visitChildren.set(false)
        reserveSnapshotVersion.set(false)
    }
}