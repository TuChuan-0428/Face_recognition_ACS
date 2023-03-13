package com.tuchuan.face_recognition_acs.Dto;

import com.tuchuan.face_recognition_acs.Entity.ACinfo;
import lombok.Data;

@Data
public class AcinDTO extends ACinfo {
    private String src;
    private String labName;

}
