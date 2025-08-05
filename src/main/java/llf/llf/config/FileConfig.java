package llf.llf.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.io.File;

/**
 * 文件配置类
 * 配置文件上传路径和静态资源映射
 */
@Configuration
public class FileConfig implements WebMvcConfigurer {
    
    @Value("${file.upload.path:uploads/}")
    private String uploadPath;
    
    @Value("${file.upload.url-pattern:/api/files/**}")
    private String urlPattern;
    
    /**
     * 配置静态资源映射
     * 将 /api/files/** 映射到文件上传目录
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取绝对路径 - 映射到covers子目录
        String coversPath = new File(uploadPath + "covers/").getAbsolutePath() + File.separator;

        // 映射文件访问路径
        registry.addResourceHandler(urlPattern)
                .addResourceLocations("file:" + coversPath)
                .setCachePeriod(3600); // 缓存1小时

        System.out.println("静态资源映射配置:");
        System.out.println("URL Pattern: " + urlPattern);
        System.out.println("File Location: file:" + coversPath);
        System.out.println("映射说明: /api/files/xxx.jpg -> " + coversPath + "xxx.jpg");
    }
    
    /**
     * 初始化上传目录
     */
    @PostConstruct
    public void initUploadDir() {
        createDirIfNotExists(uploadPath);
        createDirIfNotExists(uploadPath + "covers/");
        createDirIfNotExists(uploadPath + "content/");
        createDirIfNotExists(uploadPath + "avatars/");
        createDirIfNotExists(uploadPath + "temp/");
        
        System.out.println("文件上传目录初始化完成: " + new File(uploadPath).getAbsolutePath());
    }
    
    /**
     * 创建目录（如果不存在）
     */
    private void createDirIfNotExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("创建目录: " + dir.getAbsolutePath());
            }
        }
    }
    
    // Getter方法
    public String getUploadPath() {
        return uploadPath;
    }
    
    public String getUrlPattern() {
        return urlPattern;
    }
}
