package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.common.SaTokenUtil;
import llf.llf.pojo.Admin;
import llf.llf.pojo.User;
import llf.llf.service.AdminService;
import llf.llf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getCurrentUser() {
        Map<String, Object> result = new HashMap<>();

        // 检查是否已登录
        if (SaTokenUtil.isLogin()) {
            String loginId = (String) SaTokenUtil.getLoginId();

            // 根据loginId前缀判断用户类型
            if (loginId.startsWith("admin_")) {
                // 管理员登录
                Integer adminId = Integer.parseInt(loginId.substring(6)); // 去掉"admin_"前缀
                Admin admin = adminService.selectById(adminId);

                result.put("user", admin);
                result.put("userType", "admin");
                result.put("token", SaTokenUtil.getTokenValue());
                return Result.success(result);
            } else if (loginId.startsWith("user_")) {
                // 用户登录
                Integer userId = Integer.parseInt(loginId.substring(5)); // 去掉"user_"前缀
                User user = userService.selectById(userId);

                result.put("user", user);
                result.put("userType", "user");
                result.put("token", SaTokenUtil.getTokenValue());
                return Result.success(result);
            }
        }

        return Result.error(401, "未登录");
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/check")
    public Result<Map<String, Object>> checkLogin() {
        Map<String, Object> result = new HashMap<>();
        
        boolean adminLogin = SaTokenUtil.isLogin("admin");
        boolean userLogin = SaTokenUtil.isLogin("user");
        
        result.put("adminLogin", adminLogin);
        result.put("userLogin", userLogin);
        result.put("isLogin", adminLogin || userLogin);
        
        return Result.success(result);
    }
} 