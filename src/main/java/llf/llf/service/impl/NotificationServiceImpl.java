package llf.llf.service.impl;

import llf.llf.pojo.Notification;
import llf.llf.pojo.User;
import llf.llf.mapper.NotificationMapper;
import llf.llf.mapper.UserMapper;
import llf.llf.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息通知服务实现类
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationMapper notificationMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public void createLikeNotification(Integer recipientId, Integer senderId, Integer postId, String postTitle) {
        // 不给自己发通知
        if (recipientId.equals(senderId)) {
            return;
        }
        
        User sender = userMapper.selectById(senderId);
        if (sender == null) {
            return;
        }
        
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setSenderId(senderId);
        notification.setType(Notification.Type.LIKE);
        notification.setTitle("收到新的点赞");
        notification.setContent(sender.getName() + " 点赞了你的文章《" + postTitle + "》");
        notification.setRelatedId(postId);
        notification.setRelatedType(Notification.RelatedType.POST);
        notification.setIsRead(false);
        notification.setCreateTime(LocalDateTime.now());
        
        notificationMapper.insert(notification);
        
        // 清理旧通知
        cleanupOldNotifications(recipientId);
    }
    
    @Override
    public void createCommentNotification(Integer recipientId, Integer senderId, Integer postId, String postTitle, String commentContent) {
        if (recipientId.equals(senderId)) {
            return;
        }
        
        User sender = userMapper.selectById(senderId);
        if (sender == null) {
            return;
        }
        
        String content = commentContent.length() > 50 ? 
                commentContent.substring(0, 50) + "..." : commentContent;
        
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setSenderId(senderId);
        notification.setType(Notification.Type.COMMENT);
        notification.setTitle("收到新的评论");
        notification.setContent(sender.getName() + " 评论了你的文章《" + postTitle + "》：" + content);
        notification.setRelatedId(postId);
        notification.setRelatedType(Notification.RelatedType.POST);
        notification.setIsRead(false);
        notification.setCreateTime(LocalDateTime.now());
        
        notificationMapper.insert(notification);
        cleanupOldNotifications(recipientId);
    }
    
    @Override
    public void createReplyNotification(Integer recipientId, Integer senderId, Integer postId, String postTitle, String replyContent) {
        if (recipientId.equals(senderId)) {
            return;
        }
        
        User sender = userMapper.selectById(senderId);
        if (sender == null) {
            return;
        }
        
        String content = replyContent.length() > 50 ? 
                replyContent.substring(0, 50) + "..." : replyContent;
        
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setSenderId(senderId);
        notification.setType(Notification.Type.REPLY);
        notification.setTitle("收到新的回复");
        notification.setContent(sender.getName() + " 回复了你在《" + postTitle + "》的评论：" + content);
        notification.setRelatedId(postId);
        notification.setRelatedType(Notification.RelatedType.POST);
        notification.setIsRead(false);
        notification.setCreateTime(LocalDateTime.now());
        
        notificationMapper.insert(notification);
        cleanupOldNotifications(recipientId);
    }
    
    @Override
    public void createFollowNotification(Integer recipientId, Integer senderId) {
        if (recipientId.equals(senderId)) {
            return;
        }

        User sender = userMapper.selectById(senderId);
        if (sender == null) {
            return;
        }

        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setSenderId(senderId);
        notification.setType(Notification.Type.FOLLOW);
        notification.setTitle("新的关注者");
        notification.setContent(sender.getName() + " 关注了你");
        notification.setIsRead(false);
        notification.setCreateTime(LocalDateTime.now());

        notificationMapper.insert(notification);
        cleanupOldNotifications(recipientId);
    }

    @Override
    public void createCommentLikeNotification(Integer recipientId, Integer senderId, Integer postId, String postTitle, String commentContent) {
        if (recipientId.equals(senderId)) {
            return;
        }

        User sender = userMapper.selectById(senderId);
        if (sender == null) {
            return;
        }

        // 截取评论内容，避免过长
        String content = commentContent.length() > 30 ?
                commentContent.substring(0, 30) + "..." : commentContent;

        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setSenderId(senderId);
        notification.setType(Notification.Type.LIKE);  // 使用LIKE类型
        notification.setTitle("收到新的点赞");
        notification.setContent(sender.getName() + " 点赞了你在《" + postTitle + "》的评论：" + content);
        notification.setRelatedId(postId);
        notification.setRelatedType(Notification.RelatedType.POST);
        notification.setIsRead(false);
        notification.setCreateTime(LocalDateTime.now());

        notificationMapper.insert(notification);
        cleanupOldNotifications(recipientId);
    }
    
    @Override
    public Map<String, Object> getUserNotifications(Integer userId, int page, int size) {
        return getUserNotifications(userId, page, size, null, null);
    }

    @Override
    public Map<String, Object> getUserNotifications(Integer userId, int page, int size, String type, Boolean isRead) {
        int offset = (page - 1) * size;
        List<Notification> notifications = notificationMapper.selectNotificationsByUserIdWithFilter(userId, offset, size, type, isRead);
        int total = notificationMapper.countByUserIdWithFilter(userId, type, isRead);

        Map<String, Object> result = new HashMap<>();
        result.put("notifications", notifications);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return result;
    }
    
    @Override
    public int getUnreadCount(Integer userId) {
        return notificationMapper.countUnreadByUserId(userId);
    }
    
    @Override
    public List<Notification> getRecentUnreadNotifications(Integer userId, int limit) {
        return notificationMapper.selectRecentUnread(userId, limit);
    }
    
    @Override
    @Transactional
    public boolean markAsRead(Integer notificationId, Integer userId) {
        return notificationMapper.markAsRead(notificationId, userId) > 0;
    }
    
    @Override
    @Transactional
    public boolean markAllAsRead(Integer userId) {
        return notificationMapper.markAllAsReadByUserId(userId) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteNotification(Integer notificationId, Integer userId) {
        return notificationMapper.deleteNotification(notificationId, userId) > 0;
    }
    
    @Override
    public void cleanupOldNotifications(Integer userId) {
        // 保留最近100条通知，删除更旧的
        notificationMapper.deleteOldNotifications(userId, 100);
    }
}
