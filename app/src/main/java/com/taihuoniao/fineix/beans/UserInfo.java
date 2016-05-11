package com.taihuoniao.fineix.beans;

import android.content.Context;
import android.content.SharedPreferences;

import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;

import java.io.Serializable;

/**
 * Created by android on 2016/1/25.
 */
public class UserInfo implements Serializable {
    private static SharedPreferences sp;
    public static SharedPreferences.Editor editor;

    private String signCount;
    private String signSuccess;

    private boolean success;
    private String account;
    private String nickname;
    private String true_nickname;
    private String sex;
    private String birthday;
    private String medium_avatar_url;
    private String realname;
    private String phone;
    private String address;
    private String zip;
    private String im_qq;
    private String weixin;
    private String company;
    private String province_id;
    private String district_id;
    private String rank_id;
    private String rank_title;
    private String _id;
    private String bird_coin;
    private String order_ready_goods, order_wait_payment, order_sended_goods, order_evaluate, order_total_count;

    private UserInfo() {
    }

    private static UserInfo ourInstance;

    public static UserInfo getInstance() {
        if (ourInstance == null) {
            init(MainApplication.getContext());
        }
        return ourInstance;
    }

    //初始化于Application onCreate()中执行
    public static void init(Context context) {
        ourInstance = new UserInfo();
        sp = context.getSharedPreferences(DataConstants.USERDATA_SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "signCount='" + signCount + '\'' +
                ", signSuccess='" + signSuccess + '\'' +
                ", success='" + success + '\'' +
                ", account='" + account + '\'' +
                ", nickname='" + nickname + '\'' +
                ", true_nickname='" + true_nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", medium_avatar_url='" + medium_avatar_url + '\'' +
                ", realname='" + realname + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", zip='" + zip + '\'' +
                ", im_qq='" + im_qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", company='" + company + '\'' +
                ", province_id='" + province_id + '\'' +
                ", district_id='" + district_id + '\'' +
                ", rank_id='" + rank_id + '\'' +
                ", rank_title='" + rank_title + '\'' +
                ", _id='" + _id + '\'' +
                ", bird_coin='" + bird_coin + '\'' +
                ", order_ready_goods='" + order_ready_goods + '\'' +
                ", order_wait_payment='" + order_wait_payment + '\'' +
                ", order_sended_goods='" + order_sended_goods + '\'' +
                ", order_evaluate='" + order_evaluate + '\'' +
                ", order_total_count='" + order_total_count + '\'' +
                '}';
    }

    public String getOrder_ready_goods() {
        return order_ready_goods;
    }

    public void setOrder_ready_goods(String order_ready_goods) {
        this.order_ready_goods = order_ready_goods;
    }

    public String getOrder_wait_payment() {
        return order_wait_payment;
    }

    public void setOrder_wait_payment(String order_wait_payment) {
        this.order_wait_payment = order_wait_payment;
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

    public String getSignCount() {
        return signCount;
    }

    public void setSignCount(String signCount) {
        this.signCount = signCount;
    }

    public String getSignSuccess() {
        return signSuccess;
    }

    public void setSignSuccess(String signSuccess) {
        this.signSuccess = signSuccess;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMedium_avatar_url() {
        return medium_avatar_url;
    }

    public void setMedium_avatar_url(String medium_avatar_url) {
        this.medium_avatar_url = medium_avatar_url;
    }
//    public int getSignCount() {
//        return sp.getInt("signcount", 0);
//    }
//
//    public void setSignCount(int signCount) {
//        editor.putInt("signcount", signCount);
//        editor.commit();
//    }
//
//    public String getSignSuccess() {
//        return sp.getString("signsuccess", "");
//    }
//
//    public void setSignSuccess(String signSuccess) {
//        editor.putString("signsuccess", signSuccess);
//        editor.commit();
//    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

//    public String getNickname() {
//        return sp.getString("nickname", "");
//    }
//
//    public void setNickname(String nickname) {
//        editor.putString("nickname", nickname);
//        editor.commit();
//    }

    public String getTrue_nickname() {
        return true_nickname;
    }

    public void setTrue_nickname(String true_nickname) {
        this.true_nickname = true_nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

//    public String getMedium_avatar_url() {
//        return sp.getString("avatar", "");
//    }
//
//    public void setMedium_avatar_url(String medium_avatar_url) {
//        editor.putString("avatar", medium_avatar_url);
//        editor.commit();
//    }

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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getIm_qq() {
        return im_qq;
    }

    public void setIm_qq(String im_qq) {
        this.im_qq = im_qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public void setBird_coin(String bird_coin) {
        this.bird_coin = bird_coin;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getBird_coin() {
        return bird_coin;
    }
}
