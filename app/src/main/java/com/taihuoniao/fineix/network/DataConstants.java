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
    public static final int MyGoodsDetailsActivity = 104;
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
    public static final int RESULTCODE_CREATESCENE_SEARCHQJ = 44;
    //SelectQingjingActivity到AllQingjingActivity界面的请求码和返回码
    public static final int REQUESTCODE_SELECTQJ_ALLQJ = 63;
    public static final int RESULTCODE_SELECTQJ_ALLQJ = 62;
    public static final int RESULTCODE_SELECTQJ_SALLQJ = 45;
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
    //省市列表
    public static final int PROVINCE_LIST = 54;
    //提交编辑的地址
    public static final int COMMIT_NEW_ADDRESS = 53;
    //删除收货地址
    public static final int DELETE_ADDRESS = 52;
    //添加新地址的resultcode
    public static final int RESULTCODE_ADDNEWADDRESS = 51;
    //编辑地址requestcode
    public static final int REQUESTCODE_EDITADDRESS = 50;
    //选择收货地址resultcode
    public static final int RESULTCODE_ADDRESS = 49;
    //添加新地址的requestcode
    public static final int REQUESTCODE_ADDNEWADDRESS = 48;
    //获取收货地址
    public static final int GET_ADDRESS_LIST = 47;
    //搜索结果
    public static final int SEARCH_LIST = 46;
    //广播接收action
    public static final String BROAD_GOODS_DETAILS = "com.taihuoniao.fiu.goodsdetails";
    //自营产品详情
    public static final int GOODS_DETAILS = 43;
    //商品详情评论列表
    public static final int GOODS_DETAILS_COMMENTS = 42;
    //购物车数量
    public static final int PARSER_SHOP_CART_NUMBER = 41;
    public static final int PARSER_SHOP_CART = 40;
    public static final int PARSER_SHOP_CART_CALCULATE = 39;
    public static final int DEFAULT_ADDRESS = 38;
    public static final int REQUESTCODE_ADDRESS = 37;
    public static final int RESULTCODE_TRANSFER_TIME = 36;
    public static final int REQUESTCODE_TRANSFER_TIME = 35;
    public static final int NOW_CONFIRM_ORDER = 34;
    public static final int PARSER_PAY_ALIPAY = 33;
    public static final int PARSER_PAY_WECHAT = 32;
    public static final int BUY_NOW = 31;
    public static final int ADD_TO_CART = 30;
    public static final int CANCEL_LOVE = 29;
    public static final int LOVE = 28;
    public static final int PARSER_ORDER_DETAILS = 27;
    public static final int PARSER_APPLY_REFUND = 26;
    public static final int PARSER_ORDER = 25;
    public static final int PARSER_USER_INFO = 24;
    public static final int PASER_SHOPCART_INVENTORY_ITEM = 23;
    public static final int FIU_USER = 22;
    //购物车跳转到首页需要切换到品页面的广播
    public static final String BroadShopCart = "com.taihuoniao.fiu.shopcart";
    //删除用户添加的产品
    public static final int DELETE_PRODUCT = 21;
    public static final int RESULTCODE_MAP_SELECTQJ = 20;
    public static final int REQUESTCODE_SELECTQJ_MAP = 19;
    public static final int RESULTCODE_MAP = 18;
    //标签页面重新布局的广播接收器
    public static final String BroadLabelActivity = "com.taihuoniao.fiu.label";
    //创建场景的添加产品页面的搜索广播
    public static final String BroadSearchFragment = "com.taihuoniao.fiu.searchProduct";
    //删除场景后通知场景列表页面刷新
    public static final String BroadDeleteScene = "com.taihuoniao.fiu.delete";
    //删除评论
    public static final int DELETE_COMMENT = 17;
    //分享场景语境次数
    public static final int SHARECJ = 16;
    //场景热门标签
    public static final int CJ_HOTLABEL = 15;
    //删除场景
    public static final int DELETE_SCENE = 14;
    //删除情景
    public static final int DELETE_QINGJING = 13;

    //SharedPreferences 存储文件名
    public static final String USERDATA_SHAREDPREFERENCES_NAME = "fiu_settings";

    public static final String LOGIN_INFO = "LOGIN_INFO";
    public static final String SHAREDPREFRENCES_FIRST_IN = "fiu_first_in";//第一次进入app
    public static final String FIRST_IN_QING = "FirstInQing";//判断是不是第一次进入情界面
    public static final String FIRST_IN_JING = "FirstInJing";//判断是不是第一次进入景界面
    public static final String FIRST_IN_FIU = "FirstInFiu";//第一次进入Fiu
    public static final String FIRST_IN_PIN = "FirstInPin";//第一次进入品
    public static final String FIRST_IN_WO = "FirstInWo";//第一次进入我
    public static final String FIRST_IN_CREATE = "FirstInCreate";//第一次创建
    public static final String FIRST_IN_URL = "FirstInUrl";//第一次添加链接
    public static final String FIRST_IN_ALL = "FirstInAll";//第一次进入全部情景
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

    //首页　自定义下拉刷新中mHandler.sendEmptyMessageDelayed(__,__)的第一个参数
    public static final int CUSTOM_PULLTOREFRESH_HOME = 7;
    //验证红包是否可用
    public static final int PARSER_CHECK_REDBAG_USABLE = 42;

    //商品详情界面到可用红包界面的requestcode和resultcode
    public static final int REQUESTCODE_REDBAG = 57;
    public static final int RESULTCODE_REDBAG = 56;
    public static final String GUIDE_TAG = "guide";
    public static final int GUIDE_INTERVAL = 3000;
    public static final String APP_ID = "";
    //帐户处我的红包未过期
    public static final int PARSER_MY_REDBAG_UNTIMEOUT = 19;
    //帐户处我的红包已过期
    public static final int PARSER_MY_REDBAG_TIMEOUT = 20;
    public static final int DIALOG_DELAY=400;
}
