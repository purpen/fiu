package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.OrderInterestSlidingAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.SlidingFocusImageView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/9 14:20
 */
public class OrderInterestQJActivity extends BaseActivity<QingJingListBean.QingJingItem> {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.sfiv)
    SlidingFocusImageView sfiv;
    @Bind(R.id.tv_current)
    TextView tv_current;
    @Bind(R.id.tv_sum)
    TextView tv_sum;
    @Bind(R.id.progress_bar)
    ProgressBar progress_bar;
    private int curPage = 1;
    private static final String PAGE_SIZE = "10";
    private static final String TYPE_SCENE = "scene";
    private static final String EVENT = "subscription";
    private SVProgressHUD dialog;
    private OrderInterestSlidingAdapter adapter;
    public static OrderInterestQJActivity instance;

    public OrderInterestQJActivity() {
        super(R.layout.activity_order_interest_qj);
    }

    @Override
    protected void initView() {
        instance = OrderInterestQJActivity.this;
        custom_head.setHeadCenterTxtShow(true, "订阅的情景");
        dialog = new SVProgressHUD(this);
    }

    @Override
    protected void installListener() {
        sfiv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tv_current.setText(String.valueOf(++i));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @OnClick({R.id.btn})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                startActivity(new Intent(activity,CompleteUserInfoActivity.class));
                break;
        }
    }

    @Override
    protected void requestNet() {//sort=2 stick=0
        String stick = "0"; //是推荐
        String sort="2";
        ClientDiscoverAPI.getQJData(String.valueOf(curPage),PAGE_SIZE,sort,stick, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (curPage == 1) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                QingJingListBean listBean = JsonUtil.fromJson(responseInfo.result, QingJingListBean.class);
                if (listBean.isSuccess()) {
                    List list = listBean.getData().getRows();
                    refreshUI(list);
                    return;
                }
                Util.makeToast(listBean.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI(List<QingJingListBean.QingJingItem> list) {
        if (list == null) return;
        if (list.size() == 0) return;
        tv_sum.setText(String.format("/%s", String.valueOf(list.size())));
        if (adapter == null) {
            adapter = new OrderInterestSlidingAdapter(sfiv, list, activity,progress_bar);
            sfiv.setGravity(Gravity.CENTER_VERTICAL);
            sfiv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
