package com.tuchuan.face_recognition_acs.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tuchuan.face_recognition_acs.Entity.Face;

public interface FaceService extends IService<Face> {
    String saveImgBase64(String base64);
    byte[] extractFaceFeature(String imgPath);
    boolean saveFace(byte[] feature,String token);
    boolean deleteFace(String token);
}
