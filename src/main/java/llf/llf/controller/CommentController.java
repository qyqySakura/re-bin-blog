package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.pojo.Comment;
import llf.llf.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

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

    // 根据文章ID查询评论
    @GetMapping("/post/{postId}")
    public Result<List<Comment>> getCommentsByPostId(@PathVariable Integer postId) {
        return Result.success(commentService.selectByPostId(postId));
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
    public Result<Integer> createComment(@RequestBody Comment comment) {
        return Result.success(commentService.add(comment));
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
}
