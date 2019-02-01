package com.reading.tvirdee.project.springbootdockermysql.resource.util;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class ResponseUtil {

    public static ResponseEntity<?> checkNullGetRequest(Optional<?> toCheck, String entName, String id) {

        if(toCheck.isPresent()){
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityGetAlert(entName,id))
                    .body(toCheck.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
