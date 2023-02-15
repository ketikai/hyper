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

package pres.ketikai.hyper.agent;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

/**
 * <p>hyper agent</p>
 *
 * <p>Created on 2023/2/12 16:24</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class HyperAgent {

    public static void agentmain(String args, Instrumentation inst) {
        ClassLoader pluginClassLoader = null;
        for (final Class<?> loadedClass : inst.getAllLoadedClasses()) {
            if ("pres.ketikai.hyper.core.HyperCore".equals(loadedClass.getName())) {
                pluginClassLoader = loadedClass.getClassLoader();
                break;
            }
        }

        if (pluginClassLoader == null) {
            throw new RuntimeException("pluginClassLoader is not found");
        }

        try {
            final Object context = pluginClassLoader
                    .loadClass("pres.ketikai.hyper.commons.spring.SpringUtils")
                    .getDeclaredMethod("getContext")
                    .invoke(null);
            Method method = context.getClass().getDeclaredMethod("getBean", Class.class);
            final Class<?> agentClass = pluginClassLoader.loadClass("pres.ketikai.hyper.impl.agent.DelegateAgent");
            final Object delegateAgent = method.invoke(context, agentClass);
            method = agentClass.getDeclaredMethod("agentmain", String.class, Instrumentation.class);
            method.invoke(delegateAgent, args, inst);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
