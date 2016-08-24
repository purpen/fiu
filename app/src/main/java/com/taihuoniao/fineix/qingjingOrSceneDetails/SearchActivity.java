package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchBrandFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchProductFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchQJFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchSubjectFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchUserFragment;
import com.taihuoniao.fineix.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/7.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {


    //上个界面传递过来的数据
    private String q;//搜索关键字
    private String t;//搜索什么 3.商品;9.情境；11.情境语境；12.主题；13.品牌；14.用户

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
    private SearchViewPagerAdapter searchViewPagerAdapter;

    public SearchActivity() {
        super(R.layout.activity_search1);
    }

    @Override
    protected void initView() {
        q = getIntent().getStringExtra("q");
        t = getIntent().getStringExtra("t");
        if (TextUtils.isEmpty(t)) {
            t = "9";
        }
        if (!TextUtils.isEmpty(q)) {
            editText.setText(q);
            isContent = false;
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
                    //开始搜索
                    refreshData();
                }
                return false;
            }
        });
        WindowUtils.chenjin(this);
    }

    @Override
    protected void initList() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        //添加tab
        tabLayout.addTab(tabLayout.newTab().setText("情景"), "9".equals(t));
        titleList.add("情景");
        searchQJFragment = SearchQJFragment.newInstance(q, isContent);
        fragmentList.add(searchQJFragment);
        //添加tab
        tabLayout.addTab(tabLayout.newTab().setText("用户"), "14".equals(t));
        titleList.add("用户");
        searchUserFragment = SearchUserFragment.newInstance(q, isContent);
        fragmentList.add(searchUserFragment);
        //添加tab
        tabLayout.addTab(tabLayout.newTab().setText("产品"), "3".equals(t));
        titleList.add("产品");
        searchProductFragment = SearchProductFragment.newInstance(q, isContent);
        fragmentList.add(searchProductFragment);
        //添加tab
        tabLayout.addTab(tabLayout.newTab().setText("品牌"), "13".equals(t));
        titleList.add("品牌");
        searchBrandFragment = SearchBrandFragment.newInstance(q, isContent);
        fragmentList.add(searchBrandFragment);
        //添加tab
        tabLayout.addTab(tabLayout.newTab().setText("主题"), "12".equals(t));
        titleList.add("主题");
        searchSubjectFragment = SearchSubjectFragment.newInstance(q, isContent);
        fragmentList.add(searchSubjectFragment);
        //设置适配器
        searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(searchViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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

    private void refreshData() {
        for(SearchFragment searchFragment:fragmentList){
            searchFragment.refreshData(editText.getText().toString());
        }
    }

}
