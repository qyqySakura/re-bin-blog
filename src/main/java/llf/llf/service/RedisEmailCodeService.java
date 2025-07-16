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
        // 清理过期的内存缓存
        cleanExpiredMemoryCache();

        try {
            if (redisHealthService.isRedisAvailable()) {
                String key = EMAIL_CODE_PREFIX + email;
                redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
                System.out.println("✓ 验证码已保存到Redis | 邮箱: " + email + " | 有效期: " + CODE_EXPIRE_TIME + "分钟");
            } else {
                // 降级到内存缓存
                long expireTime = System.currentTimeMillis() + CODE_EXPIRE_TIME * 60 * 1000;
                memoryCache.put(email, new CodeInfo(code, expireTime));
                System.out.println("⚠ Redis不可用，验证码已保存到内存缓存 | 邮箱: " + email + " | 验证码: " + code);
            }
        } catch (Exception e) {
            // Redis异常时降级到内存缓存
            long expireTime = System.currentTimeMillis() + CODE_EXPIRE_TIME * 60 * 1000;
            memoryCache.put(email, new CodeInfo(code, expireTime));
            System.err.println("✗ Redis异常，验证码已保存到内存缓存 | 邮箱: " + email + " | 验证码: " + code + " | 错误: " + e.getMessage());
        }
    }

    /**
     * 清理过期的内存缓存
     */
    private void cleanExpiredMemoryCache() {
        memoryCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    /**
     * 从Redis获取验证码，如果Redis不可用则从内存缓存获取
     * @param email 邮箱地址
     * @return 验证码，如果不存在或已过期返回null
     */
    public String getCode(String email) {
        // 清理过期的内存缓存
        cleanExpiredMemoryCache();

        try {
            if (redisHealthService.isRedisAvailable()) {
                String key = EMAIL_CODE_PREFIX + email;
                Object code = redisTemplate.opsForValue().get(key);
                if (code != null) {
                    System.out.println("✓ 从Redis获取验证码 | 邮箱: " + email + " | 验证码: " + code);
                    return code.toString();
                } else {
                    System.out.println("⚠ Redis中未找到验证码 | 邮箱: " + email);
                    return null;
                }
            } else {
                // 从内存缓存获取
                CodeInfo codeInfo = memoryCache.get(email);
                if (codeInfo != null && !codeInfo.isExpired()) {
                    System.out.println("✓ 从内存缓存获取验证码 | 邮箱: " + email + " | 验证码: " + codeInfo.getCode());
                    return codeInfo.getCode();
                } else if (codeInfo != null && codeInfo.isExpired()) {
                    // 清除过期的验证码
                    memoryCache.remove(email);
                    System.out.println("⚠ 内存缓存中验证码已过期 | 邮箱: " + email);
                } else {
                    System.out.println("⚠ 内存缓存中未找到验证码 | 邮箱: " + email);
                }
                return null;
            }
        } catch (Exception e) {
            System.err.println("✗ Redis获取验证码异常，尝试内存缓存 | 邮箱: " + email + " | 错误: " + e.getMessage());
            // Redis异常时从内存缓存获取
            CodeInfo codeInfo = memoryCache.get(email);
            if (codeInfo != null && !codeInfo.isExpired()) {
                System.out.println("✓ 从内存缓存获取验证码（Redis异常） | 邮箱: " + email + " | 验证码: " + codeInfo.getCode());
                return codeInfo.getCode();
            } else if (codeInfo != null && codeInfo.isExpired()) {
                memoryCache.remove(email);
                System.out.println("⚠ 内存缓存中验证码已过期（Redis异常） | 邮箱: " + email);
            }
            return null;
        }
    }

    /**
     * 删除验证码
     * @param email 邮箱地址
     */
    public void removeCode(String email) {
        boolean redisDeleted = false;
        boolean memoryDeleted = false;

        try {
            if (redisHealthService.isRedisAvailable()) {
                String key = EMAIL_CODE_PREFIX + email;
                Boolean deleted = redisTemplate.delete(key);
                redisDeleted = Boolean.TRUE.equals(deleted);
            }
        } catch (Exception e) {
            System.err.println("✗ Redis删除验证码异常 | 邮箱: " + email + " | 错误: " + e.getMessage());
        }

        // 无论Redis是否可用，都删除内存缓存中的验证码
        CodeInfo removed = memoryCache.remove(email);
        memoryDeleted = (removed != null);

        if (redisDeleted || memoryDeleted) {
            System.out.println("✓ 验证码已删除 | 邮箱: " + email + " | Redis: " + redisDeleted + " | 内存: " + memoryDeleted);
        } else {
            System.out.println("⚠ 未找到要删除的验证码 | 邮箱: " + email);
        }
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
