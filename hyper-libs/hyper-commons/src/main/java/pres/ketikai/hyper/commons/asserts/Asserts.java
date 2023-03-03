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

package pres.ketikai.hyper.commons.asserts;

import pres.ketikai.hyper.commons.StringUtils;

import java.util.Collection;

/**
 * <p>断言相关工具</p>
 *
 * <p>Created on 2023/2/16 19:50</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class Asserts {

    /**
     * 断言表达式的结果为 true
     *
     * @param expr 表达式
     * @param msg  异常信息
     */
    public static void isTrue(Boolean expr, String... msg) {
        if (!Boolean.TRUE.equals(expr)) {
            throwEx(msg);
        }
    }

    /**
     * 断言对象为空
     *
     * @param obj 对象
     * @param msg 异常信息
     */
    public static void isNull(Object obj, String... msg) {
        isTrue(obj == null, msg);
    }

    /**
     * 断言对象非空
     *
     * @param obj 对象
     * @param msg 异常信息
     */
    public static void notNull(Object obj, String... msg) {
        isTrue(obj != null, msg);
    }

    /**
     * 断言集合为空
     *
     * @param coll 集合
     * @param msg  异常信息
     */
    public static void isEmpty(Collection<Object> coll, String... msg) {
        isTrue(coll == null || coll.isEmpty(), msg);
    }

    /**
     * 断言集合非空
     *
     * @param coll 集合
     * @param msg  异常信息
     */
    public static void notEmpty(Collection<Object> coll, String... msg) {
        isTrue(coll != null && !coll.isEmpty(), msg);
    }

    /**
     * 断言字符串有文本内容（不包括空白字符）
     *
     * @param str 字符
     * @param msg 异常信息
     */
    public static void hasText(String str, String... msg) {
        isTrue(!StringUtils.isNullOrBlank(str), msg);
    }

    private static void throwEx(String... msg) {
        final int msgLength = msg.length;
        if (msgLength == 0) {
            throw new IllegalArgumentException();
        }
        if (msgLength == 1) {
            throw new IllegalArgumentException(String.valueOf(msg[0]));
        }

        final StringBuilder sb = new StringBuilder(64);
        for (final String message : msg) {
            sb.append(message);
        }
        throw new IllegalArgumentException(sb.toString());
    }
}
