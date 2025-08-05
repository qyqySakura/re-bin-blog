package llf.llf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageUploadConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 上传文件访问
        registry.addResourceHandler("uploads/**").addResourceLocations("file:D:\\xm\\xm\\llf\\uploads\\");

        // 静态资源访问（包括默认头像）
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("file:D:\\xm\\xm\\llf\\vue\\fronted\\src\\assets\\");
    }
}
