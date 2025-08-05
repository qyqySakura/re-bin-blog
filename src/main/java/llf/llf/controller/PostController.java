package llf.llf.controller;

import cn.dev33.satoken.stp.StpUtil;
import llf.llf.common.Result;
import llf.llf.pojo.Post;
import llf.llf.service.PostService;
import llf.llf.service.PostLikeService;
import llf.llf.service.TagService;
import llf.llf.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageUtils imageUtils;

    /**
     * 获取当前登录用户的ID（处理Sa-Token的字符串格式）
     */
    private Integer getCurrentUserId() {
        if (!StpUtil.isLogin()) {
            return null;
        }

        try {
            String loginIdStr = StpUtil.getLoginIdAsString();
            // 处理"user_1"格式的ID，提取数字部分
            if (loginIdStr.startsWith("user_")) {
                return Integer.parseInt(loginIdStr.substring(5));
            } else {
                return Integer.parseInt(loginIdStr);
            }
        } catch (Exception e) {
            return null;
        }
    }

    // 查询所有文章
    @GetMapping
    public Result<List<Post>> getAllPosts() {
        return Result.success(postService.selectAll());
    }

    // 搜索文章（放在/{id}之前，避免路径冲突）
    @GetMapping("/search")
    public Result<List<Post>> searchPosts(@RequestParam String keyword) {
        // 调用实际的搜索逻辑
        return Result.success(postService.searchPosts(keyword));
    }

    // 根据ID查询文章（包含点赞状态，并增加阅读量）
    @GetMapping("/{id}")
    public Result<Post> getPostById(@PathVariable Integer id) {
        Integer currentUserId = getCurrentUserId();
        Post post = postService.selectByIdWithLikeStatusAndView(id, currentUserId);
        if (post != null) {
            // 获取文章的标签
            post.setTags(tagService.selectByPostId(id));
        }
        return Result.success(post);
    }

    // 根据用户ID查询文章
    @GetMapping("/user/{userId}")
    public Result<List<Post>> getPostsByUserId(@PathVariable Integer userId) {
        return Result.success(postService.selectByUserId(userId));
    }

    // 根据分类ID查询文章
    @GetMapping("/category/{categoryId}")
    public Result<List<Post>> getPostsByCategoryId(@PathVariable Integer categoryId) {
        return Result.success(postService.selectByCategoryId(categoryId));
    }

    // 查询已发布的文章（分页）
    @GetMapping("/published")
    public Result<Map<String, Object>> getPublishedPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        List<Post> posts = postService.selectPublishedPosts(page, size);
        int total = postService.countPublishedPosts();
        
        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (int) Math.ceil((double) total / size));
        
        return Result.success(result);
    }

    // 新增文章
    @PostMapping("/add")
    public Result<Post> createPost(@RequestBody Map<String, Object> requestData) {
        try {
            Post post = new Post();
            post.setUserId((Integer) requestData.get("userId"));
            post.setCategoryId((Integer) requestData.get("categoryId"));
            post.setTitle((String) requestData.get("title"));
            post.setContent((String) requestData.get("content"));
            post.setSummary((String) requestData.get("summary"));
            post.setStatus((Integer) requestData.get("status"));

            // 处理封面图片
            String coverData = (String) requestData.get("cover");
            if (StringUtils.hasText(coverData)) {
                if (coverData.startsWith("data:image/")) {
                    // 如果是base64图片，进行压缩处理
                    String processedCoverUrl = imageUtils.processBase64Image(coverData);
                    post.setCover(processedCoverUrl);
                } else if (imageUtils.isValidImageUrl(coverData)) {
                    // 如果是有效的图片URL，直接使用
                    post.setCover(coverData);
                } else {
                    // 无效的图片数据，设为空
                    post.setCover(null);
                }
            } else {
                post.setCover(null);
            }

            postService.add(post);

            // 处理标签 - 需要先获取插入后的文章ID
            @SuppressWarnings("unchecked")
            List<Integer> tagIds = (List<Integer>) requestData.get("tagIds");
            if (tagIds != null && !tagIds.isEmpty() && post.getId() != null) {
                tagService.setPostTags(post.getId(), tagIds);
            }

            return Result.success(post);

        } catch (Exception e) {
            return Result.error(500, "创建文章失败: " + e.getMessage());
        }
    }

    // 更新文章
    @PutMapping("/update")
    public Result<Integer> updatePost(@RequestBody Map<String, Object> requestData) {
        Post post = new Post();
        post.setId((Integer) requestData.get("id"));
        post.setCategoryId((Integer) requestData.get("categoryId"));
        post.setTitle((String) requestData.get("title"));
        post.setContent((String) requestData.get("content"));
        post.setSummary((String) requestData.get("summary"));
        post.setCover((String) requestData.get("cover"));
        post.setStatus((Integer) requestData.get("status"));
        
        int result = postService.update(post);
        
        // 处理标签
        @SuppressWarnings("unchecked")
        List<Integer> tagIds = (List<Integer>) requestData.get("tagIds");
        tagService.setPostTags(post.getId(), tagIds);
        
        return Result.success(result);
    }

    // 删除文章
    @DeleteMapping("/del/{id}")
    public Result<Integer> deletePost(@PathVariable Integer id) {
        // 删除文章的标签关联
        tagService.setPostTags(id, null);
        // 删除文章
        return Result.success(postService.deleteById(id));
    }

    // 点赞/取消点赞文章
    @PostMapping("/like/{postId}")
    public Result<String> toggleLike(@PathVariable Integer postId) {
        // 检查用户是否登录
        if (!StpUtil.isLogin()) {
            return Result.unauthorized("请先登录");
        }

        Integer userId = getCurrentUserId();
        if (userId == null) {
            return Result.badRequest("用户ID格式错误");
        }

        return postLikeService.toggleLike(postId, userId);
    }
}
