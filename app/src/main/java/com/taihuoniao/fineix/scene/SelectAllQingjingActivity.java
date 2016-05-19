package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllQingjingGridAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/9.
 */
public class SelectAllQingjingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //上个界面传递过来的数据
    private String isSearch;//判断是搜索还是展示 0展示 1搜索
    private LatLng latLng;//当前位置经纬度
    @Bind(R.id.activity_select_allqj_titlelayout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_select_allqj_searchlinear)
    LinearLayout searchLinear;
    @Bind(R.id.activity_select_allqj_edit)
    EditText editText;
    @Bind(R.id.activity_select_allqj_pullrefreshview)
    PullToRefreshGridView pullToRefreshView;
    GridView gridView;
    @Bind(R.id.activity_select_allqj_progress)
    ProgressBar progressBar;
    private WaittingDialog dialog;
    //情景列表
    private int page = 1;
    private double distance = 5000;//搜索范围
    private List<QingJingListBean.QingJingItem> qingjingList;
    private List<SearchBean.SearchItem> searchList;
    private AllQingjingGridAdapter allQingjingGridAdapter;
    //搜索情景
    private String q;

    @Override
    protected void getIntentData() {
        isSearch = getIntent().getStringExtra("isSearch");
        latLng = getIntent().getParcelableExtra("latLng");
        if (isSearch == null) {
            isSearch = "0";
        }
        if (latLng == null) {
            Toast.makeText(SelectAllQingjingActivity.this, "无法获得您当前位置信息", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public SelectAllQingjingActivity() {
        super(R.layout.activity_select_allqj);
    }

    @Override
    protected void initView() {
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setTitle(R.string.select_qingjing, getResources().getColor(R.color.black333333));
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), this);
        switch (isSearch) {
            case "0":
                searchLinear.setVisibility(View.GONE);
                break;
            case "1":
                searchLinear.setVisibility(View.VISIBLE);
                break;
        }
        editText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (SelectAllQingjingActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SelectAllQingjingActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //开始搜索
                    q = editText.getText().toString().trim();
                    page = 1;
                    dialog.show();
                    DataPaser.search(q, 8 + "", page + "","tag", handler);
                }
                return false;
            }
        });
        gridView = pullToRefreshView.getRefreshableView();
        pullToRefreshView.setPullToRefreshEnabled(false);
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                if (isSearch.equals("0")) {
                    DataPaser.qingjingList(page + "", 0 + "", distance + "", latLng.longitude + "", latLng.latitude + "", handler);
                } else if (isSearch.equals("1")) {
                    DataPaser.search(q, 8 + "", page + "","tag", handler);
                }
            }
        });
        gridView.setNumColumns(2);
        int space = DensityUtils.dp2px(SelectAllQingjingActivity.this, 5);
        gridView.setHorizontalSpacing(space);
        gridView.setVerticalSpacing(space);
        if (isSearch.equals("0")) {
            qingjingList = new ArrayList<>();
            allQingjingGridAdapter = new AllQingjingGridAdapter(qingjingList, null, SelectAllQingjingActivity.this, space);
        } else if (isSearch.equals("1")) {
            searchList = new ArrayList<>();
            allQingjingGridAdapter = new AllQingjingGridAdapter(null, searchList, SelectAllQingjingActivity.this, space);
        }
        gridView.setAdapter(allQingjingGridAdapter);
        gridView.setOnItemClickListener(this);
        dialog = new WaittingDialog(SelectAllQingjingActivity.this);
    }

    @Override
    protected void requestNet() {
        if (isSearch.equals("0")) {
            dialog.show();
            DataPaser.qingjingList(page + "", 0 + "", distance + "", latLng.longitude + "", latLng.latitude + "", handler);
        }
//        handler.sendEmptyMessageDelayed(-2, 10000);


//        ImageLoader.getInstance().loadImage("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2468356536,2310768742&fm=116&", new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                Log.e("<<<cccccccccccccccc", "url=" + imageUri);
//                HttpUtils httpUtils = new HttpUtils(60000);
//                RequestParams params = new RequestParams("utf-8");
//                ByteArrayOutputStream op = new ByteArrayOutputStream();
//                loadedImage.compress(Bitmap.CompressFormat.PNG, 40, op);
//                Log.e("<<<ccccccccccc", "size=" + op.size());
//                params.addBodyParameter("image", op.toString());
////                params.addQueryStringParameter("image", Base64Utils.encodeLines(op.toByteArray()));
//                httpUtils.send(HttpRequest.HttpMethod.POST, "http://115.28.204.191/upLoad.php", params, new RequestCallBack<String>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        Log.e("<<<ccccccccccccc", responseInfo.result);
//                    }
//
//                    @Override
//                    public void onFailure(HttpException error, String msg) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//

    }

//    public static String byte2hex(byte[] b) {
//        StringBuffer sb = new StringBuffer();
//        String stmp = "";
//        for (int n = 0; n < b.length; n++) {
//            stmp = Integer.toHexString(b[n] & 0XFF);
//            if (stmp.length() == 1) {
//                sb.append("0" + stmp);
//            } else {
//                sb.append(stmp);
//            }
//
//
//        }
//        return sb.toString();
//    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case -2:
//                    Toast.makeText(MainApplication.getContext(), "为空", Toast.LENGTH_SHORT).show();
//                    break;
                case DataConstants.SEARCH_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    SearchBean netSearch = (SearchBean) msg.obj;
                    if (netSearch.isSuccess()) {
                        if (page == 1) {
                            searchList.clear();
                            pullToRefreshView.lastTotalItem = -1;
                            pullToRefreshView.lastSavedFirstVisibleItem = -1;
                        }
                        searchList.addAll(netSearch.getData().getRows());
                        allQingjingGridAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.QINGJING_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    QingJingListBean netQingjingListBean = (QingJingListBean) msg.obj;
                    if (netQingjingListBean.isSuccess()) {
                        if (page == 1) {
                            qingjingList.clear();
                            pullToRefreshView.lastTotalItem = -1;
                            pullToRefreshView.lastSavedFirstVisibleItem = -1;
                        }
                        qingjingList.addAll(netQingjingListBean.getData().getRows());
                        allQingjingGridAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SelectAllQingjingActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
                if (isSearch.equals("0")) {
                    for (int i = 0; i < qingjingList.size(); i++) {
                        if (qingjingList.get(i).isSelect()) {
                            Intent intent1 = new Intent();
                            intent1.putExtra("qingjing", qingjingList.get(i));
                            setResult(DataConstants.RESULTCODE_SELECTQJ_ALLQJ, intent1);
                            finish();
                            break;
                        }
                    }
                    onBackPressed();
                } else if (isSearch.equals("1")) {
                    for (int i = 0; i < searchList.size(); i++) {
                        if (searchList.get(i).isSelect()) {
                            Intent intent1 = new Intent();
                            intent1.putExtra("searchqj", searchList.get(i));
                            setResult(DataConstants.RESULTCODE_SELECTQJ_SALLQJ, intent1);
                            finish();
                            break;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isSearch.equals("0")) {
            for (int i = 0; i < qingjingList.size(); i++) {
                if (position == i) {
                    qingjingList.get(i).setIsSelect(true);
                } else {
                    qingjingList.get(i).setIsSelect(false);
                }
            }
        } else if (isSearch.equals("1")) {
            for (int i = 0; i < searchList.size(); i++) {
                if (position == i) {
                    searchList.get(i).setIsSelect(true);
                } else {
                    searchList.get(i).setIsSelect(false);
                }
            }
        }
        allQingjingGridAdapter.notifyDataSetChanged();
    }
}
