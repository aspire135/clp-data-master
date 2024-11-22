package com.yzu.clp.DATA_PERFORM.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzu.clp.DATA_PERFORM.common.R;
import com.yzu.clp.DATA_PERFORM.entity.User;
import com.yzu.clp.Util.Result;

public interface UserService extends IService<User> {
    Boolean login(String userName, String password);

    Boolean register(User user);
}
