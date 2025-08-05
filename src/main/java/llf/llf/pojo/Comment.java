package llf.llf.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {
    private Integer id;
    private Integer postId;     // 文章ID
    private Integer userId;     // 评论用户ID
    private String content;     // 评论内容
    private Integer parentId;   // 父评论ID（用于回复）
    private Integer replyToUserId; // 被回复的用户ID
    private LocalDateTime createTime;
    private Integer status;     // 状态：1-正常，0-删除
    private Integer likeCount;  // 点赞数量

    // 关联对象
    private User user;          // 评论用户信息
    private Post post;          // 文章信息
    private User replyToUser;   // 被回复的用户信息

    // 扩展字段（不存储到数据库）
    private Boolean isLiked;    // 当前用户是否已点赞
}
