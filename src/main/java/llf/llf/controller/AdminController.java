package llf.llf.controller;

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
    public List<Admin> getAllAdmins() {
        return adminService.selectAll();
    }

    // 根据ID查询管理员
    @GetMapping("/{id}")
    public Admin getAdminById(@PathVariable Integer id) {
        return adminService.selectById(id);
    }

    // 新增管理员
    @PostMapping("/add")
    public int createAdmin(@RequestBody Admin admin) {
        return adminService.add(admin);
    }

    // 更新管理员
    @PutMapping("/update")
    public int updateAdmin(@RequestBody Admin admin) {
        return adminService.update(admin);
    }

    // 删除管理员
    @DeleteMapping("/del/{id}")
    public int deleteAdmin(@PathVariable Integer id) {
        return adminService.deleteById(id);
    }
}
