package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.SearchEnvirAdapter;
import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.blurview.BlurView;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/6/2.
 */
public class ShareSearchActivity extends BaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, EditRecyclerAdapter.ItemClick, View.OnClickListener {

    @Bind(R.id.blur_view)
    BlurView blurView;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.search1)
    ImageView search1;
    @Bind(R.id.search_delete)
    ImageView searchDelete;
    @Bind(R.id.edit_text)
    EditText editText;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.line)
    TextView line;
    @Bind(R.id.linear1)
    LinearLayout linear1;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.confirm)
    TextView confirm;
    private WaittingDialog dialog;
    //语境分类
    private List<CategoryListBean.CategoryListItem> envirList;
    private SearchEnvirAdapter searchEnvirAdapter;

    public ShareSearchActivity() {
        super(R.layout.activity_share_search);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        titleLayout.setBackImgVisible(false);
        titleLayout.setTitle(R.string.add_envir,getResources().getColor(R.color.white));
        titleLayout.setContinueTvVisible(false);
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
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
                    if (ShareSearchActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ShareSearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //开始搜索
                    searchStr = editText.getText().toString();
                    if (TextUtils.isEmpty(searchStr)) {
                        ToastUtils.showInfo("请输入搜索关键字");
//                        new SVProgressHUD(ShareSearchActivity.this).showInfoWithStatus("请输入搜索关键字");
                        return false;
                    }
                    page = 1;
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    search();
                }
                return false;
            }
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        envirList = new ArrayList<>();
        searchEnvirAdapter = new SearchEnvirAdapter(this, envirList, this);
        recyclerView.setAdapter(searchEnvirAdapter);
        shareCJSelectListAdapter = new ShareCJSelectListAdapter(this, list);
        listView.setAdapter(shareCJSelectListAdapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        searchDelete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        categoryList();
    }



    private List<SearchBean.Data.SearchItem> list = new ArrayList<>();
    private ShareCJSelectListAdapter shareCJSelectListAdapter;
    private int page = 1;//搜索页码
    private String searchStr;


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > 0 && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                ) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            page++;
            progressBar.setVisibility(View.VISIBLE);
            if (editText.getText().toString().length() > 0) {
                search();
            } else {
                envirList();
            }
        }
    }

    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchBean.Data.SearchItem searchItem = (SearchBean.Data.SearchItem) listView.getAdapter().getItem(position);
        Intent intent = new Intent();
        intent.putExtra("scene", searchItem);
        setResult(222, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_to_bottom);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.slide_out_to_bottom);
    }

    private String cid;


    @Override
    public void click(int postion) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        for (int i = 0; i < envirList.size(); i++) {
            if (i == postion) {
                envirList.get(i).setIsSelect(true);
            } else {
                envirList.get(i).setIsSelect(false);
            }
        }
        searchEnvirAdapter.notifyDataSetChanged();
        cid = envirList.get(postion).get_id();
        page = 1;
        if (editText.getText().toString().length() > 0) {
            search();
        } else {
            envirList();
        }
    }

    private void search() {
        ClientDiscoverAPI.search(searchStr, 11 + "", cid, page + "", "content", 0 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                SearchBean netSearch = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    netSearch = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                        lastTotalItem = -1;
                        lastSavedFirstVisibleItem = -1;
                    }
                    list.addAll(netSearch.getData().getRows());
                    shareCJSelectListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    //分类列表
    private void categoryList() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.categoryList(1 + "", 11 + "", 1 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<分类列表", "数据解析异常" + e.toString());
                }
                if (categoryListBean.isSuccess()) {
                    envirList.addAll(categoryListBean.getData().getRows());
                    searchEnvirAdapter.notifyDataSetChanged();
                    click(0);
                } else {
                    ToastUtils.showError(categoryListBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //语境列表
    private void envirList() {
        ClientDiscoverAPI.envirList(page + "", 8 + "", 1 + "", cid, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                SearchBean netSearch = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    netSearch = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                        lastTotalItem = -1;
                        lastSavedFirstVisibleItem = -1;
                    }
                    list.addAll(netSearch.getData().getRows());
                    shareCJSelectListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_delete:
                editText.setText("");
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.confirm:
                ToastUtils.showError("确定");
                break;
        }
    }
}
