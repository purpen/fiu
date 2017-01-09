package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EvaluateAdapter;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.NetBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.bean.OrderDetailBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublishEvaluateActivity extends Activity {
    private static final String TAG = "PublishEvaluateActivity";

    private EvaluateAdapter mAdapter;
    HashMap<Integer, String> mHashMapRatingBar = new HashMap<>();
    HashMap<Integer, String> mHashMap = new HashMap<>();
    private String mRid;
    private String mEvaluateContent;
    private String mRatingBarNum;
    private View mView;
    private LinearLayout mLinear;
    private String mEditContent;
    private WaittingDialog dialog;

    private OrderDetailBean shoppingDetailBean;
    private List<OrderDetailBean.ItemsBean> mListProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_publish_evaluate);
        initData();
        initView();
        WindowUtils.chenjin(this);
    }

    private void initView() {
        GlobalTitleLayout title = (GlobalTitleLayout) findViewById(R.id.title_evaluate);
        title.setTitle("发表评价");
        title.setContinueTvVisible(false);
        ListViewForScrollView mListView = (ListViewForScrollView) findViewById(R.id.lv_evaluate);

        mListProducts = new ArrayList<>();
        mAdapter = new EvaluateAdapter(mListProducts, this);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnTwoClickedListener(new EvaluateAdapter.OnTwoClickedListener() {
            @Override
            public void onLetterCliced(HashMap<Integer, String> hashMapRatingBar, HashMap<Integer, String> hashMap) {
                mHashMapRatingBar = hashMapRatingBar;
                mHashMap = hashMap;
            }
        });
        dialog = new WaittingDialog(PublishEvaluateActivity.this);
        TextView mCommit = (TextView) findViewById(R.id.tv_commit_evaluate);
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < mHashMap.size(); j++) {
                    if (mHashMap.get(j) == null) {
                        ToastUtils.showError("评论内容不能为空!");
//                        dialog.showErrorWithStatus("评论内容不能为空!");
//                        Toast.makeText(PublishEvaluateActivity.this, "评价内容不能为空！", Toast.LENGTH_LONG).show();
                    } else {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        StringBuilder builder = new StringBuilder();
                        builder.append("[");
                        //拼接须注意，字段名要加双引号，字段内容如为字符串也得加双引号，整型则不加
                        for (int i = 0; i < mListProducts.size(); i++) {
                            builder.append("{\"target_id\":").append(mListProducts.get(i).getProduct_id())
                                    .append(",\"sku_id\":").append(mListProducts.get(i).getSku()).append(",\"content\":\"")
                                    .append(mHashMap.get(i)).append("\",\"star\":").append(mHashMapRatingBar.get(i)).append("},");
                        }
                        builder.append("]");
                        builder.replace(builder.length() - 2, builder.length() - 1, "");
                        String array = builder.toString();
//                        Log.e(">>>", ">>>arrayratingbar>>" + array);
                        RequestParams params = ClientDiscoverAPI.getpublishEvaluateNetRequestParams(mRid, array);
                         HttpRequest.post(params,  URL.PRODUCT_AJAX_COMMENT, new GlobalDataCallBack(){
//                        ClientDiscoverAPI.publishEvaluateNet(mRid, array, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(String json) {
                                Gson gson = new Gson();
                                NetBean netBean = new NetBean();
                                try {
                                    Type type = new TypeToken<NetBean>() {
                                    }.getType();
                                    netBean = gson.fromJson(json, type);
                                } catch (JsonSyntaxException e) {
                                    Log.e("<<<", "数据解析异常" + e.toString());
                                }
                                dialog.dismiss();
//                                Toast.makeText(PublishEvaluateActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                                if (netBean.isSuccess()) {
                                    ToastUtils.showSuccess(netBean.getMessage());
//                                    dialog.showSuccessWithStatus(netBean.getMessage());
                                    onBackPressed();
                                } else {
                                    ToastUtils.showError(netBean.getMessage());
//                                    dialog.showErrorWithStatus(netBean.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                dialog.dismiss();
                            }
                        });
                    }
                }

            }

        });
    }

    private void initData() {
        mRid = getIntent().getStringExtra("rid");
//        DataPaser.orderPayDetailsParser(mRid, mHandler);
        //订单支付详情和订单详情都是这，发表评价界面的产品图片也从这获取
        RequestParams params = ClientDiscoverAPI.getOrderPayNetRequestParams(mRid);
        HttpRequest.post(params,  URL.SHOPPING_DETAILS, new GlobalDataCallBack(){
//        ClientDiscoverAPI.OrderPayNet(mRid, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {

                if (TextUtils.isEmpty(json)) return;
                HttpResponse<OrderDetailBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<OrderDetailBean>>() {});
                if (response.isError()) {
//                    LogUtil.e(TAG, "---------> responseInfo: "  + responseInfo.reasonPhrase);
                    return;
                }
                shoppingDetailBean = response.getData();
                mListProducts = shoppingDetailBean.getItems();

////                Log.e(">>>", ">>>OOO>>>" + json);
//                List<OrderDetails> ordersList = new ArrayList<>();
//                try {
//                    JSONObject obj = new JSONObject(json);
//                    JSONObject orderObj = obj.getJSONObject("data");
//                    OrderDetails details = new OrderDetails();
//                    details.setRid(orderObj.optString("rid"));
//                    details.setExpress_company(orderObj.optString("express_company"));
//                    details.setExpress_no(orderObj.optString("express_no"));
//                    details.setCreated_at(orderObj.optString("created_at"));
//                    details.setFreight(orderObj.optString("freight"));
//                    details.setItems_count(orderObj.optString("items_count"));
//                    details.setPay_money(orderObj.optString("pay_money"));
//                    details.setPayment_method(orderObj.optString("payment_method"));
//                    details.setTotal_money(orderObj.optString("total_money"));
//                    details.setStatus(orderObj.optString("status"));
//                    JSONObject arr = orderObj.getJSONObject("express_info");
//                    List<OrderDetailsAddress> addressList = new ArrayList<>();
//                    OrderDetailsAddress address = new OrderDetailsAddress();
//                    address.setAddress(arr.optString("address"));
//                    address.setName(arr.optString("name"));
//                    address.setCity(arr.optString("city"));
//                    address.setPhone(arr.optString("phone"));
//                    address.setProvince(arr.optString("province"));
//                    addressList.add(address);
//
//                    details.setAddresses(addressList);
//                    JSONArray productsArrays = orderObj.getJSONArray("mListProducts");
//                    List<OrderDetailsProducts> productsList = new ArrayList<>();
//                    for (int j = 0; j < productsArrays.length(); j++) {
//                        JSONObject productsArr = productsArrays.getJSONObject(j);
//                        OrderDetailsProducts products = new OrderDetailsProducts();
//                        products.setName(productsArr.optString("name"));
//                        products.setCover_url(productsArr.optString("cover_url"));
//                        products.setPrice(productsArr.optString("price"));
//                        products.setProduct_id(productsArr.optString("product_id"));
//                        products.setQuantity(productsArr.optString("quantity"));
//                        products.setSale_price(productsArr.optDouble("sale_price"));
//                        products.setSku(productsArr.optString("sku"));
//                        products.setSku_name(productsArr.optString("sku_name"));
//                        productsList.add(products);
//                    }
//                    details.setProducts(productsList);
//                    ordersList.add(details);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                mList.clear();
//                mList.addAll(ordersList);
//                mListProducts.clear();
//                mListProducts.addAll(mList.get(0).getProducts());


                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError("网络错误");
            }
        });
    }
}
