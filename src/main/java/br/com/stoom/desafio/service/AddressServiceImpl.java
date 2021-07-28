package br.com.stoom.desafio.service;

import br.com.stoom.desafio.exception.NotFoundException;
import br.com.stoom.desafio.model.Address;
import br.com.stoom.desafio.repository.AddressRepository;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Queue;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

	private final JmsTemplate jmsTemplate;

	private final Queue queue;

	private final AddressRepository repository;

	public AddressServiceImpl(AddressRepository repository, JmsTemplate jmsTemplate, Queue queue) {
		this.repository = repository;
		this.jmsTemplate = jmsTemplate;
		this.queue = queue;
	}

	@Override
	@Transactional
	public Address save(Address address) throws Exception {
		if (this.repository.existsAddressWithCoordinates(address.getLatitude(), address.getLongitude())) {
			throw new Exception("Já existe endereço cadastrado com essas coordenadas");
		}

		Address _address = repository.save(address);

		if (Objects.isNull(_address.getLatitude()) || Objects.isNull(_address.getLongitude()))
			getCoordinatesGeocodingJMS(_address);

		return _address;
	}

	//envia o endereço pra fila de consulta de coordenadas
	public void getCoordinatesGeocodingJMS(Address address) {
		this.jmsTemplate.convertAndSend(queue, address);
	}

	@Override
	public Address update(Address address) throws NotFoundException {
		Optional<Address> addressData = this.repository.findById(address.getId());

		if (addressData.isPresent()) {
			Address _address = addressData.get();
			_address.setStreetName(address.getStreetName());
			_address.setNumber(address.getNumber());
			_address.setComplement(address.getComplement());
			_address.setNeighbourhood(address.getNeighbourhood());
			_address.setCity(address.getCity());
			_address.setState(address.getState());
			_address.setCountry(address.getCountry());
			_address.setZipcode(address.getZipcode());
			_address.setLatitude(address.getLatitude());
			_address.setLongitude(address.getLongitude());

			repository.save(_address);
			return _address;
		} else {
			throw new NotFoundException("Endereço não encontrado com o ID: " + address.getId());
		}
	}

	@Override
	public Optional<List<Address>> listAll() {
		return Optional.of(this.repository.findAll());
	}

	@Override
	public Optional<Address> getById(long id) throws NotFoundException {
		Optional<Address> addressData = this.repository.findById(id);
		if (addressData.isPresent()) {
			return addressData;
		} else {
			throw new NotFoundException("Endereço não encontrado com o ID: " + id);
		}
	}

	@Override
	public void delete(long id) throws NotFoundException {
		Optional<Address> addressData = this.repository.findById(id);

		if (addressData.isPresent()) {
			this.repository.delete(addressData.get());
		} else {
			throw new NotFoundException("Endereço não encontrado com o ID: " + id);
		}
	}

	@Override
	public void deleteAll() {
		this.repository.deleteAll();
	}

}
