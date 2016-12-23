package com.sth99.stockviewer.user;

import com.sth99.stockviewer.util.MathUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 用户存储类
 * Created by STH99 on 2016/12/21.
 */
public class UserStorager {
    public static UserStorager get() {
        return singleInstance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private User currentUser = new User("临时用户", "");

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    private static UserStorager singleInstance = new UserStorager();

    private Hashtable<Long, User> table;

    public UserStorager() {
        table = new Hashtable<>();
    }

    public User register(String name, String pass) {
        User user = new User(name, pass);
        return table.put(user.getUid(), user);
    }

    public boolean nameExist(String name) {
        for (User user : table.values()) {
            if (name.equals(user.getUserName()))
                return true;
        }
        return false;
    }

    public User delete(User user) {
        return table.remove(user.getUid());
    }

    public boolean exist(User user) {
        if (table.get(user.getUid()) == null)
            return false;
        return true;
    }

    public User get(long uid) {
        return table.get(uid);
    }

    public User get(String userName) {
        for (User user : table.values()) {
            if (userName.equals(user.getUserName()))
                return user;
        }
        return null;
    }

    public void readFromFile(String file) throws Exception {
        try {
            table.clear();
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] countBytes = new byte[4];
            fileInputStream.read(countBytes);
            int count = MathUtil.byte2Int(countBytes);
            for (int i = 0; i < count; i++) {
                byte[] sizeBytes = new byte[4];
                fileInputStream.read(sizeBytes);
                int size = MathUtil.byte2Int(sizeBytes);
                byte[] userBytes = new byte[size];
                fileInputStream.read(userBytes);
                String userData = new String(userBytes);
                User user = new User(userData);
                table.put(user.getUid(), user);
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(MathUtil.int2Byte(table.values().size()));
        for (User user : table.values()) {
            fileOutputStream.write(user.toSaveBytes());
        }
        fileOutputStream.close();
    }
}
