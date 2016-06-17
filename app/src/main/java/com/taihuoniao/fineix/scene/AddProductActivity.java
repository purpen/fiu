package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        dialog.show();
//        DataPaser.categoryList(1 + "", 10 + "", 1 + "", handler);
        ClientDiscoverAPI.categoryList(1 + "", 10 + "", 1 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
//                Log.e("<<<>", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
//                Message msg = handler.obtainMessage();
//                msg.what = DataConstants.CATEGORY_LIST;
                CategoryBean categoryBean = new CategoryBean();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    categoryBean.setSuccess(job.optBoolean("success"));
                    categoryBean.setMessage(job.optString("message"));
//                    categoryBean.setStatus(job.optString("status"));
                    if (categoryBean.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        JSONArray rows = data.getJSONArray("rows");
                        List<CategoryListBean> list = new ArrayList<CategoryListBean>();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject ob = rows.getJSONObject(i);
                            CategoryListBean categoryListBean = new CategoryListBean();
                            categoryListBean.set_id(ob.optString("_id"));
                            categoryListBean.setTitle(ob.optString("title"));
                            categoryListBean.setName(ob.optString("name"));
                            categoryListBean.setTag_id(ob.optString("tag_id"));
                            categoryListBean.setApp_cover_s_url(ob.optString("app_cover_s_url"));
                            categoryListBean.setApp_cover_url(ob.optString("app_cover_url"));
                            list.add(categoryListBean);
                        }
                        categoryBean.setList(list);
                    }
//                    msg.obj = categoryBean;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                handler.sendMessage(msg);
                if (categoryBean.isSuccess()) {
                    addProductViewPagerAdapter = new AddProductViewPagerAdapter(getSupportFragmentManager(), categoryBean);
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
        deleteImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_add_product_delete:
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

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DataConstants.CATEGORY_LIST:
//                    dialog.dismiss();
//                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
//                    if (netCategoryBean.isSuccess()) {
//                        addProductViewPagerAdapter = new AddProductViewPagerAdapter(getSupportFragmentManager(), netCategoryBean);
//                        viewPager.setAdapter(addProductViewPagerAdapter);
//                        slidingTab.setViewPager(viewPager);
//                    }
//                    break;
//                case DataConstants.NET_FAIL:
//                    dialog.dismiss();
//                    break;
//            }
//        }
//    };

    public interface CancelSearch {
        void cancelSearch();
    }

//    @Override
//    protected void onDestroy() {
//        //        cancelNet();
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
//        super.onDestroy();
//    }
}
