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
import pres.ketikai.hyper.commons.asm.collect.FieldCollector;
import pres.ketikai.hyper.commons.asm.collect.MethodCollector;
import pres.ketikai.hyper.commons.asm.metadata.AnnotationMetadata;
import pres.ketikai.hyper.commons.asm.metadata.ClassMetadata;
import pres.ketikai.hyper.commons.asm.metadata.FieldMetadata;
import pres.ketikai.hyper.commons.asm.metadata.MethodMetadata;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>默认类元数据</p>
 *
 * <p>Created on 2023/2/26 16:05</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultClassMetadata implements ClassMetadata, Serializable, AnnotationCollector, FieldCollector, MethodCollector {

    @Serial
    private static final long serialVersionUID = -2227779633238061702L;
    private int version;
    private int access;
    private String name;
    private String signature;
    private String superName;
    private String[] interfaces;
    private AnnotationMetadata[] annotations;
    private FieldMetadata[] fields;
    private MethodMetadata[] methods;
    private String outer;
    private String[] inners;

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

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
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    @Override
    public String[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }

    @Override
    public AnnotationMetadata[] getAnnotations() {
        return annotations;
    }

    @Override
    public void collect(AnnotationMetadata metadata) {
        this.annotations = ArrayUtils.copyAndAdd(getAnnotations(), metadata);
    }

    @Override
    public FieldMetadata[] getFields() {
        return fields;
    }

    @Override
    public void collect(FieldMetadata metadata) {
        this.fields = ArrayUtils.copyAndAdd(getFields(), metadata);
    }

    @Override
    public MethodMetadata[] getMethods() {
        return methods;
    }

    @Override
    public void collect(MethodMetadata metadata) {
        this.methods = ArrayUtils.copyAndAdd(getMethods(), metadata);
    }

    @Override
    public String getOuter() {
        return outer;
    }

    public void setOuter(String outer) {
        this.outer = outer;
    }

    @Override
    public String[] getInners() {
        return inners;
    }

    public void setInners(String[] inners) {
        this.inners = inners;
    }
}
