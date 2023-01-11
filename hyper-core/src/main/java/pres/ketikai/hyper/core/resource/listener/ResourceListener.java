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

package pres.ketikai.hyper.core.resource.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pres.ketikai.hyper.core.HyperCore;
import pres.ketikai.hyper.core.caller.Caller;
import pres.ketikai.hyper.core.listener.annotation.HyperListener;
import pres.ketikai.hyper.core.resource.HyperResourceEvent;
import pres.ketikai.hyper.core.resource.event.DeleteResourceEvent;
import pres.ketikai.hyper.core.resource.event.LoadResourceEvent;
import pres.ketikai.hyper.core.resource.event.NewResourceEvent;
import pres.ketikai.hyper.core.resource.event.UpdateResourceEvent;

import java.io.File;

/**
 * <p>资源文件监听器</p>
 *
 * <p>Created on 2023-01-01 10:37</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@HyperListener
public class ResourceListener implements Listener {

    private static final Logger log = LoggerFactory.getLogger(ResourceListener.class);

    @EventHandler(priority = EventPriority.LOWEST)
    public void handler(HyperResourceEvent event) {
        if (!event.isReadOnly() || event.isCancelled()) {
            return;
        }

        Caller caller = event.getCaller();
        if (caller.plugin() == HyperCore.getInstance()) {
            if (event instanceof NewResourceEvent that) {
                newEvent(that);
            } else if (event instanceof DeleteResourceEvent that) {
                deleteEvent(that);
            } else if (event instanceof UpdateResourceEvent that) {
                updateEvent(that);
            } else if (event instanceof LoadResourceEvent that) {
                loadEvent(that);
            }
        }
    }

    private void newEvent(NewResourceEvent event) {
        File file = event.getFile();
        String path = file.getAbsolutePath();
        log.debug("new resource: {}", path);
    }

    private void deleteEvent(DeleteResourceEvent event) {
        File file = event.getFile();
        String path = file.getAbsolutePath();
        log.debug("delete resource: {}", path);
    }

    private void updateEvent(UpdateResourceEvent event) {
        File file = event.getFile();
        String path = file.getAbsolutePath();
        log.debug("update resource: {}", path);
    }

    private void loadEvent(LoadResourceEvent event) {
        File file = event.getFile();
        String path = file.getAbsolutePath();
        log.debug("load resource: {}", path);
    }
}
