package com.zhen.SSM.pojo;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Message {
    //状态码 100-成功  200-失败
    private String code;
    //信息
    private String mes;
    //存储需要传递的信息
    private Map<String,Object> extend = new HashMap<>();

    private Message() {
    }

    public static Message Success(){
        Message message = new Message();
        message.setCode("100");
        message.setMes("数据处理成功");
        return message;
    }

    public static Message Fail(){
        Message message = new Message();
        message.setCode("200");
        message.setMes("数据处理失败");
        return message;
    }

    //链式方法
    public Message add(String name,Object mes){
        this.getExtend().put(name,mes);
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }
}
