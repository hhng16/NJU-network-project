package com.th.marketgod.controller;

import lombok.Getter;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Result {
    @Getter//一定要符合json结构，保证可以被转化为json有setter和getter方法
    private int code;
    @Getter
    private String msg;
    @Getter
    private long total;
    @Getter
    private Object data;


    private static Result result(int code, String msg, long total, Object data){
        Result  res = new Result();
        res.setData(data);
        res.setCode(code);
        res.setMsg(msg);
        res.setTotal(total);
        return res;
    }
    public static Result fail(){
        return result(400,"失败",0L,null);
    }

    public static Result fail(Object data){
        return result(400, "失败", 0L, data);
    }

    public static Result suc(){
        return result(200,"成功",0L,null);
    }


    public static Result suc(Object data){
        return result(200,"成功",0L,data);
    }

    public static Result suc(Object data ,Long total){
        return result(200,"成功",total,data);
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
