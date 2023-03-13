package com.tuchuan.face_recognition_acs.Dto;

import lombok.Data;

/**
 * 此数据传输对象，用于解决前端上传base64，Controller直接用String接受base64字符串，乱码不匹配问题
 */
@Data
public class FaceModel {
    private String src;
    private String className;
    private int rules;
}
