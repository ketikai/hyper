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

package pres.ketikai.hyper.commons.maven;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.*;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;
import pres.ketikai.hyper.commons.Asserts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Maven 解析器工具</p>
 *
 * <p>Created on 2022-12-25 08:17</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class MavenResolver {

    private MavenResolver() {

    }

    public static RepositorySystem newSystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils
                .newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class,
                BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }

    public static RepositorySystemSession newSession(
            RepositorySystem system,
            LocalRepository localRepository
    ) {
        Asserts.notNull(localRepository);

        DefaultRepositorySystemSession session = MavenRepositorySystemUtils
                .newSession();
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session,
                localRepository));
        return session;
    }

    public static File resolveArtifact(
            RepositorySystem system,
            RepositorySystemSession session,
            Artifact artifact,
            RemoteRepository... remoteRepository
    ) {
        Asserts.notNull(session);
        Asserts.notNull(artifact);
        Asserts.notEmpty(remoteRepository);

        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(List.of(remoteRepository));

        try {
            return system.resolveArtifact(session, artifactRequest).getArtifact().getFile();
        } catch (ArtifactResolutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<File> resolveDependencies(
            RepositorySystem system,
            RepositorySystemSession session,
            Artifact artifact,
            RemoteRepository... remoteRepository
    ) {
        Asserts.notNull(session);
        Asserts.notNull(artifact);
        Asserts.notEmpty(remoteRepository);

        DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(JavaScopes.RUNTIME);
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.RUNTIME));
        collectRequest.setRepositories(List.of(remoteRepository));
        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFilter);

        List<File> result = new ArrayList<>();
        try {
            List<ArtifactResult> artifactResults = system.resolveDependencies(session, dependencyRequest).getArtifactResults();
            artifactResults.forEach(artifactResult -> result.add(artifactResult.getArtifact().getFile()));
        } catch (DependencyResolutionException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
