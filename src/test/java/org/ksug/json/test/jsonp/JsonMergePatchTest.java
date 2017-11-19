package org.ksug.json.test.jsonp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonObject;
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
import com.jayway.jsonpath.PathNotFoundException;

/**
 * Created by mhyeon.lee on 2017. 11. 19..
 */
@DisplayName("Json Merge Patch Test")
class JsonMergePatchTest {
	private Jsonb jsonb;
	private Project object;
	private JsonObject target;

	@SuppressWarnings("unchecked")
	@BeforeEach
	void setUp() {
		this.jsonb = JsonbBuilder.create();
		this.object = Project.builder()
				.name("merge-patch")
				.description("json merge patch")
				.creator(User.builder()
						.name("user")
						.age(30)
						.build())
				.build();

		String json = this.jsonb.toJson(this.object);
		Map<String, Object> map = this.jsonb.fromJson(json, Map.class);
		// johnzon 은 createObjectBuilder 에서 nested map 을 parsing 할 수 없음
		this.target = new org.glassfish.json.JsonProviderImpl()
				.createObjectBuilder(map)
				.build();
	}

	@Test
	void jsonMergePatch() {
		// Given
		String patchName = "patched";
		JsonObject patch = Json.createObjectBuilder()
				.add("name", patchName)
				.addNull("policy")
				.add("creator", Json.createObjectBuilder()
						.addNull("name")
						.build())
				.build();
		JsonMergePatch sut = Json.createMergePatch(patch);

		// When
		JsonValue result = sut.apply(this.target);
		String expected = result.asJsonObject().toString();

		// Then
		System.out.println(expected);
		DocumentContext document = JsonPath.parse(expected);
		assertAll("assertion for merge patch result.",
				() -> assertEquals(patchName, document.read("$.name", String.class)),
				() -> assertEquals(this.object.getDescription(), document.read("$.description", String.class)),
				() -> assertThrows(PathNotFoundException.class, () -> document.read("$.policy")),
				() -> assertThrows(PathNotFoundException.class, () -> document.read("$.creator.name")),
				() -> assertSame(
						this.object.getCreator().getAge(),
						document.read("$.creator.age", Integer.class)));

		Project project = this.jsonb.fromJson(expected, Project.class);
		assertAll("assertion deserialized object for merge patch result.",
				() -> assertEquals(patchName, project.getName()),
				() -> assertEquals(this.object.getDescription(), project.getDescription()),
				() -> assertNull(project.getPolicy()),
				() -> assertNull(project.getCreator().getName()),
				() -> assertSame(
						this.object.getCreator().getAge(),
						project.getCreator().getAge()));
	}
}
