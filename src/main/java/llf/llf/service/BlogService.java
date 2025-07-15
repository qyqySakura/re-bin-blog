package llf.llf.service;

import llf.llf.pojo.*;
import java.util.List;
import java.util.Map;

/**
 * 博客服务接口 - 整合所有博客相关功能
 */
public interface BlogService {
    
    // ==================== 文章相关 ====================
    
    /**
     * 获取首页文章列表（分页）
     */
    Map<String, Object> getHomePagePosts(int page, int size);
    
    /**
     * 根据ID获取文章详情（包含作者、分类、标签信息）
     */
    Post getPostDetail(Integer id);
    
    /**
     * 根据分类获取文章列表
     */
    List<Post> getPostsByCategory(Integer categoryId);
    
    /**
     * 根据标签获取文章列表
     */
    List<Post> getPostsByTag(Integer tagId);
    
    /**
     * 搜索文章
     */
    List<Post> searchPosts(String keyword);
    
    /**
     * 获取文章归档（按年月分组）
     */
    Map<String, List<Post>> getPostArchive();
    
    /**
     * 获取热门文章
     */
    List<Post> getPopularPosts(int limit);
    
    /**
     * 获取最新文章
     */
    List<Post> getLatestPosts(int limit);
    
    // ==================== 分类相关 ====================
    
    /**
     * 获取所有分类及文章数量
     */
    List<Map<String, Object>> getCategoriesWithCount();
    
    /**
     * 获取分类详情
     */
    Category getCategoryDetail(Integer id);
    
    // ==================== 标签相关 ====================
    
    /**
     * 获取所有标签及文章数量
     */
    List<Map<String, Object>> getTagsWithCount();
    
    /**
     * 获取标签云数据
     */
    List<Map<String, Object>> getTagCloud();
    
    /**
     * 获取标签详情
     */
    Tag getTagDetail(Integer id);
    
    // ==================== 评论相关 ====================
    
    /**
     * 获取文章评论列表（树形结构）
     */
    List<Comment> getPostComments(Integer postId);
    
    /**
     * 添加评论
     */
    int addComment(Comment comment);
    
    /**
     * 删除评论
     */
    int deleteComment(Integer id, Integer userId);
    
    // ==================== 用户相关 ====================
    
    /**
     * 获取用户发布的文章
     */
    List<Post> getUserPosts(Integer userId);
    
    /**
     * 获取用户信息（公开信息）
     */
    Map<String, Object> getUserPublicInfo(Integer userId);
    
    // ==================== 统计相关 ====================
    
    /**
     * 获取博客统计信息
     */
    Map<String, Object> getBlogStatistics();
    
    /**
     * 获取网站基本信息
     */
    Map<String, Object> getSiteInfo();
}
