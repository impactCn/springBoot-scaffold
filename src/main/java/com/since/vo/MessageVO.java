package com.since.vo;

import lombok.ToString;

import java.io.Serializable;

/**
 * @description: 构建网络传输的类
 * @author: SY_zheng
 * @create: 2019-04-09
 */
@ToString
public class MessageVO<T> implements Serializable {


    private String code;
    private String msg;
    private T data;

    public MessageVO(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public MessageVO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MessageVO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}