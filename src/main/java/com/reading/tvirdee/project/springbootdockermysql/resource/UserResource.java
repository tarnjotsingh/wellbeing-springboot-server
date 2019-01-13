package com.reading.tvirdee.project.springbootdockermysql.resource;

import com.reading.tvirdee.project.springbootdockermysql.entity.Users;
import com.reading.tvirdee.project.springbootdockermysql.repository.UserRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource{

    Logger logger = LoggerFactory.getLogger(UserResource.class);

    private UserRepository userRepository;
    //private PasswordEncoder passwordEncoder;

    public UserResource(UserRepository userRepository) {

        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String test() {
        String s = "This is a test bro";
        logger.info(s);
        return s;
    }

    @GetMapping(value = "/all")
    public List<Users> all() {
        logger.info("Returning all entities");
        return userRepository.findAll();
    }

//    @GetMapping(value = "/all/create")
//    public List<Users> create() {
//        Users user = new Users();
//        user.setLogin("tarnjotv");
//        user.setFirstName("Tarnjot");
//        user.setLastName("Virdee");
//
//        userRepository.save(user);
//
//        logger.debug(userRepository.findAll().toString());
//        return userRepository.findAll();
//    }

    @PostMapping(value = "/load")
    public List<Users> persist(@RequestBody final Users users) {
        userRepository.save(users);
        return userRepository.findAll();
    }


}
