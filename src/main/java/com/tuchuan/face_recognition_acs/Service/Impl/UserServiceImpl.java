package com.tuchuan.face_recognition_acs.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Override
    public boolean update(User user, String token) {
        //根据token查询redis，判断用户是否登录
        Object o = redisTemplate.opsForValue().get(token);
        boolean update;
        //执行更新操作
        if(o != null) {
            User LoginUser = JSON.parseObject(JSON.toJSONString(o),User.class);
            //如果修改的是当前登录用户本身则删除redis中数据，确保数据一致性
            if(LoginUser.getUsername().equals(user.getUsername())) {
                redisTemplate.delete(token);
            }
            if(StringUtils.isNotEmpty(user.getPassword()) && user.getPassword() != null) {
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            }
             update = this.updateById(user);
        }
        else
            return false;
        return update;
    }

    @Override
    public void setClassRules(String className,int rules)
    {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(className != null,User::getClassname,className);
        List<User> userList = this.list(wrapper);
        for(User u:userList) {
            u.setRules(rules);
        }
        this.updateBatchById(userList);
    }
}
