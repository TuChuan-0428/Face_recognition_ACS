package com.tuchuan.face_recognition_acs.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuchuan.face_recognition_acs.Common.R;
import com.tuchuan.face_recognition_acs.Dto.FaceModel;
import com.tuchuan.face_recognition_acs.Entity.User;
import com.tuchuan.face_recognition_acs.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public R<Map<String,Object>> login(@RequestBody User user)
    {
        //如果用户密码正确返回登录对象，否则返回null
        Map<String,Object> data = userService.login(user);
        if(data != null) {
            return R.success(data);
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(user.getUsername() != null,User::getUsername,user.getUsername());
        User one = userService.getOne(wrapper);
        if(one != null)
            return R.error("密码错误");
        return R.error("用户不存在");
    }
    @PostMapping("/logout")
    public R<String> logout(@RequestHeader("X-token") String token)
    {
        userService.logout(token);
        return R.success("退出登录");
    }
    @GetMapping("/info")
    public R<Map<String,Object>> getUserInfo(@RequestParam("token") String token) {
        Map<String,Object> data = userService.getUserInfo(token);
        return R.success(data);
    }
    @GetMapping("/{id}")
    public R<User> getUserById(@PathVariable Long id)
    {
        User byId = userService.getById(id);
        byId.setPassword(null);
        return R.success(byId);
    }
    @PostMapping
    public R<String> regUser(@RequestBody User user)
    {
        log.info("user:{}",user);
        //1.将页面提交的密码password进行MD5加密
        if(StringUtils.isNotEmpty(user.getPassword()) && user.getPassword() != null){
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        }
        //2.校验用户名和手机号是否重复
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        wrapper.eq(User::getPhone,user.getPhone());
        long count = userService.count(wrapper);
        if(count > 0) {
            return R.error("用户名或手机号已被注册");
        }
        //3.保存到数据库
        userService.save(user);
        return R.success("用户注册成功");
    }

    /**
     *
     * @param page
     * @param pageSize
     * @param name  用户名进行单个查找
     * @param classname 班级名用于查找班级下的全部学生
     * @return
     */
    @GetMapping("/student/page")
    public R<Page> studentPage(int page,int pageSize,String name,String classname)
    {
        Page pageInfo = new Page(page,pageSize);
        //当name和class不为空时，才匹配两者
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name),User::getName,name);
        wrapper.like(StringUtils.isNotEmpty(classname),User::getClassname,classname);
        wrapper.orderByAsc(User::getCreateTime);
        wrapper.orderByDesc(User::getUpdateTime);

        userService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
    @GetMapping("/student/classname")
    public R<List<String>> getClassnameList()
    {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT classname");
        List<String> list = userService.list(wrapper).stream().map( (item) -> {
            String classname = item.getClassname();
            return classname;
        }).collect(Collectors.toList());
        return R.success(list);
    }
    @PutMapping
    public R<String> update(@RequestBody User user,@RequestHeader("X-Token") String token)
    {
        log.info("user:{} token:{}",user,token);
        if(userService.update(user,token))
            return R.success("更新成功");
        return R.error("更新失败");
    }
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable Long id)
    {
        log.info("id:{}",id);
        userService.removeById(id);
        return R.success("删除成功");
    }
    @PutMapping("/student")
    public R<String> setClassRules(@RequestBody FaceModel faceModel)
    {
        log.info("className:{}",faceModel);
        userService.setClassRules(faceModel.getClassName(),faceModel.getRules());
        return R.success("设置班级访问权限成功");
    }
}
