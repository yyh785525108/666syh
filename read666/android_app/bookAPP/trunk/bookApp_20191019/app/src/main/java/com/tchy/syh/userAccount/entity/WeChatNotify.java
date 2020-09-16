package com.tchy.syh.userAccount.entity;

public class WeChatNotify{
    public boolean isSuccess;
    public String msg;
    public WeChatNotify(boolean isSuccess,String msg){
        this.isSuccess=isSuccess;
        this.msg=msg;
    }
}