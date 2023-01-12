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

package pres.ketikai.hyper.annotations.property;

import pres.ketikai.hyper.annotations.HyperPlugin;

import java.lang.annotation.*;

/**
 * <p>用于标记需要被注入属性的字段或方法</p>
 * 该注解只有在 {@link HyperPlugin} 生效时才会生效
 * <p>Created on 2023-01-01 11:43</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(HyperPropSources.class)
public @interface HyperPropSource {

    /**
     * <p>被注入的参数名</p>
     * 在属性或只有一个参数的方法上标记时可以省略
     *
     * @return 参数名
     */
    String name() default "";

    /**
     * <p>属性类型</p>
     *
     * @return 属性类型
     */
    String type() default "yaml";

    /**
     * <p>属性文件路径</p>
     *
     * @return 属性文件路径
     */
    String path();

    /**
     * <p>属性键</p>
     * 为空时获取所有内容，即默认<br>
     * 对于 YAML、JSON 等属性类型来说，它表示键<br>
     * 对于 TXT 类型来说，它表示行，必须是一个整数，从 0 开始，任何负数都表示获取所有内容
     *
     * @return 属性键
     */
    String key() default "";
}
