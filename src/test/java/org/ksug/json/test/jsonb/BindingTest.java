package org.ksug.json.test.jsonb;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ksug.json.fixture.Post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.owlike.genson.Genson;

/**
 * Created by mhyeon.lee on 2017. 11. 20..
 */
public class BindingTest {
	private Jsonb jsonb;
	private ObjectMapper jackson;
	private Gson gson;
	private Genson genson;

	@BeforeEach
	void setUp() {
		this.jsonb = JsonbBuilder.create();
		this.jackson = new ObjectMapper()
				.registerModules(new Jdk8Module(), new JavaTimeModule());
		this.gson = new Gson();
		this.genson = new Genson();
	}

	@Test
	void optional() throws JsonProcessingException {
		// Given
		Post post = Post.builder()
				.content("content")
				.build();

		// When
		String jsonbResult = this.jsonb.toJson(post);
		String jacksonResult = this.jackson.writeValueAsString(post);
		String gsonResult = this.gson.toJson(post);
		String gensonResult = this.genson.serialize(post);

		// Then
		System.out.println("jsonb: " + jsonbResult);
		System.out.println("jackson: " + jacksonResult);
		System.out.println("gson: " + gsonResult);
		System.out.println("genson: " + gensonResult);

		DocumentContext jsonbDocuemnt = JsonPath.parse(jsonbResult);
		assertEquals("content", jsonbDocuemnt.read("$.content", String.class));

		DocumentContext jacksonDocument = JsonPath.parse(jacksonResult);
		assertEquals("content", jacksonDocument.read("$.content", String.class));

		DocumentContext gsonDocument = JsonPath.parse(gsonResult);
		assertEquals("content", gsonDocument.read("$.content", String.class));

		DocumentContext gensonDocument = JsonPath.parse(gensonResult);
		assertEquals("{present=true}", gensonDocument.read("$.content").toString());
	}

	@Test
	void optional_empty() throws JsonProcessingException {
		// Given
		Post post = Post.builder().build();

		// When
		String jsonbResult = this.jsonb.toJson(post);
		String jacksonResult = this.jackson.writeValueAsString(post);
		String gsonResult = this.gson.toJson(post);
		String gensonResult = this.genson.serialize(post);

		// Then
		System.out.println("jsonb: " + jsonbResult);
		System.out.println("jackson: " + jacksonResult);
		System.out.println("gson: " + gsonResult);
		System.out.println("genson: " + gensonResult);

		DocumentContext jsonbDocument = JsonPath.parse(jsonbResult);
		assertThrows(PathNotFoundException.class, () -> jsonbDocument.read("$.content", String.class));

		DocumentContext jacksonDocument = JsonPath.parse(jacksonResult);
		assertNull(jacksonDocument.read("$.content", String.class));

		DocumentContext gsonDocument = JsonPath.parse(gsonResult);
		assertThrows(PathNotFoundException.class, () -> gsonDocument.read("$.content", String.class));

		DocumentContext gensonDocument = JsonPath.parse(gensonResult);
		assertEquals("{present=false}", gensonDocument.read("$.content").toString());
	}

	@Test
	void zonedDateTime() throws JsonProcessingException {
		// Given
		Post post = Post.builder()
				.createdAt(ZonedDateTime.now())
				.build();

		// When
		String jsonbResult = this.jsonb.toJson(post);
		String jacksonResult = this.jackson.writeValueAsString(post);
		String gsonResult = this.gson.toJson(post);
		String gensonResult = this.genson.serialize(post);

		// Then
		System.out.println("jsonb: " + jsonbResult);
		System.out.println("jackson: " + jacksonResult);
		System.out.println("gson: " + gsonResult);
		System.out.println("genson: " + gensonResult);

		DocumentContext jsonbDocuemnt = JsonPath.parse(jsonbResult);
		assertTrue(String.class == jsonbDocuemnt.read("$.createdAt").getClass());

		DocumentContext jacksonDocument = JsonPath.parse(jacksonResult);
		assertTrue(String.class != jacksonDocument.read("$.createdAt").getClass());

		DocumentContext gsonDocument = JsonPath.parse(gsonResult);
		assertTrue(String.class != gsonDocument.read("$.createdAt").getClass());

		DocumentContext gensonDocument = JsonPath.parse(gensonResult);
		assertTrue(String.class != gensonDocument.read("$.createdAt").getClass());
	}

	@Test
	void date() throws JsonProcessingException {
		// Given
		Post post = Post.builder()
				.dueDate(new Date())
				.build();

		// When
		String jsonbResult = this.jsonb.toJson(post);
		String jacksonResult = this.jackson.writeValueAsString(post);
		String gsonResult = this.gson.toJson(post);
		String gensonResult = this.genson.serialize(post);

		// Then
		System.out.println("jsonb: " + jsonbResult);
		System.out.println("jackson: " + jacksonResult);
		System.out.println("gson: " + gsonResult);
		System.out.println("genson: " + gensonResult);

		DocumentContext jsonbDocuemnt = JsonPath.parse(jsonbResult);
		assertTrue(String.class == jsonbDocuemnt.read("$.dueDate").getClass());

		DocumentContext jacksonDocument = JsonPath.parse(jacksonResult);
		assertTrue(Long.class == jacksonDocument.read("$.dueDate").getClass());

		DocumentContext gsonDocument = JsonPath.parse(gsonResult);
		assertTrue(String.class == gsonDocument.read("$.dueDate").getClass());

		DocumentContext gensonDocument = JsonPath.parse(gensonResult);
		assertTrue(Long.class == gensonDocument.read("$.dueDate").getClass());
	}
}
