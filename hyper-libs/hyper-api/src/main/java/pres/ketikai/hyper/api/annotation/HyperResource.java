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
 * <p>用于标记需要被注入资源的字段或方法</p>
 * 该注解只有在 {@link HyperPlugin} 生效时才会生效
 * <p>Created on 2023-01-01 12:00</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(HyperResources.class)
public @interface HyperResource {

    /**
     * <p>被注入的参数名</p>
     * 在属性或只有一个参数的方法上标记时可以省略
     *
     * @return 参数名
     */
    String name() default "";

    /**
     * <p>资源文件路径</p>
     *
     * @return 资源文件路径
     */
    String path();
}
