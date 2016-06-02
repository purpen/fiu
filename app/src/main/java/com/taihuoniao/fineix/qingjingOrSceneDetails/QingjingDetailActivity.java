package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneDetailCommentAdapter;
import com.taihuoniao.fineix.adapters.SceneDetailUserHeadAdapter;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingjingDetailBean;
import com.taihuoniao.fineix.beans.QingjingSubsBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.MapNearByQJActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/25.
 */
public class QingjingDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    //上个界面传递过来的情景id
    private String id;
    private boolean isCreate;//判断是不是从创建页面跳转过来
    //界面下的控件
    private ProgressBar progressBar;
    private ImageView backImg;
    private ImageView createImg;
    private RelativeLayout imgRelative;
    private ImageView backgroundImg;
    private TextView qingjingTitle;
    private TextView locationTv;
    private TextView timeTv;
    private LinearLayout leftLabel;
    private ImageView userHead;
    private TextView userName;
    private TextView userInfo;
    private TextView subscriptionCount;
    private TextView desTv;
    private LinearLayout labelLinear;
    private RelativeLayout headRelative;
    private TextView viewCount;
    private TextView loveCountTv;
    private ImageView commentImg;
    private TextView commentNum;
    private ImageView moreImg;
    private GridViewForScrollView userHeadGrid;
    private List<CommonBean.CommonItem> headList;
    private SceneDetailUserHeadAdapter sceneDetailUserHeadAdapter;
    private TextView moreUser;
    private LinearLayout emptyView;
    private Button createBtn;
    private ListView changjingListView;
    private List<SceneListBean> sceneList;
    private SceneListViewAdapter sceneListViewAdapter;
    //    private LinearLayout subLinear;
//    private ImageView subsImg;
    private LinearLayout addressLinear;
    //    private TextView subsTv;
    private List<CommentsBean.CommentItem> commentList;
    private SceneDetailCommentAdapter sceneDetailCommentAdapter;
    private TextView allComment;
    private ImageView moreComment;
    private ImageView love;
    private TextView loveTv;
    //网络请求对话框
    private SVProgressHUD dialog;
    private com.taihuoniao.fineix.beans.QingjingDetailBean.UserInfo netUserInfo;
    private String[] locaiton = null;//服务器返回经纬度
    //图片加载
    private DisplayImageOptions options, options750_1334;
    //场景列表页码
    private int currentPage = 1;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;
    //当前用户是否已经订阅
    private int is_subscript = 0;

    public QingjingDetailActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_qingjingdetail);
        backImg = (ImageView) findViewById(R.id.activity_qingjingdetail_back);
        createImg = (ImageView) findViewById(R.id.activity_qingjingdetail_create);
        changjingListView = (ListView) findViewById(R.id.activity_qingjingdetail_listview);
        progressBar = (ProgressBar) findViewById(R.id.activity_qingjingdetail_progress);
