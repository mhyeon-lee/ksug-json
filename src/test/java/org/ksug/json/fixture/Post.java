package org.ksug.json.fixture;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by mhyeon.lee on 2017. 11. 20..
 */
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Post {
	private Long postNo;
	private String title;
	private String content;
	private Date dueDate;
	private List<Comment> comments = new ArrayList<>();
	private User writer;
	private ZonedDateTime createdAt;

	@Builder
	public Post(
			Long postNo,
			String title,
			String content,
			Date dueDate,
			List<Comment> comments,
			User writer,
			ZonedDateTime createdAt) {
		this.setPostNo(postNo);
		this.setTitle(title);
		this.setContent(content);
		this.setDueDate(dueDate);
		this.setComments(comments);
		this.setWriter(writer);
		this.setCreatedAt(createdAt);
	}

	public Long getPostNo() {
		return this.postNo;
	}

	public String getTitle() {
		return this.title;
	}

	public Optional<String> getContent() {
		return Optional.ofNullable(this.content);
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public User getWriter() {
		return this.writer;
	}

	public ZonedDateTime getCreatedAt() {
		return this.createdAt;
	}

	@Setter
	@NoArgsConstructor
	@EqualsAndHashCode
	public static class Comment {
		private String body;
		private User writer;
		private ZonedDateTime createdAt;

		@Builder
		public Comment(String body, User writer, ZonedDateTime createdAt) {
			this.setBody(body);
			this.setWriter(writer);
			this.setCreatedAt(createdAt);
		}

		public String getBody() {
			return this.body;
		}

		public User getWriter() {
			return this.writer;
		}

		public ZonedDateTime getCreatedAt() {
			return this.createdAt;
		}
	}
}
