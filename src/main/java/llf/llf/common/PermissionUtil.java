package llf.llf.common;

import cn.dev33.satoken.stp.StpUtil;
import llf.llf.pojo.Post;
import llf.llf.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 权限验证工具类 - 个人博客模式
 * 只有网站作者(ID=1)和管理员可以管理文章
 * 普通用户只能浏览和评论
 */
@Component
public class PermissionUtil {
    
    @Autowired
    private PostService postService;
    
    /**
     * 检查当前用户是否为管理员
     * @return true-是管理员，false-不是管理员
     */
    public static boolean isAdmin() {
        if (!StpUtil.isLogin()) {
            return false;
        }
        
        String loginId = (String) StpUtil.getLoginId();
        return loginId != null && loginId.startsWith("admin_");
    }
    
    /**
     * 检查当前用户是否为普通用户
     * @return true-是普通用户，false-不是普通用户
     */
    public static boolean isUser() {
        if (!StpUtil.isLogin()) {
            return false;
        }
        
        String loginId = (String) StpUtil.getLoginId();
        return loginId != null && loginId.startsWith("user_");
    }
    
    /**
     * 检查当前用户是否为网站作者(ID=1)
     * @return true-是网站作者，false-不是网站作者
     */
    public static boolean isBlogAuthor() {
        Integer currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(1);
    }
    
    /**
     * 获取当前登录用户的ID
     * @return 用户ID，如果未登录返回null
     */
    public static Integer getCurrentUserId() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        
        String loginId = (String) StpUtil.getLoginId();
        if (loginId == null) {
            return null;
        }
        
        try {
            if (loginId.startsWith("user_")) {
                return Integer.parseInt(loginId.substring(5));
            } else if (loginId.startsWith("admin_")) {
                return Integer.parseInt(loginId.substring(6));
            }
        } catch (NumberFormatException e) {
            System.err.println("解析用户ID失败: " + loginId);
        }
        
        return null;
    }
    
    /**
     * 检查当前用户是否为文章的作者
     * @param postId 文章ID
     * @return true-是作者，false-不是作者
     */
    public boolean isPostAuthor(Integer postId) {
        if (!StpUtil.isLogin() || postId == null) {
            return false;
        }
        
        Integer currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }
        
        Post post = postService.selectById(postId);
        if (post == null) {
            return false;
        }
        
        return currentUserId.equals(post.getUserId());
    }
    
    /**
     * 检查当前用户是否有权限操作文章（网站作者或管理员）
     * 个人博客模式：只有网站作者和管理员可以操作文章
     * @param postId 文章ID
     * @return true-有权限，false-无权限
     */
    public boolean canOperatePost(Integer postId) {
        // 管理员有所有权限
        if (isAdmin()) {
            return true;
        }
        
        // 网站作者有权限操作所有文章
        if (isBlogAuthor()) {
            return true;
        }
        
        // 普通用户没有任何文章操作权限
        return false;
    }
    
    /**
     * 检查当前用户是否有权限创建文章
     * 个人博客模式：只有网站作者(ID=1)和管理员可以创建文章
     * @return true-有权限，false-无权限
     */
    public static boolean canCreatePost() {
        // 管理员可以创建
        if (isAdmin()) {
            return true;
        }
        
        // 网站作者(ID=1)可以创建
        return isBlogAuthor();
    }
    
    /**
     * 检查当前用户是否有权限发布文章
     * 个人博客模式：只有网站作者和管理员可以发布文章
     * @return true-有权限，false-无权限
     */
    public static boolean canPublishPost() {
        // 管理员可以发布
        if (isAdmin()) {
            return true;
        }
        
        // 网站作者可以发布
        return isBlogAuthor();
    }
    
    /**
     * 检查当前用户是否有权限访问文章管理功能
     * 个人博客模式：只有网站作者和管理员可以访问
     * @return true-有权限，false-无权限
     */
    public static boolean canAccessPostManagement() {
        return isAdmin() || isBlogAuthor();
    }
    
    /**
     * 验证权限，如果没有权限则抛出异常
     * @param hasPermission 是否有权限
     * @param message 错误消息
     */
    public static void requirePermission(boolean hasPermission, String message) {
        if (!hasPermission) {
            throw new RuntimeException(message);
        }
    }
    
    /**
     * 验证登录状态，如果未登录则抛出异常
     */
    public static void requireLogin() {
        if (!StpUtil.isLogin()) {
            throw new RuntimeException("请先登录");
        }
    }
    
    /**
     * 验证管理员权限，如果不是管理员则抛出异常
     */
    public static void requireAdmin() {
        requireLogin();
        if (!isAdmin()) {
            throw new RuntimeException("需要管理员权限");
        }
    }
    
    /**
     * 验证文章管理权限，如果不是网站作者或管理员则抛出异常
     */
    public static void requirePostManagementPermission() {
        requireLogin();
        if (!canAccessPostManagement()) {
            throw new RuntimeException("只有网站作者和管理员可以管理文章");
        }
    }
}
