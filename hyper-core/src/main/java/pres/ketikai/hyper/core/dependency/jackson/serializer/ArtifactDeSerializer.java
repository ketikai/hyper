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

package pres.ketikai.hyper.core.dependency.jackson.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.eclipse.aether.artifact.DefaultArtifact;

import java.io.IOException;

/**
 * <p>工件 Json 反序列化</p>
 * <p>Created on 2022-12-26 15:46</p>
 *
 * @author ketikai
 * @version 0.0.1
 * @since 0.0.1
 */
public final class ArtifactDeSerializer extends JsonDeserializer<DefaultArtifact> {

    @Override
    public DefaultArtifact deserialize(JsonParser parser,
                                       DeserializationContext context)
            throws IOException, JacksonException {
        TreeNode node = parser.getCodec().readTree(parser);
        String groupId = ((TextNode) node.get("groupId")).asText();
        String artifactId = ((TextNode) node.get("artifactId")).asText();
        String baseVersion = ((TextNode) node.get("baseVersion")).asText();
        String classifier = ((TextNode) node.get("classifier")).asText();
        String extension = ((TextNode) node.get("extension")).asText();

        DefaultArtifact artifact = new DefaultArtifact(groupId, artifactId, classifier, extension, baseVersion);
        if (artifact.isSnapshot()) {
            String version = ((TextNode) node.get("version")).asText();
            return (DefaultArtifact) artifact.setVersion(version);
        }
        return artifact;
    }
}
