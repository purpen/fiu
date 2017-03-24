package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.UnUsableRedPacketAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CheckRedBagUsableBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.RedBagUntimeout;
import com.taihuoniao.fineix.beans.RedPacketData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;


public class UnUsableRedPacketActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.pull_lv)
    PullToRefreshListView pull_lv;
    private static final String PAGE_SIZE="10";
    private int curPage=1;
    private boolean mInLoading = false;
    private List<RedBagUntimeout> mUntimeoutList = new ArrayList<>();
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
    private boolean mLookClick = false;
    private String mRid;//订单号
    private WaittingDialog mDialog;
    private UnUsableRedPacketAdapter adapter;
    private boolean isFirstLoad=true;
    public UnUsableRedPacketActivity(){
        super(R.layout.activity_red_bag);
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CUSTOM_PULLTOREFRESH_HOME:
                    dataLoadFinished();
                    break;
                case DataConstants.PARSER_CHECK_REDBAG_USABLE:
                    if (msg.obj != null) {
                        if (msg.obj instanceof HttpResponse) {
                            HttpResponse<CheckRedBagUsableBean>  checkRedBagUsableBean = ( HttpResponse<CheckRedBagUsableBean> ) msg.obj;
                            if ("1".equals(checkRedBagUsableBean.getData().getUseful())) {
                                //红包可用
                                Intent intent = new Intent();
                                intent.putExtra("code", checkRedBagUsableBean.getData().getCode());//红包码
                                intent.putExtra("money", checkRedBagUsableBean.getData().getCoin_money());//红包金额
                                setResult(DataConstants.RESULTCODE_REDBAG, intent);
                                finish();
                            }else {
                                //红包不可用
                                ToastUtils.showError("这个红包不可用");
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
                                View mUntimeoutView = LayoutInflater.from(UnUsableRedPacketActivity.this).inflate(R.layout.account_redbag_untimeout, null);

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
                                            checkRedbagUsableParser( mRid, mUntimeoutList.get(j).getCode(), mHandler);
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
                                View mTimeoutView = LayoutInflater.from(UnUsableRedPacketActivity.this).inflate(R.layout.account_redbag_timeout, null);
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
        custom_head.setHeadCenterTxtShow(true, "过期红包");
        lv = pull_lv.getRefreshableView();
        mDialog = new WaittingDialog(this);
        mRid = getIntent().getStringExtra("rid");
    }

    @Override
    protected void requestNet() {//请求可用红包
        HashMap<String, String> params = ClientDiscoverAPI.getmyRedBagNetRequestParams(String.valueOf(curPage),PAGE_SIZE,ALLUSED,TIMEOUT);
        HttpRequest.post(params,   URL.MY_BONUS, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (!activity.isFinishing()&&mDialog!=null&& isFirstLoad) mDialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (mDialog!=null) mDialog.dismiss();
                HttpResponse<RedPacketData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<RedPacketData>>() {});
                if (response.isSuccess()){
                    List<RedPacketData.RedPacketItem> rows = response.getData().rows;
                    refreshUI(rows);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (mDialog!=null) mDialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void installListener() {
        pull_lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isFirstLoad=false;
                requestNet();
            }
        });

        pull_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirstLoad=false;
                curPage=1;
                mList.clear();
                requestNet();
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
        curPage++;
        if (pull_lv!=null){
            pull_lv.onRefreshComplete();
            pull_lv.setLoadingTime();
        }
        if (list==null) return;
        if (list.size()==0) return;
        mList.addAll(list);
        if (adapter==null){
            adapter=new UnUsableRedPacketAdapter(mList,activity);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 验证红包是否可用接口
     * @param rid rid
     * @param code code
     * @param handler handler
     */
    private void checkRedbagUsableParser(String rid, String code, final Handler handler) {
        HashMap<String, String> params = ClientDiscoverAPI.getcheckRedBagUsableNetRequestParams(rid, code);
        HttpRequest.post(params,  URL.SHOPPING_USE_BONUS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                Message msg = new Message();
                msg.what = DataConstants.PARSER_CHECK_REDBAG_USABLE;
                HttpResponse<CheckRedBagUsableBean> checkRedBagUsableBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CheckRedBagUsableBean>>(){});
                if (checkRedBagUsableBeanHttpResponse.isError()) {
                    ToastUtils.showInfo(checkRedBagUsableBeanHttpResponse.getMessage());
                }
                msg.obj = checkRedBagUsableBeanHttpResponse;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(String error) {
                handler.sendEmptyMessage(DataConstants.NETWORK_FAILURE);
            }
        });
    }
}
