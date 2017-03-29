package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.WellGoodsFragment;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchBrandFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchProductFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchQJFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchSubjectFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchUserFragment;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.WindowUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/7.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    //上个界面传递过来的数据
    private String q;//搜索关键字
    private int t;//搜索什么 7.商品;9.情境；11.情境语境；12.主题；13.品牌；14.用户

    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.search_delete)
    ImageView searchDelete;
    @Bind(R.id.edit_text)
    EditText editText;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private boolean isContent = false;
    //fragment
    private SearchQJFragment searchQJFragment;
    private SearchUserFragment searchUserFragment;
    private SearchProductFragment searchProductFragment;
    private SearchBrandFragment searchBrandFragment;
    private SearchSubjectFragment searchSubjectFragment;
    private List<SearchFragment> fragmentList;
    private List<String> titleList;

    public SearchActivity() {
        super(R.layout.activity_search1);
    }

    @Override
    protected void initView() {
        q = getIntent().getStringExtra("q");
        t = getIntent().getIntExtra("t", 7);
        if (!TextUtils.isEmpty(q)) {
            editText.setText(q);
            isContent = false;
            cancel.setFocusable(true);
            cancel.setFocusableInTouchMode(true);
            cancel.requestFocus();
        } else {
            isContent = true;
        }
        searchDelete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    searchDelete.setVisibility(View.VISIBLE);
                } else {
                    searchDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (SearchActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    currentStr = editText.getText().toString();
                    //开始搜索
                    refreshData();
                }
                return false;
            }
        });
        WindowUtils.chenjin(this);
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadSearch);
        registerReceiver(searchReceiver, intentFilter);
    }

    @Override
    protected void initList() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();

        //添加tab 7
        titleList.add("产品");
        searchProductFragment = SearchProductFragment.newInstance(q, isContent);
        fragmentList.add(searchProductFragment);

        if (!getIntent().hasExtra(WellGoodsFragment.class.getSimpleName())) {

            //添加tab 14
            titleList.add("用户");
            searchUserFragment = SearchUserFragment.newInstance(q, isContent);
            fragmentList.add(searchUserFragment);
            if (getIntent().hasExtra(FindFragment.class.getSimpleName())) {
                return;
            }
        }

        //添加tab 13
        titleList.add("品牌");
        searchBrandFragment = SearchBrandFragment.newInstance(q, isContent);
        fragmentList.add(searchBrandFragment);
        if (getIntent().hasExtra(WellGoodsFragment.class.getSimpleName())) {
            return;
        }
        //添加tab 12
        titleList.add("主题");
        searchSubjectFragment = SearchSubjectFragment.newInstance(q, isContent);
        fragmentList.add(searchSubjectFragment);

        if (!getIntent().hasExtra(WellGoodsFragment.class.getSimpleName())) {
            //添加tab 9
            titleList.add("情境");
            searchQJFragment = SearchQJFragment.newInstance(q, isContent);
            fragmentList.add(searchQJFragment);
        }
    }

    @Override
    protected void requestNet() {
        //设置适配器
        SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(searchViewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setViewPager(viewPager);
        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    setIndicatorWidth();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    tabLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
//        tabLayout.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        switch (t) {
            case 14:
                viewPager.setCurrentItem(fragmentList.indexOf(searchUserFragment), false);
                break;
            case 7:
                viewPager.setCurrentItem(fragmentList.indexOf(searchProductFragment), false);
                break;
            case 9:
                viewPager.setCurrentItem(fragmentList.indexOf(searchQJFragment), false);
                break;
            case 13:
                viewPager.setCurrentItem(fragmentList.indexOf(searchBrandFragment), false);
                break;
            case 12:
                viewPager.setCurrentItem(fragmentList.indexOf(searchSubjectFragment), false);
                break;
            default:
                viewPager.setCurrentItem(fragmentList.indexOf(searchProductFragment), false);
                break;
        }
    }

    private void setIndicatorWidth() throws NoSuchFieldException, IllegalAccessException {
//        int margin = activity.getResources().getDimensionPixelSize(R.dimen.dp15);
        if (fragmentList.size() <= 0) {
            return;
        }
        int margin = (int) (((double) tabLayout.getMeasuredWidth() / fragmentList.size() - DensityUtils.sp2px(this, 42)) / 2);
        Class<?> tablayout = tabLayout.getClass();
        Field tabStrip = tablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayout);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(margin);
                params.setMarginEnd(margin);
            } else {
                params.setMargins(margin, 0, margin, 0);
            }
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_delete:
                editText.setText("");
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    private String currentStr = "";

    private void refreshData() {
//        fragmentList.get(viewPager.getCurrentItem()).refreshData(currentStr);
        for (SearchFragment searchFragment : fragmentList) {
            searchFragment.refreshData(currentStr);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(searchReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver searchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshData();
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        fragmentList.get(position).refreshData(currentStr);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
