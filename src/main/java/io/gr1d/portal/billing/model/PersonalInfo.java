package io.gr1d.portal.billing.model;

import static java.util.Optional.ofNullable;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "personal_info")
public class PersonalInfo extends BaseModel {
	private static final long serialVersionUID = 933788145308532285L;
	
	public static final int TYPE_PERSON = 1;
	public static final int TYPE_COMPANY = 2;
	
	@Column(nullable = false)
	private int version;
	
	@NotEmpty
	@Column(nullable = false, length = 64)
	private String fullName;
	
	@NotEmpty
	@Email
	@Column(nullable = false, length = 64)
	private String email;
	
	@Min(1)
	@Max(2)
	@NotNull
	@Column(nullable = false)
	private Integer documentType;
	
	@NotEmpty
	@Column(nullable = false, length = 24)
	private String document;
	
	@NotEmpty
	@Pattern(regexp = "\\+\\d{3,23}")
	@Column(nullable = false, length = 24)
	private String phone;
	
	/**
	 * [MVP] Everyone is from Brazil for now - Country ISO Code (2 lowercase
	 * letters)
	 */
	@NotEmpty
	@Column(nullable = false, length = 4)
	private String nationality = "br";
	
	@Column(nullable = false)
	private LocalDate dateOfBirth;
	
	@NotNull
	@OneToOne(fetch = LAZY, cascade = ALL, optional = false)
	@JoinColumn(name = "personal_address_id", nullable = false)
	private PersonalAddress address;
	
	@Transient
	private transient boolean modified;
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(final String fullName) {
		modified = modified || this.fullName != null && fullName == null || !fullName.equals(this.fullName);
		
		this.fullName = fullName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(final String email) {
		modified = modified || this.email != null && email == null || !email.equals(this.email);
		
		this.email = email;
	}
	
	public Integer getType() {
		return documentType;
	}
	
	public void setType(final Integer documentType) {
		modified = modified || this.documentType != null && documentType == null
			|| !documentType.equals(this.documentType);
		
		this.documentType = documentType;
	}
	
	public String getDocument() {
		return document;
	}
	
	public void setDocument(final String document) {
		modified = modified || this.document != null && document == null || !document.equals(this.document);
		
		this.document = document;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(final String phone) {
		modified = modified || this.phone != null && phone == null || !phone.equals(this.phone);
		
		this.phone = phone;
	}
	
	public String getNationality() {
		return nationality;
	}
	
	public void setNationality(final String nationality) {
		modified = modified || this.nationality != null && nationality == null || !nationality.equals(this.nationality);
		
		this.nationality = nationality;
	}
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(final LocalDate dateOfBirth) {
		modified = modified || this.dateOfBirth != null && dateOfBirth == null || !dateOfBirth.equals(this.dateOfBirth);
		
		this.dateOfBirth = dateOfBirth;
	}
	
	public PersonalAddress getAddress() {
		return address;
	}
	
	public void setAddress(final PersonalAddress address) {
		modified = modified || this.address != null && address == null || !address.equals(this.address);
		
		this.address = address;
	}
	
	public boolean isModified() {
		return modified;
	}
	
	/**
	 * @return A new {@link PersonalInfo} copying all fields <b>except
	 *         {@link PersonalInfo#getId() id}</b> from another.
	 */
	public PersonalInfo newVersion() {
		if (!isModified()) {
			return this;
		}
		
		final PersonalInfo newObj = new PersonalInfo();
		
		newObj.setUuid(getUuid());
		newObj.setActive(isActive());
		newObj.setCreatedAt(getCreatedAt());
		newObj.setLastUpdateAt(getLastUpdateAt());
		
		newObj.version = version + 1;
		newObj.fullName = fullName;
		newObj.email = email;
		newObj.documentType = documentType;
		newObj.document = document;
		newObj.phone = phone;
		newObj.nationality = nationality;
		newObj.dateOfBirth = dateOfBirth;
		newObj.address = ofNullable(getAddress()).map(PersonalAddress::newVersion).orElse(null);
		newObj.modified = modified;
		
		return newObj;
	}
}
