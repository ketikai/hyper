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

import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * <p>合法性相关工具</p>
 *
 * <p>Created on 2022-11-09 18:01</p>
 *
 * @author ketikai
 * @version 0.0.2
 * @since 0.0.1
 */
public final class Checks {

    private static final String QQ_REGEX = "^[1-9][0-9]{4,12}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9]{1,10}@[a-zA-Z0-9]{1,5}\\.[a-zA-Z0-9]{1,5}$";
    private static final String USERNAME_REGEX = "^[_a-zA-Z0-9]{5,32}$";
    private static final String PASSWORD_REGEX = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\-$@!%*#?&])[A-Za-z\\d\\-$@!%*#?&]{12,72}$";
    private static final String IPV4_REGEX = "(^((2[0-4]\\d.)|(25[0-5].)|(1\\d{2}.)|(\\d{1,2}.))((2[0-5]{2}.)|(1\\d{2}.)|(\\d{1,2}.){2})((1\\d{2})|(2[0-5]{2})|(\\d{1,2})))$";

    private Checks() {
    }

    /**
     * <p>检查 qq 合法性</p>
     *
     * @param qq qq 账号
     * @return 检查结果
     */
    public static boolean isQQ(String qq) {
        return StringUtils.hasText(qq) && Pattern.matches(QQ_REGEX, qq);
    }

    /**
     * <p>检查邮箱合法性</p>
     *
     * @param email 邮箱
     * @return 检查结果
     */
    public static boolean isEmail(String email) {
        return StringUtils.hasText(email) && Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * <p>检查用户名合法性</p>
     *
     * @param username 用户名
     * @return 检查结果
     */
    public static boolean isUsername(String username) {
        return StringUtils.hasText(username) && Pattern.matches(USERNAME_REGEX, username);
    }

    /**
     * <p>检查密码合法性</p>
     *
     * @param password 密码
     * @return 检查结果
     */
    public static boolean isPassword(String password) {
        return StringUtils.hasText(password) && Pattern.matches(PASSWORD_REGEX, password);
    }

    /**
     * <p>检查 IPv4 是否合法</p>
     *
     * @param ip ipv4 地址
     * @return 检查结果
     */
    public static boolean isIPv4(String ip) {
        return StringUtils.hasText(ip) && Pattern.matches(IPV4_REGEX, ip);
    }

    /**
     * <p>检查时间段是否合法</p>
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 检查结果
     */
    public static boolean isTimeSlot(Date start, Date end) {
        Asserts.notNull(start);
        Asserts.notNull(end);

        return end.getTime() >= start.getTime();
    }
}
