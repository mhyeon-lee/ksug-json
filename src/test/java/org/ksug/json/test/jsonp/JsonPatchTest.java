package org.ksug.json.test.jsonp;

import static org.junit.jupiter.api.Assertions.*;
import static org.ksug.json.test.JsonUtils.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ksug.json.fixture.Project;
import org.ksug.json.fixture.User;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * Created by mhyeon.lee on 2017. 11. 19..
 */
@DisplayName("Json Patch Test")
class JsonPatchTest {
	private Jsonb jsonb;

	private Project object;

	@BeforeEach
	void setUp() {
		this.jsonb = JsonbBuilder.create();
		this.object = Project.builder()
				.name("json-patch")
				.description("json patch")
				.policy("open")
				.tags(Collections.singletonList("json-tags"))
				.creator(User.builder()
						.name("user")
						.age(30)
						.build())
				.build();
	}

	@Test
	@DisplayName("Json Patch test for add operation.")
	void jsonPatch_add() {
		// Given
		JsonObject source = objectToJsonObject(this.jsonb, this.object);

		JsonObject addPatch = jsonToJsonObject(this.jsonb,
				"{\"op\":\"add\", \"path\":\"/tags/-\", \"value\":\"appended\"}");
		JsonArray patchArray = Json.createArrayBuilder()
				.add(addPatch)
				.build();
		JsonPatch sut = Json.createPatch(patchArray);

		// When
		JsonObject result = sut.apply(source);
		String expected = result.toString();

		// Then
		System.out.println(expected);
		DocumentContext document = JsonPath.parse(expected);
		assertEquals(this.object.getName(), document.read("$.name", String.class));
		assertEquals(this.object.getDescription(), document.read("$.description", String.class));
		assertEquals(this.object.getPolicy(), document.read("$.policy", String.class));

		List<String> newTags = Arrays.asList(this.object.getTags().get(0), "appended");
		assertEquals(newTags, document.read("$.tags", List.class));
		assertEquals(this.object.getCreator().getName(), document.read("$.creator.name", String.class));
		assertSame(this.object.getCreator().getAge(), document.read("$.creator.age", Integer.class));

		Project project = this.jsonb.fromJson(expected, Project.class);
		assertEquals(this.object.getName(), project.getName());
		assertEquals(this.object.getDescription(), project.getDescription());
		assertEquals(this.object.getPolicy(), project.getPolicy());
		assertEquals(newTags, project.getTags());
		assertEquals(this.object.getCreator().getName(), project.getCreator().getName());
		assertSame(this.object.getCreator().getAge(), project.getCreator().getAge());
	}

	@Test
	@DisplayName("Json Patch test for remove operation.")
	void jsonPatch_remove() {
		// Given
		JsonObject source = objectToJsonObject(this.jsonb, this.object);

		JsonObject removePatch = jsonToJsonObject(this.jsonb,
				"{\"op\":\"remove\", \"path\":\"/description\"}");
		JsonObject arrayRemovePatch = jsonToJsonObject(this.jsonb,
				"{\"op\":\"remove\", \"path\":\"/tags/0\"}");
		JsonObject nestedRemovePatch = jsonToJsonObject(this.jsonb,
				"{\"op\":\"remove\", \"path\":\"/creator/name\"}");
		JsonArray patchArray = Json.createArrayBuilder()
				.add(removePatch)
				.add(arrayRemovePatch)
				.add(nestedRemovePatch)
				.build();
		JsonPatch sut = Json.createPatch(patchArray);

		// When
		JsonObject result = sut.apply(source);
		String expected = result.toString();

		// Then
		System.out.println(expected);
		DocumentContext document = JsonPath.parse(expected);
		assertEquals(this.object.getName(), document.read("$.name", String.class));
		assertThrows(PathNotFoundException.class, () -> document.read("$.description"));
		assertEquals(this.object.getPolicy(), document.read("$.policy", String.class));
		assertEquals(Collections.emptyList(), document.read("$.tags", List.class));
		assertThrows(PathNotFoundException.class, () -> document.read("$.creator.name"));
		assertSame(this.object.getCreator().getAge(), document.read("$.creator.age", Integer.class));

		Project project = this.jsonb.fromJson(expected, Project.class);
		assertEquals(this.object.getName(), project.getName());
		assertNull(project.getDescription());
		assertEquals(this.object.getPolicy(), project.getPolicy());
		assertEquals(Collections.emptyList(), project.getTags());
		assertNull(project.getCreator().getName());
		assertSame(this.object.getCreator().getAge(), project.getCreator().getAge());
	}

