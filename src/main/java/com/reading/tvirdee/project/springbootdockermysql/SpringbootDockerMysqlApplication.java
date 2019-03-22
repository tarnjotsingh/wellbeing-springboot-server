package com.reading.tvirdee.project.springbootdockermysql;

import com.reading.tvirdee.project.springbootdockermysql.domain.Users;
import com.reading.tvirdee.project.springbootdockermysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringbootDockerMysqlApplication {

	private final UserRepository repo;
	private final PasswordEncoder encoder;

	@Autowired
	public SpringbootDockerMysqlApplication(UserRepository repo, PasswordEncoder encoder) {
		this.repo = repo;
		this.encoder = encoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDockerMysqlApplication.class, args);
	}

	@Autowired
	public void addDefaultUser() {
		if(repo.count() == 0) {
			repo.save(new Users("test", encoder.encode("test"), true));
		}
	}
}

