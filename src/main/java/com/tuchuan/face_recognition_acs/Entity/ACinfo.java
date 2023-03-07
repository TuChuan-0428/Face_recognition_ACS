package com.tuchuan.face_recognition_acs.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于记录出入信息
 */
@Data
@TableName(value = "access_infomation")
public class ACinfo implements Serializable
{
    private Long id;
    private Long user_id;
    private String user_name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime in_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime out_time;
}
