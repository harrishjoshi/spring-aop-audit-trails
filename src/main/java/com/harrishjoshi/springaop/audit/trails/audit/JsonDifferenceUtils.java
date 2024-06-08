package com.harrishjoshi.springaop.audit.trails.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class JsonDifferenceUtils {

    private static final String OLD = "old";
    private static final String NEW = "new";

    private JsonDifferenceUtils() {
    }

    public static JsonNode getJsonDifference(Object preDetails, Object postDetails) throws JsonProcessingException {
        if (preDetails == null || postDetails == null) {
            // return empty json node
            return new ObjectMapper().createObjectNode();
        }

        log.info("preDetails={}", preDetails);
        log.info("postDetails={}", postDetails);

        var objectMapper = new ObjectMapper();
        Map<String, Object> preMap = objectMapper.readValue(preDetails.toString(), new TypeReference<>() {
        });
        Map<String, Object> postMap = objectMapper.readValue(postDetails.toString(), new TypeReference<>() {
        });

        Map<String, Map<String, Object>> fieldToDifferenceMap = new LinkedHashMap<>();
        MapDifference<String, Object> differences = Maps.difference(preMap, postMap);
        Map<String, MapDifference.ValueDifference<Object>> entriesDiffering = differences.entriesDiffering();

        for (Map.Entry<String, MapDifference.ValueDifference<Object>> entry : entriesDiffering.entrySet()) {
            Map<String, Object> diffMap = new LinkedHashMap<>();
            MapDifference.ValueDifference<Object> valueDiff = entry.getValue();
            diffMap.put(OLD, valueDiff.leftValue());
            diffMap.put(NEW, valueDiff.rightValue());
            fieldToDifferenceMap.put(entry.getKey(), diffMap);
        }

        return objectMapper.valueToTree(fieldToDifferenceMap);
    }
}
