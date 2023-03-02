package com.tuchuan.face_recognition_acs.Service.Impl;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.tuchuan.face_recognition_acs.FaceEngineFactory;
import com.tuchuan.face_recognition_acs.Service.FaceEngineService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaceEngineServiceImpl implements FaceEngineService {
    @Value("${config.arcface-sdk.app-id}")
    String appId;
    @Value("${config.arcface-sdk.sdk-key}")
    String sdkKey;
    @Override
    public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
        return null;
    }

    @Override
    public Float compareFace(ImageInfo imageInfo1, ImageInfo imageInfo2) {
        FaceEngineFactory faceEngineFactory = new FaceEngineFactory(this.appId,this.sdkKey);
        FaceEngine faceEngine = faceEngineFactory.createCompareFaceEngine();
        return null;
    }

    @Override
    public byte[] extractFaceFeature(ImageInfo imageInfo, FaceInfo faceInfo) {
        return new byte[0];
    }
}
