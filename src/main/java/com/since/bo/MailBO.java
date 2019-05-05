package com.since.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description: 邮箱业务对象
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Setter
@Getter
@ToString
public class MailBO {

    private String from;

    private String[] to;

    private String subject;

    private String context;

    public MailBO(String from, String[] to, String subject, String context) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.context = context;
    }

    public MailBO() {
    }
}