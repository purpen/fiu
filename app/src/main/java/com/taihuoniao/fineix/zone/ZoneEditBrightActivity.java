package com.taihuoniao.fineix.zone;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.DataImageView;
import com.taihuoniao.fineix.view.RichTextEditor;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.bean.BrightItemBean;
import com.taihuoniao.fineix.zone.bean.LightSpotImageBean;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.taihuoniao.fineix.zone.db.ZoneBrightSqliteOpenHelper;
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

/**
 * 编辑亮点
 */
public class ZoneEditBrightActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.richEditor)
    RichTextEditor richEditor;
    private ZoneBrightSqliteOpenHelper sqliteOpenHelper;
    private File mCurrentPhotoFile;
    private ZoneDetailBean zoneDetailBean;
    private List<RichTextEditor.EditData> editList;
    RichTextEditor.ImageQueue<DataImageView> imageQueue;
    private WaittingDialog dialog;
    public ZoneEditBrightActivity() {
        super(R.layout.activity_zone_edit_bright);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TAG)){
            zoneDetailBean = intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        WindowUtils.chenjin(this);
        customHead.setHeadCenterTxtShow(true,R.string.zone_edit_spot);
        customHead.setHeadRightTxtShow(true, R.string.zone_public_bright);
        dialog = new WaittingDialog(activity);
        editList=new ArrayList<>();
        sqliteOpenHelper = new ZoneBrightSqliteOpenHelper(this);
        if (null==zoneDetailBean.bright_spot) {
            ToastUtils.showError(R.string.data_err);
            return;
        }
        if (zoneDetailBean.bright_spot.size()>0){
            List<BrightItemBean> list=new ArrayList<>();
            BrightItemBean brightItem;
            for (String item:zoneDetailBean.bright_spot){
                brightItem=new BrightItemBean();
                if (!item.contains(Constants.SEPERATOR)) continue;
                String[] split = item.split(Constants.SEPERATOR);
                if (TextUtils.equals(split[0], Constants.TEXT_TYPE)) {
                    brightItem.content=split[1];
                } else if (TextUtils.equals(split[0], Constants.IMAGE_TYPE)) {
                    brightItem.img = split[1];
                }
                list.add(brightItem);
            }
            traverseAndInsertImage(list);
        }else {
            LogUtil.e(TAG,"尝试从数据库读取草稿");
            List<BrightItemBean> list = sqliteOpenHelper.query();
            traverseAndInsertImage(list);
        }
    }

    /**
     * 遍历并插入图片
     * @param list
     */
    private void traverseAndInsertImage(List<BrightItemBean> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            BrightItemBean item = list.get(i);
            if (TextUtils.isEmpty(item.content) && TextUtils.isEmpty(item.img)) continue;
            if (TextUtils.isEmpty(item.content)) {
                insertImageAtIndex(i, item.img);
            } else {
                insertEditText(i, item.content);
            }
        }
    }


    private void insertImageAtIndex(int index, String imgPath) {
        richEditor.insertImageAtIndex(index, imgPath);
    }



    protected void openCamera() {
        mCurrentPhotoFile = ImageUtils.getDefaultFile();
        if (null==mCurrentPhotoFile) return;
        ImageUtils.getImageFromCamera(activity, ImageUtils.getUriForFile(mCurrentPhotoFile));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(Constants.REQUEST_PERMISSION_CODE)
    private void getRequestYes(List<String> grantedPermissions) {
        for (String item : grantedPermissions){
            if (item.contains("android.permission.READ_EXTERNAL_STORAGE")) {
                int count = richEditor.getImageViewCount();
                ImageUtils.getImageFromAlbum(activity,remainImageCount(count));
            }
            if(item.contains("android.permission.CAMERA")) {
                mCurrentPhotoFile = ImageUtils.getDefaultFile();
                if (null==mCurrentPhotoFile) return;
                ImageUtils.getImageFromCamera(activity, ImageUtils.getUriForFile(mCurrentPhotoFile));
            }
        }

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

    /**
     * 添加图片到富文本剪辑器
     */
    private void insertEditText(int index, String content) {
        richEditor.insertEditText(index, content);
    }


    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {
        richEditor.insertImage(imagePath);
    }

    @OnClick({R.id.button1,R.id.tv_head_right})
    void onClick(View view) {
        int count;
        switch (view.getId()) {
            case R.id.button1:// 打开系统相册
                count=remainImageCount(richEditor.getImageViewCount());
                if (count==0) return;
                if (AndPermission.hasPermission(activity,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ImageUtils.getImageFromAlbum(activity,count);
                } else {
                    // 申请权限。
                    AndPermission.with(this)
                            .requestCode(Constants.REQUEST_PERMISSION_CODE)
                            .permission(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)
                            .send();
                }
                break;
//            case R.id.button2:// 打开相机
//                count=remainImageCount(richEditor.getImageViewCount());
//                if (count==0) return;
//                if (AndPermission.hasPermission(activity, Manifest.permission.CAMERA)) {
//                    openCamera();
//                } else {
//                    // 申请权限。
//                    AndPermission.with(this)
//                            .requestCode(Constants.REQUEST_PERMISSION_CODE)
//                            .permission(Manifest.permission.CAMERA)
//                            .send();
//                }
//
//                break;
            case R.id.tv_head_right: //发布
                editList.clear();
                editList.addAll(richEditor.buildEditData());
                upload();
                break;
            case R.id.btn: //保存草稿
                sqliteOpenHelper.resetTable();
                List<RichTextEditor.EditData> draftList = richEditor.buildEditData();
                for (RichTextEditor.EditData item : draftList) {
                    if (!TextUtils.isEmpty(item.imagePath)) {
                        sqliteOpenHelper.insert(item.imagePath, null);
                    } else if (!TextUtils.isEmpty(item.inputStr)) {
                        sqliteOpenHelper.insert("", item.inputStr);
                    }
                }
                break;
            default:
                break;
        }
    }



    /**
     * 还可以上传的图片数量
     * @param count
     * @return
     */
    private int remainImageCount(int count) {
        if (count== Constants.LIGHT_SPOT_IMAGE_COUNT){
            ToastUtils.showInfo(R.string.lightspot_count_limit);
            return 0;
        }
        return Constants.LIGHT_SPOT_IMAGE_COUNT-count;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_PICK_IMAGE:
                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                    if (null==mSelected) return;
                    for (Uri uri : mSelected) {
                        insertBitmap(ImageUtils.getRealFilePath(uri));
                    }
                    prepareDataAndUploadImage();
                    break;
                case Constants.REQUEST_CODE_CAPTURE_CAMERA:
                    if (null == mCurrentPhotoFile) return;
                    insertBitmap(mCurrentPhotoFile.getAbsolutePath());
                    prepareDataAndUploadImage();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 准备要上传的图片
     */
    private void prepareDataAndUploadImage() {
        imageQueue=richEditor.getImageQueue();
        if (null==imageQueue) return;
        uploadImages(imageQueue.remove());

    }

    //上传图片
    private void uploadImages(final DataImageView imageView) {
        if (null==imageView) return;
        if (null==imageView.getBitmap()) return;
        HashMap<String,String> params=new HashMap<>();
        params.put("id",zoneDetailBean._id);
        params.put("tmp", Util.saveBitmap2Base64Str(imageView.getBitmap()));
        params.put("type","2");
        HttpRequest.post(params, URL.ZONE_ADD_COVER, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<LightSpotImageBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<LightSpotImageBean>>() {
                });
                LogUtil.e("还有"+imageQueue.size()+"张图片待上传");
                if (response.isSuccess()){
                    imageView.setAbsolutePath(response.getData().filepath.huge);
                    imageView.isUpload = true;
                }
                uploadImages(imageQueue.remove());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    //上传亮点
    private void upload() {
        if (!richEditor.isAllImageUploaded()){
            ToastUtils.showInfo(R.string.uploading_image_waiting);
            return;
        }
        List<String> strings=new ArrayList<>();
        StringBuilder builder;
        for (RichTextEditor.EditData item:editList){
            builder = new StringBuilder();
            if(!TextUtils.isEmpty(item.inputStr)){
                builder.append(Constants.TEXT_TYPE).append(Constants.SEPERATOR).append(item.inputStr);
                strings.add(builder.toString());
            }else if(!TextUtils.isEmpty(item.imagePath)){
                builder.append(Constants.IMAGE_TYPE).append(Constants.SEPERATOR).append(item.imagePath);
                strings.add(builder.toString());
            }
        }
        String s = JsonUtil.list2Json(strings);

        if (TextUtils.isEmpty(s)) return;
        HashMap<String,String> params=new HashMap<>();
        params.put("id",zoneDetailBean._id);
        params.put("bright_spot",s); //["[text]:!天气不错", "[img]:!http://img_url"]
        HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (null!=dialog &&!dialog.isShowing()&& !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (null!=dialog &&dialog.isShowing()&& !activity.isFinishing()) dialog.dismiss();
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()){
                    ToastUtils.showSuccess(response.getMessage());
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (null!=dialog &&dialog.isShowing()&& !activity.isFinishing()) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

}
