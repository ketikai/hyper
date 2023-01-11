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

package pres.ketikai.hyper.core.plugin.event;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import pres.ketikai.hyper.core.plugin.HyperPluginEvent;

/**
 * <p>插件加载事件</p>
 * 增强插件在其实例被加载前触发
 * <p>Created on 2023/1/4 18:01</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class PluginLoadEvent extends HyperPluginEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PluginLoadEvent(Plugin plugin) {
        super(plugin);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
