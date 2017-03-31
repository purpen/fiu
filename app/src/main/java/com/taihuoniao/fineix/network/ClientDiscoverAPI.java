package com.taihuoniao.fineix.network;

import android.support.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.user.EditUserInfoActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.SPUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by android on 2015/12/27.
 * 参数设置
 */
public class ClientDiscoverAPI {
    @NonNull
    public static HashMap<String, String> getDefaultParams() {
        HashMap<String, String> params = new HashMap<>();
        return params;
    }
    @NonNull
    public static HashMap<String, String> getdeleteProductRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getgetProductListRequestParams(String title, String sort, String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids, String stick, String fine, String stage) {
        HashMap<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("sort", sort);
        params.put("category_id", category_id);
        params.put("brand_id", brand_id);
        params.put("brand_id", brand_id);
        params.put("category_tags", category_tag_ids);
        params.put("page", page);
        params.put("size", size);
        params.put("ids", ids);
        params.put("ignore_ids", ignore_ids);
        params.put("stick", stick);
        params.put("fine", fine);
        params.put("stage", stage);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getaddProductRequestParams(String title, String brand_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("brand_id", brand_id);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getgoodsDetailsRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }



    @NonNull
    public static HashMap<String, String> getgetSupportQJRequestParams(String page, String type, String event) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", Constants.PAGE_SIZE);
        params.put("user_id", LoginInfo.getUserId() + "");
        params.put("type", type);
        params.put("event", event);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getsubsQingjingRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }




