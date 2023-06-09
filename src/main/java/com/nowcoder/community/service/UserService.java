package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: UserService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /5/25 22:19
 * @Version 1.0
 */
@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    //@Autowired
    //private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * Find user by id user.
     *
     * @param id the id
     * @return the user
     */
    public User findUserById(int id) {
        //return userMapper.selectById(id);
        // 加入Redis缓存用户
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    /**
     * 注册的业务逻辑
     *
     * @param user the user
     * @return the map 返回一个携带错误信息的map到Controller
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 对User进行判断
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }
        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册！");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // url为:http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }


    /**
     * 邮件激活的业务逻辑.
     *
     * @return the int 返回操作状态码
     */
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 登陆的业务逻辑
     *
     * @param username
     * @param password
     * @param expiredSeconds
     * @return
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在！");
            return map;
        }
        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活！");
            return map;
        }
        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确！");
            return map;
        }

        // 生成登陆凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        //loginTicketMapper.insertLoginTicket(loginTicket);

        // 使用Redis存储凭证
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        // 返回登陆凭证
        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    /**
     * 退出的业务逻辑
     *
     * @param ticket
     */
    public void logout(String ticket) {
        //loginTicketMapper.updateStatus(ticket, 1);

        // 使用Redis更改凭证状态
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    /**
     * 根据凭证查询对应的LoginTicket
     *
     * @param ticket
     * @return
     */
    public LoginTicket findLoginTicket(String ticket) {
        //return loginTicketMapper.selectByTicket(ticket);

        // 使用Redis查询凭证
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 更新用户头像url的业务
     *
     * @param userId
     * @param headerUrl
     * @return
     */
    public int updateHeader(int userId, String headerUrl) {
        //return userMapper.updateHeader(userId, headerUrl);
        // 清理Redis的缓存
        int rows = userMapper.updateHeader(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    /**
     * 修改密码的业务逻辑
     *
     * @param userId
     * @param password
     * @return
     */
    public int updatePassword(int userId, String password) {
        //return userMapper.updatePassword(userId, password);
        // 清理Redis的缓存
        int rows = userMapper.updatePassword(userId, password);
        clearCache(userId);
        return rows;
    }

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    // 1. 优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2. 取不到时就初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 60 * 60, TimeUnit.SECONDS);
        return user;
    }

    // 3. 当数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    /**
     * Gets authorities.
     * 查询某个用户的权限（用于给Spring Security返回用户权限）
     *
     * @param userId the user id
     * @return the authorities
     */
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new LinkedList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()) {
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }

}
