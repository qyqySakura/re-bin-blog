package llf.llf.service.impl;

import llf.llf.mapper.CommentMapper;
import llf.llf.pojo.Comment;
import llf.llf.service.CommentService;
import llf.llf.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentLikeService commentLikeService;

    @Override
    public List<Comment> selectAll() {
        return commentMapper.selectAll();
    }

    @Override
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    @Override
    public List<Comment> selectByPostId(Integer postId) {
        return commentMapper.selectByPostId(postId);
    }

    @Override
    public List<Comment> selectByPostIdWithLikeStatus(Integer postId, Integer currentUserId) {
        List<Comment> comments = commentMapper.selectByPostId(postId);

        if (currentUserId != null && !comments.isEmpty()) {
            // 获取所有评论ID
            List<Integer> commentIds = comments.stream()
                    .map(Comment::getId)
                    .collect(Collectors.toList());

            // 获取用户已点赞的评论ID列表
            List<Integer> likedCommentIds = commentLikeService.getUserLikedCommentIds(currentUserId, commentIds);

            // 设置点赞状态
            comments.forEach(comment -> {
                comment.setIsLiked(likedCommentIds.contains(comment.getId()));
            });
        } else {
            // 未登录用户，所有评论都设为未点赞
            comments.forEach(comment -> comment.setIsLiked(false));
        }

        return comments;
    }

    @Override
    public List<Comment> selectByUserId(Integer userId) {
        return commentMapper.selectByUserId(userId);
    }

    @Override
    public List<Comment> selectByParentId(Integer parentId) {
        return commentMapper.selectByParentId(parentId);
    }

    @Override
    public int add(Comment comment) {
        return commentMapper.add(comment);
    }

    @Override
    public int update(Comment comment) {
        return commentMapper.update(comment);
    }

    @Override
    public int deleteById(Integer id) {
        return commentMapper.deleteById(id);
    }

    @Override
    public int deleteByPostId(Integer postId) {
        return commentMapper.deleteByPostId(postId);
    }
}
