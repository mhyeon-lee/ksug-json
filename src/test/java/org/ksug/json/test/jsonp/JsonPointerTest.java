package org.ksug.json.test.jsonp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ksug.json.test.JsonUtils.*;

import java.util.Collections;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ksug.json.fixture.Project;
import org.ksug.json.fixture.User;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 * Created by mhyeon.lee on 2017. 11. 20..
 */
@DisplayName("Json Pointer Test")
class JsonPointerTest {
	private Jsonb jsonb;
	private Project object;

	@BeforeEach
	void setUp() {
		this.jsonb = JsonbBuilder.create();
		this.object = Project.builder()
				.name("json-pointer")
				.description("json pointer")
				.policy("open")
				.tags(Collections.singletonList("json-tags"))
				.creator(User.builder()
						.name("user")
						.age(30)
						.build())
				.build();
	}

	@Test
	void jsonPointer_path() {
		// Given
		JsonObject target = objectToJsonObject(this.jsonb, this.object);
		JsonPointer jsonPointer = Json.createPointer("/name");

		// When
		JsonValue expected = jsonPointer.getValue(target);

		// Then
		assertEquals(this.object.getName(), expected.toString().replace("\"", ""));
	}

	@Test
	void jsonPointer_array() {
		// Given
		JsonObject target = objectToJsonObject(this.jsonb, this.object);
		JsonPointer jsonPointer = Json.createPointer("/tags");

		// When
		JsonValue expected = jsonPointer.getValue(target);

		// Then
		DocumentContext document = JsonPath.parse(expected);
		assertEquals(this.object.getTags().size(), document.read("$", List.class).size());
	}

	@Test
	void jsonPointer_array_index() {
		// Given
		JsonObject target = objectToJsonObject(this.jsonb, this.object);
		JsonPointer jsonPointer = Json.createPointer("/tags/0");

		// When
		JsonValue expected = jsonPointer.getValue(target);

		// Then
		assertEquals(this.object.getTags().get(0), expected.toString().replace("\"", ""));
	}

	@Test
	void jsonPointer_nested_path() {
		// Given
		JsonObject target = objectToJsonObject(this.jsonb, this.object);
		JsonPointer jsonPointer = Json.createPointer("/creator/name");

		// When
		JsonValue expected = jsonPointer.getValue(target);

		// Then
		assertEquals(this.object.getCreator().getName(), expected.toString().replace("\"", ""));
	}
}
