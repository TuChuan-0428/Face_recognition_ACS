package com.tuchuan.face_recognition_acs;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.toolkit.ImageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

@Slf4j
public class FaceTest {
    public static void main(String[] args) {
        String appId = "AzTekTGWTkBfhjtwPJDx77pvtWp4cK4hr8nByzDtMxZE";
        String sdkKey = "7Ti1U26gGizABnb26fwBxyNSDqzcHx6PSHBfD1yGEL8i";
        FaceEngineFactory faceEngineFactory = new FaceEngineFactory(appId,sdkKey);
        FaceEngine faceEngine = faceEngineFactory.createDetectFaceEngine();
        //特征提取
        ImageInfo imageInfo = getRGBData(new File("F:" + File.separator + "faceImg" + File.separator + "a.jpg"));
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        System.out.println(faceInfoList);
        FaceFeature faceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
        ImageInfo imageInfo2 = getRGBData(new File("F:" + File.separator + "faceImg" + File.separator + "b.jpg"));
        List<FaceInfo> faceInfoList2 = new ArrayList<FaceInfo>();
        int errorCode2 = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        System.out.println(faceInfoList);
        FaceFeature faceFeature2 = new FaceFeature();
        errorCode2 = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(faceFeature2.getFeatureData());
        FaceSimilar faceSimilar = new FaceSimilar();
        errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        log.info("相似度:{}" , faceSimilar.getScore());
    }
}
