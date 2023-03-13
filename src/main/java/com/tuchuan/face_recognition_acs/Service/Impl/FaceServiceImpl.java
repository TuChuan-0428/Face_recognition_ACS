package com.tuchuan.face_recognition_acs.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuchuan.face_recognition_acs.Entity.Face;
import com.tuchuan.face_recognition_acs.Entity.User;
import com.tuchuan.face_recognition_acs.FaceEngineFactory;
import com.tuchuan.face_recognition_acs.Mapper.FaceMapper;
import com.tuchuan.face_recognition_acs.Service.FaceService;
import com.tuchuan.face_recognition_acs.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

@Service
public class FaceServiceImpl extends ServiceImpl<FaceMapper, Face> implements FaceService{
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    private final String savePath = "F:" + File.separator + "faceImg" + File.separator;
    private static final FaceEngineFactory factory = new FaceEngineFactory();

    @Override
    public String saveImgBase64(String base64) {
        // 图片分类路径+图片名+图片后缀
        String imgClassPath = savePath.concat(UUID.randomUUID().toString()).concat(".png");
        // 去掉base64前缀 data:image/png;base64,
        base64 = base64.substring(base64.indexOf(",", 1) + 1);
        // 解密，解密的结果是一个byte数组
        Base64.Decoder decoder = Base64.getMimeDecoder();
        byte[] imgbytes = decoder.decode(base64.getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < imgbytes.length; ++i) {
            if (imgbytes[i] < 0) {
                imgbytes[i] += 256;
            }
        }
        // 保存图片
        OutputStream out = null;
        try {
            out = new FileOutputStream(imgClassPath);
            out.write(imgbytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imgClassPath;
    }
    @Override
    public byte[] extractFaceFeature(String imgPath) {
        //获取人脸识别引擎
        FaceEngine faceEngine = factory.createDetectFaceEngine();
        //获取人脸图片
        ImageInfo imageInfo = getRGBData(new File(imgPath));
        //声明人脸信息列
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        //检测图片中有多少张人脸
        int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        /*
           声明人脸特征,并提取出人脸信息列中第一张人脸的特征值
        */
        FaceFeature faceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        return faceFeature.getFeatureData();
    }

    @Override
    public boolean saveFace(byte[] feature,String token) {
        Face face = new Face();
        Object o = redisTemplate.opsForValue().get(token);
        User LoginUser = JSON.parseObject(JSON.toJSONString(o),User.class);
        face.setFeature(feature);
        face.setUserId(LoginUser.getId());
        face.setName(LoginUser.getName());
        face.setUserRules(LoginUser.getRules());
        try {
            this.save(face);
        }catch (Exception e) {
            return false;
        }
        LoginUser.setStatus(1);
        userService.updateById(LoginUser);
        return true;
    }

    @Override
    public boolean deleteFace(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        User LoginUser = JSON.parseObject(JSON.toJSONString(o),User.class);
        LambdaQueryWrapper<Face> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Face::getUserId,LoginUser.getId());
        this.remove(wrapper);
        return true;
    }
}
