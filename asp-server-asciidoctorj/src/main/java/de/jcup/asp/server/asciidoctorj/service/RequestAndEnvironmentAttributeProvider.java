package de.jcup.asp.server.asciidoctorj.service;

import java.util.Map;

import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.asp.api.MapRequestParameterKey;
import de.jcup.asp.api.Request;
import de.jcup.asp.api.asciidoc.Attribute;

public class RequestAndEnvironmentAttributeProvider {

    private static final Logger LOG = LoggerFactory.getLogger(RequestAndEnvironmentAttributeProvider.class);

    public Attributes getAttributes(Request request) {
        Map<String, Object> attributesAsMap = request.getMap(MapRequestParameterKey.ATTRIBUTES);
        LOG.debug("Attributes:{}", attributesAsMap);

        AttributesBuilder attributesBuilder = createAttributesBuilderWithAttributesSet(attributesAsMap);

        addAttributesByEnvironment(attributesBuilder);

        Attributes attributes = attributesBuilder.build();
        return attributes;
    }

    private void addAttributesByEnvironment(AttributesBuilder attributesBuilder) {
        String graphvizDot = System.getenv("GRAPHVIZ_DOT");
        if (graphvizDot != null) {
            attributesBuilder.attribute("graphvizdot@", graphvizDot);
        }
    }

    private AttributesBuilder createAttributesBuilderWithAttributesSet(Map<String, Object> attributesAsMap) {
        AttributesBuilder attributesBuilder = Attributes.builder();

        for (String attribute : attributesAsMap.keySet()) {
            boolean unknownAttribute = true;
            for (Attribute wellknown : Attribute.values()) {
                if (wellknown.getKey().equals(attribute)) {
                    unknownAttribute = false;
                    break;
                }
            }
            if (unknownAttribute) {
                LOG.warn("unknown attribute used in request:{}", attribute);
            }
            Object value = attributesAsMap.get(attribute);

            attributesBuilder.attribute(attribute, value);
        }
        return attributesBuilder;
    }
}
