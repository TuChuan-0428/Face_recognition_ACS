package com.tuchuan.face_recognition_acs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableTransactionManagement
@SpringBootApplication
public class FaceRecognitionAcsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceRecognitionAcsApplication.class, args);
    }

}
