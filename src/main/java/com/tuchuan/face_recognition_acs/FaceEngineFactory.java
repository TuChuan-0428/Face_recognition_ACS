package com.tuchuan.face_recognition_acs;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FaceEngineFactory {
    private String appId;
    private String sdkKey;
    public FaceEngineFactory(String appId, String sdkKey) {
        this.appId = appId;
        this.sdkKey = sdkKey;
    }
    public FaceEngine createDetectFaceEngine()
    {
        FunctionConfiguration detectFunctionCfg = new FunctionConfiguration();
        EngineConfiguration engineConfiguration = new EngineConfiguration();

        detectFunctionCfg.setSupportFaceDetect(true);//开启人脸检测功能
        detectFunctionCfg.setSupportFaceRecognition(true);//开启人脸识别功能
        detectFunctionCfg.setSupportAge(true);//开启年龄检测功能
        detectFunctionCfg.setSupportGender(true);//开启性别检测功能
        detectFunctionCfg.setSupportLiveness(true);//开启活体检测功能

        engineConfiguration.setFunctionConfiguration(detectFunctionCfg);
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);
        FaceEngine faceEngine = new FaceEngine(System.getProperty("user.dir")+"/lib/WIN64");
        int activeCode = faceEngine.activeOnline(appId, sdkKey);
        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.error("引擎激活失败" + activeCode);
        }
        int initCode = faceEngine.init(engineConfiguration);
        if (initCode != ErrorInfo.MOK.getValue()) {
            log.error("引擎初始化失败" + initCode);
        }
        return faceEngine;
    }
    public FaceEngine createCompareFaceEngine()
    {
        FunctionConfiguration compareFunctionCfg = new FunctionConfiguration();
        EngineConfiguration compareCfg = new EngineConfiguration();
        compareFunctionCfg.setSupportFaceRecognition(true);//开启人脸识别功能
        compareCfg.setFunctionConfiguration(compareFunctionCfg);
        compareCfg.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);//图片检测模式，如果是连续帧的视频流图片，那么改成VIDEO模式
        compareCfg.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);//人脸旋转角度
        FaceEngine faceEngine = new FaceEngine("lib/WIN64");
        int activeCode = faceEngine.activeOnline(appId, sdkKey);
        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            log.error("引擎激活失败" + activeCode);
        }
        int initCode = faceEngine.init(compareCfg);
        if (initCode != ErrorInfo.MOK.getValue()) {
            log.error("引擎初始化失败" + initCode);
        }
        return faceEngine;
    }
}
