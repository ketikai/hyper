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

/**
 * <p>提供权限相关接口p>
 *
 * <p>Created on 2023/3/4 5:24</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public interface IAuthority {

    /**
     * 获取权限名称
     *
     * @return 权限名称
     */
    String getName();
}
