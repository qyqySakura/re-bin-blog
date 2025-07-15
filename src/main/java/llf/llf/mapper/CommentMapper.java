package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llf.llf.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    
    List<Comment> selectAll();
    
    Comment selectById(@Param("id") Integer id);
    
    List<Comment> selectByPostId(@Param("postId") Integer postId);
    
    List<Comment> selectByUserId(@Param("userId") Integer userId);
    
    List<Comment> selectByParentId(@Param("parentId") Integer parentId);
    
    int add(Comment comment);
    
    int update(Comment comment);
    
    int deleteById(@Param("id") Integer id);
    
    // 删除文章的所有评论
    int deleteByPostId(@Param("postId") Integer postId);
}
