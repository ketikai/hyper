/*
 *    Copyright 2023 ketikai
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pres.ketikai.hyper.commons.unsafe.loader;

import pres.ketikai.hyper.commons.Asserts;
import pres.ketikai.hyper.commons.unsafe.UnsafeWrapper;
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
