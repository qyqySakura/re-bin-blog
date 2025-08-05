package llf.llf.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 文章点赞实体类
 */
@Data
@NoArgsConstructor
public class PostLike {
    private Integer id;
    private Integer postId;         // 文章ID
    private Integer userId;         // 用户ID
    private LocalDateTime createTime;
    
    // 关联对象
    private User user;              // 点赞用户信息
    private Post post;              // 文章信息
    
    public PostLike(Integer postId, Integer userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
