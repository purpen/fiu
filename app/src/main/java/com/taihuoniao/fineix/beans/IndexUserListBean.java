package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * Created by taihuoniao on 2016/8/26.
 */
public class IndexUserListBean {

    /**
     * is_error : false
     * data : {"users":[{"_id":10,"nickname":"太火鸟","true_nickname":"太火鸟","state":2,"first_login":0,"city":"北京","sex":0,"summary":"人生是场大设计","created_on":1400729499,"email":"","birthday":"","medium_avatar_url":"http://frbird.qiniudn.com/avatar/160321/56efa2e33ffca269098c0846-avm.jpg","identify":{"is_scene_subscribe":0,"is_expert":0},"follow_count":2,"fans_count":24,"scene_count":6,"sight_count":1,"counter":{"message_count":0,"fans_count":44,"fiu_comment_count":1,"fiu_alert_count":52,"fiu_notice_count":6,"message_total_count":59,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0,"order_total_count":0},"realname":"太火鸟","phone":"1111","address":"bbbbb","job":"孵化器","zip":"123","weixin":"123","im_qq":"123","label":"","expert_label":"","expert_info":"","interest_scene_cate":[],"age_group":"","assets":"","province_id":0,"district_id":0,"subscription_count":0,"sight_love_count":0,"areas":[],"rank_id":8,"rank_title":"鸟下士","head_pic_url":"","is_love":1,"scene_sight":[]},{"_id":36,"nickname":"大静静张","true_nickname":"大静静张","state":2,"first_login":1,"sex":0,"created_on":1400808060,"birthday":"","medium_avatar_url":"http://frstatic.qiniudn.com/images/deavatar/006-m.jpg","follow_count":0,"fans_count":21,"scene_count":0,"sight_count":0,"counter":{"message_count":0,"fans_count":28,"fiu_notice_count":6,"fiu_comment_count":0,"fiu_alert_count":0,"message_total_count":6,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0,"order_total_count":0},"realname":null,"phone":null,"nation":null,"born_place":null,"card":null,"job":null,"school":null,"address":null,"zip":null,"im_qq":"","height":null,"weight":null,"marital":11,"age":[],"weixin":"","label":"","expert_label":"","expert_info":"","interest_scene_cate":[],"age_group":"","assets":"","summary":"","province_id":0,"district_id":0,"subscription_count":0,"sight_love_count":0,"areas":[],"identify":{"is_scene_subscribe":0,"is_expert":0},"rank_id":3,"rank_title":"鸟平民","head_pic_url":"","is_love":0,"scene_sight":[]}],"current_user_id":20448}
     */

    private boolean is_error;
    /**
     * users : [{"_id":10,"nickname":"太火鸟","true_nickname":"太火鸟","state":2,"first_login":0,"city":"北京","sex":0,"summary":"人生是场大设计","created_on":1400729499,"email":"","birthday":"","medium_avatar_url":"http://frbird.qiniudn.com/avatar/160321/56efa2e33ffca269098c0846-avm.jpg","identify":{"is_scene_subscribe":0,"is_expert":0},"follow_count":2,"fans_count":24,"scene_count":6,"sight_count":1,"counter":{"message_count":0,"fans_count":44,"fiu_comment_count":1,"fiu_alert_count":52,"fiu_notice_count":6,"message_total_count":59,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0,"order_total_count":0},"realname":"太火鸟","phone":"1111","address":"bbbbb","job":"孵化器","zip":"123","weixin":"123","im_qq":"123","label":"","expert_label":"","expert_info":"","interest_scene_cate":[],"age_group":"","assets":"","province_id":0,"district_id":0,"subscription_count":0,"sight_love_count":0,"areas":[],"rank_id":8,"rank_title":"鸟下士","head_pic_url":"","is_love":1,"scene_sight":[]},{"_id":36,"nickname":"大静静张","true_nickname":"大静静张","state":2,"first_login":1,"sex":0,"created_on":1400808060,"birthday":"","medium_avatar_url":"http://frstatic.qiniudn.com/images/deavatar/006-m.jpg","follow_count":0,"fans_count":21,"scene_count":0,"sight_count":0,"counter":{"message_count":0,"fans_count":28,"fiu_notice_count":6,"fiu_comment_count":0,"fiu_alert_count":0,"message_total_count":6,"order_wait_payment":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0,"order_total_count":0},"realname":null,"phone":null,"nation":null,"born_place":null,"card":null,"job":null,"school":null,"address":null,"zip":null,"im_qq":"","height":null,"weight":null,"marital":11,"age":[],"weixin":"","label":"","expert_label":"","expert_info":"","interest_scene_cate":[],"age_group":"","assets":"","summary":"","province_id":0,"district_id":0,"subscription_count":0,"sight_love_count":0,"areas":[],"identify":{"is_scene_subscribe":0,"is_expert":0},"rank_id":3,"rank_title":"鸟平民","head_pic_url":"","is_love":0,"scene_sight":[]}]
     * current_user_id : 20448
     */

