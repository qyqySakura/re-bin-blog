package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import llf.llf.pojo.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    int add(User user);

    int update(User user);

    int deleteById(Integer id);

    User selectById(Integer id);

    List<User> selectAll();
    
    // 登录验证方法
    User login(String username, String password);

    @Update("UPDATE user SET avatar = #{avatar} WHERE id = #{id}")
int updateAvatar(@Param("id") Integer id, @Param("avatar") String avatarUrl);

}
