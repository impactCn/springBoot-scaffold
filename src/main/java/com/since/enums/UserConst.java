package com.since.enums;

/**
 * @description: 统一定义用户行为
 * @author: SY_zheng
 * @create: 2019-05-05
 */
public enum UserConst {

    /**
     * 定义用户行为
     * ON_LINE 是否在线
     */
    ON_LINE("line", "on"),
    OFF_LINE("line", "off")
    ;

    private final String key;
    private final String value;

    UserConst(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }
}