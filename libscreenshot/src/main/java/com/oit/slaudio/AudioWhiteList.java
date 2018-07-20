package com.oit.slaudio;

import java.util.ArrayList;
import java.util.List;

public class AudioWhiteList {

    private static AudioWhiteList instance = null;

    private boolean openWhiteList = false;

    public void openWhiteList(boolean flag) {
        openWhiteList = flag;
    }

    public boolean isOpenWhiteList(){
        return openWhiteList;
    }

    //白名单列表
    private static String[] whiteStrs = {
            "/xinyidai/pages/limitFinal.html",
            "/xinyidai/pages/confirm.html",
            "/xinyidai/pages/workInfo.html",
            "/xinyidai/pages/familyInfo.html",
            "/xinyidai/pages/linkInfo1.html"
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

    public boolean isWhiteUrl(String pageUrl) {
        return getInstance().getWhiteList().contains(pageUrl);
    }

    public void addWhiteItem(String whiteItem) {
        getInstance().getWhiteList().add(whiteItem);
    }

    public void addWhiteList(List<String> whiteItem) {
        for (int i = 0; i < whiteItem.size(); i++) {
            getInstance().getWhiteList().add(whiteItem.get(i));
        }
    }

    public void removeWhiteItem(String whiteItem) {
        if (getInstance().getWhiteList().contains(whiteItem)) {
            getInstance().getWhiteList().remove(whiteItem);
        }
    }
}
