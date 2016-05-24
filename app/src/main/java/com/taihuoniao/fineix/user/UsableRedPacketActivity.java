package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.UsableRedPacketAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CheckRedBagUsable;
import com.taihuoniao.fineix.beans.RedBagUntimeout;
import com.taihuoniao.fineix.beans.RedPacketData;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class UsableRedPacketActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.pull_lv)
    PullToRefreshListView pull_lv;
    private static final String PAGE_SIZE="10";
    private int curPage=1;
    private boolean mInLoading = false;
    private List<RedBagUntimeout> mUntimeoutList = new ArrayList<>();
    private View mUntimeoutView;
    private View mTimeoutView;
    private LinearLayout mUntimeoutLinear;
    private LinearLayout mTimeoutLinear;
    private TextView mLook;
    private LinearLayout mLinearLook;
    private List<RedPacketData.RedPacketItem> mList=new ArrayList<>();
    private ListView lv;
    public static final String UNUSED = "1";//未使用过红包

    public static final String ALLUSED = "0";//全部

    public static final String UNTIMEOUT = "1";//未过期

    public static final String TIMEOUT = "2";//已过期
    private int total_rows;
    private boolean mLookClick = false;
    private String mRid;//订单号
    private WaittingDialog mDialog;
    private UsableRedPacketAdapter adapter;
    private View footerView;
    public UsableRedPacketActivity(){
        super(R.layout.activity_red_bag);
    }
    public boolean isFirstLoad=true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CUSTOM_PULLTOREFRESH_HOME:
                    dataLoadFinished();
                    break;
                case DataConstants.PARSER_CHECK_REDBAG_USABLE:
                    if (msg.obj != null) {
                        if (msg.obj instanceof CheckRedBagUsable) {
                            CheckRedBagUsable checkRedBagUsable= (CheckRedBagUsable) msg.obj;
                            if ("1".equals(checkRedBagUsable.getUseful())) {
                                //红包可用
                                Intent intent = new Intent();
                                intent.putExtra("code", checkRedBagUsable.getCode());//红包码
                                intent.putExtra("money", checkRedBagUsable.getCoin_money());//红包金额
                                setResult(DataConstants.RESULTCODE_REDBAG, intent);
                                finish();
                            }else {
                                //红包不可用
                                Toast.makeText(UsableRedPacketActivity.this,"这个红包不可用",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
                case DataConstants.PARSER_MY_REDBAG_UNTIMEOUT:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            mUntimeoutList.clear();
                            mUntimeoutList.addAll((List<RedBagUntimeout>) msg.obj);
                            if (mUntimeoutLinear != null) {
                                mUntimeoutLinear.removeAllViews();
                            }
                            for (int i = 0; i < mUntimeoutList.size(); i++) {
                                mUntimeoutView = LayoutInflater.from(UsableRedPacketActivity.this).inflate(R.layout.account_redbag_untimeout, null);

                                TextView mRedbagCode = (TextView) mUntimeoutView
                                        .findViewById(R.id.tv_redbag_code);
                                TextView mMinMoney = (TextView) mUntimeoutView
                                        .findViewById(R.id.tv_min_money);
                                TextView mRedbagMoney = (TextView) mUntimeoutView
                                        .findViewById(R.id.tv_money);

                                mRedbagCode
                                        .setText("红包码：" + mUntimeoutList.get(i).getCode());
                                mMinMoney
                                        .setText("最低使用限额：" + mUntimeoutList.get(i).getMin_amount() + "");
                                mRedbagMoney.setText(mUntimeoutList.get(i).getAmount() + "");
                                final int j = i;
                                mUntimeoutView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mRid != null) {
//                                            验证红包是否可用
                                            DataPaser.checkRedbagUsableParser(MainApplication.uuid, mRid, mUntimeoutList.get(j).getCode(), mHandler);
                                        }
                                    }
                                });
                                mUntimeoutLinear.addView(mUntimeoutView);
                            }
                            mDialog.dismiss();
                        }
                    }
                    break;
                case DataConstants.PARSER_MY_REDBAG_TIMEOUT:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            mUntimeoutList.clear();
                            mUntimeoutList.addAll((List<RedBagUntimeout>) msg.obj);
                            mLinearLook.setVisibility(View.GONE);
                            if (mTimeoutLinear != null) {
                                mTimeoutLinear.removeAllViews();
                            }
                            for (int i = 0; i < mUntimeoutList.size(); i++) {
                                mTimeoutView = LayoutInflater.from(UsableRedPacketActivity.this).inflate(R.layout.account_redbag_timeout, null);
                                TextView mRedbagCode = (TextView) mTimeoutView
                                        .findViewById(R.id.tv_redbag_code);
                                TextView mMinMoney = (TextView) mTimeoutView
                                        .findViewById(R.id.tv_min_money);
                                TextView mRedbagMoney = (TextView) mTimeoutView
                                        .findViewById(R.id.tv_money);
                                TextView mThnRedbag = (TextView) mTimeoutView.findViewById(R.id.tv_thn_redbag);
                                TextView mMoneySign = (TextView) mTimeoutView.findViewById(R.id.tv_money_sign);
                                mThnRedbag.setTextColor(getResources().getColor(R.color.color_333));
                                mMoneySign.setTextColor(getResources().getColor(R.color.color_333));
                                mRedbagCode.setText("红包码：" + mUntimeoutList.get(i).getCode());
                                mRedbagCode.setTextColor(getResources().getColor(R.color.color_333));
                                mMinMoney.setText("最低使用限额：" + mUntimeoutList.get(i).getMin_amount() + "");
                                mMinMoney.setTextColor(getResources().getColor(R.color.color_333));
                                mRedbagMoney.setText(mUntimeoutList.get(i).getAmount() + "");
                                mRedbagMoney.setTextColor(getResources().getColor(R.color.color_333));

                                mTimeoutLinear.addView(mTimeoutView);
                            }
                            mDialog.dismiss();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"我的红包");
        footerView=Util.inflateView(activity,R.layout.redpacket_foot_layout,null);
        lv = pull_lv.getRefreshableView();
        mUntimeoutLinear = (LinearLayout) findViewById(R.id.linear_no_timeout);
        mTimeoutLinear = (LinearLayout) findViewById(R.id.linear_timeout);
