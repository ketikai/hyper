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

package pres.ketikai.hyper.core.resource.watcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pres.ketikai.hyper.core.event.Events;
import pres.ketikai.hyper.core.resource.event.DeleteResourceEvent;
import pres.ketikai.hyper.core.resource.event.NewResourceEvent;
import pres.ketikai.hyper.core.resource.event.UpdateResourceEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>资源文件观察者</p>
 *
 * <p>Created on 2022-12-31 17:12</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ResourceWatcher {

    private static final Logger log = LoggerFactory.getLogger(ResourceWatcher.class);

    private final Path path;
    private final Map<File, Long> files = new ConcurrentHashMap<>();
    private final long delay;

    private final ResourceVisitor visitor;

    public ResourceWatcher(Path path) {
        this(path, 1L);
    }

    public ResourceWatcher(Path path, long delay) {
        this.path = path;
        this.delay = delay;
        this.visitor = new ResourceVisitor(files);
    }

    public void start() {
        while (true) {
            try {
                work();
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException e) {
                log.warn("ResourceListener stop working.");
                break;
            }
        }
    }

    private void work() {
        long lastModified;
        for (File file : files.keySet()) {
            if (!file.exists()) {
                Events.call(new DeleteResourceEvent(file, true, true));
                files.remove(file);
            }

            lastModified = file.lastModified();
            if (lastModified != files.get(file)) {
                Events.call(new UpdateResourceEvent(file, true, true));
                files.replace(file, lastModified);
            }
        }

        try {
            Files.walkFileTree(path, visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceWatcher that)) {
            return false;
        }

        return path.toString().equals(that.path.toString());
    }

    @Override
    public int hashCode() {
        return path != null ? path.toString().hashCode() : 0;
    }

    private record ResourceVisitor(Map<File, Long> files) implements FileVisitor<Path> {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
            File file = path.toFile();
            if (!files.containsKey(file)) {
                Events.call(new NewResourceEvent(file, true, true));
                files.put(file, file.lastModified());
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
            exc.printStackTrace();
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
