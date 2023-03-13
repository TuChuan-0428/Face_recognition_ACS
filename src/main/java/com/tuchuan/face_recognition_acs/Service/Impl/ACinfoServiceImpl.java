package com.tuchuan.face_recognition_acs.Service.Impl;

import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceSimilar;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuchuan.face_recognition_acs.Entity.ACinfo;
import com.tuchuan.face_recognition_acs.Entity.Face;
import com.tuchuan.face_recognition_acs.Entity.Lab;
import com.tuchuan.face_recognition_acs.FaceEngineFactory;
import com.tuchuan.face_recognition_acs.Mapper.ACinfoMapper;
import com.tuchuan.face_recognition_acs.Service.ACinfoService;
import com.tuchuan.face_recognition_acs.Service.FaceService;
import com.tuchuan.face_recognition_acs.Service.LabService;
import com.tuchuan.face_recognition_acs.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ACinfoServiceImpl extends ServiceImpl<ACinfoMapper, ACinfo> implements ACinfoService {
    @Autowired
    FaceService faceService;
    @Autowired
    LabService labService;
    @Autowired
    UserService userService;
    private static final FaceEngineFactory factory = new FaceEngineFactory();
    @Override
    public ACinfo saveACinfo(String base64, String labName) {
        boolean pass = false;
        //获取当前上传的人脸图片
        String imgClassPath = faceService.saveImgBase64(base64);
        //提取当前上传的人脸特征值
        FaceFeature sourceFaceFeature = new FaceFeature(faceService.extractFaceFeature(imgClassPath));
        //获取当前数据库中的人脸库
        List<Face> list = faceService.list();
        //判断人脸是否已经注册，是返回用户
        Face user = compareFace(sourceFaceFeature, list);
        //根据用户判断是否有进入实验室权限
        pass = passLab(user,labName);
        //判断实验室当前时间是否开放
        pass = isOpen(labName);
        //往数据库中保存当前出入信息
        if(pass) {
            return saveDB(user,labName);
        }
        return null;
    }

    private Face compareFace(FaceFeature sourceFaceFeature,List<Face> faceList)
    {
        FaceSimilar faceSimilar = new FaceSimilar();
        FaceFeature targetFaceFeature = new FaceFeature();
        for (Face a : faceList) {
            targetFaceFeature.setFeatureData(a.getFeature());
            factory.createDetectFaceEngine().compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
            if(faceSimilar.getScore() >= 0.82) {
                return a;
            }
        }
        return null;
    }

    private boolean passLab(Face user,String labName)
    {
        LambdaQueryWrapper<Lab> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Lab::getLabName,labName);
        Lab one = labService.getOne(wrapper);
        return (one.getRules() | user.getUserRules()) == 1;
    }
    private boolean isOpen(String labName)
    {
        LambdaQueryWrapper<Lab> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Lab::getLabName,labName);
        Lab one = labService.getOne(wrapper);
        LocalDateTime dateTime = LocalDateTime.now();
        String now =  dateTime.getHour() + ":" + dateTime.getMinute();
        return now.compareTo(one.getOpenTime()) > 0 && now.compareTo(one.getClosedTime()) < 0;
    }
    private ACinfo saveDB(Face user,String labName)
    {
        ACinfo aCinfo = new ACinfo();
        aCinfo.setLabName(labName);
        aCinfo.setUserId(user.getUserId());
        aCinfo.setUserClass(userService.getById(user.getUserId()).getClassname());
        aCinfo.setUserName(user.getName());
        aCinfo.setInTime(LocalDateTime.now());
        this.save(aCinfo);
        return aCinfo;
    }
}
