package br.com.stoom.desafio.repository;

import br.com.stoom.desafio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

	@Query("select case when count(u) <= 0 then true else false end from User u where u.username like :username")
	boolean usernameAvaliable(@Param("username") String username);

	@Query("select u from User u where u.username like :username")
	User findByUsername(@Param("username") String username);
}
