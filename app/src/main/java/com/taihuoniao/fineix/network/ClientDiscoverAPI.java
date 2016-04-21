package com.taihuoniao.fineix.network;

import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.taihuoniao.fineix.utils.MD5Utils;

/**
 * Created by android on 2015/12/27.
 * 参数设置
 */
public class ClientDiscoverAPI {
    //产品
    //列表
    public static void getProductList(String category, String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_productsList;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("category", category);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
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
    public static void commonList(String page, String size, String id, String type, String event, RequestCallBack<String> callBack) {
        String url = NetworkConstance.common_lists;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //情景
    //列表数据
    public static void qingjingList(String page, String stick, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = NetworkConstance.qingjing_lists;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page",page);
        params.addQueryStringParameter("stick",stick);
        params.addQueryStringParameter("dis",dis);
        params.addQueryStringParameter("lng",lng);
        params.addQueryStringParameter("lat",lat);
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
    //场景详情
    public static void sceneDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_details;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //列表数据
    public static void getSceneList(String page, String size, String stick, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("dis", dis);
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("lat", lat);
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
    public static void labelList(String parent_id, int page, String size, RequestCallBack<String> callBack) {
        String url = NetworkConstance.label_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("parent_id", parent_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("size", size);
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

    //公共
    //分类列表
    public static void categoryList(String page, String domin, RequestCallBack<String> callBack) {
        String url = NetworkConstance.category_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("domin", domin);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //评论
    //列表
    public static void commentsList(String page, String target_id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.comments_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
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
    public static void clickLoginNet(String uuid, String phone, String password, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/login";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
//        params.addQueryStringParameter();("mobile", phone);
//        params.addQueryStringParameter();("from_to", "2");
//        params.addQueryStringParameter();("password", password);
//        params.addQueryStringParameter();("uuid", uuid);
//        HttpUtils httpUtils = new HttpUtils(NetworkConstance.CONN_TIMEOUT);
//        HttpHandler<String> handler = httpUtils.send(HttpRequest.HttpMethod.POST, url, params, callBack);
        params.addQueryStringParameter("mobile", phone);
        params.addQueryStringParameter("from_to", "2");
        params.addQueryStringParameter("password", password);
////        params.addQueryStringParameter("uuid", uuid);
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
        params.addQueryStringParameter("from_to", "2");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack, false);
    }

    //第三方登录之快捷注册(不绑定手机号)
    public static void skipBindNet(String uuid, String oid, String union_id, String access_token, String nickname, String sex, String avatar_url, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/third_register_without_phone";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
////        params.addQueryStringParameter("uuid", uuid);
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

    public static void getNearByQJData(LatLng ll, int radius, int page, int pageSize, int stick, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addQueryStringParameter("size", String.valueOf(pageSize));
        params.addQueryStringParameter("sort", "0");
        params.addQueryStringParameter("stick", String.valueOf(stick));
        if (radius > 0) {
            params.addQueryStringParameter("dis", String.valueOf(radius));
        }
        if (ll != null) {
            params.addQueryStringParameter("lat", String.valueOf(ll.latitude));
            params.addQueryStringParameter("lng", String.valueOf(ll.longitude));
        }
        HttpUtils httpUtils = new HttpUtils(NetworkConstance.CONN_TIMEOUT);
        HttpHandler<String> handler = httpUtils.send(HttpRequest.HttpMethod.POST, NetworkConstance.QING_JING, params, callBack);
//        MD5Utils.sign(params,NetworkConstance.QING_JING, callBack);
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
    public static void getMineInfo(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.MINE_INFO, callBack, false);
    }

    /**
     * 获取Banner
     * @param page_name
     * @param callBack
     */
    public static void getBanners(String page_name, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("page",String.valueOf(1));
        params.addBodyParameter("size",String.valueOf(10));
        params.addBodyParameter("name",page_name);
        MD5Utils.sign(params, NetworkConstance.BANNERS_URL, callBack, false);
    }

}
