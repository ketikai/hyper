/*
 *     Copyright (C) 2023  ketikai
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
public final class HyperAgent {

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
