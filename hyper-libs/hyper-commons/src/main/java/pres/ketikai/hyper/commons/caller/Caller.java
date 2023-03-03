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

package pres.ketikai.hyper.commons.caller;

import pres.ketikai.hyper.commons.asserts.Asserts;

import java.io.Serial;
import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * <p>调用者基本信息实体类</p>
 *
 * <p>Created on 2023/2/17 20:18</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Caller implements Serializable {

    @Serial
    private static final long serialVersionUID = 4689509457459559865L;
    private final String className;
    private final String methodName;
    private final Class<?> callerClass;
    private final Method method;
    private final boolean nativeMethod;

    Caller(String className, String methodName, Class<?> callerClass, MethodType methodType, boolean nativeMethod) throws NoSuchMethodException {
        Asserts.notNull(className, "className must not be null");
        Asserts.notNull(methodName, "methodName must not be null");
        Asserts.notNull(callerClass, "callerClass must not be null");
        Asserts.notNull(methodType, "methodType must not be null");

        this.className = className;
        this.methodName = methodName;
        this.callerClass = callerClass;
        this.method = this.callerClass.getDeclaredMethod(this.methodName, methodType.parameterArray());
        this.nativeMethod = nativeMethod;
    }

    /**
     * 获取调用者所属类的全类名
     *
     * @return 调用者所属类的全类名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 获取调用者所属方法的方法名
     *
     * @return 调用者所属方法的方法名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 获取调用者所属类
     *
     * @return 调用者所属类
     */
    public Class<?> getCallerClass() {
        return callerClass;
    }

    /**
     * 获取调用者所属方法
     *
     * @return 调用者所属方法
     */
    public Method getMethod() {
        return method;
    }

    /**
     * 获取调用者所属方法是否为原生方法
     *
     * @return 调用者所属方法是否为原生方法
     */
    public boolean isNativeMethod() {
        return nativeMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final Caller caller)) {
            return false;
        }

        if (isNativeMethod() != caller.isNativeMethod()) {
            return false;
        }
        if (getClassName() != null ? !getClassName().equals(caller.getClassName()) : caller.getClassName() != null) {
            return false;
        }
        if (getMethodName() != null ? !getMethodName().equals(caller.getMethodName()) : caller.getMethodName() != null) {
            return false;
        }
        if (getCallerClass() != null ? !getCallerClass().equals(caller.getCallerClass()) : caller.getCallerClass() != null) {
            return false;
        }
        return getMethod() != null ? getMethod().equals(caller.getMethod()) : caller.getMethod() == null;
    }

    @Override
    public int hashCode() {
        int result = getClassName() != null ? getClassName().hashCode() : 0;
        result = 31 * result + (getMethodName() != null ? getMethodName().hashCode() : 0);
        result = 31 * result + (getCallerClass() != null ? getCallerClass().hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        result = 31 * result + (isNativeMethod() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Caller{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", callerClass=" + callerClass +
                ", method=" + method +
                ", nativeMethod=" + nativeMethod +
                '}';
    }
}
