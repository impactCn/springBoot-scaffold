package com.since.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.since.enums.MessageEnums;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description: 构建网络传输的类
 * @author: SY_zheng
 * @create: 2019-04-09
 */
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageVO<T> implements Serializable{


    private static final long serialVersionUID = 7366217171681294440L;
    private String code;
    private String msg;
    private T data;

    private MessageVO(Builder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = (T) builder.data;
    }


    /**
     * 使用build模式，较少重复代码
     * @return
     */

    public static MessageVO.Builder builder(){
        return new Builder();
    }

    public static class Builder<T> {
        private String code;
        private String msg;
        private T data;

        public Builder msgCode(MessageEnums messageEnums) {
            this.msg = messageEnums.getDesc();
            this.code = messageEnums.getCode();
            return this;
        }

        public Builder data(T data) {
            this.data = data;
            return this;
        }

        public MessageVO build() {
            return new MessageVO(this);
        }

    }


}