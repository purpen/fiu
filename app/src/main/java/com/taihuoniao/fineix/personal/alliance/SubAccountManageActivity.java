package com.taihuoniao.fineix.personal.alliance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.personal.alliance.adpter.AddSubAcountAdapter;
import com.taihuoniao.fineix.utils.DPUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.swipelistview.PullToRefreshSwipeMenuListView;
import com.taihuoniao.fineix.view.swipelistview.SwipeMenu;
import com.taihuoniao.fineix.view.swipelistview.SwipeMenuCreator;
import com.taihuoniao.fineix.view.swipelistview.SwipeMenuItem;

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

    private void initSwipeMenuListView() {

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                }
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(SubAccountManageActivity.this);
                item1.setBackground(R.color.black20);
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
                // TODO: 2017/4/27 删除操作
            }
        });

        mAddSubAcountAdapter = new AddSubAcountAdapter(SubAccountManageActivity.this);
        pullToRefreshSwipeMenuListView1.setAdapter(mAddSubAcountAdapter);
        pullToRefreshSwipeMenuListView1.setOnItemClickListener(this);
        pullToRefreshSwipeMenuListView1.setPullLoadEnable(false);
        pullToRefreshSwipeMenuListView1.setPullRefreshEnable(true);
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
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.linearLayout1, R.id.linearLayout2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linearLayout1:
                startActivity(new Intent(SubAccountManageActivity.this, AddSubAcountActivity.class
                ));
                break;
            case R.id.linearLayout2:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
