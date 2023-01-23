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
import org.gradle.api.provider.Property
import java.io.File

/**
 * <p>hyper 增强对应配置实体</p>
 *
 * <p>Created on 2023/1/23 19:43</p>
 * @author ketikai
 * @since 0.0.1
 * @version 0.0.1
 */
class Hyper(project: Project) {

    val baseClassesFile = File("${project.buildDir}/classes/java/main")

    private val objectFactory = project.objects

    val moreLifeEvents: Property<Boolean> = objectFactory.property(Boolean::class.java)

    init {
        moreLifeEvents.set(false)
    }
}