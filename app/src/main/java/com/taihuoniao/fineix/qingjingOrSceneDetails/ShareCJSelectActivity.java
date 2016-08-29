//package com.taihuoniao.fineix.qingjingOrSceneDetails;
//
//import android.annotation.TargetApi;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Build;
//import android.renderscript.Allocation;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicBlur;
//import android.util.Log;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
//import com.taihuoniao.fineix.base.BaseActivity;
//import com.taihuoniao.fineix.beans.SceneDetailsBean;
//import com.taihuoniao.fineix.beans.SearchBean;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.view.GlobalTitleLayout;
//import com.taihuoniao.fineix.view.WaittingDialog;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//
///**
// * Created by taihuoniao on 2016/5/25.
// */
//public class ShareCJSelectActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
//    //上个界面传递过来的数据
//    private SceneDetailsBean scene;
//    @Bind(R.id.activity_share_select_titlelayout)
//    GlobalTitleLayout titleLayout;
//    @Bind(R.id.activity_share_select_search)
//    LinearLayout searchLinear;
//    @Bind(R.id.activity_share_select_img)
//    ImageView imageView;
//    @Bind(R.id.activity_share_select_title)
//    TextView titleTv;
//    @Bind(R.id.activity_share_select_scene_title_cancelimg)
//    ImageView deleteTitleImg;
//    @Bind(R.id.activity_share_select_des)
//    TextView desTv;
//    @Bind(R.id.activity_share_select_scene_des_cancelimg)
//    ImageView deleteDesImg;
//    @Bind(R.id.activity_share_select_listview)
////    PullToRefreshListView pullToRefreshView;
//            ListView listView;
//    @Bind(R.id.activity_share_select_progress)
//    ProgressBar progressBar;
//    //    private ListView listView;
//    private WaittingDialog dialog;
//    private int currentPage = 1;
//    private List<SearchBean.Data.SearchItem> list = new ArrayList<>();
//    private ShareCJSelectListAdapter shareCJSelectListAdapter;
//
//    public ShareCJSelectActivity() {
//        super(R.layout.activity_share_select);
//    }
//
//    @Override
//    protected void initView() {
//        titleLayout.setTitleVisible(false);
//        titleLayout.setBackImgVisible(false);
//        titleLayout.setRightTv(R.string.complete, getResources().getColor(R.color.white), this);
//        scene = (SceneDetailsBean) getIntent().getSerializableExtra("scene");
//        if (scene == null) {
//            ToastUtils.showError("数据异常，请重试");
//            finish();
//        }
//        ImageLoader.getInstance().loadImage(scene.getData().getCover_url(), options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
////                ToastUtils.showError("图片加载失败");
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                imageView.setImageBitmap(blurImageAmeliorate(loadedImage));
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    try {
//                        blur(loadedImage, imageView, 20);
//                    } catch (Exception e) {
//                        imageView.setImageBitmap(loadedImage);
//                    }
//                } else {
//                    imageView.setImageBitmap(loadedImage);
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
////                ToastUtils.showError("图片加载失败");
//            }
//        });
//        titleTv.setText(scene.getData().getTitle());
//        desTv.setText(scene.getData().getDes());
//        dialog = new WaittingDialog(ShareCJSelectActivity.this);
//        listView.setOnScrollListener(this);
//        shareCJSelectListAdapter = new ShareCJSelectListAdapter(this, list);
//        listView.setAdapter(shareCJSelectListAdapter);
//        listView.setOnItemClickListener(this);
//        searchLinear.setOnClickListener(this);
//    }
//
//    private String searchStr;
//
//    @Override
//    protected void requestNet() {
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//        StringBuilder tags = new StringBuilder();
//        for (int i = 0; i < scene.getData().getTag_titles().size(); i++) {
//            tags.append(",").append(scene.getData().getTag_titles().get(i));
//        }
//        if (tags.length() <= 1) {
//            return;
//        }
//        tags.deleteCharAt(0);
//        searchStr = tags.toString();
//        search(tags.toString(), 11 + "", currentPage + "", "tag", 0 + "");
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void blur(Bitmap bkg, View view, float radius) throws Exception {
//        Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(overlay);
//        canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
//        RenderScript rs = RenderScript.create(this);
//        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
//        ScriptIntrinsicBlur blur;
//        blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
//        blur.setInput(overlayAlloc);
//        blur.setRadius(radius);
//        blur.forEach(overlayAlloc);
//        overlayAlloc.copyTo(overlay);
//        view.setBackground(new BitmapDrawable(getResources(), overlay));
//        rs.destroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.activity_share_select_search:
//                Intent intent1 = new Intent(this, ShareSearchActivity.class);
//                intent1.putExtra("scene", scene);
//                startActivityForResult(intent1, 111);
//                break;
//            case R.id.title_continue:
//                if (!isSelect) {
//                    onBackPressed();
//                } else {
////                    DataPaser.commitShareCJ(oid,handler);
//                    Intent intent = new Intent();
//                    intent.putExtra("scene", scene);
//                    setResult(2, intent);
//                    finish();
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null) {
//            return;
//        }
//        switch (resultCode) {
//            case 222:
//                scene = (SceneDetailsBean) data.getSerializableExtra("scene");
//                Intent intent = new Intent();
//                intent.putExtra("scene", scene);
//                setResult(2, intent);
//                finish();
//                break;
//        }
//    }
//
//
////    private Handler handler = new Handler() {
////        @Override
////        public void handleMessage(Message msg) {
////
////            switch (msg.what) {
////                case DataConstants.SEARCH_LIST:
////                    dialog.dismiss();
////                    progressBar.setVisibility(View.GONE);
////                    SearchBean netSearch = (SearchBean) msg.obj;
////                    if (netSearch.isSuccess()) {
////                        if (currentPage == 1) {
////                            list.clear();
////                            lastTotalItem = -1;
////                            lastSavedFirstVisibleItem = -1;
////                        }
////                        list.addAll(netSearch.getData().getRows());
////                        shareCJSelectListAdapter.notifyDataSetChanged();
////                    } else {
////                        ToastUtils.showError(netSearch.getMessage());
////                    }
////                    break;
////                case DataConstants.NET_FAIL:
////                    dialog.dismiss();
////                    progressBar.setVisibility(View.GONE);
////                    ToastUtils.showError("网络错误");
////                    break;
////            }
////        }
////    };
//
//
//    private boolean isSelect = false;
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        SearchBean.Data.SearchItem searchItem = (SearchBean.Data.SearchItem) listView.getAdapter().getItem(position);
////        titleTv.setText(searchItem.getTitle());
////        desTv.setText(searchItem.getDes());
//        isSelect = true;
////        scene.getData().setOid(searchItem.getOid());
//        scene.getData().setTitle(searchItem.getTitle());
//        scene.getData().setDes(searchItem.getDes());
//        Intent intent = new Intent();
//        intent.putExtra("scene", scene);
//        setResult(2, intent);
//        finish();
//    }
//
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        //由于添加了headerview的原因，正常只需要大于0就可以
//        if (visibleItemCount > 0 && (firstVisibleItem + visibleItemCount >= totalItemCount)
//                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
//                ) {
//            lastSavedFirstVisibleItem = firstVisibleItem;
//            lastTotalItem = totalItemCount;
//            currentPage++;
//            progressBar.setVisibility(View.VISIBLE);
//            search(searchStr, 11 + "", currentPage + "", "tag", 0 + "");
//        }
//    }
//
//    private int lastTotalItem = -1;
//    private int lastSavedFirstVisibleItem = -1;
//
//    //搜索列表
//    private void search(String q, String t, String page, String evt, String sort) {
//        ClientDiscoverAPI.search(q, t,null, page, evt, sort, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                SearchBean searchBean = new SearchBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<SearchBean>() {
//                    }.getType();
//                    searchBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<", "数据解析异常" + e.toString());
//                }
//                dialog.dismiss();
//                progressBar.setVisibility(View.GONE);
//                SearchBean netSearch = searchBean;
//                if (netSearch.isSuccess()) {
//                    if (currentPage == 1) {
//                        list.clear();
//                        lastTotalItem = -1;
//                        lastSavedFirstVisibleItem = -1;
//                    }
//                    list.addAll(netSearch.getData().getRows());
//                    shareCJSelectListAdapter.notifyDataSetChanged();
//                } else {
//                    ToastUtils.showError(netSearch.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                    dialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    ToastUtils.showError("网络错误");
//            }
//        });
//    }
//}
