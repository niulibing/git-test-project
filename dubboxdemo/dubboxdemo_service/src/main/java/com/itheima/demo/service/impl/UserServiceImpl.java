package com.itheima.demo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.demo.service.UserService;
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getName() {
        return "hello,dubbox";
    }
}
