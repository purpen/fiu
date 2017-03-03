package com.taihuoniao.fineix.network;

/**
 * Created by android on 2015/12/27
 * 网络请求url封装
 */
public class URL {
    public static final String H5_URL="https://m.taihuoniao.com";
    public static final String BASE_URL = "https://api.taihuoniao.com/app/api"; //线上
//    public static final String BASE_URL = "https://m.taihuoniao.com/app/api";//生产环境
//    public static final String BASE_URL = "http://t.taihuoniao.com/app/api";  // 测试环境

    // 公共接口
    public static final String BANNERS_URL = BASE_URL + "/gateway/slide";
    public static final String URLSTRING_PRODUCTSLIST = BASE_URL + "/product/getlist";
    public static final String SCENE_SUBJECT_GETLIST = BASE_URL + "/scene_subject/getlist";
    public static final String CATEGORY_LIST = BASE_URL + "/category/getlist";
    public static final String SEARCH = BASE_URL + "/search/getlist";

    public static final String WANT_BUY = BASE_URL + "/scene_product/sight_click_stat";
    public static final String DELETE_PRODUCT = BASE_URL + "/product/deleted";
    public static final String ADD_PRODUCT = BASE_URL + "/product/submit";
    public static final String GOOD_DETAILS = BASE_URL + "/product/view";
    public static final String URLSTRING_JD_PRODUCTSDATA = BASE_URL + "/scene_product/jd_view";
    public static final String URLSTRING_TB_PRODUCTSDATA = BASE_URL + "/scene_product/tb_view";
    public static final String COMMON_LISTS = BASE_URL + "/favorite";
    public static final String SUBS_QINGJING = BASE_URL + "/favorite/ajax_subscription";
    public static final String CANCEL_SUBS_QINGJING = BASE_URL + "/favorite/ajax_cancel_subscription";
    public static final String CREATE_QINGJING = BASE_URL + "/scene_scene/save";
    public static final String DELETE_QINGJING = BASE_URL + "/scene_scene/delete";
    public static final String QINGJING_DETAILS = BASE_URL + "/scene_scene/view";
    public static final String QINGJING_LISTS = BASE_URL + "/scene_scene/";
    public static final String CREATE_SCENE = BASE_URL + "/scene_sight/save";
    public static final String DELETE_SCENE = BASE_URL + "/scene_sight/delete";
    public static final String LOVE_SCENE = BASE_URL + "/favorite/ajax_sight_love";
    public static final String CANCEL_LOVE_SCENE = BASE_URL + "/favorite/ajax_cancel_sight_love";
    public static final String SCENE_DETAILS = BASE_URL + "/scene_sight/view";
    public static final String SCENE_LIST = BASE_URL + "/scene_sight/getlist";
    public static final String USER_LIST = BASE_URL + "/user/";
    public static final String USED_LABEL_LIST = BASE_URL + "/my/my_recent_tags";
    public static final String LABEL_LIST = BASE_URL + "/scene_tags/getlist";
    public static final String CJ_HOT_LABEL = BASE_URL + "/gateway/get_fiu_hot_sight_tags";
    public static final String PRODUCT_HOT_LABEL = BASE_URL + "/gateway/get_fiu_hot_product_tags";
    public static final String HOT_LABEL_LIST = BASE_URL + "/scene_tags/hotlist";
    public static final String URLSTRING_PROVINCE_CITIES = BASE_URL + "/shopping/fetch_areas";
    public static final String URLSTRING_DEFAULT_ADDRESS = BASE_URL + "/delivery_address/defaulted";
    public static final String URLSTRING_ADDRESS_LISTS = BASE_URL + "/delivery_address/get_list";
    public static final String URLSTRING_NEW_ADDRESS = BASE_URL + "/delivery_address/save";
    public static final String URLSTRING_DELETE_ADDRESS = BASE_URL + "/delivery_address/deleted";
    public static final String REPORT = BASE_URL + "/report_tip/save";
    public static final String BRAND_LIST = BASE_URL + "/scene_brands/getlist";
    public static final String BRAND_DETAIL = BASE_URL + "/scene_brands/view";
    public static final String PRODUCT_AND_SCENELIST = BASE_URL + "/sight_and_product/getlist";
    public static final String CART_NUMBER = BASE_URL + "/shopping/fetch_cart_count";
    public static final String SEND_COMMENT = BASE_URL + "/comment/ajax_comment";
    public static final String COMMENTS_LIST = BASE_URL + "/comment/getlist";
    public static final String DELETE_COMMENT = BASE_URL + "/comment/deleted";
    public static final String MY_COMMENTS_LIST = BASE_URL + "/my/comment_list";
    public static final String CATEGORY_LABEL = BASE_URL + "/category/fetch_child_tags";
    public static final String QING_JING = BASE_URL + "/scene_scene/";
    public static final String MINE_INFO = BASE_URL + "/user/user_info";
    public static final String GET_BONUS = BASE_URL + "/user/send_exp";
    public static final String USER_CENTER = BASE_URL + "/auth/user";
    public static final String PRODUCTS_URL = BASE_URL + "/scene_product/getlist";
    public static final String FOCUS_FAVORITE_URL = BASE_URL + "/follow";
    public static final String FOCUS_OPRATE_URL = BASE_URL + "/follow/ajax_follow";
    public static final String CANCEL_FOCUS_URL = BASE_URL + "/follow/ajax_cancel_follow";
    public static final String SUGGESTION_URL = BASE_URL + "/gateway/feedback";
    public static final String UPDATE_USERINFO_URL = BASE_URL + "/my/update_profile";
    public static final String ALL_CITY_URL = BASE_URL + "/shopping/fetch_areas";
    public static final String UPLOAD_IMG_URL = BASE_URL + "/my/upload_token";
    public static final String UPLOAD_BG_URL = BASE_URL + "/my/add_head_pic";
    public static final String SYSTEM_NOTICE = BASE_URL + "/notice/getlist";
    public static final String MESSAGE_RECORD = BASE_URL + "/message";
    public static final String SEND_MESSAGE = BASE_URL + "/message/ajax_message";
    public static final String MESSAGE_DETAIL = BASE_URL + "/message/view";
    public static final String URLSTRING_NOW_CONFIRMORDER = BASE_URL + "/shopping/confirm";
    public static final String URLSTRING_CANCELLOVE = BASE_URL + "/favorite/ajax_cancel_love";
    public static final String URLSTRING_LOVE = BASE_URL + "/favorite/ajax_love";
    public static final String URLSTRING_ADD_TO_CART = BASE_URL + "/shopping/add_cart";
    public static final String URLSTRING_BUY_NOW = BASE_URL + "/shopping/now_buy";
    public static final String UPDATE_USER_IDENTIFY = BASE_URL + "/my/update_user_identify";
    public static final String NOTICE_LIST = BASE_URL + "/remind/getlist";
    public static final String LOGOUT = BASE_URL + "/auth/logout";
    public static final String FIND_FRIENDS = BASE_URL + "/user/find_user";
    public static final String FIU_USER_LIST = BASE_URL + "/user/activity_user";
    public static final String SUBS_CJLIST = BASE_URL + "/my/my_subscription";

