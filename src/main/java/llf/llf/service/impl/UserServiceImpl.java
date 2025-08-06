package llf.llf.service.impl;


import llf.llf.common.BusinessException;
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
        // 1. 参数验证
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("邮箱地址不能为空");
        }

        // 2. 检查是否频繁发送（60秒内只能发送一次）
        String lastSendKey = "email_code_last_send:" + email;
        String lastSendTime = redisEmailCodeService.getCode(lastSendKey);
        if (lastSendTime != null) {
            long timeDiff = System.currentTimeMillis() - Long.parseLong(lastSendTime);
            if (timeDiff < 60000) { // 60秒限制
                long remainingSeconds = (60000 - timeDiff) / 1000;
                throw new BusinessException("验证码发送过于频繁，请" + remainingSeconds + "秒后再试");
            }
        }

        // 3. 生成6位安全验证码
        String code = generateSecureCode();

        // 4. 保存验证码到Redis缓存，5分钟过期
        redisEmailCodeService.saveCode(email, code);

        // 5. 记录发送时间，防止频繁发送
        redisEmailCodeService.saveCode(lastSendKey, String.valueOf(System.currentTimeMillis()));

        try {
            // 6. 发送邮件
            System.out.println("【验证码服务】邮箱: " + email + " | 验证码: " + code + " | 有效期: 5分钟");

            // 实际邮件发送代码
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("RE-BIN博客 - 邮箱验证码");
            message.setText(buildEmailContent(code));
            mailSender.send(message);
            System.out.println("验证码邮件发送成功: " + email);

        } catch (Exception e) {
            System.err.println("验证码邮件发送失败: " + email + " | 错误: " + e.getMessage());
            // 邮件发送失败不影响验证码的使用，因为已保存到缓存
            // 但是需要抛出异常让前端知道发送失败
            throw new BusinessException("邮件发送失败: " + e.getMessage());
        }

        return code;
    }

    /**
     * 生成安全的6位数字验证码
     */
    private String generateSecureCode() {
        java.security.SecureRandom random = new java.security.SecureRandom();
        int code = 100000 + random.nextInt(900000); // 生成100000-999999之间的数字
        return String.valueOf(code);
    }

    /**
     * 构建邮件内容
     */
    private String buildEmailContent(String code) {
        return "【RE-BIN博客】邮箱验证\n\n" +
               "您好！\n\n" +
               "您的验证码是：" + code + "\n\n" +
               "验证码有效期为5分钟，请及时使用。\n" +
               "为了您的账户安全，请勿将验证码告诉他人。\n\n" +
               "如果这不是您的操作，请忽略此邮件。\n\n" +
               "此邮件由系统自动发送，请勿回复。\n\n" +
               "RE-BIN博客团队";
    }

    @Override
    public boolean verifyEmailCode(String email, String code) {
        // 1. 参数验证
        if (email == null || email.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            System.out.println("验证码验证失败: 参数为空 | 邮箱: " + email + " | 验证码: " + code);
            return false;
        }

        // 2. 验证码格式检查（6位数字）
        if (!code.matches("\\d{6}")) {
            System.out.println("验证码验证失败: 格式错误 | 邮箱: " + email + " | 验证码: " + code);
            return false;
        }

        // 3. 从缓存中获取验证码
        String cachedCode = redisEmailCodeService.getCode(email);
        if (cachedCode == null) {
            System.out.println("验证码验证失败: 验证码不存在或已过期 | 邮箱: " + email);
            return false;
        }

        // 4. 验证码比对
        if (cachedCode.equals(code)) {
            // 验证成功后立即删除验证码，防止重复使用
            redisEmailCodeService.removeCode(email);
            // 同时删除发送时间限制
            redisEmailCodeService.removeCode("email_code_last_send:" + email);
            System.out.println("验证码验证成功 | 邮箱: " + email);
            return true;
        } else {
            System.out.println("验证码验证失败: 验证码错误 | 邮箱: " + email + " | 输入: " + code + " | 期望: " + cachedCode);
            return false;
        }
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

    @Override
    public boolean changePassword(Integer userId, String currentPassword, String newPassword) {
        // 1. 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 2. 验证当前密码
        if (!user.getPassword().equals(currentPassword)) {
            System.out.println("密码修改失败: 当前密码错误 | 用户ID: " + userId);
            return false;
        }

        // 3. 更新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(newPassword);

        int result = userMapper.update(updateUser);
        if (result > 0) {
            System.out.println("密码修改成功 | 用户ID: " + userId);
            return true;
        } else {
            System.out.println("密码修改失败: 数据库更新失败 | 用户ID: " + userId);
            return false;
        }
    }
}
