package llf.llf.service;

import llf.llf.pojo.Post;
import java.util.List;

public interface PostService {
    
    List<Post> selectAll();
    
    Post selectById(Integer id);
    
    List<Post> selectByUserId(Integer userId);
    
    List<Post> selectByCategoryId(Integer categoryId);
    
    List<Post> selectByStatus(Integer status);
    
    int add(Post post);
    
    int update(Post post);
    
    int deleteById(Integer id);
    
    // 分页查询已发布的文章
    List<Post> selectPublishedPosts(Integer page, Integer size);
    
    // 统计已发布文章数量
    int countPublishedPosts();
}
