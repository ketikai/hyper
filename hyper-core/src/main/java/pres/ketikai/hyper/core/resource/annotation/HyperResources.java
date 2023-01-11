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

import java.lang.annotation.*;

/**
 * <p>多个 {@link HyperResource} 注解的容器</p>
 * 该注解只有在 {@link HyperPlugin} 生效时才会生效
 * <p>Created on 2023-01-01 19:14</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HyperResources {

    HyperResource[] value();
}
