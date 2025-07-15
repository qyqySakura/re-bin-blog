package llf.llf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Redis健康检查服务
 */
@Service
public class RedisHealthService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    /**
     * 检查Redis连接是否正常
     */
    public boolean isRedisAvailable() {
        try {
            redisTemplate.opsForValue().set("health:check", "ok");
            String result = (String) redisTemplate.opsForValue().get("health:check");
            redisTemplate.delete("health:check");

            // 测试用：显示所有验证码
            Set<String> keys = redisTemplate.keys("email:code:*");
            if (keys != null && !keys.isEmpty()) {
                for (String key : keys) {
                    Object code = redisTemplate.opsForValue().get(key);
                    System.out.println("Redis中的验证码: " + key + " = " + code);
                }
            }

            return "ok".equals(result);
        } catch (Exception e) {
            System.out.println("Redis连接检查失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取Redis服务器信息
     */
    public Map<String, Object> getRedisInfo() {
        Map<String, Object> info = new HashMap<>();

        try {
            RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
            if (factory != null) {
                RedisConnection connection = factory.getConnection();
                Properties properties = connection.info();

                info.put("host", redisHost);
                info.put("port", redisPort);
                info.put("connected", true);

                // 提取一些重要信息
                if (properties != null) {
                    info.put("version", properties.getProperty("redis_version"));
                    info.put("mode", properties.getProperty("redis_mode"));
                    info.put("os", properties.getProperty("os"));
                    info.put("uptime", properties.getProperty("uptime_in_seconds"));
                    info.put("memory", properties.getProperty("used_memory_human"));
                    info.put("clients", properties.getProperty("connected_clients"));
                }

                connection.close();
            } else {
                info.put("connected", false);
                info.put("error", "无法获取Redis连接工厂");
            }
        } catch (Exception e) {
            info.put("connected", false);
            info.put("error", e.getMessage());
        }

        return info;
    }
}
