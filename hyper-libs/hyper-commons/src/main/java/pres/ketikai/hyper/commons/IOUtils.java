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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>IO 流相关工具</p>
 *
 * <p>Created on 2022-12-17 17:20</p>
 *
 * @author ketikai
 * @version 0.0.2
 * @since 0.0.1
 */
public final class IOUtils {

    private IOUtils() {
    }

    /**
     * <p>对象深拷贝</p>
     *
     * @param obj 待复制的对象
     * @param <O> 对象类型
     * @return 复制结果
     */
    @SuppressWarnings({"unchecked"})
    public static <O> O copy(O obj) throws IOException, ClassNotFoundException {
        Asserts.notNull(obj);

        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            return (O) ois.readObject();
        } finally {
            close(ois);
            close(bis);
            close(oos);
            close(bos);
        }
    }

    /**
     * <p>从文件读取字符串数组</p>
     * '\n' 为分割符
     *
     * @param file 文件
     * @return 字符串数组
     */
    public static String[] readStrArr(File file) throws IOException {
        Asserts.notNull(file);

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            List<String> banner = new ArrayList<>();
            String cache;
            while (Objects.nonNull(cache = br.readLine())) {
                banner.add(cache);
            }

            return banner.toArray(new String[]{});
        } finally {
            IOUtils.close(br);
            IOUtils.close(fr);
        }
    }

    /**
     * <p>关闭流</p>
     *
     * @param stream 待关闭流
     */
    public static void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
