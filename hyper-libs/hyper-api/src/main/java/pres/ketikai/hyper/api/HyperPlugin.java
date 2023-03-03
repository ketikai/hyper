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

package pres.ketikai.hyper.api;

import java.lang.annotation.*;

/**
 * <p>标记插件并提供基本信息</p>
 *
 * <p>Created on 2023/3/4 1:36</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HyperPlugin {

    /**
     * 插件名称<br>
     * 同服务端下，它应该是唯一的<br>
     * 默认为该注解所标记的插件类的类名
     *
     * @return 插件名
     */
    String name() default "";

    /**
     * 插件描述
     *
     * @return 插件描述
     */
    String description() default "";

    /**
     * 插件版本<br>
     * 默认为 unknown
     *
     * @return 插件版本
     */
    String version() default "unknown";

    /**
     * 插件基于哪个版本的 api 开发
     *
     * @return api 版本
     */
    String api() default "";

    /**
     * 插件作者
     *
     * @return 插件作者
     */
    String[] authors() default {};

    /**
     * 插件主页
     *
     * @return 插件主页
     */
    String website() default "";

    /**
     * 插件加载时刻
     *
     * @return 插件加载时刻
     */
    String when() default "";

    /**
     * 插件与其他插件的从属关系<br>
     * {@code pluginName} 常规写法，即插件必须依赖的其他插件<br>
     * {@code ?pluginName} 软依赖写法，即插件可能依赖的其他插件<br>
     * {@code >pluginName} 延迟加载写法，即应在插件加载之后才加载的其他插件
     *
     * @return 与其他插件的从属关系
     */
    String[] dependencies() default {};

    /**
     * 插件所引用的依赖库<br>
     * 注意：此项和 {@link HyperPlugin#dependencies()} 并不相同<br>
     * 此项所指的是插件运行时所必要的第三方库，而不是指其他插件<br>
     * 通常该项不需要手动填写
     *
     * @return 插件所引用的依赖库
     */
    String[] libraries() default {};

    /**
     * 需要参与组件扫描的基本包路径
     *
     * @return 需要参与组件扫描的基本包路径
     */
    String[] basePackages() default {};
}
