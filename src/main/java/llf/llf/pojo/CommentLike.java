package llf.llf.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 评论点赞实体类
 */
@Data
@NoArgsConstructor
public class CommentLike {
    private Integer id;
    private Integer commentId;      // 评论ID
    private Integer userId;         // 用户ID
    private LocalDateTime createTime;
    
    // 关联对象
    private User user;              // 点赞用户信息
    private Comment comment;        // 评论信息
    
    public CommentLike(Integer commentId, Integer userId) {
        this.commentId = commentId;
        this.userId = userId;
    }
}
