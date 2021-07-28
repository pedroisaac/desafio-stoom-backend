package br.com.stoom.desafio.service;

import br.com.stoom.desafio.exception.NotFoundException;
import br.com.stoom.desafio.model.User;
import br.com.stoom.desafio.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepository repository;

	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public User save(User user) throws Exception {
		if (!this.repository.usernameAvaliable(user.getUsername())) {
			throw new Exception("Nome de usuário indisponível");
		}

		User _user = repository.save(user);
		return _user;
	}

	@Override
	public User update(User user) throws NotFoundException {
		Optional<User> userData = Optional.ofNullable(this.repository.findByUsername(user.getUsername()));

		if (userData.isPresent()) {
			User _user = userData.get();
			_user.setUsername(user.getUsername());
			_user.setPassword(user.getPassword());
			_user.setEnabled(user.isEnabled());

			repository.save(_user);
			return _user;
		} else {
			throw new NotFoundException("Usuário não encontrado com o Login: " + user.getUsername());
		}
	}

	@Override
	public Optional<List<User>> listAll() {
		return Optional.of(this.repository.findAll());
	}

	@Override
	public Optional<User> findByUsername(String username) throws NotFoundException {
		Optional<User> userData = Optional.ofNullable(this.repository.findByUsername(username));
		if (userData.isPresent()) {
			return userData;
		} else {
			throw new NotFoundException("Usuário não encontrado com o Login: " + username);
		}
	}

	@Override
	public void delete(String username) throws NotFoundException {
		Optional<User> userData = Optional.ofNullable(this.repository.findByUsername(username));

		if (userData.isPresent()) {
			this.repository.delete(userData.get());
		} else {
			throw new NotFoundException("Usuário não encontrado com o Login: " + username);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userData = Optional.ofNullable(this.repository.findByUsername(username));
		if (userData.isPresent()) {
			return userData.get();
		} else {
			throw new NotFoundException("Usuário não encontrado com o Login: " + username);
		}
	}
}
