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

package pres.ketikai.hyper.core.dependency.jackson.module;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.eclipse.aether.artifact.DefaultArtifact;
import pres.ketikai.hyper.core.dependency.jackson.serializer.ArtifactDeSerializer;

import java.util.Collections;

/**
 * <p>工件 Json 序列化模块</p>
 * <p>Created on 2022-12-26 15:44</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ArtifactModule extends SimpleModule {

    public ArtifactModule() {
        super("ArtifactModule", new Version(
                0, 0, 0,
                null, "pres.ketikai.hyper", "ArtifactModule"
        ), Collections.singletonMap(DefaultArtifact.class, new ArtifactDeSerializer()));
    }
}
