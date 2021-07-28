package br.com.stoom.desafio.controller;

import br.com.stoom.desafio.model.Address;
import br.com.stoom.desafio.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AddressController.class)
@ActiveProfiles("test")
class AddressControllerTest {


	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AddressService addressService;

	@Autowired
	private ObjectMapper objectMapper;

	private List<Address> addresses;

	@BeforeEach
	void setUp() throws Exception {
		this.addresses = new ArrayList<>();

		Address addressMock1 = Address.builder()
				.streetName("Quadra 408 Norte Alameda 8")
				.number("14")
				.neighbourhood("Plano Diretor Norte")
				.city("Palmas")
				.state("TO")
				.country("Brasil")
				.zipcode("77006-512")
				.latitude(-10.170282)
				.longitude(-48.3108818)
				.build();

		Address addressMock2 = Address.builder()
				.streetName("Rua Zuneide Aparecida Marin")
				.number("43")
				.neighbourhood("Jardim Santa Genebra II")
				.city("Campinas")
				.state("SP")
				.country("Brasil")
				.zipcode("13084-780")
				.latitude(0.0)
				.longitude(0.0)
				.build();

		this.addresses.add(addressMock1);
		this.addresses.add(addressMock2);
	}

	@Test
	void shouldFetchAllAddresses() throws Exception {

		given(addressService.listAll().get()).willReturn(addresses);

		this.mockMvc.perform(get("/api/enderecos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(addresses.size())));
	}

	@Test
	void shouldFetchOneAddressById() throws Exception {
		final Address addressMock = Address.builder()
				.streetName("Quadra 408 Norte Alameda 8")
				.number("14")
				.neighbourhood("Plano Diretor Norte")
				.city("Palmas")
				.state("TO")
				.country("Brasil")
				.zipcode("77006-512")
				.latitude(-10.170282)
				.longitude(-48.3108818)
				.build();

		final Long addressId = 1L;

		given(addressService.getById(addressId)).willReturn(Optional.of(addressMock));

		this.mockMvc.perform(get("/api/endereco/{id}", addressId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city", is(addressMock.getCity())))
				.andExpect(jsonPath("$.country", is(addressMock.getCountry())));
	}

	@Test
	void shouldReturn404WhenFindAddressById() throws Exception {
		final Long addressId = 1L;
		given(addressService.getById(addressId)).willReturn(Optional.empty());

		this.mockMvc.perform(get("/api/endereco/{id}", addressId))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldCreateNewAddress() throws Exception {
		given(addressService.save(any(Address.class))).willAnswer((invocation) -> invocation.getArgument(0));

		final Address addressMock = Address.builder()
				.streetName("Quadra 408 Norte Alameda 8")
				.number("14")
				.neighbourhood("Plano Diretor Norte")
				.city("Palmas")
				.state("TO")
				.country("Brasil")
				.zipcode("77006-512")
				.latitude(-10.170282)
				.longitude(-48.3108818)
				.build();

		this.mockMvc.perform(post("/api/endereco")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addressMock)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.city", is(addressMock.getCity())))
				.andExpect(jsonPath("$.country", is(addressMock.getCountry())))
				.andExpect(jsonPath("$.zipcode", is(addressMock.getZipcode())))
		;
	}

	@Test
	void shouldReturn400WhenCreateNewAddressWithoutZipcode() throws Exception {
		final Address addressMock = Address.builder()
				.streetName("Quadra 408 Norte Alameda 8")
				.number("14")
				.neighbourhood("Plano Diretor Norte")
				.city("Palmas")
				.state("TO")
				.country("Brasil")
				//.zipcode("77006-512")
				.latitude(-10.170282)
				.longitude(-48.3108818)
				.build();

		this.mockMvc.perform(post("/api/endereco")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addressMock)))
				.andExpect(status().isBadRequest())
				.andExpect(header().string("Content-Type", is("application/problem+json")))
				.andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
				.andExpect(jsonPath("$.title", is("Constraint Violation")))
				.andExpect(jsonPath("$.status", is(400)))
				.andExpect(jsonPath("$.violations", hasSize(1)))
				.andExpect(jsonPath("$.violations[0].field", is("zipcode")))
				.andExpect(jsonPath("$.violations[0].message", is("Informe o CEP")))
				.andReturn()
		;
	}

	@Test
	void shouldUpdateAddress() throws Exception {
		Long addressId = 1L;
		final Address addressMock = Address.builder()
				.streetName("Quadra 408 Norte Alameda 8")
				.number("14")
				.neighbourhood("Plano Diretor Norte")
				.city("Palmas")
				.state("TO")
				.country("Brasil")
				.zipcode("77006-512")
				.latitude(-10.170282)
				.longitude(-48.3108818)
				.build();

		given(addressService.getById(addressId)).willReturn(Optional.of(addressMock));
		given(addressService.update(any(Address.class))).willAnswer((invocation) -> invocation.getArgument(0));

		this.mockMvc.perform(put("/api/endereco/{id}", addressMock.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addressMock)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.city", is(addressMock.getCity())))
				.andExpect(jsonPath("$.country", is(addressMock.getCountry())))
				.andExpect(jsonPath("$.zipcode", is(addressMock.getZipcode())));

	}
}
