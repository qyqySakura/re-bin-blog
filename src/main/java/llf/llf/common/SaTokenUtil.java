package llf.llf.common;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.secure.BCrypt;

/**
 * Sa-Token工具类
 */
public class SaTokenUtil {
    
    /**
     * 用户登录
     * @param loginId 用户ID
     * @param loginType 登录类型
     */
    public static void login(Object loginId, String loginType) {
        StpUtil.login(loginId, loginType);
    }
    
    /**
     * 用户登录（默认类型）
     * @param loginId 用户ID
     */
    public static void login(Object loginId) {
        StpUtil.login(loginId);
    }
    
    /**
     * 获取当前登录用户ID
     * @return 用户ID
     */
    public static Object getLoginId() {
        return StpUtil.getLoginId();
    }
    
    /**
     * 获取当前登录用户ID（指定类型）
     * @param loginType 登录类型
     * @return 用户ID
     */
    public static Object getLoginId(String loginType) {
        return StpUtil.getLoginId(loginType);
    }
    
    /**
     * 检查是否已登录
     * @return 是否已登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }
    
    /**
     * 检查是否已登录（指定类型）
     * @param loginType 登录类型
     * @return 是否已登录
     */
    public static boolean isLogin(String loginType) {
        return StpUtil.isLogin(loginType);
    }
    
    /**
     * 退出登录
     */
    public static void logout() {
        StpUtil.logout();
    }
    
    /**
     * 退出登录（指定类型）
     * @param loginType 登录类型
     */
    public static void logout(String loginType) {
        StpUtil.logout(loginType);
    }
    
    /**
     * 获取token值
     * @return token值
     */
    public static String getTokenValue() {
        return StpUtil.getTokenValue();
    }
    
    /**
     * 密码加密
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    /**
     * 密码验证
     * @param password 原始密码
     * @param hashedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
} 