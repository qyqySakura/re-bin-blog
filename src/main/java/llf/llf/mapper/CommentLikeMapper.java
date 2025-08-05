package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llf.llf.pojo.CommentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论点赞Mapper接口
 */
@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {
    
    /**
     * 添加点赞
     */
    int addLike(@Param("commentId") Integer commentId, @Param("userId") Integer userId);
    
    /**
     * 取消点赞
     */
    int removeLike(@Param("commentId") Integer commentId, @Param("userId") Integer userId);
    
    /**
     * 检查用户是否已点赞
     */
    int checkUserLike(@Param("commentId") Integer commentId, @Param("userId") Integer userId);
    
    /**
     * 获取用户点赞的评论ID列表
     */
    List<Integer> getUserLikedCommentIds(@Param("userId") Integer userId, @Param("commentIds") List<Integer> commentIds);
    
    /**
     * 获取评论的点赞数量
     */
    int getLikeCount(@Param("commentId") Integer commentId);
}
