package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * ClassName: LoginRequiredInterceptor
 * Package: com.nowcoder.community.controller.interceptor
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/29 17:14
 * @Version 1.0
 */
//@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    //@Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && hostHolder.getUser() == null) {

                String xRequestedWith = request.getHeader("x-requested-with");
                if ("XMLHttpRequest".equals(xRequestedWith)) {
                    response.setContentType("application/plain;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(CommunityUtil.getJSONString(1, "请您先登陆呢~"));
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                }

                return false;
            }
        }
        return true;
    }
}
