package llf.llf.mapper;

import llf.llf.pojo.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * 消息通知Mapper接口
 */
@Mapper
public interface NotificationMapper {

    /**
     * 插入通知
     */
    @Insert("""
        INSERT INTO notification (recipient_id, sender_id, type, title, content,
                                 related_id, related_type, is_read, create_time)
        VALUES (#{recipientId}, #{senderId}, #{type}, #{title}, #{content},
                #{relatedId}, #{relatedType}, #{isRead}, #{createTime})
        """)
    int insert(Notification notification);

    /**
     * 分页查询用户通知（包含发送者信息）
     */
    @Select("""
        SELECT n.*,
               u.name as sender_name, u.avatar as sender_avatar
        FROM notification n
        LEFT JOIN user u ON n.sender_id = u.id
        WHERE n.recipient_id = #{userId}
        ORDER BY n.create_time DESC
        LIMIT #{offset}, #{size}
        """)
    List<Notification> selectNotificationsByUserId(@Param("userId") Integer userId,
                                                   @Param("offset") int offset,
                                                   @Param("size") int size);

    /**
     * 分页查询用户通知（带筛选条件）
     */
    @Select("""
        <script>
        SELECT n.*,
               u.name as sender_name, u.avatar as sender_avatar
        FROM notification n
        LEFT JOIN user u ON n.sender_id = u.id
        WHERE n.recipient_id = #{userId}
        <if test="type != null and type != ''">
            AND n.type = #{type}
        </if>
        <if test="isRead != null">
            AND n.is_read = #{isRead}
        </if>
        ORDER BY n.create_time DESC
        LIMIT #{offset}, #{size}
        </script>
        """)
    List<Notification> selectNotificationsByUserIdWithFilter(@Param("userId") Integer userId,
                                                             @Param("offset") int offset,
                                                             @Param("size") int size,
                                                             @Param("type") String type,
                                                             @Param("isRead") Boolean isRead);
    
    /**
     * 查询用户通知总数
     */
    @Select("SELECT COUNT(*) FROM notification WHERE recipient_id = #{userId}")
    int countByUserId(@Param("userId") Integer userId);

    /**
     * 统计用户通知总数（带筛选条件）
     */
    @Select("""
        <script>
        SELECT COUNT(*) FROM notification
        WHERE recipient_id = #{userId}
        <if test="type != null and type != ''">
            AND type = #{type}
        </if>
        <if test="isRead != null">
            AND is_read = #{isRead}
        </if>
        </script>
        """)
    int countByUserIdWithFilter(@Param("userId") Integer userId,
                                @Param("type") String type,
                                @Param("isRead") Boolean isRead);

    /**
     * 查询用户未读通知数量
     */
    @Select("SELECT COUNT(*) FROM notification WHERE recipient_id = #{userId} AND is_read = 0")
    int countUnreadByUserId(@Param("userId") Integer userId);

    /**
     * 标记用户所有通知为已读
     */
    @Update("UPDATE notification SET is_read = 1, read_time = NOW() WHERE recipient_id = #{userId} AND is_read = 0")
    int markAllAsReadByUserId(@Param("userId") Integer userId);

    /**
     * 标记指定通知为已读
     */
    @Update("UPDATE notification SET is_read = 1, read_time = NOW() WHERE id = #{notificationId} AND recipient_id = #{userId}")
    int markAsRead(@Param("notificationId") Integer notificationId, @Param("userId") Integer userId);

    /**
     * 查询最近的未读通知
     */
    @Select("""
        SELECT n.*,
               u.name as sender_name, u.avatar as sender_avatar
        FROM notification n
        LEFT JOIN user u ON n.sender_id = u.id
        WHERE n.recipient_id = #{userId} AND n.is_read = 0
        ORDER BY n.create_time DESC
        LIMIT #{limit}
        """)
    List<Notification> selectRecentUnread(@Param("userId") Integer userId, @Param("limit") int limit);

    /**
     * 删除通知
     */
    @Delete("DELETE FROM notification WHERE id = #{notificationId} AND recipient_id = #{userId}")
    int deleteNotification(@Param("notificationId") Integer notificationId, @Param("userId") Integer userId);

    /**
     * 删除用户的旧通知（保留最近的指定数量）
     */
    @Delete("""
        DELETE FROM notification
        WHERE recipient_id = #{userId}
        AND id NOT IN (
            SELECT id FROM (
                SELECT id FROM notification
                WHERE recipient_id = #{userId}
                ORDER BY create_time DESC
                LIMIT #{keepCount}
            ) temp
        )
        """)
    int deleteOldNotifications(@Param("userId") Integer userId, @Param("keepCount") int keepCount);
}
