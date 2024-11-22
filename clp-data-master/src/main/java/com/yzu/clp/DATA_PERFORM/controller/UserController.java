package com.yzu.clp.DATA_PERFORM.controller;


import com.yzu.clp.DATA_PERFORM.common.R;
import com.yzu.clp.DATA_PERFORM.entity.User;
import com.yzu.clp.DATA_PERFORM.service.UserService;
import com.yzu.clp.DATA_PERFORM.service.impl.UserServiceImpl;
import com.yzu.clp.Util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "登陆注册模块")
public class UserController {

    @Resource
    private UserService userService;

    @CrossOrigin
    @PostMapping("/login")
    @ApiOperation("登陆")
    public R login(@RequestParam String userName, @RequestParam String password) {
        Boolean res = userService.login(userName, password);
        if (res ){
            return R.ok();
        }else{
            return R.error();
        }
    }


    @CrossOrigin
    @PostMapping("/register")
    @ApiOperation("注册")
    public R register(@RequestBody User user) {
        Boolean res = userService.register(user);
        if (res ){
            return R.ok();
        }else{
            return R.error();
        }
    }


}
