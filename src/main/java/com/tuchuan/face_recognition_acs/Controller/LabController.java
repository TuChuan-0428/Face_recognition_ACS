package com.tuchuan.face_recognition_acs.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuchuan.face_recognition_acs.Common.R;
import com.tuchuan.face_recognition_acs.Entity.Lab;
import com.tuchuan.face_recognition_acs.Service.LabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/lab")
public class LabController {
    @Autowired
    private LabService labService;
    @PostMapping
    public R<String> save(@RequestBody Lab lab)
    {
        log.info("lab:{}",lab);
        labService.save(lab);
        return R.success("新增实验室成功");
    }
    @GetMapping("/{id}")
    public R<Lab> getLabById(@PathVariable Long id)
    {
        Lab byId = labService.getById(id);
        return R.success(byId);
    }
    @GetMapping("/page")
    public R<Page> getlabList(int page,int pageSize,String labName)
    {
        Page pageinfo = new Page(page,pageSize);
        //当labName不为空时，才匹配条件
        LambdaQueryWrapper<Lab> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(labName),Lab::getLabName,labName);
        wrapper.orderByAsc(Lab::getCreateTime);
        wrapper.orderByDesc(Lab::getUpdateTime);
        labService.page(pageinfo,wrapper);
        return R.success(pageinfo);
    }
    @GetMapping("/name")
    public R<List<String>> labNames()
    {
        QueryWrapper<Lab> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT lab_Name");
        List<String> list = labService.list(wrapper).stream().map((item) ->{
            String labName = item.getLabName();
            return labName;
        }).collect(Collectors.toList());
        return R.success(list);
    }
    @PutMapping
    public R<String> updateLab(@RequestBody Lab lab)
    {
        labService.updateById(lab);
        return R.success("更新成功");
    }
    @DeleteMapping("/{id}")
    public R<String> deleteLabById(@PathVariable Long id)
    {
        labService.removeById(id);
        return R.success("删除成功");
    }
}
