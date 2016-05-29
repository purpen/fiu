package com.taihuoniao.fineix.network;

import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.user.EditUserInfoActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MD5Utils;

/**
 * Created by android on 2015/12/27.
 * 参数设置
 */
public class ClientDiscoverAPI {
    //产品
    //统计用户想购买的数量
    public static void wantBuy(String id) {
        String url = NetworkConstance.want_buy;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    //产品
    //删除用户添加的产品
    public static void deleteProduct(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.delete_product;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //列表
    public static void getProductList(String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids,
                                      String stick, String fine, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_productsList;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("brand_id", brand_id);
        params.addQueryStringParameter("category_tag_ids", category_tag_ids);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("ids", ids);
        params.addQueryStringParameter("ignore_ids", ignore_ids);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("fine", fine);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //添加产品
    public static void addProduct(String attrbute, String oid, String sku_id, String title, String market_price, String sale_price,
                                  String link, String cover_url, String banners_url, RequestCallBack<String> callBack) {
        String url = NetworkConstance.add_product;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("attrbute", attrbute);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("sku_id", sku_id);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("market_price", market_price);
        params.addQueryStringParameter("sale_price", sale_price);
        params.addQueryStringParameter("link", link);
        params.addQueryStringParameter("cover_url", cover_url);
        params.addQueryStringParameter("banners_url", banners_url);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //产品详情
    public static void goodsDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.good_details;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //自营商品详情
    public static void goodsDetailsNet(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_goods_details;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("goodsDetails", httpHandler);
    }


    //产品
    //获取京东商品信息
    public static void getJDProductsData(String ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_JD_productsData;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //获取淘宝商品详情
    public static void getTBProductsData(String ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_TB_productsData;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //点赞，订阅，收藏，关注列表
    public static void commonList(String page, String size, String id, String user_id, String type, String event, RequestCallBack<String> callBack) {
        String url = NetworkConstance.common_lists;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("user_id", user_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //情景订阅
    public static void subsQingjing(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.subs_qingjing;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //取消情景订阅
    public static void cancelSubsQingjing(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.cancel_subs_qingjing;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //情景新增
    public static void createQingjing(String id, String title, String des, String tags, String address, String tmp, String lat, String lng, RequestCallBack<String> callBack) {
        String url = NetworkConstance.create_qingjing;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("des", des);
        params.addQueryStringParameter("tags", tags);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("lng", lng);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //情景详情
    public static void qingjingDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.qingjing_details;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    //情景
    //列表数据
    public static void qingjingList(String page, String sort, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = NetworkConstance.qingjing_lists;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", 8 + "");
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("dis", dis);
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("lat", lat);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //新增场景
    public static void createScene(String id, String tmp, String title, String des, String scene_id, String tags, String product_id,
                                   String product_title, String product_price, String product_x, String product_y,
                                   String address, String lat, String lng, RequestCallBack<String> callBack) {
        String url = NetworkConstance.create_scene;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("des", des);
        params.addQueryStringParameter("scene_id", scene_id);
        params.addQueryStringParameter("tags", tags);
        params.addQueryStringParameter("product_id", product_id);
        params.addQueryStringParameter("product_title", product_title);
        params.addQueryStringParameter("product_price", product_price);
        params.addQueryStringParameter("product_x", product_x);
        params.addQueryStringParameter("product_y", product_y);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("lng", lng);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //场景点赞
    public static void loveScene(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.love_scene;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //取消场景点赞
    public static void cancelLoveScene(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.cancel_love_scene;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //场景详情
    public static void sceneDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_details;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //列表数据
    public static void getSceneList(String page, String size, String scene_id, String sort, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("scene_id", scene_id);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("dis", dis);
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("lat", lat);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    public static void getSceneList(LatLng ll, String page, String size, String dis, RequestCallBack<String> callBack) {
        getSceneList(page, size, null, null, dis, String.valueOf(ll.longitude), String.valueOf(ll.latitude), callBack);
    }

    /**
     * 根据用户ID查找CJ
     *
     * @param page
     * @param size
     * @param userId
     * @param callBack
     */
    public static void getSceneList(String page, String size, String userId, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("user_id", userId);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //个人中心
    //用户列表
    public static void userList(String page, String size, String sort, String has_scene, RequestCallBack<String> callBack) {
        String url = NetworkConstance.user_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("has_scene", has_scene);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //使用过的标签
    public static void usedLabelList(RequestCallBack<String> callBack) {
        String url = NetworkConstance.used_label_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("type", 1 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //标签列表
    public static void labelList(String parent_id, int page, String size, int sort, int is_hot, RequestCallBack<String> callBack) {
        String url = NetworkConstance.label_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("parent_id", parent_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("sort", sort + "");
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("is_hot", is_hot + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //热门标签
    public static void hotLabelList(String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.hot_label_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //收货地址
    //获取省市列表
    public static void getProvinceList(RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_province_cities;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("getProvinceList", httpHandler);
    }

    //获取用户默认收货地址
    public static void getDefaultAddressNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_default_address;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("getDefaultAddress", httpHandler);
    }

    //收货地址
    //获取用户收货地址列表
    public static void getAddressList(String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_address_lists;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("getAddressList", httpHandler);
    }

    //收货地址
    //提交正在编辑的收货地址
    public static void commitAddressNet(String id, String name, String phone, String province, String city, String address, String zip, String is_default, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_new_address;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("name", name);
        params.addQueryStringParameter("phone", phone);
        params.addQueryStringParameter("province", province);
        params.addQueryStringParameter("city", city);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("zip", zip);
        params.addQueryStringParameter("is_default", is_default);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("commitAddress", httpHandler);
    }

    //收货地址
    //删除某个收货地址
    public static void deleteAddressNet(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_delete_address;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("deleteAddress", httpHandler);
    }

    //公共
    //举报
    public static void report(String target_id, String type, String evt, RequestCallBack<String> callBack) {
        String url = NetworkConstance.report;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("application", 3 + "");
        params.addQueryStringParameter("from_to", 4 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //品牌列表
    public static void brandList(int page, int size, RequestCallBack<String> callBack) {
        String url = NetworkConstance.brand_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("size", size + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //品牌详情
    public static void brandDetail(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.brand_detail;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //分类列表
    public static void categoryList(String page, String domain, RequestCallBack<String> callBack) {
        String url = NetworkConstance.category_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", 30 + "");
        params.addQueryStringParameter("domain", domain);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //搜索列表
    public static void search(String q, String t, String page, String evt, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.search;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("t", t);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("size", 8 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //商品和场景关联列表
    public static void productAndScene(String page, String size, String sight_id, String product_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.product_and_scenelist;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sight_id", sight_id);
        params.addQueryStringParameter("product_id", product_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //购物车
    //商品数量
    public static void cartNum(RequestCallBack<String> callBack) {
        String url = NetworkConstance.cart_number;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //评论
    //提交评论
    public static void sendComment(String target_id, String content, String type, String target_user_id, String is_reply, String reply_id, String reply_user_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.send_comment;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("content", content);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("is_reply", is_reply);
        params.addQueryStringParameter("reply_id", reply_id);
        params.addQueryStringParameter("reply_user_id", reply_user_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("from_site", 4 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //评论
    //列表
    public static void commentsList(String page, String size, String target_id, String target_user_id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.comments_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", "1");
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //评论
    //列表
    public static void mycommentsList(String page, String size, String target_id, String target_user_id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.MY_COMMENTS_LIST;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //评论
    //删除评论
    public static void deleteComment(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.delete_comment;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //评论列表
    //商品详情评论列表
    public static void getGoodsDetailsCommentsList(String target_id, String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.comments_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", "4");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("goodsDetailsCommentsList", httpHandler);
    }

    //分类
    //分类标签
    public static void categoryLabel(String tag_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.category_label;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("tag_id", tag_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //点击注册按钮
    public static void clickRegisterNet(RequestCallBack<String> callBack, String password, String phone, String code) {
        String url = NetworkConstance.BASE_URL + "/auth/register";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("verify_code", code);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("from_to", "2");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack, false);
    }

    //点击登录按钮
    public static void clickLoginNet(String phone, String password, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/login";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("from_to", "2");     //登录渠道
        params.addQueryStringParameter("password", password);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack, false);
    }

    //第三方登录
    public static void thirdLoginNet(String oid, String access_token, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/third_sign";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("from_to", "2");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack, false);
    }

    //注册及找回密码中的获取验证码
    public static void getVerifyCodeNet(RequestCallBack<String> callBack, String phone) {
        String url = NetworkConstance.BASE_URL + "/auth/verify_code";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack, false);
    }

    //第三方登录之快捷注册(绑定手机号)
    public static void bindPhoneNet(String oid, String union_id, String access_token, String account, String password, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/third_register_with_phone";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
////        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("account", account);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("union_id", union_id);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("third_source", type);
        params.addQueryStringParameter("from_to", "2"); //Android
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack, false);
    }

    //第三方登录之快捷注册(不绑定手机号)
    public static void skipBindNet(String oid, String union_id, String access_token, String nickname, String sex, String avatar_url, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/third_register_without_phone";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("nickname", nickname);
        params.addQueryStringParameter("sex", sex);
        params.addQueryStringParameter("oid", oid);
        params.addQueryStringParameter("union_id", union_id);
        params.addQueryStringParameter("avatar_url", avatar_url);
        params.addQueryStringParameter("access_token", access_token);
        params.addQueryStringParameter("third_source", type);
        params.addQueryStringParameter("from_to", "2");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack, false);
    }

    //stick
    public static void getQJData(LatLng ll, int radius, String page, String pageSize, String stick, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("sort", "0");
        params.addQueryStringParameter("stick", stick);
        if (radius > 0) {
            params.addQueryStringParameter("dis", String.valueOf(radius));
        }
        if (ll != null) {
            params.addQueryStringParameter("lat", String.valueOf(ll.latitude));
            params.addQueryStringParameter("lng", String.valueOf(ll.longitude));
        }
//        HttpUtils httpUtils = new HttpUtils(NetworkConstance.CONN_TIMEOUT);
//        HttpHandler<String> handler = httpUtils.send(HttpRequest.HttpMethod.POST, NetworkConstance.QING_JING, params, callBack);
        MD5Utils.sign(params, NetworkConstance.QING_JING, callBack);
    }

    //订阅
    public static void getQJData(String page, String pageSize, String sort, String stick, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("stick", stick);
        MD5Utils.sign(params, NetworkConstance.QING_JING, callBack);
    }

    /**
     * 获取用户的情景列表
     *
     * @param page
     * @param pageSize
     * @param userId
     * @param callBack
     */
    public static void getQJList(String page, String pageSize, String userId, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("sort", "0");
        params.addQueryStringParameter("stick", "0");
        params.addQueryStringParameter("user_id", userId);
        MD5Utils.sign(params, NetworkConstance.QING_JING, callBack);
    }

    //找回忘记的密码
    public static void findPasswordNet(String phone, String newpassword, String code, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/find_pwd";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("password", newpassword);
        params.addQueryStringParameter("verify_code", code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack, false);
    }


    //账户处的用户个人信息
    public static void getMineInfo(String userId, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("user_id", userId);
        LogUtil.e("getMineInfo", userId);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.MINE_INFO, callBack, false);
    }

    //获取个人中心
    public static void getUserCenterData(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_CENTER, callBack, false);
    }

    /**
     * 获取Banner
     *
     * @param page_name
     * @param callBack
     */
    public static void getBanners(String page_name, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("page", String.valueOf(1));
        params.addBodyParameter("size", String.valueOf(10));
        params.addBodyParameter("name", page_name);
        MD5Utils.sign(params, NetworkConstance.BANNERS_URL, callBack, false);
    }

    /**
     * 获取产品列表
     *
     * @param page
     * @param callBack
     */
    public static void getProductList(String page, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("page", String.valueOf(page));
        params.addBodyParameter("size", String.valueOf(10));
//        params.addBodyParameter("state",String.valueOf(1));
        MD5Utils.sign(params, NetworkConstance.PRODUCTS_URL, callBack, false);
    }

    /**
     * 获取粉丝和可关注列表
     *
     * @param page
     * @param size
     * @param find_type
     * @param callBack
     */
    public static void getFocusFansList(String userId, String page, String size, String find_type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("page", page);
        params.addBodyParameter("size", size);
//        params.addBodyParameter("user_id", LoginInfo.getInstance().getId()+"");
        params.addBodyParameter("user_id", userId);//924808
//        LogUtil.e("userId",LoginInfo.getInstance().getId()+"");
        params.addBodyParameter("find_type", find_type);
        MD5Utils.sign(params, NetworkConstance.FOCUS_FAVORITE_URL, callBack, false);
    }

    /**
     * 关注操作
     *
     * @param follow_id
     * @param callBack
     */
    public static void focusOperate(String follow_id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("follow_id", follow_id);
        MD5Utils.sign(params, NetworkConstance.FOCUS_OPRATE_URL, callBack, false);
    }

    /**
     * 取消关注
     *
     * @param follow_id
     * @param callBack
     */
    public static void cancelFocusOperate(String follow_id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("follow_id", follow_id);
        MD5Utils.sign(params, NetworkConstance.CANCEL_FOCUS_URL, callBack, false);
    }

    /**
     * 意见反馈
     *
     * @param content
     * @param contact
     * @param callBack
     */
    public static void commitSuggestion(String content, String contact, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("content", content);
        params.addQueryStringParameter("contact", contact);
        params.addQueryStringParameter("from_to", "2"); //1.ios;2.android;3.ipad;4.win;5.web;6.wap
        params.addQueryStringParameter("kind", "3");   //1.网页版; 2.商城app; 3.Fiu
        MD5Utils.sign(params, NetworkConstance.SUGGESTION_URL, callBack, false);
    }

    /**
     * 更新用户信息
     *
     * @param key
     * @param value
     * @param callBack
     */
    public static void updateUserInfo(String key, String value, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        if (EditUserInfoActivity.isSubmitAddress) {
            params.addBodyParameter("province_id", key);
            params.addBodyParameter("district_id", value);
        } else {
            params.addQueryStringParameter(key, value);
        }
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 更新用户信息
     *
     * @param nickname
     * @param summary
     * @param callBack
     */
    public static void updateNickNameSummary(String nickname, String summary, String sex, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("nickname", nickname);
        params.addQueryStringParameter("summary", summary);
        params.addQueryStringParameter("sex", sex);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 更新签名和label
     * @param label
     * @param summary
     * @param callBack
     */
    public static void updateSignatrueLabel(String label, String summary,RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("label", label);
        params.addQueryStringParameter("summary", summary);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 获取所有城市
     *
     * @param callBack
     */
    public static void getAllCities(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        MD5Utils.sign(params, NetworkConstance.ALL_CITY_URL, callBack, false);
    }

    /**
     * 上传头像
     *
     * @param callBack
     */
    public static void uploadImg(String tmp, String type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("tmp", tmp);
        params.addBodyParameter("type", type);
        MD5Utils.sign(params, NetworkConstance.UPLOAD_IMG_URL, callBack, false);
    }

    /**
     * 上传个人中心背景
     *
     * @param callBack
     */
    public static void uploadBgImg(String tmp, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("tmp", tmp);
        HttpHandler<String> handler = MD5Utils.sign(params, NetworkConstance.UPLOAD_BG_URL, callBack, false);
        NetworkManager.getInstance().add(NetworkConstance.UPLOAD_BG_URL, handler);
    }

    /**
     * 获取系统通知
     *
     * @param page
     * @param pageSize
     * @param callBack
     */
    public static void getSystemNotice(String page, String pageSize, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("page", page);
        params.addBodyParameter("size", pageSize);
        MD5Utils.sign(params, NetworkConstance.SYSTEM_NOTICE, callBack, false);
    }

    /**
     * 获取聊天记录
     *
     * @param page
     * @param pageSize
     * @param type
     * @param callBack
     */
    public static void getPrivateMessageList(String page, String pageSize, String type, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("page", page);
        params.addBodyParameter("size", pageSize);
        params.addBodyParameter("type", type);
        MD5Utils.sign(params, NetworkConstance.MESSAGE_RECORD, callBack, false);
    }

    /**
     * 发送消息
     *
     * @param to_user_id
     * @param content
     * @param callBack
     */
    public static void sendMessage(String to_user_id, String content, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("to_user_id", to_user_id);
        params.addBodyParameter("content", content);
        MD5Utils.sign(params, NetworkConstance.SEND_MESSAGE, callBack, false);
    }

    /**
     * 私信详情
     *
     * @param to_user_id
     * @param callBack
     */
    public static void messageDetailList(String to_user_id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("to_user_id", to_user_id);
        MD5Utils.sign(params, NetworkConstance.MESSAGE_DETAIL, callBack, false);
    }

    //验证红包是否可用
    public static void checkRedBagUsableNet(String rid, String code, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/use_bonus";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
//        params.addQueryStringParameter("uuid", uuid);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("code", code);
        MD5Utils.sign(params, url, callBack, false);
    }


    //红包
    public static void myRedBagNet(String page, String size, String used, String time, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/my/bonus";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("used", used);
        params.addQueryStringParameter("is_expired", time);
        MD5Utils.sign(params, url, callBack, false);
    }

    /**
     * 修改密码
     *
     * @param password
     * @param new_password
     * @param callBack
     */
    public static void updatePassword(String password, String new_password, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/my/modify_password";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("new_password", new_password);
        MD5Utils.sign(params, url, callBack, false);
    }

    //立即下单
    public static void nowConfirmOrder(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_now_confirmorder;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rrid", rrid);
        params.addQueryStringParameter("from_site", "8");
        params.addQueryStringParameter("addbook_id", addbook_id);
        params.addQueryStringParameter("is_nowbuy", is_nowbuy);
        params.addQueryStringParameter("summary", summary);
        params.addQueryStringParameter("transfer_time", transfer_time);
        params.addQueryStringParameter("bonus_code", bonus_code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("nowConfirmOrder", httpHandler);
    }

    //购物车
    public static void shopCartNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/fetch_cart";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //购物车数量
    public static void shopCartNumberNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/fetch_cart_count";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //购物车结算下单
    public static void calculateShopCartNet(String array, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/checkout";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("array", array);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //删除购物车
    public static void deletShopCartNet(String array, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/remove_cart";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("array", array);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //取消点赞
    public static void cancelLoveNet(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_cancellove;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("cancelLove", httpHandler);
    }

    //点赞
    public static void loveNet(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_love;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("love", httpHandler);
    }

    //添加购物车
    public static void addToCartNet(String target_id, String type, String n, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_add_to_cart;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("n", n);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("addToCart", httpHandler);
    }

    //立即购买(验证数据并会生成临时订单)
    public static void buyNow(String target_id, String type, String n, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_buy_now;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("n", n);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        NetworkManager.getInstance().add("buyNow", httpHandler);
    }

    //删除订单/my/delete_order
    public static void deleteOrderNet(String rid, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/my/delete_order";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //取消订单
    public static void cancelOrderNet(String rid, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/my/cancel_order";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //订单支付详情和订单详情都是这,发表评价界面的产品图片也从这获取
    public static void OrderPayNet(String rid, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/detail";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //申请退款
    public static void applyForRefundNet(String rid, String option, String content, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/apply_refund";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("option", option);
        params.addQueryStringParameter("content", content);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //确认收货
    public static void confirmReceiveNet(String rid, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/take_delivery";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //发表评价
    public static void publishEvaluateNet(String rid, String array, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/product/ajax_comment";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("array", array);
        params.addQueryStringParameter("from_site", "4");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //账户处的订单列表
    public static void orderListNet(String status, String page, String size, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/orders";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("status", status);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //账户处的用户个人信息
    public static void userInfoNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/user";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //购物车中单个商品的库存（即最大加减数）
    public static void shopcartInventoryNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/fetch_cart_product_count";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //购物车中商品加减数量
    public static void shopcartAddSubtractNet(String array, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/edit_cart";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("array", array);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //最fiu伙伴
    public static void fiuUserList(String page, String size, String type, String sight_count, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.fiu_user_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sight_count", sight_count);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //首页订阅的情景下的场景和关注的人所创建的场景列表
    public static void subsCJList(String page, String size, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.subs_cjlist;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 更新用户是否首次登录的标识
     *
     * @param type
     * @param callBack
     */
    public static void updateUserIdentify(String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.UPDATE_USER_IDENTIFY;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 获得提醒列表
     *
     * @param page
     * @param size
     * @param type
     * @param callBack
     */
    public static void getNoticeList(String page, String size, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.NOTICE_LIST;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    /**
     * 发现好友列表
     *
     * @param callBack
     */
    public static void findFriends(String page, String size, String sight_count, String sort, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sight_count", sight_count); //场景数量
        params.addQueryStringParameter("sort", sort); //0是最新 1是随机
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.FIND_FRIENDS, callBack);
    }

    /**
     * 退出登录
     *
     * @param callBack
     */
    public static void logout(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("uuid", MainApplication.uuid);
        params.addQueryStringParameter("from_to", "2"); // 1.ios;2.android;3.win;4.ipad;
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.LOGOUT, callBack);
    }

    /**
     * 获取手机状态
     *
     * @param account
     * @param callBack
     */
    public static void getPhoneState(String account, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("account", account);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.GET_REGIST_STATE, callBack);
    }

    /**
     * 获取支付参数
     *
     * @param rid
     * @param payaway
     * @param callBack
     */
    public static void getPayParams(String rid, String payaway, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("payaway", payaway);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.PAY_URL, callBack, true);
    }

    /**
     * 判断服务器是否登录
     */
    public static void getLoginStatus(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.CHECK_LOGIN_URL, callBack);
    }


    public static void uploadIdentityInfo(String info, String label, String contact, String id_card_a_tmp, String business_card_tmp, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("info", info);
        params.addQueryStringParameter("label", label);
        params.addQueryStringParameter("contact", contact);
        params.addQueryStringParameter("id_card_a_tmp", id_card_a_tmp);
        params.addQueryStringParameter("business_card_tmp", business_card_tmp);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.UPLOAD_IDENTIFY_URL, callBack);
        NetworkManager.getInstance().add(NetworkConstance.UPLOAD_IDENTIFY_URL, httpHandler);
    }

    public static void tixingFahuo(String rid, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/alert_send_goods";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }
    //分享场景语境次数
    public static void commitShareCJ(String id,RequestCallBack<String> callBack){
        String url = NetworkConstance.BASE_URL+"/scene_sight/add_share_context_num";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id",id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    public static void getAuthStatus(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL+"/my/fetch_talent";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params,url, callBack);
    }
}
