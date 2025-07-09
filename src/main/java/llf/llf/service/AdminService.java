package llf.llf.service;

import llf.llf.pojo.Admin;

import java.util.List;

/**
 * @author sakura
 * @description 针对表【admin】的数据库操作Service
 * @createDate 2025-07-07 15:59:09
 */
public interface AdminService {

    List<Admin> selectAll();

    Admin selectById(Integer id);

    int add(Admin admin);

    int update(Admin admin);

    int deleteById(Integer id);

    // 登录验证方法
    Admin login(String username, String password);
}
