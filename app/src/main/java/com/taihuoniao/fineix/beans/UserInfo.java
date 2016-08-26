package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by android on 2016/1/25.
 */
public class UserInfo extends NetBean {


    /**
     * is_error : false
     * data : {"_id":924789,"nickname":"铁锤","true_nickname":"铁锤","state":2,"first_login":1,"sex":1,"summary":"呀哈","created_on":1458463859,"birthday":"2013-04-12","medium_avatar_url":"http://frbird.qiniudn.com/avatar/160511/5732f0393ffca28a0c8b51af-avm.jpg","identify":{"is_scene_subscribe":1,"is_expert":0},"follow_count":4,"fans_count":3,"scene_count":3,"sight_count":13,"counter":{"message_count":0,"fans_count":8,"fiu_comment_count":0,"fiu_alert_count":0,"fiu_notice_count":0,"order_wait_payment":1,"message_total_count":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0,"order_total_count":1},"subscription_count":4,"sight_love_count":11,"realname":null,"phone":"15810417767","nation":null,"born_place":null,"card":null,"job":null,"school":null,"address":null,"zip":null,"im_qq":"","weixin":"","height":null,"weight":null,"marital":11,"age":["2013","04","12"],"company":null,"province_id":5,"district_id":101,"label":"老炮儿","interest_scene_cate":[89,90,108],"expert_label":"","expert_info":"","age_group":"","assets":"","areas":["河北省","石家庄市"],"rank_id":3,"rank_title":"鸟平民","head_pic_url":"http://frbird.qiniudn.com/user_head_pic/160819/57b6d4073ffca252658b45bc-hu.jpg","bird_coin":0,"current_user_id":924789}
     */

    private boolean is_error;
    /**
     * _id : 924789
     * nickname : 铁锤
     * true_nickname : 铁锤
     * state : 2
     * first_login : 1
     * sex : 1
     * summary : 呀哈
     * created_on : 1458463859
     * birthday : 2013-04-12
     * medium_avatar_url : http://frbird.qiniudn.com/avatar/160511/5732f0393ffca28a0c8b51af-avm.jpg
     * identify : {"is_scene_subscribe":1,"is_expert":0}
     * follow_count : 4
     * fans_count : 3
     * scene_count : 3
     * sight_count : 13
     * counter : {"message_count":0,"fans_count":8,"fiu_comment_count":0,"fiu_alert_count":0,"fiu_notice_count":0,"order_wait_payment":1,"message_total_count":0,"order_ready_goods":0,"order_sended_goods":0,"order_evaluate":0,"order_total_count":1}
     * subscription_count : 4
     * sight_love_count : 11
     * realname : null
     * phone : 15810417767
     * nation : null
     * born_place : null
     * card : null
     * job : null
     * school : null
     * address : null
     * zip : null
     * im_qq :
     * weixin :
     * height : null
     * weight : null
     * marital : 11
     * age : ["2013","04","12"]
     * company : null
     * province_id : 5
     * district_id : 101
     * label : 老炮儿
     * interest_scene_cate : [89,90,108]
     * expert_label :
     * expert_info :
     * age_group :
     * assets :
     * areas : ["河北省","石家庄市"]
     * rank_id : 3
     * rank_title : 鸟平民
     * head_pic_url : http://frbird.qiniudn.com/user_head_pic/160819/57b6d4073ffca252658b45bc-hu.jpg
     * bird_coin : 0
     * current_user_id : 924789
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
        private String _id;
        private String nickname;
        private String true_nickname;
        private String state;
        private String first_login;
        private String sex;
        private String summary;
        private String created_on;
        private String birthday;
        private String medium_avatar_url;
        /**
         * is_scene_subscribe : 1
         * is_expert : 0
         */

