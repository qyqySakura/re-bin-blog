package llf.llf.service;

import llf.llf.common.Result;
import llf.llf.mapper.PostLikeMapper;
import llf.llf.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文章点赞服务类
 */
@Service
public class PostLikeService {

    @Autowired
    private PostLikeMapper postLikeMapper;
    
    @Autowired
    private PostMapper postMapper;

    /**
     * 切换点赞状态（点赞/取消点赞）
     */
    @Transactional
    public Result<String> toggleLike(Integer postId, Integer userId) {
        try {
            // 检查是否已点赞
            int isLiked = postLikeMapper.checkUserLike(postId, userId);
            
            if (isLiked > 0) {
                // 已点赞，取消点赞
                postLikeMapper.removeLike(postId, userId);
                // 更新文章点赞数量
                updatePostLikeCount(postId);
                return Result.success("取消点赞成功");
            } else {
                // 未点赞，添加点赞
                postLikeMapper.addLike(postId, userId);
                // 更新文章点赞数量
                updatePostLikeCount(postId);
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
    public boolean checkUserLike(Integer postId, Integer userId) {
        if (userId == null) return false;
        return postLikeMapper.checkUserLike(postId, userId) > 0;
    }

    /**
     * 获取用户点赞的文章ID列表
     */
    public List<Integer> getUserLikedPostIds(Integer userId, List<Integer> postIds) {
        if (userId == null || postIds == null || postIds.isEmpty()) {
            return List.of();
        }
        return postLikeMapper.getUserLikedPostIds(userId, postIds);
    }

    /**
     * 更新文章的点赞数量
     */
    private void updatePostLikeCount(Integer postId) {
        int likeCount = postLikeMapper.getLikeCount(postId);
        // 更新post表的like_count字段
        postMapper.updateLikeCount(postId, likeCount);
    }
}
