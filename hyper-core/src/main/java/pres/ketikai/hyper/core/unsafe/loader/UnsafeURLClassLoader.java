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

package pres.ketikai.hyper.core.unsafe.loader;

import pres.ketikai.hyper.common.util.Asserts;
import pres.ketikai.hyper.core.unsafe.UnsafeWrapper;
import sun.misc.Unsafe;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>为 {@link URLClassLoader} 提供包装</p>
 * 不推荐使用，因为基于 {@link Unsafe}
 *
 * <p>Created on 2023/1/5 12:00</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class UnsafeURLClassLoader {

    private static final Map<String, UnsafeURLClassLoader> WRAPPER_CACHES = new ConcurrentHashMap<>();

    private final List<URL> path;
    private final Deque<URL> unopenedUrls;
    private final boolean closed;

    private UnsafeURLClassLoader(List<URL> path, Deque<URL> unopenedUrls, boolean closed) {
        this.path = path;
        this.unopenedUrls = unopenedUrls;
        this.closed = closed;
    }

    public static UnsafeURLClassLoader wrap(final URLClassLoader ucl) throws ReflectiveOperationException {
        Asserts.notNull(ucl);

        final String key = ucl.toString();
        if (WRAPPER_CACHES.containsKey(key)) {
            return WRAPPER_CACHES.get(key);
        }

        final Object ucp = UnsafeWrapper.getObjectField(URLClassLoader.class, ucl, "ucp");
        final List<URL> path = UnsafeWrapper.getObjectField(ucp, "path");
        final Deque<URL> unopenedUrls = UnsafeWrapper.getObjectField(ucp, "unopenedUrls");
        final boolean closed = UnsafeWrapper.getBooleanField(ucp, "closed");

        UnsafeURLClassLoader wrapper = new UnsafeURLClassLoader(path, unopenedUrls, closed);
        WRAPPER_CACHES.put(key, wrapper);
        return wrapper;
    }

    public static void removeCache(final String key) {
        Asserts.notNull(key);

        WRAPPER_CACHES.remove(key);
    }

    public synchronized void invoke(URL url) {
        if (closed || url == null) {
            return;
        }
        synchronized (unopenedUrls) {
            if (!path.contains(url)) {
                unopenedUrls.addLast(url);
                path.add(url);
            }
        }
    }
}
