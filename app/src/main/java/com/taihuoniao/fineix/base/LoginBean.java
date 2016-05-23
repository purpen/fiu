package com.taihuoniao.fineix.base;

/**
 * Created by taihuoniao on 2016/5/23.
 */
public class LoginBean extends BaseBean {
    private String UserId;
    private int Type;
    private int CompanyType;

    public int getCompanyType() {
        return CompanyType;
    }

    public void setCompanyType(int companyType) {
        CompanyType = companyType;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
