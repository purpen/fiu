package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/3.
 * 产品场景关联列表
 */
public class ProductAndSceneListBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private List<SceneItem> rows;

        public List<SceneItem> getRows() {
            return rows;
        }

        public void setRows(List<SceneItem> rows) {
            this.rows = rows;
        }
    }
    public static class SceneItem{
        private String _id;
        private String title;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
