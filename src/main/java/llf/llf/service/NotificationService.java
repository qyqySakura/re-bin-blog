package llf.llf.service;

import llf.llf.pojo.Notification;
import java.util.List;
import java.util.Map;

/**
 * 消息通知服务接口
 */
public interface NotificationService {
    
    /**
     * 创建点赞通知
     */
    void createLikeNotification(Integer recipientId, Integer senderId, Integer postId, String postTitle);

    /**
     * 创建评论通知
     */
    void createCommentNotification(Integer recipientId, Integer senderId, Integer postId, String postTitle, String commentContent);

    /**
     * 创建回复通知
     */
    void createReplyNotification(Integer recipientId, Integer senderId, Integer postId, String postTitle, String replyContent);

    /**
     * 创建关注通知
     */
    void createFollowNotification(Integer recipientId, Integer senderId);

    /**
     * 创建评论点赞通知
     */
    void createCommentLikeNotification(Integer recipientId, Integer senderId, Integer postId, String postTitle, String commentContent);

    /**
     * 分页查询用户通知
     */
    Map<String, Object> getUserNotifications(Integer userId, int page, int size);

    /**
     * 分页查询用户通知（带筛选）
     */
    Map<String, Object> getUserNotifications(Integer userId, int page, int size, String type, Boolean isRead);

    /**
     * 获取用户未读通知数量
     */
    int getUnreadCount(Integer userId);

    /**
     * 获取最近的未读通知
     */
    List<Notification> getRecentUnreadNotifications(Integer userId, int limit);

    /**
     * 标记通知为已读
     */
    boolean markAsRead(Integer notificationId, Integer userId);

    /**
     * 标记所有通知为已读
     */
    boolean markAllAsRead(Integer userId);

    /**
     * 删除通知
     */
    boolean deleteNotification(Integer notificationId, Integer userId);

    /**
     * 清理用户的旧通知
     */
    void cleanupOldNotifications(Integer userId);
}
