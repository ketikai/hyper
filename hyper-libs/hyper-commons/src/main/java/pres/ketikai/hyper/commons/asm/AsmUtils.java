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

package pres.ketikai.hyper.commons.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import pres.ketikai.hyper.commons.ArrayUtils;
import pres.ketikai.hyper.commons.asm.metadata.AnnotationMetadata;
import pres.ketikai.hyper.commons.asm.metadata.ClassMetadata;
import pres.ketikai.hyper.commons.asm.metadata.FieldMetadata;
import pres.ketikai.hyper.commons.asm.metadata.MethodMetadata;
import pres.ketikai.hyper.commons.asm.record.impl.DefaultClassRecorder;
import pres.ketikai.hyper.commons.asserts.Asserts;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>ASM 相关工具</p>
 *
 * <p>Created on 2023/3/2 20:00</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class AsmUtils {

    /**
     * 获取一个类的元数据信息
     *
     * @param className      全类名
     * @param api            ASM API ，详见 {@link Opcodes}
     * @param parsingOptions 类读取选项，详见 {@link ClassReader}
     * @return 类元数据
     * @throws IOException 详见，{@link ClassLoader#getResourceAsStream(String)}
     */
    public static ClassMetadata getClassMetadata(String className, int api, int parsingOptions) throws IOException {
        Asserts.hasText(className, "className must be a valid text");

        final ClassReader reader = new ClassReader(className);
        final DefaultClassRecorder recorder = new DefaultClassRecorder(api);
        reader.accept(recorder, parsingOptions);
        return recorder.getMetadata();
    }

    /**
     * 获取一个类文件的元数据信息<br>
     * 该操作不会自动关闭流，请自行手动关闭
     *
     * @param inputStream    类文件输入流
     * @param api            ASM API ，详见 {@link Opcodes}
     * @param parsingOptions 类读取选项，详见 {@link ClassReader}
     * @return 类元数据
     * @throws IOException 详见，{@link InputStream#readAllBytes()}
     */
    public static ClassMetadata getClassMetadata(InputStream inputStream, int api, int parsingOptions) throws IOException {
        Asserts.notNull(inputStream, "inputStream must not be null");

        final byte[] classFile = inputStream.readAllBytes();
        return getClassMetadata(classFile, api, parsingOptions);
    }

    /**
     * 获取一个类文件的元数据信息
     *
     * @param classFile      类文件字节数组
     * @param api            ASM API ，详见 {@link Opcodes}
     * @param parsingOptions 类读取选项，详见 {@link ClassReader}
     * @return 类元数据
     */
    public static ClassMetadata getClassMetadata(byte[] classFile, int api, int parsingOptions) {
        Asserts.isTrue(ArrayUtils.isNullOrEmpty(classFile), "classFile must not be null or empty");

        final ClassReader reader = new ClassReader(classFile);
        final DefaultClassRecorder recorder = new DefaultClassRecorder(api);
        reader.accept(recorder, parsingOptions);
        return recorder.getMetadata();
    }

    /**
     * 判断类元数据中是否包含指定字段元数据
     *
     * @param classMetadata 类元数据
     * @param fieldName     字段名
     * @return 有则返回该字段元数据，反之则为 null
     */
    public static FieldMetadata hasField(ClassMetadata classMetadata, String fieldName) {
        Asserts.notNull(classMetadata, "classMetadata must not be null");
        Asserts.hasText(fieldName, "fieldName must be a valid text");

        final FieldMetadata[] fields = classMetadata.getFields();
        if (ArrayUtils.isNullOrEmpty(fields)) {
            return null;
        }
        for (final FieldMetadata metadata : fields) {
            if (fieldName.equals(metadata.getName())) {
                return metadata;
            }
        }
        return null;
    }

    /**
     * 判断类元数据中是否包含指定方法元数据
     *
     * @param classMetadata 类元数据
     * @param methodName    字段名
     * @param returnType    方法返回值类型
     * @param argumentTypes 方法形参类型
     * @return 有则返回该方法元数据，反之则为 null
     * @see Type#getType(Class)
     * @see Type#getType(String)
     */
    public static MethodMetadata hasMethod(ClassMetadata classMetadata, String methodName, Type returnType, Type... argumentTypes) {
        Asserts.notNull(classMetadata, "classMetadata must not be null");
        Asserts.hasText(methodName, "methodName must be a valid text");
        Asserts.notNull(returnType, "returnType must not be null");
        Asserts.notNull(argumentTypes, "argumentTypes must not be null");

        final MethodMetadata[] methods = classMetadata.getMethods();
        if (ArrayUtils.isNullOrEmpty(methods)) {
            return null;
        }

        final String descriptor = Type.getMethodDescriptor(returnType, argumentTypes);
        for (final MethodMetadata metadata : methods) {
            if (methodName.equals(metadata.getName()) && descriptor.equals(metadata.getDescriptor())) {
                return metadata;
            }
        }
        return null;
    }

    /**
     * 判断类元数据中是否包含指定注解元数据
     *
     * @param classMetadata  类元数据
     * @param annotationType 注解类型
     * @return 有则返回该注解元数据，反之则为 null
     * @see Type#getDescriptor(Class)
     */
    public static AnnotationMetadata hasAnnotation(ClassMetadata classMetadata, String annotationType) {
        Asserts.notNull(classMetadata, "classMetadata must not be null");
        Asserts.hasText(annotationType, "annotationType must be a valid text");

        final AnnotationMetadata[] annotations = classMetadata.getAnnotations();
        return hasAnnotation(annotations, annotationType);
    }

    /**
     * 判断字段元数据中是否包含指定注解元数据
     *
     * @param fieldMetadata  字段元数据
     * @param annotationType 注解类型
     * @return 有则返回该注解元数据，反之则为 null
     * @see Type#getDescriptor(Class)
     */
    public static AnnotationMetadata hasAnnotation(FieldMetadata fieldMetadata, String annotationType) {
        Asserts.notNull(fieldMetadata, "fieldMetadata must not be null");
        Asserts.hasText(annotationType, "annotationType must be a valid text");

        final AnnotationMetadata[] annotations = fieldMetadata.getAnnotations();
        return hasAnnotation(annotations, annotationType);
    }

    /**
     * 判断方法元数据中是否包含指定注解元数据
     *
     * @param methodMetadata 方法元数据
     * @param annotationType 注解类型
     * @return 有则返回该注解元数据，反之则为 null
     * @see Type#getDescriptor(Class)
     */
    public static AnnotationMetadata hasAnnotation(MethodMetadata methodMetadata, String annotationType) {
        Asserts.notNull(methodMetadata, "methodMetadata must not be null");
        Asserts.hasText(annotationType, "annotationType must be a valid text");

        final AnnotationMetadata[] annotations = methodMetadata.getAnnotations();
        return hasAnnotation(annotations, annotationType);
    }

    /**
     * 判断注解元数据组中是否包含指定注解元数据
     *
     * @param annotations    注解元数据组
     * @param annotationType 注解类型
     * @return 有则返回该注解元数据，反之则为 null
     * @see Type#getDescriptor(Class)
     */
    public static AnnotationMetadata hasAnnotation(AnnotationMetadata[] annotations, String annotationType) {
        Asserts.hasText(annotationType, "annotationType must be a valid text");

        if (ArrayUtils.isNullOrEmpty(annotations)) {
            return null;
        }

        AnnotationMetadata.MethodMetadata[] methods;
        Object[] array;
        for (final AnnotationMetadata metadata : annotations) {
            if (annotationType.equals(metadata.getDescriptor())) {
                return metadata;
            }
            methods = metadata.getMethods();
            if (ArrayUtils.isNullOrEmpty(methods)) {
                continue;
            }
            for (final AnnotationMetadata.MethodMetadata method : methods) {
                if (!"value".equals(method.getName())) {
                    continue;
                }

                if (method.getValue() instanceof AnnotationMetadata.MethodMetadata.ArrayMetadata that) {
                    array = that.getArray();
                    if (ArrayUtils.isNullOrEmpty(array)) {
                        continue;
                    }

                    if (array[0] instanceof AnnotationMetadata that0) {
                        if (annotationType.equals(that0.getDescriptor())) {
                            return metadata;
                        }
                    }
                }
            }
        }

        return null;
    }
}
