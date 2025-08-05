package llf.llf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器 - 用于验证文件配置
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/file-config")
    public Map<String, Object> testFileConfig() {
        Map<String, Object> result = new HashMap<>();
        
        // 检查uploads目录
        File uploadsDir = new File("uploads");
        File coversDir = new File("uploads/covers");
        
        result.put("uploadsExists", uploadsDir.exists());
        result.put("uploadsPath", uploadsDir.getAbsolutePath());
        result.put("coversExists", coversDir.exists());
        result.put("coversPath", coversDir.getAbsolutePath());
        
        // 检查目录权限
        result.put("uploadsCanWrite", uploadsDir.canWrite());
        result.put("coversCanWrite", coversDir.canWrite());
        
        // 列出covers目录下的文件
        if (coversDir.exists()) {
            String[] files = coversDir.list();
            result.put("filesInCovers", files != null ? files.length : 0);
            result.put("filesList", files);
        }
        
        return result;
    }
}
