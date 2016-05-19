package com.taihuoniao.fineix.scene;

import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SlidingTabPageAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.CustomViewPager;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 站内搜索界面
 *
 * @author lilin
 *         created at 2016/4/21 17:56
 */
public class SearchResultActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    //上个界面传递过来的数据
    private String q;//搜索关键字
    private String t;//搜索什么 7.商品；8.情景；9.场景；10.情景产品；11.场景分享语
    private boolean isSearch;//判断是不是从添加产品页面跳转过来的
//    private String evt;//搜索方式 content:内容；tag:标签搜索(情景需要传此参数)

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.custom_sliding_tab)
    CustomSlidingTab custom_sliding_tab;
    @Bind(R.id.custom_view_pager)
    CustomViewPager custom_view_pager;
    private String[] titles = {"情景", "场景", "产品"};
    //    private Class[] clazzs = {QJResultFragment.class, CJResultFragment.class, ProductResultFragment.class};
    private SlidingTabPageAdapter slidingTabPageAdapter;

    @Override
    protected void getIntentData() {
        q = getIntent().getStringExtra("q");
        t = getIntent().getStringExtra("t");
        isSearch = getIntent().getBooleanExtra("isSearch",false);
//        evt = getIntent().getStringExtra("evt");
        Log.e("<<<sousuo", "q=" + q + ",t=" + t);
    }

    public SearchResultActivity() {
        super(R.layout.activity_search_result);
    }

    @Override
    protected void initView() {
        custom_head.setSearchShow(true);
        custom_head.setHeadRightTxtShow(true, R.string.cancel);
        custom_sliding_tab.setTextColor(getResources().getColor(R.color.color_333));
        custom_sliding_tab.setTypeface(null, Typeface.NORMAL);
        custom_sliding_tab.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp14));
        slidingTabPageAdapter = new SlidingTabPageAdapter(getSupportFragmentManager(), Arrays.asList(titles), q, t);
        custom_view_pager.setAdapter(slidingTabPageAdapter);
        custom_sliding_tab.setViewPager(custom_view_pager);
        custom_sliding_tab.setOnPageChangeListener(this);
        custom_head.getHeadRightTV().setFocusable(true);
        custom_head.getHeadRightTV().setFocusableInTouchMode(true);
        custom_head.getHeadRightTV().requestFocus();
    }


    @Override
    protected void installListener() {
        custom_head.getSearchET().setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (SearchResultActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchResultActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //开始搜索
                    slidingTabPageAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    @Override
    protected void initList() {
        if (t != null) {
            switch (t) {
                case "9":
                    custom_view_pager.setCurrentItem(1);
                    break;
                case "10":
                    custom_view_pager.setCurrentItem(2);
                    break;
                default:
                    custom_view_pager.setCurrentItem(0);
                    break;
            }
        }

    }

    @OnClick({R.id.tv_head_right, R.id.ib_search})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn:
                custom_head.getSearchET().setText("");
                break;
            case R.id.tv_head_right:
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 1:
                t = "9";
                break;
            case 2:
                t = "10";
                break;
            default:
                t = "8";
                break;
        }
        slidingTabPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
