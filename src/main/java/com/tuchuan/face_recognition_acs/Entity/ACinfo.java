package com.tuchuan.face_recognition_acs.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于记录出入信息
 */
@Data
@TableName(value = "access_information")
public class ACinfo implements Serializable
{
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Long userId;
    private String userName;
    private String userClass;
    private String labName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime outTime;
}
