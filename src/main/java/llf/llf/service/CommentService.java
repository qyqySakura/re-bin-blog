package llf.llf.service;

import llf.llf.pojo.Comment;
import java.util.List;

public interface CommentService {
    
    List<Comment> selectAll();
    
    Comment selectById(Integer id);
    
    List<Comment> selectByPostId(Integer postId);
    
    List<Comment> selectByUserId(Integer userId);
    
    List<Comment> selectByParentId(Integer parentId);
    
    int add(Comment comment);
    
    int update(Comment comment);
    
    int deleteById(Integer id);
    
    // 删除文章的所有评论
    int deleteByPostId(Integer postId);
}