//        subLinear = (LinearLayout) findViewById(R.id.activity_qingjingdetail_sublinear);
//        subsImg = (ImageView) findViewById(R.id.activity_qingjingdetail_subsimg);
//        subsTv = (TextView) findViewById(R.id.activity_qingjingdetail_substv);
        //headerView中的控件
        View header = View.inflate(QingjingDetailActivity.this, R.layout.header_qingjing_detail, null);
        backgroundImg = (ImageView) header.findViewById(R.id.activity_qingjingdetail_background);
        qingjingTitle = (TextView) header.findViewById(R.id.activity_qingjingdetail_qingjing_title);
        locationTv = (TextView) header.findViewById(R.id.activity_qingjingdetail_location);
        addressLinear = (LinearLayout) header.findViewById(R.id.activity_qingjingdetail_addresslinear);
        timeTv = (TextView) header.findViewById(R.id.activity_qingjingdetail_time);
        leftLabel = (LinearLayout) header.findViewById(R.id.activity_qingjingdetail_leftlabel);
        userHead = (ImageView) header.findViewById(R.id.activity_qingjingdetail_userhead);
        userName = (TextView) header.findViewById(R.id.activity_qingjingdetail_username);
        userInfo = (TextView) header.findViewById(R.id.activity_qingjingdetail_userinfo);
        subscriptionCount = (TextView) header.findViewById(R.id.activity_qingjingdetail_subsnum);
        headRelative = (RelativeLayout) header.findViewById(R.id.header_qingjing_detail_head_relative);
        desTv = (TextView) header.findViewById(R.id.activity_qingjingdetail_des);
        labelLinear = (LinearLayout) header.findViewById(R.id.activity_qingjingdetail_labellinear);
        userHeadGrid = (GridViewForScrollView) header.findViewById(R.id.activity_qingjingdetail_grid);
        moreUser = (TextView) header.findViewById(R.id.activity_qingjingdetail_more_user);
        emptyView = (LinearLayout) header.findViewById(R.id.activity_qingjingdetail_emptyview);
        createBtn = (Button) header.findViewById(R.id.activity_qingjingdetail_createscene);
        changjingListView.addHeaderView(header);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        dialog = new SVProgressHUD(QingjingDetailActivity.this);
    }

    @Override
    protected void initList() {
        MainApplication.which_activity = DataConstants.QingjingDetailActivity;
        id = getIntent().getStringExtra("id");
        isCreate = getIntent().getBooleanExtra("create", false);
        if (isCreate) {
            backImg.setImageResource(R.mipmap.cancel_black);
        }
        if (id == null) {
            new SVProgressHUD(this).showErrorWithStatus("没有这个情景");
//            Toast.makeText(QingjingDetailActivity.this, "没有这个情景", Toast.LENGTH_SHORT).show();
            finish();
        }
        backImg.setOnClickListener(this);
        createImg.setOnClickListener(this);
        ViewGroup.LayoutParams lp = backgroundImg.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 16 / 9;
        backgroundImg.setLayoutParams(lp);
        backgroundImg.setFocusable(true);
        backgroundImg.setFocusableInTouchMode(true);
        backgroundImg.requestFocus();
        addressLinear.setOnClickListener(this);
        leftLabel.setOnClickListener(this);
        headList = new ArrayList<>();
        sceneDetailUserHeadAdapter = new SceneDetailUserHeadAdapter(QingjingDetailActivity.this, headList);
        userHeadGrid.setAdapter(sceneDetailUserHeadAdapter);
        userHeadGrid.setOnItemClickListener(this);
        moreUser.setOnClickListener(this);
        createBtn.setOnClickListener(this);
        sceneList = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(QingjingDetailActivity.this, sceneList, null, null, null);
        changjingListView.setAdapter(sceneListViewAdapter);
        changjingListView.setOnScrollListener(this);
//        changjingListView.setOnTouchListener(this);
        changjingListView.setOnItemClickListener(this);
//        subLinear.setOnClickListener(this);
        subscriptionCount.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(DataConstants.BroadQingjingDetail);
        registerReceiver(qingjingReceiver, filter);
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.qingjingDetails(id, handler);
        DataPaser.commonList(1 + "", 14 + "", id, null, "scene", "subscription", handler);
        DataPaser.getSceneList(currentPage + "", null, id, null, null, null, null, handler);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CANCEL_SUBS_QINGJING:
                    QingjingSubsBean netQingjingSubs = (QingjingSubsBean) msg.obj;
                    if (netQingjingSubs.isSuccess()) {
                        DataPaser.commonList(1 + "", 14 + "", id, null, "scene", "subscription", handler);
                        is_subscript = 0;
                        subscriptionCount.setBackgroundResource(R.mipmap.sub_qingjing);
//                        subsImg.setImageResource(R.mipmap.subscribe_height_49px);
                        subscriptionCount.setText(String.format("%d人订阅", netQingjingSubs.getData().getSubscription_count()));
                        moreUser.setText(String.format("%d+", netQingjingSubs.getData().getSubscription_count()));
                        if (netQingjingSubs.getData().getSubscription_count() > 14) {
                            moreUser.setVisibility(View.VISIBLE);
                        } else {
                            moreUser.setVisibility(View.GONE);
                        }
                    } else {
                        dialog.dismiss();
                        dialog.showErrorWithStatus(netQingjingSubs.getMessage());
//                        Toast.makeText(QingjingDetailActivity.this, netQingjingSubs.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.SUBS_QINGJING:
                    QingjingSubsBean netQingjingSubsBean = (QingjingSubsBean) msg.obj;
                    if (netQingjingSubsBean.isSuccess()) {
                        DataPaser.commonList(1 + "", 14 + "", id, null, "scene", "subscription", handler);
                        is_subscript = 1;
                        subscriptionCount.setBackgroundResource(R.mipmap.subed_qingjing);
//                        subsImg.setImageResource(R.mipmap.subs_yes);
                        subscriptionCount.setText(String.format("%d人订阅", netQingjingSubsBean.getData().getSubscription_count()));
                        moreUser.setText(String.format("%d+", netQingjingSubsBean.getData().getSubscription_count()));
                        if (netQingjingSubsBean.getData().getSubscription_count() > 14) {
                            moreUser.setVisibility(View.VISIBLE);
                        } else {
                            moreUser.setVisibility(View.GONE);
                        }
                    } else {
                        dialog.dismiss();
                        dialog.showErrorWithStatus(netQingjingSubsBean.getMessage());
//                        Toast.makeText(QingjingDetailActivity.this, netQingjingSubsBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.SCENE_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    SceneList netSceneList = (SceneList) msg.obj;
                    if (netSceneList.isSuccess()) {
                        if (currentPage == 1) {
                            sceneList.clear();
                            lastSavedFirstVisibleItem = -1;
                            lastTotalItem = -1;
                        }
                        sceneList.addAll(netSceneList.getSceneListBeanList());
                        if (sceneList.size() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                        sceneListViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.COMMON_LIST:
                    dialog.dismiss();
                    CommonBean netCommonBean = (CommonBean) msg.obj;
                    if (netCommonBean.isSuccess()) {
                        headList.clear();
                        headList.addAll(netCommonBean.getData().getRows());
                        if (headList.size() <= 0) {
                            headRelative.setVisibility(View.GONE);
                        } else {
                            headRelative.setVisibility(View.VISIBLE);
                        }
                        sceneDetailUserHeadAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.QINGJING_DETAILS:
                    dialog.dismiss();
                    QingjingDetailBean netQingjingDetailBean = (QingjingDetailBean) msg.obj;
                    if (netQingjingDetailBean.isSuccess()) {
//                        Log.e("<<<", "cover_url=" + netQingjingDetailBean.getData().getCover_url());
                        QingjingDetailBean = netQingjingDetailBean;
                        ImageLoader.getInstance().displayImage(netQingjingDetailBean.getData().getCover_url(), backgroundImg, options750_1334);
                        SpannableStringBuilder style = new SpannableStringBuilder(netQingjingDetailBean.getData().getTitle());
                        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(getResources().getColor(R.color.black));
                        style.setSpan(backgroundColorSpan, 0, netQingjingDetailBean.getData().getTitle().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        qingjingTitle.setText(style);
                        locationTv.setText(netQingjingDetailBean.getData().getAddress());
                        timeTv.setText(netQingjingDetailBean.getData().getCreated_at());
                        netUserInfo = netQingjingDetailBean.getData().getUser_info();
                        ImageLoader.getInstance().displayImage(netQingjingDetailBean.getData().getUser_info().getAvatar_url(), userHead, options);
                        userName.setText(netQingjingDetailBean.getData().getUser_info().getNickname());
                        isSpertAndSummary(userInfo, netQingjingDetailBean.getData().getUser_info().getIs_expert(), netQingjingDetailBean.getData().getUser_info().getSummary());
                        subscriptionCount.setText(String.format("%d人订阅", netQingjingDetailBean.getData().getSubscription_count()));
                        moreUser.setText(String.format("%d+", netQingjingDetailBean.getData().getSubscription_count()));
                        desTv.setText(netQingjingDetailBean.getData().getDes());
                        locaiton = new String[]{netQingjingDetailBean.getData().getLocation().getCoordinates().get(0), netQingjingDetailBean.getData()
                                .getLocation().getCoordinates().get(1)};
                        //添加标签
                        addLabelToLinear(netQingjingDetailBean.getData().getTag_titles(), netQingjingDetailBean.getData().getTags());
                        is_subscript = netQingjingDetailBean.getData().getIs_subscript();
                        switch (is_subscript) {
                            case 1:
                                subscriptionCount.setBackgroundResource(R.mipmap.subed_qingjing);
//                                subsImg.setImageResource(R.mipmap.subs_yes);
//                                subsTv.setText("取消订阅");
                                break;
                            default:
                                subscriptionCount.setBackgroundResource(R.mipmap.sub_qingjing);
//                                subsImg.setImageResource(R.mipmap.subscribe_height_49px);
//                                subsTv.setText("+订阅此情景");
                                break;
                        }
                        if (netQingjingDetailBean.getData().getSubscription_count() <= 0) {
                            headRelative.setVisibility(View.GONE);
                        } else {
                            headRelative.setVisibility(View.VISIBLE);
                        }
                        if (netQingjingDetailBean.getData().getSubscription_count() > 14) {
                            moreUser.setVisibility(View.VISIBLE);
                        } else {
                            moreUser.setVisibility(View.GONE);
                        }
                    } else {
                        dialog.showErrorWithStatus(netQingjingDetailBean.getMessage());
//                        Toast.makeText(QingjingDetailActivity.this, netQingjingDetailBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private QingjingDetailBean QingjingDetailBean;//网络请求返回数据

    private void addLabelToLinear(final List<String> tagsTitleList, List<Integer> tagsList) {
        for (int i = 0; i < tagsTitleList.size(); i++) {
            View view = View.inflate(QingjingDetailActivity.this, R.layout.view_horizontal_label_item, null);
            TextView textView = (TextView) view.findViewById(R.id.view_horizontal_label_item_tv);
            textView.setText(tagsTitleList.get(i));
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.rightMargin = DensityUtils.dp2px(QingjingDetailActivity.this, 10);
            view.setLayoutParams(lp);
            view.setTag(tagsList.get(i));
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QingjingDetailActivity.this, SearchActivity.class);
                    intent.putExtra("q", tagsTitleList.get(finalI));
                    intent.putExtra("t", "8");
                    startActivity(intent);
                }
            });
            labelLinear.addView(view);
        }
    }

    private void isSpertAndSummary(TextView userInfo, String isSpert, String summary) {
        if ("1".equals(isSpert) && (summary == null || "null".equals(summary))) {
            userInfo.setText("达人");
        } else if ("1".equals(isSpert)) {
            userInfo.setText(String.format("%s | %s", "达人", summary));
        } else if (summary == null || "null".equals(summary)) {
            userInfo.setText("");
        } else {
            userInfo.setText(summary);
        }
    }

    @Override
    protected void onDestroy() {
        //cancelNet();
        unregisterReceiver(qingjingReceiver);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_qingjingdetail_addresslinear:
                if (locaiton == null) {
                    dialog.show();
                    DataPaser.qingjingDetails(id, handler);
                    return;
                }
                if (QingjingDetailBean == null) {
                    dialog.show();
                    DataPaser.qingjingDetails(id, handler);
                    return;
                }
                String address = QingjingDetailBean.getData().getAddress();
                LatLng ll = new LatLng(Double.parseDouble(locaiton[1]), Double.parseDouble(locaiton[0]));
                Intent intent = new Intent(QingjingDetailActivity.this, MapNearByQJActivity.class);
                intent.putExtra(MapNearByQJActivity.class.getSimpleName(), ll);
                intent.putExtra("address", address);
                startActivity(intent);
                break;
            case R.id.activity_qingjingdetail_leftlabel:
                if (netUserInfo == null) {
                    dialog.show();
                    DataPaser.qingjingDetails(id, handler);
                    return;
                }
                Intent intent1 = new Intent(QingjingDetailActivity.this, UserCenterActivity.class);
                intent1.putExtra(FocusActivity.USER_ID_EXTRA, Long.parseLong(netUserInfo.getUser_id()));
                startActivity(intent1);
                break;
            case R.id.activity_qingjingdetail_back:
                onBackPressed();
                break;
            case R.id.activity_qingjingdetail_more_user:
                break;
            case R.id.activity_qingjingdetail_createscene:
            case R.id.activity_qingjingdetail_create:
                MainApplication.whichQingjing = QingjingDetailBean;
                MainApplication.tag = 1;
                startActivity(new Intent(QingjingDetailActivity.this, SelectPhotoOrCameraActivity.class));
                break;
            case R.id.activity_qingjingdetail_subsnum:
//            case R.id.activity_qingjingdetail_sublinear:
                if (!LoginInfo.isUserLogin()) {
//                    Toast.makeText(QingjingDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.QingjingDetailActivity;
                    LoginCompleteUtils.id = id;
                    startActivity(new Intent(QingjingDetailActivity.this, OptRegisterLoginActivity.class));
                    return;
                }
                dialog.show();
                switch (is_subscript) {
                    case 1:
                        DataPaser.cancelSubsQingjing(id, handler);
                        break;
                    default:
                        DataPaser.subsQingjing(id, handler);
                        break;
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.activity_qingjingdetail_listview:
                SceneListBean sceneListBean = (SceneListBean) changjingListView.getAdapter().getItem(position);
                Intent intent = new Intent(QingjingDetailActivity.this, SceneDetailActivity.class);
                intent.putExtra("id", sceneListBean.get_id());
                startActivity(intent);
                break;
            case R.id.activity_qingjingdetail_grid:
                Log.e("<<<", "点击position=" + position);
                Intent intent1 = new Intent(QingjingDetailActivity.this, UserCenterActivity.class);
                intent1.putExtra(FocusActivity.USER_ID_EXTRA, Long.parseLong(headList.get(position).getUser().getUser_id()));
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //由于添加了headerview的原因，所以visibleitemcount要大于1，正常只需要大于0就可以
        if (visibleItemCount > changjingListView.getHeaderViewsCount() && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                ) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            currentPage++;
            progressBar.setVisibility(View.VISIBLE);
            DataPaser.getSceneList(currentPage + "", null, id, null, null, null, null, handler);
        }
    }

    private BroadcastReceiver qingjingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dialog.show();
            DataPaser.qingjingDetails(id, handler);
            currentPage = 1;
            DataPaser.getSceneList(currentPage + "", null, id, null, null, null, null, handler);
        }
    };

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startP.x = event.getX();
//                startP.y = event.getY();
//                RelativeLayout.LayoutParams lpd = (RelativeLayout.LayoutParams) subLinear.getLayoutParams();
//                cha = startP.y + lpd.bottomMargin;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                nowP.x = event.getX();
//                nowP.y = event.getY();
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) subLinear.getLayoutParams();
//                lp.bottomMargin = (int) (cha - nowP.y);
//                if (nowP.y > startP.y) {
//                    backImg.setVisibility(View.VISIBLE);
//                    createImg.setVisibility(View.VISIBLE);
//                } else if (nowP.y < startP.y) {
//                    backImg.setVisibility(View.GONE);
//                    createImg.setVisibility(View.GONE);
//                }
//                if (lp.bottomMargin > 0) {
//                    lp.bottomMargin = 0;
//                    cha = nowP.y + lp.bottomMargin;
//                } else if (lp.bottomMargin < -subLinear.getMeasuredHeight()) {
//                    lp.bottomMargin = -subLinear.getMeasuredHeight();
//                    cha = nowP.y + lp.bottomMargin;
//                }
//                subLinear.setLayoutParams(lp);
//                break;
//        }
//        return false;
//    }
//
//    private double cha;//手指按下的时候，y与bottomMargin的差值
//    private PointF startP = new PointF();
//    private PointF nowP = new PointF();
}
