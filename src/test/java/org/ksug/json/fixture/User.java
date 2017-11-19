package org.ksug.json.fixture;

import java.beans.ConstructorProperties;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by mhyeon.lee on 2017. 11. 19..
 *
 * - 공개범위와 deserialize 지원 이슈
 *    > yasson 은 기본 생성자로 PUBLIC, PROTECTED 공개 범위가 필요하다.
 *    > yasson 은 setter 로 PUBLIC 공개 범위가 필요하다.
 *    > genson 은 기본 생성자로 PUBLIC, PACKAGE-PUBLIC 공개 범위가 필요하다.
 *    > genson 은 setter 로 PUBLIC, PACKAGE-PUBLIC 공개 범위가 필요하다.
 */
@Setter
@NoArgsConstructor
public class User {
	private String name;
	private int age;

	@Builder
	@ConstructorProperties({"name", "age"})
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
