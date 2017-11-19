package org.ksug.json.fixture;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private List<String> tags;
	private User creator;

	@Builder
	@ConstructorProperties({"name", "description", "policy", "tags", "creator"})
	public Project(String name, String description, String policy, List<String> tags, User creator) {
		this.setName(name);
		this.setDescription(description);
		this.setPolicy(policy);
		this.setTags(tags);
		this.setCreator(creator);
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

	public List<String> getTags() {
		return Collections.unmodifiableList(this.tags);
	}

	public void setTags(List<String> tags) {
		if (tags == null || tags.isEmpty()) {
			this.tags = Collections.emptyList();
		} else{
			this.tags = new ArrayList<>(tags);
		}
	}
}
