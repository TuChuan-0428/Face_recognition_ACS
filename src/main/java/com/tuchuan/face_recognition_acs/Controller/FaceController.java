package com.tuchuan.face_recognition_acs.Controller;

import com.tuchuan.face_recognition_acs.Common.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/common")
public class FaceController {
    String BasePath = System.getProperty("user.dir");
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        File dir = new File(BasePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        val s = UUID.randomUUID().toString() + suffix;
        try {
            log.info("dir:{}",dir);
            file.transferTo(new File(BasePath + s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(s);
    }
}
