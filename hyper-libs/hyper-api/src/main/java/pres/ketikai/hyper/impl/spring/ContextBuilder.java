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

package pres.ketikai.hyper.impl.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>标题</p>
 *
 * <p>Created on 2023/2/13 11:03</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ContextBuilder {

    private final Set<String> basePackages = new LinkedHashSet<>(16);
    private ClassLoader classLoader;

    public ContextBuilder addBasePackages(String... basePackages) {
        if (basePackages == null || basePackages.length == 0) {
            return this;
        }

        for (final String pkg : basePackages) {
            if (pkg != null) {
                this.basePackages.add(pkg);
            }
        }

        return this;
    }

    public ContextBuilder setClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            return this;
        }

        this.classLoader = classLoader;
        return this;
    }

    public ApplicationContext build() {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        if (classLoader != null) {
            context.setClassLoader(classLoader);
        }
        if (!basePackages.isEmpty()) {
            context.scan(basePackages.toArray(String[]::new));
        }
        context.refresh();

        return context;
    }
}
