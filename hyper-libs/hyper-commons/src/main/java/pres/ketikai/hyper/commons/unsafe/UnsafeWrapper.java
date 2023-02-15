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
public abstract class UnsafeWrapper {

    public static final Unsafe UNSAFE;

    static {
        Field unsafeField = null;
        try {
            unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            UNSAFE = (Unsafe) unsafeField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (unsafeField != null) {
                unsafeField.setAccessible(false);
            }
        }
    }

    /**
     * <p>获取对象在所属类中所声明的指定字段的值</p>
     *
     * @param object    字段所属对象
     * @param fieldName 字段名
     * @return 字段值
     * @throws NoSuchFieldException 字段不存在时抛出
     */
    public static Object getValue(Object object, String fieldName) throws NoSuchFieldException {
        Assert.notNull(UNSAFE, "UNSAFE must not be null");
        Assert.notNull(object, "object must not be null");
        Assert.hasText(fieldName, "fieldName must be a valid string");

        return getValue(object, object.getClass().getDeclaredField(fieldName));
    }

    /**
     * <p>获取对象在指定类中所声明的指定字段的值</p>
     *
     * @param object    字段所属对象
     * @param clazz     字段所属类
     * @param fieldName 字段名
     * @return 字段值
     * @throws NoSuchFieldException 字段不存在时抛出
     */
    public static Object getValue(Class<?> clazz, Object object, String fieldName) throws NoSuchFieldException {
        Assert.notNull(UNSAFE, "UNSAFE must not be null");
        Assert.notNull(clazz, "clazz must not be null");
        Assert.notNull(object, "object must not be null");
        Assert.hasText(fieldName, "fieldName must be a valid string");

        return getValue(object, clazz.getDeclaredField(fieldName));
    }

    /**
     * <p>获取类中指定静态字段的值</p>
     *
     * @param clazz     字段所属类
     * @param fieldName 字段名
     * @return 字段值
     * @throws NoSuchFieldException 字段不存在时抛出
     */
    public static Object getStaticValue(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Assert.notNull(UNSAFE, "UNSAFE must not be null");
        Assert.notNull(clazz, "clazz must not be null");
        Assert.hasText(fieldName, "fieldName must be a valid string");

        final Field field = clazz.getDeclaredField(fieldName);
        return getStaticValue(UNSAFE.staticFieldBase(field), field);
    }

    /**
     * <p>获取对象中指定字段的值</p>
     *
     * @param object 字段所属对象
     * @param field  字段
     * @return 字段值
     */
    public static Object getValue(Object object, Field field) {
        Assert.notNull(object, "object must not be null");
        Assert.notNull(field, "field must not be null");

        return getValue(object, field.getType(), UNSAFE.objectFieldOffset(field));
    }

    /**
     * <p>获取指定静态字段的值</p>
     *
     * @param fieldBase 字段基对象，详见 {@link Unsafe#staticFieldBase(Field)}
     * @param field     字段
     * @return 字段值
     */
    public static Object getStaticValue(Object fieldBase, Field field) {
        Assert.notNull(field, "field must not be null");

        return getValue(fieldBase, field.getType(), UNSAFE.staticFieldOffset(field));
    }

    private static Object getValue(Object object, Class<?> fieldType, long fieldOffset) {
        if (byte.class.equals(fieldType)) {
            return UNSAFE.getByte(object, fieldOffset);
        }

        if (short.class.equals(fieldType)) {
            return UNSAFE.getShort(object, fieldOffset);
        }

        if (int.class.equals(fieldType)) {
            return UNSAFE.getInt(object, fieldOffset);
        }

        if (long.class.equals(fieldType)) {
            return UNSAFE.getLong(object, fieldOffset);
        }

        if (float.class.equals(fieldType)) {
            return UNSAFE.getFloat(object, fieldOffset);
        }

        if (double.class.equals(fieldType)) {
            return UNSAFE.getDouble(object, fieldOffset);
        }

        if (char.class.equals(fieldType)) {
            return UNSAFE.getChar(object, fieldOffset);
        }

        if (boolean.class.equals(fieldType)) {
            return UNSAFE.getBoolean(object, fieldOffset);
        }

        return UNSAFE.getObject(object, fieldOffset);
    }

    /**
     * <p>修改对象在所属类中所声明的指定字段的值</p>
     *
     * @param object    字段所属对象
     * @param fieldName 字段名
     * @param newValue 新值
     * @throws NoSuchFieldException 字段不存在时抛出
     */
    public static void putValue(Object object, String fieldName, Object newValue) throws NoSuchFieldException {
        Assert.notNull(UNSAFE, "UNSAFE must not be null");
        Assert.notNull(object, "object must not be null");
        Assert.hasText(fieldName, "fieldName must be a valid string");

        putValue(object, object.getClass().getDeclaredField(fieldName), newValue);
    }

    /**
     * <p>修改对象在指定类中所声明的指定字段的值</p>
     *
     * @param object    字段所属对象
     * @param clazz     字段所属类
     * @param fieldName 字段名
     * @param newValue 新值
     * @throws NoSuchFieldException 字段不存在时抛出
     */
    public static void putValue(Class<?> clazz, Object object, String fieldName, Object newValue) throws NoSuchFieldException {
        Assert.notNull(UNSAFE, "UNSAFE must not be null");
        Assert.notNull(clazz, "clazz must not be null");
        Assert.notNull(object, "object must not be null");
        Assert.hasText(fieldName, "fieldName must be a valid string");

        putValue(object, clazz.getDeclaredField(fieldName), newValue);
    }

    /**
     * <p>修改类中指定静态字段的值</p>
     *
     * @param clazz     字段所属类
     * @param fieldName 字段名
     * @param newValue  新值
     * @throws NoSuchFieldException 字段不存在时抛出
     */
    public static void putStaticValue(Class<?> clazz, String fieldName, Object newValue) throws NoSuchFieldException {
        Assert.notNull(UNSAFE, "UNSAFE must not be null");
        Assert.notNull(clazz, "clazz must not be null");
        Assert.hasText(fieldName, "fieldName must be a valid string");

        final Field field = clazz.getDeclaredField(fieldName);
        putStaticValue(UNSAFE.staticFieldBase(field), field, newValue);
    }

    /**
     * <p>修改对象中指定字段的值</p>
     *
     * @param object   字段所属对象
     * @param field    字段
     * @param newValue 新值
     */
    public static void putValue(Object object, Field field, Object newValue) {
        Assert.notNull(object, "object must not be null");
        Assert.notNull(field, "field must not be null");

        UNSAFE.putObject(object, UNSAFE.objectFieldOffset(field), newValue);
    }

    /**
     * <p>修改指定静态字段的值</p>
     *
     * @param fieldBase 字段基对象，详见 {@link Unsafe#staticFieldBase(Field)}
     * @param field     字段
     * @param newValue  新值
     */
    public static void putStaticValue(Object fieldBase, Field field, Object newValue) {
        Assert.notNull(field, "field must not be null");

        UNSAFE.putObject(fieldBase, UNSAFE.staticFieldOffset(field), newValue);
    }
}
