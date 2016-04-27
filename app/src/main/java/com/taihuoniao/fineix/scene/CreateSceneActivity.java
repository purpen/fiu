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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddressRecycleAdapter;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.BDSearchAddressActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.Base64Utils;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/6.
 */
public class CreateSceneActivity extends BaseActivity implements View.OnClickListener, EditRecyclerAdapter.ItemClick {
    //上个界面传递过来的图片存储地址
    private Uri imageUri;
    //控件
    private GlobalTitleLayout titleLayout;
    //    private ImageView sceneImg;
    private RoundedImageView sceneImg;
    private Bitmap sceneBitmap;//图片
    private EditText contentEdt;
    private EditText titleEdt;
    private TextView locationTv;
    private LinearLayout addAddressLinear;
    private ImageView locationImg;
    private RelativeLayout addressRelative;
    private RecyclerView recyclerView;
    private AddressRecycleAdapter recyclerAdapter;
    private TextView addressTv, areaTv;
    private ImageView deleteAddressImg;
    private RelativeLayout labelRelative;
    private LinearLayout labelLinear;
    private RelativeLayout qingjingRelative;
    private LinearLayout qingjingLinear;
    //图片上存储的地址信息
    private double[] location;
    //工具
    private WaittingDialog dialog;
    //从选择标签界面返回的用户选择的标签
    private List<UsedLabelBean> selectList;
    //地址列表数据
    private List<PoiInfo> poiInfoList;
    //位置信息
    private String city, district;
    private double lng, lat;


