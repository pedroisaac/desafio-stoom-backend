package br.com.stoom.desafio.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "address") //uniqueConstraints = {@UniqueConstraint(columnNames = {"latitude", "longitude"})}
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "streetName")
	private String streetName;

	@Column(name = "number")
	private String number;

	@Column(name = "complement")
	private String complement;

	@Column(name = "neighbourhood")
	private String neighbourhood;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "country")
	private String country;

	@Column(name = "zipcode")
	private String zipcode;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Deprecated
	public Address() {
	}

	public Address(String streetName, String number, String complement, String neighbourhood, String city, String state, String country, String zipcode, Double latitude, Double longitude) {
		this.streetName = streetName;
		this.number = number;
		this.complement = complement;
		this.neighbourhood = neighbourhood;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipcode = zipcode;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public static AddressBuilder builder() {
		return new AddressBuilder();
	}

	public static class AddressBuilder {
		private String streetName;
		private String number;
		private String complement;
		private String neighbourhood;
		private String city;
		private String state;
		private String country;
		private String zipcode;
		private Double latitude;
		private Double longitude;

		private AddressBuilder() {
		}

		public AddressBuilder streetName(String streetName) {
			this.streetName = Objects.requireNonNull(streetName, "Informe o nome da rua");
			return this;
		}

		public AddressBuilder number(String number) {
			this.number = Objects.requireNonNull(number, "Informe o número do endereço");
			return this;
		}

		public AddressBuilder complement(String complement) {
			this.complement = complement;
			return this;
		}

		public AddressBuilder neighbourhood(String neighbourhood) {
			this.neighbourhood = Objects.requireNonNull(neighbourhood, "Informe o Bairro");
			return this;
		}

		public AddressBuilder city(String city) {
			this.city = Objects.requireNonNull(city, "Informe a cidade");
			return this;
		}

		public AddressBuilder state(String state) {
			this.state = Objects.requireNonNull(state, "Informe o estado");
			return this;
		}

		public AddressBuilder country(String country) {
			this.country = Objects.requireNonNull(country, "Informe o país");
			return this;
		}

		public AddressBuilder zipcode(String zipcode) {
			this.zipcode = Objects.requireNonNull(zipcode, "Informe o CEP");
			return this;
		}

		public AddressBuilder latitude(Double latitude) {
			this.latitude = latitude;
			return this;
		}

		public AddressBuilder longitude(Double longitude) {
			this.longitude = longitude;
			return this;
		}

		public Address build() throws Exception {
			Objects.requireNonNull(streetName, "Informe o nome da rua");
			Objects.requireNonNull(number, "Informe o número do endereço");
			Objects.requireNonNull(neighbourhood, "Informe a vizinhança");
			Objects.requireNonNull(city, "Informe a cidade");
			Objects.requireNonNull(state, "Informe o estado");
			Objects.requireNonNull(country, "Informe o país");
			Objects.requireNonNull(zipcode, "Informe o CEP");

			return new Address(streetName, number, complement, neighbourhood, city, state, country, zipcode, latitude, longitude);
		}
	}

	@Override
	public String toString() {
		return "Address{" +
				"id='" + id + "'" +
				", streetName='" + streetName + "'" +
				", number='" + number + "'" +
				", complement='" + complement + "'" +
				", neighbourhood='" + neighbourhood + "'" +
				", city='" + city + "'" +
				", state='" + state + "'" +
				", country='" + country + "'" +
				", zipcode='" + zipcode + "'" +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}

	public String toStringForGoogleGeocoding() {
		return streetName.replaceAll(" ", "+") +
				",+" + number +
				",+" + neighbourhood +
				",+" + city +
				",+" + state +
				",+" + country +
				",+" + zipcode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getNeighbourhood() {
		return neighbourhood;
	}

	public void setNeighbourhood(String neighbourhood) {
		this.neighbourhood = neighbourhood;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
