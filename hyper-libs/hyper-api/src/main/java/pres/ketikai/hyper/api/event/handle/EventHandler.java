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

package pres.ketikai.hyper.api.event.handle;

import java.lang.annotation.*;

/**
 * <p>标记事件处理器并提供基本信息</p>
 *
 * <p>Created on 2023/3/4 4:00</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandler {

    /**
     * 监听器的优先级<br>
     * 值越大，优先级越低
     *
     * @return 监听器的优先级
     */
    int priority() default Integer.MAX_VALUE;

    /**
     * 监听器的属性
     *
     * @return 监听器的属性
     */
    Attribute attribute() default Attribute.CANCELABLE;

    /**
     * <p>监听器属性常量</p>
     *
     * <p>Created on 2023/3/4 4:00</p>
     *
     * @author ketikai
     * @version 0.0.1
     * @since 0.0.1
     */
    enum Attribute {

        /**
         * 可取消的
         */
        CANCELABLE,
        /**
         * 不可取消的
         */
        IRREVOCABLE
    }
}
