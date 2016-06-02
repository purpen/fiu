package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class ShareCJSelectActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    //上个界面传递过来的数据
    private SceneDetails scene;
    @Bind(R.id.activity_share_select_titlelayout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_share_select_search)
    LinearLayout searchLinear;
    @Bind(R.id.activity_share_select_img)
    ImageView imageView;
    @Bind(R.id.activity_share_select_title)
    TextView titleTv;
    @Bind(R.id.activity_share_select_scene_title_cancelimg)
    ImageView deleteTitleImg;
    @Bind(R.id.activity_share_select_des)
    TextView desTv;
    @Bind(R.id.activity_share_select_scene_des_cancelimg)
    ImageView deleteDesImg;
    @Bind(R.id.activity_share_select_listview)
//    PullToRefreshListView pullToRefreshView;
            ListView listView;
    @Bind(R.id.activity_share_select_progress)
    ProgressBar progressBar;
    //    private ListView listView;
    private SVProgressHUD dialog;
    private int currentPage = 1;
    private List<SearchBean.SearchItem> list = new ArrayList<>();
    private ShareCJSelectListAdapter shareCJSelectListAdapter;

    public ShareCJSelectActivity() {
        super(R.layout.activity_share_select);
    }

    @Override
    protected void initView() {
        titleLayout.setTitleVisible(false);
        titleLayout.setRightTv(R.string.complete, getResources().getColor(R.color.white), this);
        scene = (SceneDetails) getIntent().getSerializableExtra("scene");
        if (MainApplication.shareBitmap == null || scene == null) {
            new SVProgressHUD(this).showErrorWithStatus("数据异常，请重试");
//            Toast.makeText(ShareCJSelectActivity.this, "数据异常，请返回重试", Toast.LENGTH_SHORT).show();
            finish();
        }
        ImageLoader.getInstance().displayImage(scene.getCover_url(), imageView);
//        imageView.setImageBitmap(MainApplication.shareBitmap);
        titleTv.setText(scene.getTitle());
        desTv.setText(scene.getDes());
        dialog = new SVProgressHUD(ShareCJSelectActivity.this);
        listView.setOnScrollListener(this);
//        listView = pullToRefreshView.getRefreshableView();
//        pullToRefreshView.setPullToRefreshEnabled(false);
//        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//            @Override
//            public void onLastItemVisible() {
//                if (searchStr == null) {
//                    return;
//                }
//                progressBar.setVisibility(View.VISIBLE);
//                currentPage++;
//                DataPaser.search(searchStr, 11 + "", currentPage + "", "tag", 0 + "", handler);
//            }
//        });
        shareCJSelectListAdapter = new ShareCJSelectListAdapter(this, list);
        listView.setAdapter(shareCJSelectListAdapter);
        listView.setOnItemClickListener(this);
        searchLinear.setOnClickListener(this);
    }

    private String searchStr;

    @Override
    protected void requestNet() {
        dialog.show();
        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < scene.getTag_titles().size(); i++) {
            tags.append(",").append(scene.getTag_titles().get(i));
        }
        if (tags.length() <= 1) {
            return;
        }
        tags.deleteCharAt(0);
        searchStr = tags.toString();
        DataPaser.search(tags.toString(), 11 + "", currentPage + "", "tag", 0 + "", handler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_share_select_search:
                Intent intent1 = new Intent(this, ShareSearchActivity.class);
                intent1.putExtra("scene", scene);
                startActivityForResult(intent1, 111);
                break;
            case R.id.title_continue:
                if (!isSelect) {
                    onBackPressed();
                } else {
//                    DataPaser.commitShareCJ(oid,handler);
                    Intent intent = new Intent();
                    intent.putExtra("scene", scene);
                    setResult(2, intent);
                    finish();
                    MainApplication.shareBitmap = null;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case 222:
                SceneDetails resultScene = (SceneDetails) data.getSerializableExtra("scene");
                scene = resultScene;
                Intent intent = new Intent();
                intent.putExtra("scene", scene);
                setResult(2, intent);
                finish();
                MainApplication.shareBitmap = null;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        MainApplication.shareBitmap = null;
        super.onBackPressed();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            progressBar.setVisibility(View.GONE);
            switch (msg.what) {
                case DataConstants.SEARCH_LIST:
                    SearchBean netSearch = (SearchBean) msg.obj;
                    if (netSearch.isSuccess()) {
                        if (currentPage == 1) {
                            list.clear();
                            lastTotalItem = -1;
                            lastSavedFirstVisibleItem = -1;
                        }
                        list.addAll(netSearch.getData().getRows());
                        shareCJSelectListAdapter.notifyDataSetChanged();
                    } else {
                        dialog.showErrorWithStatus(netSearch.getMessage());
//                        Toast.makeText(ShareCJSelectActivity.this, netSearch.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.showErrorWithStatus("网络错误");
//                    Toast.makeText(ShareCJSelectActivity.this, R.string.host_failure, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    private boolean isSelect = false;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchBean.SearchItem searchItem = (SearchBean.SearchItem) listView.getAdapter().getItem(position);
        titleTv.setText(searchItem.getTitle());
        desTv.setText(searchItem.getDes());
        isSelect = true;
        scene.setOid(searchItem.getOid());
        scene.setTitle(searchItem.getTitle());
        scene.setDes(searchItem.getDes());
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //由于添加了headerview的原因，正常只需要大于0就可以
        if (visibleItemCount > 0 && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                ) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            currentPage++;
            progressBar.setVisibility(View.VISIBLE);
            DataPaser.search(searchStr, 11 + "", currentPage + "", "tag", 0 + "", handler);
        }
    }

    private int lastTotalItem = -1;
    private int lastSavedFirstVisibleItem = -1;
}
