package com.tuchuan.face_recognition_acs.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuchuan.face_recognition_acs.Entity.User;
import com.tuchuan.face_recognition_acs.Mapper.UserMapper;
import com.tuchuan.face_recognition_acs.Service.UserService;
import com.tuchuan.face_recognition_acs.Utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Map<String,Object> login(User user) {
        //1.将页面提交的密码password进行MD5加密
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查找数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(user.getUsername() != null,User::getUsername,user.getUsername());
        wrapper.eq(User::getPassword,password);
        User LoginUser = this.getOne(wrapper);
        if(LoginUser != null)
        {
            String key = JWTUtils.generateToken(LoginUser.getUsername());
            Map<String,Object> data = new HashMap<>();
            redisTemplate.opsForValue().set(key,LoginUser,30, TimeUnit.MINUTES);
            data.put("token",key);
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if(o != null) {
            User LoginUser = JSON.parseObject(JSON.toJSONString(o),User.class);
            HashMap<String,Object> data = new HashMap<>();
            LoginUser.setPassword(null);
            data.put("user",LoginUser);
            return data;
        }
        return null;
    }
}
