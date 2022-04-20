package com.uol.bloodmanagementsystem;

public class SignUp_UserInfo {

    public String user_name, user_email, user_password, user_phone;

    public SignUp_UserInfo()
    {

    }

    public SignUp_UserInfo(String user_name, String user_email, String user_password, String user_phone)
    {
        this.user_name=user_name;
        this.user_email=user_email;
        this.user_password=user_password;
        this.user_phone=user_phone;
    }
}
