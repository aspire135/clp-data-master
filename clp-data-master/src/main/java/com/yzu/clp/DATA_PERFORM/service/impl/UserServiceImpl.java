package com.yzu.clp.DATA_PERFORM.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzu.clp.DATA_PERFORM.common.R;
import com.yzu.clp.DATA_PERFORM.dao.UserDao;
import com.yzu.clp.DATA_PERFORM.entity.User;
import com.yzu.clp.DATA_PERFORM.service.UserService;
import com.yzu.clp.Util.Result;
import com.yzu.clp.Util.md5Util;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User>  implements UserService {

    @Override
    public Boolean login(String userName, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        User user = this.getOne(queryWrapper);     //根据用户名获取用户对象
        if (user == null) {
            return false;
        } else {
            // 判断密码的正确性
            if (user.getPassword().equals(md5Util.getHMAC(password, user.getSalt()))) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public Boolean register(User user) {
        user.setSalt(md5Util.getRandomSalt());
        user.setPassword(md5Util.getHMAC(user.getPassword(), user.getSalt()));
        return this.save(user);
    }
}
