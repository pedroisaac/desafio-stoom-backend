package br.com.stoom.desafio.repository;

import br.com.stoom.desafio.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Long> {

	@Query("select case when count(a)> 0 then true else false end from Address a where a.latitude = :latitude and a.longitude = :longitude")
	boolean existeEnderecoComCoordenadas(@Param("latitude") Double latitude, @Param("longitude") Double longitude);
}
