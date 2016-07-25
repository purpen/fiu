package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.LabelRecycleAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.AddProductBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SceneDetailsBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.SearchLabelBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.MapSearchAddressActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.ShareSearchActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.Base64Utils;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.FlowLayout;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/7/24.
 */
public class CreateActivity extends BaseActivity implements View.OnClickListener {
    private SceneDetailsBean sceneDetails;//情景详情界面跳转过来
    //上个界面传递过来的图片路径
    private String imgUrl;
    TextView selectEnvirTv;
    EditText titleEditText;
    EditText desEditText;
    FlowLayout flowlayout;
    TextView addLabelTv;
    @Bind(R.id.background_img)
    ImageView backgroundImg;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.bottom_linear)
    LinearLayout bottomLinear;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.framelayout)
    FrameLayout frameLayout;
    @Bind(R.id.des_tv)
    TextView desTv;
    @Bind(R.id.label_relative)
    RelativeLayout labelRelative;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.delete_address)
    ImageView deleteAddress;
    @Bind(R.id.add_location_linear)
    LinearLayout addLocationLinear;
    @Bind(R.id.location_tv)
    TextView locationTv;
    @Bind(R.id.city_tv)
    TextView cityTv;
    @Bind(R.id.category_tv)
    TextView categoryTv;
    @Bind(R.id.category_linear)
    RelativeLayout categoryLinear;
    private View activityView;//当前页面的view
    private DisplayImageOptions option;//图片加载
    private PopupWindow popupWindow;//编辑信息时弹出的界面
    private Bitmap sceneBitmap;//当前图片背景
    private SVProgressHUD dialog;//耗时操作对话框
    private double[] location;//图片上存储的地址信息或当前位置
    private double lng, lat;//上传情景时所需的经纬度
    private List<String> list;//已选择的标签
    private LabelRecycleAdapter labelRecycleAdapter;//已经选择的标签
    private String dipanId;//所属地盘id
    private EditText editText;//添加标签时编辑标签

    public CreateActivity() {
        super(0);
    }

    @Override
    protected void setContenttView() {
        activityView = View.inflate(this, R.layout.activity_create, null);
        setContentView(activityView);
    }

    @Override
    protected void initView() {
        titleLayout.setBackgroundResource(R.drawable.jianbian_head);
        titleLayout.setTitle(R.string.create_scene);
        titleLayout.setRightTv(R.string.publish, getResources().getColor(R.color.yellow_bd8913), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();
        labelRecycleAdapter = new LabelRecycleAdapter(list);
        recyclerView.setAdapter(labelRecycleAdapter);
        dialog = new SVProgressHUD(this);
        option = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(false).cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .build();
        initPopuptWindow();
        titleTv.setOnClickListener(this);
        addLocationLinear.setOnClickListener(this);
        categoryLinear.setOnClickListener(this);
        deleteAddress.setOnClickListener(this);
        desTv.setOnClickListener(this);
        recyclerView.setOnClickListener(this);
        SceneTitleSetUtils.setTitle(titleTv, frameLayout, 0, 0, 1);
        editText = (EditText) View.inflate(this, R.layout.add_label_edit_text, null);
        editText.setOnClickListener(this);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    if (flowlayout.getChildCount() <= 1) {
                        return false;
                    }
                    for (int i = flowlayout.getChildCount() - 2; i >= 0; i--) {
                        TextView textView = (TextView) flowlayout.getChildAt(i);
                        if (textView.getTag() == null) {
                            continue;
                        }
                        if ((Boolean) textView.getTag()) {
                            flowlayout.removeView(textView);
                            onClick(editText);
                            return true;
                        }
                    }
                    if (editText.getText().toString().length() <= 0 && flowlayout.getChildCount() > 1) {
                        TextView textView = (TextView) flowlayout.getChildAt(flowlayout.getChildCount() - 2);
                        textView.setBackgroundResource(R.drawable.tags_yellow);
                        textView.setTextColor(getResources().getColor(R.color.white));
                        textView.setTag(true);
                        editText.setCursorVisible(false);
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP && editText.getText().toString().length() > 0) {
                    flowlayout.removeView(editText);
                    addToFlow(editText.getText().toString(), flowlayout, true);
                    flowlayout.addView(editText);
                    editText.setText("");
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void initList() {
        if (getIntent().hasExtra(SceneDetailActivity.class.getSimpleName())) {
            sceneDetails = (SceneDetailsBean) getIntent().getSerializableExtra(SceneDetailActivity.class.getSimpleName());
            imgUrl = sceneDetails.getData().getCover_url();
            titleTv.setText(sceneDetails.getData().getTitle());
            titleEditText.setText(sceneDetails.getData().getTitle());
            SceneTitleSetUtils.setTitle(titleTv, frameLayout, 0, 0, 1);
            desTv.setText(sceneDetails.getData().getDes());
            desEditText.setText(sceneDetails.getData().getDes());
            desTv.setVisibility(View.VISIBLE);
            list.addAll(sceneDetails.getData().getTags());
            labelRecycleAdapter.notifyDataSetChanged();
            locationTv.setText(sceneDetails.getData().getAddress());
            if (sceneDetails.getData().getCity() != null) {
                cityTv.setText(sceneDetails.getData().getCity());
            }
            categoryTv.setText(sceneDetails.getData().getScene_title());
            dipanId = sceneDetails.getData().getScene_id() + "";
            lng = sceneDetails.getData().getLocation().getCoordinates().get(0);
            lat = sceneDetails.getData().getLocation().getCoordinates().get(1);
            labelRelative.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            for (String str : sceneDetails.getData().getTags()) {
                addToFlow(str, flowlayout, true);
            }
            ImageLoader.getInstance().loadImage(imgUrl, option, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    sceneBitmap = loadedImage;
                    backgroundImg.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } else {
            imgUrl = getIntent().getData().toString();
            ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
                @Override
                public void callback(Bitmap result) {
                    sceneBitmap = result;
                    backgroundImg.setImageBitmap(result);
                }
            });
        }
//        ImageLoader.getInstance().clearMemoryCache();
        Log.e("<<<", "create>>>url=" + imgUrl + ",Uri=" + getIntent().getData());


        location = ImageUtils.location;
        if (location == null) {
            //图片上无位置信息
            getCurrentLocation();
        }
    }

    //获得当前位置信息
    private void getCurrentLocation() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        MapUtil.getCurrentLocation(new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    dialog.dismiss();
                    MapUtil.destroyLocationClient();
                    MapUtil.destroyGeoCoder();
                    MapUtil.destroyPoiSearch();
                }
            }
        });
    }


    private void initPopuptWindow() {
        View popup_view = View.inflate(this, R.layout.pop_create, null);
        selectEnvirTv = (TextView) popup_view.findViewById(R.id.select_envir_tv);
        titleEditText = (EditText) popup_view.findViewById(R.id.title_edit_text);
        desEditText = (EditText) popup_view.findViewById(R.id.des_edit_text);
        flowlayout = (FlowLayout) popup_view.findViewById(R.id.flowlayout);
        addLabelTv = (TextView) popup_view.findViewById(R.id.add_label_tv);
        popupWindow = new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        selectEnvirTv.setOnClickListener(this);
        addLabelTv.setOnClickListener(this);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.alpha);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (flowlayout.getChildAt(flowlayout.getChildCount() - 1) instanceof EditText) {
                    flowlayout.removeView(flowlayout.getChildAt(flowlayout.getChildCount() - 1));
                }
                list.clear();
                if (flowlayout.getChildCount() <= 0
                        && titleEditText.getText().toString().length() > 0
                        && desEditText.getText().toString().length() > 0) {
                    searchLabel();
                } else {
                    for (int i = 0; i < flowlayout.getChildCount(); i++) {
                        TextView textView = (TextView) flowlayout.getChildAt(i);
                        if (textView.getTag() != null && (boolean) textView.getTag()) {
                            list.add(textView.getText().toString());
                        }
                    }
                }
                if (list.size() > 0) {
                    labelRecycleAdapter.notifyDataSetChanged();
                    labelRelative.setVisibility(View.VISIBLE);
                }
                if (titleEditText.getText().toString().length() > 0) {
                    titleTv.setText(titleEditText.getText().toString());
                    SceneTitleSetUtils.setTitle(titleTv, frameLayout, 0, 0, 1);
                }
                if (desEditText.getText().toString().length() > 0) {
                    desTv.setText(desEditText.getText().toString());
                    desTv.setVisibility(View.VISIBLE);
                } else {
                    desTv.setVisibility(View.GONE);
                }
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);
                titleLayout.setVisibility(View.VISIBLE);
                bottomLinear.setVisibility(View.VISIBLE);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this,
                R.color.nothing));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }


    private void showPopupWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//这行代码可以使window后的所有东西边暗淡
        popupWindow.showAtLocation(activityView, Gravity.TOP, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_text:
                editText.setCursorVisible(true);

                break;
            case R.id.delete_address:
                locationTv.setText("添加情景地点");
                cityTv.setVisibility(View.GONE);
                deleteAddress.setVisibility(View.GONE);
                break;
            case R.id.category_linear:
                startActivityForResult(new Intent(this, CategoryActivity.class), 111);
                break;
            case R.id.add_location_linear:
                if (location == null) {
                    getCurrentLocation();
                    return;
                }
                Intent intent = new Intent(this, MapSearchAddressActivity.class);
                intent.putExtra("latLng", new LatLng(location[1], location[0]));
                startActivityForResult(intent, DataConstants.REQUESTCODE_CREATESCENE_BDSEARCH);
                break;
            case R.id.select_envir_tv:
                Intent intent22 = new Intent(this, ShareSearchActivity.class);
                intent22.putExtra("url", imgUrl);
                startActivityForResult(intent22, 1);
                break;
            case R.id.add_label_tv:
                try {
                    flowlayout.addView(editText);
                    editText.setCursorVisible(true);
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                } catch (Exception e) {

                }
                break;
            case R.id.recycler_view:
            case R.id.des_tv:
            case R.id.title_tv:
                titleLayout.setVisibility(View.GONE);
                bottomLinear.setVisibility(View.GONE);
                showPopupWindow();
                break;
            case R.id.title_continue:
                if (!LoginInfo.isUserLogin()) {
//                    Toast.makeText(CreateSceneActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(this, OptRegisterLoginActivity.class));
                    return;
                }
                if (sceneBitmap == null) {
                    ToastUtils.showError("图片加载失败，请返回重试");
                    return;
                }
                if (TextUtils.isEmpty(titleTv.getText()) || titleTv.getText().equals("请填写情景标题")) {
                    dialog.showErrorWithStatus(MainApplication.tag == 2 ? "请填写地盘标题" : "请填写情景标题");
//                    Toast.makeText(CreateSceneActivity.this, , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(desTv.getText())) {
                    dialog.showErrorWithStatus(MainApplication.tag == 2 ? "请填写地盘描述" : "请填写情景描述");
//                    Toast.makeText(CreateSceneActivity.this, "请填写" + (MainApplication.tag == 2 ? "情" : "场") + "景描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (list == null || list.size() == 0) {
                    dialog.showErrorWithStatus("请添加标签");
//                    Toast.makeText(CreateSceneActivity.this, "请添加标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cityTv.getText())) {
                    dialog.showErrorWithStatus("请添加地点");
                    return;
                }
                dialog.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Black);
                StringBuilder tags = new StringBuilder();
                for (String each : list) {
                    tags.append(",").append(each);
                }
                if (tags.length() > 0) {
                    tags.deleteCharAt(0);
                }
