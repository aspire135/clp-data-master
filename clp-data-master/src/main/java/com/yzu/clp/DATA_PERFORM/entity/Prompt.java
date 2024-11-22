package com.yzu.clp.DATA_PERFORM.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@TableName("prompt")
@Data
public class Prompt implements Serializable {

    // 当你一个类实现了Serializable接口，就会有显式地定义serialVersionUID
    // 序列化时为了保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性。
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type =IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("分类标题")
    private String title;

    @ApiModelProperty("分类内容")
    private String content;

    @ApiModelProperty("等级")
    private Integer level;

}
