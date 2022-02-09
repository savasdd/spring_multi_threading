package com.multi.thread.services;

import com.multi.thread.model.User;
import com.multi.thread.repostory.UserRepostory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private UserRepostory repostory;

    @Async
    public CompletableFuture<List<User>> save(MultipartFile file) throws Exception {
        Long start = System.currentTimeMillis();
        List<User> list = parseCSV(file);
        log.info("User listesi => " + list.size() + " " + Thread.currentThread().getName());
        list = repostory.saveAll(list);
        Long end = System.currentTimeMillis();
        log.info("Gecen Zaman: " + (end - start));
        return CompletableFuture.completedFuture(list);
    }

    @Async
    public CompletableFuture<List<User>> getAll() {
        log.info("Get All User " + Thread.currentThread().getName());
        return CompletableFuture.completedFuture(repostory.findAll());
    }

    public List<User> parseCSV(MultipartFile file) throws Exception {
        List<User> list = new ArrayList<>();
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String say;
                while ((say = reader.readLine()) != null) {
                    String[] data = say.split(",");
                    User user = new User();
                    user.setFirstName(data[1]);
                    user.setLastName(data[2]);
                    user.setEmail(data[3]);
                    user.setGender(data[4]);
                    user.setIpAddress(data[5]);
                    list.add(user);
                }
                return list;
            }
        } catch (Exception e) {
            throw new Exception("CSV File Hatası Oluştu..", e);
        }
    }
}
