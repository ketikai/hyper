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

package pres.ketikai.hyper.api.command;

import pres.ketikai.hyper.api.role.Authority;

import java.lang.annotation.*;

/**
 * <p>标记命令并提供基本信息</p>
 *
 * <p>Created on 2023/2/27 19:29</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {

    /**
     * 命令名称
     *
     * @return 命令名称
     */
    String name();

    /**
     * 命令别名
     *
     * @return 命令别名
     */
    String[] aliases() default {};

    /**
     * 命令描述
     *
     * @return 命令描述
     */
    String description() default "";

    /**
     * 命令用法
     *
     * @return 命令用法
     */
    String usage() default "";

    /**
     * 命令所须权限
     *
     * @return 命令所须权限
     * @see Authority#authorities()
     */
    String[] authorities() default {};
}
