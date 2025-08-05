package llf.llf.controller;

import llf.llf.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件服务控制器
 * 处理图片上传和访问
 */
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Autowired
    private ImageUtils imageUtils;
    
    private static final String UPLOAD_DIR = "uploads/covers/";
    
    /**
     * 处理图片上传（base64）
     */
    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String base64Data = request.get("image");
            if (base64Data == null || base64Data.trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "图片数据不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 处理图片
            String imageUrl = imageUtils.processBase64Image(base64Data);
            
            response.put("code", 200);
            response.put("message", "图片上传成功");
            response.put("data", Map.of("url", imageUrl));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "图片上传失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取图片文件 - 调试版本
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // 构建完整的文件路径
            Path filePath = Paths.get(UPLOAD_DIR, filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            System.out.println("=== 文件访问调试信息 ===");
            System.out.println("请求文件: " + filename);
            System.out.println("UPLOAD_DIR: " + UPLOAD_DIR);
            System.out.println("文件路径: " + filePath.toAbsolutePath());
            System.out.println("文件存在: " + filePath.toFile().exists());
            System.out.println("文件可读: " + filePath.toFile().canRead());

            if (resource.exists() && resource.isReadable()) {
                // 根据文件扩展名设置Content-Type
                String contentType = getContentType(filename);

                System.out.println("文件访问成功，Content-Type: " + contentType);

                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600") // 缓存1小时
                    .body(resource);
            } else {
                System.out.println("文件不存在或不可读");
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            System.err.println("文件访问异常: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 删除图片
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String imageUrl = request.get("url");
            imageUtils.deleteImageFile(imageUrl);
            
            response.put("code", 200);
            response.put("message", "图片删除成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "图片删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取文件的Content-Type
     */
    private String getContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
}
