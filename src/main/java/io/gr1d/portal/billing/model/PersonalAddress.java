package io.gr1d.portal.billing.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "personal_address")
public class PersonalAddress extends BaseModel {
	private static final long serialVersionUID = 9042512715453256043L;
	
	@Column(nullable = false)
	private int version;
	
	@NotEmpty
	@Size(max = 64)
	private String street;
	
	@Size(max = 64)
	private String number;
	
	@NotEmpty
	@Size(max = 64)
	private String neighborhood;
	
	@Size(max = 64)
	private String complement;
	
	@NotEmpty
	@Size(max = 64)
	private String city;
	
	@NotEmpty
	@Size(max = 64)
	private String county;
	
	@NotEmpty
	@Size(max = 64)
	private String postalCode;
	
	@NotEmpty
	@Size(min = 2, max = 4)
	private String country;
	
	@DecimalMin(value = "-90.0", inclusive = true)
	@DecimalMax(value = "90.0", inclusive = false)
	private BigDecimal latitude;
	
	@DecimalMin(value = "-180.0", inclusive = true)
	@DecimalMax(value = "180.0", inclusive = false)
	private BigDecimal longitude;
	
	@Transient
	private transient boolean modified;
	
	public boolean isModified() {
		return modified;
	}
	
	/** Endereço: Logradouro */
	public String getStreet() {
		return street;
	}
	
	public void setStreet(final String street) {
		modified = modified || this.street != null && street == null || !street.equals(this.street);
		this.street = street;
	}
	
	/** Endereço: Número (pode ser em branco) */
	public String getNumber() {
		return number;
	}
	
	public void setNumber(final String number) {
		modified = modified || this.number != null && number == null || !number.equals(this.number);
		this.number = number;
	}
	
	/** Endereço: Bairro */
	public String getNeighborhood() {
		return neighborhood;
	}
	
	public void setNeighborhood(final String neighborhood) {
		modified = modified || this.neighborhood != null && neighborhood == null
			|| !neighborhood.equals(this.neighborhood);
		this.neighborhood = neighborhood;
	}
	
	/** Endereço: Complemento */
	public String getComplement() {
		return complement;
	}
	
	public void setComplement(final String complement) {
		modified = modified || this.complement != null && complement == null || !complement.equals(this.complement);
		this.complement = complement;
	}
	
	/** Endereço: Cidade */
	public String getCity() {
		return city;
	}
	
	public void setCity(final String city) {
		modified = modified || this.city != null && city == null || !city.equals(this.city);
		this.city = city;
	}
	
	/** Endereço: Estado */
	public String getCounty() {
		return county;
	}
	
	public void setCounty(final String county) {
		modified = modified || this.county != null && county == null || !county.equals(this.county);
		this.county = county;
	}
	
	/** Endereço: CEP */
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(final String postalCode) {
		modified = modified || this.postalCode != null && postalCode == null || !postalCode.equals(this.postalCode);
		this.postalCode = postalCode;
	}
	
	/** Endereço: País */
	public String getCountry() {
		return country;
	}
	
	public void setCountry(final String country) {
		modified = modified || this.country != null && country == null || !country.equals(this.country);
		this.country = country;
	}
	
	/** Endereço: Latitude */
	public BigDecimal getLatitude() {
		return latitude;
	}
	
	public void setLatitude(final BigDecimal latitude) {
		modified = modified || this.latitude != null && latitude == null || !latitude.equals(this.latitude);
		this.latitude = latitude;
	}
	
	/** Endereço: Longitude */
	public BigDecimal getLongitude() {
		return longitude;
	}
	
	public void setLongitude(final BigDecimal longitude) {
		modified = modified || this.longitude != null && longitude == null || !longitude.equals(this.longitude);
		this.longitude = longitude;
	}
	
	/**
	 * @return A new {@link PersonalAddress} copying all fields <b>except
	 *         {@link PersonalInfo#getId() id}</b> from another.
	 */
	public PersonalAddress newVersion() {
		// TODO On the MVP everyone will always use the same address
		if (getId() == 1L) {
			return this;
		}
		
		if (!isModified()) {
			return this;
		}
		
		final PersonalAddress newObj = new PersonalAddress();
		
		newObj.setUuid(getUuid());
		newObj.setActive(isActive());
		newObj.setCreatedAt(getCreatedAt());
		newObj.setLastUpdateAt(getLastUpdateAt());
		
		newObj.version = version + 1;
		newObj.street = street;
		newObj.number = number;
		newObj.neighborhood = neighborhood;
		newObj.complement = complement;
		newObj.city = city;
		newObj.county = county;
		newObj.postalCode = postalCode;
		newObj.country = country;
		newObj.latitude = latitude;
		newObj.longitude = longitude;
		newObj.modified = modified;
		
		return newObj;
	}
}
