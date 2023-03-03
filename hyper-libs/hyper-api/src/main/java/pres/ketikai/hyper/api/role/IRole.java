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

import java.util.Collection;

/**
 * <p>提供权限拥有者相关接口</p>
 *
 * <p>Created on 2023/3/4 5:19</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public interface IRole {

    /**
     * 添加权限
     *
     * @param authority 权限
     */
    void addAuthority(IAuthority authority);

    /**
     * 添加权限
     *
     * @param authorities 权限集合
     */
    void addAuthorities(Collection<IAuthority> authorities);

    /**
     * 移除权限
     *
     * @param authority 权限
     */
    void removeAuthority(IAuthority authority);

    /**
     * 移除权限
     *
     * @param authorities 权限集合
     */
    void removeAuthorities(Collection<IAuthority> authorities);

    /**
     * 判断是否拥有权限
     *
     * @param authority 权限
     * @return 是否拥有权限
     */
    boolean hasAuthority(IAuthority authority);

    /**
     * 判断是否拥有权限
     *
     * @param authorities 权限集合
     * @return 是否拥有权限
     */
    boolean hasAuthorities(Collection<IAuthority> authorities);

    /**
     * 获取拥有的所有权限
     *
     * @return 拥有的所有权限
     */
    IAuthority[] getAuthorities();
}
