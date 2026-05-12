package com.github.ylyan2015.controller;


import com.github.ylyan2015.dao.UserRepository;
import com.github.ylyan2015.entity.UserEO;
import com.github.ylyan2015.util.RedisUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/get/{id}")
    public Object getUser(@PathVariable Long id) {
        // 1.先查Redis
        String key = "user:" + id;
        if (redisUtil.hasKey(key)) {
            return redisUtil.get(key);
        }
        // 2.再查数据库JPA
        Optional<UserEO> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            UserEO user = optional.get();
            // 3.放入Redis 过期300秒
            redisUtil.set(key, user, 300);
            return user;
        }
        return "用户不存在";
    }
}