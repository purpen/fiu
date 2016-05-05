package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author lilin
 *         created at 2016/4/7 2001
 */
public class User implements Serializable {
    public long _id;
    public String account;
    public String nickname;
    public String true_nickname;
    public int state;
    public int first_login;
    public int sex;
    public long created_on;
    public String birthday;
    public String medium_avatar_url;
    public String avatar;
    public Identify identify;
    public String realname;
    public String phone;
    public String nation;
    public String born_place;
    public String card;
    public String job;
    public String school;
    public String address;
    public String zip;
    public String im_qq;
    public String weixin;
    public String height;
    public String weight;
    public int marital;
    public ArrayList age;
    public String company;
    public String industry;
    public int province_id;
    public int district_id;
    public int rank_id;
    public String rank_title;
    public int bird_coin;
    public long current_user_id;
    public String summary;
    public String city;
    public int follow_count;
    public int fans_count;
    public int scene_count; //情境
    public int sight_count; //场景
    public Counter counter;
    public String head_pic_url;
    public class Counter implements Serializable{
        public int notice_count;
        public int message_count;
        public int comment_count;
        public int fans_count;
        public int fiu_comment_count;
        public int fiu_notice_count;
        public int sight_love_count;
        public int message_total_count;
        public int order_wait_payment;
        public int order_ready_goods;
        public int order_sended_goods;
        public int order_evaluate;
        public int order_total_count;
    }
    public class Identify implements Serializable{
        public int d3in_volunteer;
        public int d3in_vip;
        public int d3in_tag;
        public int is_scene_subscribe;
    }
}
