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

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import pres.ketikai.hyper.commons.Asserts;
import pres.ketikai.hyper.core.plugin.event.PluginInitializeEvent;

/**
 * <p>标题</p>
 *
 * <p>Created on 2023/1/4 16:06</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class HyperPluginEvent extends Event {

    public final Plugin plugin;

    protected HyperPluginEvent(Plugin plugin) {
        if (!(this instanceof PluginInitializeEvent)) {
            Asserts.notNull(plugin);
        }

        this.plugin = plugin;
    }
}