	@Test
	@DisplayName("Json Patch test for replace operation.")
	void jsonPatch_replace() {
		// Given
		JsonObject source = objectToJsonObject(this.jsonb, this.object);

		JsonObject replacePatch = jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"replace\", \"path\":\"/description\", \"value\":\"desc patched\"}");
		JsonObject nestedReplacePatch = jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"replace\", \"path\":\"/creator/age\", \"value\":40}");
		JsonArray patchArray = Json.createArrayBuilder()
				.add(replacePatch)
				.add(nestedReplacePatch)
				.build();
		JsonPatch sut = Json.createPatch(patchArray);

		// When
		JsonObject result = sut.apply(source);
		String expected = result.toString();

		// Then
		System.out.println(expected);
		DocumentContext document = JsonPath.parse(expected);
		assertEquals(this.object.getName(), document.read("$.name", String.class));
		assertEquals("desc patched", document.read("$.description", String.class));
		assertEquals(this.object.getPolicy(), document.read("$.policy", String.class));
		assertEquals(this.object.getTags(), document.read("$.tags", List.class));
		assertEquals(this.object.getCreator().getName(), document.read("$.creator.name", String.class));
		assertSame(40, document.read("$.creator.age", Integer.class));

		Project project = this.jsonb.fromJson(expected, Project.class);
		assertEquals(this.object.getName(), project.getName());
		assertEquals("desc patched", project.getDescription());
		assertEquals(this.object.getPolicy(), project.getPolicy());
		assertEquals(this.object.getTags(), project.getTags());
		assertEquals(this.object.getCreator().getName(), project.getCreator().getName());
		assertSame(40, project.getCreator().getAge());
	}

	@Test
	@DisplayName("Json Patch test for copy operation.")
	void jsonPatch_copy() {
		// Given
		JsonObject source = objectToJsonObject(this.jsonb, this.object);

		JsonObject copyPath = jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"copy\", \"from\":\"/name\", \"path\":\"/creator/name\"}");
		JsonArray patchArray = Json.createArrayBuilder()
				.add(copyPath)
				.build();
		JsonPatch sut = Json.createPatch(patchArray);

		// When
		JsonObject result = sut.apply(source);
		String expected = result.toString();

		// Then
		System.out.println(expected);
		DocumentContext document = JsonPath.parse(expected);
		assertEquals(this.object.getName(), document.read("$.name", String.class));
		assertEquals(this.object.getDescription(), document.read("$.description", String.class));
		assertEquals(this.object.getPolicy(), document.read("$.policy", String.class));
		assertEquals(this.object.getTags(), document.read("$.tags", List.class));
		assertEquals(this.object.getName(), document.read("$.creator.name", String.class));
		assertSame(this.object.getCreator().getAge(), document.read("$.creator.age", Integer.class));

		Project project = this.jsonb.fromJson(expected, Project.class);
		assertEquals(this.object.getName(), project.getName());
		assertEquals(this.object.getDescription(), project.getDescription());
		assertEquals(this.object.getPolicy(), project.getPolicy());
		assertEquals(this.object.getTags(), project.getTags());
		assertEquals(this.object.getName(), project.getCreator().getName());
		assertSame(this.object.getCreator().getAge(), project.getCreator().getAge());
	}

	@Test
	@DisplayName("Json Patch test for move operation.")
	void jsonPatch_move() {
		// Given
		JsonObject source = objectToJsonObject(this.jsonb, this.object);

		JsonObject movePath = jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"move\", \"from\":\"/tags/0\", \"path\":\"/description\"}");
		JsonArray patchArray = Json.createArrayBuilder()
				.add(movePath)
				.build();
		JsonPatch sut = Json.createPatch(patchArray);

		// When
		JsonObject result = sut.apply(source);
		String expected = result.toString();

		// Then
		System.out.println(expected);
		DocumentContext document = JsonPath.parse(expected);
		assertEquals(this.object.getName(), document.read("$.name", String.class));
		assertEquals(this.object.getTags().get(0), document.read("$.description", String.class));
		assertEquals(this.object.getPolicy(), document.read("$.policy", String.class));
		assertEquals(Collections.emptyList(), document.read("$.tags", List.class));
		assertEquals(this.object.getCreator().getName(), document.read("$.creator.name", String.class));
		assertSame(this.object.getCreator().getAge(), document.read("$.creator.age", Integer.class));

		Project project = this.jsonb.fromJson(expected, Project.class);
		assertEquals(this.object.getName(), project.getName());
		assertEquals(this.object.getTags().get(0), project.getDescription());
		assertEquals(this.object.getPolicy(), project.getPolicy());
		assertEquals(Collections.emptyList(), project.getTags());
		assertEquals(this.object.getCreator().getName(), project.getCreator().getName());
		assertSame(this.object.getCreator().getAge(), project.getCreator().getAge());
	}

	@Test
	@DisplayName("Json Patch test for test operation.")
	void jsonPatch_test() {
		// Given
		JsonObject source = objectToJsonObject(this.jsonb, this.object);

		JsonObject testPath = jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"test\", \"path\":\"/name\", \"value\":\"json-patch\"}");
		JsonObject addPath = jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"add\", \"path\":\"/tags/1\", \"value\":\"second tag\"}");
		JsonArray patchArray = Json.createArrayBuilder()
				.add(testPath)
				.add(addPath)
				.build();
		JsonPatch sut = Json.createPatch(patchArray);

		// When
		JsonObject result = sut.apply(source);
		String expected = result.toString();

		// Then
		System.out.println(expected);
		List<String> newTags = Arrays.asList(this.object.getTags().get(0), "second tag");
		DocumentContext document = JsonPath.parse(expected);
		assertEquals(this.object.getName(), document.read("$.name", String.class));
		assertEquals(this.object.getDescription(), document.read("$.description", String.class));
		assertEquals(this.object.getPolicy(), document.read("$.policy", String.class));
		assertEquals(newTags, document.read("$.tags", List.class));
		assertEquals(this.object.getCreator().getName(), document.read("$.creator.name", String.class));
		assertSame(this.object.getCreator().getAge(), document.read("$.creator.age", Integer.class));

		Project project = this.jsonb.fromJson(expected, Project.class);
		assertEquals(this.object.getName(), project.getName());
		assertEquals(this.object.getDescription(), project.getDescription());
		assertEquals(this.object.getPolicy(), project.getPolicy());
		assertEquals(newTags, project.getTags());
		assertEquals(this.object.getCreator().getName(), project.getCreator().getName());
		assertSame(this.object.getCreator().getAge(), project.getCreator().getAge());
	}

	@Test
	@DisplayName("Json Patch test for test operation fail then throws exception.")
	void jsonPatch_test_fail() {
		// Given
		JsonObject source = objectToJsonObject(this.jsonb, this.object);

		JsonObject testPath = jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"test\", \"path\":\"/name\", \"value\":\"json-patch-fail\"}");
		JsonObject addPath = jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"add\", \"path\":\"/tags/1\", \"value\":\"second tag\"}");
		JsonArray patchArray = Json.createArrayBuilder()
				.add(testPath)
				.add(addPath)
				.build();
		JsonPatch sut = Json.createPatch(patchArray);

		// When
		JsonException expected = null;
		try {
			sut.apply(source);
		} catch (JsonException e) {
			expected = e;
		}

		// Then
		assertNotNull(expected);
		assertTrue(expected.getMessage().contains("\'/name\'"));
	}

	@Test
	void diff() {
		// Given
		JsonObject source = objectToJsonObject(this.jsonb, this.object);
		Project targetObject = Project.builder()
				.name("diff-target")
				.description(null)
				.policy(this.object.getPolicy())
				.tags(Arrays.asList("tag1", "tag2"))
				.creator(this.object.getCreator())
				.build();
		JsonObject target = objectToJsonObject(this.jsonb, targetObject);

		// When
		JsonPatch expected = Json.createDiff(source, target);

		// Then
		System.out.println(expected.toString());
		JsonArray jsonArray = expected.toJsonArray();
		assertTrue(jsonArray.size() == 4);
		assertTrue(jsonArray.contains(jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"diff-target\"}")));
		assertTrue(jsonArray.contains(jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"remove\",\"path\":\"/description\"}")));
		assertTrue(jsonArray.contains(jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"replace\",\"path\":\"/tags/0\",\"value\":\"tag2\"}")));
		assertTrue(jsonArray.contains(jsonToJsonObject(
				this.jsonb,
				"{\"op\":\"add\",\"path\":\"/tags/0\",\"value\":\"tag1\"}")));

		JsonObject patched = expected.apply(source);
		Project patchedObject = this.jsonb.fromJson(patched.toString(), Project.class);
		assertEquals(targetObject, patchedObject);
	}
}
