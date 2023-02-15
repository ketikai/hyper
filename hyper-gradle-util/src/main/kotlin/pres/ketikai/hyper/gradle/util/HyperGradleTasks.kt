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

package pres.ketikai.hyper.gradle.util

import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import pres.ketikai.hyper.gradle.util.task.HyperBukkit

/**
 * <p>在任务容器中声明并赋值 Hyper Tasks</p>
 *
 * <p>Created on 2023/1/7 17:44</p>
 *
 * @author ketikai
 * @since 0.0.1
 * @version 1.0.6
 */
val TaskContainer.hyperBukkit: TaskProvider<HyperBukkit>
    get() {
        return named("hyperBukkit", HyperBukkit::class.java)
    }