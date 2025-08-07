package llf.llf.service;

import llf.llf.pojo.Comment;
import java.util.List;

public interface CommentService {

    List<Comment> selectAll();

    Comment selectById(Integer id);

    List<Comment> selectByPostId(Integer postId);

    /**
     * 根据文章ID获取评论列表（包含点赞状态）
     */
    List<Comment> selectByPostIdWithLikeStatus(Integer postId, Integer currentUserId);

    List<Comment> selectByUserId(Integer userId);

    List<Comment> selectByParentId(Integer parentId);

    int add(Comment comment);

    int update(Comment comment);

    int deleteById(Integer id);

    // 删除文章的所有评论
    int deleteByPostId(Integer postId);

    // 分页查询评论（支持状态和关键词筛选）
    List<Comment> selectWithPagination(int offset, int size, String status, String keyword);

    // 获取筛选后的评论总数
    int countWithFilter(String status, String keyword);
}
