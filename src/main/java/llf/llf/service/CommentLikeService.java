package llf.llf.service;

import llf.llf.pojo.Comment;
import llf.llf.pojo.Post;
import llf.llf.mapper.CommentLikeMapper;
import llf.llf.mapper.CommentMapper;
import llf.llf.mapper.PostMapper;
import llf.llf.common.Result;
import llf.llf.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论点赞服务类
 */
@Service
public class CommentLikeService {

    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 切换点赞状态（点赞/取消点赞）
     */
    @Transactional
    public Result<String> toggleLike(Integer commentId, Integer userId) {
        try {
            // 检查是否已点赞
            int isLiked = commentLikeMapper.checkUserLike(commentId, userId);
            
            if (isLiked > 0) {
                // 已点赞，取消点赞
                commentLikeMapper.removeLike(commentId, userId);
                // 更新评论点赞数量
                updateCommentLikeCount(commentId);
                return Result.success("取消点赞成功");
            } else {
                // 未点赞，添加点赞
                commentLikeMapper.addLike(commentId, userId);
                // 更新评论点赞数量
                updateCommentLikeCount(commentId);

                // 发送评论点赞通知
                try {
                    Comment comment = commentMapper.selectById(commentId);
                    if (comment != null && comment.getUserId() != null) {
                        Post post = postMapper.selectById(comment.getPostId());
                        if (post != null) {
                            // 使用专门的评论点赞通知方法
                            notificationService.createCommentLikeNotification(
                                comment.getUserId(),  // 评论作者ID
                                userId,               // 点赞用户ID
                                post.getId(),         // 文章ID
                                post.getTitle(),      // 文章标题
                                comment.getContent()  // 评论内容
                            );
                        }
                    }
                } catch (Exception e) {
                    // 通知发送失败不影响点赞功能
                    System.err.println("发送评论点赞通知失败: " + e.getMessage());
                }

                return Result.success("点赞成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.internalError("操作失败");
        }
    }

    /**
     * 检查用户是否已点赞
     */
    public boolean checkUserLike(Integer commentId, Integer userId) {
        if (userId == null) return false;
        return commentLikeMapper.checkUserLike(commentId, userId) > 0;
    }

    /**
     * 获取用户点赞的评论ID列表
     */
    public List<Integer> getUserLikedCommentIds(Integer userId, List<Integer> commentIds) {
        if (userId == null || commentIds == null || commentIds.isEmpty()) {
            return List.of();
        }
        return commentLikeMapper.getUserLikedCommentIds(userId, commentIds);
    }

    /**
     * 更新评论的点赞数量
     */
    private void updateCommentLikeCount(Integer commentId) {
        int likeCount = commentLikeMapper.getLikeCount(commentId);
        // 更新comment表的like_count字段
        commentMapper.updateLikeCount(commentId, likeCount);
    }
}
