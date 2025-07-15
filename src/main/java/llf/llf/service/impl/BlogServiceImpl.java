package llf.llf.service.impl;

import llf.llf.mapper.*;
import llf.llf.pojo.*;
import llf.llf.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private TagMapper tagMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getHomePagePosts(int page, int size) {
        // 计算偏移量
        int offset = (page - 1) * size;

        // 获取已发布的文章
        List<Post> posts = postMapper.selectPublishedPosts(offset, size);
        int total = postMapper.countPublishedPosts();

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (int) Math.ceil((double) total / size));

        return result;
    }

    @Override
    public Post getPostDetail(Integer id) {
        Post post = postMapper.selectById(id);
        if (post != null && post.getStatus() == 1) { // 只返回已发布的文章
            return post;
        }
        return null;
    }

    @Override
    public List<Post> getPostsByCategory(Integer categoryId) {
        List<Post> posts = postMapper.selectByCategoryId(categoryId);
        // 只返回已发布的文章
        return posts.stream()
                .filter(post -> post.getStatus() == 1)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getPostsByTag(Integer tagId) {
        // 这里需要在PostMapper中添加根据标签查询的方法
        // List<Post> posts = postMapper.selectByTagId(tagId);
        return new ArrayList<>(); // 临时返回空列表
    }

    @Override
    public List<Post> searchPosts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // 这里需要在PostMapper中添加搜索方法
        // List<Post> posts = postMapper.searchPosts(keyword);
        return new ArrayList<>(); // 临时返回空列表
    }

    @Override
    public Map<String, List<Post>> getPostArchive() {
        List<Post> allPosts = postMapper.selectByStatus(1); // 获取所有已发布文章

        // 按年月分组
        return allPosts.stream()
                .collect(Collectors.groupingBy(post -> {
                    LocalDateTime createTime = post.getCreateTime();
                    return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                }));
    }

    @Override
    public List<Post> getPopularPosts(int limit) {
        // 这里可以根据评论数、点击数等排序，暂时按创建时间排序
        List<Post> posts = postMapper.selectByStatus(1);
        return posts.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getLatestPosts(int limit) {
        return postMapper.selectPublishedPosts(0, limit);
    }

    @Override
    public List<Map<String, Object>> getCategoriesWithCount() {
        List<Category> categories = categoryMapper.selectAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Category category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", category.getId());
            item.put("name", category.getName());
            item.put("description", category.getDescription());

            // 统计该分类下的文章数量
            List<Post> posts = postMapper.selectByCategoryId(category.getId());
            long count = posts.stream().filter(post -> post.getStatus() == 1).count();
            item.put("postCount", count);

            result.add(item);
        }

        return result;
    }

    @Override
    public Category getCategoryDetail(Integer id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<Map<String, Object>> getTagsWithCount() {
        List<Tag> tags = tagMapper.selectAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Tag tag : tags) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", tag.getId());
            item.put("name", tag.getName());

            // 这里需要统计标签下的文章数量，暂时设为0
            item.put("postCount", 0);

            result.add(item);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getTagCloud() {
        // 标签云数据，包含标签名和权重
        return getTagsWithCount();
    }

    @Override
    public Tag getTagDetail(Integer id) {
        return tagMapper.selectById(id);
    }

    @Override
    public List<Comment> getPostComments(Integer postId) {
        return commentMapper.selectByPostId(postId);
    }

    @Override
    public int addComment(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        int result = commentMapper.add(comment);
        return result;
    }

    @Override
    public int deleteComment(Integer id, Integer userId) {
        Comment comment = commentMapper.selectById(id);
        if (comment != null && comment.getUserId().equals(userId)) {
            return commentMapper.deleteById(id);
        }
        return 0;
    }

    @Override
    public List<Post> getUserPosts(Integer userId) {
        return postMapper.selectByUserId(userId);
    }

    @Override
    public Map<String, Object> getUserPublicInfo(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("name", user.getName());
            userInfo.put("username", user.getUsername());
            userInfo.put("avatar", user.getAvatar());
            userInfo.put("createTime", user.getCreateTime());

            // 统计用户文章数
            List<Post> posts = postMapper.selectByUserId(userId);
            long publishedCount = posts.stream().filter(post -> post.getStatus() == 1).count();
            userInfo.put("postCount", publishedCount);

            return userInfo;
        }
        return null;
    }

    @Override
    public Map<String, Object> getBlogStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // 文章统计
        stats.put("totalPosts", postMapper.countPublishedPosts());
        stats.put("totalCategories", categoryMapper.selectAll().size());
        stats.put("totalTags", tagMapper.selectAll().size());
        stats.put("totalUsers", userMapper.selectAll().size());

        return stats;
    }

    @Override
    public Map<String, Object> getSiteInfo() {
        Map<String, Object> siteInfo = new HashMap<>();
        siteInfo.put("siteName", "RE-BIN博客");
        siteInfo.put("siteDescription", "一个基于Spring Boot + Vue的个人博客系统");
        siteInfo.put("author", "RE-BIN");
        siteInfo.put("version", "1.0.0");

        return siteInfo;
    }
}
