package com.tuchuan.face_recognition_acs.Service;


import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageInfo;

import java.util.List;


public interface FaceEngineService {

    List<FaceInfo> detectFaces(ImageInfo imageInfo);

    Float compareFace(ImageInfo imageInfo1,ImageInfo imageInfo2) ;

    byte[] extractFaceFeature(ImageInfo imageInfo,FaceInfo faceInfo);

    //List<UserCompareInfo> faceRecognition(byte[] faceFeature, List<UserRamCache.UserInfo> userInfoList, float passRate) ;

    //List<ProcessInfo> process(ImageInfo imageInfo,List<FaceInfo> faceInfoList);





}
