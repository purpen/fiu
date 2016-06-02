package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/6/2.
 */
public class ShareSearchActivity extends BaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    //上个界面传递过来的数据
    private SceneDetails scene;
    @Bind(R.id.activity_share_select_img)
    ImageView backImg;
    @Bind(R.id.activity_share_select_titlelayout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_share_search_edittext)
    EditText editText;
    @Bind(R.id.activity_share_search_delete)
    ImageView deleteImg;
    @Bind(R.id.activity_share_select_listview)
    ListView listView;
    @Bind(R.id.activity_share_select_progress)
    ProgressBar progressBar;
    WaittingDialog dialog;

    public ShareSearchActivity() {
        super(R.layout.activity_share_search);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        scene = (SceneDetails) getIntent().getSerializableExtra("scene");
        if (scene != null) {
            ImageLoader.getInstance().displayImage(scene.getCover_url(), backImg);
        }
        titleLayout.setTitleVisible(false);
        titleLayout.setContinueTvVisible(false);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
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
                    if (ShareSearchActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ShareSearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //开始搜索
                    searchStr = editText.getText().toString();
                    if (TextUtils.isEmpty(searchStr)) {
                        new SVProgressHUD(ShareSearchActivity.this).showInfoWithStatus("请输入搜索关键字");
                        return false;
                    }
                    page = 1;
                    dialog.show();
                    search();
                }
                return false;
            }
        });
        shareCJSelectListAdapter = new ShareCJSelectListAdapter(this, list);
        listView.setAdapter(shareCJSelectListAdapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
    }

    private List<SearchBean.SearchItem> list = new ArrayList<>();
    private ShareCJSelectListAdapter shareCJSelectListAdapter;
    private int page = 1;//搜索页码
    private String searchStr;

    private void search() {
        ClientDiscoverAPI.search(searchStr, 11 + "", page + "", "content", 0 + "", new RequestCallBack<String>() {
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
                    new SVProgressHUD(ShareSearchActivity.this).showErrorWithStatus(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                new SVProgressHUD(ShareSearchActivity.this).showErrorWithStatus("网络错误");
            }
        });
    }

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
            search();
        }
    }

    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchBean.SearchItem searchItem = (SearchBean.SearchItem) listView.getAdapter().getItem(position);
        scene.setOid(searchItem.getOid());
        scene.setTitle(searchItem.getTitle());
        scene.setDes(searchItem.getDes());
        Intent intent = new Intent();
        intent.putExtra("scene", scene);
        setResult(222, intent);
        finish();
    }
}
