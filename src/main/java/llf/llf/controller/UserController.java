package llf.llf.controller;

import llf.llf.common.BusinessException;
import llf.llf.pojo.User;
import llf.llf.service.UserService;
import llf.llf.common.SaTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.HashMap;
import java.util.Map;

import llf.llf.common.Result;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 发送邮箱验证码
    @PostMapping("/sendEmailCode")
    public Result<String> sendEmailCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return Result.error(400, "邮箱地址不能为空");
        }
        try {
            userService.generateAndSendEmailCode(email);
            return Result.success("验证码已发送");
        } catch (Exception e) {
            return Result.error(500, "发送验证码失败：" + e.getMessage());
        }
    }



    // 用户登录接口
    @PostMapping("/auth/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        System.out.println("收到登录请求 - 用户名: " + user.getUsername() + ", 密码: " + user.getPassword());

        // 检查参数是否为空
        if (user.getUsername() == null || user.getPassword() == null) {
            System.out.println("登录失败：用户名或密码为空");
            return Result.error(400, "用户名或密码不能为空");
        }

        User foundUser = userService.login(user.getUsername(), user.getPassword());
        if (foundUser != null) {
            // 使用Sa-Token登录
            SaTokenUtil.login(foundUser.getId(), "user");

            Map<String, Object> result = new HashMap<>();
            result.put("token", SaTokenUtil.getTokenValue());
            result.put("user", foundUser);

            System.out.println("用户登录成功 - ID: " + foundUser.getId() + ", 用户名: " + foundUser.getUsername());
            return Result.success(result);
        } else {
            System.out.println("登录失败：用户名或密码错误");
            return Result.error(400, "用户名或密码错误");
        }
    }

    // 验证邮箱验证码
    @PostMapping("/verifyEmailCode")
    public Result<String> verifyEmailCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        if (email == null || email.trim().isEmpty()) {
            return Result.error(400, "邮箱地址不能为空");
        }

        if (code == null || code.trim().isEmpty()) {
            return Result.error(400, "验证码不能为空");
        }

        try {
            boolean isValid = userService.verifyEmailCode(email, code);
            if (isValid) {
                return Result.success("验证码验证成功");
            } else {
                return Result.error(400, "验证码错误或已过期");
            }
        } catch (Exception e) {
            return Result.error(500, "验证失败：" + e.getMessage());
        }
    }

    // 查询所有用户
    @GetMapping
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.selectAll());
    }

    // 根据ID查询用户
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        return Result.success(userService.selectById(id));
    }

    // 新增用户（带验证码校验）
    @PostMapping("/add")
    public Result<Integer> createUser(@RequestBody User user, @RequestParam String code) {
        // 使用UserService中的verifyEmailCode方法验证验证码
        boolean isValid = userService.verifyEmailCode(user.getEmail(), code);
        if (!isValid) {
            return Result.error(400, "验证码错误或已过期");
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        return Result.success(userService.add(user));
    }

    // 更新用户
    @PutMapping("/update")
    public Result<Integer> updateUser(@RequestBody User user) {
        return Result.success(userService.update(user));
    }

    // 删除用户
    @DeleteMapping("/del/{id}")
    public Result<Integer> deleteUser(@PathVariable Integer id) {
        return Result.success(userService.deleteById(id));
    }
    
    // 退出登录
    @PostMapping("/auth/logout")
    public Result<String> logout() {
        SaTokenUtil.logout("user");
        return Result.success("退出登录成功");
    }
}

