package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

/**
 * Created by taihuoniao on 2016/3/22.
 */
public class AddProductActivity extends BaseActivity implements View.OnClickListener {
    private GlobalTitleLayout titleLayout;
    private CustomSlidingTab slidingTab;
    private AddProductViewPagerAdapter addProductViewPagerAdapter;
    private ViewPager viewPager;
    private RelativeLayout search;
    private EditText editText;
    private TextView cancelTv;
    private WaittingDialog dialog;
    private int searchPage = 1;
    //viewpager当前页面
//    private int pos = 0;
    //判断是不是搜索状态下的标识
    private boolean isSearch = false;

    public AddProductActivity() {
        super(0);
    }

    @Override
    protected void requestNet() {
        //获取分类列表
        dialog.show();
        DataPaser.categoryList(1 + "", 10 + "", handler);
    }

    @Override
    protected void initList() {
        titleLayout.setTitle(R.string.add_product, getResources().getColor(R.color.black333333));
        titleLayout.setBackgroundColor(getResources().getColor(R.color.white));
        titleLayout.setContinueTvVisible(false);
        titleLayout.setBackImgVisible(false);
        titleLayout.setCancelImgVisible(true);
        titleLayout.setCancelImg(R.mipmap.cancel_black);
        slidingTab.setIndicatorColor(getResources().getColor(R.color.yellow_bd8913));
        slidingTab.setTextColor(getResources().getColor(R.color.black333333));
        slidingTab.setCurTabTextColor(getResources().getColor(R.color.yellow_bd8913));
        slidingTab.setTypeface(null, Typeface.NORMAL);
        slidingTab.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp14));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_product);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_add_product_title);
        slidingTab = (CustomSlidingTab) findViewById(R.id.activity_add_product_slidingtab);
        viewPager = (ViewPager) findViewById(R.id.activity_add_product_viewpager);
        search = (RelativeLayout) findViewById(R.id.rl);
        editText = (EditText) findViewById(R.id.activity_add_product_edit);
        cancelTv = (TextView) findViewById(R.id.activity_add_product_cancel);
        dialog = new WaittingDialog(this);
    }

    @Override
    protected void installListener() {
//        search.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cancelTv.setVisibility(View.VISIBLE);
                } else {
                    cancelTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (AddProductActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddProductActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //开始搜索
                    Log.e("<<", "点击搜索");
                    if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                        Log.e("<<<", "为空");
                        return false;
                    } else {
                        isSearch = true;
                        Intent intent = new Intent(DataConstants.BroadSearchFragment);
                        intent.putExtra("pos", viewPager.getCurrentItem());
                        intent.putExtra("q", editText.getText().toString().trim());
                        intent.putExtra("search", isSearch);
                        sendBroadcast(intent);
                        Log.e("<<<", "发送广播");
//                        DataPaser.search(editText.getText().toString().trim(), searchPage + "", "10", null, handler);
                    }
                }
                return false;
            }
        });
        cancelTv.setOnClickListener(this);
        slidingTab.setAddProductActivity(true, new CancelSearch() {
            @Override
            public void cancelSearch() {
                Intent intent = new Intent(DataConstants.BroadSearchFragment);
                intent.putExtra("search", false);
                sendBroadcast(intent);
                editText.setText("");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_add_product_cancel:
//                isSearch = false;
//                Intent intent = new Intent(DataConstants.BroadSearchFragment);
//                intent.putExtra("pos", viewPager.getCurrentItem());
//                intent.putExtra("search", isSearch);
//                sendBroadcast(intent);
                editText.setText("");
//                Log.e("<<<", "取消搜索");
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CATEGORY_LIST:
                    dialog.dismiss();
                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
                    if (netCategoryBean.isSuccess()) {
                        addProductViewPagerAdapter = new AddProductViewPagerAdapter(getSupportFragmentManager(), netCategoryBean);
                        viewPager.setAdapter(addProductViewPagerAdapter);
                        slidingTab.setViewPager(viewPager);
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    public interface CancelSearch{
        void cancelSearch();
    }

    @Override
    protected void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
