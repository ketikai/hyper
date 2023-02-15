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

package pres.ketikai.hyper.commons.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>JSON 相关工具</p>
 *
 * <p>Created on 2022-11-21 11:33</p>
 *
 * @author ketikai
 * @version 0.0.2
 * @since 0.0.1
 */
public final class JSONUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER;

    static {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        prettyPrinter.indentObjectsWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        WRITER = MAPPER.writer(prettyPrinter);
    }

    private JSONUtils() {
    }

    /**
     * 将对象序列化为 Json 字符串
     *
     * @param obj 待序列化的对象
     * @return json 字符串
     */

    public static String toJson(Object obj) {
        return toJson(obj, null);
    }

    /**
     * 将对象序列化为 Json 字符串
     *
     * @param obj 待序列化的对象
     * @return json 字符串
     */

    public static String toJson(Object obj, ObjectMapper objectMapper) {
        try {
            return selectMapper(objectMapper).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对象序列化为格式化 Json 字符串
     *
     * @param obj 待序列化的对象
     * @return json 字符串
     */

    public static String toFormatJson(Object obj) {
        return toFormatJson(obj, WRITER);
    }

    /**
     * 将对象序列化为格式化 Json 字符串
     *
     * @param obj 待序列化的对象
     * @return json 字符串
     */

    public static String toFormatJson(Object obj, ObjectWriter objectWriter) {
        try {
            return selectWriter(objectWriter).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 Json 字符串反序列化为对象
     *
     * @param inputStream 待反序列化的 Json 输入流
     * @param clazz       目标类型的 Class 对象
     * @param <T>         目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(InputStream inputStream, Class<T> clazz) throws IOException {
        Assert.notNull(inputStream, "inputStream must not be null");

        byte[] bytes = inputStream.readAllBytes();
        String jsonStr = new String(bytes, 0, bytes.length);
        return toBean(jsonStr, clazz);
    }

    /**
     * 将 Json 字符串反序列化为对象
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param clazz   目标类型的 Class 对象
     * @param <T>     目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        return toBean(jsonStr, clazz, null);
    }

    /**
     * 将 Json 字符串反序列化为对象
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param clazz   目标类型的 Class 对象
     * @param <T>     目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(String jsonStr, Class<T> clazz, ObjectMapper objectMapper) {
        try {
            return selectMapper(objectMapper).readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 Json 解析器反序列化为对象
     *
     * @param jsonParser 待反序列化的 Json 解析器
     * @param clazz      目标类型的 Class 对象
     * @param <T>        目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(JsonParser jsonParser, Class<T> clazz) {
        return toBean(jsonParser, clazz, null);
    }

    /**
     * 将 Json 解析器反序列化为对象
     *
     * @param jsonParser 待反序列化的 Json 解析器
     * @param clazz      目标类型的 Class 对象
     * @param <T>        目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(JsonParser jsonParser, Class<T> clazz, ObjectMapper objectMapper) {
        try {
            return selectMapper(objectMapper).readValue(jsonParser, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 Json 字符串反序列化为对象
     *
     * @param inputStream   待反序列化的 Json 输入流
     * @param typeReference 目标的引用类型
     * @param <T>           目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(InputStream inputStream, TypeReference<T> typeReference) throws IOException {
        Assert.notNull(inputStream, "inputStream must not be null");

        byte[] bytes = inputStream.readAllBytes();
        String jsonStr = new String(bytes, 0, bytes.length);
        return toBean(jsonStr, typeReference, null);
    }

    /**
     * 将 Json 字符串反序列化为对象
     *
     * @param jsonStr       待反序列化的 Json 字符串
     * @param typeReference 目标的引用类型
     * @param <T>           目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference) {
        return toBean(jsonStr, typeReference, null);
    }

    /**
     * 将 Json 字符串反序列化为对象
     *
     * @param jsonStr       待反序列化的 Json 字符串
     * @param typeReference 目标的引用类型
     * @param <T>           目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        try {
            return selectMapper(objectMapper).readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 Json 解析器反序列化为对象
     *
     * @param jsonParser    待反序列化的 Json 解析器
     * @param typeReference 目标的引用类型
     * @param <T>           目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(JsonParser jsonParser, TypeReference<T> typeReference) {
        return toBean(jsonParser, typeReference, null);
    }

    /**
     * 将 Json 解析器反序列化为对象
     *
     * @param jsonParser    待反序列化的 Json 解析器
     * @param typeReference 目标的引用类型
     * @param <T>           目标类型
     * @return 目标类型对象
     */

    public static <T> T toBean(JsonParser jsonParser, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        try {
            return selectMapper(objectMapper).readValue(jsonParser, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 Json 字符串反序列化为 Array
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param clazz   目标类型的 Class 对象
     * @param <T>     目标类型
     * @return 目标类型 T[]
     */

    public static <T> T[] toArray(String jsonStr, Class<T> clazz) {
        return toArray(jsonStr, clazz, null);
    }

    /**
     * 将 Json 字符串反序列化为 Array
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param clazz   目标类型的 Class 对象
     * @param <T>     目标类型
     * @return 目标类型 T[]
     */

    public static <T> T[] toArray(String jsonStr, Class<T> clazz, ObjectMapper objectMapper) {
        objectMapper = selectMapper(objectMapper);
        JavaType javaType = objectMapper.getTypeFactory().constructArrayType(clazz);
        return toJavaType(jsonStr, javaType, objectMapper);
    }

    /**
     * 将 Json 字符串反序列化为 List
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param clazz   目标类型的 Class 对象
     * @param <T>     目标类型
     * @return 目标类型 List<T>
     */

    public static <T> List<T> toList(String jsonStr, Class<T> clazz) {
        return toList(jsonStr, clazz, null);
    }

    /**
     * 将 Json 字符串反序列化为 List
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param clazz   目标类型的 Class 对象
     * @param <T>     目标类型
     * @return 目标类型 List<T>
     */

    public static <T> List<T> toList(String jsonStr, Class<T> clazz, ObjectMapper objectMapper) {
        objectMapper = selectMapper(objectMapper);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return toJavaType(jsonStr, javaType, objectMapper);
    }

    /**
     * 将 Json 字符串反序列化为 Set
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param clazz   目标类型的 Class 对象
     * @param <T>     目标类型
     * @return 目标类型 Set<T>
     */

    public static <T> Set<T> toSet(String jsonStr, Class<T> clazz) {
        return toSet(jsonStr, clazz, null);
    }

    /**
     * 将 Json 字符串反序列化为 Set
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param clazz   目标类型的 Class 对象
     * @param <T>     目标类型
     * @return 目标类型 Set<T>
     */

    public static <T> Set<T> toSet(String jsonStr, Class<T> clazz, ObjectMapper objectMapper) {
        objectMapper = selectMapper(objectMapper);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Set.class, clazz);
        return toJavaType(jsonStr, javaType, objectMapper);
    }

    /**
     * 将 Json 字符串反序列化为 Map
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param kClass  键类型的 Class 对象
     * @param vClass  值类型的 Class 对象
     * @param <K>     键类型
     * @param <V>     值类型
     * @return 目标类型 Map<K, V>
     */

    public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> kClass, Class<V> vClass) {
        return toMap(jsonStr, kClass, vClass, null);
    }

    /**
     * 将 Json 字符串反序列化为 Map
     *
     * @param jsonStr 待反序列化的 Json 字符串
     * @param kClass  键类型的 Class 对象
     * @param vClass  值类型的 Class 对象
     * @param <K>     键类型
     * @param <V>     值类型
     * @return 目标类型 Map<K, V>
     */

    public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> kClass, Class<V> vClass, ObjectMapper objectMapper) {
        objectMapper = selectMapper(objectMapper);
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, kClass, vClass);
        return toJavaType(jsonStr, javaType, objectMapper);
    }


    private static <T> T toJavaType(String jsonStr, JavaType javaType, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(jsonStr, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ObjectMapper selectMapper(ObjectMapper objectMapper) {
        return objectMapper != null ? objectMapper : MAPPER;
    }


    private static ObjectWriter selectWriter(ObjectWriter objectWriter) {
        return objectWriter != null ? objectWriter : WRITER;
    }
}
