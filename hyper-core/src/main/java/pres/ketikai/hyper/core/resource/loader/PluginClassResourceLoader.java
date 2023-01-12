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

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pres.ketikai.hyper.commons.Asserts;
import pres.ketikai.hyper.core.resource.ResourceLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;

/**
 * <p>插件类资源文件加载器</p>
 *
 * <p>Created on 2022-12-31 14:07</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class PluginClassResourceLoader implements ResourceLoader {

    private static final int MAX_ARGS_LENGTH = 2;

    /**
     * <p>匹配资源文件加载器</p>
     *
     * @param path 路径
     * @return 是否匹配
     */
    @Override
    public boolean matches(String path) {
        Asserts.notNull(path);

        return path.startsWith("classpath:");
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

        path = path.substring(11);
        String[] args = path.split(":");
        int argsLen = args.length;
        Class<?> clazz;
        if (argsLen == MAX_ARGS_LENGTH) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(args[0]);
            if (plugin == null) {
                return null;
            }
            clazz = plugin.getClass();
        } else if (argsLen < MAX_ARGS_LENGTH) {
            String className = new Exception().getStackTrace()[1].getClassName();
            ClassLoader classLoader = PluginClassResourceLoader.class.getClassLoader();
            try {
                clazz = classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

        URL url = clazz.getResource(path);
        if (url == null) {
            return null;
        }

        try (FileSystem fileSystem = FileSystems.newFileSystem(url.toURI(), new HashMap<>(0))) {
            return Files.readAllBytes(fileSystem.getPath(path));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