    private DataBean data;

    public boolean isIs_error() {
        return is_error;
    }

    public void setIs_error(boolean is_error) {
        this.is_error = is_error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int current_user_id;

        private List<UsersBean> users;

        public int getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(int current_user_id) {
            this.current_user_id = current_user_id;
        }

        public List<UsersBean> getUsers() {
            return users;
        }

        public void setUsers(List<UsersBean> users) {
            this.users = users;
        }

        public static class UsersBean {
            private String _id;
            private String nickname;
            private String true_nickname;
            private String state;
            private String first_login;
            private String city;
            private String sex;
            private String summary;
            private String created_on;
            private String email;
            private String birthday;
            private String medium_avatar_url;
            /**
             * is_scene_subscribe : 0
             * is_expert : 0
             */

            private IdentifyBean identify;
            private String follow_count;
            private String fans_count;
            private String scene_count;
            private String sight_count;
            private String realname;
            private String phone;
            private String address;
            private String job;
            private String zip;
            private String weixin;
            private String im_qq;
            private String label;
            private String expert_label;
            private String expert_info;
            private String age_group;
            private String assets;
            private String head_pic_url;
            private int is_love;
            private int is_follow;

            public int getIs_follow() {
                return is_follow;
            }

            public void setIs_follow(int is_follow) {
                this.is_follow = is_follow;
            }

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

            public String getTrue_nickname() {
                return true_nickname;
            }

            public void setTrue_nickname(String true_nickname) {
                this.true_nickname = true_nickname;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getFirst_login() {
                return first_login;
            }

            public void setFirst_login(String first_login) {
                this.first_login = first_login;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getCreated_on() {
                return created_on;
            }

            public void setCreated_on(String created_on) {
                this.created_on = created_on;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getMedium_avatar_url() {
                return medium_avatar_url;
            }

            public void setMedium_avatar_url(String medium_avatar_url) {
                this.medium_avatar_url = medium_avatar_url;
            }

            public IdentifyBean getIdentify() {
                return identify;
            }

            public void setIdentify(IdentifyBean identify) {
                this.identify = identify;
            }

            public String getFollow_count() {
                return follow_count;
            }

            public void setFollow_count(String follow_count) {
                this.follow_count = follow_count;
            }

            public String getFans_count() {
                return fans_count;
            }

            public void setFans_count(String fans_count) {
                this.fans_count = fans_count;
            }

            public String getScene_count() {
                return scene_count;
            }

            public void setScene_count(String scene_count) {
                this.scene_count = scene_count;
            }

            public String getSight_count() {
                return sight_count;
            }

            public void setSight_count(String sight_count) {
                this.sight_count = sight_count;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getJob() {
                return job;
            }

            public void setJob(String job) {
                this.job = job;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getWeixin() {
                return weixin;
            }

            public void setWeixin(String weixin) {
                this.weixin = weixin;
            }

            public String getIm_qq() {
                return im_qq;
            }

            public void setIm_qq(String im_qq) {
                this.im_qq = im_qq;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getExpert_label() {
                return expert_label;
            }

            public void setExpert_label(String expert_label) {
                this.expert_label = expert_label;
            }

            public String getExpert_info() {
                return expert_info;
            }

            public void setExpert_info(String expert_info) {
                this.expert_info = expert_info;
            }

            public String getAge_group() {
                return age_group;
            }

            public void setAge_group(String age_group) {
                this.age_group = age_group;
            }

            public String getAssets() {
                return assets;
            }

            public void setAssets(String assets) {
                this.assets = assets;
            }

            public String getHead_pic_url() {
                return head_pic_url;
            }

            public void setHead_pic_url(String head_pic_url) {
                this.head_pic_url = head_pic_url;
            }

            public int getIs_love() {
                return is_love;
            }

            public void setIs_love(int is_love) {
                this.is_love = is_love;
            }

            public static class IdentifyBean {
                private String is_scene_subscribe;
                private int is_expert;

                public String getIs_scene_subscribe() {
                    return is_scene_subscribe;
                }

                public void setIs_scene_subscribe(String is_scene_subscribe) {
                    this.is_scene_subscribe = is_scene_subscribe;
                }

                public int getIs_expert() {
                    return is_expert;
                }

                public void setIs_expert(int is_expert) {
                    this.is_expert = is_expert;
                }
            }
        }
    }
}
