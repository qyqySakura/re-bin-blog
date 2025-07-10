package llf.llf.controller;

import llf.llf.common.BusinessException;
import llf.llf.pojo.User;
import llf.llf.service.UserService;
import llf.llf.common.SaTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import llf.llf.common.Result;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户登录接口
    @PostMapping("/auth/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        User foundUser = userService.login(user.getUsername(), user.getPassword());
        if (foundUser != null) {
            // 使用Sa-Token登录
            SaTokenUtil.login(foundUser.getId(), "user");
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", SaTokenUtil.getTokenValue());
            result.put("user", foundUser);
            
            return Result.success(result);
        } else {
            return Result.error(400, "用户名或密码错误");
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

    // 新增用户
    @PostMapping("/add")
    public Result<Integer> createUser(@RequestBody User user) {
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

