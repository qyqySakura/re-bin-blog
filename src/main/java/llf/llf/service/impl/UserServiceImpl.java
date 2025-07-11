package llf.llf.service.impl;


import llf.llf.common.EmailCodeCache;
import llf.llf.mapper.UserMapper;
import llf.llf.pojo.User;
import llf.llf.service.UserService;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JavaMailSender mailSender;
    @Override
    public String generateAndSendEmailCode(String email) {
        // 1. 生成6位验证码
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        // 2. 保存到缓存（如Redis，或session，或Map，生产建议用Redis）
        // 这里简单用静态Map演示
        EmailCodeCache.save(email, code);
    
        // 3. 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("3035526940@qq.com");
        message.setTo(email);
        message.setSubject("您的注册验证码");
        message.setText("mq：" +
                "您的验证码是：" + code + "，有效期5分钟。");
        mailSender.send(message);
    
        return code;
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
