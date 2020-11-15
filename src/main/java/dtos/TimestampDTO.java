package dtos;

import java.time.LocalDateTime;

public class TimestampDTO {
	private LocalDateTime created;
	private LocalDateTime updated;

	public TimestampDTO(LocalDateTime created, LocalDateTime updated) {
		this.created = created;
		this.updated = updated;
	}

	public TimestampDTO() {
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
}
