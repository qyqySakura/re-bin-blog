package llf.llf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llf.llf.pojo.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章点赞Mapper接口
 */
@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {
    
    /**
     * 添加点赞
     */
    int addLike(@Param("postId") Integer postId, @Param("userId") Integer userId);
    
    /**
     * 取消点赞
     */
    int removeLike(@Param("postId") Integer postId, @Param("userId") Integer userId);
    
    /**
     * 检查用户是否已点赞
     */
    int checkUserLike(@Param("postId") Integer postId, @Param("userId") Integer userId);
    
    /**
     * 获取用户点赞的文章ID列表
     */
    List<Integer> getUserLikedPostIds(@Param("userId") Integer userId, @Param("postIds") List<Integer> postIds);
    
    /**
     * 获取文章的点赞数量
     */
    int getLikeCount(@Param("postId") Integer postId);
}
