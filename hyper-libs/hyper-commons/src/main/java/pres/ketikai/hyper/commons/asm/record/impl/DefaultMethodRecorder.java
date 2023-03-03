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

package pres.ketikai.hyper.commons.asm.record.impl;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import pres.ketikai.hyper.commons.asm.collect.MethodCollector;
import pres.ketikai.hyper.commons.asm.metadata.MethodMetadata;
import pres.ketikai.hyper.commons.asm.metadata.impl.DefaultMethodMetadata;
import pres.ketikai.hyper.commons.asm.record.MetadataRecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>默认方法元数据记录器</p>
 *
 * <p>Created on 2023/2/26 17:47</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultMethodRecorder extends MethodVisitor implements MetadataRecorder<MethodMetadata, MethodCollector> {

    private final List<DefaultMethodMetadata.DefaultParameterMetadata> parameters = new ArrayList<>(8);
    private MethodCollector collector;
    private DefaultMethodMetadata metadata;
    private DefaultMethodMetadata temp;
    private boolean isStatic;

    public DefaultMethodRecorder(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        final DefaultAnnotationRecorder annotationRecorder = new DefaultAnnotationRecorder(api, super.visitAnnotation(descriptor, visible));
        annotationRecorder.init(descriptor, visible);
        annotationRecorder.setCollector(temp);
        return annotationRecorder;
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        final DefaultMethodMetadata.DefaultParameterMetadata parameterMetadata = new DefaultMethodMetadata.DefaultParameterMetadata();
        parameters.add(parameterMetadata);
        final DefaultAnnotationRecorder annotationRecorder = new DefaultAnnotationRecorder(api, super.visitParameterAnnotation(parameter, descriptor, visible));
        annotationRecorder.init(descriptor, visible);
        annotationRecorder.setCollector(parameterMetadata);
        return annotationRecorder;
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        final int paramIndex;
        if (isStatic) {
            paramIndex = index;
        } else if (index != 0) {
            paramIndex = index - 1;
        } else {
            super.visitLocalVariable(name, descriptor, signature, start, end, index);
            return;
        }

        final DefaultMethodMetadata.DefaultParameterMetadata parameterMetadata;
        if (parameters.size() - 1 < paramIndex) {
            parameterMetadata = new DefaultMethodMetadata.DefaultParameterMetadata();
            parameters.add(parameterMetadata);
        } else {
            parameterMetadata = parameters.get(paramIndex);
        }

        if (parameterMetadata != null) {
            parameterMetadata.setName(name);
            parameterMetadata.setDescriptor(descriptor);
            parameterMetadata.setSignature(signature);
        }

        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public void visitEnd() {
        temp.setParameters(parameters.toArray(DefaultMethodMetadata.DefaultParameterMetadata[]::new));
        parameters.clear();
        metadata = temp;
        temp = null;
        isStatic = false;
        if (collector != null) {
            collector.collect(metadata);
            setCollector(null);
        }
        super.visitEnd();
    }

    public void init(int access, String name, String descriptor, String signature, String[] exceptions) {
        temp = new DefaultMethodMetadata();
        temp.setAccess(access);
        temp.setName(name);
        temp.setDescriptor(descriptor);
        temp.setSignature(signature);
        temp.setExceptions(exceptions);

        isStatic = Opcodes.ACC_STATIC <= access;
    }

    @Override
    public void setCollector(MethodCollector collector) {
        this.collector = collector;
    }

    @Override
    public MethodMetadata getMetadata() {
        return metadata;
    }
}
