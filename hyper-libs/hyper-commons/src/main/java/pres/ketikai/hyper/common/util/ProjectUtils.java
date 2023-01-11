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

package pres.ketikai.hyper.common.util;

import org.springframework.util.StringUtils;

/**
 * <p>项目相关工具</p>
 *
 * <p>Created on 2022-12-24 04:28</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ProjectUtils {

    private ProjectUtils() {
    }

    /**
     * <p>将名字中的每个单词首字母大写</p>
     * 以分割符分割每个单词<br>
     * 不会保留分割符
     *
     * @param name      名字
     * @param separator 分割符
     * @return 新名字
     */
    public static String capitalizeName(String name, String separator) {
        Asserts.notNull(name);

        if (separator == null) {
            separator = "";
        }

        if (!name.contains(separator)) {
            return StringUtils.capitalize(name);
        }

        final String[] words = name.split(separator);
        final int nameLen = name.length();
        final int wordLen = words.length;
        StringBuilder sb = new StringBuilder(wordLen < 2 ? nameLen - 1 : nameLen + 1 - wordLen);
        for (String word : words) {
            sb.append(StringUtils.capitalize(word));
        }

        return sb.toString();
    }

    /**
     * <p>将名字中的每个单词首字母小写，并用分割符将它们依次连接</p>
     * 以大写字母分割每个单词<br>
     *
     * @param name      名字
     * @param separator 分割符
     * @return 新名字
     */
    public static String uncapitalizeName(String name, String separator) {
        Asserts.notNull(name);

        final char[] chars = name.toCharArray();
        final int charsLen = chars.length;
        if (charsLen == 0) {
            return name;
        }

        if (separator == null) {
            separator = "";
        }

        final StringBuilder sb = new StringBuilder(charsLen * 2);
        sb.append(Character.toLowerCase(chars[0]));
        char temp;
        for (int i = 1; i < charsLen; i++) {
            temp = chars[i];
            if (64 < temp && temp < 91) {
                sb.append(separator).append(Character.toLowerCase(temp));
            }
        }
        return sb.toString();
    }
}
