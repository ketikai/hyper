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

package pres.ketikai.hyper.core.resource;

/**
 * <p>资源文件加载器接口</p>
 *
 * <p>Created on 2022-12-31 13:45</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public interface ResourceLoader {

    /**
     * <p>匹配资源文件加载器</p>
     *
     * @param path 路径
     * @return 是否匹配
     */
    boolean matches(String path);

    /**
     * <p>加载资源文件</p>
     *
     * @param path 路径
     * @return 成功返回资源文件数据字节组，失败返回 null
     */
    byte[] load(String path);
}
