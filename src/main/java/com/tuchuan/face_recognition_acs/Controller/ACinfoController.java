package com.tuchuan.face_recognition_acs.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuchuan.face_recognition_acs.Common.R;
import com.tuchuan.face_recognition_acs.Dto.AcinDTO;
import com.tuchuan.face_recognition_acs.Entity.ACinfo;
import com.tuchuan.face_recognition_acs.Entity.Face;
import com.tuchuan.face_recognition_acs.Entity.Lab;
import com.tuchuan.face_recognition_acs.Entity.User;
import com.tuchuan.face_recognition_acs.Service.ACinfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/acinfo")
public class ACinfoController {
    @Autowired
    private ACinfoService aCinfoService;
    @PostMapping
    public R<Map<String,Object>> save(@RequestBody AcinDTO accessModel)
    {
        log.info("acInfo:{}",accessModel);
        ACinfo aCinfo = aCinfoService.saveACinfo(accessModel.getSrc(), accessModel.getLabName());
        Map<String,Object> map = new HashMap<>();
        map.put("msg","欢迎用户: " + aCinfo.getUserName() + "进入" + aCinfo.getLabName());
        map.put("acinfo",aCinfo);
        if(aCinfo != null)
            return R.success(map);
        return R.error("无法进入实验室");
    }
    @GetMapping("/page")
    public R<Page> acinList(int page,int pageSize,String name,String classname,String labname)
    {
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<ACinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),ACinfo::getUserName,name);
        wrapper.like(StringUtils.isNotEmpty(classname),ACinfo::getUserClass,classname);
        wrapper.like(StringUtils.isNotEmpty(labname),ACinfo::getLabName,labname);
        wrapper.orderByAsc(ACinfo::getInTime);
        wrapper.orderByDesc(ACinfo::getOutTime);
        aCinfoService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
    @PutMapping("/{id}")
    public R<String> update(@PathVariable Long id)
    {
        ACinfo byId = aCinfoService.getById(id);
        byId.setOutTime(LocalDateTime.now());
        aCinfoService.updateById(byId);
        return R.success("您已成功退出实验室");
    }
}
