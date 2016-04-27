package com.taihuoniao.fineix.scene;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SlidingTabPageAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.scene.fragments.CJResultFragment;
import com.taihuoniao.fineix.scene.fragments.ProductResultFragment;
import com.taihuoniao.fineix.scene.fragments.QJResultFragment;
import com.taihuoniao.fineix.scene.fragments.UserResultFragment;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.CustomViewPager;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 站内搜索界面
 * @author lilin
 *         created at 2016/4/21 17:56
 */
public class SearchResultActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.custom_sliding_tab)
    CustomSlidingTab custom_sliding_tab;
    @Bind(R.id.custom_view_pager)
    CustomViewPager custom_view_pager;
    private String[] titles = {"情景", "场景", "用户", "产品"};
    private Class[] clazzs={QJResultFragment.class, CJResultFragment.class, UserResultFragment.class, ProductResultFragment.class};

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
        custom_view_pager.setAdapter(new SlidingTabPageAdapter(getSupportFragmentManager(),clazzs,Arrays.asList(titles)));
        custom_sliding_tab.setViewPager(custom_view_pager);
    }


    @Override
    protected void installListener() {
        custom_head.getSearchET().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                String keyWord = cs.toString().trim();
                if (!TextUtils.isEmpty(keyWord)) {

                } else {

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick({R.id.tv_head_right, R.id.ib_search})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn:
                custom_head.getSearchET().getText().clear();
                break;
            case R.id.tv_head_right:
                finish();
                break;
        }
    }

}
