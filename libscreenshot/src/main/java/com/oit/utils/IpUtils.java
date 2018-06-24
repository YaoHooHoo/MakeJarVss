package com.oit.utils;

/**
 * Created by Yao on 2018/2/7.
 */

public class IpUtils {

    public static String cleanSpace(String IP) {//去掉IP字符串前后所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1, IP.length()).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    public static boolean isIp(String IP) {//判断是否是一个IP
        boolean flag = false;
        IP = cleanSpace(IP);
        if (IP.contains(":")) {
            //如果含有端口
            String[] strs = IP.split(":");
            String url = strs[0];
            if (url.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                String s[] = url.split("\\.");
                if (Integer.parseInt(s[0]) < 255)
                    if (Integer.parseInt(s[1]) < 255)
                        if (Integer.parseInt(s[2]) < 255)
                            if (Integer.parseInt(s[3]) < 255)
                                flag = true;
            }
            return flag;
        } else {
            if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                String s[] = IP.split("\\.");
                if (Integer.parseInt(s[0]) < 255)
                    if (Integer.parseInt(s[1]) < 255)
                        if (Integer.parseInt(s[2]) < 255)
                            if (Integer.parseInt(s[3]) < 255)
                                flag = true;
            }
        }
        return flag;
    }
}
