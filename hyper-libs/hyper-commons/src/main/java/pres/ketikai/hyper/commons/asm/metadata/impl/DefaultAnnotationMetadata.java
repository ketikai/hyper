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

package pres.ketikai.hyper.commons.asm.metadata.impl;

import pres.ketikai.hyper.commons.ArrayUtils;
import pres.ketikai.hyper.commons.asm.collect.AnnotationCollector;
import pres.ketikai.hyper.commons.asm.metadata.AnnotationMetadata;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>默认注解元数据</p>
 *
 * <p>Created on 2023/2/26 16:20</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultAnnotationMetadata implements AnnotationMetadata, Serializable, AnnotationCollector.MethodCollector {

    @Serial
    private static final long serialVersionUID = -2465349168156920230L;
    private String descriptor;
    private boolean visible;
    private MethodMetadata[] methods;

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public MethodMetadata[] getMethods() {
        return methods;
    }

    @Override
    public void collect(MethodMetadata metadata) {
        this.methods = ArrayUtils.copyAndAdd(getMethods(), metadata);
    }


    /**
     * <p>默认注解方法元数据</p>
     *
     * <p>Created on 2023/2/26 16:20</p>
     *
     * @author ketikai
     * @version 0.0.1
     * @since 0.0.1
     */
    public static class DefaultMethodMetadata implements MethodMetadata, Serializable, AnnotationCollector, ArrayCollector {

        @Serial
        private static final long serialVersionUID = -2702037833223771540L;
        private String name;
        private Object value;

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public void collect(AnnotationMetadata metadata) {
            this.value = metadata;
        }

        @Override
        public void collect(ArrayMetadata metadata) {
            this.value = metadata;
        }


        /**
         * <p>默认注解方法枚举值元数据</p>
         *
         * <p>Created on 2023/2/26 16:20</p>
         *
         * @author ketikai
         * @version 0.0.1
         * @since 0.0.1
         */
        public static class DefaultEnumMetadata implements EnumMetadata, Serializable {

            @Serial
            private static final long serialVersionUID = -4056333554344737670L;
            private String descriptor;
            private String value;

            @Override
            public String getDescriptor() {
                return descriptor;
            }

            public void setDescriptor(String descriptor) {
                this.descriptor = descriptor;
            }

            @Override
            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }


        /**
         * <p>默认注解方法数组值元数据</p>
         *
         * <p>Created on 2023/2/26 16:20</p>
         *
         * @author ketikai
         * @version 0.0.1
         * @since 0.0.1
         */
        public static class DefaultArrayMetadata implements ArrayMetadata, Serializable, AnnotationCollector {

            @Serial
            private static final long serialVersionUID = 6023398817707731194L;
            private Object[] array;

            @Override
            public Object[] getArray() {
                return array;
            }

            public void setArray(Object[] array) {
                this.array = array;
            }

            @Override
            public void collect(AnnotationMetadata metadata) {
                this.array = ArrayUtils.copyAndAdd(getArray(), metadata);
            }
        }
    }
}
