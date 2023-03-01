package com.tuchuan.face_recognition_acs.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuchuan.face_recognition_acs.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
