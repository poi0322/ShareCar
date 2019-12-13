package com.example.sharecar.DataSet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private String userName;
    private String userBirth;
//    private String age;

    public User() {
    }
    public User(String userName,
            String userBirth
            ){
        this.userName = userName;
        this.userBirth = userBirth;
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        int iage = Integer.parseInt(sdf.format(date)) / 10000 -
//                Integer.parseInt(userBirth) / 10000;
//        this.age = String.valueOf(iage);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

//    public String getAge() {
//        return age;
//    }
//
//    public void setAge(String age) {
//        this.age = age;
//    }
}