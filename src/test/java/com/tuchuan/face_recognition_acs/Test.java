package com.tuchuan.face_recognition_acs;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class Test {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        String now1 =  now.getHour() + ":" + now.getMinute();
        boolean b = false;
        log.info("now:{}",now1 );
        log.info("result:{}",now1.compareTo("08:30"));
        log.info("result:{}",now1.compareTo("15:30"));
        if(now1.compareTo("08:30") > 0 && now1.compareTo("15:30") < 0)
            b = true;
        log.info("pass:{}",b);
    }
}
