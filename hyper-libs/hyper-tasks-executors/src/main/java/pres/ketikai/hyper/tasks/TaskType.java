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

package pres.ketikai.hyper.tasks;

/**
 * <p>任务类型枚举</p>
 *
 * <p>Created on 2023/1/12 12:15</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public enum TaskType {

    /**
     * <p>扫描</p>
     * 通常用于标识对包、类或文件实施内容扫描的任务<br>
     * 执行此类任务往往期望得到一个装有文件或路径的集合
     */
    SCAN,
    /**
     * <p>观察</p>
     * 通常用于标识对文件进行实时状态监视的任务<br>
     * 执行此类任务往往期望得到一个装有文件或路径的集合
     */
    WATCH,
    /**
     * <p>增强</p>
     * 通常用于标识对类、方法实施修改或代理的任务<br>
     * 执行此类任务往往期望得到一个修改或代理后的类文件或类对象
     */
    ENHANCE,
    /**
     * <p>资源</p>
     * 通常用于标识耗时较长的 IO 相关操作的任务<br>
     * 执行此类任务往往不需要返回值，但不排除特殊情况
     */
    RESOURCE,
    /**
     * <p>循环</p>
     * 通常用于标识耗时极长或永不停止的任务<br>
     * 执行此类任务往往不需要返回值，但不排除特殊情况
     */
    WHILE,
    /**
     * <p>其他</p>
     * 通常用于标识自定义执行类型的任务<br>
     * 执行此类任务是否需要返回值由其业务需求决定
     */
    OTHER
}
