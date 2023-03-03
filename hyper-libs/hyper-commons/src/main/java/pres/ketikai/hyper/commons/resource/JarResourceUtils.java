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

import pres.ketikai.hyper.commons.resource.exception.FileNotCreatedException;
import pres.ketikai.hyper.commons.resource.exception.FileNotDeletedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>Jar 包内资源相关工具</p>
 *
 * <p>Created on 2023/3/1 20:38</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class JarResourceUtils {

    private static final String RESOURCES_DIR = "resources/";

    /**
     * 解压指定 Jar 包内路径 {@link JarResourceUtils#RESOURCES_DIR} 下的所有内容到目标目录下<br>
     * 此方法不会替换目标目录下已有内容
     *
     * @param sourceJar 源 Jar 包
     * @param targetDir 目标目录
     * @throws IOException 资源操作异常时抛出，详见方法体逻辑
     */
    public static void save(String sourceJar, File targetDir) throws IOException {
        save(sourceJar, targetDir, false);
    }

    /**
     * 解压指定 Jar 包内路径 {@link JarResourceUtils#RESOURCES_DIR} 下的所有内容到目标目录下
     *
     * @param sourceJar 源 Jar 包
     * @param targetDir 目标目录
     * @param replace   是否替换已有内容
     * @throws IOException 资源操作异常时抛出，详见方法体逻辑
     */
    public static void save(String sourceJar, File targetDir, boolean replace) throws IOException {
        final String targetDirAbsolutePath = targetDir.getAbsolutePath();
        if (targetDir.exists()) {
            if (targetDir.isFile() && !targetDir.delete()) {
                throw new FileNotDeletedException(targetDirAbsolutePath);
            }
        }
        if (!targetDir.exists() && !targetDir.mkdir()) {
            throw new FileNotCreatedException(targetDirAbsolutePath);
        }

        try (final JarFile jarFile = new JarFile(sourceJar)) {
            final Iterator<JarEntry> iterator = jarFile.stream().iterator();
            JarEntry jarEntry;
            String entryName;
            File file;
            boolean fileIsExists;
            boolean fileIsDirectory;
            boolean entryIsDirectory;
            String fileAbsolutePath;
            while (iterator.hasNext()) {
                jarEntry = iterator.next();
                entryName = jarEntry.getName();
                if (!entryName.startsWith(RESOURCES_DIR) || RESOURCES_DIR.equals(entryName)) {
                    continue;
                }

                file = new File(targetDir, entryName.substring(RESOURCES_DIR.length()));
                fileIsExists = file.exists();
                fileIsDirectory = file.isDirectory();
                entryIsDirectory = jarEntry.isDirectory();
                fileAbsolutePath = file.getAbsolutePath();
                if (fileIsExists) {
                    if (fileIsDirectory != entryIsDirectory && !file.delete()) {
                        throw new FileNotDeletedException(fileAbsolutePath);
                    }

                    if (!fileIsDirectory && !entryIsDirectory) {
                        if (replace) {
                            try (InputStream is = jarFile.getInputStream(jarEntry)) {
                                Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                        continue;
                    }

                    if (fileIsDirectory && entryIsDirectory && !file.exists() && !file.mkdir()) {
                        throw new FileNotCreatedException(fileAbsolutePath);
                    }
                } else {
                    if (entryIsDirectory) {
                        if (!file.mkdir()) {
                            throw new FileNotCreatedException(fileAbsolutePath);
                        }
                    } else {
                        try (InputStream is = jarFile.getInputStream(jarEntry)) {
                            Files.copy(is, file.toPath());
                        }
                    }
                }
            }
        }
    }
}
