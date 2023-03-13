package com.tuchuan.face_recognition_acs.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tuchuan.face_recognition_acs.Common.R;
import com.tuchuan.face_recognition_acs.Dto.FaceModel;
import com.tuchuan.face_recognition_acs.Entity.Face;
import com.tuchuan.face_recognition_acs.Service.FaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/face")
public class FaceController {
    @Autowired
    private FaceService faceService;
    private final String savePath = "F:" + File.separator + "faceImg" + File.separator;
    @PostMapping
    public R<String> save(@RequestBody FaceModel faceModel,@RequestHeader("X-Token") String token)
    {
        String base64 = faceModel.getSrc();
        String imgBase64Path = faceService.saveImgBase64(base64);
        if(faceService.saveFace(faceService.extractFaceFeature(imgBase64Path),token))
            return R.success("人脸信息录入成功");
        else
            return R.error("人脸信息已录入");
    }
    @DeleteMapping
    public R<String> delete(@RequestHeader("X-Token") String token)
    {
        faceService.deleteFace(token);
        return R.success("删除人脸信息成功");
    }
}