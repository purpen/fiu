package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/7/8.
 */
public class SerSceneListBean implements Serializable {
    private List<SceneList.DataBean.RowsBean> sceneList;

    public List<SceneList.DataBean.RowsBean> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<SceneList.DataBean.RowsBean> sceneList) {
        this.sceneList = sceneList;
    }
}
