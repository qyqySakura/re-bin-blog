package llf.llf.controller;

import llf.llf.pojo.Notification;
import llf.llf.service.NotificationService;
import llf.llf.common.SaTokenUtil;
import llf.llf.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息通知控制器
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    /**
     * 获取用户通知列表
     */
    @GetMapping
    public Result<Map<String, Object>> getNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean isRead) {

        try {
            if (!SaTokenUtil.isLogin()) {
                return Result.unauthorized("请先登录");
            }

            Integer userId = getCurrentUserId();
            if (userId == null) {
                return Result.unauthorized("用户身份验证失败");
            }

            Map<String, Object> data = notificationService.getUserNotifications(userId, page, size, type, isRead);

            return Result.success(data);

        } catch (Exception e) {
            return Result.internalError("获取通知失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount() {
        try {
            if (!SaTokenUtil.isLogin()) {
                return Result.unauthorized("请先登录");
            }

            Integer userId = getCurrentUserId();
            if (userId == null) {
                return Result.unauthorized("用户身份验证失败");
            }

            int count = notificationService.getUnreadCount(userId);

            return Result.success(count);

        } catch (Exception e) {
            return Result.internalError("获取未读数量失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取最近的未读通知
     */
    @GetMapping("/recent")
    public Result<List<Notification>> getRecentNotifications(
            @RequestParam(defaultValue = "5") int limit) {

        try {
            if (!SaTokenUtil.isLogin()) {
                return Result.unauthorized("请先登录");
            }

            Integer userId = getCurrentUserId();
            if (userId == null) {
                return Result.unauthorized("用户身份验证失败");
            }

            List<Notification> notifications = notificationService.getRecentUnreadNotifications(userId, limit);

            return Result.success(notifications);

        } catch (Exception e) {
            return Result.internalError("获取最近通知失败: " + e.getMessage());
        }
    }
    
    /**
     * 标记通知为已读
     */
    @PutMapping("/{id}/read")
    public Result<String> markAsRead(@PathVariable Integer id) {

        try {
            if (!SaTokenUtil.isLogin()) {
                return Result.unauthorized("请先登录");
            }

            Integer userId = getCurrentUserId();
            if (userId == null) {
                return Result.unauthorized("用户身份验证失败");
            }

            boolean success = notificationService.markAsRead(id, userId);

            if (success) {
                return Result.success("标记成功");
            } else {
                return Result.error(404, "通知不存在");
            }

        } catch (Exception e) {
            return Result.internalError("标记失败: " + e.getMessage());
        }
    }
    
    /**
     * 标记所有通知为已读
     */
    @PutMapping("/read-all")
    public Result<String> markAllAsRead() {
        try {
            if (!SaTokenUtil.isLogin()) {
                return Result.unauthorized("请先登录");
            }

            Integer userId = getCurrentUserId();
            if (userId == null) {
                return Result.unauthorized("用户身份验证失败");
            }

            notificationService.markAllAsRead(userId);

            return Result.success("全部标记为已读");

        } catch (Exception e) {
            return Result.internalError("标记失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除通知
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteNotification(@PathVariable Integer id) {

        try {
            if (!SaTokenUtil.isLogin()) {
                return Result.unauthorized("请先登录");
            }

            Integer userId = getCurrentUserId();
            if (userId == null) {
                return Result.unauthorized("用户身份验证失败");
            }

            boolean success = notificationService.deleteNotification(id, userId);

            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error(404, "通知不存在");
            }

        } catch (Exception e) {
            return Result.internalError("删除失败: " + e.getMessage());
        }
    }

    /**
     * 创建测试通知（仅用于测试）
     */
    @GetMapping("/test")
    public Result<String> createTestNotifications() {
        try {
            if (!SaTokenUtil.isLogin()) {
                return Result.unauthorized("请先登录");
            }

            Integer userId = getCurrentUserId();
            if (userId == null) {
                return Result.unauthorized("用户身份验证失败");
            }

            // 创建不同类型的测试通知
            notificationService.createLikeNotification(userId, 2, 5, "aa");
            notificationService.createCommentNotification(userId, 3, 5, "aa", "这篇文章写得很好！");
            notificationService.createReplyNotification(userId, 2, 5, "aa", "我也是这么认为的");
            notificationService.createFollowNotification(userId, 3);

            return Result.success("测试通知创建成功");

        } catch (Exception e) {
            return Result.internalError("创建测试通知失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户ID
     * 从Sa-Token的loginId中解析出实际的用户ID
     */
    private Integer getCurrentUserId() {
        try {
            String loginId = SaTokenUtil.getLoginId().toString();

            // 检查是否是用户登录（以"user_"开头）
            if (loginId.startsWith("user_")) {
                return Integer.parseInt(loginId.substring(5)); // 去掉"user_"前缀
            }

            // 如果不是用户登录，返回null
            return null;

        } catch (Exception e) {
            return null;
        }
    }
}
