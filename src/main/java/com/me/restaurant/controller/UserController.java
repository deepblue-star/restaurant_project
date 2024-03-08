package com.me.restaurant.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.me.restaurant.common.R;
import com.me.restaurant.entity.User;
import com.me.restaurant.service.UserService;
import com.me.restaurant.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("验证码是：{}", code);

//            session.setAttribute(phone, code);

            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

//        Object codeInSession = session.getAttribute(phone);
        Object codeInRedis = redisTemplate.opsForValue().get(phone);

        if (codeInRedis != null && codeInRedis.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);

            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            redisTemplate.delete(phone);

            return R.success(user);
        }

        return R.error("登录失败");
    }
}
