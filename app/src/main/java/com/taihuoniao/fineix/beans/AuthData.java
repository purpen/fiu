package com.taihuoniao.fineix.beans;

import java.io.Serializable;

/**
 * @author lilin
 *         created at 2016/5/29 17:10
 */
public class AuthData implements Serializable {
    public String _id;
    public int verified; //审核状态：0.未审核；1.拒绝；2.通过
    public String info; //认证信息
    public String label; //认证标签
    public String id_card_cover_url; //身份证
    public String business_card_cover_url; //名片
    public String contact; //联系方式
}
