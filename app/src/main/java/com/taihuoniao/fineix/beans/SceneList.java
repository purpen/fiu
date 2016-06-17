package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/18.
 */
public class SceneList extends NetBean {
    
    private List<SceneListBean> sceneListBeanList;

    public List<SceneListBean> getSceneListBeanList() {
        return sceneListBeanList;
    }

    public void setSceneListBeanList(List<SceneListBean> sceneListBeanList) {
        this.sceneListBeanList = sceneListBeanList;
    }
}
