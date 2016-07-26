package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneDetailCommentAdapter;
import com.taihuoniao.fineix.adapters.SceneDetailUserHeadAdapter;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.CommonBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingjingDetailBean;
import com.taihuoniao.fineix.beans.QingjingSubsBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.map.MapNearByQJActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
    private RoundedImageView userHead;
    private RoundedImageView vImg;
    private TextView userName;
    private TextView userInfo;
    private TextView subscriptionCount;
    private TextView desTv;
    private ImageView more;
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
    private WaittingDialog dialog;
    private com.taihuoniao.fineix.beans.QingjingDetailBean.DataBean.UserInfoBean netUserInfo;
    private String[] locaiton = null;//服务器返回经纬度
    //图片加载
    private DisplayImageOptions options, options750_1334;
    //场景列表页码
    private int currentPage = 1;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;
    //当前用户是否已经订阅
    private int is_subscript = 0;
    public static QingjingDetailActivity instance;

    public QingjingDetailActivity() {
        super(0);
    }

    private View activity_view;
//    选择情景点击没反应

    @Override
    protected void initView() {
        instance = QingjingDetailActivity.this;
        activity_view = View.inflate(this, R.layout.activity_qingjingdetail, null);
        setContentView(activity_view);
        backImg = (ImageView) findViewById(R.id.activity_qingjingdetail_back);
        createImg = (ImageView) findViewById(R.id.activity_qingjingdetail_create);
        changjingListView = (ListView) findViewById(R.id.activity_qingjingdetail_listview);
        progressBar = (ProgressBar) findViewById(R.id.activity_qingjingdetail_progress);
//        subLinear = (LinearLayout) findViewById(R.id.activity_qingjingdetail_sublinear);
//        subsImg = (ImageView) findViewById(R.id.activity_qingjingdetail_subsimg);
//        subsTv = (TextView) findViewById(R.id.activity_qingjingdetail_substv);
        //headerView中的控件
        View header = View.inflate(QingjingDetailActivity.this, R.layout.header_qingjing_detail, null);
        imgRelative = (RelativeLayout) header.findViewById(R.id.activity_qingjingdetail_imgrelative);
        backgroundImg = (ImageView) header.findViewById(R.id.activity_qingjingdetail_background);
        qingjingTitle = (TextView) header.findViewById(R.id.activity_qingjingdetail_qingjing_title);
        locationTv = (TextView) header.findViewById(R.id.activity_qingjingdetail_location);
        addressLinear = (LinearLayout) header.findViewById(R.id.activity_qingjingdetail_addresslinear);
        timeTv = (TextView) header.findViewById(R.id.activity_qingjingdetail_time);
        leftLabel = (LinearLayout) header.findViewById(R.id.activity_qingjingdetail_leftlabel);
        userHead = (RoundedImageView) header.findViewById(R.id.activity_qingjingdetail_userhead);
        vImg = (RoundedImageView) header.findViewById(R.id.riv_auth);
        userName = (TextView) header.findViewById(R.id.activity_qingjingdetail_username);
        userInfo = (TextView) header.findViewById(R.id.activity_qingjingdetail_userinfo);
        subscriptionCount = (TextView) header.findViewById(R.id.activity_qingjingdetail_subsnum);
        headRelative = (RelativeLayout) header.findViewById(R.id.header_qingjing_detail_head_relative);
        desTv = (TextView) header.findViewById(R.id.activity_qingjingdetail_des);
        more = (ImageView) header.findViewById(R.id.activity_qingjingdetail_more);
        labelLinear = (LinearLayout) header.findViewById(R.id.activity_qingjingdetail_labellinear);
        userHeadGrid = (GridViewForScrollView) header.findViewById(R.id.activity_qingjingdetail_grid);
        moreUser = (TextView) header.findViewById(R.id.activity_qingjingdetail_more_user);
        emptyView = (LinearLayout) header.findViewById(R.id.activity_qingjingdetail_emptyview);
        createBtn = (Button) header.findViewById(R.id.activity_qingjingdetail_createscene);
        changjingListView.addHeaderView(header);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        dialog = new WaittingDialog(QingjingDetailActivity.this);
        initPopupWindow();
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
            ToastUtils.showError("没有这个地盘");
//            new SVProgressHUD(this).showErrorWithStatus("没有这个情景");
//            Toast.makeText(QingjingDetailActivity.this, "没有这个情景", Toast.LENGTH_SHORT).show();
            finish();
        }
        backImg.setOnClickListener(this);
        createImg.setOnClickListener(this);
        ViewGroup.LayoutParams lp = backgroundImg.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = MainApplication.getContext().getScreenHeight();
        backgroundImg.setLayoutParams(lp);
