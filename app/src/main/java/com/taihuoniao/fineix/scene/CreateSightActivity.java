//package com.taihuoniao.fineix.scene;
//
//import android.animation.Animator;
//import android.animation.ObjectAnimator;
//import android.animation.ValueAnimator;
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Handler;
//import android.renderscript.Allocation;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicBlur;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.baidu.location.BDLocation;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.search.core.PoiInfo;
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.LabelRecycleAdapter;
//import com.taihuoniao.fineix.base.BaseActivity;
//import com.taihuoniao.fineix.beans.CategoryListBean;
//import com.taihuoniao.fineix.beans.SearchBean;
//import com.taihuoniao.fineix.beans.SearchLabelBean;
//import com.taihuoniao.fineix.main.MainActivity;
//import com.taihuoniao.fineix.map.MapSearchAddressActivity;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.network.DataConstants;
//import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
//import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
//import com.taihuoniao.fineix.qingjingOrSceneDetails.ShareSearchActivity;
//import com.taihuoniao.fineix.utils.DensityUtils;
//import com.taihuoniao.fineix.utils.ImageUtils;
//import com.taihuoniao.fineix.utils.MapUtil;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.view.FlowLayout;
//import com.taihuoniao.fineix.view.GlobalTitleLayout;
//import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//
///**
// * Created by taihuoniao on 2016/7/20.
// */
//public class CreateSightActivity extends BaseActivity implements View.OnClickListener {
//    @Bind(R.id.select_envir_tv)
//    TextView selectEnvirTv;
//    @Bind(R.id.des_edit_text)
//    EditText desEditText;
//    @Bind(R.id.flowlayout)
//    FlowLayout flowlayout;
//    @Bind(R.id.edit_linear)
//    LinearLayout editLinear;
//    @Bind(R.id.container)
//    RelativeLayout container;
//    private View activityView;
//    @Bind(R.id.background_img)
//    ImageView backgroundImg;
//    @Bind(R.id.translation_layout)
//    RelativeLayout translationLayout;
//    @Bind(R.id.title_layout)
//    GlobalTitleLayout titleLayout;
//    @Bind(R.id.img)
//    ImageView img;
//    @Bind(R.id.delete_address)
//    ImageView deleteAddress;
//    @Bind(R.id.location_tv)
//    TextView locationTv;
//    @Bind(R.id.address_linear)
//    RelativeLayout addressLinear;
//    @Bind(R.id.category_tv)
//    TextView categoryTv;
//    @Bind(R.id.category_linear)
//    RelativeLayout categoryLinear;
//    @Bind(R.id.title_relative)
//    RelativeLayout titleRelative;
//    @Bind(R.id.title_tv)
//    TextView titleTv;
//    @Bind(R.id.title_edit_text)
//    EditText titleEditText;
//    @Bind(R.id.add_label_tv)
//    TextView addLabelTv;
//    @Bind(R.id.recycler_view)
//    RecyclerView recyclerView;
//    private String imgUrl;//图片地址
//    private Bitmap sceneBitmap;//当前图片背景
//    private SVProgressHUD dialog;//耗时操作对话框
//    private double[] location;//图片上存储的地址信息或当前位置
//    private double lng, lat;//上传情景时所需的经纬度
//    private DisplayImageOptions options;//图片加载
//    private List<String> list;//已选择的标签
//    private LabelRecycleAdapter labelRecycleAdapter;
//
//    public CreateSightActivity() {
//        super(0);
//    }
//
//    @Override
//    protected void setContenttView() {
//        activityView = View.inflate(this, R.layout.activity_create_sight, null);
//        setContentView(activityView);
//    }
//
//    @Override
//    protected void initView() {
//        editText = (EditText) View.inflate(this, R.layout.add_label_edit_text, null);
//        editText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
//                    for (int i = 0; i < flowlayout.getChildCount(); i++) {
//                        if (i == flowlayout.getChildCount() - 1) {
//                            continue;
//                        }
//                        TextView textView = (TextView) flowlayout.getChildAt(i);
//                        if (textView.getTag() == null) {
//                            continue;
//                        }
//                        if ((Boolean) textView.getTag()) {
//                            flowlayout.removeView(textView);
//                            onClick(editText);
//                            return true;
//                        }
//                    }
//                    if (editText.getText().toString().length() <= 0 && flowlayout.getChildCount() > 1) {
//                        TextView textView = (TextView) flowlayout.getChildAt(flowlayout.getChildCount() - 2);
//                        textView.setBackgroundResource(R.drawable.tags_yellow);
//                        textView.setTextColor(getResources().getColor(R.color.white));
//                        textView.setTag(true);
//                        editText.setCursorVisible(false);
//                    }
//                    return true;
//                } else if (editText.getText().toString().length() <= 0 && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
////                    addToFlow(editText.getText().toString(), flowlayout);
//                    editText.setText("");
//                    editText.setFocusable(true);
//                    editText.setFocusableInTouchMode(true);
//                    editText.requestFocus();
//                    return true;
//                }
//                return false;
//            }
//        });
//        titleLayout.setBackgroundResource(R.color.nothing);
//        titleLayout.setBackImgVisible(true);
//        titleLayout.setBackListener(this);
//        titleLayout.setTitle(R.string.create_scene);
//        titleLayout.setRightTv(R.string.publish, getResources().getColor(R.color.white), this);
//        addressLinear.setOnClickListener(this);
//        deleteAddress.setOnClickListener(this);
//        categoryLinear.setOnClickListener(this);
//        titleRelative.setOnClickListener(this);
//        translationLayout.setOnClickListener(this);
//        editLinear.setOnClickListener(this);
//        selectEnvirTv.setOnClickListener(this);
//        titleEditText.setOnClickListener(this);
//        desEditText.setOnClickListener(this);
//        container.setOnClickListener(this);
//        addLabelTv.setOnClickListener(this);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        list = new ArrayList<>();
//        labelRecycleAdapter = new LabelRecycleAdapter(list);
//        recyclerView.setAdapter(labelRecycleAdapter);
//        dialog = new SVProgressHUD(this);
//        options = new DisplayImageOptions.Builder()
////                .showImageOnLoading(R.mipmap.default_background_750_1334)
////                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
////                .showImageOnFail(R.mipmap.default_background_750_1334)
//                .cacheInMemory(false)
//                .cacheOnDisk(false).considerExifParams(true)
//                .build();
//    }
//
//    @Override
//    protected void initList() {
//        Uri imgUri = getIntent().getData();
//        imgUrl = imgUri.toString();
//        ImageLoader.getInstance().loadImage(imgUrl, options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                ToastUtils.showError("图片加载失败，请返回重试");
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                sceneBitmap = loadedImage;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    try {
//                        blur(loadedImage, backgroundImg, 25);
//                    } catch (Exception e) {
//                        backgroundImg.setImageBitmap(loadedImage);
//                    }
//                } else {
//                    backgroundImg.setImageBitmap(loadedImage);
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                ToastUtils.showError("图片加载失败，请返回重试");
//            }
//        });
//        location = ImageUtils.location;
//        if (location == null) {
//            //图片上无位置信息
//            getCurrentLocation();
//        }
//        titleEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().length() > 20) {
//                    titleEditText.setText(s.toString().substring(0, 20));
//                    titleEditText.setSelection(20);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    //高斯模糊
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
//    private EditText editText;//添加自定义标签
//
//    @Override
//    public void onClick(final View v) {
//        switch (v.getId()) {
//            case R.id.edit_text:
//                editText.setCursorVisible(true);
//                break;
//            case R.id.add_label_tv:
//                if (translationLayout.getTranslationY() == 0) {
//                    for (int i = 0; i < flowlayout.getChildCount(); i++) {
//                        if (flowlayout.getChildAt(i) instanceof EditText) {
//                            return;
//                        }
//                        TextView textView = (TextView) flowlayout.getChildAt(i);
////                        弹出之后标签变成白色
//                    }
//                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(translationLayout, "translationY", -DensityUtils.dp2px(this, 350)).setDuration(300);
//                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            titleRelative.setAlpha(1f - animation.getAnimatedFraction());
//                            editLinear.setAlpha(animation.getAnimatedFraction());
//                            selectEnvirTv.setAlpha(animation.getAnimatedFraction());
//                        }
//                    });
//                    objectAnimator.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            recyclerView.setVisibility(View.GONE);
//                            selectEnvirTv.setAlpha(0f);
//                            editLinear.setAlpha(0f);
//                            selectEnvirTv.setVisibility(View.VISIBLE);
//                            editLinear.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            flowlayout.setVisibility(View.VISIBLE);
//                            titleRelative.setVisibility(View.GONE);
//                            desEditText.setFocusable(true);
//                            desEditText.setFocusableInTouchMode(true);
//                            titleEditText.setFocusable(true);
//                            titleEditText.setFocusableInTouchMode(true);
//                            desEditText.setCursorVisible(true);
//                            desEditText.requestFocus();
//                            titleEditText.setCursorVisible(true);
//                            titleEditText.requestFocus();
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInput(titleEditText, InputMethodManager.SHOW_FORCED);
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
//                    objectAnimator.start();
//                } else {
//
//                }
//                break;
//            case R.id.select_envir_tv:
//                if (translationLayout.getTranslationY() == 0) {
//                    onClick(editLinear);
//                } else {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0);
//                    Intent intent22 = new Intent(this, ShareSearchActivity.class);
//                    intent22.putExtra("url", imgUrl);
//                    startActivityForResult(intent22, 1);
//                }
//                break;
//            case R.id.container:
//            case R.id.translation_layout:
//                if (translationLayout.getTranslationY() < 0) {
//                    if (flowlayout.getChildAt(flowlayout.getChildCount() - 1) instanceof EditText) {
//                        flowlayout.removeView(flowlayout.getChildAt(flowlayout.getChildCount() - 1));
//                    }
//                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(translationLayout, "translationY", 0).setDuration(300);
//                    objectAnimator.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            flowlayout.setVisibility(View.GONE);
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(titleEditText.getWindowToken(), 0);
//                            if (flowlayout.getChildCount() <= 0 && titleEditText.getText().toString().length() > 0 && desEditText.getText().toString().length() > 0) {
//                                searchLabel();
//                            }
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            new Handler().post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    selectEnvirTv.setVisibility(View.GONE);
//                                    titleEditText.setCursorVisible(false);
//                                    desEditText.setCursorVisible(false);
//                                    titleEditText.setFocusable(false);
//                                    titleEditText.setFocusableInTouchMode(false);
//                                    desEditText.setFocusable(false);
//                                    desEditText.setFocusableInTouchMode(false);
//                                    recyclerView.setVisibility(View.VISIBLE);
//                                    for (int i = 0; i < flowlayout.getChildCount(); i++) {
//                                        final View view = flowlayout.getChildAt(i);
//                                        new Handler().post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                if (!(boolean) view.getTag()) {
//                                                    flowlayout.removeView(view);
//                                                }
//                                            }
//                                        });
//
//                                    }
//                                }
//                            });
//
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
//                    objectAnimator.start();
//
//                }
//                break;
//            case R.id.title_edit_text:
//            case R.id.des_edit_text:
//            case R.id.edit_linear:
//            case R.id.title_relative:
//                if (translationLayout.getTranslationY() == 0) {
//                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(translationLayout, "translationY", -DensityUtils.dp2px(this, 350)).setDuration(300);
//                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            titleRelative.setAlpha(1f - animation.getAnimatedFraction());
//                            editLinear.setAlpha(animation.getAnimatedFraction());
//                            selectEnvirTv.setAlpha(animation.getAnimatedFraction());
//                        }
//                    });
//                    objectAnimator.addListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            recyclerView.setVisibility(View.GONE);
//                            selectEnvirTv.setAlpha(0f);
//                            editLinear.setAlpha(0f);
//                            selectEnvirTv.setVisibility(View.VISIBLE);
//                            editLinear.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            flowlayout.setVisibility(View.VISIBLE);
//                            titleRelative.setVisibility(View.GONE);
//                            desEditText.setFocusable(true);
//                            desEditText.setFocusableInTouchMode(true);
//                            titleEditText.setFocusable(true);
//                            titleEditText.setFocusableInTouchMode(true);
//                            desEditText.setCursorVisible(true);
//                            desEditText.requestFocus();
//                            titleEditText.setCursorVisible(true);
//                            titleEditText.requestFocus();
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInput(titleEditText, InputMethodManager.SHOW_FORCED);
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//
//                        }
//                    });
//                    objectAnimator.start();
//                }
//                break;
//            case R.id.category_linear:
//                startActivityForResult(new Intent(this, CategoryActivity.class), 111);
//                break;
//            case R.id.title_continue:
//                ToastUtils.showError("上传情景");
//                break;
//            case R.id.delete_address:
//                locationTv.setText("添加情景地点");
//                deleteAddress.setVisibility(View.GONE);
//                break;
//            case R.id.address_linear:
//                if (location == null) {
//                    getCurrentLocation();
//                    return;
//                }
//                Intent intent = new Intent(CreateSightActivity.this, MapSearchAddressActivity.class);
//                intent.putExtra("latLng", new LatLng(location[1], location[0]));
//                startActivityForResult(intent, DataConstants.REQUESTCODE_CREATESCENE_BDSEARCH);
//                break;
//            case R.id.title_back:
//                showDialog();
//                break;
//        }
//    }
//
//    //根据标题描述搜索标签
//    private void searchLabel() {
//        ClientDiscoverAPI.searchLabel(titleEditText.getText().toString(), desEditText.getText().toString(), new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                SearchLabelBean searchLabelBean = new SearchLabelBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<SearchLabelBean>() {
//                    }.getType();
//                    searchLabelBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<", "搜索标签数据解析异常");
//                }
//                if (searchLabelBean.isSuccess()) {
//                    flowlayout.removeAllViews();
//                    addToFlow(searchLabelBean.getData().getWord(), flowlayout, true);
//                } else {
//                    ToastUtils.showError(searchLabelBean.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                ToastUtils.showError(R.string.net_fail);
//            }
//        });
//    }
//
//    //获得当前位置信息
//    private void getCurrentLocation() {
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//        MapUtil.getCurrentLocation(new MapUtil.OnReceiveLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                if (location == null && bdLocation != null) {
//                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
//                    dialog.dismiss();
//                    MapUtil.destroyLocationClient();
//                    MapUtil.destroyGeoCoder();
//                    MapUtil.destroyPoiSearch();
//                }
//            }
//        });
//    }
//
//    private void showDialog() {
//        if (getIntent().hasExtra(SceneDetailActivity.class.getSimpleName()) || getIntent().hasExtra(QingjingDetailActivity.class.getSimpleName())) {
//            finish();
//        } else {
//            AlertDialog.Builder builder = new AlertDialog.Builder(CreateSightActivity.this);
//            builder.setMessage("返回上一步？");
//            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    CreateSightActivity.this.finish();
//                }
//            });
//            builder.setNegativeButton("取消创建", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    startActivity(new Intent(CreateSightActivity.this, MainActivity.class));
//                }
//            });
//            builder.create().show();
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        showDialog();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data != null) {
//            switch (resultCode) {
//                case 1:
//                    CategoryListBean.CategoryListItem categoryListItem = (CategoryListBean.CategoryListItem) data.getSerializableExtra("category");
//                    categoryTv.setText(categoryListItem.getTitle());
//                    break;
//                case 222:
//                    SearchBean.SearchItem search = (SearchBean.SearchItem) data.getSerializableExtra("scene");
//                    titleEditText.setText(search.getTitle());
//                    desEditText.setText(search.getDes());
//                    flowlayout.removeAllViews();
//                    List<String> strList = search.getTags();
//                    if (search.getTags().size() > 10) {
//                        strList = search.getTags().subList(0, 10);
//                    }
//                    addToFlow(strList, flowlayout, false);
//                    break;
//                case DataConstants.RESULTCODE_CREATESCENE_BDSEARCH:
//                    PoiInfo poiInfo = data.getParcelableExtra(PoiInfo.class.getSimpleName());
//                    if (poiInfo != null) {
//                        locationTv.setText(poiInfo.name);
//                        deleteAddress.setVisibility(View.VISIBLE);
//                        lng = poiInfo.location.longitude;
//                        lat = poiInfo.location.latitude;
//                    }
//                    break;
//            }
//        }
//    }
//
//    private void addToFlow(List<String> lis, FlowLayout flowLayout, boolean select_all) {
//        if (lis == null || lis.size() <= 0) {
//            return;
//        }
//        for (int i = 0; i < lis.size(); i++) {
//            if (i > 9) {
//                break;
//            }
//            final TextView textView = (TextView) View.inflate(this, R.layout.view_horizontal_label_item, null);
//            textView.setText(lis.get(i));
//            textView.setTag(select_all);
//            if (select_all) {
//                textView.setTextColor(getResources().getColor(R.color.white));
//                textView.setBackgroundResource(R.drawable.tags_yellow);
//            }
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (translationLayout.getTranslationY() == 0) {
//                        CreateSightActivity.this.onClick(editLinear);
//                        return;
//                    }
//                    if (!(Boolean) v.getTag()) {
//                        textView.setTextColor(getResources().getColor(R.color.white));
//                        textView.setBackgroundResource(R.drawable.tags_yellow);
//                        textView.setPadding(DensityUtils.dp2px(CreateSightActivity.this, 10), 0,
//                                DensityUtils.dp2px(CreateSightActivity.this, 18), 0);
//                        textView.setTag(true);
//                    } else {
//                        textView.setTextColor(getResources().getColor(R.color.color_666));
//                        textView.setBackgroundResource(R.drawable.tags_gray);
//                        textView.setTag(false);
//                    }
//                    list.clear();
//                    for (int i = 0; i < flowlayout.getChildCount(); i++) {
//                        TextView textView = (TextView) flowlayout.getChildAt(i);
//                        if ((boolean) textView.getTag()) {
//                            list.add(textView.getText().toString());
//                        }
//                    }
//                    labelRecycleAdapter.notifyDataSetChanged();
//                }
//            });
//            flowLayout.addView(textView);
//        }
//    }
//
//}
