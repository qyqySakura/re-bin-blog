package llf.llf.service;

import llf.llf.common.BusinessException;
import llf.llf.aremove.SpringContextUtil;
import llf.llf.mapper.AdminMapper;
import llf.llf.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class AvatarService {

    // 物理磁盘路径，和静态资源映射一致
    private static final String AVATAR_BASE_PATH = "D:/xm/xm/llf/avatar/";

    public String uploadAvatar(MultipartFile file, String role, Integer id) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        // 1. 保存文件到物理磁盘
        String originalName = Objects.requireNonNull(file.getOriginalFilename()).replaceAll("[^a-zA-Z0-9.\\-_]", "_");
        String fileName = System.currentTimeMillis() + "_" + originalName;
        File dest = new File(AVATAR_BASE_PATH + fileName);
        // 自动创建目录
        File parent = dest.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new BusinessException("头像上传失败: " + e.getMessage());
        }
        // 数据库只存相对路径
        String url = "/avatar/" + fileName;

        // 2. 更新数据库
        if ("user".equals(role)) {
            UserMapper userMapper = SpringContextUtil.getBean(llf.llf.mapper.UserMapper.class);
            userMapper.updateAvatar(id, url);
        } else if ("admin".equals(role)) {
            AdminMapper adminMapper = SpringContextUtil.getBean(llf.llf.mapper.AdminMapper.class);
            adminMapper.updateAvatar(id, url);
        } else {
            throw new BusinessException("未知角色类型: " + role);
        }
        return url;
    }
}