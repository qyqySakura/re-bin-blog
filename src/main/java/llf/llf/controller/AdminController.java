package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.pojo.Admin;
import llf.llf.common.SaTokenUtil;
import llf.llf.service.UserService;
import llf.llf.service.PostService;
import llf.llf.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private llf.llf.service.AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    // @Autowired
    // private SystemLogService systemLogService;

    @Autowired
    private HttpServletRequest request;

    // 登录认证接口
    @PostMapping("/auth/login")
    public Result<Map<String, Object>> login(@RequestBody Admin admin) {
        Admin foundAdmin = adminService.login(admin.getUsername(), admin.getPassword());
        if (foundAdmin != null) {
            // 使用Sa-Token登录，使用管理员ID作为登录标识，并添加管理员类型前缀
            String loginId = "admin_" + foundAdmin.getId();
            SaTokenUtil.login(loginId);

            Map<String, Object> result = new HashMap<>();
            result.put("token", SaTokenUtil.getTokenValue());
            result.put("user", foundAdmin);

            // 记录登录日志
            // systemLogService.recordLoginLog(admin.getUsername(), getClientIp(), true);

            return Result.success(result);
        } else {
            // 记录登录失败日志
            // systemLogService.recordLoginLog(admin.getUsername(), getClientIp(), false);
            return Result.error(400, "用户名或密码错误");
        }
    }

    // 查询所有管理员
    @GetMapping
    public Result<List<Admin>> getAllAdmins() {
        return Result.success(adminService.selectAll());
    }

    // 根据ID查询管理员
    @GetMapping("/{id}")
    public Result<Admin>getAdminById(@PathVariable Integer id)
    {
        return Result.success(adminService.selectById(id));
    }

    // 新增管理员
    @PostMapping("/add")
    public Result<Integer> createAdmin(@RequestBody Admin admin) {
        try {
            int result = adminService.add(admin);
            // systemLogService.recordLog("INFO", "管理员管理", "添加管理员",
            //     "添加了新管理员: " + admin.getUsername(), getCurrentUser(), getClientIp(), null);
            return Result.success(result);
        } catch (Exception e) {
            // systemLogService.recordError("管理员管理", "添加管理员失败", e.getMessage());
            throw e;
        }
    }


    // 更新管理员
    @PutMapping("/update")
    public Result<Integer> updateAdmin(@RequestBody Admin admin) {
        try {
            System.out.println("收到更新请求: " + admin);
            int result = adminService.update(admin);
            // systemLogService.recordLog("INFO", "管理员管理", "更新管理员",
            //     "更新了管理员: " + admin.getUsername(), getCurrentUser(), getClientIp(), null);
            return Result.success(result);
        } catch (Exception e) {
            // systemLogService.recordError("管理员管理", "更新管理员失败", e.getMessage());
            throw e;
        }
    }

    // 删除管理员
    @DeleteMapping("/del/{id}")
    public Result<Integer> deleteAdmin(@PathVariable Integer id) {
        try {
            Admin admin = adminService.selectById(id);
            int result = adminService.deleteById(id);
            // systemLogService.recordLog("WARN", "管理员管理", "删除管理员",
            //     "删除了管理员: " + (admin != null ? admin.getUsername() : "ID:" + id),
            //     getCurrentUser(), getClientIp(), null);
            return Result.success(result);
        } catch (Exception e) {
            // systemLogService.recordError("管理员管理", "删除管理员失败", e.getMessage());
            throw e;
        }
    }

    // 获取统计数据
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // 获取各种统计数据
            stats.put("userCount", userService.selectAll().size());
            stats.put("postCount", postService.selectAll().size());
            stats.put("commentCount", commentService.selectAll().size());
            stats.put("notificationCount", 0); // 暂时设为0，后续可以添加通知统计

            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(500, "获取统计数据失败: " + e.getMessage());
        }
    }

    // 获取最近活动
    @GetMapping("/activities")
    public Result<List<Map<String, Object>>> getRecentActivities() {
        try {
            List<Map<String, Object>> activities = new java.util.ArrayList<>();

            // 这里可以根据实际需求获取最近的活动数据
            // 暂时返回模拟数据
            Map<String, Object> activity1 = new HashMap<>();
            activity1.put("id", 1);
            activity1.put("type", "user");
            activity1.put("content", "新用户注册");
            activity1.put("createTime", new java.util.Date());
            activities.add(activity1);

            Map<String, Object> activity2 = new HashMap<>();
            activity2.put("id", 2);
            activity2.put("type", "post");
            activity2.put("content", "发布新文章");
            activity2.put("createTime", new java.util.Date());
            activities.add(activity2);

            return Result.success(activities);
        } catch (Exception e) {
            return Result.error(500, "获取活动数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp() {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }

    /**
     * 获取当前登录用户名
     */
    private String getCurrentUser() {
        try {
            if (SaTokenUtil.isLogin()) {
                String loginId = (String) SaTokenUtil.getLoginId();
                if (loginId.startsWith("admin_")) {
                    Integer adminId = Integer.parseInt(loginId.substring(6));
                    Admin admin = adminService.selectById(adminId);
                    return admin != null ? admin.getUsername() : "未知管理员";
                }
            }
            return "系统";
        } catch (Exception e) {
            return "系统";
        }
    }
    
    // 退出登录
    @PostMapping("/auth/logout")
    public Result<String> logout() {
        SaTokenUtil.logout();
        return Result.success("退出登录成功");
    }
}
