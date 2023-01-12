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

package pres.ketikai.hyper.commons.caller;

import java.util.Optional;

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
     * <p>获取调用当前方法的首个调用者</p>
     *
     * @return 调用者
     */
    public static Caller getCaller() {
        return getCaller(1L);
    }

    /**
     * <p>获取调用当前方法的根调用者</p>
     *
     * @return 根调用者
     */
    public static Caller getRootCaller() {
        return getCaller(-1L);
    }

    /**
     * <p>获取调用当前方法的调用者</p>
     * 当 depth 参数为负数或其大于等于当前堆栈最大深度时，depth 的值等价于当前堆栈最大深度 - 1
     *
     * @param depth 调用者在堆栈中的深度
     * @return 调用者
     */
    public static Caller getCaller(final long depth) {
        final StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        final Class<?> clazz = walker.walk(s -> {
            final long size = s.count();
            final Optional<StackWalker.StackFrame> frame = s.skip(depth < 0 || depth >= size ? size - 1 : depth).findFirst();
            return frame.<Class<?>>map(StackWalker.StackFrame::getDeclaringClass).orElse(null);
        });

        if (clazz == null) {
            return null;
        }

        return new Caller(clazz, clazz.getClassLoader());
    }

}
