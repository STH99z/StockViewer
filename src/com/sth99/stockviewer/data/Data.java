package com.sth99.stockviewer.data;

import java.io.*;
import java.util.function.ObjIntConsumer;

/**
 * 最基础的数据类，想到什么通用的功能在往上加
 * Created by STH99 on 2016/12/8.
 */
public abstract class Data implements Serializable {
    public abstract boolean equals(Data data);

    public abstract String toString();

    public byte[] toByteData() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(0x100);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public void saveData(String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public Data readByteData(byte[] byteData) throws IOException, ClassNotFoundException, ClassCastException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return (Data) object;
    }

    public Data readData(String fileName) throws IOException, ClassNotFoundException, ClassCastException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return (Data) object;
    }
}
