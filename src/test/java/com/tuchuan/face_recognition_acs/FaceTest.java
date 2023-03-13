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
        FaceEngineFactory faceEngineFactory = new FaceEngineFactory();
        FaceEngine faceEngine = faceEngineFactory.createDetectFaceEngine();
        int errorCode;
        //人脸提取
        ImageInfo imageInfo = getRGBData(new File("F:" + File.separator + "faceImg" + File.separator + "myself0.png"));
        ImageInfo imageInfo1 = getRGBData(new File("F:" + File.separator + "faceImg" + File.separator + "myself1.png"));
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
        System.out.println(faceInfoList);
        List<FaceInfo> faceInfoList1 = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo1.getImageData(), imageInfo1.getWidth(), imageInfo1.getHeight(), imageInfo1.getImageFormat(), faceInfoList1);
        System.out.println(faceInfoList1);
        //特征提取
        FaceFeature faceFeature = new FaceFeature();
        FaceFeature faceFeature1 = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        errorCode = faceEngine.extractFaceFeature(imageInfo1.getImageData(),imageInfo1.getWidth(),imageInfo1.getHeight(),imageInfo1.getImageFormat(),faceInfoList1.get(0),faceFeature1);
        //人脸特征值比对
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(faceFeature1.getFeatureData());
        FaceSimilar faceSimilar = new FaceSimilar();
        System.out.println("FaceFeature:" + faceFeature.getFeatureData());
        System.out.println("FaceFeature:" + faceFeature.getFeatureData());

        errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        System.out.println("faceSimilar:" + faceSimilar.getScore());
    }
}