        private IdentifyBean identify;
        private String follow_count;
        private String fans_count;
        private String scene_count;
        private String sight_count;
        /**
         * message_count : 0
         * fans_count : 8
         * fiu_comment_count : 0
         * fiu_alert_count : 0
         * fiu_notice_count : 0
         * order_wait_payment : 1
         * message_total_count : 0
         * order_ready_goods : 0
         * order_sended_goods : 0
         * order_evaluate : 0
         * order_total_count : 1
         */

        private CounterBean counter;
        private String subscription_count;
        private String sight_love_count;
        private String realname;
        private String phone;
        private String nation;
        private String born_place;
        private String card;
        private String job;
        private String school;
        private String address;
        private String zip;
        private String im_qq;
        private String weixin;
        private String height;
        private String weight;
        private String marital;
        private String company;
        private String province_id;
        private String district_id;
        private String label;
        private String expert_label;
        private String expert_info;
        private String age_group;
        private String assets;
        private String rank_id;
        private String rank_title;
        private String head_pic_url;
        private String bird_coin;
        private String current_user_id;
        private List<String> age;
        private List<String> interest_scene_cate;
        private List<String> areas;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<String> getAge() {
            return age;
        }

        public void setAge(List<String> age) {
            this.age = age;
        }

        public String getAge_group() {
            return age_group;
        }

        public void setAge_group(String age_group) {
            this.age_group = age_group;
        }

        public List<String> getAreas() {
            return areas;
        }

        public void setAreas(List<String> areas) {
            this.areas = areas;
        }

        public String getAssets() {
            return assets;
        }

        public void setAssets(String assets) {
            this.assets = assets;
        }

        public String getBird_coin() {
            return bird_coin;
        }

