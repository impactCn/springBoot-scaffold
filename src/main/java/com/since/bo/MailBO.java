package com.since.bo;

import lombok.Getter;
import lombok.ToString;

/**
 * @description: 邮箱业务对象
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Getter
@ToString
public class MailBO {

    /**
     * 发送者
     */
    private String from;
    /**
     * 接受对象
     */
    private String[] to;
    /**
     * 主题
     */
    private String subject;
    /**
     * 内容
     */
    private String context;

    public MailBO(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.subject = builder.subject;
        this.context = builder.context;
    }

    public static MailBO.Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String from;
        private String[] to;
        private String subject;
        private String context;


        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String[] to) {
            this.to = to;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder context(String context) {
            this.context = context;
            return this;
        }

        public MailBO build() {
            return new MailBO(this);
        }



    }

}