package com.tuchuan.face_recognition_acs.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuchuan.face_recognition_acs.Entity.ACinfo;
import com.tuchuan.face_recognition_acs.Mapper.ACinfoMapper;
import com.tuchuan.face_recognition_acs.Service.ACinfoService;
import org.springframework.stereotype.Service;

@Service
public class ACinfoServiceImpl extends ServiceImpl<ACinfoMapper, ACinfo> implements ACinfoService {
}
