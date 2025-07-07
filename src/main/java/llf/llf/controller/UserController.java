package llf.llf.controller;

import llf.llf.pojo.User;
import llf.llf.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 查询所有用户
    @GetMapping
    public List<User> getAllUsers() {
        return userService.selectAll();
    }

    // 根据ID查询用户
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.selectById(id);
    }

    // 新增用户
    @PostMapping("/add")
    public int createUser(@RequestBody User user) {
        return userService.add(user);
    }

    // 更新用户
    @PutMapping("/update")
    public int updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    // 删除用户
    @DeleteMapping("/del/{id}")
    public int deleteUser(@PathVariable Integer id) {
        return userService.deleteById(id);
    }
}
