/*
 *     Copyright (C) 2023  ketikai
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package pres.ketikai.hyper.core.property.annotation;

import pres.ketikai.hyper.core.annotation.HyperPlugin;
import pres.ketikai.hyper.core.property.PropertyType;
import pres.ketikai.hyper.core.resource.ResourceLoader;

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
    PropertyType type() default PropertyType.YAML;

    /**
     * <p>属性文件路径</p>
     *
     * @return 属性文件路径
     * @see ResourceLoader
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