    //  检查是否注册
    public static final String GET_REGIST_STATE = BASE_URL + "/auth/check_account";
    public static final String PAY_URL = BASE_URL + "/shopping/payed";
    public static final String CHECK_LOGIN_URL = BASE_URL + "/auth/check_login";
    public static final String UPLOAD_IDENTIFY_URL = BASE_URL + "/my/talent_save";

    //收藏产品和取消收藏
    public static final String FAVORITE_PRODUCT = BASE_URL + "/favorite/ajax_favorite";
    public static final String CANCEL_FAVORITE_PRODUCT = BASE_URL + "/favorite/ajax_cancel_favorite";
    public static final String CHOOSEN_SUBJECT_URL = BASE_URL + "/scene_subject/getlist";

    public static final String SETTINGS_ABOUTUS = H5_URL + "/view/about";

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

    //火鸟联盟
    public static final String ALLIANCE_ACCOUNT = BASE_URL + "/alliance/view";
    public static final String ALLIANCE_BALANCE_LIST = BASE_URL + "/balance/getlist";
    public static final String ALLIANCE_BALANCE_VIEW = BASE_URL + "/balance/view";
    public static final String ALLIANCE_BALANCE_RECORD_LIST = BASE_URL + "/balance_record/getlist";
    public static final String ALLIANCE_BALANCE_RECORD_ITEM = BASE_URL + "/balance_record/item_list";
    public static final String ALLIANCE_BALANCE_WITHDRAW_CASH_LIST = BASE_URL + "/withdraw_cash/getlist";
    public static final String ALLIANCE_BALANCE_WITHDRAW_CASH_VIEW = BASE_URL + "/withdraw_cash/view";
    public static final String ALLIANCE_BALANCE_WITHDRAW_CASH_APPLY_CASH = BASE_URL + "/withdraw_cash/apply_cash";
    public static final String ALLIANCE_BALANCE_PRIVACY_POLICY = BASE_URL + "/alliance/info";

    //地盘详情
    public static final String ZONE_DETAIL = BASE_URL + "/scene_scene/view";
    public static final String ZONE_RELATE_SCENE = BASE_URL + "/scene_sight/getlist";
    public static final String ZONE_RELATE_PRODUCTS = BASE_URL + "/sight_and_product/scene_getlist";

    //发现
    public static final String DISCOVER_URL = BASE_URL+"/gateway/find";
    //企业轻定制
    public static final String COMPANY_DZ_URL = H5_URL+"/storage/custom?from=2";

    //合伙人招募计划
    public static final String COMPANY_PARTNER_URL = H5_URL+"/storage/plan?from=2";

    public static final String SHARE_H5_URL = BASE_URL+"/gateway/share_link";

}
