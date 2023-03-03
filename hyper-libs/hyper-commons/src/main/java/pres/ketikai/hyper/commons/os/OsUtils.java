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

package pres.ketikai.hyper.commons.os;

import java.util.Locale;

/**
 * <p>操作系统相关工具</p>
 *
 * <p>Created on 2023/2/24 16:16</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class OsUtils {

    private static final String NAME = System.getProperty("os.name").toUpperCase(Locale.ROOT);
    private static final String WINDOWS = "WINDOWS";
    private static final String LINUX = "LINUX";

    /**
     * 判断是否为 windows 系统
     *
     * @return 是否为 windows 系统
     */
    public static boolean isWindows() {
        return NAME.startsWith(WINDOWS);
    }

    /**
     * 判断是否为 linux 系统
     *
     * @return 是否为 linux 系统
     */
    public static boolean isLinux() {
        return NAME.startsWith(LINUX);
    }

    /**
     * 获取系统名称
     *
     * @return 系统名
     */
    public static String getName() {
        return NAME;
    }
}
