package com.since.entity;

import java.util.Date;

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

    public Account(Long accountId, String account, String password, Byte authority, String email, String question, String answer, String subsidiaryAccount, Date registerTime) {
        this.accountId = accountId;
        this.account = account;
        this.password = password;
        this.authority = authority;
        this.email = email;
        this.question = question;
        this.answer = answer;
        this.subsidiaryAccount = subsidiaryAccount;
        this.registerTime = registerTime;
    }

    public Account() {
        super();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Byte getAuthority() {
        return authority;
    }

    public void setAuthority(Byte authority) {
        this.authority = authority;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public String getSubsidiaryAccount() {
        return subsidiaryAccount;
    }

    public void setSubsidiaryAccount(String subsidiaryAccount) {
        this.subsidiaryAccount = subsidiaryAccount == null ? null : subsidiaryAccount.trim();
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}