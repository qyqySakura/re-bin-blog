package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.service.RedisHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Redis测试控制器
 */
@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisHealthService redisHealthService;

    /**
     * 测试Redis连接
     */
    @GetMapping("/test")
    public Result<Map<String, Object>> testRedisConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查Redis连接
            boolean isAvailable = redisHealthService.isRedisAvailable();
            result.put("connected", isAvailable);
            
            if (isAvailable) {
                // 测试基本操作
                redisTemplate.opsForValue().set("test:key", "Hello Redis!");
                String value = (String) redisTemplate.opsForValue().get("test:key");
                result.put("testValue", value);
                
                // 获取Redis信息
                result.put("message", "Redis连接成功");
                
                // 清理测试数据
                redisTemplate.delete("test:key");
            } else {
                result.put("message", "Redis连接失败");
            }
            
            return Result.success(result);
        } catch (Exception e) {
            result.put("connected", false);
            result.put("error", e.getMessage());
            result.put("message", "Redis连接异常");
            return Result.error(500, "Redis连接测试失败", result);
        }
    }

    /**
     * 获取Redis中的所有验证码
     */
    @GetMapping("/codes")
    public Result<Map<String, Object>> getAllCodes() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Set<String> keys = redisTemplate.keys("email:code:*");
            Map<String, Object> codes = new HashMap<>();
            
            if (keys != null) {
                for (String key : keys) {
                    Object code = redisTemplate.opsForValue().get(key);
                    Long expire = redisTemplate.getExpire(key);
                    
                    Map<String, Object> codeInfo = new HashMap<>();
                    codeInfo.put("code", code);
                    codeInfo.put("expireSeconds", expire);
                    
                    codes.put(key, codeInfo);
                }
            }
            
            result.put("codes", codes);
            result.put("count", codes.size());
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取验证码失败：" + e.getMessage());
        }
    }

    /**
     * 清除所有验证码
     */
    @DeleteMapping("/codes")
    public Result<String> clearAllCodes() {
        try {
            Set<String> keys = redisTemplate.keys("email:code:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                return Result.success("已清除 " + keys.size() + " 个验证码");
            } else {
                return Result.success("没有找到验证码");
            }
        } catch (Exception e) {
            return Result.error(500, "清除验证码失败：" + e.getMessage());
        }
    }

    /**
     * 获取Redis统计信息
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getRedisStats() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取所有key的数量
            Set<String> allKeys = redisTemplate.keys("*");
            result.put("totalKeys", allKeys != null ? allKeys.size() : 0);
            
            // 按前缀分组统计
            Map<String, Integer> keysByPrefix = new HashMap<>();
            if (allKeys != null) {
                for (String key : allKeys) {
                    String prefix = key.contains(":") ? key.substring(0, key.indexOf(":")) : "other";
                    keysByPrefix.put(prefix, keysByPrefix.getOrDefault(prefix, 0) + 1);
                }
            }
            result.put("keysByPrefix", keysByPrefix);
            
            // Redis连接状态
            result.put("connected", redisHealthService.isRedisAvailable());
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "获取Redis统计信息失败：" + e.getMessage());
        }
    }
}
