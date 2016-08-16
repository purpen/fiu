package com.taihuoniao.fineix.scene;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/15.
 */
public class SearchBrandActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.search_cancel)
    TextView searchCancel;
    @Bind(R.id.search_delete)
    ImageView searchDelete;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    private ListView listView;

    public SearchBrandActivity() {
        super(R.layout.activity_search_brand);
    }

    @Override
    protected void initView() {
        listView = pullRefreshView.getRefreshableView();
    }

    @Override
    protected void initList() {
        searchCancel.setOnClickListener(this);
        searchDelete.setOnClickListener(this);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchEditText.getText().toString().length() > 0) {
                    searchDelete.setVisibility(View.VISIBLE);
                } else {
                    searchDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchEditText.setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (SearchBrandActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchBrandActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    if (TextUtils.isEmpty(searchEditText.getText().toString().trim())) {
                        return false;
                    } else {
                        ToastUtils.showError("搜索品牌");
                    }
                }
                return false;
            }
        });
        pullRefreshView.setPullToRefreshEnabled(false);
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancel:
                if (searchEditText.getText().toString().length() > 0) {
                    searchEditText.setText("");
                } else {
                    finish();
                }
                break;
            case R.id.search_delete:
                searchEditText.setText("");
                break;
        }
    }
}
