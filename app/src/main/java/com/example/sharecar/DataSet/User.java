package com.example.sharecar.DataSet;

public class User {
    private String uid;
    private String carfull;
    private String userName;
    private String userBirth;
    private double Latitude;
    private double Longitude;
//    private String age;

    public User() {
    }
    public User(String uid, String carfull, String userName,
                String userBirth,
                double latitude, double longitude){
        this.uid = uid;
        this.carfull = carfull;
        this.userName = userName;
        this.userBirth = userBirth;
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        int iage = Integer.parseInt(sdf.format(date)) / 10000 -
//                Integer.parseInt(userBirth) / 10000;
//        this.age = String.valueOf(iage);
        Latitude = latitude;
        Longitude = longitude;
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

    public String getCarfull() {
        return carfull;
    }

    public void setCarfull(String carfull) {
        this.carfull = carfull;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

//    public String getAge() {
//        return age;
//    }
//
//    public void setAge(String age) {
//        this.age = age;
//    }
}