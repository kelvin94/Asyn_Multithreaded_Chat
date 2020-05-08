package com.jyl.portfolio.socketprogramming;


import java.io.Serializable;

public class Message implements Serializable {
    private String msgType;
    private String clientName;
    private String messageBody;
    private String date;

    public Message(String msgType, String clientName, String messageBody, String date) {
        this.msgType = msgType;
        this.clientName = clientName;
        this.messageBody = messageBody;
        this.date = date;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgType='" + msgType + '\'' +
                ", clientName='" + clientName + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
