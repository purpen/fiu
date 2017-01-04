package com.taihuoniao.fineix.network;

/**
 * Created by android on 2015/12/27
 * 网络请求url封装
 */
public class NetworkConstance {
    public static final String BASE_URL = "https://api.taihuoniao.com/app/api"; //线上
//    public static final String BASE_URL = "http://m.taihuoniao.com/app/api";//生产环境
//    public static final String BASE_URL = "http://t.taihuoniao.com/app/api";  // 测试环境

    //产品
    //统计用户想购买的数量
    public static final String WANT_BUY = BASE_URL + "/scene_product/sight_click_stat";

    //删除用户添加的产品
    public static final String DELETE_PRODUCT = BASE_URL + "/product/deleted";

    //列表
    public static final String URLSTRING_PRODUCTSLIST = BASE_URL + "/product/getlist";

    //添加产品
    public static final String ADD_PRODUCT = BASE_URL + "/product/submit";

    //产品详情
    public static final String GOOD_DETAILS = BASE_URL + "/product/view";

    //获取京东商品信息
    public static final String URLSTRING_JD_PRODUCTSDATA = BASE_URL + "/scene_product/jd_view";

    //获取淘宝商品信息
    public static final String URLSTRING_TB_PRODUCTSDATA = BASE_URL + "/scene_product/tb_view";

    //情景
    //点赞，订阅，关注，收藏通用列表
    public static final String COMMON_LISTS = BASE_URL + "/favorite";

    //订阅情景
    public static final String SUBS_QINGJING = BASE_URL + "/favorite/ajax_subscription";

    //取消订阅情景
    public static final String CANCEL_SUBS_QINGJING = BASE_URL + "/favorite/ajax_cancel_subscription";

    //情景新增
    public static final String CREATE_QINGJING = BASE_URL + "/scene_scene/save";

    //删除情景
    public static final String DELETE_QINGJING = BASE_URL + "/scene_scene/delete";

    //情景详情
    public static final String QINGJING_DETAILS = BASE_URL + "/scene_scene/view";

    //情景列表
    public static final String QINGJING_LISTS = BASE_URL + "/scene_scene/";

    //场景
    //新增场景
    public static final String CREATE_SCENE = BASE_URL + "/scene_sight/save";

    //删除场景
    public static final String DELETE_SCENE = BASE_URL + "/scene_sight/delete";

    //场景点赞
    public static final String LOVE_SCENE = BASE_URL + "/favorite/ajax_sight_love";

    //取消场景点赞
    public static final String CANCEL_LOVE_SCENE = BASE_URL + "/favorite/ajax_cancel_sight_love";

    //场景详情
    public static final String SCENE_DETAILS = BASE_URL + "/scene_sight/view";

    //列表数据
    public static final String SCENE_LIST = BASE_URL + "/scene_sight/getlist";

    //个人中心
    //用户列表
    public static final String USER_LIST = BASE_URL + "/user/";

    //标签
    //最近使用的标签
    public static final String USED_LABEL_LIST = BASE_URL + "/my/my_recent_tags";

    //标签列表
    public static final String LABEL_LIST = BASE_URL + "/scene_tags/getlist";

    //场景页热门标签
    public static final String CJ_HOT_LABEL = BASE_URL + "/gateway/get_fiu_hot_sight_tags";

    //产品页热门标签
    public static final String PRODUCT_HOT_LABEL = BASE_URL + "/gateway/get_fiu_hot_product_tags";

    //热门标签
    public static final String HOT_LABEL_LIST = BASE_URL + "/scene_tags/hotlist";

    //收货地址
    //省市列表
    public static final String URLSTRING_PROVINCE_CITIES = BASE_URL + "/shopping/fetch_areas";

    //  获取用户默认收货地址接口
    public static final String URLSTRING_DEFAULT_ADDRESS = BASE_URL + "/delivery_address/defaulted";

    //  获取用户收货地址列表
    public static final String URLSTRING_ADDRESS_LISTS = BASE_URL + "/delivery_address/get_list";

    //  新增收货地址(编辑)
    public static final String URLSTRING_NEW_ADDRESS = BASE_URL + "/delivery_address/save";

