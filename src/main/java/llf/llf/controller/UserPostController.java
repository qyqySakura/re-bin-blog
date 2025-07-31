package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.common.PermissionUtil;
import llf.llf.pojo.Post;
import llf.llf.service.PostService;
import llf.llf.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户文章管理控制器 - 个人博客模式
 * 只有网站作者(ID=1)和管理员可以访问这些接口
 */
@RestController
@RequestMapping("/user/posts")
public class UserPostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private PermissionUtil permissionUtil;

    /**
     * 获取文章列表（只有网站作者和管理员可以访问）
     */
    @GetMapping("/my")
    public Result<Map<String, Object>> getMyPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        try {
            // 验证文章管理权限
            PermissionUtil.requirePostManagementPermission();
            
            // 获取所有文章（网站作者和管理员可以看到所有文章）
            List<Post> allPosts = postService.selectAll();
            
            // 根据状态过滤
            List<Post> filteredPosts = allPosts.stream()
                    .filter(post -> status == null || post.getStatus().equals(status))
                    .toList();
            
            // 手动分页
            int total = filteredPosts.size();
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, total);
            
            List<Post> pagedPosts = filteredPosts.subList(startIndex, endIndex);
            
            // 为每篇文章添加标签信息
            for (Post post : pagedPosts) {
                post.setTags(tagService.selectByPostId(post.getId()));
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("posts", pagedPosts);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (int) Math.ceil((double) total / size));
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(403, e.getMessage());
        }
    }

    /**
     * 获取草稿文章
     */
    @GetMapping("/drafts")
    public Result<Map<String, Object>> getMyDrafts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return getMyPosts(page, size, 0); // status = 0 表示草稿
    }

    /**
     * 获取已发布文章
     */
    @GetMapping("/published")
    public Result<Map<String, Object>> getMyPublishedPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return getMyPosts(page, size, 1); // status = 1 表示已发布
    }

    /**
     * 发布文章（将草稿改为已发布状态）
     */
    @PutMapping("/{id}/publish")
    public Result<String> publishPost(@PathVariable Integer id) {
        try {
            // 验证文章管理权限
            PermissionUtil.requirePostManagementPermission();
            
            Post post = postService.selectById(id);
            if (post == null) {
                return Result.error(404, "文章不存在");
            }
            
            if (post.getStatus() == 1) {
                return Result.error(400, "文章已经是发布状态");
            }
            
            // 更新状态为已发布
            post.setStatus(1);
            postService.update(post);
            
            return Result.success("文章发布成功");
        } catch (Exception e) {
            return Result.error(403, e.getMessage());
        }
    }

    /**
     * 撤回文章（将已发布改为草稿状态）
     */
    @PutMapping("/{id}/unpublish")
    public Result<String> unpublishPost(@PathVariable Integer id) {
        try {
            // 验证文章管理权限
            PermissionUtil.requirePostManagementPermission();
            
            Post post = postService.selectById(id);
            if (post == null) {
                return Result.error(404, "文章不存在");
            }
            
            if (post.getStatus() == 0) {
                return Result.error(400, "文章已经是草稿状态");
            }
            
            // 更新状态为草稿
            post.setStatus(0);
            postService.update(post);
            
            return Result.success("文章已撤回为草稿");
        } catch (Exception e) {
            return Result.error(403, e.getMessage());
        }
    }

    /**
     * 获取文章统计信息
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getPostStats() {
        try {
            // 验证文章管理权限
            PermissionUtil.requirePostManagementPermission();
            
            List<Post> allPosts = postService.selectAll();
            
            long totalPosts = allPosts.size();
            long publishedPosts = allPosts.stream().filter(post -> post.getStatus() == 1).count();
            long draftPosts = allPosts.stream().filter(post -> post.getStatus() == 0).count();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", totalPosts);
            stats.put("published", publishedPosts);
            stats.put("drafts", draftPosts);
            
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(403, e.getMessage());
        }
    }

    /**
     * 批量删除文章
     */
    @DeleteMapping("/batch")
    public Result<String> batchDeletePosts(@RequestBody List<Integer> postIds) {
        try {
            // 验证文章管理权限
            PermissionUtil.requirePostManagementPermission();
            
            if (postIds == null || postIds.isEmpty()) {
                return Result.error(400, "请选择要删除的文章");
            }
            
            int deletedCount = 0;
            for (Integer postId : postIds) {
                // 删除文章的标签关联
                tagService.setPostTags(postId, null);
                // 删除文章
                postService.deleteById(postId);
                deletedCount++;
            }
            
            return Result.success("成功删除 " + deletedCount + " 篇文章");
        } catch (Exception e) {
            return Result.error(403, e.getMessage());
        }
    }

    /**
     * 批量更改文章状态
     */
    @PutMapping("/batch/status")
    public Result<String> batchUpdateStatus(@RequestBody Map<String, Object> requestData) {
        try {
            // 验证文章管理权限
            PermissionUtil.requirePostManagementPermission();
            
            @SuppressWarnings("unchecked")
            List<Integer> postIds = (List<Integer>) requestData.get("postIds");
            Integer status = (Integer) requestData.get("status");
            
            if (postIds == null || postIds.isEmpty()) {
                return Result.error(400, "请选择要操作的文章");
            }
            
            if (status == null || (status != 0 && status != 1)) {
                return Result.error(400, "状态参数错误");
            }
            
            int updatedCount = 0;
            for (Integer postId : postIds) {
                Post post = postService.selectById(postId);
                if (post != null) {
                    post.setStatus(status);
                    postService.update(post);
                    updatedCount++;
                }
            }
            
            String statusText = status == 1 ? "发布" : "设为草稿";
            return Result.success("成功" + statusText + " " + updatedCount + " 篇文章");
        } catch (Exception e) {
            return Result.error(403, e.getMessage());
        }
    }
}
