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
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import pres.ketikai.hyper.commons.ArrayUtils;
import pres.ketikai.hyper.commons.asm.collect.ClassCollector;
import pres.ketikai.hyper.commons.asm.metadata.ClassMetadata;
import pres.ketikai.hyper.commons.asm.metadata.impl.DefaultClassMetadata;
import pres.ketikai.hyper.commons.asm.record.MetadataRecorder;

/**
 * <p>默认类元数据记录器</p>
 *
 * <p>Created on 2023/2/26 16:01</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class DefaultClassRecorder extends ClassVisitor implements MetadataRecorder<ClassMetadata, ClassCollector> {

    private DefaultClassMetadata metadata;
    private DefaultClassMetadata temp;

    public DefaultClassRecorder(int api) {
        this(api, null);
    }

    public DefaultClassRecorder(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        temp = new DefaultClassMetadata();
        temp.setVersion(version);
        temp.setAccess(access);
        temp.setName(name);
        temp.setSignature(signature);
        temp.setSuperName(superName);
        temp.setInterfaces(interfaces);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        temp.setOuter(descriptor);
        super.visitOuterClass(owner, name, descriptor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        final DefaultAnnotationRecorder annotationRecorder = new DefaultAnnotationRecorder(api, super.visitAnnotation(descriptor, visible));
        annotationRecorder.init(descriptor, visible);
        annotationRecorder.setCollector(temp);
        return annotationRecorder;
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        temp.setInners(ArrayUtils.copyAndAdd(temp.getInners(), name));
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        final DefaultFieldRecorder fieldRecorder = new DefaultFieldRecorder(api, super.visitField(access, name, descriptor, signature, value));
        fieldRecorder.init(access, name, descriptor, signature, value);
        fieldRecorder.setCollector(temp);
        return fieldRecorder;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        final DefaultMethodRecorder methodRecorder = new DefaultMethodRecorder(api, super.visitMethod(access, name, descriptor, signature, exceptions));
        methodRecorder.init(access, name, descriptor, signature, exceptions);
        methodRecorder.setCollector(temp);
        return methodRecorder;
    }

    @Override
    public void visitEnd() {
        metadata = temp;
        temp = null;
        super.visitEnd();
    }

    @Override
    public void setCollector(ClassCollector collector) {
        // nothing to do
    }

    @Override
    public ClassMetadata getMetadata() {
        return metadata;
    }
}
