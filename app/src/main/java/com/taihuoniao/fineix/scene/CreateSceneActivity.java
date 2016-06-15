package com.taihuoniao.fineix.scene;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddressRecycleAdapter;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.AddProductBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingItem;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.QingjingDetailBean;
import com.taihuoniao.fineix.beans.SceneDetailsBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.BDSearchAddressActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.Base64Utils;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/6.
 */
public class CreateSceneActivity extends BaseActivity implements View.OnClickListener, EditRecyclerAdapter.ItemClick {
    private SceneDetailsBean sceneDetails;
    private QingjingDetailBean qingjingDetails;
    //控件
    private GlobalTitleLayout titleLayout;
    //    private ImageView sceneImg;
    private ImageView sceneImg;
    private Bitmap sceneBitmap;//图片
    private EditText contentEdt;
    private EditText titleEdt;
    private TextView locationTv;
    private RelativeLayout addAddressLinear;
    private ImageView locationImg;
    //    private RelativeLayout addressRelative;
    private RecyclerView recyclerView;
    private AddressRecycleAdapter recyclerAdapter;
    //    private TextView addressTv, areaTv;
    private ImageView deleteAddressImg;
    private RelativeLayout labelRelative;
    private HorizontalScrollView labelHorizontal;
    private LinearLayout labelLinear;
    private RelativeLayout qingjingRelative;
    private LinearLayout qingjingLinear;
    //图片上存储的地址信息
    private double[] location;
    //工具
    private SVProgressHUD dialog;
    //从选择标签界面返回的用户选择的标签
    private List<UsedLabelBean> selectList;
    //地址列表数据
    private List<PoiInfo> poiInfoList;
    //位置信息
    private String city, district;
    private double lng, lat;
    //创建场景时所属情景
    private String scene_id;
    private DisplayImageOptions options;


    public CreateSceneActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_create_scene);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_create_scene_titlelayout);
        sceneImg = (ImageView) findViewById(R.id.activity_create_scene_img);
        contentEdt = (EditText) findViewById(R.id.activity_create_scene_contentedt);
        titleEdt = (EditText) findViewById(R.id.activity_create_scene_titleedt);
        locationTv = (TextView) findViewById(R.id.activity_create_scene_location);
        addAddressLinear = (RelativeLayout) findViewById(R.id.activity_create_scene_add_addresslinear);
//        addressRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_addressrelative);
        locationImg = (ImageView) findViewById(R.id.location);
        recyclerView = (RecyclerView) findViewById(R.id.activity_create_scene_recycler);
