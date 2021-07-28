package br.com.stoom.desafio.service;

import br.com.stoom.desafio.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user) throws Exception;

    User update(User user) throws Exception;

    Optional<List<User>> listAll() throws Exception;

    Optional<User> findByUsername(String username) throws Exception;

    void delete(String username) throws Exception;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
