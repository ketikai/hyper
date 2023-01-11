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

package pres.ketikai.hyper.core.listener;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Warning;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.spigotmc.CustomTimingsHandler;
import org.springframework.core.annotation.AnnotationUtils;
import pres.ketikai.hyper.core.property.annotation.HyperPropSource;
import pres.ketikai.hyper.core.resource.annotation.HyperResource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>监听器相关工具</p>
 *
 * <p>Created on 2023-01-01 11:42</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Listeners {

    private Listeners() {
    }

    /**
     * <p>兼容 CGLIB 代理类的事件监听器注册方法</p>
     * 改自 {@link SimplePluginManager#registerEvents(Listener, Plugin)}
     *
     * @param listener 监听器
     * @param plugin   插件实例
     */
    public static void register(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalPluginAccessException("Plugin attempted to register " +
                    listener + " while not enabled");
        }

        Map<Class<? extends Event>, Set<RegisteredListener>> registeredListeners =
                createRegisteredListeners(listener, plugin);

        if (!registeredListeners.isEmpty()) {
            for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry :
                    registeredListeners.entrySet()) {
                getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
            }
        } else {
            plugin.getLogger().log(Level.WARNING,
                    "RegisteredListeners is empty (" + listener.getClass().getName() + ")",
                    Bukkit.getServer().getWarningState() == Warning.WarningState.ON ?
                            new AuthorNagException(null) : null);
        }
    }

    /**
     * <p>兼容 CGLIB 代理类的监听器创建方法</p>
     * 改自 {@link JavaPluginLoader#createRegisteredListeners(Listener, Plugin)}
     *
     * @param listener 监听器
     * @param plugin   插件实例
     * @return 已注册的监听器集合
     */
    private static Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, final Plugin plugin) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(listener, "Listener can not be null");

        final Server server = Bukkit.getServer();
        final Logger log = plugin.getLogger();
        final String fullName = plugin.getDescription().getFullName();
        final Class<? extends Listener> listenerClass = listener.getClass();

        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>(1);
        Set<Method> methods;
        try {
            Method[] publicMethods = listenerClass.getMethods();
            Method[] privateMethods = listenerClass.getDeclaredMethods();
            methods = new HashSet<>(
                    publicMethods.length + privateMethods.length, 1.0f);
            methods.addAll(Arrays.asList(publicMethods));
            methods.addAll(Arrays.asList(privateMethods));
        } catch (NoClassDefFoundError e) {
            log.severe("Plugin " +
                    fullName +
                    " has failed to register events for " +
                    listenerClass + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (Method method : methods) {
            final EventHandler eh = AnnotationUtils.findAnnotation(method, EventHandler.class);
            if (eh == null) {
                continue;
            }
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> paramClass = method.getParameterTypes()[0];
            final boolean onlyOneParam = method.getParameterTypes().length == 1;
            final boolean paramIsEvent = Event.class.isAssignableFrom(paramClass);
            if (!paramIsEvent) {
                log.severe(fullName +
                        " attempted to register an invalid EventHandler method signature \"" +
                        method.toGenericString() + "\" in " + listenerClass);
                continue;
            }

            // 兼容增强方法
            // start --------------------------------------------------------------------------------------
            if (!onlyOneParam && !isHyperMethod(listener, method, paramClass)) {
                log.severe(fullName +
                        " attempted to register an invalid EventHandler method signature \"" +
                        method.toGenericString() + "\" in " + listenerClass);
                continue;
            }
            // end ----------------------------------------------------------------------------------------

            final Class<? extends Event> eventClass = paramClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.computeIfAbsent(eventClass, k -> new HashSet<>());

            for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (AnnotationUtils.findAnnotation(clazz, Deprecated.class) != null) {
                    Warning warning = AnnotationUtils.findAnnotation(clazz, Warning.class);
                    Warning.WarningState warningState = server.getWarningState();
                    if (!warningState.printFor(warning)) {
                        break;
                    }
                    log.log(
                            Level.WARNING,
                            String.format(
                                    "\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated. \"%s\"; please notify the authors %s.",
                                    fullName,
                                    clazz.getName(),
                                    method.toGenericString(),
                                    (warning != null && warning.reason().length() != 0) ? warning.reason() : "Server performance will be affected",
                                    Arrays.toString(plugin.getDescription().getAuthors().toArray())),
                            warningState == Warning.WarningState.ON ? new AuthorNagException(null) : null);
                    break;
                }
            }

            final CustomTimingsHandler timings = new CustomTimingsHandler(
                    "Plugin: " +
                            fullName +
                            " Event: " + listenerClass.getName() +
                            "::" + method.getName() + "(" +
                            eventClass.getSimpleName() + ")", JavaPluginLoader.pluginParentTimer);
            final Method temp = method;
            EventExecutor executor = (l, e) -> {
                try {
                    if (!eventClass.isAssignableFrom(e.getClass())) {
                        return;
                    }
                    // Spigot start
                    boolean isAsync = e.isAsynchronous();
                    if (!isAsync) {
                        timings.startTiming();
                    }
                    temp.invoke(l, e);
                    if (!isAsync) {
                        timings.stopTiming();
                    }
                    // Spigot end
                } catch (InvocationTargetException ex) {
                    throw new EventException(ex.getCause());
                } catch (Throwable t) {
                    throw new EventException(t);
                }
            };
            eventSet.add(new RegisteredListener(listener, executor, eh.priority(),
                    plugin, eh.ignoreCancelled()));
        }
        return ret;
    }

    private static boolean isHyperMethod(Listener listener, Method method, Class<?> paramClass) {
        if (AnnotationUtils.findAnnotation(method, HyperPropSource.class) != null ||
                AnnotationUtils.findAnnotation(method, HyperResource.class) != null) {
            try {
                method = listener.getClass().getMethod(method.getName(), paramClass);
                if (AnnotationUtils.findAnnotation(method, EventHandler.class) != null) {
                    return true;
                }
            } catch (NoSuchMethodException ignored) {
            }
        }
        return false;
    }

    private static HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    private static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " +
                        clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }
}