    @NonNull
    public static HashMap<String, String> getcreateSceneRequestParams(String id, String title, String des, String scene_id, String tags, String products, String address, String city, String tmp, String lat, String lng, String subject_ids) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("tmp", tmp);
        params.put("title", title);
        params.put("des", des);
        params.put("city", city);
        params.put("scene_id", scene_id);
        params.put("tags", tags);
        params.put("products", products);
        params.put("address", address);
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("subject_ids", subject_ids);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getdeleteSceneRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getloveQJRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getcancelLoveQJRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getsceneDetailsRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getSceneListRequestParams(String page, String size, String scene_id, String category_ids, String sort, String fine, String lng, String lat) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("scene_id", scene_id);
        params.put("category_ids", category_ids);
        params.put("sort", sort);
        params.put("fine", fine);
        params.put("dis", 10000 + "");
        params.put("lng", lng);
        params.put("lat", lat);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getQJListRequestParams(String page, String category_ids) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", Constants.PAGE_SIZE);
        params.put("category_ids", category_ids);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getparticipateActivityRequestParams(String page, String subject_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", Constants.PAGE_SIZE);
        params.put("subject_id", subject_id);
        params.put("use_cache", "1");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getSceneListRequestParams(String page, String size, String userId, String show_all) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("user_id", userId);
        params.put("show_all", show_all);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getusedLabelListRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", 1 + "");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getgetAddressListRequestParams(String page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getcommitAddressNetRequestParams(String id, String name, String phone, String province_id, String city_id, String county_id, String town_id, String address, String zip, String is_default) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("phone", phone);
        params.put("province_id", province_id);
        params.put("city_id", city_id);
        params.put("county_id", county_id);
        params.put("town_id", town_id);
        params.put("address", address);
        params.put("zip", zip);
        params.put("is_default", is_default);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getdeleteAddressNetRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getreportRequestParams(String target_id, String type, String evt) {
        HashMap<String, String> params = new HashMap<>();
        params.put("target_id", target_id);
        params.put("type", type);
        params.put("evt", evt);
        params.put("application", 3 + "");
        params.put("from_to", 4 + "");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getbrandDetailRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getcategoryListRequestParams(String page, String domain, String show_all) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", 300 + "");
        params.put("show_all", show_all);
        params.put("domain", domain);
        params.put("use_cache", "1");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getcategoryListRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "10");
        params.put("domain", "13");//情景主题
        params.put("use_cache", "1");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getsearchRequestParams(String q, String t, String cid, String page, String size, String evt, String sort) {
        HashMap<String, String> params = new HashMap<>();
        params.put("q", q);
        params.put("t", t);
        params.put("tid", cid);
        params.put("page", page);
        params.put("evt", evt);
        params.put("sort", sort);
        params.put("size", size);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getsearchUserRequestParams(String q, String page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("q", q);
        params.put("t", "14"); //14.用户
        params.put("page", page);
        params.put("size", Constants.PAGE_SIZE);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getproductAndSceneRequestParams(String page, String size, String sight_id, String product_id, String brand_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("sight_id", sight_id);
        params.put("product_id", product_id);
        params.put("brand_id", brand_id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getsendCommentRequestParams(String target_id, String content, String type, String target_user_id, String is_reply, String reply_id, String reply_user_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("target_id", target_id);
        params.put("content", content);
        params.put("target_user_id", target_user_id);
        params.put("is_reply", is_reply);
        params.put("reply_id", reply_id);
        params.put("reply_user_id", reply_user_id);
        params.put("type", type);
        params.put("from_site", 4 + "");
        return params;
    }


    @NonNull
    public static HashMap<String, String> getcommentsListRequestParams(String page, String size, String target_id, String target_user_id, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("sort", "1");
        params.put("target_id", target_id);
        params.put("target_user_id", target_user_id);
        params.put("type", type);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getmycommentsListRequestParams(String page, String size, String target_id, String target_user_id, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("target_id", target_id);
        params.put("target_user_id", target_user_id);
        params.put("type", type);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getdeleteCommentRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getGoodsDetailsCommentsListRequestParams(String target_id, String page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("target_id", target_id);
        params.put("type", "4");
        return params;
    }


    @NonNull
    public static HashMap<String, String> getclickLoginNetRequestParams(String phone, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("from_to", "2");     //登录渠道
        params.put("password", password);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getthirdLoginNetRequestParams(String oid, String access_token, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("oid", oid);
        params.put("access_token", access_token);
        params.put("type", type);
        params.put("from_to", "2");
        return params;
    }


    @NonNull
    public static HashMap<String, String> getgetVerifyCodeNetRequestParams(String phone) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getbindPhoneNetRequestParams(String oid, String union_id, String access_token, String account, String password, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("password", password);
        params.put("oid", oid);
        params.put("union_id", union_id);
        params.put("access_token", access_token);
        params.put("third_source", type);
        params.put("from_to", "2"); //Android
        return params;
    }


    @NonNull
    public static HashMap<String, String> getskipBindNetRequestParams(String oid, String union_id, String access_token, String nickname, String sex, String avatar_url, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("sex", sex);
        params.put("oid", oid);
        params.put("union_id", union_id);
        params.put("avatar_url", avatar_url);
        params.put("access_token", access_token);
        params.put("third_source", type);
        params.put("from_to", "2");
        return params;
    }


    @NonNull
    public static HashMap<String, String> getQJDataRequestParams(LatLng ll, int radius, String page, String pageSize, String stick) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", pageSize);
        params.put("sort", "0");
        params.put("stick", stick);
        if (radius > 0) {
            params.put("dis", String.valueOf(radius));
        }
        if (ll != null) {
            params.put("lat", String.valueOf(ll.latitude));
            params.put("lng", String.valueOf(ll.longitude));
        }
        return params;
    }


    @NonNull
    public static HashMap<String, String> getQJListRequestParams(String page, String pageSize, String userId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", pageSize);
        params.put("sort", "0");
        params.put("stick", "0");
        params.put("user_id", userId);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getfindPasswordNetRequestParams(String phone, String newpassword, String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("password", newpassword);
        params.put("verify_code", code);
        return params;
    }



    @NonNull
    public static HashMap<String, String> getgetMineInfoRequestParams(String userId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        LogUtil.e("getMineInfo", userId);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getgetBannersRequestParams(String page_name) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(1));
        params.put("size", String.valueOf(5));
        params.put("name", page_name);
        params.put("use_cache", "1");
        return params;
    }


    @NonNull
    public static HashMap<String, String> getFocusFansListRequestParams(String userId, String page, String size, String find_type, String clean_remind) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("user_id", userId);//924808
        params.put("find_type", find_type);
        params.put("clean_remind", clean_remind);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getfocusOperateRequestParams(String follow_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("follow_id", follow_id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getcancelFocusOperateRequestParams(String follow_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("follow_id", follow_id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getcommitSuggestionRequestParams(String content, String contact) {
        HashMap<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("contact", contact);
        params.put("from_to", "2"); //1.ios;2.android;3.ipad;4.win;5.web;6.wap
        params.put("kind", "3");   //1.网页版; 2.商城app; 3.Fiu
        return params;
    }


    @NonNull
    public static HashMap<String, String> getupdateUserInfoRequestParams(String key, String value) {
        HashMap<String, String> params = new HashMap<>();
        if (EditUserInfoActivity.isSubmitAddress) {
            params.put("province_id", key);
            params.put("district_id", value);
        } else {
            params.put(key, value);
        }
        return params;
    }


    @NonNull
    public static HashMap<String, String> getupdateNickNameSexRequestParams(String nickname, String sex) {
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("sex", sex);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getupdateAgeAssetsRequestParams(String age_group, String assets) {
        HashMap<String, String> params = new HashMap<>();
        params.put("age_group", age_group);
        params.put("assets", assets);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getsubscribeThemeRequestParams(String interest_scene_cate) {
        HashMap<String, String> params = new HashMap<>();
        params.put("interest_scene_cate", interest_scene_cate);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getupdateSignatrueLabelRequestParams(String label, String summary) {
        HashMap<String, String> params = new HashMap<>();
        params.put("label", label);
        params.put("summary", summary);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getgetAllCitiesRequestParams() {
        return new HashMap<>();
    }


    @NonNull
    public static HashMap<String, String> getuploadImgRequestParams(String tmp, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("tmp", tmp);
        params.put("type", type);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getuploadBgImgRequestParams(String tmp) {
        HashMap<String, String> params = new HashMap<>();
        params.put("tmp", tmp);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getgetSystemNoticeRequestParams(String page, String pageSize) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", pageSize);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getgetPrivateMessageListRequestParams(String page, String pageSize, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", pageSize);
        params.put("type", type);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getsendMessageRequestParams(String to_user_id, String content) {
        HashMap<String, String> params = new HashMap<>();
        params.put("to_user_id", to_user_id);
        params.put("content", content);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getmessageDetailListRequestParams(String to_user_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("to_user_id", to_user_id);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getcheckRedBagUsableNetRequestParams(String rid, String code) {
        HashMap<String, String> params = new HashMap<>();
//        params.put("uuid", uuid);
        params.put("rid", rid);
        params.put("code", code);
        return params;
    }



    @NonNull
    public static HashMap<String, String> getmyRedBagNetRequestParams(String page, String size, String used, String time) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("used", used);
        params.put("is_expired", time);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getupdatePasswordRequestParams(String password, String new_password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("new_password", new_password);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getnowConfirmOrderRequestParams(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rrid", rrid);
        params.put("from_site", "8");
        params.put("addbook_id", addbook_id);
        params.put("is_nowbuy", is_nowbuy);
        params.put("summary", summary);
        params.put("transfer_time", transfer_time);
        params.put("bonus_code", bonus_code);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getcalculateShopCartNetRequestParams(String array) {
        HashMap<String, String> params = new HashMap<>();
        params.put("array", array);
        params.put("referral_code", SPUtil.read("referral_code" ));
        return params;
    }


    @NonNull
    public static HashMap<String, String> getdeletShopCartNetRequestParams(String array) {
        HashMap<String, String> params = new HashMap<>();
        params.put("array", array);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getcancelLoveNetRequestParams(String id, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("type", type);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getloveNetRequestParams(String id, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("type", type);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getaddToCartNetRequestParams(String target_id, String type, String n, String storageId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("target_id", target_id);
        params.put("type", type);
        params.put("n", n);
        params.put("storage_id", storageId);
        params.put("referral_code", SPUtil.read("referral_code" ));
        return params;
    }


    @NonNull
    public static HashMap<String, String> getbuyNowRequestParams(String target_id, String type, String n, String storageId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("target_id", target_id);
        params.put("type", type);
        params.put("n", n);
        params.put("storage_id", storageId);
        params.put("referral_code", SPUtil.read("referral_code" ));
        return params;
    }


    @NonNull
    public static HashMap<String, String> getdeleteOrderNetRequestParams(String rid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getcancelOrderNetRequestParams(String rid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getOrderPayNetRequestParams(String rid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getconfirmReceiveNetRequestParams(String rid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getpublishEvaluateNetRequestParams(String rid, String array) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        params.put("array", array);
        params.put("from_site", "4");
        return params;
    }


    @NonNull
    public static HashMap<String, String> getorderListNetRequestParams(String status, String page, String size) {
        HashMap<String, String> params = new HashMap<>();
        params.put("status", status);
        params.put("page", page);
        params.put("size", size);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getshopcartAddSubtractNetRequestParams(String array) {
        HashMap<String, String> params = new HashMap<>();
        params.put("array", array);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getfiuUserListRequestParams(String page, String size, String sort) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("sort", sort);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getupdateUserIdentifyRequestParams(String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getgetNoticeListRequestParams(String page, String size, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("type", type);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getfindFriendsRequestParams(String page, String size, String sight_count, String sort) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("type", "1"); //过滤已关注
        params.put("sight_count", sight_count); //场景数量
        params.put("sort", sort); //0是最新 1是随机
        params.put("use_cache", "1");
        return params;
    }


    @NonNull
    public static HashMap<String, String> getlogoutRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("from_to", "2"); // 1.ios;2.android;3.win;4.ipad;
        return params;
    }


    @NonNull
    public static HashMap<String, String> getgetPhoneStateRequestParams(String account) {
        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getgetPayParamsRequestParams(String rid, String payaway) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        params.put("payaway", payaway);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getuploadIdentityInfoRequestParams(String id, String info, String label, String contact, String id_card_a_tmp, String business_card_tmp) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("info", info);
        params.put("label", label);
        params.put("contact", contact);
        params.put("id_card_a_tmp", id_card_a_tmp);
        params.put("business_card_tmp", business_card_tmp);
        return params;
    }

    @NonNull
    public static HashMap<String, String> gettixingFahuoRequestParams(String rid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getgetBonusRequestParams(String type, String evt, String target_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("evt", evt);
        params.put("target_id", target_id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getgetSubjectDataRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getfavoriteRequestParams(String id, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("type", type);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getcancelFavoriteRequestParams(String id, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("type", type);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getsubmitInviteCodeRequestParams(String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("code", code);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getupdateInviteCodeStatusRequestParams(String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("code", code);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getgetCollectOrderedRequestParams(String page, String size, String type, String event) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("user_id", String.valueOf(LoginInfo.getUserId()));
        params.put("type", type);
        params.put("event", event);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getenvirListRequestParams(String page, String size, String sort, String category_id, String stick) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("sort", sort);
        params.put("category_id", category_id);
        params.put("stick", stick);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getactiveStatusRequestParams() {
        return new HashMap<>();
    }

    @NonNull
    public static HashMap<String, String> getsubmitCheckCodeRequestParams(String phone, String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getregisterUserRequestParams(String mobile, String password, String verify_code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("verify_code", verify_code);
        params.put("from_to", "2"); //1.ios;2.android;3.win;
        return params;
    }

    @NonNull
    public static HashMap<String, String> getfocusInterestUserRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "18");
        params.put("type", "1");
        params.put("edit_stick", "1");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getfocusUsersRequestParams(String follow_ids) {
        HashMap<String, String> params = new HashMap<>();
        params.put("follow_ids", follow_ids);
        return params;
    }

    @NonNull
     public static HashMap<String, String> getactiveTagsRequestParams() {
        return new HashMap<>();
    }

    @NonNull
     public static HashMap<String, String> getaddBrandRequestParams(String title) {
        HashMap<String, String> params = new HashMap<>();
        params.put("title", title);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getsearchExpandRequestParams(String q, String size) {
        HashMap<String, String> params = new HashMap<>();
        params.put("q", q);
        params.put("size", size);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getcancelSubscribeRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getsubscribeRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getsubjectListRequestParams(String page, String size, String stick, String fine, String type, String sort) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("stick", stick);
        params.put("fine", fine);
        params.put("type", type);
        params.put("sort", sort);
        params.put("use_cache", "1");
        return params;
    }

    @NonNull
   public static HashMap<String, String> getfirstProductsRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("use_cache", "1");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getChoosenSubjectRequestParams(String page, String pageType, String fine, String sort) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", Constants.PAGE_SIZE);//
        params.put("fine", fine);
        params.put("type", pageType);
        params.put("sort", sort);
        params.put("use_cache", "1");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getshoucangRequestParams(String id, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("type", type);
        return params;
    }


    @NonNull
   public static HashMap<String, String> getcancelShoucangRequestParams(String id, String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("type", type);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getUserListRequestParams(int size) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("type", "1");
        params.put("size", size + "");
        params.put("edit_stick", "1");
        params.put("sort", "1");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getupdateShareCountRequestParams(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getisEditorRequestParams() {
        return new HashMap<String,String>();
    }

    @NonNull
    public static HashMap<String, String> getsetFineRequestParams(String id, String evt) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("evt", evt);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getsetStickRequestParams(String id, String evt) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("evt", evt);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getsetCheckRequestParams(String id, String evt) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("evt", evt);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getrequestAddressRequestParams(String oid, String pid, String layer) {
        HashMap<String, String> params = new HashMap<>();
        params.put("oid",oid);
        params.put("pid",pid);
        params.put("layer",layer);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getIndexChosenSubjectRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("use_cache", "1");
        return params;
    }

    @NonNull
    public static HashMap<String, String> getRefundListRequestParams(String page, String size) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getChargeBackInfoRequestParams(String rId, String skuId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rId);
        params.put("sku_id", skuId);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getApplyProductRefundRequestParams(String rid, String sku_id, String refund_type, String refund_reason, String refund_content, String refund_price) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        params.put("sku_id", sku_id);
        params.put("refund_type", refund_type);
        params.put("refund_reason", refund_reason);
        params.put("refund_content", refund_content);
        params.put("refund_price", refund_price);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getRefundSuccessInfoRequestParams(String chargebackId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", chargebackId);
        return params;
    }


    @NonNull
    public static HashMap<String, String> getupdateToLatestVersionRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("from_to", "2");
        return params;
    }


    @NonNull
    public static HashMap<String, String> getcheckVersionInfoRequestParams(String versionName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from_to", "2");
        params.put("version", versionName);
        return params;
    }

    @NonNull
    public static HashMap<String, String> getFetchFreightRequestParams(String addbook_id, String rid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("addbook_id", addbook_id);
        params.put("rid", rid);
        return params;
    }

    public static HashMap<String, String> getShoppingTrackingRequestParams(String rid, String express_no, String express_caty) {
        HashMap<String, String> params = new HashMap<>();
        params.put("rid", rid);
        params.put("express_no", express_no);
        params.put("express_caty", express_caty);
        return params;
    }

    public static HashMap<String, String> getallianceAccount() {
        return new HashMap<>();
    }

    public static HashMap<String, String> getWithdraw_cash(String id, String amount, String accountId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("amount", amount);
        params.put("payment_card_id", accountId);
        return params;
    }

    public static HashMap<String, String> getTradeRecordelist(String page, String size, String sort) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("sort", sort);
        return params;
    }

    /**
     * @param _id 佣金ID
     * @return
     */
    public static HashMap<String, String> getTradeRecordedetails(String _id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", _id);
        return params;
    }

    public static HashMap<String, String> getSettlementRecordedetails(String page, String size, String sort, String balance_record_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("sort", sort);
        params.put("balance_record_id", balance_record_id);
        return params;
    }

    public static HashMap<String, String> getWithdrawRecordedetails(String id, String amount) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("amount", amount);
        return params;
    }

    public static HashMap<String, String> getZoneDetailParams(String id,String isEdit) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("is_edit",isEdit);
        return params;
    }

    public static HashMap getRelateScene(int curPage,String scene_id,String sort,String stick) {
        HashMap<String, String> params = new HashMap<>();
        params.put("scene_id",scene_id);
        params.put("page", curPage+"");
        params.put("size",Constants.PAGE_SIZE);
        params.put("sort",sort);
        params.put("stick",stick);
        return params;
    }

    public static HashMap getRelateProducts(int curPage, String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("scene_id",id);
        params.put("page", curPage+"");
        params.put("size",Constants.PAGE_SIZE);
        return params;
    }

    //分享h5链接
    public static Map<String,String> getH5ShareParams(String id,String type,String storage_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id",id);
        params.put("type", type);
        params.put("storage_id",storage_id);
        return params;
    }

    //编辑参数
    public static HashMap<String,String> getEditZoneInfo(String id,String title,String sub_title,String des,String category_id) {
        HashMap<String,String> params= new HashMap<>();
        params.put("id",id);
        params.put("title",title);
        params.put("sub_title",sub_title);
        params.put("des",des);
        params.put("category_id",category_id);
        return params;
    }

    //地盘封面图
    public static HashMap getZoneCoverParams(String id,String base64Str,String type) {
        HashMap<String,String> params = new HashMap<>();
        params.put("id",id);
        params.put("tmp",base64Str);
        params.put("type",type);
        return params;
    }


    public static Map<String,String> getAllianceWithDraw01(String id,String alliance_id,String kind,String pay_type,String account,String bank_address ,String is_default,String username,String phone,String verify_code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id",id);
        params.put("alliance_id", alliance_id);
        params.put("kind",kind);
        params.put("pay_type",pay_type);
        params.put("account",account);
        params.put("bank_address",bank_address);
        params.put("is_default",is_default);
        params.put("username",username);
        params.put("phone",phone);
        params.put("verify_code",verify_code);
        return params;
    }
    public static Map<String,String> getAllianceWithDraw02(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id",id);
        return params;
    }
    public static Map<String,String> getAllianceWithDraw03(String page, String size, String sort) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page",page);
        params.put("size",size);
        params.put("sort",sort);
        return params;
    }
    public static Map<String,String> getAllianceWithDraw04() {
        HashMap<String, String> params = new HashMap<>();
        return params;
    }
    public static Map<String,String> getAllianceWithDraw05(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id",id);
        return params;
    }
}
