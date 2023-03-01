package com.tuchuan.face_recognition_acs.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuchuan.face_recognition_acs.Common.R;
import com.tuchuan.face_recognition_acs.Entity.User;
import com.tuchuan.face_recognition_acs.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/info")
    public R<String> test() {
        return R.success("123");
    }
    @PostMapping("/login")
    public R<String> login(HttpServletRequest request,@RequestBody User user)
    {
        log.info("user:{}",user);
        //1.将页面提交的密码password进行MD5加密
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查找数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(user.getUsername() != null,User::getUsername,user.getUsername());
        User user1 = userService.getOne(wrapper);
        //3.如果没有查询结果则返回用户不存在
        if(user1 == null) {
            return R.error("用户不存在");
        }
        //4.密码比对，密码不一致返回密码错误
        if(!user1.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        //5.登录成功，将员工id存入session中并返回登录结果
        request.getSession().setAttribute("user",user.getId());
        return R.success("登录成功，当前身份"+user.getClassname());
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request)
    {
        request.removeAttribute("user");
        return R.success("退出登录");
    }
    @PostMapping
    public R<String> regUser(User user)
    {
        //1.将页面提交的密码password进行MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
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
        wrapper.orderByDesc(User::getUpdateTime);

        userService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(User user,HttpServletRequest request)
    {
        //1.查询现在登录的用户权限
        Object id = request.getSession().getAttribute("user");
        User older = userService.getById((Long) id);
        //2.如果当前登录用户为学生则不能更改班级等信息
        if(!older.getClassname().equals("教师")) {
            if(!user.getClassname().equals(older.getClassname()))
                return R.error("无权限修改");
        }
        userService.updateById(user);
        return null;
    }
    @GetMapping("/{id}")
    public R<User> getUserById(@PathVariable Long id)
    {
        User byId = userService.getById(id);
        if(byId != null) {
            return R.success(byId);
        }
        return R.error("查无此用户");
    }
}
