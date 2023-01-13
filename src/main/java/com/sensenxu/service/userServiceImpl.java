package com.sensenxu.service;

import com.sensenxu.dao.loginTicketMapper;
import com.sensenxu.dao.userMapper;
import com.sensenxu.entity.User;
import com.sensenxu.entity.loginTicket;
import com.sensenxu.util.communityConstant;
import com.sensenxu.util.communityUtil;
import com.sensenxu.util.mailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service

public class userServiceImpl implements userService , communityConstant {
    @Autowired
    private userMapper userMapper;
    @Autowired
    private loginTicketMapper loginTicketMapper;

    @Autowired
    private mailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Override
    public User findUserById(int userId) {
        return  userMapper.selectById(userId);
    }

    @Override
    public Map<String, Object> register(User user) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if(user == null){
            throw new IllegalAccessException("参数不能为空");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        //验证账号
        User u = userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMsg","该账号已存在");
            return map;
         }
        u = userMapper.selectByEmail(user.getEmail());
        if(u!=null){
            map.put("emailMsg","该邮箱已存在");
            return map;
        }

        //走到这一步了 证明username,password,email不为空，并且账号和邮箱未被注册
        //可以注册了
        //配置默认值
        user.setSalt(communityUtil.generateUUID().substring(0,5));
        user.setPassword(communityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(communityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation"+ "/" +user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);




        return null;
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //验证账号
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg","该账号不存在");
            return map;
        }
        if(user.getStatus() == 0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }
        //验证密码
        String md5 = communityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(md5)){
            map.put("passwordMsg","密码不正确");
            return map;
        }
        //生成登陆凭证
        loginTicket ticket = new loginTicket();
        ticket.setUserId(user.getId());
        ticket.setTicket(communityUtil.generateUUID());
        ticket.setStatus(0);
        ticket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds* 1000));
        loginTicketMapper.insertLoginTicket(ticket);

        map.put("ticket",ticket.getTicket()); //给凭证就行
        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket,1);
    }

    @Override
    public loginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    @Override
    public int updateHeader(int userId, String headUrl) {
        return userMapper.updateHeader(userId, headUrl);
    }

    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }
    public int updatePassword(int userId, String password){
        return userMapper.updatePassword(userId,password);
    }

    @Override
    public User findUserByName(String toName) {
        return userMapper.selectByName(toName);
    }

}
