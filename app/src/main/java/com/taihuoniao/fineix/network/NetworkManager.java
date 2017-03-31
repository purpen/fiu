package com.taihuoniao.fineix.network;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/5.
 */
public class NetworkManager {
    /**
     * 网络请求标签集合
     */
    private static List<String> tagList;
    private static NetworkManager ourInstance;

    public static NetworkManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new NetworkManager();
        }
        return ourInstance;
    }

    private NetworkManager() {
//        networkList = new ArrayList<>();
        tagList = new ArrayList<>();
    }

    /**
     * 取消正在运行的任务,并移除
     *
     * @param name 任务名
     */
    public void cancel(String name) {
        if (tagList == null) return;
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).equals(name)) {
                tagList.remove(i);
            }
        }
    }

    public List<String> getTagList() {
        return tagList;
    }
}
