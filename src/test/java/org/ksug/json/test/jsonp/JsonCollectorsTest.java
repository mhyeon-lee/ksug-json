package org.ksug.json.test.jsonp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.stream.JsonCollectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ksug.json.test.JsonUtils;

/**
 * Created by mhyeon.lee on 2017. 11. 20..
 */
@DisplayName("Json Collectors Test")
class JsonCollectorsTest {
	private Jsonb jsonb;

	@BeforeEach
	void setUp() {
		this.jsonb = JsonbBuilder.create();
	}

	@Test
	void collectors_toJsonArray() {
		// Given
		List<String> jsons = Arrays.asList(
				"{\"name\":\"project1\",\"policy\":\"public\",\"creator\":{\"name\":\"user1\",\"age\":30}}",
				"{\"name\":\"project2\",\"policy\":\"private\",\"creator\":{\"name\":\"user2\",\"age\":40}}",
				"{\"name\":\"project3\",\"description\":\"desc\",\"creator\":{\"name\":\"user3\",\"age\":50}}");

		// When
		JsonArray expected = jsons.stream()
				.map(json -> JsonUtils.jsonToJsonObject(this.jsonb, json))
				.collect(JsonCollectors.toJsonArray());

		// Then
		assertEquals(jsons.get(0), expected.get(0).toString());
		assertEquals(jsons.get(1), expected.get(1).toString());
		assertEquals(jsons.get(2), expected.get(2).toString());
	}

	@Test
	void collectors_toJsonObject() {
		// Given
		Map<String, String> map = new HashMap<>();
		map.put("name", "project");
		map.put("description", "project desc");
		map.put("policy", "public");

		// When
		JsonObject expected = map.entrySet().stream()
				.map(e -> new SimpleEntry<String, JsonValue>(
						e.getKey(), Json.createValue(e.getValue())))
				.collect(JsonCollectors.toJsonObject());

		// Then
		assertEquals(map.get("name"), expected.getString("name"));
		assertEquals(map.get("description"), expected.getString("description"));
		assertEquals(map.get("policy"), expected.getString("policy"));
	}

	@Test
	void collectors_groupingBy() {
		// Given
		List<String> jsons = Arrays.asList(
				"{\"name\":\"project1\",\"policy\":\"public\",\"creator\":{\"name\":\"user1\",\"age\":30}}",
				"{\"name\":\"project2\",\"policy\":\"private\",\"creator\":{\"name\":\"user2\",\"age\":40}}",
				"{\"name\":\"project3\",\"policy\":\"public\",\"creator\":{\"name\":\"user3\",\"age\":50}}");

		// When
		JsonObject expected = jsons.stream()
				.map(json -> JsonUtils.jsonToJsonObject(this.jsonb, json))
				.collect(JsonCollectors.groupingBy(j -> j.asJsonObject().getString("policy")));

		// Then
		assertSame(2, expected.getJsonArray("public").size());
		assertEquals(jsons.get(0), expected.getJsonArray("public").get(0).toString());
		assertEquals(jsons.get(2), expected.getJsonArray("public").get(1).toString());
		assertSame(1, expected.getJsonArray("private").size());
		assertEquals(jsons.get(1), expected.getJsonArray("private").get(0).toString());
	}
}
