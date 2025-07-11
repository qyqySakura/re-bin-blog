package llf.llf.service;

import llf.llf.pojo.User;

import java.util.List;

public interface UserService {

String generateAndSendEmailCode(String email);

    List<User> selectAll();

    User selectById(Integer id);

    int add(User user);

    int update(User user);

    int deleteById(Integer id);
    
    // 登录验证方法
    User login(String username, String password);
}
