package llf.llf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务
 */
@Service
public class RedisCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 缓存前缀
    private static final String USER_PREFIX = "user:";
    private static final String POST_PREFIX = "post:";
    private static final String CATEGORY_PREFIX = "category:";
    private static final String TAG_PREFIX = "tag:";
    private static final String COMMENT_PREFIX = "comment:";

    // 默认过期时间（小时）
    private static final long DEFAULT_EXPIRE_TIME = 24;

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存并指定过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 设置缓存并指定过期时间（秒）
     */
    public void set(String key, Object value, int seconds) {
        set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存并转换为指定类型
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object obj = get(key);
        return obj == null ? null : (T) obj;
    }

    /**
     * 删除缓存
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除缓存
     */
    public void delete(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 检查key是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置过期时间
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取过期时间
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    // ========== 用户相关缓存 ==========

    /**
     * 缓存用户信息
     */
    public void cacheUser(Long userId, Object user) {
        String key = USER_PREFIX + userId;
        set(key, user, DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 获取用户缓存
     */
    public Object getUser(Long userId) {
        String key = USER_PREFIX + userId;
        return get(key);
    }

    /**
     * 删除用户缓存
     */
    public void deleteUser(Long userId) {
        String key = USER_PREFIX + userId;
        delete(key);
    }

    // ========== 文章相关缓存 ==========

    /**
     * 缓存文章信息
     */
    public void cachePost(Long postId, Object post) {
        String key = POST_PREFIX + postId;
        set(key, post, DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 获取文章缓存
     */
    public Object getPost(Long postId) {
        String key = POST_PREFIX + postId;
        return get(key);
    }

    /**
     * 删除文章缓存
     */
    public void deletePost(Long postId) {
        String key = POST_PREFIX + postId;
        delete(key);
    }

    /**
     * 缓存文章列表
     */
    public void cachePostList(String listKey, List<Object> posts) {
        String key = POST_PREFIX + "list:" + listKey;
        set(key, posts, 1, TimeUnit.HOURS); // 列表缓存时间较短
    }

    /**
     * 获取文章列表缓存
     */
    public Object getPostList(String listKey) {
        String key = POST_PREFIX + "list:" + listKey;
        return get(key);
    }

    // ========== 分类相关缓存 ==========

    /**
     * 缓存分类信息
     */
    public void cacheCategory(Long categoryId, Object category) {
        String key = CATEGORY_PREFIX + categoryId;
        set(key, category, DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 获取分类缓存
     */
    public Object getCategory(Long categoryId) {
        String key = CATEGORY_PREFIX + categoryId;
        return get(key);
    }

    /**
     * 删除分类缓存
     */
    public void deleteCategory(Long categoryId) {
        String key = CATEGORY_PREFIX + categoryId;
        delete(key);
    }

    // ========== 标签相关缓存 ==========

    /**
     * 缓存标签信息
     */
    public void cacheTag(Long tagId, Object tag) {
        String key = TAG_PREFIX + tagId;
        set(key, tag, DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 获取标签缓存
     */
    public Object getTag(Long tagId) {
        String key = TAG_PREFIX + tagId;
        return get(key);
    }

    /**
     * 删除标签缓存
     */
    public void deleteTag(Long tagId) {
        String key = TAG_PREFIX + tagId;
        delete(key);
    }

    // ========== 评论相关缓存 ==========

    /**
     * 缓存评论信息
     */
    public void cacheComment(Long commentId, Object comment) {
        String key = COMMENT_PREFIX + commentId;
        set(key, comment, DEFAULT_EXPIRE_TIME, TimeUnit.HOURS);
    }

    /**
     * 获取评论缓存
     */
    public Object getComment(Long commentId) {
        String key = COMMENT_PREFIX + commentId;
        return get(key);
    }

    /**
     * 删除评论缓存
     */
    public void deleteComment(Long commentId) {
        String key = COMMENT_PREFIX + commentId;
        delete(key);
    }

    // ========== 清除相关缓存 ==========

    /**
     * 清除所有用户相关缓存
     */
    public void clearUserCache() {
        Set<String> keys = redisTemplate.keys(USER_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            delete(keys);
        }
    }

    /**
     * 清除所有文章相关缓存
     */
    public void clearPostCache() {
        Set<String> keys = redisTemplate.keys(POST_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            delete(keys);
        }
    }

    /**
     * 清除所有缓存
     */
    public void clearAllCache() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            delete(keys);
        }
    }
}
