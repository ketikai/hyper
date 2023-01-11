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

package pres.ketikai.hyper.gradle.util.bukkit.boot.asm.life.method

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter
import pres.ketikai.hyper.gradle.util.bukkit.boot.asm.BukkitUtilType
import java.util.concurrent.ConcurrentMap

/**
 * <p>Hyper Bukkit 插件主类生命周期方法增强适配器</p>
 *
 * <p>Created on 2023/1/5 18:43</p>
 * @author ketikai
 * @since 0.0.1
 * @version 0.0.1
 */
class HyperLifeAdapter(
    api: Int,
    methodVisitor: MethodVisitor?,
    private val eventMap: ConcurrentMap<String, String>,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(
    api, methodVisitor, access, name, descriptor
) {

    companion object {
        const val PLUGIN_INITIALIZE_EVENT_TYPE = "pres/ketikai/hyper/manager/plugin/event/PluginInitializeEvent"
        const val PLUGIN_CONSTRUCT_EVENT_TYPE = "pres/ketikai/hyper/manager/plugin/event/PluginConstructEvent"
        const val PLUGIN_LOAD_EVENT_TYPE = "pres/ketikai/hyper/manager/plugin/event/PluginLoadEvent"
        const val PLUGIN_ENABLE_EVENT_TYPE = "pres/ketikai/hyper/manager/plugin/event/PluginEnableEvent"
        const val PLUGIN_DISABLE_EVENT_TYPE = "pres/ketikai/hyper/manager/plugin/event/PluginDisableEvent"
    }

    private val isPublic: Boolean
    private val isInit: Boolean

    init {
        this.isPublic = access == ACC_PUBLIC
        this.isInit = (!isPublic && name == "<clinit>") ||
                (isPublic && name == "<init>")
    }

    override fun visitInsn(opcode: Int) {
        if (isInit && opcode == RETURN) {
            callEvent()
        }
        super.visitInsn(opcode)
    }

    override fun visitCode() {
        super.visitCode()
        if (isPublic && !isInit) {
            callEvent()
        }
    }

    private fun callEvent() {
        val eventType = eventMap[name] ?: return

        mv.visitTypeInsn(NEW, eventType)
        mv.visitInsn(DUP)
        mv.visitMethodInsn(INVOKESPECIAL, eventType, "<init>", "()V", false)
        mv.visitMethodInsn(
            INVOKESTATIC, BukkitUtilType.EVENT_UTILS,
            "call", "(Lorg/bukkit/event/Event;)Z", false
        )
        mv.visitInsn(POP)

        eventMap.remove(name)
    }
}