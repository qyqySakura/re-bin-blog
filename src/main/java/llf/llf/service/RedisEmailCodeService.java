package llf.llf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的邮件验证码服务
 */
@Service
public class RedisEmailCodeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisHealthService redisHealthService;

    // Redis降级方案：使用内存缓存
    private static final Map<String, CodeInfo> memoryCache = new ConcurrentHashMap<>();

    private static final String EMAIL_CODE_PREFIX = "email:code:";
    private static final long CODE_EXPIRE_TIME = 5; // 5分钟过期

    // 内存缓存的验证码信息
    private static class CodeInfo {
        private final String code;
        private final long expireTime;

        public CodeInfo(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }

        public String getCode() {
            return code;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    /**
     * 保存验证码到Redis，如果Redis不可用则使用内存缓存
     * @param email 邮箱地址
     * @param code 验证码
     */
    public void saveCode(String email, String code) {
        try {
            if (redisHealthService.isRedisAvailable()) {
                String key = EMAIL_CODE_PREFIX + email;
                redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
                System.out.println("验证码已保存到Redis: " + email);
            } else {
                // 降级到内存缓存
                long expireTime = System.currentTimeMillis() + CODE_EXPIRE_TIME * 60 * 1000;
                memoryCache.put(email, new CodeInfo(code, expireTime));
                System.out.println("Redis不可用，验证码已保存到内存缓存: " + email + ", 验证码: " + code);
            }
        } catch (Exception e) {
            // Redis异常时降级到内存缓存
            long expireTime = System.currentTimeMillis() + CODE_EXPIRE_TIME * 60 * 1000;
            memoryCache.put(email, new CodeInfo(code, expireTime));
            System.out.println("Redis异常，验证码已保存到内存缓存: " + email + ", 验证码: " + code + ", 错误: " + e.getMessage());
        }
    }

    /**
     * 从Redis获取验证码，如果Redis不可用则从内存缓存获取
     * @param email 邮箱地址
     * @return 验证码，如果不存在或已过期返回null
     */
    public String getCode(String email) {
        try {
            if (redisHealthService.isRedisAvailable()) {
                String key = EMAIL_CODE_PREFIX + email;
                Object code = redisTemplate.opsForValue().get(key);
                return code != null ? code.toString() : null;
            } else {
                // 从内存缓存获取
                CodeInfo codeInfo = memoryCache.get(email);
                if (codeInfo != null && !codeInfo.isExpired()) {
                    return codeInfo.getCode();
                } else if (codeInfo != null && codeInfo.isExpired()) {
                    // 清除过期的验证码
                    memoryCache.remove(email);
                }
                return null;
            }
        } catch (Exception e) {
            // Redis异常时从内存缓存获取
            CodeInfo codeInfo = memoryCache.get(email);
            if (codeInfo != null && !codeInfo.isExpired()) {
                return codeInfo.getCode();
            } else if (codeInfo != null && codeInfo.isExpired()) {
                memoryCache.remove(email);
            }
            return null;
        }
    }

    /**
     * 删除验证码
     * @param email 邮箱地址
     */
    public void removeCode(String email) {
        try {
            if (redisHealthService.isRedisAvailable()) {
                String key = EMAIL_CODE_PREFIX + email;
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            System.out.println("Redis删除验证码异常: " + e.getMessage());
        }
        // 无论Redis是否可用，都删除内存缓存中的验证码
        memoryCache.remove(email);
    }

    /**
     * 检查验证码是否存在
     * @param email 邮箱地址
     * @return true如果存在，false如果不存在或已过期
     */
    public boolean hasCode(String email) {
        String key = EMAIL_CODE_PREFIX + email;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 获取验证码剩余过期时间（秒）
     * @param email 邮箱地址
     * @return 剩余时间，-1表示不存在或已过期
     */
    public long getCodeExpireTime(String email) {
        String key = EMAIL_CODE_PREFIX + email;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}
