package llf.llf.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

/**
 * 图片处理工具类
 * 提供图片压缩、格式转换、存储等功能
 */
@Component
public class ImageUtils {
    
    // 上传目录配置
    private static final String UPLOAD_DIR = "uploads/covers/";
    private static final String BASE_URL = "/api/files/";
    
    // 图片压缩配置
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 600;
    private static final float QUALITY = 0.8f;
    private static final long MAX_SIZE = 2 * 1024 * 1024; // 2MB
    
    /**
     * 处理base64图片：压缩并保存为文件
     * @param base64Data base64图片数据
     * @return 图片访问路径
     */
    public String processBase64Image(String base64Data) {
        if (!StringUtils.hasText(base64Data)) {
            return null;
        }
        
        try {
            // 解析base64数据
            String[] parts = base64Data.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("无效的base64图片数据");
            }
            
            String header = parts[0]; // data:image/jpeg;base64
            String data = parts[1];   // 实际的base64数据
            
            // 获取图片格式
            String format = extractImageFormat(header);
            
            // 解码base64
            byte[] imageBytes = Base64.getDecoder().decode(data);
            
            // 检查文件大小
            if (imageBytes.length > MAX_SIZE) {
                throw new IllegalArgumentException("图片文件过大，请选择小于2MB的图片");
            }
            
            // 压缩图片
            byte[] compressedBytes = compressImage(imageBytes, format);
            
            // 保存文件
            String fileName = generateFileName(format);
            saveImageFile(compressedBytes, fileName);

            // 返回访问URL
            String accessUrl = BASE_URL + fileName;
            System.out.println("图片保存成功: " + fileName);
            System.out.println("访问URL: " + accessUrl);

            return accessUrl;
            
        } catch (Exception e) {
            throw new RuntimeException("图片处理失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 压缩图片
     */
    private byte[] compressImage(byte[] imageBytes, String format) throws IOException {
        // 读取原图
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (originalImage == null) {
            throw new IllegalArgumentException("无法读取图片数据");
        }
        
        // 计算压缩后的尺寸
        Dimension newSize = calculateNewSize(originalImage.getWidth(), originalImage.getHeight());
        
        // 创建压缩后的图片
        BufferedImage compressedImage = new BufferedImage(
            newSize.width, newSize.height, BufferedImage.TYPE_INT_RGB
        );
        
        // 绘制压缩图片
        Graphics2D g2d = compressedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.drawImage(originalImage, 0, 0, newSize.width, newSize.height, null);
        g2d.dispose();
        
        // 转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(compressedImage, "jpg", baos); // 统一转为jpg格式
        
        return baos.toByteArray();
    }
    
    /**
     * 计算新的图片尺寸
     */
    private Dimension calculateNewSize(int originalWidth, int originalHeight) {
        int newWidth = originalWidth;
        int newHeight = originalHeight;
        
        // 按比例缩放
        if (originalWidth > MAX_WIDTH) {
            newWidth = MAX_WIDTH;
            newHeight = (originalHeight * MAX_WIDTH) / originalWidth;
        }
        
        if (newHeight > MAX_HEIGHT) {
            newHeight = MAX_HEIGHT;
            newWidth = (newWidth * MAX_HEIGHT) / newHeight;
        }
        
        return new Dimension(newWidth, newHeight);
    }
    
    /**
     * 提取图片格式
     */
    private String extractImageFormat(String header) {
        if (header.contains("image/png")) {
            return "png";
        } else if (header.contains("image/gif")) {
            return "gif";
        } else {
            return "jpg"; // 默认jpg
        }
    }
    
    /**
     * 生成文件名
     */
    private String generateFileName(String format) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + ".jpg"; // 统一使用jpg扩展名
    }
    
    /**
     * 保存图片文件
     */
    private void saveImageFile(byte[] imageBytes, String fileName) throws IOException {
        // 确保上传目录存在
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("创建上传目录: " + uploadPath.toAbsolutePath());
        }

        // 保存文件
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, imageBytes);

        System.out.println("文件保存到: " + filePath.toAbsolutePath());
    }
    
    /**
     * 删除图片文件
     */
    public void deleteImageFile(String imageUrl) {
        if (!StringUtils.hasText(imageUrl) || !imageUrl.startsWith(BASE_URL)) {
            return;
        }
        
        try {
            String fileName = imageUrl.substring(BASE_URL.length());
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            // 删除失败不影响主流程，记录日志即可
            System.err.println("删除图片文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证图片URL格式
     */
    public boolean isValidImageUrl(String imageUrl) {
        if (!StringUtils.hasText(imageUrl)) {
            return false;
        }
        
        // 支持网络URL和本地文件路径
        return imageUrl.startsWith("http://") || 
               imageUrl.startsWith("https://") || 
               imageUrl.startsWith(BASE_URL);
    }
    
    /**
     * 获取图片文件大小（KB）
     */
    public long getImageSize(byte[] imageBytes) {
        return imageBytes.length / 1024;
    }
}
