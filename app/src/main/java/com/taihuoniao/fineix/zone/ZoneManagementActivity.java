package com.taihuoniao.fineix.zone;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.BuildConfig;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.album.ImageLoaderEngine;
import com.taihuoniao.fineix.album.Picker;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.ImageCropActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.FileCameraUtil;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.adapter.ZoneEditCoversAdapter;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PERMISSION_CODE;

/**
 * 地盘管理
 */
public class ZoneManagementActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_MODIFY_BRIEF = 101;
    private static final int REQUEST_MODIFY_PHONE = 102;
    private static final int REQUEST_BUSINESS_TIME = 103;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 101;
    @Bind(R.id.custom_head)
    CustomHeadView customHeadView;
    @Bind(R.id.item_zone_base_info)
    CustomItemLayout itemZoneBaseInfo;
    @Bind(R.id.item_zone_brief)
    CustomItemLayout itemZoneBrief;
    @Bind(R.id.item_light_spot)
    CustomItemLayout itemLightSpot;
    @Bind(R.id.item_zone_address)
    CustomItemLayout itemZoneAddress;
    @Bind(R.id.item_zone_phone)
    CustomItemLayout itemZonePhone;
    @Bind(R.id.item_zone_auth)
    CustomItemLayout itemZoneAuth;
    @Bind(R.id.item_zone_business)
    CustomItemLayout itemZoneBusiness;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private String zoneId = "";
    private ZoneDetailBean zoneDetailBean;
    private WaittingDialog dialog;
    private List<ZoneDetailBean.NcoverBean> list;
    private ZoneEditCoversAdapter adapter;
    private Uri mUri;
    private String mFilePath;
    public ZoneManagementActivity() {
        super(R.layout.activity_zone_management);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            zoneId = intent.getStringExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(activity);
        customHeadView.setHeadCenterTxtShow(true, R.string.title_zone_manage);
        itemZoneBaseInfo.setTVStyle(0, R.string.zone_base_info, R.color.color_666);
        itemZoneBrief.setTVStyle(0, R.string.zone_brief, R.color.color_666);
        itemLightSpot.setTVStyle(0, R.string.zone_light_spot, R.color.color_666);
        itemZoneAddress.setTVStyle(0, R.string.zone_address, R.color.color_666);
        itemZonePhone.setTVStyle(0, R.string.zone_phone, R.color.color_666);
        itemZoneBusiness.setTVStyle(0, R.string.zone_business_times, R.color.color_666);
        itemZoneAuth.setTVStyle(0, R.string.zone_auth, R.color.color_666);
        list = new ArrayList<>();
        adapter = new ZoneEditCoversAdapter(activity,list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new ZoneCoverMarginDecoration(activity,R.dimen.dp5));
        recyclerView.setLayoutManager(new GridLayoutManager(activity,4));
        recyclerView.setAdapter(adapter);
        mFilePath = FileCameraUtil.getFileDir() + File.separator;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestNet();
    }

    @Override
    protected void installListener() {
        adapter.setOnItemClickListener(new ZoneEditCoversAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position==list.size()-1){
                    PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "更换个人主页封面"));
                }else {

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
    private View initPopView(int layout, String title) {
        View view = Util.inflateView(activity, layout, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        View iv_take_photo = view.findViewById(R.id.tv_take_photo);
        View iv_take_album = view.findViewById(R.id.tv_album);
        View iv_close = view.findViewById(R.id.tv_cancel);
        iv_take_photo.setOnClickListener(this);
        iv_take_album.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_take_photo:
                PopupWindowUtil.dismiss();
                if (AndPermission.hasPermission(activity, Manifest.permission.CAMERA)) {
                    getImageFromCamera();
                } else {
                    // 申请权限。
                    AndPermission.with(this)
                            .requestCode(REQUEST_PERMISSION_CODE)
                            .permission(Manifest.permission.CAMERA)
                            .send();
                }

                break;
            case R.id.tv_album:
                PopupWindowUtil.dismiss();
                if (AndPermission.hasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    getImageFromAlbum();
                } else {
                    // 申请权限。
                    AndPermission.with(this)
                            .requestCode(REQUEST_PERMISSION_CODE)
                            .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .send();
                }

                break;
            case R.id.tv_cancel:
                PopupWindowUtil.dismiss();
                break;
            default:
                break;
        }
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(Constants.REQUEST_PERMISSION_CODE)
    private void getRequestYes(List<String> grantedPermissions) {
        getImageFromAlbum();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(Constants.REQUEST_PERMISSION_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
        }
    }

    protected void getImageFromAlbum() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Picker.from(this)
                    .count(1)
                    .enableCamera(false)
                    .singleChoice()
                    .setEngine(new ImageLoaderEngine())
                    .forResult(REQUEST_CODE_PICK_IMAGE);
        } else {
            ToastUtils.showError("未检测到SD卡");
        }
    }


    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.showInfo("请插入SD卡");
            return;
        }

