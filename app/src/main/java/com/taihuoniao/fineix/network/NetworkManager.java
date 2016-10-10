package com.taihuoniao.fineix.network;

import com.lidroid.xutils.http.HttpHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/5.
 */
public class NetworkManager {
    /**
     * 网络请求栈
     */
    private static List<HttpHandler<String>> networkList;
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
        networkList = new ArrayList<>();
        tagList = new ArrayList<>();
    }

    /**
     * 强行结束所有在运行的网络请求
     */
    public void cancelAll() {
        int size = networkList.size();
        for (int i = size - 1; i >= 0; i--) {
            HttpHandler<String> handler = networkList.get(i);
            if (handler != null)
                handler.cancel();
            networkList.remove(i);
            tagList.remove(i);
        }
    }


    /**
     * 取消正在运行的任务,并移除
     *
     * @param name 任务名
     */
    public void cancel(String name) {
        if (tagList==null) return;
        if (networkList==null) return;
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).equals(name)) {
                networkList.get(i).cancel();
                networkList.remove(i);
                tagList.remove(i);
            }
        }
    }

    /**
     * 添加任务
     *
     * @param name        任务名
     * @param httpHandler 任务
     */
    public void add(String name, HttpHandler<String> httpHandler) {
        if (tagList==null) return;
        if (networkList==null) return;
        tagList.add(name);
        networkList.add(httpHandler);
    }

    public List<HttpHandler<String>> getNetworkList() {
        return networkList;
    }

    public List<String> getTagList() {
        return tagList;
    }
}
