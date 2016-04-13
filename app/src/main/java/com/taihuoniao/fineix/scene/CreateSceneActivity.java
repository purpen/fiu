package com.taihuoniao.fineix.scene;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddressRecycleAdapter;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

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
    private EditText contentEdt;
    private EditText titleEdt;
    private TextView locationTv;
    private RecyclerView recyclerView;
    private AddressRecycleAdapter recyclerAdapter;
    private RelativeLayout labelRelative;
    private LinearLayout labelLinear;
    private RelativeLayout qingjingRelative;
    //从选择标签界面返回的用户选择的标签
    private List<UsedLabelBean> selectList;
    //地址列表数据
    private List<PoiInfo> poiInfoList;


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
        recyclerView = (RecyclerView) findViewById(R.id.activity_create_scene_recycler);
        labelRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_labelrelative);
        labelLinear = (LinearLayout) findViewById(R.id.activity_create_scene_labellinear);
        qingjingRelative = (RelativeLayout) findViewById(R.id.activity_create_scene_qingjing);
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
        ImageUtils.asyncLoadImage(CreateSceneActivity.this, imageUri, new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                sceneImg.setImageBitmap(result);
                sceneImg.setCornerRadius(DensityUtils.dp2px(CreateSceneActivity.this, 5));
            }
        });
        poiInfoList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        double[] location = ImageUtils.location;
        if (location != null) {
            //图片上有位置信息
            Log.e("<<<", "不为空");
            MapUtil.getAddressByCoordinate(location[1], location[0], new MapUtil.OnGetReverseGeoCodeResultListener() {
                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
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
                    recyclerAdapter = new AddressRecycleAdapter(CreateSceneActivity.this, poiInfoList, CreateSceneActivity.this);
                    recyclerView.setAdapter(recyclerAdapter);
                }
            });
        } else {
            //图片上无位置信息
            Log.e("<<<", "为空");

        }

        labelRelative.setOnClickListener(this);
        qingjingRelative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_create_scene_qingjing:
                Toast.makeText(CreateSceneActivity.this, "情景未做", Toast.LENGTH_SHORT).show();

                break;
            case R.id.activity_create_scene_labelrelative:
                //需要登录
                Toast.makeText(CreateSceneActivity.this, "需要登录", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(CreateSceneActivity.this, AddLabelActivity.class), DataConstants.REQUESTCODE_CREATESCENE_ADDLABEL);
                break;
            case R.id.title_continue:

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
                SelectPhotoOrCameraActivity.instance.finish();
                CropPictureActivity.instance.finish();
                EditPictureActivity.instance.finish();
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
                case DataConstants.RESULTCODE_CREATESCENE_ADDLABEL:
                    if (MainApplication.selectList != null) {
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
        Log.e("<<<", "itemClick:" + postion);
    }
}
