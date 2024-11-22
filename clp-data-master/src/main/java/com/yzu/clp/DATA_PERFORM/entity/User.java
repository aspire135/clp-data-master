package com.yzu.clp.DATA_PERFORM.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@TableName("user")
public class User implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;                     //用户ID

    private String userName;            //用户名
    private String password;             //用户密码
    private String salt;                 //盐

}

