package com.taihuoniao.fineix.beans;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SPUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by android on 2016/1/20.
 */
public class LoginInfo implements Serializable {
    private static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    private long _id;
    private String message;
    private String account;
    private String nickname;
    private String true_nickname;
    private String realname;
    private String sex;
    private String medium_avatar_url;
    private String birthday;
    private String address;
    private String zip;
    private String im_qq;
    private String weixin;
    private String company;
    private String phone;
    private int first_login;
    private static LoginInfo loginInfo;
    public Identify identify;
    public ArrayList<String> areas;
    public class Identify{
        public int is_scene_subscribe;
    }
    private LoginInfo() {
    }

    private static LoginInfo instance;

    public static LoginInfo getInstance() {
        if (instance == null)
            instance=new LoginInfo();
        return instance;
    }

    //初始化于Application onCreate()中执行
//    public static void init(Context context) {
//        ourInstance = new LoginInfo();
//        sp = context.getSharedPreferences(DataConstants.USERDATA_SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
//        editor = sp.edit();
//
//    }

    public static String getHeadPicUrl() {
        if (isUserLogin()) {
            String login_info = SPUtil.read(MainApplication.getContext(), DataConstants.LOGIN_INFO);
            loginInfo = JsonUtil.fromJson(login_info,LoginInfo.class);
            return loginInfo.medium_avatar_url;
        }
        return null;
    }

    public static LoginInfo getLoginInfo(){
        if (isUserLogin()) {
            String login_info = SPUtil.read(MainApplication.getContext(), DataConstants.LOGIN_INFO);
            loginInfo = JsonUtil.fromJson(login_info,LoginInfo.class);
            return loginInfo;
        }
        return null;
    }

    public static long getUserId() {
        if (isUserLogin()) {
            String login_info = SPUtil.read(MainApplication.getContext(), DataConstants.LOGIN_INFO);
            loginInfo = JsonUtil.fromJson(login_info,LoginInfo.class);
            return loginInfo._id;
        }
        return -1;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public static boolean isUserLogin() {
        String login_info = SPUtil.read(MainApplication.getContext(), DataConstants.LOGIN_INFO);
        return !TextUtils.isEmpty(login_info);
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMedium_avatar_url() {
        return medium_avatar_url;
    }

    public void setMedium_avatar_url(String medium_avatar_url) {
        this.medium_avatar_url = medium_avatar_url;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public int getFirst_login() {
        return first_login;
    }

    public void setFirst_login(int first_login) {
        this.first_login = first_login;
    }

    public String getSuccess() {
        return sp.getString("isLogin", "");
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
