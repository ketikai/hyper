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

package pres.ketikai.hyper.core.unsafe;

import pres.ketikai.hyper.common.util.Asserts;
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
