package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EvaluateAdapter;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.OrderDetails;
import com.taihuoniao.fineix.beans.OrderDetailsProducts;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PublishEvaluateActivity extends Activity {
    private ListViewForScrollView mListView;
    private EvaluateAdapter mAdapter;
    HashMap<Integer, String> mHashMapRatingBar = new HashMap<Integer, String>();
    HashMap<Integer, String> mHashMap = new HashMap<Integer, String>();
    private List<OrderDetails> mList = new ArrayList<>();
    private List<OrderDetailsProducts> mListProducts = new ArrayList<>();
    private TextView mCommit;
    private String mRid;
    private String mSkuId;
    private String mTargetId;
    private String mEvaluateContent;
    private String mRatingBarNum;
    private View mView;
    private LinearLayout mLinear;
    private String mEditContent;
    private SVProgressHUD dialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_ORDER_DETAILS:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            mList.clear();
                            mList.addAll((Collection<? extends OrderDetails>) msg.obj);
                            mListProducts.clear();
                            mListProducts.addAll(mList.get(0).getProducts());

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_publish_evaluate);
        initData();
        initView();

    }

    private void initView() {
        MyGlobalTitleLayout title = (MyGlobalTitleLayout) findViewById(R.id.title_evaluate);
        title.setTitle("发表评价");
        title.setBackgroundResource(R.color.white);
        title.setBackImg(R.mipmap.back_black);
        title.setTitleColor(getResources().getColor(R.color.black333333));
        title.setBackButtonVisibility(true);
        title.setRightSearchButton(false);
        title.setRightShopCartButton(false);
        mListView = (ListViewForScrollView) findViewById(R.id.lv_evaluate);
        mAdapter = new EvaluateAdapter(mListProducts, this);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnTwoClickedListener(new EvaluateAdapter.OnTwoClickedListener() {
            @Override
            public void onLetterCliced(HashMap<Integer, String> hashMapRatingBar, HashMap<Integer, String> hashMap) {
                mHashMapRatingBar = hashMapRatingBar;
                mHashMap = hashMap;
            }
        });
        dialog = new SVProgressHUD(PublishEvaluateActivity.this);
        mCommit = (TextView) findViewById(R.id.tv_commit_evaluate);
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < mHashMap.size(); j++) {
                    if (mHashMap.get(j) == null) {
                        dialog.showErrorWithStatus("评论内容不能为空!");
//                        Toast.makeText(PublishEvaluateActivity.this, "评价内容不能为空！", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.show();
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
                        ClientDiscoverAPI.publishEvaluateNet(mRid, array, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Gson gson = new Gson();
                                NetBean netBean = new NetBean();
                                try {
                                    Type type = new TypeToken<NetBean>() {
                                    }.getType();
                                    netBean = gson.fromJson(responseInfo.result, type);
                                } catch (JsonSyntaxException e) {
                                    Log.e("<<<", "数据解析异常" + e.toString());
                                }
                                dialog.dismiss();
//                                Toast.makeText(PublishEvaluateActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                                if (netBean.isSuccess()) {
                                    dialog.showSuccessWithStatus(netBean.getMessage());
                                    onBackPressed();
                                }else{
                                    dialog.showErrorWithStatus(netBean.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                dialog.dismiss();
                            }
                        });
                    }
                }

            }

        });
    }

    private void initData() {
        mTargetId = getIntent().getStringExtra("productId");
        mSkuId = getIntent().getStringExtra("skuId");
        mRid = getIntent().getStringExtra("rid");
        DataPaser.orderPayDetailsParser(mRid, mHandler);
    }
}
