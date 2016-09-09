package com.taihuoniao.fineix.network;

import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.user.EditUserInfoActivity;
import com.taihuoniao.fineix.utils.Constants;
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
    public static void deleteProduct(String id) {
        deleteProduct(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    public static void deleteProduct(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.delete_product;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //列表
    public static HttpHandler<String> getProductList(String title, String sort, String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids,
                                                     String stick, String fine, String stage, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_productsList;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("brand_id", brand_id);
        params.addQueryStringParameter("category_tags", category_tag_ids);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("ids", ids);
        params.addQueryStringParameter("ignore_ids", ignore_ids);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("stage", stage);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
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
    //添加产品
    public static void addProduct(String title, String brand_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.add_product;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("brand_id", brand_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //产品详情
    public static HttpHandler<String> goodsDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.good_details;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
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


    /**
     * 赞过的情境
     *
     * @param page
     * @param type
     * @param event
     * @param callBack
     */
    public static void getSupportQJ(String page, String type, String event, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/favorite/get_new_list";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("user_id", LoginInfo.getUserId() + "");
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
    //删除情景
    public static void deleteQingjing(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.delete_qingjing;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
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
    public static void qingjingList(String page, String category_id, String sort, String fine, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = NetworkConstance.qingjing_lists;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("size", 10 + "");
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("dis", 10000 + "");
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("lat", lat);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //新增情景
    public static void createScene(String id, String title, String des, String scene_id, String tags,
                                   String products, String address, String city, String tmp, String lat, String lng,
                                   String subject_ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.create_scene;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("des", des);
        params.addQueryStringParameter("city", city);
        params.addQueryStringParameter("scene_id", scene_id);
        params.addQueryStringParameter("tags", tags);
        params.addQueryStringParameter("products", products);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("subject_ids", subject_ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //删除场景
    public static HttpHandler<String> deleteScene(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.delete_scene;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //情境
    //情境点赞
    public static HttpHandler<String> loveQJ(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.love_scene;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //情境
    //取消情境点赞
    public static HttpHandler<String> cancelLoveQJ(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.cancel_love_scene;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //场景
    //场景详情
    public static HttpHandler<String> sceneDetails(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_details;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //场景
    //列表数据
    public static HttpHandler<String> getSceneList(String page, String size, String scene_id, String category_ids, String sort, String fine, String dis, String lng, String lat, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("scene_id", scene_id);
        params.addQueryStringParameter("category_ids", category_ids);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("dis", 10000 + "");
        params.addQueryStringParameter("lng", lng);
        params.addQueryStringParameter("lat", lat);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    public static void getSceneList(LatLng ll, String page, String size, String dis, RequestCallBack<String> callBack) {
        getSceneList(page, size, null, null, null, null, dis, String.valueOf(ll.longitude), String.valueOf(ll.latitude), callBack);
    }


    /**
     * 获取订阅情境的列表
     *
     * @param page
     * @param category_ids
     * @param callBack
     */
    public static void getQJList(String page, String category_ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("category_ids", category_ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 参与的情境
     *
     * @param subject_id
     * @param callBack
     */
    public static void participateActivity(String page, String subject_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.scene_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        params.addQueryStringParameter("subject_id", subject_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
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
    //场景页热门标签
    public static void cjHotLabel(boolean isCJ, RequestCallBack<String> callBack) {
        String url;
        if (isCJ) {
            url = NetworkConstance.cj_hot_label;
        } else {
            url = NetworkConstance.product_hot_label;
        }
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
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
    public static HttpHandler<String> getProvinceList(RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_province_cities;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //获取用户默认收货地址
    public static HttpHandler<String> getDefaultAddressNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_default_address;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //收货地址
    //获取用户收货地址列表
    public static void getAddressList(String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_address_lists;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
//        NetworkManager.getInstance().add("getAddressList", httpHandler);
    }

    //收货地址
    //提交正在编辑的收货地址
    public static HttpHandler<String> commitAddressNet(String id, String name, String phone, String province, String city, String address, String zip, String is_default, RequestCallBack<String> callBack) {
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
        return httpHandler;
    }

    //收货地址
    //删除某个收货地址
    public static void deleteAddressNet(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_delete_address;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
//        NetworkManager.getInstance().add("deleteAddress", httpHandler);
    }

    //公共
    //举报
    public static HttpHandler<String> report(String target_id, String type, String evt, RequestCallBack<String> callBack) {
        String url = NetworkConstance.report;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("application", 3 + "");
        params.addQueryStringParameter("from_to", 4 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //公共
    //品牌列表
    public static void brandList(int page, int size, String mark, String self_run, String stick, String title, RequestCallBack<String> callBack) {
        String url = NetworkConstance.brand_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("size", size + "");
        params.addQueryStringParameter("kind", 1 + "");
        params.addQueryStringParameter("mark", mark);
        params.addQueryStringParameter("self_run", self_run);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("title", title);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //品牌详情
    public static HttpHandler<String> brandDetail(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.brand_detail;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //公共
    //分类列表
    public static void categoryList(String page, String domain, String show_all, RequestCallBack<String> callBack) {
        String url = NetworkConstance.category_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", 300 + "");
        params.addQueryStringParameter("show_all", show_all);
        params.addQueryStringParameter("domain", domain);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    /**
     * 获得情景主题
     *
     * @param callBack
     */
    public static void categoryList(RequestCallBack<String> callBack) {
        String url = NetworkConstance.category_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("size", "10");
        params.addQueryStringParameter("domain", "13");//情景主题
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //搜索列表
    public static void search(String q, String t, String cid, String page, String size, String evt, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.search;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("t", t);
        params.addQueryStringParameter("tid", cid);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("size", size);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    public static void searchUser(String q, String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.search;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("t", "14"); //14.用户
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //商品和场景关联列表
    public static HttpHandler<String> productAndScene(String page, String size, String sight_id, String product_id, String brand_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.product_and_scenelist;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sight_id", sight_id);
        params.addQueryStringParameter("product_id", product_id);
        params.addQueryStringParameter("brand_id", brand_id);
        return MD5Utils.sign(params, url, callBack);
    }

    //购物车
    //商品数量
    public static HttpHandler<String> cartNum(RequestCallBack<String> callBack) {
        String url = NetworkConstance.cart_number;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //评论
    //提交评论
    public static HttpHandler<String> sendComment(String target_id, String content, String type, String target_user_id, String is_reply, String reply_id, String reply_user_id, RequestCallBack<String> callBack) {
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
        return httpHandler;
    }

    //评论
    //列表
    public static HttpHandler<String> commentsList(String page, String size, String target_id, String target_user_id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.comments_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", "1");
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("target_user_id", target_user_id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
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
    public static HttpHandler<String> deleteComment(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.delete_comment;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //评论列表
    //商品详情评论列表
    public static HttpHandler<String> getGoodsDetailsCommentsList(String target_id, String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.comments_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", "4");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
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
    public static HttpHandler<String> getUserCenterData(RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, NetworkConstance.USER_CENTER, callBack, false);
        return httpHandler;
    }

    /**
     * 获取Banner
     *
     * @param page_name
     * @param callBack
     */
    public static void getBanners(String page_name, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", String.valueOf(1));
        params.addQueryStringParameter("size", String.valueOf(5));
        params.addQueryStringParameter("name", page_name);
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
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addQueryStringParameter("size", String.valueOf(10));
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
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
//        params.addBodyParameter("user_id", LoginInfo.getInstance().getId()+"");
        params.addQueryStringParameter("user_id", userId);//924808
//        LogUtil.e("userId",LoginInfo.getInstance().getId()+"");
        params.addQueryStringParameter("find_type", find_type);
        MD5Utils.sign(params, NetworkConstance.FOCUS_FAVORITE_URL, callBack, false);
    }

    /**
     * 关注操作
     *
     * @param follow_id
     * @param callBack
     */
    public static HttpHandler<String> focusOperate(String follow_id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("follow_id", follow_id);
        return MD5Utils.sign(params, NetworkConstance.FOCUS_OPRATE_URL, callBack, false);
    }

    /**
     * 取消关注
     *
     * @param follow_id
     * @param callBack
     */
    public static HttpHandler<String> cancelFocusOperate(String follow_id, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("follow_id", follow_id);
        return MD5Utils.sign(params, NetworkConstance.CANCEL_FOCUS_URL, callBack, false);
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
            params.addQueryStringParameter("province_id", key);
            params.addQueryStringParameter("district_id", value);
        } else {
            params.addQueryStringParameter(key, value);
        }
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 更新用户信息
     *
     * @param nickname
     * @param sex
     * @param callBack
     */
    public static void updateNickNameSex(String nickname, String sex, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("nickname", nickname);
        params.addQueryStringParameter("sex", sex);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 更新用户信息
     *
     * @param age_group
     * @param assets
     * @param callBack
     */
    public static void updateAgeAssets(String age_group, String assets, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("age_group", age_group);
        params.addQueryStringParameter("assets", assets);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 提交订阅的主题
     *
     * @param interest_scene_cate
     * @param callBack
     */
    public static void subscribeTheme(String interest_scene_cate, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("interest_scene_cate", interest_scene_cate);
        MD5Utils.sign(params, NetworkConstance.UPDATE_USERINFO_URL, callBack, false);
    }

    /**
     * 更新签名和label
     *
     * @param label
     * @param summary
     * @param callBack
     */
    public static void updateSignatrueLabel(String label, String summary, RequestCallBack<String> callBack) {
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
        params.addQueryStringParameter("tmp", tmp);
        params.addQueryStringParameter("type", type);
        MD5Utils.sign(params, NetworkConstance.UPLOAD_IMG_URL, callBack, false);
    }

    /**
     * 上传个人中心背景
     *
     * @param callBack
     */
    public static HttpHandler<String> uploadBgImg(String tmp, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("tmp", tmp);
        HttpHandler<String> handler = MD5Utils.sign(params, NetworkConstance.UPLOAD_BG_URL, callBack, false);
        NetworkManager.getInstance().add(NetworkConstance.UPLOAD_BG_URL, handler);
        return handler;
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
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
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
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", pageSize);
        params.addQueryStringParameter("type", type);
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
        params.addQueryStringParameter("to_user_id", to_user_id);
        params.addQueryStringParameter("content", content);
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
        params.addQueryStringParameter("to_user_id", to_user_id);
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
    public static HttpHandler<String> nowConfirmOrder(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code, RequestCallBack<String> callBack) {
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
//        NetworkManager.getInstance().add("nowConfirmOrder", httpHandler);
        return httpHandler;
    }

    //购物车
    public static HttpHandler<String> shopCartNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/fetch_cart";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //购物车数量
    public static HttpHandler<String> shopCartNumberNet(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/fetch_cart_count";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
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
    public static HttpHandler<String> cancelLoveNet(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_cancellove;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //点赞
    public static HttpHandler<String> loveNet(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_love;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //添加购物车
    public static HttpHandler<String> addToCartNet(String target_id, String type, String n, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_add_to_cart;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("n", n);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //立即购买(验证数据并会生成临时订单)
    public static HttpHandler<String> buyNow(String target_id, String type, String n, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_buy_now;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("target_id", target_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("n", n);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
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
    public static HttpHandler<String> OrderPayNet(String rid, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/detail";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //申请退款
    public static HttpHandler<String> applyForRefundNet(String rid, String option, String content, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/shopping/apply_refund";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("rid", rid);
        params.addQueryStringParameter("option", option);
        params.addQueryStringParameter("content", content);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
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
    public static HttpHandler<String> fiuUserList(String page, String size, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.fiu_user_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
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
        params.addQueryStringParameter("type", "1"); //过滤已关注
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


    public static void uploadIdentityInfo(String id, String info, String label, String contact, String id_card_a_tmp, String business_card_tmp, RequestCallBack<String> callBack) {
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
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
    public static void commitShareCJ(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/scene_sight/add_share_context_num";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 获得认证信息
     *
     * @param callBack
     */
    public static void getAuthStatus(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/my/fetch_talent";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //送积分
    public static HttpHandler<String> getBonus(String type, String evt, String target_id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.GET_BONUS;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("evt", evt);
        params.addQueryStringParameter("target_id", target_id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    /**
     * 获取专题信息
     *
     * @param id
     * @param callBack
     */
    public static void getSubjectData(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/scene_subject/view";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    //收藏产品和取消收藏
    public static void favorite(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.favorite_product;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    public static void cancelFavorite(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.cancel_favorite_product;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    public static void isInvited(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/gateway/is_invited";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


    public static void submitInviteCode(String code, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/gateway/valide_invite_code";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("code", code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    public static void updateInviteCodeStatus(String code, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/gateway/del_invite_code";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("code", code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * @param page
     * @param size
     * @param type
     * @param event
     * @param callBack
     */
    public static void getCollectOrdered(String page, String size, String type, String event, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/favorite/get_new_list";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("user_id", String.valueOf(LoginInfo.getUserId()));
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("event", event);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //创建情景时，根据标题和描述搜索标签
    public static void searchLabel(String title, String content, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/gateway/fetch_chinese_word";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("size", 10 + "");
        params.addQueryStringParameter("content", content);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //语境列表
    public static void envirList(String page, String size, String sort, String category_id, String stick, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/scene_context/getlist";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("sort", sort);
        params.addQueryStringParameter("category_id", category_id);
        params.addQueryStringParameter("stick", stick);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);

    }

    //用户激活状态
    public static void activeStatus(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/gateway/record_fiu_user_active";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //验证验证码
    public static void submitCheckCode(String phone, String code, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/check_verify_code";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("code", code);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //注册用户
    public static void registerUser(String mobile, String password, String verify_code, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/auth/register";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("password", password);
        params.addBodyParameter("verify_code", verify_code);
        params.addBodyParameter("from_to", "2"); //1.ios;2.android;3.win;
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //完善资料--->关注感兴趣的人
    public static void focusInterestUser(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/user/find_user";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("size", "18");
        params.addQueryStringParameter("type", "1");
        params.addQueryStringParameter("edit_stick", "1");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //开始关注感兴趣的人
    public static void focusUsers(String follow_ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/follow/batch_follow";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("follow_ids", follow_ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //获取推荐活动标签
    public static void activeTags(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/scene_sight/stick_active_tags";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //添加品牌
    public static void addBrand(String title, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/scene_brands/submit";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("title", title);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //搜索建议
    public static void searchExpand(String q, String size, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/search/expanded";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("q", q);
        params.addQueryStringParameter("size", size);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 取消订阅情景主题
     *
     * @param id
     */
    public static HttpHandler<String> cancelSubscribe(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/my/remove_interest_scene_id";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    public static HttpHandler<String> subscribe(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/my/add_interest_scene_id";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //记录情景浏览次数
    public static void viewCount(String qjId) {
        String url = NetworkConstance.BASE_URL + "/scene_sight/record_view";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", qjId);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    //首页精选主题
    public static void subjectList(String page, String size, String stick, String fine, String type, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/scene_subject/getlist";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", size);
        params.addQueryStringParameter("stick", stick);
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //最新好货推荐
    public static void firstProducts(RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/product/index_new";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * @param page
     * @param pageType
     * @param sort
     * @param callBack
     */
    public static void getChoosenSubject(String page, String pageType, String fine, String sort, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/scene_subject/getlist";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("size", Constants.PAGE_SIZE);//
        params.addQueryStringParameter("fine", fine);
        params.addQueryStringParameter("type", pageType);
        params.addQueryStringParameter("sort", sort);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //收藏情景
    public static HttpHandler<String> shoucang(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/favorite/ajax_favorite";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //取消收藏
    public static HttpHandler<String> cancelShoucang(String id, String type, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/favorite/ajax_cancel_favorite";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
        return httpHandler;
    }

    //首页用户列表
    public static void getUserList(int size, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/user/find_user";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("type", "1");
        params.addQueryStringParameter("size", size + "");
        params.addQueryStringParameter("edit_stick", "1");
        params.addQueryStringParameter("sort", "1");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //临时产品库
    public static void getTempGoods(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/user_temp/view";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    /**
     * 更新分享数量
     *
     * @param id
     * @param callBack
     */
    public static void updateShareCount(String id, RequestCallBack<String> callBack) {
        String url = NetworkConstance.BASE_URL + "/scene_subject/record_share_count";
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }
}
