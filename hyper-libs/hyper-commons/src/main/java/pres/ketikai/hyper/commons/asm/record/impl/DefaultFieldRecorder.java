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
import org.objectweb.asm.FieldVisitor;
import pres.ketikai.hyper.commons.asm.collect.FieldCollector;
import pres.ketikai.hyper.commons.asm.metadata.FieldMetadata;
import pres.ketikai.hyper.commons.asm.metadata.impl.DefaultFieldMetadata;
import pres.ketikai.hyper.commons.asm.record.MetadataRecorder;

/**
 * <p>默认字段元数据记录器</p>
 *
 * <p>Created on 2023/2/26 16:41</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultFieldRecorder extends FieldVisitor implements MetadataRecorder<FieldMetadata, FieldCollector> {

    private FieldCollector collector;
    private DefaultFieldMetadata metadata;
    private DefaultFieldMetadata temp;

    public DefaultFieldRecorder(int api, FieldVisitor fieldVisitor) {
        super(api, fieldVisitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        final DefaultAnnotationRecorder annotationRecorder = new DefaultAnnotationRecorder(api, super.visitAnnotation(descriptor, visible));
        annotationRecorder.init(descriptor, visible);
        annotationRecorder.setCollector(temp);
        return annotationRecorder;
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

    public void init(int access, String name, String descriptor, String signature, Object value) {
        temp = new DefaultFieldMetadata();
        temp.setAccess(access);
        temp.setName(name);
        temp.setDescriptor(descriptor);
        temp.setSignature(signature);
        temp.setValue(value);
    }

    @Override
    public void setCollector(FieldCollector collector) {
        this.collector = collector;
    }

    @Override
    public FieldMetadata getMetadata() {
        return metadata;
    }
}
