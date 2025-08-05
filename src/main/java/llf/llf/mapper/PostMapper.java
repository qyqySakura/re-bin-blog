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

    // 根据标签ID查询文章
    List<Post> selectByTagId(@Param("tagId") Integer tagId);

    // 根据标签ID分页查询文章
    List<Post> selectByTagIdWithPaging(@Param("tagId") Integer tagId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    // 统计标签下的文章数量
    int countPostsByTagId(@Param("tagId") Integer tagId);

    // 搜索文章（根据标题和内容）
    List<Post> searchPosts(@Param("keyword") String keyword);

    // 高级搜索文章（支持分页和过滤）
    List<Post> searchPostsAdvanced(@Param("keyword") String keyword,
                                 @Param("categoryId") Integer categoryId,
                                 @Param("timeRange") String timeRange,
                                 @Param("sortBy") String sortBy,
                                 @Param("offset") Integer offset,
                                 @Param("limit") Integer limit);

    // 统计搜索结果数量
    int countSearchResults(@Param("keyword") String keyword,
                          @Param("categoryId") Integer categoryId,
                          @Param("timeRange") String timeRange);

    // 根据分类ID分页查询文章
    List<Post> selectByCategoryIdWithPaging(@Param("categoryId") Integer categoryId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    // 统计分类下的文章数量
    int countPostsByCategoryId(@Param("categoryId") Integer categoryId);

    // 增加阅读量
    int incrementViewCount(@Param("postId") Integer postId);

    // 更新文章点赞数量
    int updateLikeCount(@Param("postId") Integer postId, @Param("likeCount") Integer likeCount);
}
