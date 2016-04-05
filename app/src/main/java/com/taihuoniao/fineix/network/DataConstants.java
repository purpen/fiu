package com.taihuoniao.fineix.network;

/**
 * Created by taihuoniao on 2016/3/14.
 * 常量
 */
public class DataConstants {
    public static final int NET_FAIL = 404;//网络请求失败
    //activity跳转的请求码和返回码
    public static final int REQUESTCODE_PHOTO_TO_CROP = 100;
    //EditPictureActivity跳转到AddProductActivity界面的请求码和返回码
    public static final int REQUESTCODE_EDIT_ADDPRODUCT = 99;
    public static final int RESULTCODE_EDIT_ADDPRODUCT = 98;
    //SelectStoreActivity跳转到SearchURLActivity界面，点击的是哪个按钮，进入哪个商城
    public static final int JINGDONG = 97;
    public static final int TAOBAO = 96;
    public static final int TIANMAO = 95;
    public static final int YAMAXUN = 94;
    public static final String URL_JINGDONG = "https://www.jd.com/";
    public static final String URL_TAOBAO = "https://www.taobao.com/";
    public static final String URL_TIANMAO = "https://www.tmall.com/";
    public static final String URL_YAMAXUN = "http://www.amazon.cn/";
}
