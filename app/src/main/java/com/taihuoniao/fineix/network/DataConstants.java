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
    //EditPictureActivity跳转到SearchStoreActivity界面的请求码和返回码
    public static final int REQUESTCODE_EDIT_SEARCHSTORE = 93;
    public static final int RESULTCODE_EDIT_SEARCHSTORE = 92;
    public static final int REQUESTCODE_EDITEDIT_SEARCHSTORE = 88;//点击链接标签跳转请求码
    //SearchStoreActivity跳转到SearchURLActivity界面的请求码和返回码
    public static final int REQUESTCODE_SEARCHSTORE_SEARCHURL = 91;
    public static final int RESULTCODE_SEARCHSTORE_SEARCHURL = 90;
    public static final String URL_JINGDONG = "https://www.jd.com/";
    public static final String URL_TAOBAO = "https://www.taobao.com/";
    public static final String URL_TIANMAO = "https://www.tmall.com/";
    public static final String URL_YAMAXUN = "http://www.amazon.cn/";
    //添加产品界面产品列表
    public static final int ADD_PRODUCT_LIST = 89;
    //添加标签界面使用过的标签
    public static final int USED_LABEL_LIST = 88;
    //添加标签界面所有标签
    public static final int LABEL_LIST = 87;
    //添加标签页面热门标签列表
    public static final int HOT_LABEL_LIST = 86;
    //CreateSceneActivity跳转到AddLabelActivity界面的请求码和返回码
    public static final int REQUESTCODE_CREATESCENE_ADDLABEL = 85;
    public static final int RESULTCODE_CREATESCENE_ADDLABEL = 84;
    //AddProductActivity分类列表
    public static final int CATEGORY_LIST = 83;
}