//                Log.e("<<<", "创建" + MainApplication.tag);
                if (MainApplication.tag == 1) {
                    if (dipanId == null) {
                        dialog.dismiss();
                        dialog.showErrorWithStatus("请选择所属地盘");
//                        Toast.makeText(CreateSceneActivity.this, "请选择所属情景", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StringBuilder products = new StringBuilder();

                    if (MainApplication.tagInfoList != null && MainApplication.tagInfoList.size() > 0) {
                        products.append("[");
                        for (int i = 0; i < MainApplication.tagInfoList.size(); i++) {
                            products.append(MainApplication.tagInfoList.get(i).toString());
                            if (i != MainApplication.tagInfoList.size() - 1) {
                                products.append(",");
                            }
                        }
                        products.append("]");
                    }
                    StringBuilder product_id = new StringBuilder();
                    StringBuilder product_title = new StringBuilder();
                    StringBuilder product_price = new StringBuilder();
                    StringBuilder product_x = new StringBuilder();
                    StringBuilder product_y = new StringBuilder();

                    for (TagItem each : MainApplication.tagInfoList) {
                        product_id.append(",").append(each.getId());
                        product_title.append(",").append(each.getName());
                        product_price.append(",").append(each.getPrice());
                        product_x.append(",").append(each.getX());
                        product_y.append(",").append(each.getY());
                    }
                    if (product_id.length() > 0) {
                        product_id.deleteCharAt(0);
                    }
                    if (product_title.length() > 0) {
                        product_title.deleteCharAt(0);
                    }
                    if (product_price.length() > 0) {
                        product_price.deleteCharAt(0);
                    }
                    if (product_x.length() > 0) {
                        product_x.deleteCharAt(0);
                    }
                    if (product_y.length() > 0) {
                        product_y.deleteCharAt(0);
                    }
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    int sapleSize = 100;
                    do {
                        stream.reset();
                        if (sapleSize > 10) {
                            sapleSize -= 10;
                        } else {
                            sapleSize--;
                        }
                        if (sapleSize > 100 || sapleSize <= 0) {
                            dialog.dismiss();
                            dialog.showErrorWithStatus("图片过大");
//                            Toast.makeText(CreateSceneActivity.this, "图片过大", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sceneBitmap.compress(Bitmap.CompressFormat.JPEG, sapleSize, stream);
//                        Log.e("<<<", "图片大小=" + stream.size());
                    } while (stream.size() > MainApplication.MAXPIC);//最大上传图片不得超过512K
                    String tmp = Base64Utils.encodeLines(stream.toByteArray());
                    createCJ(sceneDetails == null ? null : sceneDetails.getData().get_id() + "", tmp, titleTv.getText().toString()
                            , desTv.getText().toString(), dipanId, tags.toString(),
                            products.toString(),
                            locationTv.getText().toString(), cityTv.getText().toString(),
                            lat + "", lng + "");
                } else if (MainApplication.tag == 2) {
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    int sapleSize = 100;
//                    do {
//                        stream.reset();
//                        if (sapleSize > 10) {
//                            sapleSize -= 10;
//                        } else {
//                            sapleSize--;
//                        }
//                        if (sapleSize > 100 || sapleSize <= 0) {
//                            dialog.dismiss();
//                            dialog.showErrorWithStatus("图片过大");
////                            Toast.makeText(CreateSceneActivity.this, "图片过大", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        sceneBitmap.compress(Bitmap.CompressFormat.JPEG, sapleSize, stream);
////                        Log.e("<<<", "图片大小=" + stream.size());
//                    } while (stream.size() > MainApplication.MAXPIC);//最大上传图片不得超过512K
//                    String tmp = Base64Utils.encodeLines(stream.toByteArray());
//                    createQJ(qingjingDetails == null ? null : qingjingDetails.getData().get_id() + "", titleEdt.getText().toString(), contentEdt.getText().toString(),
//                            tags.toString(), locationTv.getText().toString(), tmp, lat + "", lng + "");
                } else {
                    dialog.dismiss();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case 1:
                    CategoryListBean.CategoryListItem categoryListItem = (CategoryListBean.CategoryListItem) data.getSerializableExtra("category");
                    categoryTv.setText(categoryListItem.getTitle());
                    break;
                case 222:
                    SearchBean.SearchItem search = (SearchBean.SearchItem) data.getSerializableExtra("scene");
                    titleEditText.setText(search.getTitle());
                    desEditText.setText(search.getDes());
                    flowlayout.removeAllViews();
                    List<String> strList = search.getTags();
                    if (strList.size() > 10) {
                        strList = strList.subList(0, 10);
                    }
                    for (String str : strList) {
                        addToFlow(str, flowlayout, false);
                    }
                    break;
                case 3:
                    QingJingItem qingJing = (QingJingItem) data.getSerializableExtra("qingjing");
                    if (qingJing != null) {
                        categoryTv.setText(qingJing.title);
                        dipanId = qingJing._id + "";
                    }
                    break;
                case 4:
                case 6:
                    QingJingListBean.QingJingItem qingJingItem = (QingJingListBean.QingJingItem) data.getSerializableExtra("qingjing");
                    if (qingJingItem != null) {
                        categoryTv.setText(qingJingItem.getTitle());
                        dipanId = qingJingItem.get_id();
                    }
                    break;
                case 5:
                    SearchBean.SearchItem searchItem = (SearchBean.SearchItem) data.getSerializableExtra("searchqj");
                    if (searchItem != null) {
                        categoryTv.setText(searchItem.getTitle());
                        dipanId = searchItem.get_id();
                    }
                    break;
                case DataConstants.RESULTCODE_CREATESCENE_BDSEARCH:
                    PoiInfo poiInfo = data.getParcelableExtra(PoiInfo.class.getSimpleName());
                    String city = data.getStringExtra("city");
                    if (poiInfo != null) {
                        locationTv.setText(poiInfo.name);
                        deleteAddress.setVisibility(View.VISIBLE);
                        cityTv.setText(city);
                        cityTv.setVisibility(View.VISIBLE);
                        lng = poiInfo.location.longitude;
                        lat = poiInfo.location.latitude;
                    }
                    break;
            }
        }
    }

    private void addToFlow(String str, FlowLayout flowLayout, boolean select_all) {
        final TextView textView = (TextView) View.inflate(this, R.layout.view_horizontal_label_item, null);
        textView.setText(str);
        textView.setTag(select_all);
        if (select_all) {
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.tags_yellow);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Boolean) v.getTag()) {
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setBackgroundResource(R.drawable.tags_yellow);
                    textView.setPadding(DensityUtils.dp2px(CreateActivity.this, 10), 0,
                            DensityUtils.dp2px(CreateActivity.this, 18), 0);
                    textView.setTag(true);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.color_666));
                    textView.setBackgroundResource(R.drawable.tags_gray);
                    textView.setTag(false);
                }
            }
        });
        flowLayout.addView(textView);
    }

    //根据标题描述搜索标签
    private void searchLabel() {
        ClientDiscoverAPI.searchLabel(titleEditText.getText().toString(), desEditText.getText().toString(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SearchLabelBean searchLabelBean = new SearchLabelBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchLabelBean>() {
                    }.getType();
                    searchLabelBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "搜索标签数据解析异常");
                }
                if (searchLabelBean.isSuccess()) {
                    flowlayout.removeAllViews();
                    if (searchLabelBean.getData() != null && searchLabelBean.getData().getWord() != null) {
                        list = searchLabelBean.getData().getWord();
                        labelRecycleAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showError(searchLabelBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private void createCJ(String id, String tmp, String title, String des, String scene_id, String tags, String products,
                          String address, String city, String lat, String lng) {
        ClientDiscoverAPI.createScene(id, tmp, title, des, scene_id, tags, products, address, city,
                lat, lng, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        AddProductBean addProductBean = new AddProductBean();
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<AddProductBean>() {
                            }.getType();
                            addProductBean = gson.fromJson(responseInfo.result, type);
                        } catch (JsonSyntaxException e) {
                            Log.e("<<<创建场景", "数据解析异常");
                        }
                        dialog.dismiss();
                        AddProductBean netBean = addProductBean;
                        if (netBean.isSuccess()) {
                            if (SceneDetailActivity.instance != null) {
                                SceneDetailActivity.instance.finish();
                            }
                            dialog.showSuccessWithStatus("您的" + (MainApplication.tag == 2 ? "情" : "场") + "景发布成功，品味又升级啦");
                            if (MainApplication.whichQingjing != null) {
                                sendBroadcast(new Intent(DataConstants.BroadQingjingDetail));
                            }
                            MainApplication.whichQingjing = null;
                            MainApplication.tagInfoList = null;
                            Intent intent = new Intent(CreateActivity.this, SceneDetailActivity.class);
                            intent.putExtra("id", netBean.getData().getId());
                            intent.putExtra("create", true);
                            startActivity(intent);
                            if (SelectPhotoOrCameraActivity.instance != null) {
                                SelectPhotoOrCameraActivity.instance.finish();
                            }
                            if (CropPictureActivity.instance != null) {
                                CropPictureActivity.instance.finish();
                            }
                            if (EditPictureActivity.instance != null) {
                                EditPictureActivity.instance.finish();
                            }
                            if (FilterActivity.instance != null) {
                                FilterActivity.instance.finish();
                            }
                            CreateActivity.this.finish();
                        } else {
                            ToastUtils.showError(netBean.getMessage());
//                            Toast.makeText(this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        dialog.dismiss();
                        ToastUtils.showError("网络错误");
                    }
                });
    }
}
