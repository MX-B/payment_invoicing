package io.gr1d.portal.billing.model.enumerations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

/**
 * Base class for Enumeration-like entities
 * 
 * @author Rafael M. Lins
 *
 */
@MappedSuperclass
public class BaseEnum implements Serializable {
	private static final long serialVersionUID = -2640892240711768321L;

	public static final Map<String, BaseEnum> ENUMS_MAP = new HashMap<>();
	
	static void addEnum(final BaseEnum element) {
		ENUMS_MAP.put(element.getClass().getName() + "_" + element.getName().toUpperCase(), element);
	}
	
	static <T extends BaseEnum> T getEnum(final String name, final Class<T> enumType) {
		final BaseEnum element = ENUMS_MAP.get(enumType.getName() + "_" + name.toUpperCase());
		return (T) element;
	}
	
	@Id
	private Long id;
	
	@NotEmpty
	@Column(nullable = false, length = 32)
	private String name;
	
	@Transient
	private String description;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String name() {
		return getName();
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return String.format("%s.%s", getClass().getSimpleName(), name);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		
		return getClass().equals(obj.getClass()) && id != null && ((BaseEnum) obj).id != null
			&& id.equals(((BaseEnum) obj).id);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = getClass().getName().hashCode();
		result = prime * result + (id == null ? 0 : id.hashCode());
		return result;
	}
}
