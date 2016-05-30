package com.taihuoniao.fineix.scene;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.scene.fragments.CJResultFragment;
import com.taihuoniao.fineix.scene.fragments.ProductResultFragment;
import com.taihuoniao.fineix.scene.fragments.QJResultFragment;

/**
 * Created by taihuoniao on 2016/5/7.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的数据
    private String q;//搜索关键字
    private String t;//搜索什么 7.商品；8.情景；9.场景；10.情景产品；11.场景分享语
    private boolean isSearch = false;
    //界面下的控件
    private ImageView backImg;
    private EditText editText;
    private ImageView deleteImg;
    private TextView cancelTv;
    private RelativeLayout qjRelative;
    private TextView qjTv;
    private TextView qjLine;
    private RelativeLayout cjRelative;
    private TextView cjTv;
    private TextView cjLine;
    private RelativeLayout productRelative;
    private TextView productTv;
    private TextView productLine;
    //fragment
    private FragmentManager fm;
    private QJResultFragment qjResultFragment;
    private CJResultFragment cjResultFragment;
    private ProductResultFragment productResultFragment;

    public SearchActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        overridePendingTransition(R.anim.in_from_left,0);
        setContentView(R.layout.activity_search);
        backImg = (ImageView) findViewById(R.id.activity_search_back);
        editText = (EditText) findViewById(R.id.activity_search_edittext);
        deleteImg = (ImageView) findViewById(R.id.activity_search_delete);
        cancelTv = (TextView) findViewById(R.id.activity_search_cancel);
        qjRelative = (RelativeLayout) findViewById(R.id.activity_search_qjrelative);
        qjTv = (TextView) findViewById(R.id.activity_search_qjtv);
        qjLine = (TextView) findViewById(R.id.activity_search_qjline);
        cjRelative = (RelativeLayout) findViewById(R.id.activity_search_cjrelative);
        cjTv = (TextView) findViewById(R.id.activity_search_cjtv);
        cjLine = (TextView) findViewById(R.id.activity_search_cjline);
        productRelative = (RelativeLayout) findViewById(R.id.activity_search_productrelative);
        productTv = (TextView) findViewById(R.id.activity_search_producttv);
        productLine = (TextView) findViewById(R.id.activity_search_productline);
        fm = getSupportFragmentManager();
    }

    @Override
    protected void initList() {
        q = getIntent().getStringExtra("q");
        t = getIntent().getStringExtra("t");
        isSearch = getIntent().getBooleanExtra("isSearch", false);
        if (!TextUtils.isEmpty(q)) {
            editText.setText(q);
        }
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        backImg.setOnClickListener(this);
        backImg.setFocusable(true);
        backImg.setFocusableInTouchMode(true);
        backImg.requestFocus();
        deleteImg.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteImg.setVisibility(View.VISIBLE);
                } else {
                    deleteImg.setVisibility(View.GONE);
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
        qjRelative.setOnClickListener(this);
        cjRelative.setOnClickListener(this);
        productRelative.setOnClickListener(this);
        selectFragment();
//        refreshData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_search_delete:
                editText.setText("");
                break;
            case R.id.activity_search_productrelative:
                t = "10";
                if (productResultFragment != null) {
                    selectFragment();
                    refreshData();
                } else {
                    selectFragment();
                }
                break;
            case R.id.activity_search_cjrelative:
                t = "9";
                if (cjResultFragment != null) {
                    selectFragment();
                    refreshData();
                } else {
                    selectFragment();
                }
                break;
            case R.id.activity_search_qjrelative:
                t = "8";
                if (qjResultFragment != null) {
                    selectFragment();
                    refreshData();
                } else {
                    selectFragment();
                }
                break;
            case R.id.activity_search_cancel:
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setText("");
                } else {
                    onBackPressed();
                }
                break;
            case R.id.activity_search_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.out_from_left);
    }

    private void selectFragment() {
        if (t == null) {
            t = 8 + "";
        }
        FragmentTransaction ft = fm.beginTransaction();
        goneColor();
        goneFragment();
        switch (t) {
            case "8":
                if (qjResultFragment == null) {
                    qjResultFragment = QJResultFragment.newInstance(editText.getText().toString().trim(), t);
                    ft.add(R.id.activity_search_container, qjResultFragment);
                } else {
                    ft.show(qjResultFragment);
                }
                qjTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                qjLine.setVisibility(View.VISIBLE);
                break;
            case "9":
                if (cjResultFragment == null) {
                    cjResultFragment = CJResultFragment.newInstance(editText.getText().toString().trim(), t);
                    ft.add(R.id.activity_search_container, cjResultFragment);
                } else {
                    ft.show(cjResultFragment);
                }
                cjTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                cjLine.setVisibility(View.VISIBLE);
                break;
            case "10":
                if (productResultFragment == null) {
                    productResultFragment = ProductResultFragment.newInstance(editText.getText().toString().trim(), t, false);
                    ft.add(R.id.activity_search_container, productResultFragment);
                } else {
                    ft.show(productResultFragment);
                }
                productTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                productLine.setVisibility(View.VISIBLE);
                break;
        }
        ft.commit();
    }

    private void refreshData() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            return;
        }
        if (qjResultFragment != null && "8".equals(t)) {
            qjResultFragment.refreshData(editText.getText().toString().trim(), t);
        }
        if (cjResultFragment != null && "9".equals(t)) {
            cjResultFragment.refreshData(editText.getText().toString().trim(), t);
        }
        if (productResultFragment != null && "10".equals(t)) {
            productResultFragment.refreshData(editText.getText().toString().trim(), t);
        }

    }

    private void goneColor() {
        qjTv.setTextColor(getResources().getColor(R.color.black333333));
        qjLine.setVisibility(View.GONE);
        cjTv.setTextColor(getResources().getColor(R.color.black333333));
        cjLine.setVisibility(View.GONE);
        productTv.setTextColor(getResources().getColor(R.color.black333333));
        productLine.setVisibility(View.GONE);
    }

    private void goneFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (qjResultFragment != null) {
            ft.hide(qjResultFragment);
        }
        if (cjResultFragment != null) {
            ft.hide(cjResultFragment);
        }
        if (productResultFragment != null) {
            ft.hide(productResultFragment);
        }
        ft.commit();
    }
}
