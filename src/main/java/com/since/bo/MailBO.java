package com.since.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @description: 邮箱业务对象
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Getter
@ToString
@Builder
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






}