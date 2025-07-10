package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import llf.llf.pojo.Admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author sakura
* @description 针对表【admin】的数据库操作Mapper
* @createDate 2025-07-07 15:59:09
* @Entity llf.domain.Admin
*/

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> selectAll();

    Admin selectById(@Param("id") Integer id);

    int add(Admin admin);

    int update(Admin admin);

    int deleteById(@Param("id") Integer id);

    // 登录验证方法
    Admin login(@Param("username") String username, @Param("password") String password);

    // 头像专用更新
    @Update("UPDATE admin SET avatar = #{avatarUrl} WHERE id = #{id}")
    int updateAvatar(@Param("id") Integer id, @Param("avatarUrl") String avatarUrl);
}




