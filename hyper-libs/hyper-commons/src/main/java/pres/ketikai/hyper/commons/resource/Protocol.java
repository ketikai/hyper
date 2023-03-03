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

package pres.ketikai.hyper.commons.resource;

/**
 * <p>资源协议常量</p>
 *
 * <p>Created on 2023/2/24 16:38</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public interface Protocol {

    String FILE = "file:";
    String JAR = "jar:";
    String HTTP = "http:";
    String HTTPS = "https:";
    String FTP = "ftp:";
    String JRT = "jrt:";
    String JMOD = "jmod:";
    String MAILTO = "mailto:";
}
