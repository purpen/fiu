package com.taihuoniao.fineix.network;

/**
 * Created by android on 2015/12/27
 * 网络请求url封装
 */
public class NetworkConstance {
    public static final String ALI_PAY = "alipay";
    public static final String WX_PAY = "weichat";
    public static final String JD_PAY = "jdpay";
            public static final String BASE_URL = "http://api.taihuoniao.com/app/api"; //线上
//    public static final String BASE_URL = "http://m.taihuoniao.com/app/api";//生产环境
//    public static final String BASE_URL = "http://t.taihuoniao.com/app/api";  // 测试环境

    //热门城市 暂用ESTORE的URL
    public static final String BASE_URL_ESTORE = BASE_URL + "/estore/";

    public static final String CHARSET = "utf-8";
    public static final int CONN_TIMEOUT = 60000;
    public static final String STATUS_NEED_LOGIN = "4008";
    //产品
    //统计用户想购买的数量
    public static final String want_buy = BASE_URL + "/scene_product/sight_click_stat";
    //删除用户添加的产品
    public static final String delete_product = BASE_URL + "/product/deleted";
    //列表
    public static final String urlString_productsList = BASE_URL + "/product/getlist";
    //添加产品
    public static final String add_product = BASE_URL + "/product/submit";
    //产品详情
    public static final String good_details = BASE_URL + "/product/view";
    //获取京东商品信息
    public static final String urlString_JD_productsData = BASE_URL + "/scene_product/jd_view";
    //获取淘宝商品信息
    public static final String urlString_TB_productsData = BASE_URL + "/scene_product/tb_view";
    //情景
    //点赞，订阅，关注，收藏通用列表
    public static final String common_lists = BASE_URL + "/favorite";
    //订阅情景
    public static final String subs_qingjing = BASE_URL + "/favorite/ajax_subscription";
    //取消订阅情景
    public static final String cancel_subs_qingjing = BASE_URL + "/favorite/ajax_cancel_subscription";
    //情景新增
    public static final String create_qingjing = BASE_URL + "/scene_scene/save";
    //删除情景
    public static final String delete_qingjing = BASE_URL + "/scene_scene/delete";
    //情景详情
    public static final String qingjing_details = BASE_URL + "/scene_scene/view";
    //情景列表
    public static final String qingjing_lists = BASE_URL + "/scene_scene/";
    //场景
    //新增场景
    public static final String create_scene = BASE_URL + "/scene_sight/save";
    //删除场景
    public static final String delete_scene = BASE_URL + "/scene_sight/delete";
    //场景点赞
    public static final String love_scene = BASE_URL + "/favorite/ajax_sight_love";
    //取消场景点赞
    public static final String cancel_love_scene = BASE_URL + "/favorite/ajax_cancel_sight_love";
    //场景详情
    public static final String scene_details = BASE_URL + "/scene_sight/view";
    //列表数据
    public static final String scene_list = BASE_URL + "/scene_sight";
    //个人中心
    //用户列表
    public static final String user_list = BASE_URL + "/user/";
    //标签
    //最近使用的标签
    public static final String used_label_list = BASE_URL + "/my/my_recent_tags";
    //标签列表
    public static final String label_list = BASE_URL + "/scene_tags/getlist";
    //场景页热门标签
    public static final String cj_hot_label = BASE_URL + "/gateway/get_fiu_hot_sight_tags";
    //产品页热门标签
    public static final String product_hot_label = BASE_URL + "/gateway/get_fiu_hot_product_tags";
    //热门标签
    public static final String hot_label_list = BASE_URL + "/scene_tags/hotlist";
    //收货地址
    //省市列表
    public static final String urlString_province_cities = BASE_URL + "/shopping/fetch_areas";
    //  获取用户默认收货地址接口
    public static final String urlString_default_address = BASE_URL + "/shopping/default_address";
    //  获取用户收货地址列表
    public static final String urlString_address_lists = BASE_URL + "/shopping/address";
    //  新增收货地址(编辑)
    public static final String urlString_new_address = BASE_URL + "/shopping/ajax_address";
    //  删除某个收货地址
    public static final String urlString_delete_address = BASE_URL + "/shopping/remove_address";
    //公共
    //举报
    public static final String report = BASE_URL + "/report_tip/save";
    //品牌列表
    public static final String brand_list = BASE_URL + "/scene_brands/getlist";
    //品牌详情
    public static final String brand_detail = BASE_URL + "/scene_brands/view";
    //分类列表
    public static final String category_list = BASE_URL + "/category/getlist";
    //搜索列表
    public static final String search = BASE_URL + "/search/getlist";
    //商品和场景关联列表
    public static final String product_and_scenelist = BASE_URL + "/sight_and_product/getlist";
    //购物车
    //购物车数量
    public static final String cart_number = BASE_URL + "/shopping/fetch_cart_count";
    //评论
    //提交评论
    public static final String send_comment = BASE_URL + "/comment/ajax_comment";
    //列表
    public static final String comments_list = BASE_URL + "/comment/getlist";
    //删除评论
    public static final String delete_comment = BASE_URL + "/comment/deleted";
    //我接收到的评论列表
    public static final String MY_COMMENTS_LIST = BASE_URL + "/my/comment_list";
    //分类
    //分类标签
    public static final String category_label = BASE_URL + "/category/fetch_child_tags";
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
    public static final String urlString_now_confirmorder = BASE_URL + "/shopping/confirm";

    //  取消点赞
    public static final String urlString_cancellove = BASE_URL + "/favorite/ajax_cancel_love";

    //  点赞接口
    public static final String urlString_love = BASE_URL + "/favorite/ajax_love";
    //  添加购物车
    public static final String urlString_add_to_cart = BASE_URL + "/shopping/add_cart";
    //  立即购买
    public static final String urlString_buy_now = BASE_URL + "/shopping/now_buy";

    //  更新是否首次登录标识
    public static final String UPDATE_USER_IDENTIFY = BASE_URL + "/my/update_user_identify";

    //  提醒
    public static final String NOTICE_LIST = BASE_URL + "/remind/getlist";

    //  退出登录
    public static final String LOGOUT = BASE_URL + "/auth/logout";

    //  发现好友
    public static final String FIND_FRIENDS = BASE_URL + "/user/find_user";
    //最fiu伙伴
    public static final String fiu_user_list = BASE_URL + "/user/activity_user";
    //首页订阅的场景列表
    public static final String subs_cjlist = BASE_URL + "/my/my_subscription";
    //  检查是否注册
    public static final String GET_REGIST_STATE = BASE_URL + "/auth/check_account";

    //支付URL
    public static final String PAY_URL = BASE_URL + "/shopping/payed";

    //检查登录状态
    public static final String CHECK_LOGIN_URL = BASE_URL + "/auth/check_login";

    //上传官方认证信息
    public static final String UPLOAD_IDENTIFY_URL = BASE_URL + "/my/talent_save";
    //收藏产品和取消收藏
    public static final String favorite_product = BASE_URL + "/favorite/ajax_favorite";
    public static final String cancel_favorite_product = BASE_URL + "/favorite/ajax_cancel_favorite";
}