//        mLook = (TextView) findViewById(R.id.tv_look_redbag);
//        mLinearLook = (LinearLayout) findViewById(R.id.linear_look_redbag);
        mDialog = new WaittingDialog(this);
        mRid = getIntent().getStringExtra("rid");

        //未过期未使用
//        DataPaser.unTimeoutParser( UNUSED, UNTIMEOUT, mHandler);



//        mLook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.show();
//                //已过期、使没使用全有
//                mLookClick = true;
////                DataParser.unTimeoutParser(ALLUSED, TIMEOUT, mHandler);
//                DataPaser.unTimeoutParser(ALLUSED, UNTIMEOUT, mHandler);
//            }
//        });

    }

    @Override
    protected void requestNet() {//请求可用红包
        ClientDiscoverAPI.myRedBagNet(String.valueOf(curPage),PAGE_SIZE,UNUSED,UNTIMEOUT, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!activity.isFinishing()&&mDialog!=null) mDialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (mDialog!=null) mDialog.dismiss();
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<RedPacketData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<RedPacketData>>() {});
                if (response.isSuccess()){
                    if (isFirstLoad){
                        isFirstLoad=false;
                        total_rows = response.getData().total_rows;
                    }
                    List<RedPacketData.RedPacketItem> rows = response.getData().rows;
                    refreshUI(rows);
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (mDialog!=null) mDialog.dismiss();
                Util.makeToast("网络异常,请确保网络畅通");
            }
        });
    }

    @Override
    protected void installListener() {
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity,UnUsableRedPacketActivity.class));
            }
        });
        pull_lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                requestNet();
            }
        });

        pull_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage=1;
                mList.clear();
                if (footerView!=null&&lv.getFooterViewsCount()>0){
                    lv.removeFooterView(footerView);
                }
                requestNet();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //获取红包操作
            }
        });
    }

    private void dataLoadFinished() {
        if (mInLoading) {
            mInLoading = false;

        }
    }

    @Override
    protected void refreshUI(List list) {
        if (lv.getFooterViewsCount()==0 && mList.size()==total_rows){
            lv.addFooterView(footerView);
        }
        curPage++;
        if (list==null) return;
        if (list.size()==0) return;
        mList.addAll(list);
        if (adapter==null){
            adapter=new UsableRedPacketAdapter(mList,activity);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        if (pull_lv!=null){
            pull_lv.onRefreshComplete();
            pull_lv.setLoadingTime();
        }
    }
}