//        addressTv = (TextView) findViewById(R.id.activity_create_scene_address);
//        areaTv = (TextView) findViewById(R.id.activity_create_scene_area);
        deleteAddressImg = (ImageView) findViewById(R.id.activity_create_scene_deleteaddress);
        labelRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_labelrelative);
        labelHorizontal = (HorizontalScrollView) findViewById(R.id.activity_create_scene_label_horizontal);
        labelLinear = (LinearLayout) findViewById(R.id.activity_create_scene_labellinear);
        qingjingRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_qingjing);
        qingjingLinear = (LinearLayout) findViewById(R.id.activity_create_scene_qingjinglinear);
        if (MainApplication.tag == 2) {
            qingjingRelative.setVisibility(View.GONE);
        }
        dialog = new SVProgressHUD(CreateSceneActivity.this);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .build();
    }

    @Override
    protected void initList() {
        Uri imageUri = getIntent().getData();

        titleLayout.setFocusable(true);
        titleLayout.setFocusableInTouchMode(true);
        titleLayout.requestFocus();
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setBackListener(this);
        titleLayout.setTitle(R.string.create_scene, getResources().getColor(R.color.black333333));
        titleLayout.setRightTv(R.string.publish, getResources().getColor(R.color.yellow_bd8913), this);
        titleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 20) {
                    titleEdt.setText(s.toString().substring(0, 20));
                    titleEdt.setSelection(20);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        ImageUtils.asyncLoadImage(CreateSceneActivity.this, imageUri, new ImageUtils.LoadImageCallback() {
//            @Override
//            public void callback(Bitmap result) {
//                sceneBitmap = result;
//                sceneImg.setImageBitmap(result);
////                sceneImg.setCornerRadius(DensityUtils.dp2px(CreateSceneActivity.this, 5));
//            }
//        });
        addAddressLinear.setOnClickListener(this);
//        addressRelative.setOnClickListener(this);
        locationImg.setOnClickListener(this);
        poiInfoList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        location = ImageUtils.location;
        if (location != null) {
            recyclerView.setVisibility(View.VISIBLE);
            //图片上有位置信息
            getAddressByCoordinate();
        } else {
            recyclerView.setVisibility(View.GONE);
            //图片上无位置信息
            getCurrentLocation();
        }
        deleteAddressImg.setOnClickListener(this);
        labelRelative.setOnClickListener(this);
        qingjingRelative.setOnClickListener(this);
        if (MainApplication.tag == 2) {
            titleLayout.setRightTv(R.string.publish, getResources().getColor(R.color.black333333), this);
            titleLayout.setTitle(R.string.create_qingjing, getResources().getColor(R.color.black333333));
            contentEdt.setHint("请输入140以内的描述");
            contentEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() > 140) {
                        titleEdt.setText(s.toString().substring(0, 140));
                        titleEdt.setSelection(140);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        if (getIntent().hasExtra(SceneDetailActivity.class.getSimpleName())) {
            titleLayout.setTitle(R.string.bianji_scene, getResources().getColor(R.color.black333333));
            titleLayout.setBackImg(R.mipmap.cancel_black);
            getSceneDetails();
        } else if (getIntent().hasExtra(QingjingDetailActivity.class.getSimpleName())) {
            titleLayout.setTitle(R.string.bianji_qingjing, getResources().getColor(R.color.black333333));
            titleLayout.setBackImg(R.mipmap.cancel_black);
            getQingjingDetails();
        } else {
            imageUri = getIntent().getData();
            ImageLoader.getInstance().loadImage(imageUri.toString(), options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    ToastUtils.showError("图片加载失败，请返回重试");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    sceneBitmap = loadedImage;
                    sceneImg.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    ToastUtils.showError("图片加载失败，请返回重试");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
                }
            });
        }
        //在情景下创建场景
        if (MainApplication.whichQingjing != null) {
            scene_id = MainApplication.whichQingjing.getData().get_id();
            qingjingLinear.removeAllViews();
            addQingjingToLinear(MainApplication.whichQingjing.getData().getTitle());
            qingjingRelative.setEnabled(false);
        }
    }

    //编辑场景时调用
    private void getQingjingDetails() {
//        dialog.show();
        qingjingDetails = (QingjingDetailBean) getIntent().getSerializableExtra(QingjingDetailActivity.class.getSimpleName());
        if (qingjingDetails == null) {
            ToastUtils.showError("数据错误");
            finish();
            return;
        }

        titleEdt.setText(qingjingDetails.getData().getTitle());
        contentEdt.setText(qingjingDetails.getData().getDes());
        ImageLoader.getInstance().loadImage(qingjingDetails.getData().getCover_url(), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ToastUtils.showError("图片加载失败");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                sceneBitmap = loadedImage;
                sceneImg.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                ToastUtils.showError("图片加载失败");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
            }
        });
//                scene_id = netScene.getData().getScene_id();
        selectList = new ArrayList<UsedLabelBean>();
        for (int i = 0; i < qingjingDetails.getData().getTags().size(); i++) {
            UsedLabelBean usedLabelBean = new UsedLabelBean(qingjingDetails.getData().getTags().get(i) + "", qingjingDetails.getData().getTag_titles().get(i));
            selectList.add(usedLabelBean);
        }
        for (int i = 0; i < selectList.size(); i++) {
            UsedLabelBean labelBean = selectList.get(i);
            addToLinear(labelBean.getTitle_cn(), Integer.parseInt(labelBean.get_id()));
        }
//                MainApplication.tagInfoList = new ArrayList<TagItem>();
//                for (int i = 0; i < netScene.getData().getProduct().size(); i++) {
//                    TagItem tagItem = new TagItem();
//                    tagItem.setId(netScene.getData().getProduct().get(i).getId());
//                    tagItem.setName(netScene.getData().getProduct().get(i).getTitle());
//                    tagItem.setPrice(netScene.getData().getProduct().get(i).getPrice());
//                    tagItem.setX(netScene.getData().getProduct().get(i).getX());
//                    tagItem.setY(netScene.getData().getProduct().get(i).getY());
//                    MainApplication.tagInfoList.add(tagItem);
//                }
        locationTv.setText(qingjingDetails.getData().getAddress());
        lng = qingjingDetails.getData().getLocation().getCoordinates().get(0);
        lat = qingjingDetails.getData().getLocation().getCoordinates().get(1);
