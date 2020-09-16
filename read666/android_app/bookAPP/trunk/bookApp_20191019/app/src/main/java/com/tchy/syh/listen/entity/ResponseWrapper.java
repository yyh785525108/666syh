package com.tchy.syh.listen.entity;



public class ResponseWrapper<T> {
    private int status;
    private String info;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public boolean isSuccess(){
        return 1==status;
    }
}
