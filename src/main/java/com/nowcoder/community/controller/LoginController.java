package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * ClassName: LoginController
 * Package: com.nowcoder.community.controller
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /5/27 20:15
 * @Version 1.0
 */
@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

    /**
     * Gets register page.
     *
     * @return the register page
     */
    @GetMapping("/register")
    public String getRegisterPage() {
        return "site/register";
    }

    /**
     * Gets login page.
     *
     * @return the login page
     */
    @GetMapping("/login")
    public String getLoginPage() {
        return "site/login";
    }

    /**
     * 注册业务功能.
     *
     * @param model the model
     * @param user  the user
     * @return the string
     */
    @PostMapping("/register")
    public String register(Model model, @Validated User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一份激活邮件，请您尽快激活~");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "site/register";
        }
    }

    /**
     * 邮件激活账号业务
     *
     * @param model
     * @param userId
     * @param code
     * @return
     */
    @GetMapping("/activation/{userId}/{code}")
    private String activation(Model model, @PathVariable int userId, @PathVariable String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，您的账号已经可以正常使用了~");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效的操作，该账号已经激活成功~");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，您提供的激活码不正确！");
            model.addAttribute("target", "/index");
        }
        return "site/operate-result";
    }

}
