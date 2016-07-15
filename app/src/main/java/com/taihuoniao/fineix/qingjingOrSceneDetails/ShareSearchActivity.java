package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WriteJsonToSD;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/6/2.
 */
public class ShareSearchActivity extends BaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    //上个界面传递过来的数据
//    private SceneDetailsBean scene;
    private String imgUrl;
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
//        scene = (SceneDetailsBean) getIntent().getSerializableExtra("scene");
//        if (scene != null) {
//            ImageLoader.getInstance().displayImage(scene.getData().getCover_url(), backImg);
//        }
        imgUrl = getIntent().getStringExtra("url");
//        ImageLoader.getInstance().displayImage(imgUrl, backImg);
        ImageLoader.getInstance().loadImage(imgUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    try {
                        blur(loadedImage, backImg, 20);
                    } catch (Exception e) {
                        backImg.setImageBitmap(loadedImage);
                    }
                } else {
                    backImg.setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
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
                        ToastUtils.showInfo("请输入搜索关键字");
//                        new SVProgressHUD(ShareSearchActivity.this).showInfoWithStatus("请输入搜索关键字");
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view, float radius) throws Exception {
        Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
        RenderScript rs = RenderScript.create(this);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur;
        blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
        rs.destroy();
    }

    private List<SearchBean.SearchItem> list = new ArrayList<>();
    private ShareCJSelectListAdapter shareCJSelectListAdapter;
    private int page = 1;//搜索页码
    private String searchStr;

    private void search() {
        ClientDiscoverAPI.search(searchStr, 11 + "", page + "", "content", 0 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<搜索标题", responseInfo.result);
                WriteJsonToSD.writeToSD("json",responseInfo.result);
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
//                    new SVProgressHUD(ShareSearchActivity.this).showErrorWithStatus(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
//                new SVProgressHUD(ShareSearchActivity.this).showErrorWithStatus("网络错误");
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
//        scene.getData().setOid(searchItem.getOid());
//        scene.getData().setTitle(searchItem.getTitle());
//        scene.getData().setDes(searchItem.getDes());
        Intent intent = new Intent();
        intent.putExtra("scene", searchItem);
        setResult(222, intent);
        finish();
    }
}
