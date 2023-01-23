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

package pres.ketikai.hyper.core.libraries;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferResource;
import pres.ketikai.hyper.commons.library.LibraryResolver;
import pres.ketikai.hyper.commons.unsafe.loader.UnsafeURLClassLoader;
import pres.ketikai.hyper.core.HyperCore;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>依赖库相关工具</p>
 *
 * <p>Created on 2023/1/23 14:26</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class Libraries {

    private static final String LIBRARIES_PATH = "/libraries";
    private static final String LIBRARY_REPORT_PATH = "/library-report.txt";
    private static final RepositorySystem SYSTEM = LibraryResolver.newSystem();
    private static final DefaultRepositorySystemSession SESSION;
    private static final CommandSender SENDER = Bukkit.getConsoleSender();
    private static final RemoteRepository[] REMOTE_REPOSITORIES;

    static {
        final File localDir = new File(HyperCore.getInstance().getDataFolder(), LIBRARIES_PATH);
        localDir.mkdirs();
        final LocalRepository localRepository = new LocalRepository(localDir);
        SESSION = (DefaultRepositorySystemSession)
                LibraryResolver.newSession(SYSTEM, localRepository);
        SESSION.setTransferListener(new TransferLogger());

        // todo 加载远程仓库配置
        REMOTE_REPOSITORIES = new RemoteRepository[0];
    }

    private Libraries() {
    }

    /**
     * <p>为插件加载依赖库</p>
     * 本地仓库不存在的依赖会从远程仓库解析并下载，功能等同于 Maven<br>
     * 须要插件提供 library-report.txt 文件
     *
     * @param plugin 插件实例
     */
    public static void loadLibraries(Plugin plugin) {
        final InputStream is = plugin.getResource(LIBRARY_REPORT_PATH);
        if (is == null) {
            return;
        }

        final Set<Artifact> artifacts = new HashSet<>();
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            br.lines().forEach(artifactId -> {
                if (artifactId != null && !"".equals((artifactId = artifactId.trim()))) {
                    artifacts.add(new DefaultArtifact(artifactId));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (artifacts.isEmpty()) {
            return;
        }

        final ClassLoader pcl = plugin.getClass().getClassLoader();
        Field libraryLoaderField = null;
        try {
            libraryLoaderField = pcl.getClass().getDeclaredField("libraryLoader");
            libraryLoaderField.setAccessible(true);
            final UnsafeURLClassLoader unsafeUcl = UnsafeURLClassLoader.wrap((URLClassLoader) libraryLoaderField.get(pcl));

            final String pluginName = plugin.getName();
            File file;
            for (final Artifact artifact : artifacts) {
                file = LibraryResolver.resolveArtifact(SYSTEM, SESSION, artifact, REMOTE_REPOSITORIES);
                unsafeUcl.addURL(file.toURI().toURL());
                SENDER.sendMessage("[" + pluginName + "] Loaded Library " + file);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            libraryLoaderField.setAccessible(false);
            throw new RuntimeException(e);
        }
    }

    private static final class TransferLogger extends AbstractTransferListener {

        @Override
        public void transferStarted(TransferEvent event) throws TransferCancelledException {
            final TransferResource resource = event.getResource();
            SENDER.sendMessage("Downloading " +
                    resource.getRepositoryUrl() + resource.getResourceName());
        }
    }
}
