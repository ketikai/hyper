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

package pres.ketikai.hyper.core.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import pres.ketikai.hyper.core.HyperCore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

/**
 * <p>插件相关工具</p>
 *
 * <p>Created on 2023/1/5 15:33</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Plugins {

    private static final PluginManager PLUGIN_MANAGER = Bukkit.getPluginManager();

    private Plugins() {

    }

    /**
     * <p>获取插件实例</p>
     *
     * @param pluginName 插件名
     * @return 插件实例
     */
    public static Plugin getInstance(final String pluginName) {
        return PLUGIN_MANAGER.getPlugin(pluginName);
    }

    /**
     * <p>获取类所属的插件实例</p>
     *
     * @param clazz 类
     * @return 插件实例
     */
    public static Plugin getInstance(final Class<?> clazz) {
        if (!HyperCore.class.getClassLoader().getClass().equals(clazz.getClassLoader().getClass())) {
            return null;
        }

        final String pluginYamlFileName = "/plugin.yml";
        final URL url = clazz.getResource(pluginYamlFileName);
        if (url == null) {
            return null;
        }

        try (final FileSystem fileSystem = FileSystems.newFileSystem(url.toURI(), Collections.emptyMap())) {
            final String prefix = "name:";
            final List<String> lines = Files.readAllLines(fileSystem.getPath(pluginYamlFileName));
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith(prefix)) {
                    return getInstance(line.substring(prefix.length()).trim());
                }
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
