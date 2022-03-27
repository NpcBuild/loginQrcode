package com.qrcodelogin.service;

//kafka消息业务发送接口
public interface IMessageProducer {
    public void send(String msg);
}
