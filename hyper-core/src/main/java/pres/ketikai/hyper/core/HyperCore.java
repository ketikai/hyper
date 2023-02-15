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

package pres.ketikai.hyper.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.springframework.util.Assert;

/**
 * <p>插件入口</p>
 * <p>Created on 2022-12-17 16:19</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class HyperCore extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {
        Assert.notNull(instance, "instance is not ready yet");

        return instance;
    }

    @Override
    public void onLoad() {
    }

    private static void sendBanner() {
        Bukkit.getConsoleSender().sendMessage(
                "",
                " §b.__",
                " §b|  |__ ___.__.______   ___________",
                " §b|  |  <   |  |\\____ \\_/ __ \\_  __ \\",
                " §b|   Y  \\___  ||  |_> >  ___/|  | \\/",
                " §b|___|  / ____||   __/ \\___  >__|",
                " §b     \\/\\/     |__|        \\/",
                ""
        );
    }

    @Override
    public void onEnable() {
        // banner
        sendBanner();
        // set init var
        {
            instance = this;
        }
        // agent
        System.out.println(Bukkit.getPluginManager().getClass().getClassLoader());
    }

    @Override
    public void onDisable() {
        // unset init var
        {
            instance = null;
        }
    }
}
