package com.taihuoniao.fineix.zone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.view.RichTextEditor;
import com.taihuoniao.fineix.zone.bean.BrightItemBean;
import com.taihuoniao.fineix.zone.db.ZoneBrightSqliteOpenHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;

/**
 * 编辑亮点
 */
public class ZoneEditBrightActivity extends BaseActivity {
    @Bind(R.id.richEditor)
    RichTextEditor richEditor;
    ZoneBrightSqliteOpenHelper sqliteOpenHelper;
    private File mCurrentPhotoFile;
    public ZoneEditBrightActivity() {
        super(R.layout.activity_zone_edit_bright);
    }

    @Override
    protected void initView() {
        sqliteOpenHelper = new ZoneBrightSqliteOpenHelper(this);
        List<BrightItemBean> list = sqliteOpenHelper.query();
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

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<RichTextEditor.EditData> editList) {
        for (RichTextEditor.EditData itemData : editList) {
            if (!TextUtils.isEmpty(itemData.inputStr)) {
                Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (!TextUtils.isEmpty(itemData.imagePath)) {
                Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
            }
        }
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
        if (grantedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE")) {
            ImageUtils.getImageFromAlbum(activity, 10);
        } else if (grantedPermissions.contains("android.permission.CAMERA")) {
            mCurrentPhotoFile = ImageUtils.getDefaultFile();
            if (null==mCurrentPhotoFile) return;
            ImageUtils.getImageFromCamera(activity, ImageUtils.getUriForFile(mCurrentPhotoFile));
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

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.btn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:// 打开系统相册
                if (AndPermission.hasPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ImageUtils.getImageFromAlbum(activity, 10);
                } else {
                    // 申请权限。
                    AndPermission.with(this)
                            .requestCode(Constants.REQUEST_PERMISSION_CODE)
                            .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .send();
                }
                break;
            case R.id.button2:// 打开相机
                if (AndPermission.hasPermission(activity, Manifest.permission.CAMERA)) {
                    openCamera();
                } else {
                    // 申请权限。
                    AndPermission.with(this)
                            .requestCode(Constants.REQUEST_PERMISSION_CODE)
                            .permission(Manifest.permission.CAMERA)
                            .send();
                }

                break;
            case R.id.button3: //发布
                List<RichTextEditor.EditData> publishList = richEditor.buildEditData();
                // 下面的代码可以上传、或者保存，请自行实现
                dealEditData(publishList);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_PICK_IMAGE:
                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                    if (null==mSelected) return;
                    for (Uri uri : mSelected) {
                        insertBitmap(getRealFilePath(uri));
                    }
                    break;
                case Constants.REQUEST_CODE_CAPTURE_CAMERA:
                    if (null == mCurrentPhotoFile) return;
                    insertBitmap(mCurrentPhotoFile.getAbsolutePath());
                    break;
                default:
                    break;
            }
        }
    }
}
