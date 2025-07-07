package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import llf.llf.pojo.User;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    int add(User user);

    int update(User user);

    int deleteById(Integer id);

    User selectById(Integer id);

    List<User> selectAll();
}
