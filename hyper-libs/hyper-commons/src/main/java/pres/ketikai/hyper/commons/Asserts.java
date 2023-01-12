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

package pres.ketikai.hyper.commons;

import java.util.Collection;

/**
 * <p>断言相关工具</p>
 *
 * <p>Created on 2023/1/4 16:36</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Asserts {

    private Asserts() {

    }

    public static void expr(final boolean expr) {
        expr(expr, null);
    }

    public static void expr(final boolean expr, final String msg) {
        if (expr) {
            if (msg == null) {
                throw new AssertionError();
            }
            throw new AssertionError(msg);
        }
    }

    public static void isNull(final Object object) {
        isNull(object, "Object must be null.");
    }

    public static void isNull(final Object object, final String msg) {
        if (object != null) {
            throw new AssertionError(msg);
        }
    }

    public static void notNull(final Object object) {
        notNull(object, "Object must be not null.");
    }

    public static void notNull(final Object object, final String msg) {
        if (object == null) {
            throw new AssertionError(msg);
        }
    }

    public static void isEmpty(final Object[] array) {
        isEmpty(array, "Array must be empty.");
    }

    public static void isEmpty(final Object[] array, final String msg) {
        if (array != null && array.length > 0) {
            throw new AssertionError(msg);
        }
    }

    public static void notEmpty(final Object[] array) {
        notEmpty(array, "Array must be not empty.");
    }

    public static void notEmpty(final Object[] array, final String msg) {
        if (array == null || array.length == 0) {
            throw new AssertionError(msg);
        }
    }

    public static void isEmpty(final Collection<?> collection) {
        isEmpty(collection, "Collection must be empty.");
    }

    public static void isEmpty(final Collection<?> collection, final String msg) {
        if (collection != null && !collection.isEmpty()) {
            throw new AssertionError(msg);
        }
    }

    public static void notEmpty(final Collection<?> collection) {
        notEmpty(collection, "Collection must be not empty.");
    }

    public static void notEmpty(final Collection<?> collection, final String msg) {
        if (collection == null || collection.isEmpty()) {
            throw new AssertionError(msg);
        }
    }
}
