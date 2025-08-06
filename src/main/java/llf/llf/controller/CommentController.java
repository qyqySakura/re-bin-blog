package llf.llf.controller;

import cn.dev33.satoken.stp.StpUtil;
import llf.llf.common.Result;
import llf.llf.pojo.Comment;
import llf.llf.service.CommentService;
import llf.llf.service.CommentLikeService;
import llf.llf.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentLikeService commentLikeService;

    @Autowired
    private BlogService blogService;

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

    // 查询所有评论
    @GetMapping
    public Result<List<Comment>> getAllComments() {
        return Result.success(commentService.selectAll());
    }

    // 根据ID查询评论
    @GetMapping("/{id}")
    public Result<Comment> getCommentById(@PathVariable Integer id) {
        return Result.success(commentService.selectById(id));
    }

    // 根据文章ID查询评论（包含点赞状态）
    @GetMapping("/post/{postId}")
    public Result<List<Comment>> getCommentsByPostId(@PathVariable Integer postId) {
        Integer currentUserId = getCurrentUserId();
        return Result.success(commentService.selectByPostIdWithLikeStatus(postId, currentUserId));
    }

    // 根据用户ID查询评论
    @GetMapping("/user/{userId}")
    public Result<List<Comment>> getCommentsByUserId(@PathVariable Integer userId) {
        return Result.success(commentService.selectByUserId(userId));
    }

    // 根据父评论ID查询回复
    @GetMapping("/parent/{parentId}")
    public Result<List<Comment>> getCommentsByParentId(@PathVariable Integer parentId) {
        return Result.success(commentService.selectByParentId(parentId));
    }

    // 新增评论
    @PostMapping("/add")
    public Result<String> createComment(@RequestBody Comment comment) {
        // 检查用户是否登录
        if (!StpUtil.isLogin()) {
            return Result.unauthorized("请先登录");
        }

        Integer userId = getCurrentUserId();
        if (userId == null) {
            return Result.badRequest("用户ID格式错误");
        }

        // 设置评论的用户ID
        comment.setUserId(userId);

        // 使用BlogService的addComment方法，这样会触发通知逻辑
        int result = blogService.addComment(comment);

        if (result > 0) {
            return Result.success("评论成功");
        } else {
            return Result.error(500, "评论失败");
        }
    }

    // 更新评论
    @PutMapping("/update")
    public Result<Integer> updateComment(@RequestBody Comment comment) {
        return Result.success(commentService.update(comment));
    }

    // 删除评论
    @DeleteMapping("/del/{id}")
    public Result<Integer> deleteComment(@PathVariable Integer id) {
        return Result.success(commentService.deleteById(id));
    }

    // 点赞/取消点赞评论
    @PostMapping("/like/{commentId}")
    public Result<String> toggleLike(@PathVariable Integer commentId) {
        // 检查用户是否登录
        if (!StpUtil.isLogin()) {
            return Result.unauthorized("请先登录");
        }

        Integer userId = getCurrentUserId();
        if (userId == null) {
            return Result.badRequest("用户ID格式错误");
        }

        return commentLikeService.toggleLike(commentId, userId);
    }
}
