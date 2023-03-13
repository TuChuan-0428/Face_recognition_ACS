package com.tuchuan.face_recognition_acs.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tuchuan.face_recognition_acs.Entity.ACinfo;
import com.tuchuan.face_recognition_acs.Entity.Face;

public interface ACinfoService extends IService<ACinfo> {
    ACinfo saveACinfo(String base64, String labName);
}
