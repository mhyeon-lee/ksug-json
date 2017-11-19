package org.ksug.json.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ksug.json.fixture.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;

/**
 * Created by mhyeon.lee on 2017. 11. 19..
 */
@DisplayName("Simple Json Tests")
class JsonTests {
	private Jsonb jsonb;
	private ObjectMapper jackson;
	private Gson gson;
	private Genson genson;

	private User object;
	private String json;

	@BeforeEach
	void setUp() {
		this.jsonb = JsonbBuilder.create();
		this.jackson = new ObjectMapper();
		this.gson = new Gson();
		this.genson = new Genson();
		new GensonBuilder()
				.useRuntimeType(true)
				.useConstructorWithArguments(true)
				.create();

		this.object = new User("user", 30);
		this.json = "{\"name\" : \"user\", \"age\" : 30 }";
	}

	@Test
	@DisplayName("JSON-B serialize test.")
	void jsonbSerialize() {
		String expected = this.jsonb.toJson(this.object);
		assertJson(expected);
	}

	@Test
	@DisplayName("JSON-B deserialize test.")
	void jsonbDeserialize() {
		User expected = this.jsonb.fromJson(this.json, User.class);
		assertObject(expected);
	}

	@Test
	@DisplayName("Jackson serialize test.")
	void jacksonSerialize() throws IOException {
		String expected = this.jackson.writeValueAsString(this.object);
		assertJson(expected);
	}

	@Test
	@DisplayName("Jackson deserialize test.")
	void jacksonDeserialize() throws IOException {
		User expected = this.jackson.readValue(this.json, User.class);
		assertObject(expected);
	}

	@Test
	@DisplayName("Gson serialize test.")
	void gsonSerialize() {
		String expected = this.gson.toJson(this.object);
		assertJson(expected);
	}

	@Test
	@DisplayName("Gson deserialize test.")
	void gsonDeserialize() {
		User expected = this.gson.fromJson(this.json, User.class);
		assertObject(expected);
	}

	@Test
	@DisplayName("Genson serialize test.")
	void gensonSerialize() {
		String expected = this.genson.serialize(this.object);
		assertJson(expected);
	}

	@Test
	@DisplayName("Genson deserialize test.")
	void gensonDeserialize() {
		User expected = this.genson.deserialize(this.json, User.class);
		assertObject(expected);
	}

	private void assertObject(User user) {
		assertAll("Assertions for user.",
				() -> assertEquals(this.object.getName(), user.getName()),
				() -> assertEquals(this.object.getAge(), user.getAge()));
	}

	private void assertJson(String json) {
		System.out.println(json);
		DocumentContext document = JsonPath.parse(json);
		assertAll("Assertions for json.",
				() -> assertEquals(this.object.getName(), document.read("$.name", String.class)),
				() -> assertEquals(this.object.getAge(), document.read("$.age", Integer.class).intValue()));
	}
}
