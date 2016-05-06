package com.taihuoniao.fineix.network;

/**
 * Created by taihuoniao on 2016/3/14.
 * 常量
 */
public class DataConstants {
    //判断从哪个activity跳转到登录界面
    public static final int SceneDetailActivity = 101;
    public static final int ElseActivity = 102;//其他不需要刷新界面的activity
    public static final int QingjingDetailActivity = 103;
    //场景详情界面跳转到注册界面广播Action
    public static final String BroadSceneDetail = "com.taihuoniao.fiu.scenedetail";
    //情景详情界面跳转到注册界面广播action
    public static final String BroadQingjingDetail = "com.taihuonioa.fiu.qingjingdetail";
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
    //创建场景
    public static final int CREATE_SCENE = 82;
    //场景列表数据
    public static final int SCENE_LIST = 81;
    //CreateSceneActivity跳转到BDSearchAddressActivity界面的请求码和返回码
    public static final int REQUESTCODE_CREATESCENE_BDSEARCH = 80;
    public static final int RESULTCODE_CREATESCENE_BDSEARCH = 79;
    //场景详情
    public static final int SCENE_DETAILS = 78;
    //评论列表
    public static final int COMMENTS_LIST = 77;
    //点赞，订阅，关注，收藏通用列表
    public static final int COMMON_LIST = 76;
    //情景列表
    public static final int QINGJING_LIST = 75;
    //提交评论
    public static final int SEND_COMMENT = 74;
    //创建情景
    public static final int CREATE_QINGJING = 73;
    //情景详情
    public static final int QINGJING_DETAILS = 72;
    //购物车商品数量
    public static final int CART_NUM = 71;
    //场景点赞
    public static final int LOVE_SCENE = 70;
    //取消场景点赞
    public static final int CANCEL_LOVE_SCENE = 69;
    //情景订阅
    public static final int SUBS_QINGJING = 68;
    //取消情景订阅
    public static final int CANCEL_SUBS_QINGJING = 67;
    //品牌列表
    public static final int BRAND_LIST = 66;
    //CreateSceneActivity到SelectQingjingActivity界面的请求码和返回码
    public static final int REQUESTCODE_CREATESCENE_SELECTQJ = 65;
    public static final int RESULTCODE_CREATESCENE_SELECTQJ = 64;
    //SelectQingjingActivity到AllQingjingActivity界面的请求码和返回码
    public static final int REQUESTCODE_SELECTQJ_ALLQJ = 63;
    public static final int RESULTCODE_SELECTQJ_ALLQJ = 62;
    //举报
    public static final int REPORT = 61;
    //产品详情
    public static final int GOODS_DETAIL = 60;
    //产品场景关联列表
    public static final int PRODUCT_AND_SCENE = 59;
    //用户列表
    public static final int USER_LIST = 58;
    //商品分类的子分类列表
    public static final int CATEGORY_LABEL = 57;
    //品牌详情
    public static final int BRAND_DETAIL = 56;
    //用户添加商品
    public static final int ADD_PRODUCT = 55;


    //SharedPreferences 存储文件名
    public static final String USERDATA_SHAREDPREFERENCES_NAME = "thn_settings";

    public static final String LOGIN_INFO = "LOGIN_INFO";

    //登录
    public static final int PARSER_LOGIN = 9;
    //网络数据请求失败
    public static final int NETWORK_FAILURE = 999;


    //第三方登录绑定手机号
    public static final int PARSER_THIRD_LOGIN_BIND_PHONE = 37;
    //第三方登录回调时error
    public static final int PARSER_THIRD_LOGIN_ERROR = 38;
    //第三方登录回调时cancel
    public static final int PARSER_THIRD_LOGIN_CANCEL = 39;
    //第三方登录回调时complete
    public static final int PARSER_THIRD_LOGIN = 35;
    //第三方登录不绑定手机号
    public static final int PARSER_THIRD_LOGIN_SKIP_PHONE = 36;
    //找回密码
    public static final int PARSER_FIND_PASSWORD = 10;

    public static final String GUIDE_TAG = "guide";
    public static final int GUIDE_INTERVAL = 3000;
    public static final String APP_ID = "";
}
