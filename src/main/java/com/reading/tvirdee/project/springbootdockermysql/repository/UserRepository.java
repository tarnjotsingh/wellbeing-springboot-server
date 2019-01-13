package com.reading.tvirdee.project.springbootdockermysql.repository;

import com.reading.tvirdee.project.springbootdockermysql.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
}
