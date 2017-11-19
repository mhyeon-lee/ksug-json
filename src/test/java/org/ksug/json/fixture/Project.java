package org.ksug.json.fixture;

import java.beans.ConstructorProperties;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by mhyeon.lee on 2017. 11. 19..
 */
@Setter
@NoArgsConstructor
public class Project {
	private String name;
	private String description;
	private String policy;
	private User creator;

	@Builder
	@ConstructorProperties({"name", "description", "policy", "creator"})
	public Project(String name, String description, String openPolicy, User creator) {
		this.name = name;
		this.description = description;
		this.policy = policy;
		this.creator = creator;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public User getCreator() {
		return this.creator;
	}

	public String getPolicy() {
		return this.policy;
	}
}
