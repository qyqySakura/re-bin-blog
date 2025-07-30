package llf.llf.service.impl;

import llf.llf.mapper.*;
import llf.llf.pojo.*;
import llf.llf.service.BlogService;
import llf.llf.service.PostService;
import llf.llf.service.FriendLinkService;
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

    @Autowired
    private PostService postService;

    @Autowired
    private FriendLinkService friendLinkService;

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
        return postService.selectByTagId(tagId);
    }

    @Override
    public List<Post> searchPosts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return postService.searchPosts(keyword);
    }

    @Override
    public Map<String, Object> searchPostsWithPaging(String keyword, int page, int pageSize,
                                                    String sortBy, Integer categoryId, String timeRange) {
        if (keyword == null || keyword.trim().isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("posts", new ArrayList<>());
            result.put("total", 0);
            result.put("page", page);
            result.put("size", pageSize);
            result.put("totalPages", 0);
            return result;
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        // 获取搜索结果
        List<Post> posts = postMapper.searchPostsAdvanced(keyword, categoryId, timeRange, sortBy, offset, pageSize);
        int total = postMapper.countSearchResults(keyword, categoryId, timeRange);

        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("total", total);
        result.put("page", page);
        result.put("size", pageSize);
        result.put("totalPages", (int) Math.ceil((double) total / pageSize));

        return result;
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
        List<Category> categories = categoryMapper.selectAllWithPostCount();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Category category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", category.getId());
            item.put("name", category.getName());
            item.put("description", category.getDescription());
            item.put("postCount", category.getPostCount() != null ? category.getPostCount() : 0);

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
        List<Tag> tags = tagMapper.selectAllWithPostCount();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Tag tag : tags) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", tag.getId());
            item.put("name", tag.getName());
            item.put("postCount", tag.getPostCount() != null ? tag.getPostCount() : 0);

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

    @Override
    public List<FriendLink> getFriendLinks() {
        return friendLinkService.selectAll();
    }
}
