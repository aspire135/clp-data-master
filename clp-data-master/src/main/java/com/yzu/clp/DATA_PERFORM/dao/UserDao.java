package com.yzu.clp.DATA_PERFORM.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzu.clp.DATA_PERFORM.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
