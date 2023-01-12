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

import pres.ketikai.hyper.commons.Asserts;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * <p>为 {@link Unsafe} 提供方法包装</p>
 *
 * <p>Created on 2023/1/5 13:04</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@SuppressWarnings({"unchecked"})
public final class UnsafeWrapper {

    public static final Unsafe UNSAFE;

    static {
        try {
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            UNSAFE = (Unsafe) unsafeField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private UnsafeWrapper() {
    }

    /**
     * <p>获取字段地址的偏移</p>
     *
     * @param clazz     所在类
     * @param fieldName 字段名
     * @return 偏移量
     * @throws ReflectiveOperationException 反射获取字段出错时抛出，详见 {@link Class#getDeclaredField}
     */
    public static long getFieldOffset(final Class<?> clazz, final String fieldName) throws ReflectiveOperationException {
        Asserts.notNull(clazz);
        Asserts.notNull(fieldName);

        final Field field = clazz.getDeclaredField(fieldName);
        return UNSAFE.objectFieldOffset(field);
    }

    /**
     * <p>获取实例的指定字段对象</p>
     *
     * @param object    所在实例
     * @param fieldName 字段名
     * @param <O>       实例泛型
     * @param <R>       返回结果泛型
     * @return 指定泛型类型的对象
     * @throws ReflectiveOperationException 详见 {@link UnsafeWrapper#getFieldOffset}
     */
    public static <O, R> R getObjectField(final O object, final String fieldName) throws ReflectiveOperationException {
        Asserts.notNull(object);
        Asserts.notNull(fieldName);

        final Class<?> clazz = object.getClass();
        return (R) UNSAFE.getObject(object, getFieldOffset(clazz, fieldName));
    }

    /**
     * <p>获取实例的指定字段对象</p>
     *
     * @param clazz     所在类
     * @param object    所在实例
     * @param fieldName 字段名
     * @param <O>       实例泛型
     * @param <R>       返回结果泛型
     * @return 指定泛型类型的对象
     * @throws ReflectiveOperationException 详见 {@link UnsafeWrapper#getFieldOffset}
     */
    public static <O, R> R getObjectField(final Class<? extends O> clazz, final O object, final String fieldName) throws ReflectiveOperationException {
        Asserts.notNull(clazz);
        Asserts.notNull(object);
        Asserts.notNull(fieldName);

        return (R) UNSAFE.getObject(object, getFieldOffset(clazz, fieldName));
    }

    /**
     * <p>获取实例的指定字段布尔值</p>
     *
     * @param object    所在实例
     * @param fieldName 字段名
     * @param <O>       实例泛型
     * @return 指定泛型类型的对象
     * @throws ReflectiveOperationException 详见 {@link UnsafeWrapper#getFieldOffset}
     */
    public static <O> boolean getBooleanField(final O object, final String fieldName) throws ReflectiveOperationException {
        Asserts.notNull(object);
        Asserts.notNull(fieldName);

        final Class<?> clazz = object.getClass();
        return UNSAFE.getBoolean(object, getFieldOffset(clazz, fieldName));
    }

    /**
     * <p>获取实例的指定字段布尔值</p>
     *
     * @param clazz     所在类
     * @param object    所在实例
     * @param fieldName 字段名
     * @param <O>       实例泛型
     * @return 指定泛型类型的对象
     * @throws ReflectiveOperationException 详见 {@link UnsafeWrapper#getFieldOffset}
     */
    public static <O> boolean getBooleanField(final Class<? extends O> clazz, final O object, final String fieldName) throws ReflectiveOperationException {
        Asserts.notNull(clazz);
        Asserts.notNull(object);
        Asserts.notNull(fieldName);

        return UNSAFE.getBoolean(object, getFieldOffset(clazz, fieldName));
    }
}
