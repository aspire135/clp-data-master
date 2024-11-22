package com.yzu.clp.Util;

public class Result {
    /*状态码*/
    private int status;
    /*信息*/
    private String msg;
    /*数据*/
    private Object data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void insertSuccess(){
        this.setMsg("新增成功");
        this.setStatus(200);
    }

    public void insertError(){
        this.setMsg("新增失败");
        this.setStatus(400);
    }

    public void deleteSuccess(){
        this.setMsg("删除成功");
        this.setStatus(200);
    }

    public void deleteError(){
        this.setMsg("删除失败");
        this.setStatus(400);
    }

    public void updateSuccess(){
        this.setMsg("更新成功");
        this.setStatus(200);
    }

    public void updateError(){
        this.setMsg("更新失败");
        this.setStatus(400);
    }
}
