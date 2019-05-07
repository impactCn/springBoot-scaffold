package com.since.utils.impl;

import com.since.bo.MailBO;
import com.since.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @description: 实现代码
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Component
public class MailUtilsImpl implements MailUtils {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Integer simpleMail(MailBO mailBO) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailBO.getFrom());
        mailMessage.setTo(mailBO.getTo());
        mailMessage.setSubject(mailBO.getSubject());
        mailMessage.setText(mailBO.getContext());
        mailSender.send(mailMessage);
        return 0;
    }
}