//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mUri = getUriForFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
    }

    public Uri getUriForFile() {
        File path = new File(mFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String mFileName = "tmp.png";
        File file = new File(path, mFileName);
        if (file.exists()) {
            file.delete();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_IMAGE:
                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                    if (mSelected == null) return;
                    if (mSelected.size() == 0) return;
                    toCropActivity(mSelected.get(0));
                    break;
                case REQUEST_CODE_CAPTURE_CAMERA:
                    if (mUri != null) {
                        toCropActivity(mUri);
                    }
                    break;
            }
        }
    }

    private void toCropActivity(Uri uri) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.class.getSimpleName(), uri);
        intent.putExtra(ImageCropActivity.class.getName(), TAG);
        intent.putExtra(TAG,zoneDetailBean._id);
        startActivity(intent);
    }

    @Override
    protected void requestNet() {
        HashMap params = ClientDiscoverAPI.getZoneDetailParams(zoneId,"1");
        HttpRequest.post(params, URL.ZONE_DETAIL, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                HttpResponse<ZoneDetailBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZoneDetailBean>>() {
                });
                if (response.isSuccess()) {
                    zoneDetailBean = response.getData();
                    refreshUI();
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (zoneDetailBean==null) return;
        if (list.size()>0){
            list.clear();
        }
        list.addAll(zoneDetailBean.n_covers);
        list.add(new ZoneDetailBean.NcoverBean());
        if (adapter==null){
            adapter=new ZoneEditCoversAdapter(activity,list);
        }else {
            adapter.notifyDataSetChanged();
        }
        itemZoneAddress.setTvArrowLeftStyle(true, zoneDetailBean.address, R.color.color_333);
        itemZonePhone.setTvArrowLeftStyle(true, zoneDetailBean.extra.tel, R.color.color_333);
        itemZoneBusiness.setTvArrowLeftStyle(true, zoneDetailBean.extra.shop_hours, R.color.color_333);
    }

    @OnClick({R.id.item_zone_base_info, R.id.item_zone_brief,R.id.item_zone_address,R.id.item_zone_phone,R.id.item_zone_business,R.id.item_zone_auth})
    void performClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.item_zone_base_info: //地盘基本信息
                intent = new Intent(activity, ZoneBaseInfoActivity.class);
                intent.putExtra(ZoneBaseInfoActivity.class.getSimpleName(), zoneDetailBean);
                startActivity(intent);
                break;
            case R.id.item_zone_brief://地盘简介
                intent = new Intent(activity, ZoneEditBriefActivity.class);
                intent.putExtra(ZoneEditBriefActivity.class.getSimpleName(), zoneDetailBean);
                startActivity(intent);
                break;
            case R.id.item_zone_address://地盘地址
                intent = new Intent(activity, ZoneEditAddressActivity.class);
                intent.putExtra(ZoneEditAddressActivity.class.getSimpleName(), zoneDetailBean);
                startActivity(intent);
                break;
            case R.id.item_zone_phone://地盘电话
                intent = new Intent(activity, ZoneEditPhoneActivity.class);
                intent.putExtra(ZoneEditPhoneActivity.class.getSimpleName(), zoneDetailBean);
                startActivity(intent);
                break;
            case R.id.item_zone_business://营业时间
                intent = new Intent(activity, ZoneEditTimeActivity.class);
                intent.putExtra(ZoneEditTimeActivity.class.getSimpleName(), zoneDetailBean);
                startActivity(intent);
                break;

            case R.id.item_zone_auth: //地盘认证

                break;
            default:
                break;
        }
    }

}
