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

package pres.ketikai.hyper.commons.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import pres.ketikai.hyper.commons.asserts.Asserts;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>Json 相关工具</p>
 *
 * <p>Created on 2022-11-21 11:33</p>
 *
 * @author ketikai
 * @version 0.0.2
 * @since 0.0.1
 */
public abstract class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER;

    static {
        final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        prettyPrinter.indentObjectsWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        WRITER = MAPPER.writer(prettyPrinter);
    }

    /**
     * 将对象序列化为 Json 字符串
     *
     * @param object 待序列化的对象
     * @return json 字符串
     */
    public static String toJson(Object object) throws JsonProcessingException {
        return toJson(object, null);
    }

    /**
     * 将对象序列化为 Json 字符串
     *
     * @param object 待序列化的对象
     * @return json 字符串
     */
    public static String toJson(Object object, ObjectMapper objectMapper) throws JsonProcessingException {
        return selectMapper(objectMapper).writeValueAsString(object);
    }

    /**
     * 将对象序列化为格式化 Json 字符串
     *
     * @param object 待序列化的对象
     * @return json 字符串
     */
    public static String toFormatJson(Object object) throws JsonProcessingException {
        return toFormatJson(object, WRITER);
    }

    /**
     * 将对象序列化为格式化 Json 字符串
     *
     * @param object 待序列化的对象
     * @return json 字符串
     */
    public static String toFormatJson(Object object, ObjectWriter objectWriter) throws JsonProcessingException {
        return selectWriter(objectWriter).writeValueAsString(object);
    }

    /**
     * 将 Json 字符串反序列化为对象<br>
     * 该操作不会自动关闭流，请自行手动关闭
     *
     * @param inputStream 待反序列化的 Json 输入流
     * @param beanType    目标的类型
     * @param <T>         目标类型
     * @return 目标类型对象
     */
    public static <T> T toBean(InputStream inputStream, Class<T> beanType) throws IOException {
        Asserts.notNull(inputStream, "inputStream must not be null");

        final byte[] bytes = inputStream.readAllBytes();
        final String jsonStr = new String(bytes, 0, bytes.length);
        return toBean(jsonStr, beanType);
    }

    /**
     * 将 Json 字符串反序列化为对象
     *
     * @param jsonStr  待反序列化的 Json 字符串
     * @param beanType 目标的类型
     * @param <T>      目标类型
     * @return 目标类型对象
     */
    public static <T> T toBean(String jsonStr, Class<T> beanType) throws JsonProcessingException {
        return toBean(jsonStr, beanType, null);
    }

    /**
     * 将 Json 字符串反序列化为对象
     *
     * @param jsonStr  待反序列化的 Json 字符串
     * @param beanType 目标的类型
     * @param <T>      目标类型
     * @return 目标类型对象
     */
    public static <T> T toBean(String jsonStr, Class<T> beanType, ObjectMapper objectMapper) throws JsonProcessingException {
        return selectMapper(objectMapper).readValue(jsonStr, beanType);
    }

    /**
     * 将 Json 解析器反序列化为对象
     *
     * @param jsonParser 待反序列化的 Json 解析器
     * @param beanType   目标的类型
     * @param <T>        目标类型
     * @return 目标类型对象
     */
    public static <T> T toBean(JsonParser jsonParser, Class<T> beanType) throws IOException {
        return toBean(jsonParser, beanType, null);
    }

    /**
     * 将 Json 解析器反序列化为对象
     *
     * @param jsonParser 待反序列化的 Json 解析器
     * @param beanType   目标的类型
     * @param <T>        目标类型
     * @return 目标类型对象
     */
    public static <T> T toBean(JsonParser jsonParser, Class<T> beanType, ObjectMapper objectMapper) throws IOException {
        return selectMapper(objectMapper).readValue(jsonParser, beanType);
    }

    /**
     * 将 Json 字符串反序列化为对象<br>
     * 该操作不会自动关闭流，请自行手动关闭
     *
     * @param inputStream   待反序列化的 Json 输入流
     * @param typeReference 目标的引用类型
     * @param <T>           目标类型
     * @return 目标类型对象
     */
    public static <T> T toBean(InputStream inputStream, TypeReference<T> typeReference) throws IOException {
        Asserts.notNull(inputStream, "inputStream must not be null");

        final byte[] bytes = inputStream.readAllBytes();
        final String jsonStr = new String(bytes, 0, bytes.length);
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
    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference) throws JsonProcessingException {
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
    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference, ObjectMapper objectMapper) throws JsonProcessingException {
        return selectMapper(objectMapper).readValue(jsonStr, typeReference);
    }

    /**
     * 将 Json 解析器反序列化为对象
     *
     * @param jsonParser    待反序列化的 Json 解析器
     * @param typeReference 目标的引用类型
     * @param <T>           目标类型
     * @return 目标类型对象
     */
    public static <T> T toBean(JsonParser jsonParser, TypeReference<T> typeReference) throws IOException {
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
    public static <T> T toBean(JsonParser jsonParser, TypeReference<T> typeReference, ObjectMapper objectMapper) throws IOException {
        return selectMapper(objectMapper).readValue(jsonParser, typeReference);
    }

    /**
     * 将 Json 字符串反序列化为 Array
     *
     * @param jsonStr  待反序列化的 Json 字符串
     * @param beanType 目标的类型
     * @param <T>      目标类型
     * @return 目标类型 T[]
     */
    public static <T> T[] toArray(String jsonStr, Class<T> beanType) {
        return toArray(jsonStr, beanType, null);
    }

    /**
     * 将 Json 字符串反序列化为 Array
     *
     * @param jsonStr  待反序列化的 Json 字符串
     * @param beanType 目标的类型
     * @param <T>      目标类型
     * @return 目标类型 T[]
     */
    public static <T> T[] toArray(String jsonStr, Class<T> beanType, ObjectMapper objectMapper) {
        objectMapper = selectMapper(objectMapper);
        JavaType javaType = objectMapper.getTypeFactory().constructArrayType(beanType);
        return toJavaType(jsonStr, javaType, objectMapper);
    }

    /**
     * 将 Json 字符串反序列化为 List
     *
     * @param jsonStr  待反序列化的 Json 字符串
     * @param beanType 目标的类型
     * @param <T>      目标类型
     * @return 目标类型 List<T>
     */
    public static <T> List<T> toList(String jsonStr, Class<T> beanType) {
        return toList(jsonStr, beanType, null);
    }

    /**
     * 将 Json 字符串反序列化为 List
     *
     * @param jsonStr  待反序列化的 Json 字符串
     * @param beanType 目标的类型
     * @param <T>      目标类型
     * @return 目标类型 List<T>
     */
    public static <T> List<T> toList(String jsonStr, Class<T> beanType, ObjectMapper objectMapper) {
        objectMapper = selectMapper(objectMapper);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, beanType);
        return toJavaType(jsonStr, javaType, objectMapper);
    }

    /**
     * 将 Json 字符串反序列化为 Set
     *
     * @param jsonStr  待反序列化的 Json 字符串
     * @param beanType 目标的类型
     * @param <T>      目标类型
     * @return 目标类型 Set<T>
     */
    public static <T> Set<T> toSet(String jsonStr, Class<T> beanType) {
        return toSet(jsonStr, beanType, null);
    }

    /**
     * 将 Json 字符串反序列化为 Set
     *
     * @param jsonStr  待反序列化的 Json 字符串
     * @param beanType 目标的类型
     * @param <T>      目标类型
     * @return 目标类型 Set<T>
     */
    public static <T> Set<T> toSet(String jsonStr, Class<T> beanType, ObjectMapper objectMapper) {
        objectMapper = selectMapper(objectMapper);
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Set.class, beanType);
        return toJavaType(jsonStr, javaType, objectMapper);
    }

    /**
     * 将 Json 字符串反序列化为 Map
     *
     * @param jsonStr   待反序列化的 Json 字符串
     * @param keyType   键的类型
     * @param valueType 值的类型
     * @param <K>       键泛型
     * @param <V>       值泛型
     * @return 目标类型 Map<K, V>
     */
    public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> keyType, Class<V> valueType) {
        return toMap(jsonStr, keyType, valueType, null);
    }

    /**
     * 将 Json 字符串反序列化为 Map
     *
     * @param jsonStr   待反序列化的 Json 字符串
     * @param keyType   键的类型
     * @param valueType 值的类型
     * @param <K>       键泛型
     * @param <V>       值泛型
     * @return 目标类型 Map<K, V>
     */
    public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> keyType, Class<V> valueType, ObjectMapper objectMapper) {
        objectMapper = selectMapper(objectMapper);
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
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
