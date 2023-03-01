package com.tuchuan.face_recognition_acs.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuchuan.face_recognition_acs.Entity.User;
import com.tuchuan.face_recognition_acs.Mapper.UserMapper;
import com.tuchuan.face_recognition_acs.Service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
