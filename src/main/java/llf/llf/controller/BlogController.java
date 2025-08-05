package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.pojo.*;
import llf.llf.service.BlogService;
import llf.llf.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 博客前台控制器
 */
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private PostService postService;

    // ==================== 首页相关 ====================

    /**
     * 获取首页文章列表
     */
    @GetMapping("/posts")
    public Result<Map<String, Object>> getHomePosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = blogService.getHomePagePosts(page, size);
        return Result.success(result);
    }

    /**
     * 获取文章详情
     */
    @GetMapping("/posts/{id}")
    public Result<Post> getPostDetail(@PathVariable Integer id) {
        Post post = blogService.getPostDetail(id);
        if (post != null) {
            return Result.success(post);
        } else {
            return Result.error(404, "文章不存在");
        }
    }

    /**
     * 获取最新文章
     */
    @GetMapping("/posts/latest")
    public Result<List<Post>> getLatestPosts(@RequestParam(defaultValue = "5") int limit) {
        List<Post> posts = blogService.getLatestPosts(limit);
        return Result.success(posts);
    }

    /**
     * 获取热门文章
     */
    @GetMapping("/posts/popular")
    public Result<List<Post>> getPopularPosts(@RequestParam(defaultValue = "5") int limit) {
        List<Post> posts = blogService.getPopularPosts(limit);
        return Result.success(posts);
    }

    // ==================== 分类相关 ====================

    /**
     * 获取所有分类及文章数量
     */
    @GetMapping("/categories")
    public Result<List<Map<String, Object>>> getCategories() {
        List<Map<String, Object>> categories = blogService.getCategoriesWithCount();
        return Result.success(categories);
    }

    /**
     * 获取分类详情
     */
    @GetMapping("/categories/{id}")
    public Result<Category> getCategoryDetail(@PathVariable Integer id) {
        Category category = blogService.getCategoryDetail(id);
        if (category != null) {
            return Result.success(category);
        } else {
            return Result.error(404, "分类不存在");
        }
    }

    /**
     * 根据分类获取文章列表（支持分页）
     */
    @GetMapping("/categories/{id}/posts")
    public Result<Map<String, Object>> getPostsByCategory(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {

        // 获取分类信息
        Category category = blogService.getCategoryDetail(id);
        if (category == null) {
            return Result.error(404, "分类不存在");
        }

        // 分页查询文章
        List<Post> posts = postService.selectByCategoryIdWithPaging(id, page, size);
        int total = postService.countPostsByCategoryId(id);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("category", category);

        return Result.success(result);
    }

    // ==================== 标签相关 ====================

    /**
     * 获取所有标签及文章数量
     */
    @GetMapping("/tags")
    public Result<List<Map<String, Object>>> getTags() {
        List<Map<String, Object>> tags = blogService.getTagsWithCount();
        return Result.success(tags);
    }

    /**
     * 获取标签云数据
     */
    @GetMapping("/tags/cloud")
    public Result<List<Map<String, Object>>> getTagCloud() {
        List<Map<String, Object>> tagCloud = blogService.getTagCloud();
        return Result.success(tagCloud);
    }

    /**
     * 获取标签详情
     */
    @GetMapping("/tags/{id}")
    public Result<Tag> getTagDetail(@PathVariable Integer id) {
        Tag tag = blogService.getTagDetail(id);
        if (tag != null) {
            return Result.success(tag);
        } else {
            return Result.error(404, "标签不存在");
        }
    }

    /**
     * 根据标签获取文章列表（支持分页）
     */
    @GetMapping("/tags/{id}/posts")
    public Result<Map<String, Object>> getPostsByTag(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size) {

        // 获取标签信息
        Tag tag = blogService.getTagDetail(id);
        if (tag == null) {
            return Result.error(404, "标签不存在");
        }

        // 分页查询文章
        List<Post> posts = postService.selectByTagIdWithPaging(id, page, size);
        int total = postService.countPostsByTagId(id);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("tag", tag);

        return Result.success(result);
    }

    // ==================== 搜索相关 ====================

    /**
     * 搜索文章（支持分页和过滤）
     */
    @GetMapping("/search")
    public Result<Map<String, Object>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "relevance") String sortBy,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String timeRange) {

        Map<String, Object> result = blogService.searchPostsWithPaging(
            keyword, page, pageSize, sortBy, category, timeRange
        );
        return Result.success(result);
    }

    // ==================== 归档相关 ====================

    /**
     * 获取文章归档
     */
    @GetMapping("/archive")
    public Result<Map<String, List<Post>>> getPostArchive() {
        Map<String, List<Post>> archive = blogService.getPostArchive();
        return Result.success(archive);
    }

    // ==================== 评论相关 ====================

    /**
     * 获取文章评论
     */
    @GetMapping("/posts/{postId}/comments")
    public Result<List<Comment>> getPostComments(@PathVariable Integer postId) {
        List<Comment> comments = blogService.getPostComments(postId);
        return Result.success(comments);
    }

    /**
     * 添加评论
     */
    @PostMapping("/comments")
    public Result<String> addComment(@RequestBody Comment comment) {
        // 这里应该从当前登录用户获取userId，暂时从请求体获取
        int result = blogService.addComment(comment);
        if (result > 0) {
            return Result.success("评论成功");
        } else {
            return Result.error(500, "评论失败");
        }
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/comments/{id}")
    public Result<String> deleteComment(@PathVariable Integer id, @RequestParam Integer userId) {
        int result = blogService.deleteComment(id, userId);
        if (result > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error(500, "删除失败或无权限");
        }
    }

    // ==================== 用户相关 ====================

    /**
     * 获取用户公开信息
     */
    @GetMapping("/users/{id}")
    public Result<Map<String, Object>> getUserInfo(@PathVariable Integer id) {
        Map<String, Object> userInfo = blogService.getUserPublicInfo(id);
        if (userInfo != null) {
            return Result.success(userInfo);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * 获取用户文章列表
     */
    @GetMapping("/users/{id}/posts")
    public Result<List<Post>> getUserPosts(@PathVariable Integer id) {
        List<Post> posts = blogService.getUserPosts(id);
        return Result.success(posts);
    }

    // ==================== 统计相关 ====================

    /**
     * 获取博客统计信息
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getBlogStatistics() {
        Map<String, Object> stats = blogService.getBlogStatistics();
        return Result.success(stats);
    }

    /**
     * 获取网站信息
     */
    @GetMapping("/site-info")
    public Result<Map<String, Object>> getSiteInfo() {
        Map<String, Object> siteInfo = blogService.getSiteInfo();
        return Result.success(siteInfo);
    }

    // ==================== 友链相关 ====================

    /**
     * 获取所有友链
     */
    @GetMapping("/friendlinks")
    public Result<List<FriendLink>> getFriendLinks() {
        List<FriendLink> friendLinks = blogService.getFriendLinks();
        return Result.success(friendLinks);
    }
}
