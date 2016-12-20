package com.sth99.stockviewer.data;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by STH99 on 2016/12/7.
 * 用于对网络资源索取并整合到程序
 */
public class WebData extends Data {
    URL url;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    StringBuffer stringBuffer;
    String charsetName = "utf-8";
    boolean isGetCalled = false;
    String result = "";

    public WebData(URL url) {
        this.url = url;
    }

    public WebData(String URLaddress) throws MalformedURLException {
        this.url = new URL(URLaddress);
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getData() {
        if (isGetCalled)
            return result;
        try {
            if (stringBuffer == null) {
                inputStreamReader = new InputStreamReader(url.openStream(), charsetName);
                bufferedReader = new BufferedReader(inputStreamReader);
                stringBuffer = new StringBuffer(0x1000);
                while (true) {
                    int read = bufferedReader.read();
                    if (read == -1)
                        break;
                    stringBuffer.append((char) read);
                }
                bufferedReader.close();
                result = stringBuffer.toString();
                if (result.length() == 0) {
                    System.out.println("网络文件长度为0");
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("网络文件未找到");
            stringBuffer = new StringBuffer();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            stringBuffer = new StringBuffer();
        } finally {
            isGetCalled = true;
        }
        return result;
    }

    public Matcher filterData(String regex) throws IOException {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(getData());
    }

    public void saveData(String fileName) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
        bufferedWriter.write(getData());
        bufferedWriter.close();
    }

    @Override
    public String toString() {
        return "WebData{" +
                "url=" + url +
                ", charsetName='" + charsetName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Data o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebData webData = (WebData) o;

        if (url != null ? !url.equals(webData.url) : webData.url != null) return false;
        return charsetName != null ? charsetName.equals(webData.charsetName) : webData.charsetName == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (charsetName != null ? charsetName.hashCode() : 0);
        return result;
    }
}
