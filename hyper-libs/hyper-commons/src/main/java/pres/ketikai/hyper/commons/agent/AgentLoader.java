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

package pres.ketikai.hyper.commons.agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import pres.ketikai.hyper.commons.unsafe.UnsafeWrapper;

import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * <p>Java agent 加载器相关工具</p>
 *
 * <p>Created on 2023/2/13 13:04</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class AgentLoader {

    private static final VirtualMachine VM;

    static {
        try {
            final String className = "sun.tools.attach.HotSpotVirtualMachine";
            final Class<?> clazz = Class.forName(className);
            final String fieldName = "ALLOW_ATTACH_SELF";
            final boolean allowAttachSelf = (boolean) UnsafeWrapper.getStaticValue(clazz, fieldName);
            if (!allowAttachSelf) {
                UnsafeWrapper.putStaticValue(clazz, fieldName, true);
            }
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        final String pid = String.valueOf(ManagementFactory.getRuntimeMXBean().getPid());
        try {
            VM = VirtualMachine.attach(pid);
        } catch (AttachNotSupportedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadAgent(String agent) {
        try {
            VM.loadAgent(agent);
        } catch (AgentLoadException | AgentInitializationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
