package org.ksug.json.fixture;

import java.beans.ConstructorProperties;

/**
 * Created by mhyeon.lee on 2017. 11. 19..
 */
public class User {
	private final String name;
	private final int age;

	@ConstructorProperties( {"name", "age"})
	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return this.name;
	}

	public int getAge() {
		return this.age;
	}
}
