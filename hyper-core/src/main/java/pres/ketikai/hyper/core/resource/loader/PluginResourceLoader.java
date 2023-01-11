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
import org.bukkit.plugin.PluginManager;
import pres.ketikai.hyper.common.util.Asserts;
import pres.ketikai.hyper.core.caller.Callers;
import pres.ketikai.hyper.core.resource.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * <p>插件资源文件加载器</p>
 *
 * <p>Created on 2022-12-31 15:09</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class PluginResourceLoader implements ResourceLoader {

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

        return path.startsWith("plugin:");
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

        path = path.substring(8);
        String[] args = path.split(":");
        int argsLen = args.length;
        Plugin plugin;
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (argsLen == MAX_ARGS_LENGTH) {
            plugin = pluginManager.getPlugin(args[0]);
        } else if (argsLen < MAX_ARGS_LENGTH) {
            plugin = Callers.getCaller().plugin();
        } else {
            return null;
        }

        if (plugin == null) {
            return null;
        }

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            return null;
        }

        File file = new File(dataFolder, path);
        if (!file.exists() || file.isDirectory()) {
            return null;
        }

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
