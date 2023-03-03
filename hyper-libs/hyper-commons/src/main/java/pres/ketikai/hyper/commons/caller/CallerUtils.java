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

/**
 * <p>调用者相关工具</p>
 *
 * <p>Created on 2023/2/17 20:15</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@SkipCallerWalk
public abstract class CallerUtils {

    private static final String PRES_KETIKAI_HYPER = "pres.ketikai.hyper";
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * 获取调用者所属类
     *
     * @return 调用者所属类
     * @see CallerUtils#getCaller()
     */
    public static Class<?> getCallerClass() {
        return getCaller().getCallerClass();
    }

    /**
     * 获取调用者基本信息实体
     *
     * @return 调用者基本信息实体
     * @throws IllegalCallerException 未找到调用者时抛出，此异常通常是在主程序类入口方法内调用该方法时发生
     */
    public static Caller getCaller() {
        return STACK_WALKER.walk(stream -> stream.map(stackFrame -> {
            try {
                return new Caller(
                        stackFrame.getClassName(),
                        stackFrame.getMethodName(),
                        stackFrame.getDeclaringClass(),
                        stackFrame.getMethodType(),
                        stackFrame.isNativeMethod()
                );
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }).filter(caller -> {
            if (!caller.getClassName().startsWith(PRES_KETIKAI_HYPER)) {
                return true;
            }
            return caller.getCallerClass().getAnnotation(SkipCallerWalk.class) == null &&
                    caller.getMethod().getAnnotation(SkipCallerWalk.class) == null;
        }).skip(1).findFirst().orElseThrow(IllegalCallerException::new));
    }

}
