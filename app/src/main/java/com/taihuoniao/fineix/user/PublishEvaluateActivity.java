package com.taihuoniao.fineix.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EvaluateAdapter;
import com.taihuoniao.fineix.base.Base2Activity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublishEvaluateActivity extends Base2Activity {

    private EvaluateAdapter mAdapter;
    HashMap<Integer, String> mHashMapRatingBar = new HashMap<>();
    HashMap<Integer, String> mHashMap = new HashMap<>();
    private String mRid;
    private WaittingDialog dialog;

    private OrderDetailBean shoppingDetailBean;
    private List<OrderDetailBean.ItemsBean> mListProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        HashMap<String, String> params = ClientDiscoverAPI.getpublishEvaluateNetRequestParams(mRid, array);
                         HttpRequest.post(params,  URL.PRODUCT_AJAX_COMMENT, new GlobalDataCallBack(){
                            @Override
                            public void onSuccess(String json) {

                                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                                dialog.dismiss();
                                if (netBean.isSuccess()) {
                                    ToastUtils.showSuccess(netBean.getMessage());
                                    onBackPressed();
                                } else {
                                    ToastUtils.showError(netBean.getMessage());
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

        //订单支付详情和订单详情都是这，发表评价界面的产品图片也从这获取
        HashMap<String, String> params = ClientDiscoverAPI.getOrderPayNetRequestParams(mRid);
        HttpRequest.post(params,  URL.SHOPPING_DETAILS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {

                if (TextUtils.isEmpty(json)) return;
                HttpResponse<OrderDetailBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<OrderDetailBean>>() {});
                if (response.isError()) {
                    return;
                }
                shoppingDetailBean = response.getData();
                mListProducts = shoppingDetailBean.getItems();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError("网络错误");
            }
        });
    }
}
