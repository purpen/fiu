package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/9/21.
 */

public class NoticeBean {

    private String current_user_id;
    /**
     * _id : 57e3b42d3ffca249198b464f
     * user_id : 924789
     * s_user_id : 719877
     * evt : 15
     * kind : 3
     * related_id : 57e3b42d3ffca249198b464e
     * parent_related_id : 424
     * from_to : 2
     * readed : 0
     * content : null
     * created_on : 1474540589
     * updated_on : 1474540589
     * info : 回复了你的
     * comment_type_str : 情境
     * kind_str : 评论
     * send_user : {"_id":719877,"nickname":"Fynn","avatar_url":"http://frbird.qiniudn.com/avatar/160420/5716ff563ffca2e3108bcf89-avm.jpg"}
     * revice_user : {"_id":924789,"nickname":"铁锤","avatar_url":"http://frbird.qiniudn.com/avatar/160511/5732f0393ffca28a0c8b51af-avm.jpg"}
     * target_obj : {"_id":"57e3b42d3ffca249198b464e","content":"Hahahah"}
     * comment_target_obj : {"_id":424,"content":"","cover_url":"http://frbird.qiniudn.com/scene_sight/160915/57da09a23ffca200068b47cb-s.jpg"}
     * created_at : 14秒前
     */

    private List<RowsBean> rows;

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private String _id;
        private String user_id;
        private String s_user_id;
        private int evt;
        private int kind;
        private String related_id;
        private String parent_related_id;
        private String from_to;
        private int readed=1;
        private String content;
        private String created_on;
        private String updated_on;
        private String info;
        private String comment_type_str;
        private String kind_str;
        /**
         * _id : 719877
         * nickname : Fynn
         * avatar_url : http://frbird.qiniudn.com/avatar/160420/5716ff563ffca2e3108bcf89-avm.jpg
         */

        private SendUserBean send_user;
        /**
         * _id : 924789
         * nickname : 铁锤
         * avatar_url : http://frbird.qiniudn.com/avatar/160511/5732f0393ffca28a0c8b51af-avm.jpg
         */

        private ReviceUserBean revice_user;
        /**
         * _id : 57e3b42d3ffca249198b464e
         * content : Hahahah
         */

        private TargetObjBean target_obj;
        /**
         * _id : 424
         * content :
         * cover_url : http://frbird.qiniudn.com/scene_sight/160915/57da09a23ffca200068b47cb-s.jpg
         */

        private CommentTargetObjBean comment_target_obj;
        private String created_at;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getS_user_id() {
            return s_user_id;
        }

        public void setS_user_id(String s_user_id) {
            this.s_user_id = s_user_id;
        }

        public int getEvt() {
            return evt;
        }

        public void setEvt(int evt) {
            this.evt = evt;
        }

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        public String getRelated_id() {
            return related_id;
        }

        public void setRelated_id(String related_id) {
            this.related_id = related_id;
        }

        public String getParent_related_id() {
            return parent_related_id;
        }

        public void setParent_related_id(String parent_related_id) {
            this.parent_related_id = parent_related_id;
        }

        public String getFrom_to() {
            return from_to;
        }

        public void setFrom_to(String from_to) {
            this.from_to = from_to;
        }

        public int getReaded() {
            return readed;
        }

        public void setReaded(int readed) {
            this.readed = readed;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_on() {
            return created_on;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getComment_type_str() {
            return comment_type_str;
        }

        public void setComment_type_str(String comment_type_str) {
            this.comment_type_str = comment_type_str;
        }

        public String getKind_str() {
            return kind_str;
        }

        public void setKind_str(String kind_str) {
            this.kind_str = kind_str;
        }

        public SendUserBean getSend_user() {
            return send_user;
        }

        public void setSend_user(SendUserBean send_user) {
            this.send_user = send_user;
        }

        public ReviceUserBean getRevice_user() {
            return revice_user;
        }

        public void setRevice_user(ReviceUserBean revice_user) {
            this.revice_user = revice_user;
        }

        public TargetObjBean getTarget_obj() {
            return target_obj;
        }

        public void setTarget_obj(TargetObjBean target_obj) {
            this.target_obj = target_obj;
        }

        public CommentTargetObjBean getComment_target_obj() {
            return comment_target_obj;
        }

        public void setComment_target_obj(CommentTargetObjBean comment_target_obj) {
            this.comment_target_obj = comment_target_obj;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public static class SendUserBean {
            private String _id;
            private String nickname;
            private String avatar_url;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }
        }

        public static class ReviceUserBean {
            private String _id;
            private String nickname;
            private String avatar_url;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }
        }

        public static class TargetObjBean {
            private String _id;
            private String content;
            private String cover_url;

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class CommentTargetObjBean {
            private String _id;
            private String content;
            private String cover_url;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }
        }
    }
}
