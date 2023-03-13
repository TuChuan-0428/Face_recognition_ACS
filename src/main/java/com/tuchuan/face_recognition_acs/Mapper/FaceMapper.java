package com.tuchuan.face_recognition_acs.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuchuan.face_recognition_acs.Entity.Face;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FaceMapper extends BaseMapper<Face> {
}
