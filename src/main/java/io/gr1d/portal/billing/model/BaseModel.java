package io.gr1d.portal.billing.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotEmpty;

import io.gr1d.portal.billing.util.ToString;

/**
 * Base class for the model classes.
 * 
 * @author Rafael M. Lins
 *
 */
@MappedSuperclass
public class BaseModel implements Serializable {
	private static final long serialVersionUID = -3870723261524137309L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 40)
	private String uuid;
	
	@Column(name = "created_at", nullable = false)
	private ZonedDateTime createdAt;
	
	@NotEmpty
	@Column(name = "created_by", nullable = false)
	private String createdBy;
	
	@Column(name = "last_update_at")
	private ZonedDateTime lastUpdateAt;
	
	@Column(name = "last_update_by")
	private String lastUpdateBy;
	
	@Column(nullable = false)
	private boolean active;
	
	@PrePersist
	public void beforeSave() {
		uuid = nextUuid();
		createdAt = ZonedDateTime.now();
	}
	
	protected String nextUuid() {
		return UUID.randomUUID().toString();
	}
	
	@PreUpdate
	public void beforeUpdate() {
		lastUpdateAt = ZonedDateTime.now();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	public ZonedDateTime getLastUpdateAt() {
		return lastUpdateAt;
	}

	public void setLastUpdateAt(final ZonedDateTime lastUpdateAt) {
		this.lastUpdateAt = lastUpdateAt;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(final String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BaseModel other = (BaseModel) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