    public CreateSceneActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_create_scene);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_create_scene_titlelayout);
        sceneImg = (RoundedImageView) findViewById(R.id.activity_create_scene_img);
        contentEdt = (EditText) findViewById(R.id.activity_create_scene_contentedt);
        titleEdt = (EditText) findViewById(R.id.activity_create_scene_titleedt);
        locationTv = (TextView) findViewById(R.id.activity_create_scene_location);
        addAddressLinear = (LinearLayout) findViewById(R.id.activity_create_scene_add_addresslinear);
        addressRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_addressrelative);
        locationImg = (ImageView) findViewById(R.id.location);
        recyclerView = (RecyclerView) findViewById(R.id.activity_create_scene_recycler);
        addressTv = (TextView) findViewById(R.id.activity_create_scene_address);
        areaTv = (TextView) findViewById(R.id.activity_create_scene_area);
        deleteAddressImg = (ImageView) findViewById(R.id.activity_create_scene_deleteaddress);
        labelRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_labelrelative);
        labelLinear = (LinearLayout) findViewById(R.id.activity_create_scene_labellinear);
        qingjingRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_qingjing);
        qingjingLinear = (LinearLayout) findViewById(R.id.activity_create_scene_qingjinglinear);
        if (MainApplication.tag == 2) {
            qingjingLinear.setVisibility(View.GONE);
            qingjingRelative.setVisibility(View.GONE);
        }
        dialog = new WaittingDialog(CreateSceneActivity.this);
    }

    @Override
    protected void initList() {
        imageUri = getIntent().getData();
        titleLayout.setFocusable(true);
        titleLayout.setFocusableInTouchMode(true);
        titleLayout.requestFocus();
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setBackListener(this);
        titleLayout.setTitle(R.string.create_scene, getResources().getColor(R.color.black333333));
        titleLayout.setRightTv(R.string.publish, getResources().getColor(R.color.yellow_bd8913), this);
        if (MainApplication.tag == 2) {
            titleLayout.setRightTv(R.string.publish, getResources().getColor(R.color.black333333), this);
            titleLayout.setTitle(R.string.create_qingjing, getResources().getColor(R.color.black333333));
        }
        ImageUtils.asyncLoadImage(CreateSceneActivity.this, imageUri, new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                sceneBitmap = result;
                sceneImg.setImageBitmap(result);
                sceneImg.setCornerRadius(DensityUtils.dp2px(CreateSceneActivity.this, 5));
            }
        });
        addAddressLinear.setOnClickListener(this);
        addressRelative.setOnClickListener(this);
        locationImg.setOnClickListener(this);
        poiInfoList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        location = ImageUtils.location;
        if (location != null) {
            //图片上有位置信息
            Log.e("<<<", "不为空");
            getAddressByCoordinate();
        } else {
            //图片上无位置信息
            getCurrentLocation();
        }
        deleteAddressImg.setOnClickListener(this);
        labelRelative.setOnClickListener(this);
        qingjingRelative.setOnClickListener(this);
    }

    //获得当前位置信息
    private void getCurrentLocation() {
        dialog.show();
        MapUtil.getCurrentLocation(CreateSceneActivity.this, new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    getAddressByCoordinate();
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
                    Log.e("<<<", "list为空");
                    return;
                }
//                    for (PoiInfo each : poiInfoList) {
//                        Log.e("<<<", "address = " + each.address + ",经度 = " + each.location.longitude + ",纬度 = " + each.location.latitude
//                        +",city = "+each.city+",name = "+each.name+",phoneNum = "+each.phoneNum+",postCode = "+each.postCode
//                        +",uid="+each.uid+",describeContents = "+each.describeContents()+",tostring = "+each.toString());
//                    }
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
            case R.id.activity_create_scene_addressrelative:
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
                addressTv.setText("");
                areaTv.setText("");
                break;
            case R.id.activity_create_scene_qingjing:
                Toast.makeText(CreateSceneActivity.this, "情景未做", Toast.LENGTH_SHORT).show();

                break;
            case R.id.activity_create_scene_labelrelative:
                //需要登录
                Toast.makeText(CreateSceneActivity.this, "需要登录", Toast.LENGTH_SHORT).show();
                MainApplication.selectList = selectList;
                startActivityForResult(new Intent(CreateSceneActivity.this, AddLabelActivity.class), DataConstants.REQUESTCODE_CREATESCENE_ADDLABEL);
                break;
            case R.id.title_continue:
                Toast.makeText(CreateSceneActivity.this, "需要登录", Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(titleEdt.getText())) {
                    Toast.makeText(CreateSceneActivity.this, "请填写" + (MainApplication.tag == 2 ? "情" : "场") + "景标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contentEdt.getText())) {
                    Toast.makeText(CreateSceneActivity.this, "请填写" + (MainApplication.tag == 2 ? "情" : "场") + "景描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectList == null || selectList.size() == 0) {
                    Toast.makeText(CreateSceneActivity.this, "请添加标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(addressTv.getText()) || TextUtils.isEmpty(areaTv.getText())) {
                    Toast.makeText(CreateSceneActivity.this, "请选择地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();
                StringBuilder tags = new StringBuilder();
                for (UsedLabelBean each : selectList) {
                    tags.append(",").append(each.get_id());
                }
                if (tags.length() > 0) {
                    tags.deleteCharAt(0);
                }
                if (MainApplication.tag == 1) {
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
                            Toast.makeText(CreateSceneActivity.this, "图片过大", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sceneBitmap.compress(Bitmap.CompressFormat.JPEG, sapleSize, stream);
                        Log.e("<<<", "图片大小=" + stream.size());
                    } while (stream.size() > 512 * 1024);//最大上传图片不得超过512K
                    String tmp = Base64Utils.encodeLines(stream.toByteArray());
                    DataPaser.createScene(null, tmp, titleEdt.getText().toString(), contentEdt.getText().toString(),
                            5 + "", tags.toString(), product_id.toString(), product_title.toString(),
                            product_price.toString(), product_x.toString(), product_y.toString(), city
//                                    + district
                                    + addressTv.getText().toString(),
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
                            Toast.makeText(CreateSceneActivity.this, "图片过大", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sceneBitmap.compress(Bitmap.CompressFormat.JPEG, sapleSize, stream);
                        Log.e("<<<", "图片大小=" + stream.size());
                    } while (stream.size() > 512 * 1024);//最大上传图片不得超过512K
                    String tmp = Base64Utils.encodeLines(stream.toByteArray());
                    DataPaser.createQingjing(null, titleEdt.getText().toString(), contentEdt.getText().toString(),
                            tags.toString(), city
//                                    + district
                                    + addressTv.getText().toString(), tmp, lat + "", lng + "", handler);
                }
                break;
            case R.id.title_back:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateSceneActivity.this);
        builder.setMessage("确认放弃编辑吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (SelectPhotoOrCameraActivity.instance != null) {
                    SelectPhotoOrCameraActivity.instance.finish();
                }
                if (CropPictureActivity.instance != null) {
                    CropPictureActivity.instance.finish();
                }
                if (EditPictureActivity.instance != null) {
                    EditPictureActivity.instance.finish();
                }
                CreateSceneActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case DataConstants.RESULTCODE_CREATESCENE_BDSEARCH:
                    PoiInfo poiInfo = data.getParcelableExtra(PoiInfo.class.getSimpleName());
                    String city = data.getStringExtra("city");
                    String district = data.getStringExtra("district");
                    if (poiInfo != null) {
                        addressTv.setText(poiInfo.name);
                        areaTv.setText(String.format("%s，%s", district, city));
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


    //标签
    private void addToLinear(String str, int _id) {
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
        addressTv.setText(poiInfoList.get(postion).name);
        areaTv.setText(String.format("%s，%s", district, city));
        lng = poiInfoList.get(postion).location.longitude;
        lat = poiInfoList.get(postion).location.latitude;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CREATE_QINGJING:
                    dialog.dismiss();
                    NetBean netBean1 = (NetBean) msg.obj;
                    Toast.makeText(CreateSceneActivity.this, netBean1.getMessage(), Toast.LENGTH_SHORT).show();
                    if (netBean1.isSuccess()) {
                        MainApplication.tagInfoList = null;
                        if (SelectPhotoOrCameraActivity.instance != null) {
                            SelectPhotoOrCameraActivity.instance.finish();
                        }
                        if (CropPictureActivity.instance != null) {
                            CropPictureActivity.instance.finish();
                        }
                        if (EditPictureActivity.instance != null) {
                            EditPictureActivity.instance.finish();
                        }
                        CreateSceneActivity.this.finish();
                    }
                    break;
                case DataConstants.CREATE_SCENE:
                    dialog.dismiss();
                    NetBean netBean = (NetBean) msg.obj;
                    Toast.makeText(CreateSceneActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                    if (netBean.isSuccess()) {
                        if (SelectPhotoOrCameraActivity.instance != null) {
                            SelectPhotoOrCameraActivity.instance.finish();
                        }
                        if (CropPictureActivity.instance != null) {
                            CropPictureActivity.instance.finish();
                        }
                        if (EditPictureActivity.instance != null) {
                            EditPictureActivity.instance.finish();
                        }
                        CreateSceneActivity.this.finish();
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