    //  删除某个收货地址
    public static final String URLSTRING_DELETE_ADDRESS = BASE_URL + "/delivery_address/deleted";

    //公共
    //举报
    public static final String REPORT = BASE_URL + "/report_tip/save";

    //品牌列表
    public static final String BRAND_LIST = BASE_URL + "/scene_brands/getlist";

    //品牌详情
    public static final String BRAND_DETAIL = BASE_URL + "/scene_brands/view";

    //分类列表
    public static final String CATEGORY_LIST = BASE_URL + "/category/getlist";

    //搜索列表
    public static final String SEARCH = BASE_URL + "/SEARCH/getlist";

    //商品和场景关联列表
    public static final String PRODUCT_AND_SCENELIST = BASE_URL + "/sight_and_product/getlist";

    //购物车
    //购物车数量
    public static final String CART_NUMBER = BASE_URL + "/shopping/fetch_cart_count";

    //评论
    //提交评论
    public static final String SEND_COMMENT = BASE_URL + "/comment/ajax_comment";

    //列表
    public static final String COMMENTS_LIST = BASE_URL + "/comment/getlist";

    //删除评论
    public static final String DELETE_COMMENT = BASE_URL + "/comment/deleted";

    //我接收到的评论列表
    public static final String MY_COMMENTS_LIST = BASE_URL + "/my/comment_list";

    //分类
    //分类标签
    public static final String CATEGORY_LABEL = BASE_URL + "/category/fetch_child_tags";

    //情景列表
    public static final String QING_JING = BASE_URL + "/scene_scene/";

    //获得用户信息自己和别人的主页
    public static final String MINE_INFO = BASE_URL + "/user/user_info";

    //送积分
    public static final String GET_BONUS = BASE_URL + "/user/send_exp";

    //获得个人中心
    public static final String USER_CENTER = BASE_URL + "/auth/user";

    //获得Banners
    public static final String BANNERS_URL = BASE_URL + "/gateway/slide";

    //获得产品列表
    public static final String PRODUCTS_URL = BASE_URL + "/scene_product/getlist";

    //获得关注列表和粉丝列表
    public static final String FOCUS_FAVORITE_URL = BASE_URL + "/follow";

    //关注操作
    public static final String FOCUS_OPRATE_URL = BASE_URL + "/follow/ajax_follow";

    //取消关注
    public static final String CANCEL_FOCUS_URL = BASE_URL + "/follow/ajax_cancel_follow";

    //意见反馈
    public static final String SUGGESTION_URL = BASE_URL + "/gateway/feedback";

    //修改用户信息
    public static final String UPDATE_USERINFO_URL = BASE_URL + "/my/update_profile";

    //获取全部城市
    public static final String ALL_CITY_URL = BASE_URL + "/shopping/fetch_areas";

    //上传头像
    public static final String UPLOAD_IMG_URL = BASE_URL + "/my/upload_token";

    //上传个人中心背景
    public static final String UPLOAD_BG_URL = BASE_URL + "/my/add_head_pic";

    //获取系统通知
    public static final String SYSTEM_NOTICE = BASE_URL + "/notice/getlist";

    //获取消息记录
    public static final String MESSAGE_RECORD = BASE_URL + "/message";

    //发送消息
    public static final String SEND_MESSAGE = BASE_URL + "/message/ajax_message";

    //消息详情
    public static final String MESSAGE_DETAIL = BASE_URL + "/message/view";

    //  立即下单
    public static final String URLSTRING_NOW_CONFIRMORDER = BASE_URL + "/shopping/confirm";

    //  取消点赞
    public static final String URLSTRING_CANCELLOVE = BASE_URL + "/favorite/ajax_cancel_love";

    //  点赞接口
    public static final String URLSTRING_LOVE = BASE_URL + "/favorite/ajax_love";
    //  添加购物车
    public static final String URLSTRING_ADD_TO_CART = BASE_URL + "/shopping/add_cart";
    //  立即购买
    public static final String URLSTRING_BUY_NOW = BASE_URL + "/shopping/now_buy";

    //  更新是否首次登录标识
    public static final String UPDATE_USER_IDENTIFY = BASE_URL + "/my/update_user_identify";

