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

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import pres.ketikai.hyper.common.util.Asserts;
import pres.ketikai.hyper.core.caller.Caller;
import pres.ketikai.hyper.core.caller.Callers;
import pres.ketikai.hyper.core.resource.exception.EventReadOnlyException;

import java.io.File;

/**
 * <p>资源相关事件</p>
 *
 * <p>Created on 2022-12-31 18:40</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class HyperResourceEvent extends Event implements Cancellable {

    private final ResourceEventType type;
    private final boolean readOnly;
    private final Caller caller;
    private File file;
    private boolean cancel;

    public HyperResourceEvent(ResourceEventType type, File file, boolean readOnly, boolean async) {
        super(async);
        Asserts.notNull(type);
        Asserts.notNull(file);

        this.type = type;
        this.file = file;
        this.readOnly = readOnly;
        this.caller = Callers.getCaller();
        this.cancel = false;
    }

    public ResourceEventType getType() {
        return type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        Asserts.notNull(file);

        if (isReadOnly()) {
            throw new EventReadOnlyException();
        }
        this.file = file;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Caller getCaller() {
        return caller;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        if (isReadOnly()) {
            throw new EventReadOnlyException();
        }
        this.cancel = cancel;
    }
}
