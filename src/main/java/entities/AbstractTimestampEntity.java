package entities;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AbstractTimestampEntity {

	private LocalDateTime created;

	private LocalDateTime updated;

	@PrePersist
	private void onCreate() {
		created = LocalDateTime.now();
	}

	@PreUpdate
	private void onUpdate() {
		updated = LocalDateTime.now();
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}
}
