package com.tuchuan.face_recognition_acs.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用于记录出入信息
 */
@Data
@TableName(value = "access_infomation")
public class ACinfo {
    private Long id;
    private Long user_id;
    private String user_name;
    private LocalDateTime in_time;
    private LocalDateTime out_time;
}
