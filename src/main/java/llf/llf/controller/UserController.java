package llf.llf.controller;

import llf.llf.common.BusinessException;
import llf.llf.pojo.User;
import llf.llf.service.UserService;
import llf.llf.service.AvatarService;
import llf.llf.common.SaTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import llf.llf.common.Result;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AvatarService avatarService;

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
            // 使用Sa-Token登录，使用用户ID作为登录标识，并添加用户类型前缀
            String loginId = "user_" + foundUser.getId();
            SaTokenUtil.login(loginId);

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
        SaTokenUtil.logout();
        return Result.success("退出登录成功");
    }

    // 修改密码
    @PostMapping("/changePassword")
    public Result<String> changePassword(@RequestBody Map<String, String> request) {
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return Result.error(400, "当前密码不能为空");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error(400, "新密码不能为空");
        }

        if (newPassword.length() < 6 || newPassword.length() > 20) {
            return Result.error(400, "新密码长度必须在6-20个字符之间");
        }

        try {
            // 获取当前登录用户ID
            if (!SaTokenUtil.isLogin("user")) {
                return Result.error(401, "请先登录");
            }

            Integer userId = (Integer) SaTokenUtil.getLoginId("user");
            boolean success = userService.changePassword(userId, currentPassword, newPassword);

            if (success) {
                return Result.success("密码修改成功");
            } else {
                return Result.error(400, "当前密码错误");
            }
        } catch (Exception e) {
            return Result.error(500, "密码修改失败：" + e.getMessage());
        }
    }

    // 获取当前登录用户信息
    @GetMapping("/auth/info")
    public Result<Map<String, Object>> getCurrentUserInfo() {
        try {
            // 检查用户登录状态
            if (!SaTokenUtil.isLogin("user")) {
                return Result.error(401, "未登录");
            }

            Object userId = SaTokenUtil.getLoginId("user");
            User user = userService.selectById((Integer) userId);

            if (user == null) {
                return Result.error(404, "用户不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("userType", "user");
            result.put("token", SaTokenUtil.getTokenValue("user"));

            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取用户信息失败：" + e.getMessage());
        }
    }

    // 上传头像
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            if (!SaTokenUtil.isLogin()) {
                return Result.error(401, "未登录");
            }

            String loginId = (String) SaTokenUtil.getLoginId();
            if (!loginId.startsWith("user_")) {
                return Result.error(403, "权限不足");
            }

            Integer userId = Integer.parseInt(loginId.substring(5)); // 去掉"user_"前缀
            String url = avatarService.uploadAvatar(file, "user", userId);
            return Result.success(url);
        } catch (Exception e) {
            return Result.error(500, "头像上传失败：" + e.getMessage());
        }
    }
}

