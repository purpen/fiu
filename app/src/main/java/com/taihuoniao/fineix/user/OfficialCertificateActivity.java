package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.labelview.AutoLabelUI;
import com.taihuoniao.fineix.view.labelview.Label;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/22 18:47
 */
public class OfficialCertificateActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.label_view)
    AutoLabelUI label_view;
    @Bind(R.id.tv_tag)
    TextView tv_tag;
    @Bind(R.id.iv_card)
    ImageView iv_card;
    @Bind(R.id.iv_id)
    ImageView iv_id;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 101;
    private static final int REQUEST_CODE_ID = 102;
    private static final int REQUEST_CODE_CARD = 103;
    private int currentCode;
    public static final Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));

    public OfficialCertificateActivity() {
        super(R.layout.activity_official_certificate);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "官方认证");
        String[] stringArray = getResources().getStringArray(R.array.official_tags);
        for (int i = 0; i < stringArray.length; i++) {
            label_view.addLabel(stringArray[i]);
        }
    }

    @Override
    protected void installListener() {
        label_view.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(Label labelClicked) {
                tv_tag.setVisibility(View.VISIBLE);
                tv_tag.setText(labelClicked.getText());
            }
        });

    }

    @OnClick({R.id.btn, R.id.iv_clear, R.id.rl_id, R.id.rl_card})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.btn: //提交
                //TODO
                break;
            case R.id.rl_card: //上传名片 相册/拍照
                currentCode = REQUEST_CODE_CARD;
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "上传名片"));
                ImageCropActivity.setOnClipCompleteListener(new ImageCropActivity.OnClipCompleteListener() {
                    @Override
                    public void onClipComplete(Bitmap bitmap) {
                        iv_card.setVisibility(View.VISIBLE);
                        iv_card.setImageBitmap(bitmap);
                    }
                });
                break;
            case R.id.rl_id: //上传身份证 相册/拍照
                currentCode = REQUEST_CODE_ID;
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "上传身份证"));
                ImageCropActivity.setOnClipCompleteListener(new ImageCropActivity.OnClipCompleteListener() {
                    @Override
                    public void onClipComplete(Bitmap bitmap) {
                        LogUtil.e("onClipComplete", "hahahah");
                        iv_id.setVisibility(View.VISIBLE);
                        iv_id.setImageBitmap(bitmap);
                    }
                });
                break;
            case R.id.iv_clear:
                tv_tag.setText("");
                tv_tag.setVisibility(View.GONE);
                break;
        }
    }

    private View initPopView(int layout, String title) {
        View view = Util.inflateView(this, layout, null);
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
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_take_photo:
                PopupWindowUtil.dismiss();
                getImageFromCamera();
                break;
            case R.id.tv_album:
                PopupWindowUtil.dismiss();
                getImageFromAlbum();
                break;
            case R.id.tv_cancel:
                PopupWindowUtil.dismiss();
                break;
        }
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
        } else {
            Util.makeToast("请确认已经插入SD卡");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = null;
            File file = null;
            switch (requestCode) {
                case REQUEST_CODE_PICK_IMAGE:
                    Uri uri = data.getData();
                    if (uri != null) {
//                        Bitmap bitmap = ImageUtils.decodeUriAsBitmap(uri);
//                        mClipImageLayout.setImageBitmap(bitmap);
                        toCropActivity(uri);
                    } else {
                        Util.makeToast("抱歉，从相册获取图片失败");
                    }
                    break;
                case REQUEST_CODE_CAPTURE_CAMERA:
//                    Bitmap bitmap =ImageUtils.decodeUriAsBitmap(imageUri);
                    if (imageUri != null) {
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                        toCropActivity(imageUri);
                    }
                    break;
                case REQUEST_CODE_CARD: //名片
                    if (data.hasExtra(Bitmap.class.getSimpleName())) {
                        byte[] bytes = data.getByteArrayExtra(Bitmap.class.getSimpleName());
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        iv_card.setImageBitmap(bitmap);
                    }
                    break;
                case REQUEST_CODE_ID: //身份证
                    if (data.hasExtra(Bitmap.class.getSimpleName())) {
                        byte[] bytes = data.getByteArrayExtra(Bitmap.class.getSimpleName());
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        iv_id.setImageBitmap(bitmap);
                    }
                    break;
            }
        }
    }

    private void toCropActivity(Uri uri) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.class.getSimpleName(), uri);
        startActivity(intent);
    }
}
