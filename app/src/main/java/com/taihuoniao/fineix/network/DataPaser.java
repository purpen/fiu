package com.taihuoniao.fineix.network;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CartDoOrder;
import com.taihuoniao.fineix.beans.CartDoOrderBonus;
import com.taihuoniao.fineix.beans.CartOrderContentItem;
import com.taihuoniao.fineix.beans.CheckRedBagUsable;
import com.taihuoniao.fineix.beans.DictBean;
import com.taihuoniao.fineix.beans.FindPasswordInfo;
import com.taihuoniao.fineix.beans.OrderEntity;
import com.taihuoniao.fineix.beans.OrderItem;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.ShopCart;
import com.taihuoniao.fineix.beans.ShopCartItem;
import com.taihuoniao.fineix.beans.ShopCartNumber;
import com.taihuoniao.fineix.beans.TryCommentsBean;
import com.taihuoniao.fineix.beans.TryDetailsUserBean;
import com.taihuoniao.fineix.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/3/14.
 * 数据解析类
 */
public class DataPaser {

    /**
     * 公共 搜索列表
     * @param q q
     * @param t t
     * @param page page
     * @param evt evt
     * @param sort sort
     * @param handler handler
     */
    public static void search(String q, String t, String page, String evt, String sort, final Handler handler) {
        HashMap<String, String> params = ClientDiscoverAPI.getsearchRequestParams(q, t, null, page, "8", evt, sort);
        HttpRequest.post(params, URL.SEARCH, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.SEARCH_LIST;
                msg.obj = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    msg.obj = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(String error) {
                handler.sendEmptyMessage(DataConstants.NET_FAIL);
            }
        });
    }

    public static List<TryCommentsBean> parserTryDetailsCommentsList(String jsonString) {
        List<TryCommentsBean> list = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONObject data = obj.getJSONObject("data");
            JSONArray rows = data.getJSONArray("rows");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject job = rows.getJSONObject(i);
                TryCommentsBean bean = new TryCommentsBean();
                bean.set_id(job.optString("_id"));
                bean.setUser_id(job.optString("user_id"));
                bean.setContent(job.optString("content"));
                bean.setStar(job.optString("star"));
                bean.setTarget_id(job.optString("target_id"));
                bean.setTarget_user_id(job.optString("target_user_id"));
                bean.setSku_id(job.optString("sku_id"));
                bean.setDeleted(job.optString("deleted"));
                bean.setReply_user_id(job.optString("reply_user_id"));
                bean.setFloor(job.optString("floor"));
                bean.setType(job.optString("type"));
                bean.setSub_type(job.optString("sub_type"));
                JSONObject userJob = job.getJSONObject("user");
                TryDetailsUserBean user = new TryDetailsUserBean();
                user.set_id(userJob.optString("_id"));
                user.setNickname(userJob.optString("nickname"));
                user.setHome_url(userJob.optString("home_url"));
                user.setSmall_avatar_url(userJob.optString("small_avatar_url"));
                user.setSymbol(userJob.optString("symbol"));
                user.setMini_avatar_url(userJob.optString("mini_avatar_url"));
                user.setMedium_avatar_url(userJob.optString("medium_avatar_url"));
                user.setBig_avatar_url(userJob.optString("big_avatar_url"));
                bean.setUser(user);
                if (!job.optString("target_user").equals("null")) {
                    JSONObject targetUserJob = job.getJSONObject("target_user");
                    TryDetailsUserBean targetUser = new TryDetailsUserBean();
                    targetUser.set_id(targetUserJob.optString("_id"));
                    targetUser.setNickname(targetUserJob.optString("nickname"));
                    targetUser.setHome_url(targetUserJob.optString("home_url"));
                    targetUser.setSmall_avatar_url(targetUserJob.optString("small_avatar_url"));
                    targetUser.setSymbol(targetUserJob.optString("symbol"));
                    targetUser.setMini_avatar_url(targetUserJob.optString("mini_avatar_url"));
                    targetUser.setMedium_avatar_url(targetUserJob.optString("medium_avatar_url"));
                    targetUser.setBig_avatar_url(targetUserJob.optString("big_avatar_url"));
                    bean.setTarget_user(targetUser);
                }
                bean.setLove_count(job.optString("love_count"));
                bean.setInvented_love_count(job.optString("invented_love_count"));
                bean.setIs_reply(job.optString("is_reply"));
                bean.setReply_id(job.optString("reply_id"));
                bean.setCreated_at(job.optString("created_at"));
                bean.setCreated_on(job.optString("created_on"));
                bean.setUpdated_on(job.optString("updated_on"));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 找回密码的解析
     * @param handler handler
     * @param phone phone
     * @param password password
     * @param code code
     */
    public static void findPasswordParser(final Handler handler, String phone, String password, String code) {
        HashMap<String, String> params = ClientDiscoverAPI.getfindPasswordNetRequestParams(phone, password, code);
        HttpRequest.post(params, URL.AUTH_FIND_PWD, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                FindPasswordInfo findPasswordInfo = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_FIND_PASSWORD;
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONObject findPasswordObj = obj.getJSONObject("data");
                    findPasswordInfo = new FindPasswordInfo();
                    findPasswordInfo.setSuccess(Boolean.parseBoolean(obj.optString("success")));
                    findPasswordInfo.setMessage(obj.optString("message"));
                    findPasswordInfo.setAccount(findPasswordObj.optString("account"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = findPasswordInfo;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(String error) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    /**
     * 验证红包是否可用接口
     * @param rid rid
     * @param code code
     * @param handler handler
     */
    public static void checkRedbagUsableParser(String rid, String code, final Handler handler) {
        HashMap<String, String> params = ClientDiscoverAPI.getcheckRedBagUsableNetRequestParams(rid, code);
        HttpRequest.post(params,  URL.SHOPPING_USE_BONUS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                CheckRedBagUsable checkRedBagUsable = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_CHECK_REDBAG_USABLE;
                try {
                    checkRedBagUsable = new CheckRedBagUsable();
                    JSONObject obj = new JSONObject(json);
                    if (!obj.optBoolean("success")) {
                        ToastUtils.showInfo(obj.optString("message"));
                    }
                    JSONObject redbagObj = obj.getJSONObject("data");
                    checkRedBagUsable.setCode(redbagObj.optString("code"));
                    checkRedBagUsable.setCoin_money(redbagObj.optString("coin_money"));
                    checkRedBagUsable.setUseful(redbagObj.optString("useful"));
                    checkRedBagUsable.setUseful(redbagObj.optString("useful"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = checkRedBagUsable;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(String error) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }


    /**
     * 购物车
     * @param handler handler
     * @return
     */
    public static Call shopCartParser(final Handler handler) {
        return HttpRequest.post(URL.SHOPPING_FETCH_CART, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                List<ShopCart> list = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_SHOP_CART;
                try {
                    list = new ArrayList<>();
                    JSONObject obj = new JSONObject(json);
                    JSONObject shopCartObj = obj.getJSONObject("data");
                    ShopCart shopCart = new ShopCart();
                    shopCart.set_id(shopCartObj.optString("_id"));
                    shopCart.setTotal_price(shopCartObj.optString("total_price"));
                    List<ShopCartItem> itemList = new ArrayList<>();
                    List<ShopCartItem> jdList = new ArrayList<>();
                    JSONArray shopCartArrs = shopCartObj.getJSONArray("items");
                    for (int i = 0; i < shopCartArrs.length(); i++) {
                        JSONObject shopCartArr = shopCartArrs.getJSONObject(i);
                        ShopCartItem shopCartItem = new ShopCartItem();
                        shopCartItem.vop_id = shopCartArr.optString("vop_id");
                        shopCartItem.setTotal_price(shopCartArr.optString("total_price"));
                        shopCartItem.setCover(shopCartArr.optString("cover"));
                        shopCartItem.setN(shopCartArr.optString("n"));
                        shopCartItem.setTarget_id(shopCartArr.optString("target_id"));
                        shopCartItem.setProduct_id(shopCartArr.optString("product_id"));
                        shopCartItem.setType(shopCartArr.optString("type"));
                        shopCartItem.setSku_mode(shopCartArr.optString("sku_mode"));
                        shopCartItem.setTitle(shopCartArr.optString("title"));
                        shopCartItem.setReferral_code(shopCartArr.optString("referral_code"));
                        shopCartItem.setStorage_id(shopCartArr.optString("storage_id"));
                        if (TextUtils.equals("null", shopCartItem.vop_id) || TextUtils.isEmpty(shopCartItem.vop_id)) {
                            itemList.add(shopCartItem);
                        } else {
                            jdList.add(shopCartItem);
                        }
                    }
                    if (!jdList.isEmpty()) {
                        jdList.get(0).isFirstJD = true;
                    }
                    itemList.addAll(jdList);
                    shopCart.setShopCartItemList(itemList);
                    list.add(shopCart);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = list;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(String error) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    /**
     * 购物车数量
     * @param handler handler
     * @return
     */
    public static Call shopCartNumberParser(final Handler handler) {
        HashMap<String, String> params = ClientDiscoverAPI.getDefaultParams();
        return HttpRequest.post(params,  URL.SHOPPING_FETCH_CART_COUNT, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                ShopCartNumber shopCartNumber = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_SHOP_CART_NUMBER;
                try {
                    shopCartNumber = new ShopCartNumber();
                    JSONObject obj = new JSONObject(json);
                    shopCartNumber.setSuccess(obj.optBoolean("success"));
                    JSONObject cartNumberObj = obj.getJSONObject("data");
                    shopCartNumber.setCount(cartNumberObj.optInt("count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = shopCartNumber;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(String error) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

    /**
     * 购物车结算获取数据传给确认订单界面
     * @param array array
     * @param handler handler
     */
    public static void shopCartCalculateParser(String array, final Handler handler) {
        HashMap<String, String> params = ClientDiscoverAPI.getcalculateShopCartNetRequestParams(array);
        HttpRequest.post(params,  URL.SHOPING_CHECKOUT, new GlobalDataCallBack(){
                    @Override
                    public void onSuccess(String json) {
                        List<CartDoOrder> list = null;
                        Message msg = new Message();
                        msg.what = DataConstants.PARSER_SHOP_CART_CALCULATE;
                        try {
                            JSONObject obj = new JSONObject(json);
                            if (!obj.optBoolean("success")) {
                                msg.obj = obj.optString("message");
                                handler.sendMessage(msg);
                                return;
                            }
                            list = new ArrayList<>();
                            JSONObject cartDoOrderObjs = obj.getJSONObject("data");
                            CartDoOrder cartDoOrder = new CartDoOrder();
                            cartDoOrder.setSuccess(obj.optString("success"));
                            cartDoOrder.setIs_nowbuy(cartDoOrderObjs.optString("is_nowbuy"));
                            cartDoOrder.setPay_money(cartDoOrderObjs.optString("pay_money"));
                            List<CartDoOrderBonus> bonusList = new ArrayList<>();
                            JSONArray bonusArray = cartDoOrderObjs.getJSONArray("bonus");
                            for (int j = 0; j < bonusArray.length(); j++) {
                                JSONObject bonusObj = bonusArray.getJSONObject(j);
                                CartDoOrderBonus cartDoOrderBonus = new CartDoOrderBonus();
                                cartDoOrderBonus.set__extend__(bonusObj.optString("__extend__"));
                                cartDoOrderBonus.setAmount(bonusObj.optString("amount"));
                                cartDoOrderBonus.setCode(bonusObj.optString("code"));
                                cartDoOrderBonus.setCreated_on(bonusObj.optString("created_on"));
                                cartDoOrderBonus.setExpired_at(bonusObj.optString("expired_at"));
                                cartDoOrderBonus.setExpired_label(bonusObj.optString("expired_label"));
                                cartDoOrderBonus.setGet_at(bonusObj.optString("get_at"));
                                cartDoOrderBonus.setMin_amount(bonusObj.optString("min_amount"));
                                cartDoOrderBonus.setOrder_rid(bonusObj.optString("order_rid"));
                                cartDoOrderBonus.setProduct_id(bonusObj.optString("product_id"));
                                cartDoOrderBonus.setStatus(bonusObj.optString("status"));
                                cartDoOrderBonus.setUpdated_on(bonusObj.optString("updated_on"));
                                cartDoOrderBonus.setUsed(bonusObj.optString("used"));
                                cartDoOrderBonus.setUsed_at(bonusObj.optString("used_at"));
                                cartDoOrderBonus.setUsed_by(bonusObj.optString("used_by"));
                                cartDoOrderBonus.setUser_id(bonusObj.optString("user_id"));
                                cartDoOrderBonus.setXname(bonusObj.optString("xname"));
                                bonusList.add(cartDoOrderBonus);
                            }
                            cartDoOrder.setBonus(bonusList);
                            JSONObject carDoOrderObj = cartDoOrderObjs.getJSONObject("order_info");
                            cartDoOrder.setRid(carDoOrderObj.optString("rid"));
                            cartDoOrder.set_id(carDoOrderObj.optString("_id"));
                            cartDoOrder.setCreated_on(carDoOrderObj.optString("created_on"));
                            cartDoOrder.setExpired(carDoOrderObj.optString("expired"));
                            cartDoOrder.setUpdated_on(carDoOrderObj.optString("updated_on"));
                            cartDoOrder.setUser_id(carDoOrderObj.optString("user_id"));
                            cartDoOrder.setIs_cart(carDoOrderObj.optString("is_cart"));
                            cartDoOrder.setKind(carDoOrderObj.optString("kind"));

                            List<DictBean> contentList = new ArrayList<>();
                            JSONObject contentObjs = carDoOrderObj.getJSONObject("dict");
                            DictBean dictBean = new DictBean();
                            dictBean.setCard_money(contentObjs.optString("card_money"));
                            dictBean.setCoin_money(contentObjs.optString("coin_money"));
                            dictBean.setFreight(contentObjs.optString("freight"));
                            dictBean.setInvoice_caty(contentObjs.optString("invoice_caty"));
                            dictBean.setInvoice_type(contentObjs.optString("invoice_type"));
                            dictBean.setItems_count(contentObjs.optString("items_count"));
                            dictBean.setSummary(contentObjs.optString("summary"));
                            dictBean.setPayment_method(contentObjs.optString("payment_method"));
                            dictBean.setTotal_money(contentObjs.optString("total_money"));
                            dictBean.setTransfer(contentObjs.optString("transfer"));
                            dictBean.setTransfer_time(contentObjs.optString("transfer_time"));
                            dictBean.setInvoice_content(contentObjs.optString("invoice_content"));
                            contentList.add(dictBean);
                            cartDoOrder.setDictBeen(contentList);

                            JSONArray itemArray = contentObjs.getJSONArray("items");
                            List<CartOrderContentItem> itemList = new ArrayList<>();
                            for (int i = 0; i < itemArray.length(); i++) {
                                JSONObject itemObj = itemArray.getJSONObject(i);
                                CartOrderContentItem item = new CartOrderContentItem();
                                item.setCover(itemObj.optString("cover"));
                                item.setPrice(itemObj.optString("price"));
                                item.setProduct_id(itemObj.optString("product_id"));
                                item.setQuantity(itemObj.optString("quantity"));
                                item.setSale_price(itemObj.optString("sale_price"));
                                item.setSku(itemObj.optString("sku"));
                                item.setSku_mode(itemObj.optString("sku_mode"));
                                item.setSubtotal(itemObj.optString("subtotal"));
                                item.setTarget_id(itemObj.optString("target_id"));
                                item.setTitle(itemObj.optString("title"));
                                item.setType(itemObj.optString("type"));
                                item.setView_url(itemObj.optString("view_url"));
                                itemList.add(item);
                            }
                            cartDoOrder.setCartOrderContentItems(itemList);
                            list.add(cartDoOrder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        msg.obj = list;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(String error) {
                        handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
                    }
                }
        );
    }

    /**
     * 账户处的订单列表
     * @param status status
     * @param page page
     * @param size size
     * @param handler handler
     */
    public static void orderListParser(String status, String page, String size, final Handler handler) {
        HashMap<String, String> params = ClientDiscoverAPI.getorderListNetRequestParams(status, page, size);
        HttpRequest.post(params,  URL.SHOPPING_ORDERS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                List<OrderEntity> list = null;
                Message msg = new Message();
                msg.what = DataConstants.PARSER_ORDER;
                try {
                    list = new ArrayList<>();
                    JSONObject obj = new JSONObject(json);
                    JSONObject orderObj = obj.getJSONObject("data");
                    JSONArray orderArrs = orderObj.getJSONArray("rows");
                    for (int i = 0; i < orderArrs.length(); i++) {
                        JSONObject orderArr = orderArrs.getJSONObject(i);
                        OrderEntity orderEntity = new OrderEntity();
                        orderEntity.setRid(orderArr.optString("rid"));
                        orderEntity.setItems_count(orderArr.optString("items_count"));
                        orderEntity.setTotal_money(orderArr.optString("total_money"));
                        orderEntity.setPay_money(orderArr.optString("pay_money"));
                        orderEntity.setDiscount_money(orderArr.optString("discount_money"));
                        orderEntity.setFreight(orderArr.optString("freight"));
                        orderEntity.setStatus_label(orderArr.optString("status_label"));
                        orderEntity.setCreated_at(orderArr.optString("created_at"));
                        orderEntity.setStatus(orderArr.optString("status"));
                        JSONArray array = orderArr.getJSONArray("items");
                        List<OrderItem> itemList = new ArrayList<>();
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject arr = array.getJSONObject(j);
                            OrderItem item = new OrderItem();
                            item.setSku(arr.optString("sku"));
                            item.setProduct_id(arr.optString("product_id"));
                            item.setQuantity(arr.optString("quantity"));
                            item.setSale_price(arr.optString("sale_price"));
                            item.setName(arr.optString("name"));
                            item.setSku_name(arr.optString("sku_name"));
                            item.setCover_url(arr.optString("cover_url"));
                            itemList.add(item);
                        }
                        orderEntity.setOrderItem(itemList);
                        list.add(orderEntity);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = list;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(String error) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }

}
