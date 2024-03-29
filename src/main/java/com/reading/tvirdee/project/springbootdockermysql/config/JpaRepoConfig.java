package com.reading.tvirdee.project.springbootdockermysql.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "com.reading.tvirdee.project.springbootdockermysql.repository")
@EnableTransactionManagement // Need to enable transaction management if you want to use custom derived repo methods that require transactions to work.
@Configuration
public class JpaRepoConfig {

}
