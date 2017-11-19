package org.ksug.json.test;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ksug.json.fixture.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mhyeon.lee on 2017. 11. 8..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@JsonTest
public class SpringBootJsonTests {
	@Autowired
	private JsonbTester<User> jsonb;
	@Autowired
	private JacksonTester<User> jackson;
	@Autowired
	private GsonTester<User> gson;

	private User object;
	private String json;

	@Before
	public void setUp() {
		this.object = new User("user", 30);
		this.json = "{\"name\" : \"user\", \"age\" : 30 }";
	}

	@Test
	public void jasonbSerialize() throws IOException {
		JsonContent<User> expected = this.jsonb.write(this.object);
		assertJson(expected);
	}

	@Test
	public void jasonbDeserialize() throws IOException {
		ObjectContent<User> expected = this.jsonb.parse(this.json);
		assertObject(expected);
	}

	@Test
	public void jacksonSerialize() throws IOException {
		JsonContent<User> expected = this.jackson.write(this.object);
		assertJson(expected);
	}

	@Test
	public void jacksonDeserialize() throws IOException {
		ObjectContent<User> expected = this.jackson.parse(this.json);
		assertObject(expected);
	}

	@Test
	public void gsonSerialize() throws IOException {
		JsonContent<User> expected = this.gson.write(this.object);
		assertJson(expected);
	}

	@Test
	public void gsonDeserialize() throws IOException {
		ObjectContent<User> expected = this.gson.parse(this.json);
		assertObject(expected);
	}

	private void assertObject(ObjectContent<User> userObject) {
		User user = userObject.getObject();
		assertThat(user.getName()).isEqualTo(this.object.getName());
		assertThat(user.getAge()).isEqualTo(this.object.getAge());
	}

	private void assertJson(JsonContent<User> json) {
		System.out.println(json.getJson());
		assertThat(json).extractingJsonPathStringValue("@.name").isEqualTo(this.object.getName());
		assertThat(json).extractingJsonPathNumberValue("@.age").isSameAs(this.object.getAge());
	}
}
