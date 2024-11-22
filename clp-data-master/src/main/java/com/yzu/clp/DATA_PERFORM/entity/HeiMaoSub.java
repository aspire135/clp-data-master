package com.yzu.clp.DATA_PERFORM.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("heimao_data")
public class HeiMaoSub implements  Serializable  {

    // 当你一个类实现了Serializable接口，就会有显式地定义serialVersionUID
    // 序列化时为了保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性。
    private static final long serialVersionUID = 1L;

    /**
     * 投诉编号
     */
    private String num;
    /**
     * 投诉标题
     */
    private String title;
    private String name;
    private String problem;
    private String need;
    private String yuan;
    private String progress;
    private String d1;
    private String d2;

    private Date time1;
    private Date time2;
    private String url;
    private String problemTags;
    private String industryTags;

}
