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

package pres.ketikai.hyper.api.role;

import java.util.Arrays;

/**
 * <p>无授权异常</p>
 *
 * <p>Created on 2023/3/4 23:59</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class UnauthorizedExecption extends RuntimeException {

    private final IAuthority[] authorities;

    /**
     * @param authorities 缺少的权限
     */
    public UnauthorizedExecption(IAuthority... authorities) {
        super(authorities.length == 0 ? "unauthorized" : "unauthorized " + Arrays.toString(authorities));
        this.authorities = authorities;
    }

    /**
     * 获取缺少的权限
     *
     * @return 权限
     */
    public IAuthority[] getAuthorities() {
        return authorities;
    }
}