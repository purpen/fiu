package com.taihuoniao.fineix.network;

/**
 * Created by android on 2015/12/27.
 * 网络请求url封装
 */
public class NetworkConstance {
    public static final String BASE_URL = "http://t.taihuoniao.com/app/api";

    //热门城市 暂用ESTORE的URL
    public static final String BASE_URL_ESTORE = "http://t.taihuoniao.com/app/api/estore/";

    public static final String CHARSET = "utf-8";
    public static final int CONN_TIMEOUT = 60000;
    //产品
    //列表
    public static final String urlString_productsList = BASE_URL + "/scene_product/getlist";
    //获取京东商品信息
    public static final String urlString_JD_productsData = BASE_URL + "/scene_product/jd_view";
    //获取淘宝商品信息
    public static final String urlString_TB_productsData = BASE_URL + "/scene_product/tb_view";
    //情景
    //点赞，订阅，关注，收藏通用列表
    public static final String common_lists = BASE_URL + "/favorite";
    //情景列表
    public static final String qingjing_lists = BASE_URL + "/scene_scene/";
    //场景
    //新增场景
    public static final String create_scene = BASE_URL + "/scene_sight/save";
    //场景详情
    public static final String scene_details = BASE_URL + "/scene_sight/view";
    //列表数据
    public static final String scene_list = BASE_URL + "/scene_sight";
    //标签
    //最近使用的标签
    public static final String used_label_list = BASE_URL + "/my/my_recent_tags";
    //标签列表
    public static final String label_list = BASE_URL + "/scene_tags/getlist";
    //热门标签
    public static final String hot_label_list = BASE_URL + "/scene_tags/hotlist";
    //公共
    //分类列表
    public static final String category_list = BASE_URL + "/category/getlist";
    //评论
    //提交评论
    public static final String send_comment = BASE_URL + "/comment/ajax_comment";
    //列表
    public static final String comments_list = BASE_URL + "/comment/getlist";

    //热门城市
    public static final String HOT_CITIES = BASE_URL_ESTORE + "/get_city_list";
    //情景列表
    public static final String QING_JING = BASE_URL + "/scene_scene/";
    //场景列表
    public static final String  CHANG_JING= BASE_URL + "/scene_sight";

    //获得用户信息
    public static final String MINE_INFO = BASE_URL + "/auth/user";

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
}