//                addQingjingToLinear(netScene.getData().getScene_title());
//                Log.e("<<<编辑场景", lat + "," + lng);

    }

    //编辑情景时调用
    private void getSceneDetails() {
//        dialog.show();
        sceneDetails = (SceneDetailsBean) getIntent().getSerializableExtra(SceneDetailActivity.class.getSimpleName());
        if (sceneDetails == null) {
            ToastUtils.showError("数据错误");
            finish();
            return;
        }

        titleEdt.setText(sceneDetails.getData().getTitle());
        contentEdt.setText(sceneDetails.getData().getDes());
        ImageLoader.getInstance().loadImage(sceneDetails.getData().getCover_url(), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ToastUtils.showError("图片加载失败");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                sceneBitmap = loadedImage;
                sceneImg.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                ToastUtils.showError("图片加载失败");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
            }
        });
        scene_id = sceneDetails.getData().getScene_id();
        selectList = new ArrayList<UsedLabelBean>();
        for (int i = 0; i < sceneDetails.getData().getTags().size(); i++) {
            UsedLabelBean usedLabelBean = new UsedLabelBean(sceneDetails.getData().getTags().get(i) + "", sceneDetails.getData().getTag_titles().get(i));
            selectList.add(usedLabelBean);
        }
        for (int i = 0; i < selectList.size(); i++) {
            UsedLabelBean labelBean = selectList.get(i);
            addToLinear(labelBean.getTitle_cn(), Integer.parseInt(labelBean.get_id()));
        }
        MainApplication.tagInfoList = new ArrayList<TagItem>();
        for (int i = 0; i < sceneDetails.getData().getProduct().size(); i++) {
            TagItem tagItem = new TagItem();
            tagItem.setId(sceneDetails.getData().getProduct().get(i).getId());
            tagItem.setName(sceneDetails.getData().getProduct().get(i).getTitle());
            tagItem.setPrice(sceneDetails.getData().getProduct().get(i).getPrice());
            tagItem.setX(sceneDetails.getData().getProduct().get(i).getX());
            tagItem.setY(sceneDetails.getData().getProduct().get(i).getY());
            MainApplication.tagInfoList.add(tagItem);
        }
        locationTv.setText(sceneDetails.getData().getAddress());
        lng = sceneDetails.getData().getLocation().getCoordinates().get(0);
        lat = sceneDetails.getData().getLocation().getCoordinates().get(1);
        addQingjingToLinear(sceneDetails.getData().getScene_title());
