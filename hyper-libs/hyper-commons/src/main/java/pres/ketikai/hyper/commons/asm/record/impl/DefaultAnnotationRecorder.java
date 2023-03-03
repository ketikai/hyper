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
import pres.ketikai.hyper.commons.ArrayUtils;
import pres.ketikai.hyper.commons.asm.collect.AnnotationCollector;
import pres.ketikai.hyper.commons.asm.metadata.AnnotationMetadata;
import pres.ketikai.hyper.commons.asm.metadata.impl.DefaultAnnotationMetadata;
import pres.ketikai.hyper.commons.asm.record.MetadataRecorder;

/**
 * <p>默认注解元数据记录器</p>
 *
 * <p>Created on 2023/2/26 18:16</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultAnnotationRecorder extends AnnotationVisitor implements MetadataRecorder<AnnotationMetadata, AnnotationCollector> {

    private AnnotationCollector collector;
    private DefaultAnnotationMetadata metadata;
    private DefaultAnnotationMetadata temp;

    public DefaultAnnotationRecorder(int api, AnnotationVisitor annotationVisitor) {
        super(api, annotationVisitor);
    }

    @Override
    public void visit(String name, Object value) {
        final DefaultAnnotationMetadata.DefaultMethodMetadata methodMetadata = new DefaultAnnotationMetadata.DefaultMethodMetadata();
        methodMetadata.setName(name);
        methodMetadata.setValue(value);
        temp.collect(methodMetadata);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        final DefaultAnnotationMetadata.DefaultMethodMetadata methodMetadata = new DefaultAnnotationMetadata.DefaultMethodMetadata();
        methodMetadata.setName(name);
        final DefaultAnnotationMetadata.DefaultMethodMetadata.DefaultEnumMetadata enumMetadata =
                new DefaultAnnotationMetadata.DefaultMethodMetadata.DefaultEnumMetadata();
        enumMetadata.setDescriptor(descriptor);
        enumMetadata.setValue(value);
        methodMetadata.setValue(enumMetadata);
        temp.collect(methodMetadata);
        super.visitEnum(name, descriptor, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        final DefaultAnnotationMetadata.DefaultMethodMetadata methodMetadata = new DefaultAnnotationMetadata.DefaultMethodMetadata();
        methodMetadata.setName(name);
        final DefaultAnnotationRecorder annotationRecorder = new DefaultAnnotationRecorder(api, super.visitAnnotation(name, descriptor));
        annotationRecorder.init(descriptor, temp.isVisible());
        annotationRecorder.setCollector(methodMetadata);
        temp.collect(methodMetadata);
        return annotationRecorder;
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        final DefaultMethodRecorder methodRecorder = new DefaultMethodRecorder(api, super.visitArray(name));
        methodRecorder.init(name, temp.isVisible());
        methodRecorder.setCollector(temp);
        return methodRecorder;
    }

    @Override
    public void visitEnd() {
        metadata = temp;
        temp = null;
        if (collector != null) {
            collector.collect(metadata);
            setCollector(null);
        }
        super.visitEnd();
    }

    public void init(String descriptor, boolean visible) {
        temp = new DefaultAnnotationMetadata();
        temp.setDescriptor(descriptor);
        temp.setVisible(visible);
    }

    @Override
    public void setCollector(AnnotationCollector collector) {
        this.collector = collector;
    }

    @Override
    public AnnotationMetadata getMetadata() {
        return metadata;
    }

    /**
     * <p>默认注解方法元数据记录器</p>
     *
     * <p>Created on 2023/2/26 18:16</p>
     *
     * @author ketikai
     * @version 0.0.1
     * @since 0.0.1
     */
    public static class DefaultMethodRecorder extends AnnotationVisitor implements MetadataRecorder<AnnotationMetadata.MethodMetadata, AnnotationCollector.MethodCollector> {

        private AnnotationCollector.MethodCollector collector;
        private DefaultAnnotationMetadata.DefaultMethodMetadata metadata;
        private DefaultAnnotationMetadata.DefaultMethodMetadata temp;
        private DefaultAnnotationMetadata.DefaultMethodMetadata.DefaultArrayMetadata arrayMetadata;
        private boolean visible;

        public DefaultMethodRecorder(int api, AnnotationVisitor annotationVisitor) {
            super(api, annotationVisitor);
        }

        @Override
        public void visit(String name, Object value) {
            arrayMetadata.setArray(ArrayUtils.copyAndAdd(arrayMetadata.getArray(), value));
            super.visit(name, value);
        }

        @Override
        public void visitEnum(String name, String descriptor, String value) {
            final DefaultAnnotationMetadata.DefaultMethodMetadata.DefaultEnumMetadata enumMetadata =
                    new DefaultAnnotationMetadata.DefaultMethodMetadata.DefaultEnumMetadata();
            enumMetadata.setDescriptor(descriptor);
            enumMetadata.setValue(value);
            arrayMetadata.setArray(ArrayUtils.copyAndAdd(arrayMetadata.getArray(), enumMetadata));
            super.visitEnum(name, descriptor, value);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String name, String descriptor) {
            final DefaultAnnotationRecorder annotationRecorder = new DefaultAnnotationRecorder(api, super.visitAnnotation(name, descriptor));
            annotationRecorder.init(descriptor, visible);
            annotationRecorder.setCollector(arrayMetadata);
            return annotationRecorder;
        }

        @Override
        public void visitEnd() {
            temp.setValue(arrayMetadata);
            arrayMetadata = null;
            metadata = temp;
            temp = null;
            visible = false;
            if (collector != null) {
                collector.collect(metadata);
                setCollector(null);
            }
            super.visitEnd();
        }

        public void init(String name, boolean visible) {
            temp = new DefaultAnnotationMetadata.DefaultMethodMetadata();
            temp.setName(name);
            arrayMetadata = new DefaultAnnotationMetadata.DefaultMethodMetadata.DefaultArrayMetadata();

            this.visible = visible;
        }

        @Override
        public void setCollector(AnnotationCollector.MethodCollector collector) {
            this.collector = collector;
        }

        @Override
        public AnnotationMetadata.MethodMetadata getMetadata() {
            return metadata;
        }
    }
}
