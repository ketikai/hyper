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

package pres.ketikai.hyper.core.resource.event;

import org.bukkit.event.HandlerList;
import pres.ketikai.hyper.core.resource.HyperResourceEvent;
import pres.ketikai.hyper.core.resource.ResourceEventType;

import java.io.File;

/**
 * <p>资源新增事件</p>
 *
 * <p>Created on 2022-12-31 19:01</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class NewResourceEvent extends HyperResourceEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public NewResourceEvent(File file, boolean onlyRead, boolean async) {
        super(ResourceEventType.NEW, file, onlyRead, async);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
