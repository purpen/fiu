package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;

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
    private ImageView deleteImg;
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
        if (!dialog.isShowing()) {
            dialog.show();
        }
//        DataPaser.categoryList(1 + "", 10 + "", 1 + "", handler);
        ClientDiscoverAPI.categoryList(1 + "", 10 + "", 1 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                dialog.dismiss();
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<分类列表", "数据解析异常" + e.toString());
                }
//                handler.sendMessage(msg);
                if (categoryListBean.isSuccess()) {
                    addProductViewPagerAdapter = new AddProductViewPagerAdapter(getSupportFragmentManager(), categoryListBean);
                    viewPager.setAdapter(addProductViewPagerAdapter);
                    slidingTab.setViewPager(viewPager);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
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
        deleteImg = (ImageView) findViewById(R.id.activity_add_product_delete);
        cancelTv = (TextView) findViewById(R.id.activity_add_product_cancel);
        dialog = new WaittingDialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.black));
            tintManager.setStatusBarTintEnabled(true);
        }
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
                    if (AddProductActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddProductActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //开始搜索
//                    Log.e("<<", "点击搜索");
                    if (TextUtils.isEmpty(editText.getText().toString().trim())) {
//                        Log.e("<<<", "为空");
                        return false;
                    } else {
                        isSearch = true;
                        Intent intent = new Intent(DataConstants.BroadSearchFragment);
                        intent.putExtra("pos", viewPager.getCurrentItem());
                        intent.putExtra("q", editText.getText().toString().trim());
                        intent.putExtra("search", isSearch);
                        sendBroadcast(intent);
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
        deleteImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_add_product_delete:
            case R.id.activity_add_product_cancel:
                if (editText.getText().toString().length() > 0) {
                    editText.setText("");
                }else{
                    onBackPressed();
                }
                break;
        }
    }


    public interface CancelSearch {
        void cancelSearch();
    }

}