//                Log.e("<<<编辑场景", lat + "," + lng);

    }

    //获得当前位置信息
    private void getCurrentLocation() {
        dialog.show();
        MapUtil.getCurrentLocation(CreateSceneActivity.this, new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    Log.e("<<<获取位置", location[1] + "," + location[0]);
//                    getAddressByCoordinate();
                    dialog.dismiss();
                }
            }
        });
    }

    //根据经纬度获得周边位置信息
    private void getAddressByCoordinate() {
        dialog.show();
        MapUtil.getAddressByCoordinate(location[1], location[0], new MapUtil.MyOnGetGeoCoderResultListener() {
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                dialog.dismiss();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                city = result.getAddressDetail().city;
//                    province = result.getAddressDetail().province;
                district = result.getAddressDetail().district;
                poiInfoList = result.getPoiList();
                if (poiInfoList == null) {
                    return;
                }
                ImageUtils.location = null;
                MapUtil.destroyLocationClient();
                recyclerAdapter = new AddressRecycleAdapter(CreateSceneActivity.this, poiInfoList, CreateSceneActivity.this);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_create_scene_add_addresslinear:
//            case R.id.activity_create_scene_addressrelative:
            case R.id.location:
                if (location == null) {
                    getCurrentLocation();
                    return;
                }
                Intent intent = new Intent(CreateSceneActivity.this, BDSearchAddressActivity.class);
                intent.putExtra("latLng", new LatLng(location[1], location[0]));
                startActivityForResult(intent, DataConstants.REQUESTCODE_CREATESCENE_BDSEARCH);
                break;
            case R.id.activity_create_scene_deleteaddress:
                locationTv.setText("添加地点");
                deleteAddressImg.setVisibility(View.GONE);
                break;
            case R.id.activity_create_scene_qingjing:
//                if (location == null) {
//                    getCurrentLocation();
//                    return;
//                }
                Intent intent1 = new Intent(CreateSceneActivity.this, SelectQingjingActivity.class);
//                intent1.putExtra("latLng", new LatLng(location[1], location[0]));
                startActivityForResult(intent1, DataConstants.REQUESTCODE_CREATESCENE_SELECTQJ);
//                Intent intent1 = new Intent(CreateSceneActivity.this, SelectOrSearchQJActivity.class);
//                startActivity(intent1);
                break;
            case R.id.activity_create_scene_labelrelative:
                if (!LoginInfo.isUserLogin()) {
//                    Toast.makeText(CreateSceneActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(CreateSceneActivity.this, OptRegisterLoginActivity.class));
                    return;
                }
                MainApplication.selectList = selectList;
                startActivityForResult(new Intent(CreateSceneActivity.this, AddLabelActivity.class), DataConstants.REQUESTCODE_CREATESCENE_ADDLABEL);
                break;
            case R.id.title_continue:
                if (!LoginInfo.isUserLogin()) {
//                    Toast.makeText(CreateSceneActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(CreateSceneActivity.this, OptRegisterLoginActivity.class));
                    return;
                }
                if (TextUtils.isEmpty(titleEdt.getText())) {
                    dialog.showErrorWithStatus("请填写" + (MainApplication.tag == 2 ? "情" : "场") + "景标题");
//                    Toast.makeText(CreateSceneActivity.this, , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contentEdt.getText())) {
                    dialog.showErrorWithStatus("请填写" + (MainApplication.tag == 2 ? "情" : "场") + "景描述");
//                    Toast.makeText(CreateSceneActivity.this, "请填写" + (MainApplication.tag == 2 ? "情" : "场") + "景描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectList == null || selectList.size() == 0) {
                    dialog.showErrorWithStatus("请添加标签");
//                    Toast.makeText(CreateSceneActivity.this, "请添加标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(locationTv.getText()) && locationTv.getText().toString().equals("添加地点")) {
                    dialog.showErrorWithStatus("请添加地点");
                    return;
                }
                dialog.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Black);
                StringBuilder tags = new StringBuilder();
                for (UsedLabelBean each : selectList) {
                    tags.append(",").append(each.get_id());
                }
                if (tags.length() > 0) {
                    tags.deleteCharAt(0);
                }
                Log.e("<<<", "创建" + MainApplication.tag);
                if (MainApplication.tag == 1) {
                    if (scene_id == null) {
                        dialog.dismiss();
                        dialog.showErrorWithStatus("请选择所属情景");
//                        Toast.makeText(CreateSceneActivity.this, "请选择所属情景", Toast.LENGTH_SHORT).show();
                        return;
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
                    DataPaser.createScene(sceneDetails == null ? null : sceneDetails.getData().get_id(), tmp, titleEdt.getText().toString(), contentEdt.getText().toString(),
                            scene_id, tags.toString(), product_id.toString(), product_title.toString(),
                            product_price.toString(), product_x.toString(), product_y.toString(), locationTv.getText().toString(),
                            lat + "", lng + "",
                            handler);
                } else if (MainApplication.tag == 2) {
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
                    DataPaser.createQingjing(qingjingDetails == null ? null : qingjingDetails.getData().get_id(), titleEdt.getText().toString(), contentEdt.getText().toString(),
                            tags.toString(), locationTv.getText().toString(), tmp, lat + "", lng + "", handler);
                } else {
                    dialog.dismiss();
                }
                break;
            case R.id.title_back:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        if (getIntent().hasExtra(SceneDetailActivity.class.getSimpleName()) || getIntent().hasExtra(QingjingDetailActivity.class.getSimpleName())) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateSceneActivity.this);
            builder.setMessage("返回上一步？");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CreateSceneActivity.this.finish();
                }
            });
            builder.setNegativeButton("取消创建", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(CreateSceneActivity.this, MainActivity.class));
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case DataConstants.RESULTCODE_CREATESCENE_SEARCHQJ:
                    SearchBean.SearchItem searchItem = (SearchBean.SearchItem) data.getSerializableExtra("searchqj");
                    if (searchItem != null) {
                        scene_id = searchItem.get_id();
                        qingjingLinear.removeAllViews();
                        addQingjingToLinear(searchItem.getTitle());
                    }
                    break;
                case DataConstants.RESULTCODE_MAP_SELECTQJ:
                    QingJingItem qingJing = (QingJingItem) data.getSerializableExtra("qingjing");
                    if (qingJing != null) {
                        scene_id = qingJing._id + "";
                        qingjingLinear.removeAllViews();
                        addQingjingToLinear(qingJing.title);
                    }
                    break;
                case DataConstants.RESULTCODE_CREATESCENE_SELECTQJ:
                    QingJingListBean.QingJingItem qingJingItem = (QingJingListBean.QingJingItem) data.getSerializableExtra("qingjing");
                    if (qingJingItem != null) {
                        scene_id = qingJingItem.get_id();
                        qingjingLinear.removeAllViews();
                        addQingjingToLinear(qingJingItem.getTitle());
                    }
                    break;
                case DataConstants.RESULTCODE_CREATESCENE_BDSEARCH:
                    PoiInfo poiInfo = data.getParcelableExtra(PoiInfo.class.getSimpleName());
                    String city = data.getStringExtra("city");
                    String district = data.getStringExtra("district");
                    if (poiInfo != null) {
                        locationTv.setText(poiInfo.name);
                        deleteAddressImg.setVisibility(View.VISIBLE);
                        lng = poiInfo.location.longitude;
                        lat = poiInfo.location.latitude;
                    }
                    break;
                case DataConstants.RESULTCODE_CREATESCENE_ADDLABEL:
                    if (MainApplication.selectList != null) {
                        if (selectList != null) {
                            selectList.clear();
                            labelLinear.removeAllViews();
                        }
                        selectList = MainApplication.selectList;
                        MainApplication.selectList = null;
                        for (int i = 0; i < selectList.size(); i++) {
                            UsedLabelBean labelBean = selectList.get(i);
                            addToLinear(labelBean.getTitle_cn(), Integer.parseInt(labelBean.get_id()));
                        }
                    }
                    break;
            }
        }
    }

    private void addQingjingToLinear(String title) {
        qingjingLinear.setVisibility(View.VISIBLE);
        View view = View.inflate(CreateSceneActivity.this, R.layout.item_horizontal_address, null);
        TextView textView = (TextView) view.findViewById(R.id.item_horizontal_tv);
        textView.setText(title);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        qingjingLinear.addView(view);
    }


    //标签
    private void addToLinear(String str, int _id) {
        labelHorizontal.setVisibility(View.VISIBLE);
        View view = View.inflate(CreateSceneActivity.this, R.layout.view_horizontal_label_item, null);
        TextView textView = (TextView) view.findViewById(R.id.view_horizontal_label_item_tv);
        textView.setText(str);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.rightMargin = DensityUtils.dp2px(CreateSceneActivity.this, 10);
        view.setLayoutParams(lp);
        view.setTag(_id);
        labelLinear.addView(view);
    }

    @Override
    public void click(int postion) {
        locationTv.setText(String.format("%s %s", city, poiInfoList.get(postion).name));
        deleteAddressImg.setVisibility(View.VISIBLE);
        lng = poiInfoList.get(postion).location.longitude;
        lat = poiInfoList.get(postion).location.latitude;
        recyclerView.setVisibility(View.GONE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CREATE_QINGJING:
                    dialog.dismiss();
                    AddProductBean netBean1 = (AddProductBean) msg.obj;
                    if (netBean1.isSuccess()) {
                        if (QingjingDetailActivity.instance != null) {
                            QingjingDetailActivity.instance.finish();
                        }
                        dialog.showSuccessWithStatus("您的" + (MainApplication.tag == 2 ? "情" : "场") + "景发布成功，品味又升级啦");
                        Intent in = new Intent(CreateSceneActivity.this, QingjingDetailActivity.class);
                        in.putExtra("id", netBean1.getData().getId());
                        in.putExtra("create", true);
                        startActivity(in);
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
                        CreateSceneActivity.this.finish();
                    } else {
                        Toast.makeText(CreateSceneActivity.this, netBean1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.CREATE_SCENE:
                    dialog.dismiss();
                    AddProductBean netBean = (AddProductBean) msg.obj;
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
                        Intent intent = new Intent(CreateSceneActivity.this, SceneDetailActivity.class);
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
                        CreateSceneActivity.this.finish();
                    } else {
                        Toast.makeText(CreateSceneActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
