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

package pres.ketikai.hyper.api.role;

import java.lang.annotation.*;

/**
 * <p>标记方法执行时指定目标所须的权限</p>
 *
 * <p>Created on 2023/3/4 4:22</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authority {

    /**
     * 目标<br>
     * 指定的方法形参名
     *
     * @return 目标
     */
    String target();

    /**
     * 所须权限<br>
     * {@code authority} 表示必须拥有该权限<br>
     * {@code -authority} 表示必须没有该权限
     *
     * @return 所须权限
     */
    String[] authorities();
}
