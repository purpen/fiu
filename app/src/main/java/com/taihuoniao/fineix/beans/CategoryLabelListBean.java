package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/5.
 */
public class CategoryLabelListBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<CategoryTagItem> tags;

        public List<CategoryTagItem> getTags() {
            return tags;
        }

        public void setTags(List<CategoryTagItem> tags) {
            this.tags = tags;
        }
    }

    public static class CategoryTagItem {
        private String _id;
        private String title_cn;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getTitle_cn() {
            return title_cn;
        }

        public void setTitle_cn(String title_cn) {
            this.title_cn = title_cn;
        }
    }
}