    //  提醒
    public static final String NOTICE_LIST = BASE_URL + "/remind/getlist";

    //  退出登录
    public static final String LOGOUT = BASE_URL + "/auth/logout";

    //  发现好友
    public static final String FIND_FRIENDS = BASE_URL + "/user/find_user";

    //最fiu伙伴
    public static final String FIU_USER_LIST = BASE_URL + "/user/activity_user";

    //首页订阅的场景列表
    public static final String SUBS_CJLIST = BASE_URL + "/my/my_subscription";

    //  检查是否注册
    public static final String GET_REGIST_STATE = BASE_URL + "/auth/check_account";

    //支付URL
    public static final String PAY_URL = BASE_URL + "/shopping/payed";

    //检查登录状态
    public static final String CHECK_LOGIN_URL = BASE_URL + "/auth/check_login";

    //上传官方认证信息
    public static final String UPLOAD_IDENTIFY_URL = BASE_URL + "/my/talent_save";

    //收藏产品和取消收藏
    public static final String FAVORITE_PRODUCT = BASE_URL + "/favorite/ajax_favorite";
    public static final String CANCEL_FAVORITE_PRODUCT = BASE_URL + "/favorite/ajax_cancel_favorite";
    public static final String CHOOSEN_SUBJECT_URL = BASE_URL + "/scene_subject/getlist";

