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

package pres.ketikai.hyper.core.caller;

import pres.ketikai.hyper.core.plugin.Plugins;

/**
 * <p>调用者相关工具</p>
 *
 * <p>Created on 2023-01-01 09:55</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Callers {

    private Callers() {

    }

    /**
     * <p>获取当前栈的底层调用者</p>
     * 注意：该方法目前仅在主线程有效
     *
     * @return 调用者
     */
    public static Caller getCaller() {
        final StackTraceElement[] stackTrace = new Exception().getStackTrace();
        final String className = stackTrace[stackTrace.length - 1].getClassName();
        final ClassLoader classLoader = Callers.class.getClassLoader();
        try {
            final Class<?> clazz = classLoader.loadClass(className);
            if (Thread.class.isAssignableFrom(clazz)) {
                throw new UnsupportedOperationException();
            }

            return new Caller(clazz, Plugins.getInstance(clazz));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
