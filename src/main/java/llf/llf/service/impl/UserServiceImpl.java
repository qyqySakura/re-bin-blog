package llf.llf.service.impl;


import llf.llf.mapper.UserMapper;
import llf.llf.pojo.User;
import llf.llf.service.RedisEmailCodeService;
import llf.llf.service.UserService;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisEmailCodeService redisEmailCodeService;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Override
    public String generateAndSendEmailCode(String email) {
        // 1. 生成6位验证码
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        // 2. 保存到Redis缓存，5分钟过期
        redisEmailCodeService.saveCode(email, code);

        try {
            // 3. 发送邮件 (临时禁用，直接打印验证码)
            System.out.println("【测试模式】邮件发送已禁用，验证码: " + code + " 已保存到Redis，请使用此验证码测试");

            // /* 实际邮件发送代码（暂时注释）
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // 使用配置文件中的邮箱地址
            message.setTo(email);
            message.setSubject("RE-BIN博客 - 注册验证码");
            message.setText("RE-BIN博客注册验证：\n\n" +
                    "您的验证码是：" + code + "\n\n" +
                    "验证码有效期5分钟，请及时使用。\n\n" +
                    "如果这不是您的操作，请忽略此邮件。\n\n" +
                    "感谢您使用RE-BIN博客！");
            mailSender.send(message);

            System.out.println("邮件发送成功: " + email + ", 验证码: " + code);
        } catch (Exception e) {
            System.out.println("邮件发送失败: " + email + ", 错误: " + e.getMessage());
            // 即使邮件发送失败，也返回验证码，因为已经保存到缓存中
            // 在实际生产环境中，可以记录日志或发送警报
        }

        return code;
    }

    @Override
    public boolean verifyEmailCode(String email, String code) {
        if (email == null || code == null) {
            return false;
        }

        String cachedCode = redisEmailCodeService.getCode(email);
        if (cachedCode != null && cachedCode.equals(code)) {
            // 验证成功后删除验证码
            redisEmailCodeService.removeCode(email);
            return true;
        }

        return false;
    }

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public int add(User user) {
        return userMapper.add(user);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    public int deleteById(Integer id) {
        return userMapper.deleteById(id);
    }
    
    @Override
    public User login(String username, String password) {
        return userMapper.login(username, password);
    }
}
