package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llf.llf.pojo.FriendLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
    
    List<FriendLink> selectAll();
    
    FriendLink selectById(@Param("id") Integer id);
    
    int add(FriendLink friendLink);
    
    int update(FriendLink friendLink);
    
    int deleteById(@Param("id") Integer id);
}
