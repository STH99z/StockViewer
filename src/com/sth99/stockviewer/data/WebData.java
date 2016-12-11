package com.sth99.stockviewer.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by STH99 on 2016/12/7.
 * 用于对网络资源索取并整合到程序
 */
public class WebData {
    URL url;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    StringBuffer stringBuffer;
    String charsetName = "gb2312";

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
        if (stringBuffer == null) {
            inputStreamReader = new InputStreamReader(url.openStream(), charsetName);
            bufferedReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer(1024);
            while (true) {
                int read = bufferedReader.read();
                if (read == -1)
                    break;
                stringBuffer.append((char) read);
            }
            bufferedReader.close();
        }
        return stringBuffer.toString();
    }

    public Matcher filterData(String regex) throws IOException {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(getData());

    }
}
