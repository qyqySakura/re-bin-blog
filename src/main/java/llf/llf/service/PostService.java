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

    // 根据标签ID查询文章
    List<Post> selectByTagId(Integer tagId);

    // 根据标签ID分页查询文章
    List<Post> selectByTagIdWithPaging(Integer tagId, Integer page, Integer size);

    // 统计标签下的文章数量
    int countPostsByTagId(Integer tagId);

    // 搜索文章
    List<Post> searchPosts(String keyword);

    // 根据分类ID分页查询文章
    List<Post> selectByCategoryIdWithPaging(Integer categoryId, Integer page, Integer size);

    // 统计分类下的文章数量
    int countPostsByCategoryId(Integer categoryId);
}
