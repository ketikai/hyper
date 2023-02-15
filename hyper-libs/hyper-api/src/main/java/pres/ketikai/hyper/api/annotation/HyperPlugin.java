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

package pres.ketikai.hyper.api.annotation;

import java.lang.annotation.*;

/**
 * <p>用于标记需要被管理的插件</p>
 * 该注解只有在合法的 hyper 插件入口类上才会生效
 * <p>Created on 2022-12-25 00:42</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HyperPlugin {
}
