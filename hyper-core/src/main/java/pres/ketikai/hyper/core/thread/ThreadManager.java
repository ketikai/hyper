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

package pres.ketikai.hyper.core.thread;

import pres.ketikai.hyper.commons.caller.Caller;
import pres.ketikai.hyper.commons.caller.Callers;
import pres.ketikai.hyper.core.thread.factory.WorkerThreadFactory;

import java.util.concurrent.*;

/**
 * <p>线程管理器</p>
 *
 * <p>Created on 2022-12-25 00:09</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ThreadManager {

    private final ExecutorService executor;
    private final Caller caller;

    public ThreadManager(String prefix, int coreSize, int maxSize, long keepTime, TimeUnit timeUnit) {
        this.executor = new ThreadPoolExecutor(
                coreSize, maxSize, keepTime, timeUnit,
                new SynchronousQueue<>(), new WorkerThreadFactory(prefix),
                (r, e) -> {
                    throw new RejectedExecutionException();
                }
        );
        this.caller = Callers.getCaller();
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public Caller getCaller() {
        return caller;
    }
}
