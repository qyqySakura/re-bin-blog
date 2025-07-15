package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.pojo.Post;
import llf.llf.service.PostService;
import llf.llf.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TagService tagService;

    // 查询所有文章
    @GetMapping
    public Result<List<Post>> getAllPosts() {
        return Result.success(postService.selectAll());
    }

    // 根据ID查询文章
    @GetMapping("/{id}")
    public Result<Post> getPostById(@PathVariable Integer id) {
        Post post = postService.selectById(id);
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
        Post post = new Post();
        post.setUserId((Integer) requestData.get("userId"));
        post.setCategoryId((Integer) requestData.get("categoryId"));
        post.setTitle((String) requestData.get("title"));
        post.setContent((String) requestData.get("content"));
        post.setSummary((String) requestData.get("summary"));
        post.setCover((String) requestData.get("cover"));
        post.setStatus((Integer) requestData.get("status"));

        postService.add(post);

        // 处理标签 - 需要先获取插入后的文章ID
        @SuppressWarnings("unchecked")
        List<Integer> tagIds = (List<Integer>) requestData.get("tagIds");
        if (tagIds != null && !tagIds.isEmpty() && post.getId() != null) {
            tagService.setPostTags(post.getId(), tagIds);
        }

        return Result.success(post);
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
}
