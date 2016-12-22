package com.sth99.stockviewer.user;

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

    public void readFromFile(String file) {

    }

    public void saveToFile(String file) {

    }
}
