package org.ksug.json.test.jsonp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.spi.JsonProvider;

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
	private JsonProvider jsonProvider;

	private Project object;

	@BeforeEach
	void setUp() {
		this.jsonb = JsonbBuilder.create();
		// johnzon 은 createObjectBuilder 에서 nested map 을 parsing 할 수 없음
		this.jsonProvider = new org.glassfish.json.JsonProviderImpl();
		this.object = Project.builder()
				.name("merge-patch")
				.description("json merge patch")
				.creator(User.builder()
						.name("user")
						.age(30)
						.build())
				.build();
	}

	@Test
	void jsonMergePatch() {
		// Given
		JsonObject target = this.objectToJsonObject(this.object);

		String mergePatchJson = "{\"name\":\"patched\", \"policy\":null, \"creator\":{\"name\":null}}";
		JsonObject mergePatch = this.jsonToJsonObject(mergePatchJson);
		JsonMergePatch sut = Json.createMergePatch(mergePatch);

		// When
		JsonValue result = sut.apply(target);
		String expected = result.asJsonObject().toString();

		// Then
		System.out.println(expected);
		DocumentContext document = JsonPath.parse(expected);
		assertEquals("patched", document.read("$.name", String.class));
		assertEquals(this.object.getDescription(), document.read("$.description", String.class));
		assertThrows(PathNotFoundException.class, () -> document.read("$.policy"));
		assertThrows(PathNotFoundException.class, () -> document.read("$.creator.name"));
		assertSame(this.object.getCreator().getAge(), document.read("$.creator.age", Integer.class));

		Project project = this.jsonb.fromJson(expected, Project.class);
		assertEquals("patched", project.getName());
		assertEquals(this.object.getDescription(), project.getDescription());
		assertNull(project.getPolicy());
		assertNull(project.getCreator().getName());
		assertSame(this.object.getCreator().getAge(), project.getCreator().getAge());
	}

	private JsonObject objectToJsonObject(Project object) {
		String json = this.jsonb.toJson(object);
		return this.jsonToJsonObject(json);
	}

	@SuppressWarnings("unchecked")
	private JsonObject jsonToJsonObject(String json) {
		Map<String, Object> map = this.jsonb.fromJson(json, Map.class);
		return this.jsonProvider.createObjectBuilder(map).build();
	}
}
