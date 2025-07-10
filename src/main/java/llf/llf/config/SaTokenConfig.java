package llf.llf.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    
    // 注册Sa-Token拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 指定需要登录认证的路径
            SaRouter.match("/**")
                    .notMatch("/admins/auth/login")  // 管理员登录接口不需要认证
                    .notMatch("/admins/auth/logout") // 管理员退出登录接口不需要认证
                    .notMatch("/users/auth/login")   // 用户登录接口不需要认证
                    .notMatch("/users/auth/logout")  // 用户退出登录接口不需要认证
                    .notMatch("/auth/info")          // 获取用户信息接口不需要认证
                    .notMatch("/auth/check")         // 检查登录状态接口不需要认证
                    .notMatch("/error")              // 错误页面不需要认证
                    .notMatch("/**", "OPTIONS") // 关键：放行所有OPTIONS请求
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
} 