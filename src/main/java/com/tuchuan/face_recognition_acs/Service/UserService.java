package com.tuchuan.face_recognition_acs.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tuchuan.face_recognition_acs.Entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String,Object> login(User user);//用户登录

    Map<String, Object> getUserInfo(String token);

    void logout(String token);

    boolean update(User user,String token);

    void setClassRules(String className,int rules);
}
