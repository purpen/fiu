package com.taihuoniao.fineix.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SPUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by android on 2016/1/20.
 */
public class LoginInfo implements Serializable, Parcelable {

    private long _id;
    private int is_bonus;
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
    public String created_on;

    public static String getCreated_on() {
        if (isUserLogin()) {
            String login_info = SPUtil.read(DataConstants.LOGIN_INFO);
            loginInfo = JsonUtil.fromJson(login_info,LoginInfo.class);
            return loginInfo.created_on;
        }
        return null;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public static class Identify implements Serializable{
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
    public static String getHeadPicUrl() {
        if (isUserLogin()) {
            String login_info = SPUtil.read(DataConstants.LOGIN_INFO);
            loginInfo = JsonUtil.fromJson(login_info,LoginInfo.class);
            return loginInfo.medium_avatar_url;
        }
        return null;
    }

    public static String getNickName() {
        if (isUserLogin()) {
            String login_info = SPUtil.read(DataConstants.LOGIN_INFO);
            loginInfo = JsonUtil.fromJson(login_info, LoginInfo.class);
            return loginInfo.nickname;
        }
        return null;
    }

    public static String getGender() {
        if (isUserLogin()) {
            String login_info = SPUtil.read(DataConstants.LOGIN_INFO);
            loginInfo = JsonUtil.fromJson(login_info, LoginInfo.class);
            return loginInfo.sex;
        }
        return null;
    }

    public static LoginInfo getLoginInfo(){
        if (isUserLogin()) {
            String login_info = SPUtil.read(DataConstants.LOGIN_INFO);
            loginInfo = JsonUtil.fromJson(login_info,LoginInfo.class);
            return loginInfo;
        }
        return null;
    }

    public static long getUserId() {
        if (isUserLogin()) {
            String login_info = SPUtil.read(DataConstants.LOGIN_INFO);
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
        String login_info = SPUtil.read(DataConstants.LOGIN_INFO);
        return !TextUtils.isEmpty(login_info);
    }

    public int getIs_bonus() {
        return is_bonus;
    }

    public void setIs_bonus(int is_bonus) {
        this.is_bonus = is_bonus;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeInt(this.is_bonus);
        dest.writeString(this.message);
        dest.writeString(this.account);
        dest.writeString(this.nickname);
        dest.writeString(this.true_nickname);
        dest.writeString(this.realname);
        dest.writeString(this.sex);
        dest.writeString(this.medium_avatar_url);
        dest.writeString(this.birthday);
        dest.writeString(this.address);
        dest.writeString(this.zip);
        dest.writeString(this.im_qq);
        dest.writeString(this.weixin);
        dest.writeString(this.company);
        dest.writeString(this.phone);
        dest.writeInt(this.first_login);
        dest.writeSerializable(this.identify);
        dest.writeStringList(this.areas);
        dest.writeString(this.created_on);
    }

    protected LoginInfo(Parcel in) {
        this._id = in.readLong();
        this.is_bonus = in.readInt();
        this.message = in.readString();
        this.account = in.readString();
        this.nickname = in.readString();
        this.true_nickname = in.readString();
        this.realname = in.readString();
        this.sex = in.readString();
        this.medium_avatar_url = in.readString();
        this.birthday = in.readString();
        this.address = in.readString();
        this.zip = in.readString();
        this.im_qq = in.readString();
        this.weixin = in.readString();
        this.company = in.readString();
        this.phone = in.readString();
        this.first_login = in.readInt();
        this.identify = (Identify) in.readSerializable();
        this.areas = in.createStringArrayList();
        this.created_on = in.readString();
    }

    public static final Parcelable.Creator<LoginInfo> CREATOR = new Parcelable.Creator<LoginInfo>() {
        @Override
        public LoginInfo createFromParcel(Parcel source) {
            return new LoginInfo(source);
        }

        @Override
        public LoginInfo[] newArray(int size) {
            return new LoginInfo[size];
        }
    };
}
