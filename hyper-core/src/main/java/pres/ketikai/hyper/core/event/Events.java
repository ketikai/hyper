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

package pres.ketikai.hyper.core.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import pres.ketikai.hyper.commons.Asserts;

/**
 * <p>事件相关工具</p>
 *
 * <p>Created on 2022-12-31 18:55</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Events {

    private Events() {
    }

    /**
     * <p>调用事件</p>
     *
     * @param event 事件
     * @return 事件在调用过程被取消则返回 false
     */
    public static boolean call(Event event) {
        Asserts.notNull(event);

        Bukkit.getPluginManager().callEvent(event);
        if (event instanceof Cancellable that) {
            return !that.isCancelled();
        }
        return true;
    }
}
