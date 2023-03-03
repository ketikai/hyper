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

package pres.ketikai.hyper.commons.resource.exception;

import java.io.IOException;
import java.io.Serial;

/**
 * <p>未能完成文件创建时抛出</p>
 *
 * <p>Created on 2023/3/2 1:40</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class FileNotCreatedException extends IOException {

    @Serial
    private static final long serialVersionUID = -8690433120214785684L;

    public FileNotCreatedException(String path) {
        super("failed to delete: " + path);
    }

    public FileNotCreatedException(String path, Throwable cause) {
        super("failed to delete: " + path, cause);
    }
}
