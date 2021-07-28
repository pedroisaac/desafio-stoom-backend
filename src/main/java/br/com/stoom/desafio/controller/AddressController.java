package br.com.stoom.desafio.controller;

import br.com.stoom.desafio.config.InitialDbConfig;
import br.com.stoom.desafio.model.Address;
import br.com.stoom.desafio.service.AddressService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AddressController {

	@Autowired
	AddressService addressService;

	@Autowired
	InitialDbConfig initialDbConfig;

	@GetMapping(path = "/enderecos", produces = "application/json")
	public ResponseEntity<?> getAll() {
		try {
			var enderecosData = addressService.listAll();

			if (enderecosData.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(enderecosData.get(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new JSONObject().put("error", e.getMessage()).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/endereco/{id}", produces = "application/json")
	public ResponseEntity<?> getById(@PathVariable("id") long id) {
		try {
			Optional<Address> enderecoData = addressService.getById(id);

			if (enderecoData.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(enderecoData.get(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new JSONObject().put("error", e.getMessage()).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(path = "/endereco", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> create(@RequestBody Map<String, String> payload) {
		try {
			Address newAddress = Address.builder()
					.streetName(payload.get("streetName"))
					.number(payload.get("number"))
					.complement(payload.get("complement"))
					.neighbourhood(payload.get("neighbourhood"))
					.city(payload.get("city"))
					.state(payload.get("state"))
					.country(payload.get("country"))
					.zipcode(payload.get("zipcode"))
					.latitude(StringUtils.isEmpty(payload.get("latitude")) ? null : Double.parseDouble(payload.get("latitude")))
					.longitude(StringUtils.isEmpty(payload.get("longitude")) ? null : Double.parseDouble(payload.get("longitude")))
					.build();

			addressService.save(newAddress);

			return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new JSONObject().put("error", e.getMessage()).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "/endereco/{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Map<String, String> payload) {
		try {
			Optional<Address> AddressData = addressService.getById(id);

			if (AddressData.isPresent()) {
				Address _address = AddressData.get();
				_address.setStreetName(payload.get("streetName"));
				_address.setNumber(payload.get("number"));
				_address.setComplement(payload.get("complement"));
				_address.setNeighbourhood(payload.get("neighbourhood"));
				_address.setCity(payload.get("city"));
				_address.setState(payload.get("state"));
				_address.setCountry(payload.get("country"));
				_address.setZipcode(payload.get("zipcode"));
				_address.setLatitude(StringUtils.isEmpty(payload.get("latitude")) ? null : Double.parseDouble(payload.get("latitude")));
				_address.setLongitude(StringUtils.isEmpty(payload.get("longitude")) ? null : Double.parseDouble(payload.get("longitude")));

				return new ResponseEntity<>(addressService.save(_address), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new JSONObject().put("error", e.getMessage()).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/endereco/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		try {
			addressService.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(new JSONObject().put("error", e.getMessage()).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
