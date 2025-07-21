package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.pojo.Admin;
import llf.llf.common.SaTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private llf.llf.service.AdminService adminService;

    // 登录认证接口
    @PostMapping("/auth/login")
    public Result<Map<String, Object>> login(@RequestBody Admin admin) {
        Admin foundAdmin = adminService.login(admin.getUsername(), admin.getPassword());
        if (foundAdmin != null) {
            // 使用Sa-Token登录
            SaTokenUtil.login(foundAdmin.getId(), "admin");
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", SaTokenUtil.getTokenValue("admin"));
            result.put("user", foundAdmin);
            
            return Result.success(result);
        } else {
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
        return Result.success(adminService.add(admin));
    }


    // 更新管理员
    @PutMapping("/update")
    public Result<Integer> updateAdmin(@RequestBody Admin admin) {
        System.out.println("收到更新请求: " + admin);
        return Result.success(adminService.update(admin));
    }

    // 删除管理员
    @DeleteMapping("/del/{id}")
    public Result<Integer> deleteAdmin(@PathVariable Integer id) {
        return Result.success(adminService.deleteById(id));
    }
    
    // 退出登录
    @PostMapping("/auth/logout")
    public Result<String> logout() {
        SaTokenUtil.logout("admin");
        return Result.success("退出登录成功");
    }
}
