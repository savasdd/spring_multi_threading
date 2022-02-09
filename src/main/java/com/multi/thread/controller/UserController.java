package com.multi.thread.controller;

import com.multi.thread.model.User;
import com.multi.thread.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private Service service;

    @PostMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseEntity saveUser(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files)
            service.save(file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users")
    public CompletableFuture<ResponseEntity> getUsers() {
        return service.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/users/thread", produces = "application/json")
    public ResponseEntity getAllUser() {
        CompletableFuture<List<User>> users1 = service.getAll();
        CompletableFuture<List<User>> users2 = service.getAll();
        CompletableFuture<List<User>> users3 = service.getAll();
        CompletableFuture.allOf(users1, users2, users3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
