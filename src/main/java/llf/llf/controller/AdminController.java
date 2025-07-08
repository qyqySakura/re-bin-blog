package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.pojo.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private llf.llf.service.AdminService adminService;

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
        return Result.success(adminService.update(admin));
    }

    // 删除管理员
    @DeleteMapping("/del/{id}")
    public Result<Integer> deleteAdmin(@PathVariable Integer id) {
        return Result.success(adminService.deleteById(id));
    }
}
