package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llf.llf.pojo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    
    List<Post> selectAll();
    
    Post selectById(@Param("id") Integer id);
    
    List<Post> selectByUserId(@Param("userId") Integer userId);
    
    List<Post> selectByCategoryId(@Param("categoryId") Integer categoryId);
    
    List<Post> selectByStatus(@Param("status") Integer status);
    
    int add(Post post);
    
    int update(Post post);
    
    int deleteById(@Param("id") Integer id);
    
    // 分页查询已发布的文章
    List<Post> selectPublishedPosts(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    // 统计已发布文章数量
    int countPublishedPosts();
}
