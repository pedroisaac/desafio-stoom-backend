package br.com.stoom.desafio.service;

import br.com.stoom.desafio.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    Address save(Address address) throws Exception;

    Address update(Address address) throws Exception;

    Optional<List<Address>> listAll() throws Exception;

    Optional<Address> getById(long id) throws Exception;

    void delete(long id) throws Exception;

    void deleteAll() throws Exception;
}
