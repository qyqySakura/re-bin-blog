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
    private LocalDateTime createTime;
    
    // 关联对象
    private User user;          // 评论用户信息
    private Post post;          // 文章信息
}
