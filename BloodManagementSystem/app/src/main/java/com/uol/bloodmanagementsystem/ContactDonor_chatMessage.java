package com.uol.bloodmanagementsystem;

import java.util.Date;

public class ContactDonor_chatMessage {
    private String messageText;
    private String messageFrom;
    private long messageTime;

    public ContactDonor_chatMessage(String messageText, String messageFrom) {
        this.messageText = messageText;
        this.messageFrom = messageFrom;

        messageTime = new Date().getTime();
    }

    public ContactDonor_chatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }


    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