    public static final String SETTINGS_ABOUTUS = BASE_URL + "/view/about";
    public static final String GATEWAY_RECORD_FIU_USER_ACTIVE = BASE_URL + "/gateway/record_fiu_user_active";
    public static final String SCENE_SIGHT_STICK_ACTIVE_TAGS = BASE_URL + "/scene_sight/stick_active_tags";
    public static final String SCENE_SIGHT_RECORD_VIEW = BASE_URL + "/scene_sight/record_view";
    public static final String AUTH_USER = BASE_URL + "/auth/user";
    public static final String SCENE_SUBJECT_RECORD_SHARE_COUNT = BASE_URL + "/scene_subject/record_share_count";
    public static final String MY_MODIFY_PASSWORD = BASE_URL + "/my/modify_password";
    public static final String GATEWAY_DEL_INVITE_CODE = BASE_URL + "/gateway/del_invite_code";
    public static final String SHOPPING_ALERT_SEND_GOODS = BASE_URL + "/shopping/alert_send_goods";
    public static final String AUTH_THIRD_SIGN = BASE_URL + "/auth/third_sign";
    public static final String MY_ADD_INTEREST_SCENE_ID = BASE_URL + "/my/add_interest_scene_id";
    public static final String GATEWAY_VALIDE_INVITE_CODE = BASE_URL + "/gateway/valide_invite_code";
    public static final String AUTH_CHECK_VERIFY_CODE = BASE_URL + "/auth/check_verify_code";
    public static final String SCENE_SUBJECT_GETLIST = BASE_URL + "/scene_subject/getlist";
    public static final String AUTH_THIRD_REGISTER_WITHOUT_PHONE = BASE_URL + "/auth/third_register_without_phone";
    public static final String FAVORITE_AJAX_FAVORITE = BASE_URL + "/favorite/ajax_favorite";
    public static final String SHOPPING_FETCH_CART_COUNT = BASE_URL + "/shopping/fetch_cart_count";
    public static final String SHOPPING_FETCH_CART = BASE_URL + "/shopping/fetch_cart";
    public static final String SHOPPING_FETCH_CART_PRODUCT_COUUNT = BASE_URL + "/shopping/fetch_cart_product_count";
    public static final String SHOPPINGN_EDIT_CART = BASE_URL + "/shopping/edit_cart";
    public static final String USER_DO_STICK = BASE_URL + "/user/do_stick";
    public static final String USER_DO_FINE = BASE_URL + "/user/do_fine";
    public static final String USER_DO_CHECK = BASE_URL + "/user/do_check";
    public static final String GATEWAY_FETCH_CHINESE_WORD = BASE_URL + "/gateway/fetch_chinese_word";
    public static final String SEARCH_EXPANDED = BASE_URL + "/SEARCH/expanded";
    public static final String SHOPPING_FETCH_CHINA_CITY = BASE_URL + "/shopping/fetch_china_city";
    public static final String PRODUCT_AJAX_COMMENT = BASE_URL + "/product/ajax_comment";
    public static final String SHOPPING_DETAILS = BASE_URL + "/shopping/detail";
    public static final String SHOPPING_ORDERS = BASE_URL + "/shopping/orders";
    public static final String MY_BONUS = BASE_URL + "/my/bonus";
    public static final String GATEWAY_IS_INVITED = BASE_URL + "/gateway/is_invited";
    public static final String USER_IS_EDITOR = BASE_URL + "/user/is_editor";
    public static final String AUTH_VERIFY_CODE = BASE_URL + "/auth/verify_code";
    public static final String USER_TEMP_VIEW = BASE_URL + "/user_temp/view";
    public static final String SCENE_CONTEXT_GETLIST = BASE_URL + "/scene_context/getlist";
    public static final String SHOPPING_TAKE_DELIVERY = BASE_URL + "/shopping/take_delivery";
    public static final String SETTINGSS_ABOUTUS = BASE_URL + "/view/about";
    public static final String SCENE_SUBJECT_VIEW = BASE_URL + "/scene_subject/view";
    public static final String SHOPPING_REFUND_VIEW = BASE_URL + "/shopping/refund_view";
    public static final String SHOPPING_REFUND_LIST = BASE_URL + "/shopping/refund_list";
    public static final String SCENE_SUBJECT_INDEX_SUJECT_STICK = BASE_URL + "/scene_subject/index_subject_stick";
    public static final String FAVORITE_GET_NEW_LIST = BASE_URL + "/favorite/get_new_list";
    public static final String SHOPPING_CHECK_REFUND = BASE_URL + "/shopping/check_refund";
    public static final String MY_FETCH_TALENT = BASE_URL + "/my/fetch_talent";
    public static final String SHOPPING_APPLY_PRODUCT_REFUND = BASE_URL + "/shopping/apply_product_refund";
    public static final String FOLLOW_BATCH_FOLLOW = BASE_URL + "/follow/batch_follow";
    public static final String USER_FIND_USER = BASE_URL + "/user/find_user";
    public static final String PRODUCCT_INDEX_NEW = BASE_URL + "/product/index_new";
    public static final String AUTH_FIND_PWD = BASE_URL + "/auth/find_pwd";
    public static final String SHOPPING_REMOVE_CART = BASE_URL + "/shopping/remove_cart";
    public static final String MY_DELETE_ORDER = BASE_URL + "/my/delete_order";
    public static final String SCENE_SIGHT_ADD_SHARE_CONTEXT_NUM = BASE_URL + "/scene_sight/add_share_context_num";
    public static final String AUTH_REGISTER = BASE_URL + "/auth/register";
    public static final String AUTH_LOGIN = BASE_URL + "/auth/login";
    public static final String SHOPPING_USE_BONUS = BASE_URL + "/shopping/use_bonus";
    public static final String MY_REMOVE_INTEREST_SCENE_ID = BASE_URL + "/my/remove_interest_scene_id";
    public static final String FAVORITE_AJAX_CANCEL_FAVORITE = BASE_URL + "/favorite/ajax_cancel_favorite";
    public static final String MY_CANCEL_ORDER = BASE_URL + "/my/cancel_order";
    public static final String SHOPING_CHECKOUT = BASE_URL + "/shopping/checkout";
    public static final String AUTH_THIRD_REGISTER_WITH_PHONE = BASE_URL + "/auth/third_register_with_phone";
    public static final String SHOPPING_APPLY_REFUND = BASE_URL + "/shopping/apply_refund";
    public static final String SCENE_BRANDS_SUBMIT = BASE_URL + "/scene_brands/submit";

    public static final String FETCH_LATEST_VERSION = BASE_URL + "/gateway/fetch_version";
    public static final String CHECK_VERSION_INFO = BASE_URL + "/gateway/check_version";
    public static final String SHOPPING_FETCH_FREIGHT = BASE_URL + "/shopping/fetch_freight";   //获取邮费接口
    public static final String SHOPPING_TRACKING = BASE_URL + "/shopping/logistic_tracking";   //物流跟踪


}
