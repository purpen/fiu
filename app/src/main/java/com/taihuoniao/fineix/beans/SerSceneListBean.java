package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/7/8.
 */
public class SerSceneListBean implements Serializable {
    private List<SceneListBean> sceneList;

    public List<SceneListBean> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<SceneListBean> sceneList) {
        this.sceneList = sceneList;
    }
}
