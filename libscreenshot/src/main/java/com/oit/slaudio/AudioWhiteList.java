package com.oit.slaudio;

import java.util.ArrayList;
import java.util.List;

public class AudioWhiteList {

    private static AudioWhiteList instance = null;

    private static String[] whiteStrs = {
            "1",
            "2",
            "3",
            "4",
            "5"
    };

    private static List<String> whiteList = new ArrayList<>();

    private AudioWhiteList() {
        whiteList.clear();
        for (int i = 0; i < whiteStrs.length; i++) {
            whiteList.add(whiteStrs[i]);
        }
    }

    public static AudioWhiteList getInstance() {
        if (instance == null) {
            synchronized (AudioWhiteList.class) {
                if (instance == null) {
                    instance = new AudioWhiteList();
                }
            }
        }
        return instance;
    }

    private List<String> getWhiteList() {
        return whiteList;
    }

    public boolean isWhiteUrl(String pageUrl){
        for (int i = 0; i < whiteList.size(); i++) {
            if (pageUrl.equals(whiteList.get(i))){
                return true;
            }
        }
        return false;
    }

    public void addWhiteItem(String whiteItem) {
        getInstance().getWhiteList().add(whiteItem);
    }

    public void removeWhiteItem(String whiteItem){
        if (getInstance().getWhiteList().contains(whiteItem)){
            getInstance().getWhiteList().remove(whiteItem);
        }
    }
}
