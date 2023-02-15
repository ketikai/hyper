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

package pres.ketikai.hyper.commons.unsafe;

import org.springframework.util.Assert;
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

    private static final Map<URLClassLoader, UnsafeURLClassLoader> CACHES = new ConcurrentHashMap<>();

    private final Object ucp;
    private final List<URL> path;
    private final Deque<URL> unopenedUrls;

    private UnsafeURLClassLoader(Object ucp, List<URL> path, Deque<URL> unopenedUrls) {
        this.ucp = ucp;
        this.path = path;
        this.unopenedUrls = unopenedUrls;
    }

    /**
     * 包装一个 {@link URLClassLoader}
     *
     * @param ucl 详见 {@link URLClassLoader}
     * @return 详见 {@link UnsafeURLClassLoader}
     */
    @SuppressWarnings({"unchecked"})
    public static UnsafeURLClassLoader wrap(URLClassLoader ucl) {
        Assert.notNull(ucl, "ucl must not be null");

        if (CACHES.containsKey(ucl)) {
            return CACHES.get(ucl);
        }

        final Object ucp;
        final List<URL> path;
        final Deque<URL> unopenedUrls;
        try {
            ucp = UnsafeWrapper.getValue(URLClassLoader.class, ucl, "ucp");
            path = (List<URL>) UnsafeWrapper.getValue(ucp, "path");
            unopenedUrls = (Deque<URL>) UnsafeWrapper.getValue(ucp, "unopenedUrls");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return CACHES.putIfAbsent(ucl, new UnsafeURLClassLoader(ucp, path, unopenedUrls));
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        CACHES.clear();
    }

    /**
     * 添加 url 到类加载器
     *
     * @param urls 待添加的 url
     */
    public synchronized void addURL(URL... urls) {
        if (urls == null || urls.length == 0 || isClosed()) {
            return;
        }
        synchronized (unopenedUrls) {
            for (final URL url : urls) {
                if (url != null && !path.contains(url)) {
                    unopenedUrls.addLast(url);
                    path.add(url);
                }
            }
        }
    }

    private boolean isClosed() {
        try {
            return (boolean) UnsafeWrapper.getValue(ucp, "closed");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
