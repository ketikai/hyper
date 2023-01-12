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

package pres.ketikai.hyper.core.resource.loader;

import pres.ketikai.hyper.commons.Asserts;
import pres.ketikai.hyper.core.resource.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>策略资源文件加载器</p>
 *
 * <p>Created on 2022-12-31 14:02</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class StrategyResourceLoader implements ResourceLoader {

    private final Set<ResourceLoader> loaders = new CopyOnWriteArraySet<>();

    private ResourceLoader defaultLoader;

    public StrategyResourceLoader() {
        this(new DefaultResourceLoader());
    }

    public StrategyResourceLoader(ResourceLoader defaultLoader) {
        Asserts.notNull(defaultLoader);

        this.defaultLoader = defaultLoader;
    }

    /**
     * <p>匹配资源文件加载器</p>
     *
     * @param path 路径
     * @return 是否匹配
     */
    @Override
    public boolean matches(String path) {
        Asserts.notNull(path);

        return true;
    }

    /**
     * <p>加载资源文件</p>
     *
     * @param path 路径
     * @return 成功返回资源文件数据字节组，失败返回 null
     */
    @Override
    public byte[] load(String path) {
        Asserts.notNull(path);

        byte[] result;
        for (ResourceLoader loader : loaders) {
            if (loader.matches(path) && (result = loader.load(path)) != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * <p>添加资源文件加载器</p>
     *
     * @param loaders 资源文件加载器
     */
    public void addLoaders(ResourceLoader... loaders) {
        Asserts.notEmpty(loaders);

        for (ResourceLoader parser : loaders) {
            if (parser != this) {
                this.loaders.add(parser);
            }
        }
    }

    /**
     * <p>删除资源文件加载器</p>
     *
     * @param loaders 资源文件加载器
     */
    public void delLoaderss(ResourceLoader... loaders) {
        Asserts.notEmpty(loaders);

        for (ResourceLoader loader : loaders) {
            this.loaders.remove(loader);
        }
    }

    /**
     * <p>清空资源文件加载器</p>
     */
    public void clear() {
        this.loaders.clear();
    }

    /**
     * <p>获取资源文件加载器列表</p>
     * 此方法获取的是加载器实例的全类名
     *
     * @return 资源文件加载器列表，列表为空时返回 null
     */
    public List<String> getLoaders() {
        int size = loaders.size();
        if (size == 0) {
            return null;
        }

        List<String> list = new ArrayList<>(size);
        loaders.forEach(loader -> list.add(loader.getClass().getName()));
        return list;
    }

    /**
     * <p>获取默认资源文件加载器</p>
     *
     * @return 默认资源文件加载器
     */
    public ResourceLoader getDefaultLoader() {
        return defaultLoader;
    }

    public void setDefaultLoader(ResourceLoader defaultLoader) {
        Asserts.notNull(defaultLoader);

        this.defaultLoader = defaultLoader;
    }

    /**
     * <p>默认资源文件加载器</p>
     *
     * <p>Created on 2022-12-31 13:58</p>
     *
     * @author ketikai
     * @version 0.0.1
     * @since 0.0.1
     */
    private static final class DefaultResourceLoader implements ResourceLoader {

        /**
         * <p>匹配资源文件加载器</p>
         *
         * @param path 路径
         * @return 是否匹配
         */
        @Override
        public boolean matches(String path) {
            Asserts.notNull(path);

            return true;
        }

        /**
         * <p>加载资源文件</p>
         *
         * @param path 路径
         * @return 成功返回资源文件数据字节组，失败返回 null
         */
        @Override
        public byte[] load(String path) {
            Asserts.notNull(path);

            File file = new File(path);
            try {
                if (!file.exists() || file.isDirectory()) {
                    throw new NoSuchFileException("The path must be an existing file.");
                }
                return Files.readAllBytes(file.toPath());
            } catch (InvalidPathException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
