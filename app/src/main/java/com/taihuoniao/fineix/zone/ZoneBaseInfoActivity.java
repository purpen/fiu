package com.taihuoniao.fineix.zone;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.album.ImageLoaderEngine;
import com.taihuoniao.fineix.album.Picker;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.user.ImageCropActivity;
import com.taihuoniao.fineix.utils.FileCameraUtil;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_CAPTURE_CAMERA;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_PICK_IMAGE;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PERMISSION_CODE;


/**
 * 地盘的基本信息
 */
public class ZoneBaseInfoActivity extends BaseActivity implements View.OnClickListener{
    private static final int REQUEST_MODIFY_AVATAR = 99;
    private static final int REQUEST_MODIFY_TITLE = 100;
    private static final int REQUEST_MODIFY_SUBTITLE = 101;
    private static final int REQUEST_ZONE_CATEGORY = 102;
    private static final int REQUEST_ZONE_TAGS = 103;
    @Bind(R.id.item_zone_avatar)
    CustomItemLayout itemZoneAvatar;
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.item_zone_title)
    CustomItemLayout itemZoneTitle;
    @Bind(R.id.item_sub_title)
    CustomItemLayout itemSubTitle;
    @Bind(R.id.item_zone_category)
    CustomItemLayout itemZoneCategory;
    @Bind(R.id.item_zone_tags)
    CustomItemLayout itemZoneTags;
    @Bind(R.id.item_zone_address)
    CustomItemLayout itemZoneAddress;
    @Bind(R.id.item_zone_phone)
    CustomItemLayout itemZonePhone;
    @Bind(R.id.item_zone_business)
    CustomItemLayout itemZoneBusiness;
    private ZoneDetailBean zoneDetailBean;
    private Uri mUri;
    private Bitmap bitmap;
    public ZoneBaseInfoActivity() {
        super(R.layout.activity_zone_base_info);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            zoneDetailBean = intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        WindowUtils.chenjin(activity);
        customHead.setHeadCenterTxtShow(true, R.string.title_zone_info);
        itemZoneAvatar.setTVStyle(0, R.string.zone_avatar, R.color.color_666);
        itemZoneAvatar.setUserAvatar(null);
        itemZoneTitle.setTVStyle(0, R.string.zone_name, R.color.color_666);
        itemSubTitle.setTVStyle(0, R.string.zone_subtitle, R.color.color_666);
        itemZoneCategory.setTVStyle(0, R.string.zone_category, R.color.color_666);
        itemZoneTags.setTVStyle(0, R.string.zone_tags, R.color.color_666);
        itemZoneAddress.setTVStyle(0, R.string.zone_address, R.color.color_666);
        itemZonePhone.setTVStyle(0, R.string.zone_phone, R.color.color_666);
        itemZoneBusiness.setTVStyle(0, R.string.zone_business_times, R.color.color_666);
        if (zoneDetailBean != null) {
            GlideUtils.displayImage(zoneDetailBean.avatar_url, itemZoneAvatar.getAvatarIV());
            itemZoneCategory.setTvArrowLeftStyle(true, zoneDetailBean.category.title, R.color.color_333);
            itemSubTitle.setTvArrowLeftStyle(true, zoneDetailBean.sub_title, R.color.color_333);
            itemZoneTitle.setTvArrowLeftStyle(true, zoneDetailBean.title, R.color.color_333);
            itemZoneAddress.setTvArrowLeftStyle(true, zoneDetailBean.address, R.color.color_333);
            itemZonePhone.setTvArrowLeftStyle(true, zoneDetailBean.extra.tel, R.color.color_333);
            itemZoneBusiness.setTvArrowLeftStyle(true, zoneDetailBean.extra.shop_hours, R.color.color_333);
        }
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


    protected void getImageFromAlbum() {
        Picker.from(this)
                .count(1)
                .enableCamera(false)
                .singleChoice()
                .setEngine(new ImageLoaderEngine())
                .forResult(REQUEST_CODE_PICK_IMAGE);

    }


    protected void getImageFromCamera() {
        if (!Util.isExternalStorageStateMounted()) {
            ToastUtils.showInfo("请插入SD卡");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mUri = FileCameraUtil.getUriForFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(REQUEST_PERMISSION_CODE)
    private void getRequestYes(List<String> grantedPermissions) {
        if (grantedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE")){
            getImageFromAlbum();
        }else if(grantedPermissions.contains("android.permission.CAMERA")){
            getImageFromCamera();
        }
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(REQUEST_PERMISSION_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
        }
    }

    private void toCropActivity(Uri uri) {
        ImageCropActivity.setOnClipCompleteListener(new ImageCropActivity.OnClipCompleteListener() {
            @Override
            public void onClipComplete(Bitmap bitmap) {
                ZoneBaseInfoActivity.this.bitmap=bitmap;
                itemZoneAvatar.getAvatarIV().setImageBitmap(bitmap);
            }
        });
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.class.getSimpleName(), uri);
        intent.putExtra(ImageCropActivity.class.getName(), TAG);
        intent.putExtra(ZoneManagementActivity.class.getSimpleName(),zoneDetailBean._id);
        startActivity(intent);
    }

    @OnClick({R.id.item_zone_avatar, R.id.item_zone_title, R.id.item_sub_title, R.id.item_zone_category, R.id.item_zone_tags, R.id.item_zone_address, R.id.item_zone_phone, R.id.item_zone_business})
    void performClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.item_zone_avatar: //地盘头像
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "上传地盘logo"));
                break;
            case R.id.item_zone_title: //地盘标题
                if (zoneDetailBean == null) return;
                intent = new Intent(activity, ZoneEditTitleActivity.class);
                intent.putExtra(ZoneEditTitleActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent, REQUEST_MODIFY_TITLE);
                break;
            case R.id.item_sub_title: //地盘副标题
                if (zoneDetailBean == null) return;
                intent = new Intent(activity, ZoneEditSubtitleActivity.class);
                intent.putExtra(ZoneEditSubtitleActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent, REQUEST_MODIFY_SUBTITLE);
                break;
            case R.id.item_zone_category: //地盘分类
                if (zoneDetailBean == null) return;
                intent = new Intent(activity, ZoneClassifyActivity.class);
                intent.putExtra(ZoneClassifyActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent, REQUEST_ZONE_CATEGORY);
                break;
            case R.id.item_zone_tags: //地盘标签
                if (zoneDetailBean == null) return;
                intent = new Intent(activity, ZoneEditTagActivity.class);
                intent.putExtra(ZoneEditTagActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent,REQUEST_ZONE_TAGS);
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
            default:

                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_MODIFY_AVATAR: //修改头像

                break;
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
            case REQUEST_MODIFY_TITLE: //修改标题
                String zoneName = data.getStringExtra(ZoneEditTitleActivity.class.getSimpleName());
                if (TextUtils.isEmpty(zoneName)) return;
                itemZoneTitle.setTvArrowLeftStyle(true, zoneName, R.color.color_333);
                break;
            case REQUEST_MODIFY_SUBTITLE: //修改副标题
                zoneDetailBean = data.getParcelableExtra(ZoneEditSubtitleActivity.class.getSimpleName());
                if (zoneDetailBean==null) return;
                itemSubTitle.setTvArrowLeftStyle(true,zoneDetailBean.sub_title, R.color.color_333);
                break;
            case REQUEST_ZONE_CATEGORY: //修改地盘分类
                zoneDetailBean = data.getParcelableExtra(ZoneClassifyActivity.class.getSimpleName());
                if (zoneDetailBean == null) return;
                itemZoneCategory.setTvArrowLeftStyle(true, zoneDetailBean.category.title, R.color.color_333);
                break;
            case REQUEST_ZONE_TAGS:  //地盘标签
                zoneDetailBean = data.getParcelableExtra(ZoneEditTagActivity.class.getSimpleName());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap!=null) bitmap.recycle();
        if (mUri!=null) mUri=null;
        if (zoneDetailBean!=null) zoneDetailBean=null;
    }
}
