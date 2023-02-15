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

package pres.ketikai.hyper.core.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * <p>spring 基础配置</p>
 *
 * <p>Created on 2023/2/12 10:42</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
@ComponentScan({"pres.ketikai.hyper.core"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SpringConfiguration {
}
