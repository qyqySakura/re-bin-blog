package llf.llf.controller;

import llf.llf.common.BusinessException;
import llf.llf.pojo.User;
import llf.llf.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import llf.llf.common.Result;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
}

