package llf.llf.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 消息通知实体类
 */
@Data
@NoArgsConstructor
public class Notification {
    
    private Integer id;
    
    /**
     * 接收者用户ID
     */
    private Integer recipientId;
    
    /**
     * 发送者用户ID
     */
    private Integer senderId;
    
    /**
     * 通知类型
     */
    private String type;
    
    /**
     * 通知标题
     */
    private String title;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 相关对象ID（文章ID、评论ID等）
     */
    private Integer relatedId;
    
    /**
     * 相关对象类型
     */
    private String relatedType;
    
    /**
     * 是否已读
     */
    private Boolean isRead;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 阅读时间
     */
    private LocalDateTime readTime;
    
    /**
     * 发送者信息（非数据库字段）
     */
    private User sender;
    
    /**
     * 相关文章信息（非数据库字段）
     */
    private Post relatedPost;
    
    /**
     * 通知类型常量
     */
    public static class Type {
        public static final String LIKE = "LIKE";
        public static final String COMMENT = "COMMENT";
        public static final String REPLY = "REPLY";
        public static final String FOLLOW = "FOLLOW";
    }
    
    /**
     * 相关对象类型常量
     */
    public static class RelatedType {
        public static final String POST = "POST";
        public static final String COMMENT = "COMMENT";
    }
}
