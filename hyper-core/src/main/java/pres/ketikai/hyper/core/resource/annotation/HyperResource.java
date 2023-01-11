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

package pres.ketikai.hyper.core.resource.annotation;

import pres.ketikai.hyper.core.annotation.HyperPlugin;
import pres.ketikai.hyper.core.resource.ResourceLoader;

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
     * @see ResourceLoader
     */
    String path();
}
