package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/21.
 */
public class CommentsBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<CommentItem> rows;

        public List<CommentItem> getRows() {
            return rows;
        }

        public void setRows(List<CommentItem> rows) {
            this.rows = rows;
        }
    }

    public static class CommentItem {
        private String content;
        private String created_at;
        private User user;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    public static class User {
        private String nickname;
        private String small_avatar_url;

        public String getSmall_avatar_url() {
            return small_avatar_url;
        }

        public void setSmall_avatar_url(String small_avatar_url) {
            this.small_avatar_url = small_avatar_url;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
