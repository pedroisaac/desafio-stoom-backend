package br.com.stoom.desafio.config;

import br.com.stoom.desafio.model.Address;
import br.com.stoom.desafio.model.User;
import br.com.stoom.desafio.service.AddressService;
import br.com.stoom.desafio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class InitialDbConfig {

	@Autowired
	private AddressService addressService;

	@Autowired
	private UserService userService;

	@PostConstruct
	public void init() throws Exception {
		mockUserJWT();
		mockAddress();
	}

	private void mockUserJWT() throws Exception {
		//password=password
		User admin = new User("stoom", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
		userService.save(admin);
	}


	private void mockAddress() throws Exception {
		Address addressWithCoordinates = Address.builder()
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

		Address addressWithoutCoordinates = Address.builder()
				.streetName("Rua Zuneide Aparecida Marin")
				.number("43")
				.neighbourhood("Jardim Santa Genebra II")
				.city("Campinas")
				.state("SP")
				.country("Brasil")
				.zipcode("13084-780")
				//.latitude(0.0)
				//.longitude(0.0)
				.build();

		addressService.save(addressWithCoordinates);
		addressService.save(addressWithoutCoordinates);
	}
}
