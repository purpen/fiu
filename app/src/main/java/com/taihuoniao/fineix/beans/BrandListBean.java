package com.taihuoniao.fineix.beans;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/28.
 */
public class BrandListBean extends NetBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int current_user_id;
        /**
         * _id : 5784568dfc8b12b21c8bb16c
         * title : 威视达康
         * des : 深圳市威视达康科技有限公司专注于数字视频处理、网络视频通讯、音视频编解码、图象处理及嵌入式操作系统等领域的综合应用。为多个营运商的“平安e家”等增值服务提供系统平台和网络监控产品。目前，用户已涵盖电信、银行、公共安全、军队、企业、等多个领域。产品销售不仅覆盖国内、欧美等成熟市场、也成功进入印度、非洲等新兴国际市场。
         * kind : 1
         * self_run : 1
         * mark : w
         * used_count : 0
         * stick : 0
         * status : 1
         * created_on : 1468290701
         * updated_on : 1468290701
         * kind_label : Fiu
         * cover_url : http://frbird.qiniudn.com/scene_brands/160712/57845671fc8b12be1c8bb10a-1-hu.jpg
         * banner_url : http://frbird.qiniudn.com/scene_brands/160712/57845671fc8b12be1c8bb10b-2-p750x422.jpg
         */

        private List<RowsBean> rows;

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean implements Comparable<RowsBean> {
            private String _id;
            private String title;
            private String des;
            private int kind;
            private int self_run;
            private String mark;
            private int stick;
            private int status;
            private String kind_label;
            private String cover_url;
            private String banner_url;

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

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public int getKind() {
                return kind;
            }

            public void setKind(int kind) {
                this.kind = kind;
            }

            public int getSelf_run() {
                return self_run;
            }

            public void setSelf_run(int self_run) {
                this.self_run = self_run;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public int getStick() {
                return stick;
            }

            public void setStick(int stick) {
                this.stick = stick;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getKind_label() {
                return kind_label;
            }

            public void setKind_label(String kind_label) {
                this.kind_label = kind_label;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getBanner_url() {
                return banner_url;
            }

            public void setBanner_url(String banner_url) {
                this.banner_url = banner_url;
            }

            @Override
            public String toString() {
                return getMark().toUpperCase();
            }

            @Override
            public int compareTo(@NonNull RowsBean another) {
                return this.mark.compareTo(another.getMark());
            }
        }
    }
}
