package com.sth99.stockviewer.user;

import com.sth99.stockviewer.data.StockCodeData;
import com.sth99.stockviewer.util.MathUtil;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 单个用户
 * Created by STH99 on 2016/12/21.
 */
public class User {
    private long uid;
    private String userName;
    private String password;

    ArrayList<StockCodeData> selfSelected;

    private User(long uid, String userName, String password) {
        this.uid = uid;
        this.userName = userName;
        this.password = password;
        selfSelected = new ArrayList<>();
    }

    public User() {
        uid = generateUid();
        userName = "";
        password = "";
        selfSelected = new ArrayList<>();
    }

    public User(String userName, String password) {
        this.uid = generateUid();
        this.userName = userName;
        this.password = password;
        selfSelected = new ArrayList<>();
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
        loadSelfList();
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

    public byte[] toSaveBytes() {
        String save = toSaveData();
        byte[] bytes1 = save.getBytes();
        int len = bytes1.length;
        byte[] bytes = new byte[len + 4];
        System.arraycopy(bytes1, 0, bytes, 4, len);
        System.arraycopy(MathUtil.int2Byte(len), 0, bytes, 0, 4);
        return bytes;
    }

    public void saveSelfList() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("userdata\\" + getUserName() + ".bin"));
        for (StockCodeData stockCodeData : selfSelected) {
            bufferedWriter.write(stockCodeData.getName() + ";" + stockCodeData.getFullCode() + "\n");
        }
        bufferedWriter.close();
    }

    public ArrayList<StockCodeData> getSelfSelected() {
        return selfSelected;
    }

    public void loadSelfList() throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("userdata\\" + getUserName() + ".bin"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] sp = line.split(";");
                selfSelected.add(new StockCodeData(sp[0], sp[1]));
            }
            bufferedReader.close();
            System.out.println("User " + getUserName() + " has " + selfSelected.size() + " selected stock.");
        } catch (FileNotFoundException e) {
            System.out.println("User " + getUserName() + " has no selected stock.");
        }

    }
}
