package llf.llf.service;

import llf.llf.pojo.User;

import java.util.List;

public interface UserService {

    List<User> selectAll();

    User selectById(Integer id);

    int add(User user);

    int update(User user);

    int deleteById(Integer id);
}
