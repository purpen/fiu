package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/1/19.
 */
public class TryDetailsUserBean {
    private String _id;
    private String nickname;
    private String home_url;
    private String small_avatar_url;
    private String symbol;
    private String mini_avatar_url;
    private String medium_avatar_url;
    private String big_avatar_url;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBig_avatar_url() {
        return big_avatar_url;
    }

    public void setBig_avatar_url(String big_avatar_url) {
        this.big_avatar_url = big_avatar_url;
    }

    public String getHome_url() {
        return home_url;
    }

    public void setHome_url(String home_url) {
        this.home_url = home_url;
    }

    public String getMedium_avatar_url() {
        return medium_avatar_url;
    }

    public void setMedium_avatar_url(String medium_avatar_url) {
        this.medium_avatar_url = medium_avatar_url;
    }

    public String getMini_avatar_url() {
        return mini_avatar_url;
    }

    public void setMini_avatar_url(String mini_avatar_url) {
        this.mini_avatar_url = mini_avatar_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSmall_avatar_url() {
        return small_avatar_url;
    }

    public void setSmall_avatar_url(String small_avatar_url) {
        this.small_avatar_url = small_avatar_url;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
