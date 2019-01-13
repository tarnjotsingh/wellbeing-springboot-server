package com.reading.tvirdee.project.springbootdockermysql.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.reading.tvirdee.project.springbootdockermysql.repository")
@Configuration
public class JpaRepoConfig {
}
