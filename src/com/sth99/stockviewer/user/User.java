package com.sth99.stockviewer.user;

import com.sth99.stockviewer.util.MathUtil;

import java.util.regex.Pattern;

/**
 * 单个用户
 * Created by STH99 on 2016/12/21.
 */
public class User {
    private long uid;
    private String userName;
    private String password;

    private User(long uid, String userName, String password) {
        this.uid = uid;
        this.userName = userName;
        this.password = password;
    }

    public User() {
        uid = generateUid();
        userName = "";
        password = "";
    }

    public User(String userName, String password) {
        this.uid = generateUid();
        this.userName = userName;
        this.password = password;
    }

    public User(String encodedSaveData) throws Exception {
        this();
        String message = MathUtil.base64Decode(encodedSaveData);
        String[] split = message.split(";");
        if (split.length != 3)
            throw new Exception("Read encodedSaveData Failed! data=" + encodedSaveData);
        uid = Long.valueOf(split[0]);
        userName = split[1];
        password = split[2];
    }

    private long generateUid() {
        return System.currentTimeMillis();
    }

    public synchronized boolean equals(User user) {
        return uid == user.uid;
    }

    public synchronized boolean nameEquals(User user) {
        return userName.equals(user.userName);
    }

    public synchronized boolean changePassword(String original, String newPassword) {
        if (!original.equals(password))
            return false;
        password = newPassword;
        return true;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public synchronized void setUserName(String userName) {
        this.userName = userName;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
    }

    public synchronized long getUid() {

        return uid;
    }

    public synchronized String getUserName() {
        return userName;
    }

    public synchronized String getPassword() {
        return password;
    }

    public String toSaveData() {
        return MathUtil.base64Encode(uid + ";" + userName + ";" + password);
    }
}
