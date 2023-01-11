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

package pres.ketikai.hyper.gradle.util.bukkit.boot.asm.life

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import pres.ketikai.hyper.gradle.util.bukkit.boot.asm.life.method.HyperLifeAdapter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * <p>Hyper Bukkit 插件主类增强访问者</p>
 *
 * <p>Created on 2023/1/5 17:30</p>
 * @author ketikai
 * @since 0.0.1
 * @version 0.0.1
 */
class HyperBukkitVisitor(api: Int, classVisitor: ClassVisitor?) :
    ClassVisitor(api, classVisitor) {

    private var isHyperPlugin = false

    private val eventMap: ConcurrentMap<String, String> = ConcurrentHashMap(5)

    init {
        eventMap.putAll(
            mapOf(
                "<clinit>" to HyperLifeAdapter.PLUGIN_INITIALIZE_EVENT_TYPE,
                "<init>" to HyperLifeAdapter.PLUGIN_CONSTRUCT_EVENT_TYPE,
                "onLoad" to HyperLifeAdapter.PLUGIN_LOAD_EVENT_TYPE,
                "onEnable" to HyperLifeAdapter.PLUGIN_ENABLE_EVENT_TYPE,
                "onDisable" to HyperLifeAdapter.PLUGIN_DISABLE_EVENT_TYPE
            )
        )
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        val publicSuper = Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER
        val publicFinalSuper = Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_SUPER
        val bukkitJavaPluginType = "org/bukkit/plugin/java/JavaPlugin"
        val exception = UnsupportedOperationException(
            "Plugin's main class must extends from \"${bukkitJavaPluginType.replace('/', '.')}\"."
        )
        if (access != publicSuper && access != publicFinalSuper) {
            throw exception
        }
        if (superName != bukkitJavaPluginType) {
            throw exception
        }
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        if (visible && descriptor == "Lpres/ketikai/hyper/manager/annotation/HyperPlugin;") {
            isHyperPlugin = true
        }
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<String?>?
    ): MethodVisitor? {
        if (!isHyperPlugin) {
            throw UnsupportedOperationException(
                "The @HyperPlugin annotation was not found on Plugin's main class."
            )
        }

        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        val isPublicOrStatic = access == Opcodes.ACC_PUBLIC || access == Opcodes.ACC_STATIC
        if (methodVisitor != null && eventMap.containsKey(name) && isPublicOrStatic &&
            descriptor == "()V" && signature == null && exceptions == null
        ) {
            return HyperLifeAdapter(api, methodVisitor, eventMap, access, name, descriptor)
        }
        return methodVisitor
    }

    override fun visitEnd() {
        eventMap.keys.forEach {
            val methodVisitor = visitMethod(
                if (it == "<clinit>") {
                    Opcodes.ACC_STATIC
                } else {
                    Opcodes.ACC_PUBLIC
                },
                it, "()V", null, null
            )
            methodVisitor!!.visitCode()
            methodVisitor.visitInsn(Opcodes.RETURN)
            methodVisitor.visitEnd()
        }
        super.visitEnd()
    }
}