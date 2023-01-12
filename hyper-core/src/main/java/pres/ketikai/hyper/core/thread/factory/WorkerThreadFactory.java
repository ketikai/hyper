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

package pres.ketikai.hyper.core.thread.factory;

import pres.ketikai.hyper.commons.Asserts;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>工作者线程工厂</p>
 * <p>Created on 2022-12-24 23:46</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public class WorkerThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final String prefix;
    private final AtomicInteger serial = new AtomicInteger();

    public WorkerThreadFactory() {
        this("工作者线程");
    }

    public WorkerThreadFactory(String prefix) {
        this.group = Thread.currentThread().getThreadGroup();
        this.prefix = prefix + " NO.";
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Asserts.notNull(runnable);

        Thread thread = new Thread(group, runnable,
                prefix + serial.getAndIncrement());

        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }

        return thread;
    }
}
