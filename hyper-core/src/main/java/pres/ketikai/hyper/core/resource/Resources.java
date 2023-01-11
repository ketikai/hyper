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

import org.bukkit.plugin.Plugin;
import pres.ketikai.hyper.common.util.Asserts;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * <p>资源文件相关工具</p>
 *
 * <p>Created on 2022-12-31 17:04</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Resources {

    private static final char SEPARATOR = '/';
    private static final String RESOURCES_PATH = "/resources";

    private Resources() {

    }

    public static void save(Plugin plugin, boolean replace) {
        save(plugin, RESOURCES_PATH, replace);
    }

    /**
     * <p>保存 jar 包内资源文件到 {@link Plugin#getDataFolder()}</p>
     *
     * @param plugin        插件实例
     * @param resourcesPath 资源路径
     * @param replace       是否启用替换
     */
    public static void save(Plugin plugin, String resourcesPath, boolean replace) {
        Asserts.notNull(plugin);
        Asserts.notNull(resourcesPath);

        resourcesPath = resourcesPath.trim();
        if (resourcesPath.charAt(0) != SEPARATOR) {
            resourcesPath = SEPARATOR + resourcesPath;
        }

        Class<? extends Plugin> clazz = plugin.getClass();
        URL url = clazz.getResource(resourcesPath);
        if (url == null) {
            return;
        }

        File dataFolder = plugin.getDataFolder();
        Stream<Path> walk = null;
        try (FileSystem fileSystem = FileSystems.newFileSystem(url.toURI(), Collections.emptyMap())) {
            Path path = fileSystem.getPath(resourcesPath);
            walk = Files.walk(path);
            Iterator<Path> it = walk.iterator();
            Path next;

            CopyOption[] copyOptions;
            if (replace) {
                copyOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
            } else {
                copyOptions = new CopyOption[0];
            }
            int resourcesPathLen = resourcesPath.length();
            while (it.hasNext()) {
                next = it.next();
                Files.copy(next,
                        new File(dataFolder, next.toString().substring(resourcesPathLen)).toPath(),
                        copyOptions);
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (walk != null) {
                walk.close();
            }
        }
    }
}
