package com.taihuoniao.fineix.personal.alliance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.adpter.AddSubAcountAdapter;
import com.taihuoniao.fineix.personal.alliance.bean.SubAccountListBean;
import com.taihuoniao.fineix.utils.DPUtil;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.StringFormatUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.TypeConversionUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.swipelistview.PullToRefreshSwipeMenuListView;
import com.taihuoniao.fineix.view.swipelistview.SwipeMenu;
import com.taihuoniao.fineix.view.swipelistview.SwipeMenuCreator;
import com.taihuoniao.fineix.view.swipelistview.SwipeMenuItem;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Stephen on 2017/4/27 16:05
 * Email: 895745843@qq.com
 */

public class SubAccountManageActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @Bind(R.id.pullToRefreshSwipeMenuListView1)
    PullToRefreshSwipeMenuListView pullToRefreshSwipeMenuListView1;

    private AddSubAcountAdapter mAddSubAcountAdapter;

    public SubAccountManageActivity() {
        super(R.layout.activity_alliance_my_sub_account);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "子账号管理");
        WindowUtils.chenjin(this);
        initSwipeMenuListView();
    }

    @Override
    protected void requestNet() {
        requestDataList();
    }

    private void initSwipeMenuListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                }
            }
            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(SubAccountManageActivity.this);
                item1.setBackground(R.color.colorDeleteBg);
                item1.setWidth(DPUtil.dip2px(SubAccountManageActivity.this, 80));
                item1.setTitle("删除");
                item1.setTitleSize(13);
                item1.setTitleColor(Color.WHITE);
//                item1.setIcon(R.drawable.delete_details);
                menu.addMenuItem(item1);
            }
        };
        pullToRefreshSwipeMenuListView1.setMenuCreator(creator);
        pullToRefreshSwipeMenuListView1.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                deleteSubAccount(mAddSubAcountAdapter.getmRows().get(position).get_id());
            }
        });

        mAddSubAcountAdapter = new AddSubAcountAdapter(SubAccountManageActivity.this);
        pullToRefreshSwipeMenuListView1.setAdapter(mAddSubAcountAdapter);
        pullToRefreshSwipeMenuListView1.setOnItemClickListener(this);
        pullToRefreshSwipeMenuListView1.setChoiceMode(PullToRefreshSwipeMenuListView.CHOICE_MODE_NONE);
        pullToRefreshSwipeMenuListView1.setPullLoadEnable(false);
        pullToRefreshSwipeMenuListView1.setPullRefreshEnable(false);
        pullToRefreshSwipeMenuListView1.setXListViewListener(new PullToRefreshSwipeMenuListView.IXListViewListener() {
            @Override
            public void onRefresh() {
//                isAdd = false;
//                pageNum = 1;
//                sendRequest();
//                computTribeCount();
            }

            @Override
            public void onLoadMore() {
//                isAdd = true;
//                pageNum++;
//                sendRequest();
//                computTribeCount();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.linearLayout1, R.id.linearLayout2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout1:
                startActivityForResult(new Intent(SubAccountManageActivity.this, AddSubAcountActivity.class) , 200);
                break;
            case R.id.linearLayout2:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SubAccountListBean.RowsEntity rowsEntity = mAddSubAcountAdapter.getmRows().get(position - 1);
        String id1 = rowsEntity.getCid();
        Intent intent = new Intent(SubAccountManageActivity.this, TradeRecordActivity.class);
        intent.putExtra("user_id", id1);
        startActivity(intent);
    }

    private void requestDataList(){
        HashMap<String, String> tradeRecordelist = ClientDiscoverAPI.getDefaultParams();
        HttpRequest.post(tradeRecordelist, URL.STORAGE_MANAGE_LIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<SubAccountListBean> subAccountListBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubAccountListBean>>() {
                });
                if (subAccountListBeanHttpResponse.isSuccess()) {
                    dealUI(subAccountListBeanHttpResponse);
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void dealUI(HttpResponse<SubAccountListBean> subAccountListBeanHttpResponse) {
        SubAccountListBean data = subAccountListBeanHttpResponse.getData();
        String total_rows = data.getTotal_rows();
        if (TypeConversionUtils.StringConvertInt(total_rows) > 0) {
            List<SubAccountListBean.RowsEntity> rows = data.getRows();
            mAddSubAcountAdapter.setList(rows);

            double totalAmout = 0;
            for(int i = 0; i < rows.size(); i++) {
                totalAmout += TypeConversionUtils.StringConvertDouble(rows.get(i).getAmount());
            }
            textView1.setText(StringFormatUtils.convert2double(String.valueOf(totalAmout)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            requestDataList();
        }
    }

    private void deleteSubAccount(String id){
        HashMap<String, String> tradeRecordelist = ClientDiscoverAPI.getDefaultParams();
        tradeRecordelist.put("id", id);
        HttpRequest.post(tradeRecordelist, URL.STORAGE_MANAGE_DELETED, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<Object>>() { });
                if (response.isSuccess()) {
                    ToastUtils.showSuccess("删除成功");
                    requestDataList();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}
