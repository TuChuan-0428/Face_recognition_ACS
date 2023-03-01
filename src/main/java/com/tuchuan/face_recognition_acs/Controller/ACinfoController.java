package com.tuchuan.face_recognition_acs.Controller;

import com.tuchuan.face_recognition_acs.Common.R;
import com.tuchuan.face_recognition_acs.Entity.ACinfo;
import com.tuchuan.face_recognition_acs.Service.ACinfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/acinfo")
public class ACinfoController {
    @Autowired
    private ACinfoService aCinfoService;
    @PostMapping
    public R<String> save_in_information(ACinfo aCinfo)
    {
        log.info("acinfo:{}",aCinfo);
        aCinfo.setIn_time(LocalDateTime.now());
        return R.success("进入信息已录入");
    }

}
