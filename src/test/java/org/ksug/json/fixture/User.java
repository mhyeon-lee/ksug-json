package org.ksug.json.fixture;

import java.beans.ConstructorProperties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by mhyeon.lee on 2017. 11. 19..
 */
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class User {
	private String name;
	private int age;

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
