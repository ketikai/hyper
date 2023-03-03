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
import pres.ketikai.hyper.commons.asm.metadata.FieldMetadata;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>默认字段元数据</p>
 *
 * <p>Created on 2023/2/26 16:20</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @Override
 * @since 0.0.1
 */
public class DefaultFieldMetadata implements FieldMetadata, Serializable, AnnotationCollector {

    @Serial
    private static final long serialVersionUID = 9104489147483725194L;
    private int access;
    private String name;
    private String descriptor;
    private String signature;
    private Object value;
    private AnnotationMetadata[] annotations;

    @Override
    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public AnnotationMetadata[] getAnnotations() {
        return annotations;
    }

    @Override
    public void collect(AnnotationMetadata metadata) {
        this.annotations = ArrayUtils.copyAndAdd(getAnnotations(), metadata);
    }
}
