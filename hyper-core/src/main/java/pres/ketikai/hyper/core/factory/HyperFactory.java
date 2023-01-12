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

package pres.ketikai.hyper.core.factory;

import org.springframework.cglib.proxy.Enhancer;
import pres.ketikai.hyper.commons.Asserts;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>增强工厂</p>
 *
 * <p>Created on 2023-01-01 15:08</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class HyperFactory {

    private static final Map<ClassKey, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

    private HyperFactory() {

    }

    @SuppressWarnings({"unchecked"})
    public static <O> O newInstance(Class<? extends O> clazz, final Object... args) {
        Asserts.notNull(clazz);
        Asserts.notNull(args);

        boolean first = true;
        ClassKey classKey = null;
        if (CLASS_CACHE.size() != 0) {
            for (final ClassKey key : CLASS_CACHE.keySet()) {
                if (clazz == key.clazz) {
                    classKey = key;
                    first = false;
                    break;
                }
            }
        }

        try {
            if (first) {
                classKey = new ClassKey(clazz);

                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(clazz);
                enhancer.setClassLoader(clazz.getClassLoader());
                // todo: 生成增强类
                clazz = enhancer.createClass();
            } else {
                clazz = CLASS_CACHE.get(classKey).asSubclass(clazz);
            }

            final O instance;
            final int argsLen = args.length;
            if (argsLen == 0) {
                instance = clazz.getConstructor().newInstance();
            } else {
                final Class<?>[] argsClass = new Class[argsLen];
                for (int i = 0; i < argsLen; i++) {
                    argsClass[i] = args[i].getClass();
                }
                instance = clazz.getConstructor(argsClass).newInstance(args);
            }

            if (first) {
                CLASS_CACHE.put(classKey, clazz);
            } else {
                classKey.vitality.getAndIncrement();
            }

            return instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>触发增强类缓存列表的垃圾回收</p>
     * 不要频繁触发！！！
     */
    public static void gc() {
        if (CLASS_CACHE.size() == 0) {
            return;
        }

        int count = 0;
        Iterator<ClassKey> it = CLASS_CACHE.keySet().iterator();
        while (it.hasNext()) {
            if (it.next().vitality.decrementAndGet() < 0) {
                it.remove();
                count++;
            }
        }

        if (count > 0) {
            System.gc();
        }
    }

    private static final class ClassKey {

        private final Class<?> clazz;
        private final AtomicInteger vitality = new AtomicInteger(1);

        private ClassKey(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ClassKey classKey)) {
                return false;
            }

            return Objects.equals(clazz, classKey.clazz);
        }

        @Override
        public int hashCode() {
            return clazz.hashCode();
        }
    }
}
