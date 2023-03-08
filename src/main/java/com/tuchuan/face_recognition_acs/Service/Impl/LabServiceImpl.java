package com.tuchuan.face_recognition_acs.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuchuan.face_recognition_acs.Entity.Lab;
import com.tuchuan.face_recognition_acs.Mapper.LabMapper;
import com.tuchuan.face_recognition_acs.Service.LabService;
import org.springframework.stereotype.Service;

@Service
public class LabServiceImpl extends ServiceImpl<LabMapper, Lab> implements LabService {
}