//        backgroundImg.setFocusable(true);
//        backgroundImg.setFocusableInTouchMode(true);
//        backgroundImg.requestFocus();
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
        more.setOnClickListener(this);
//        subLinear.setOnClickListener(this);
        subscriptionCount.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(DataConstants.BroadQingjingDetail);
        registerReceiver(qingjingReceiver, filter);
    }

    @Override
    protected void requestNet() {
        dialog.show();
        qingjingDetails(id);
        commonList(1 + "", 14 + "", id, null, "scene", "subscription");
        getSceneList(currentPage + "", null, id, null, null, null, null, null);
    }

    private void getSceneList(String page, String size, String scene_id, String sort, String fine, final String dis, String lng, String lat) {
        ClientDiscoverAPI.getSceneList(page, size, scene_id, sort, fine, dis, lng, lat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SceneList sceneList1 = new SceneList();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    sceneList1.setSuccess(jsonObject.optBoolean("success"));
                    sceneList1.setMessage(jsonObject.optString("message"));
//                    sceneList.setStatus(jsonObject.optString("status"));
                    if (sceneList1.isSuccess()) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray rows = data.getJSONArray("rows");
                        List<SceneListBean> list = new ArrayList<>();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject job = rows.getJSONObject(i);
                            SceneListBean sceneListBean = new SceneListBean();
                            sceneListBean.set_id(job.optString("_id"));
                            sceneListBean.setAddress(job.optString("address"));
                            sceneListBean.setScene_title(job.optString("scene_title"));
                            sceneListBean.setView_count(job.optString("view_count"));
                            sceneListBean.setCreated_at(job.optString("created_at"));
                            sceneListBean.setLove_count(job.optString("love_count"));
                            sceneListBean.setCover_url(job.optString("cover_url"));
                            sceneListBean.setTitle(job.optString("title"));
                            sceneListBean.setDes(job.optString("des"));
                            JSONObject us = job.getJSONObject("user_info");
                            SceneListBean.User user = new SceneListBean.User();
                            user.setAccount(us.optString("account"));
//                            user.setLabel(us.optString("label"));
                            user.is_expert = us.optInt("is_expert");
                            user.expert_info = us.optString("expert_info");
                            user.expert_label = us.optString("expert_label");
                            user.setUser_id(us.optString("user_id"));
                            user.setSummary(us.optString("summary"));
                            user.setNickname(us.optString("nickname"));
                            user.setLove_count(us.optString("love_count"));
                            user.setFollow_count(us.optString("follow_count"));
                            user.setFans_count(us.optString("fans_count"));
//                            user.setCounter(us.optString("counter"));
                            user.setAvatar_url(us.optString("avatar_url"));
                            sceneListBean.setUser_info(user);
                            JSONArray product = job.getJSONArray("product");
                            List<SceneListBean.Products> productsList = new ArrayList<>();
                            for (int j = 0; j < product.length(); j++) {
                                JSONObject ob = product.getJSONObject(j);
                                SceneListBean.Products products = new SceneListBean.Products();
                                products.setId(ob.optString("id"));
                                products.setTitle(ob.optString("title"));
                                products.setPrice(ob.optString("price"));
                                products.setX(ob.optDouble("x"));
                                products.setY(ob.optDouble("y"));
                                productsList.add(products);
                            }
                            sceneListBean.setProductsList(productsList);
                            list.add(sceneListBean);
                        }
                        sceneList1.setSceneListBeanList(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (sceneList1.isSuccess()) {
                    if (currentPage == 1) {
                        sceneList.clear();
                        lastSavedFirstVisibleItem = -1;
                        lastTotalItem = -1;
                    }
                    sceneList.addAll(sceneList1.getSceneListBeanList());
                    if (sceneList.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                    sceneListViewAdapter.notifyDataSetChanged();
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

    private void commonList(String page, String size, String id, String user_id, String type, String event) {
        ClientDiscoverAPI.commonList(page, size, id, user_id, type, event, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CommonBean commonBean = new CommonBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CommonBean>() {
                    }.getType();
                    commonBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<通用列表>>>", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                CommonBean netCommonBean = commonBean;
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
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void qingjingDetails(String id) {
        ClientDiscoverAPI.qingjingDetails(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<情景详情", responseInfo.result);
                QingjingDetailBean qingjingDetailBean = new QingjingDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingjingDetailBean>() {
                    }.getType();
                    qingjingDetailBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常：" + e.toString());
                }
                dialog.dismiss();
                QingjingDetailBean netQingjingDetailBean = qingjingDetailBean;
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
                    if (netQingjingDetailBean.getData().getUser_info().getIs_expert() == 1) {
                        vImg.setVisibility(View.VISIBLE);
                        userInfo.setText(netQingjingDetailBean.getData().getUser_info().getExpert_label() + " | " + netQingjingDetailBean.getData().getUser_info().getExpert_info());
                    } else {
                        vImg.setVisibility(View.GONE);
                        userInfo.setText(netQingjingDetailBean.getData().getUser_info().getSummary());
                    }
                    userName.setText(netQingjingDetailBean.getData().getUser_info().getNickname());
//                        isSpertAndSummary(userInfo, netQingjingDetailBean.getData().getUser_info().getIs_expert(), netQingjingDetailBean.getData().getUser_info().getSummary());
                    subscriptionCount.setText(String.format("%d人订阅", netQingjingDetailBean.getData().getSubscription_count()));
                    moreUser.setText(String.format("%d+", netQingjingDetailBean.getData().getSubscription_count()));
                    desTv.setText(netQingjingDetailBean.getData().getDes());
                    locaiton = new String[]{netQingjingDetailBean.getData().getLocation().getCoordinates().get(0) + "", netQingjingDetailBean.getData()
                            .getLocation().getCoordinates().get(1) + ""};
                    //添加标签
                    addLabelToLinear(netQingjingDetailBean.getData().getTags());
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
                    if (netQingjingDetailBean.getCurrent_user_id() != null && netQingjingDetailBean.getCurrent_user_id().equals(netQingjingDetailBean.getData().getUser_info().getUser_id())) {
                        more.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtils.showError(netQingjingDetailBean.getMessage());
//                        dialog.showErrorWithStatus(netQingjingDetailBean.getMessage());
//                        Toast.makeText(QingjingDetailActivity.this, netQingjingDetailBean.getMessage(), Toast.LENGTH_SHORT).show();
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

    private QingjingDetailBean QingjingDetailBean;//网络请求返回数据

    private void addLabelToLinear(final List<String> tagsTitleList) {
        for (int i = 0; i < tagsTitleList.size(); i++) {
            View view = View.inflate(QingjingDetailActivity.this, R.layout.view_horizontal_label_item, null);
            TextView textView = (TextView) view.findViewById(R.id.view_horizontal_label_item_tv);
            textView.setText(tagsTitleList.get(i));
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.rightMargin = DensityUtils.dp2px(QingjingDetailActivity.this, 10);
            view.setLayoutParams(lp);
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


    @Override
    protected void onDestroy() {
        //cancelNet();
        instance = null;
        unregisterReceiver(qingjingReceiver);
        super.onDestroy();
    }

    @Override
    public void finish() {
        instance = null;
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_scene_detail_more_jubao:
                popupWindow.dismiss();
                dialog.show();
                deleteQingjing(id);
                break;
//            case R.id.popup_scene_detail_more_bianji_linear:
//                if (!LoginInfo.isUserLogin()) {
////                    Toast.makeText(QingjingDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
//                    MainApplication.which_activity = DataConstants.QingjingDetailActivity;
//                    LoginCompleteUtils.id = id;
//                    startActivity(new Intent(QingjingDetailActivity.this, OptRegisterLoginActivity.class));
//                    return;
//                }
//                Intent intent5 = new Intent(QingjingDetailActivity.this, CreateActivity.class);
//                MainApplication.tag = 2;
//                intent5.putExtra(QingjingDetailActivity.class.getSimpleName(), QingjingDetailBean);
//                startActivity(intent5);
//                break;
            case R.id.popup_scene_detail_more_cancel:
                popupWindow.dismiss();
                break;
            case R.id.activity_qingjingdetail_more:
                showPopup();
                break;
            case R.id.activity_qingjingdetail_addresslinear:
                if (locaiton == null) {
                    dialog.show();
                    qingjingDetails(id);
                    return;
                }
                if (QingjingDetailBean == null) {
                    dialog.show();
                    qingjingDetails(id);
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
                    qingjingDetails(id);
                    return;
                }
                Intent intent1 = new Intent(QingjingDetailActivity.this, UserCenterActivity.class);
                intent1.putExtra(FocusActivity.USER_ID_EXTRA, netUserInfo.getUser_id());
                startActivity(intent1);
                break;
            case R.id.activity_qingjingdetail_back:
                onBackPressed();
                break;
            case R.id.activity_qingjingdetail_more_user:
                break;
            case R.id.activity_qingjingdetail_createscene:
            case R.id.activity_qingjingdetail_create:
                if (LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = 0;
                    startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
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
                        cancelSubsQingjing(id);
                        break;
                    default:
                        subsQingjing(id);
                        break;
                }
                break;
        }
    }

    private void subsQingjing(String i) {
        ClientDiscoverAPI.subsQingjing(i, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                QingjingSubsBean qingjingSubsBean = new QingjingSubsBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingjingSubsBean>() {
                    }.getType();
                    qingjingSubsBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
//                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                QingjingSubsBean netQingjingSubsBean = qingjingSubsBean;
                if (netQingjingSubsBean.isSuccess()) {
                    commonList(1 + "", 14 + "", id, null, "scene", "subscription");
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
                    ToastUtils.showError(netQingjingSubsBean.getMessage());
//                        dialog.showErrorWithStatus(netQingjingSubsBean.getMessage());
//                        Toast.makeText(QingjingDetailActivity.this, netQingjingSubsBean.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void cancelSubsQingjing(String i) {
        ClientDiscoverAPI.cancelSubsQingjing(i, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                QingjingSubsBean qingjingSubsBean = new QingjingSubsBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingjingSubsBean>() {
                    }.getType();
                    qingjingSubsBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<数据解析异常", e.toString());
//                    Toast.makeText(MainApplication.getContext(), "解析异常" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                QingjingSubsBean netQingjingSubs = qingjingSubsBean;
                if (netQingjingSubs.isSuccess()) {
                    commonList(1 + "", 14 + "", id, null, "scene", "subscription");
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
                    ToastUtils.showError(netQingjingSubs.getMessage());
//                        dialog.showErrorWithStatus(netQingjingSubs.getMessage());
//                        Toast.makeText(QingjingDetailActivity.this, netQingjingSubs.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void deleteQingjing(String id) {
        ClientDiscoverAPI.deleteQingjing(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<删除情景", "数据异常");
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess("删除成功");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    ToastUtils.showError(netBean.getMessage());
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

    private PopupWindow popupWindow;

    private void initPopupWindow() {
        WindowManager windowManager = QingjingDetailActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(QingjingDetailActivity.this, R.layout.popup_scene_details_more, null);
        TextView pinglunTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_pinglun);
        TextView shareTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_share);
        LinearLayout bianjiLinear = (LinearLayout) popup_view.findViewById(R.id.popup_scene_detail_more_bianji_linear);
        TextView jubaoTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_jubao);
        TextView cancelTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_cancel);
        popupWindow = new PopupWindow(popup_view, display.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pinglunTv.setVisibility(View.GONE);
        jubaoTv.setText("删除");
        shareTv.setVisibility(View.GONE);
        bianjiLinear.setVisibility(View.VISIBLE);
        jubaoTv.setOnClickListener(this);
        bianjiLinear.setOnClickListener(this);
        jubaoTv.setVisibility(View.VISIBLE);
        cancelTv.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(QingjingDetailActivity.this,
                R.color.white));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    private void showPopup() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(activity_view, Gravity.BOTTOM, 0, 0);
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
//                Log.e("<<<", "点击position=" + position);
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
            getSceneList(currentPage + "", null, id, null, null, null, null, null);
        }
//        Log.e("<<<", "没进来" + getScrollY());
        backgroundImg.setTranslationY(getScrollY() / 3);
        if (backgroundImg.getTranslationY() >= backgroundImg.getMeasuredHeight() / 5) {
            backgroundImg.setTranslationY(backgroundImg.getMeasuredHeight() / 5 + getScrollY() - backgroundImg.getMeasuredHeight() * 3 / 5);
        }
        if (backgroundImg.getTranslationY() <= 0) {
            backgroundImg.setTranslationY(0);
        }
    }

    private int getScrollY() {
        View c = changjingListView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        return -c.getTop();
    }

    private BroadcastReceiver qingjingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(IndexFragment.class.getSimpleName())) {
                dialog.show();
                currentPage = 1;
                getSceneList(currentPage + "", null, id, null, null, null, null, null);
            } else {
                dialog.show();
                qingjingDetails(id);
                currentPage = 1;
                getSceneList(currentPage + "", null, id, null, null, null, null, null);
            }
        }
    };

}
