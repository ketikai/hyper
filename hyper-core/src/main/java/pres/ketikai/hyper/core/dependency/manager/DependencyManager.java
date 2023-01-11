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

package pres.ketikai.hyper.core.dependency.manager;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * <p>依赖管理器接口</p>
 *
 * <p>Created on 2022-12-31 13:13</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public interface DependencyManager {

    /**
     * <p>解析依赖</p>
     * 该方法只会下载 id 所对应的依赖
     *
     * @param id 依赖 id
     * @return 成功返回文件对象，反之则为 null
     */
    File resolve(String id);

    /**
     * <p>解析依赖</p>
     * 该方法会下载 id 所对应的依赖及其所需要的依赖
     *
     * @param id 依赖 id
     * @return 成功返回文件对象集合，反之则为 null
     */

    Set<File> resolveDependencies(String id);

    /**
     * <p>删除依赖</p>
     *
     * @param id 依赖 id
     * @throws IOException 删除失败时抛出
     */
    void delete(String id) throws IOException;

    /**
     * <p>搜索依赖</p>
     * 该方法只会搜索 id 所对应的依赖
     *
     * @param id 依赖 id
     * @return 找到返回文件对象，反之则为 null
     */

    File search(String id);

    /**
     * <p>搜索依赖</p>
     * 该方法会搜索 id 所对应的依赖及其所需要的依赖
     *
     * @param id 依赖 id
     * @return 找到返回文件对象集合，反之则为 null
     */

    Set<File> searchDependencies(String id);
}
