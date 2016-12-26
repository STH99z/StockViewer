package com.sth99.stockviewer.data;

import com.sth99.stockviewer.util.MathUtil;

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

    public String getData() throws IOException {
        if (isGetCalled)
            return result;
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
            isGetCalled = true;
//            if (result.length() == 0) {
//                System.out.println("网络文件长度为0");
//            }
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

    public void loadData(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        stringBuffer = new StringBuffer(0x1000);
        String line;
        while ((line = bufferedReader.readLine()) != null)
            stringBuffer.append(line);
        result = stringBuffer.toString();
        isGetCalled = true;
    }

    private String getDefaultFilePath() {
        String b = MathUtil.base64Encode(url.getFile());
        return "data\\" + b.replaceAll("\\\\", "").replaceAll("=", "") + ".bin";
    }

    public void deleteFile() {
        File file = new File(getDefaultFilePath());
        if (file.exists())
            file.delete();
    }

    public boolean fileExist() {
        File file = new File(getDefaultFilePath());
        return file.exists();
    }

    public void saveData() throws IOException {
        saveData(getDefaultFilePath());
    }

    public void loadData() throws IOException {
        loadData(getDefaultFilePath());
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
