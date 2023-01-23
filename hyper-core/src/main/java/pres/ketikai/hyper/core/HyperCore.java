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

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pres.ketikai.hyper.annotations.HyperPlugin;
import pres.ketikai.hyper.core.libraries.Libraries;
import pres.ketikai.hyper.core.resources.Resources;

/**
 * <p>插件入口</p>
 * <p>Created on 2022-12-17 16:19</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@HyperPlugin
public final class HyperCore extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {
        return instance;
    }

    private static void setInstance(Plugin instance) {
        HyperCore.instance = instance;
    }

    @Override
    public void onLoad() {
        Resources.save(this, false);
        Libraries.loadLibraries(this);
    }

    @Override
    public void onEnable() {
        setInstance(this);
    }

    @Override
    public void onDisable() {
        setInstance(null);
    }
}
