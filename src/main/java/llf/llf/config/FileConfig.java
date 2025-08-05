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
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根目录
        String projectRoot = System.getProperty("user.dir");

        // 文章封面访问 - /api/files/** -> uploads/covers/
        String coversPath = new File(uploadPath + "covers/").getAbsolutePath() + File.separator;
        registry.addResourceHandler(urlPattern)
                .addResourceLocations("file:" + coversPath)
                .setCachePeriod(3600);

        // 用户头像访问 - /avatar/** -> avatar/
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:" + projectRoot + File.separator + "avatar" + File.separator);

        // 静态资源访问 - /static/** -> vue/fronted/src/assets/
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("file:" + projectRoot + File.separator + "vue" + File.separator + "fronted" + File.separator + "src" + File.separator + "assets" + File.separator);

        System.out.println("静态资源映射配置完成:");
        System.out.println("文章封面: " + urlPattern + " -> " + coversPath);
        System.out.println("用户头像: /avatar/** -> " + projectRoot + "/avatar/");
        System.out.println("静态资源: /static/** -> " + projectRoot + "/vue/fronted/src/assets/");
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
