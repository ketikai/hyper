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

package pres.ketikai.hyper.core.resource.exception;

/**
 * <p>事件只读异常</p>
 * 当对一个只读事件进行修改操作时应抛出该异常
 *
 * <p>Created on 2023-01-01 10:09</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class EventReadOnlyException extends RuntimeException {

    private static final String EX_MSG = "Cannot operate on read-only events.";

    public EventReadOnlyException() {
        this(EX_MSG);
    }

    public EventReadOnlyException(String message) {
        super(message);
    }
}