        public void setBird_coin(String bird_coin) {
            this.bird_coin = bird_coin;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBorn_place() {
            return born_place;
        }

        public void setBorn_place(String born_place) {
            this.born_place = born_place;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public CounterBean getCounter() {
            return counter;
        }

        public void setCounter(CounterBean counter) {
            this.counter = counter;
        }

        public String getCreated_on() {
            return created_on;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public String getCurrent_user_id() {
            return current_user_id;
        }

        public void setCurrent_user_id(String current_user_id) {
            this.current_user_id = current_user_id;
        }

        public String getDistrict_id() {
            return district_id;
        }

        public void setDistrict_id(String district_id) {
            this.district_id = district_id;
        }

        public String getExpert_info() {
            return expert_info;
        }

        public void setExpert_info(String expert_info) {
            this.expert_info = expert_info;
        }

        public String getExpert_label() {
            return expert_label;
        }

        public void setExpert_label(String expert_label) {
            this.expert_label = expert_label;
        }

        public String getFans_count() {
            return fans_count;
        }

        public void setFans_count(String fans_count) {
            this.fans_count = fans_count;
        }

        public String getFirst_login() {
            return first_login;
        }

        public void setFirst_login(String first_login) {
            this.first_login = first_login;
        }

        public String getFollow_count() {
            return follow_count;
        }

        public void setFollow_count(String follow_count) {
            this.follow_count = follow_count;
        }

        public String getHead_pic_url() {
            return head_pic_url;
        }

        public void setHead_pic_url(String head_pic_url) {
            this.head_pic_url = head_pic_url;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public IdentifyBean getIdentify() {
            return identify;
        }

        public void setIdentify(IdentifyBean identify) {
            this.identify = identify;
        }

        public String getIm_qq() {
            return im_qq;
        }

        public void setIm_qq(String im_qq) {
            this.im_qq = im_qq;
        }

        public List<String> getInterest_scene_cate() {
            return interest_scene_cate;
        }

        public void setInterest_scene_cate(List<String> interest_scene_cate) {
            this.interest_scene_cate = interest_scene_cate;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getMarital() {
            return marital;
        }

        public void setMarital(String marital) {
            this.marital = marital;
        }

        public String getMedium_avatar_url() {
            return medium_avatar_url;
        }

        public void setMedium_avatar_url(String medium_avatar_url) {
            this.medium_avatar_url = medium_avatar_url;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvince_id() {
            return province_id;
        }

        public void setProvince_id(String province_id) {
            this.province_id = province_id;
        }

        public String getRank_id() {
            return rank_id;
        }

        public void setRank_id(String rank_id) {
            this.rank_id = rank_id;
        }

        public String getRank_title() {
            return rank_title;
        }

        public void setRank_title(String rank_title) {
            this.rank_title = rank_title;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getScene_count() {
            return scene_count;
        }

        public void setScene_count(String scene_count) {
            this.scene_count = scene_count;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSight_count() {
            return sight_count;
        }

        public void setSight_count(String sight_count) {
            this.sight_count = sight_count;
        }

        public String getSight_love_count() {
            return sight_love_count;
        }

        public void setSight_love_count(String sight_love_count) {
            this.sight_love_count = sight_love_count;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getSubscription_count() {
            return subscription_count;
        }

        public void setSubscription_count(String subscription_count) {
            this.subscription_count = subscription_count;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTrue_nickname() {
            return true_nickname;
        }

        public void setTrue_nickname(String true_nickname) {
            this.true_nickname = true_nickname;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public static class IdentifyBean {
            private String is_scene_subscribe;
            private String is_expert;

            public String getIs_scene_subscribe() {
                return is_scene_subscribe;
            }

            public void setIs_scene_subscribe(String is_scene_subscribe) {
                this.is_scene_subscribe = is_scene_subscribe;
            }

            public String getIs_expert() {
                return is_expert;
            }

            public void setIs_expert(String is_expert) {
                this.is_expert = is_expert;
            }
        }

        public static class CounterBean {
            private String message_count;
            private String fans_count;
            private String fiu_comment_count;
            private String fiu_alert_count;
            private String fiu_notice_count;
            private String order_wait_payment;
            private String message_total_count;
            private String order_ready_goods;
            private String order_sended_goods;
            private String order_evaluate;
            private String order_total_count;

            public String getMessage_count() {
                return message_count;
            }

            public void setMessage_count(String message_count) {
                this.message_count = message_count;
            }

            public String getFans_count() {
                return fans_count;
            }

            public void setFans_count(String fans_count) {
                this.fans_count = fans_count;
            }

            public String getFiu_comment_count() {
                return fiu_comment_count;
            }

            public void setFiu_comment_count(String fiu_comment_count) {
                this.fiu_comment_count = fiu_comment_count;
            }

            public String getFiu_alert_count() {
                return fiu_alert_count;
            }

            public void setFiu_alert_count(String fiu_alert_count) {
                this.fiu_alert_count = fiu_alert_count;
            }

            public String getFiu_notice_count() {
                return fiu_notice_count;
            }

            public void setFiu_notice_count(String fiu_notice_count) {
                this.fiu_notice_count = fiu_notice_count;
            }

            public String getOrder_wait_payment() {
                return order_wait_payment;
            }

            public void setOrder_wait_payment(String order_wait_payment) {
                this.order_wait_payment = order_wait_payment;
            }

            public String getMessage_total_count() {
                return message_total_count;
            }

            public void setMessage_total_count(String message_total_count) {
                this.message_total_count = message_total_count;
            }

            public String getOrder_ready_goods() {
                return order_ready_goods;
            }

            public void setOrder_ready_goods(String order_ready_goods) {
                this.order_ready_goods = order_ready_goods;
            }

            public String getOrder_sended_goods() {
                return order_sended_goods;
            }

            public void setOrder_sended_goods(String order_sended_goods) {
                this.order_sended_goods = order_sended_goods;
            }

            public String getOrder_evaluate() {
                return order_evaluate;
            }

            public void setOrder_evaluate(String order_evaluate) {
                this.order_evaluate = order_evaluate;
            }

            public String getOrder_total_count() {
                return order_total_count;
            }

            public void setOrder_total_count(String order_total_count) {
                this.order_total_count = order_total_count;
            }
        }
    }
}
