package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/9/2.
 */
public class SearchEnvirActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.background_img)
    ImageView backgroundImg;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.edit_text)
    EditText editText;
    @Bind(R.id.search_delete)
    ImageView searchDelete;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private WaittingDialog dialog;
    private int page = 1;
    private List<SearchBean.Data.SearchItem> list;
    private ShareCJSelectListAdapter shareCJSelectListAdapter;

    public SearchEnvirActivity() {
        super(R.layout.activity_search_envir);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        backgroundImg.setImageBitmap(MainApplication.blurBitmap);
        titleLayout.setTitle("搜索语境");
        titleLayout.setContinueTvVisible(false);
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
                    if (getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //开始搜索
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        ToastUtils.showInfo("请输入搜索关键字");
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
        searchDelete.setOnClickListener(this);
        ListView listView = pullRefreshView.getRefreshableView();
        pullRefreshView.setPullToRefreshEnabled(false);
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                search();
            }
        });
        list = new ArrayList<>();
        shareCJSelectListAdapter = new ShareCJSelectListAdapter(this, list);
        listView.setDividerHeight(0);
        listView.setSelector(R.color.nothing);
        listView.setAdapter(shareCJSelectListAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_delete:
                editText.setText("");
                break;
        }
    }

    private void search() {
        RequestParams params = ClientDiscoverAPI.getsearchRequestParams(editText.getText().toString(), 11 + "", null, page + "", "8", "content", 0 + "");
      Call httpHandler =  HttpRequest.post(params, URL.SEARCH, new GlobalDataCallBack(){
//     HttpHandler<String> httpHandler= ClientDiscoverAPI.search(editText.getText().toString(), 11 + "", null, page + "", "8", "content", 0 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                SearchBean netSearch = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    netSearch = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                        pullRefreshView.lastTotalItem = -1;
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    list.addAll(netSearch.getData().getRows());
                    shareCJSelectListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchBean.Data.SearchItem searchItem = list.get(position);
        if (searchItem == null) {
            return;
        }
        String titleIntent = searchItem.getTitle();
        String desIntent = searchItem.getDes();
        Intent intent = new Intent();
        intent.putExtra("title", titleIntent);
        intent.putExtra("des", desIntent);
        setResult(3, intent);
        onBackPressed();
    }
}
