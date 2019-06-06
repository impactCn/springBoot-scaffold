package com.since.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long accountId;

    private String account;

    private String password;

    private Byte authority;

    private String email;

    private String question;

    private String answer;

    private String subsidiaryAccount;

    private Date registerTime;




}