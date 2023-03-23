package com.nervous.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nervous.common.R;
import com.nervous.entity.User;
import com.nervous.service.UserService;
import com.nervous.utils.SMSUtils;
import com.nervous.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码的逻辑
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (!StringUtils.isEmpty(phone)) {
            //随机生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            //调用阿里云短信服务
            SMSUtils.sendMessage("验证码", "SMS_245415052", phone, code);
            //存储验证码，session
            session.setAttribute(phone, code);
            Object attribute = session.getAttribute(phone);
            System.out.println(attribute.toString());
            return R.success("手机验证码短信发送成功！");
        }
        return R.error("手机验证码短信发送失败！");
    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取手机号
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        //获取验证码
        Object codeInSession = session.getAttribute(phone);

        //从Session做对比，验证
        if (codeInSession != null && codeInSession.equals(code)) { //如果能够比对成功，说明登录成功；
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);//返回登录成功的user
        }
        return R.error("登录失败，请输入正确的验证码");
    }

}
