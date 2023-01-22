package com.sensenxu.controller;

import com.sensenxu.annotation.loginRequired;
import com.sensenxu.entity.User;
import com.sensenxu.service.followService;
import com.sensenxu.service.likeService;
import com.sensenxu.service.userService;
import com.sensenxu.util.communityConstant;
import com.sensenxu.util.communityUtil;
import com.sensenxu.util.hostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class userController implements communityConstant {
    private static final Logger logger =  LoggerFactory.getLogger(userController.class);

    @Autowired
    private userService userService;

    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private hostHolder hostHolder;
    @Autowired
    private likeService likeService;
    @Autowired
    private followService followService;

    @loginRequired
    @RequestMapping(value = "/setting" , method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @loginRequired
    @RequestMapping(value = "/upload" , method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headImage, Model model){
        if(headImage == null){
            model.addAttribute("error", "未选择图片");
            return "/site/setting";
        }
        String filename = headImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));//最后一个点 往后截取，获得类型
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error", "文件的格式不正确");
            return "/site/setting";
        }
        //生成一个随机文件名
        String fileName = communityUtil.generateUUID() + suffix;
        //确定文件存放的路径 uploadPath:/Users/xutianyi/Documents/后端开发/uploadphoto
        File file = new File(uploadPath + "/" + fileName);
        try {
            headImage.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件上传失败"+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常"+ e);
        }
        //成功 更新当前用户的头像的路径(web路径)
        //http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser(); //拦截器中已存在
        String headerUrl = domain+contextPath+"/user/header/"+fileName;
        userService.updateHeader(user.getId(),headerUrl);


        return "redirect:/index"; //去首页
    }
    @RequestMapping(value = "/header/{fileName}" , method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务器存放的路径 upload+"/"+fileName
        fileName = uploadPath + "/" + fileName; //从本地拿的 /Users/xutianyi/Documents/后端开发/uploadphoto
        String suffix = fileName.substring(fileName.lastIndexOf("."));//最后一个点 往后截取，获得类型
        //响应图片 固定的
        response.setContentType("image/"+suffix);

        try (
                //会自动关闭
                FileInputStream inputStream = new FileInputStream(fileName  );
                OutputStream outputStream = response.getOutputStream();
                ){


            byte[] buffer = new byte[1024];
            int b = 0;
            while((b=inputStream.read(buffer))!= -1){
                outputStream.write(buffer, 0 ,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败"+e.getMessage());
            e.printStackTrace();
        }


    }

    @RequestMapping(value = "/modifyPassword",method = RequestMethod.POST)
    public String modifyPassword(String password, String newPassword,String newPasswordRepeat,Model model){
        User user = hostHolder.getUser(); //拦截器中已存在
        String md5 = communityUtil.md5(password + user.getSalt());

        if(!user.getPassword() .equals(md5) ){//如果密码不对 返回错误信息
            model.addAttribute("passwordError", "密码错误");
            return "/site/setting";
        }
        if(!newPassword .equals(newPasswordRepeat) ){
            model.addAttribute("repeatError", "密码重复错误");
            return "/site/setting";
        }
        //走到这里 成功修改
        //user.setSalt(communityUtil.generateUUID().substring(0,5));
        userService.updatePassword(user.getId(),communityUtil.md5(newPassword+user.getSalt()));
        return "redirect:/index"; //去首页
    }
    /**
     * 个人主页
     */
    @RequestMapping(value = "/profile/{userId}" , method = RequestMethod.GET)
    public String getProfilePage(@PathVariable int userId, Model model){
        User user = userService.findUserById(userId);
        if(user == null) throw  new RuntimeException("用户不存在");
        //用户
        model.addAttribute("user", user);
        //点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);
        /**
         * 这个方法里的userId是访问的某个人的主页，不是登陆的用户
         * 登陆的用户用hostholder来取
         */
        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        //是否已关注访问的用户
         boolean hasFollowed = false;
         if(hostHolder.getUser() != null){
             hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER,userId);
         }
         model.addAttribute("hasFollowed",hasFollowed);

        return "/site/profile";
    }



}
