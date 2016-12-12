package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.album.ImageLoaderEngine;
import com.taihuoniao.fineix.album.Picker;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.AuthData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.labelview.AutoLabelUI;
import com.taihuoniao.fineix.view.labelview.Label;

import java.io.File;
import java.util.List;

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
    @Bind(R.id.et_info)
    EditText et_info;
    @Bind(R.id.et_contacts)
    EditText et_contacts;
    @Bind(R.id.rl_card)
    RelativeLayout rl_card;
    @Bind(R.id.rl_id)
    RelativeLayout rl_id;
    private Bitmap bitmap_id;
    private Bitmap bitmap_card;
    private WaittingDialog dialog;
    @Bind(R.id.iv_clear)
    ImageButton iv_clear;
    @Bind(R.id.btn)
    Button btn;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 101;
    public static final Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
    private AuthData authData;
    private CertificateStatusActivity certificateStatusActivity;
    public OfficialCertificateActivity() {
        super(R.layout.activity_official_certificate);
    }

    @Override
    protected void getIntentData() {
        certificateStatusActivity = CertificateStatusActivity.instance;
        Intent intent = getIntent();
        if (intent.hasExtra(AuthData.class.getSimpleName())){
            authData=(AuthData) intent.getSerializableExtra(AuthData.class.getSimpleName());
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"官方认证");
        dialog=new WaittingDialog(this);
        WaittingDialog svProgressHUD = new WaittingDialog(this);
        String[] stringArray = getResources().getStringArray(R.array.official_tags);
        for (String aStringArray : stringArray) {
            label_view.addLabel(aStringArray);
        }
        if (authData!=null){
            tv_tag.setVisibility(View.VISIBLE);
            tv_tag.setText(authData.label);
            iv_clear.setVisibility(View.VISIBLE);
            et_info.setText(authData.info);
            et_contacts.setText(authData.contact);
            iv_id.setVisibility(View.VISIBLE);
            iv_card.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(authData.id_card_cover_url,iv_id,options);
            ImageLoader.getInstance().displayImage(authData.business_card_cover_url,iv_card,options);
        }
        WindowUtils.chenjin(this);
    }

    @Override
    protected void installListener() {
        label_view.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(Label labelClicked) {
                tv_tag.setVisibility(View.VISIBLE);
                tv_tag.setText(labelClicked.getText());
                iv_clear.setVisibility(View.VISIBLE);
            }
        });

    }

    @OnClick({R.id.btn, R.id.iv_clear, R.id.rl_id, R.id.rl_card})
    void performClick(View view) {
        switch (view.getId()) {
            case R.id.btn: //提交
                submitData();
                break;
            case R.id.rl_card: //上传名片 相册/拍照
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "上传名片"));
                ImageCropActivity.setOnClipCompleteListener(new ImageCropActivity.OnClipCompleteListener() {
                    @Override
                    public void onClipComplete(Bitmap bitmap) {
                        bitmap_card = bitmap;
                        iv_card.setVisibility(View.VISIBLE);
                        iv_card.setImageBitmap(bitmap_card);
                    }
                });
                break;
            case R.id.rl_id: //上传身份证 相册/拍照
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "上传身份证"));
                ImageCropActivity.setOnClipCompleteListener(new ImageCropActivity.OnClipCompleteListener() {
                    @Override
                    public void onClipComplete(Bitmap bitmap) {
                        bitmap_id = bitmap;
                        iv_id.setVisibility(View.VISIBLE);
                        iv_id.setImageBitmap(bitmap_id);
                    }
                });
                break;
            case R.id.iv_clear:
                tv_tag.setText("");
                tv_tag.setVisibility(View.GONE);
                iv_clear.setVisibility(View.GONE);
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

    private void submitData() {
        String label = tv_tag.getText().toString().trim();
        if (TextUtils.isEmpty(label)) {
            ToastUtils.showError("请选择认证身份");
            return;
        }

        String info = et_info.getText().toString().trim();
        if (TextUtils.isEmpty(info)) {
            ToastUtils.showError("请填写认证信息");
            return;
        }

        String contacts = et_contacts.getText().toString().trim();
        if (TextUtils.isEmpty(contacts)) {
            ToastUtils.showError("请填写联系方式");
            return;
        }
        if (authData==null){//如果是首次认证身份证和名片必传
            if (bitmap_id == null) {
                ToastUtils.showError("请先选择身份证");
                return;
            }
            if (bitmap_card == null) {
                ToastUtils.showError("请先选择名片");
                return;
            }
        }
        String id=null;
        if (authData!=null){ //非首次上传_id有值
            id=authData._id;
        }

        setViewsEnable(false);

        ClientDiscoverAPI.uploadIdentityInfo(id,info, label, contacts, Util.saveBitmap2Base64Str(bitmap_id), Util.saveBitmap2Base64Str(bitmap_card), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!activity.isFinishing()&&dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (bitmap_id != null) bitmap_id.recycle();
                if (bitmap_card != null) bitmap_card.recycle();
                setViewsEnable(true);
                if (!activity.isFinishing()&&dialog != null) dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    ToastUtils.showSuccess("认证信息提交成功");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (certificateStatusActivity != null) {
                                certificateStatusActivity.finish();
                            }
                            finish();
                        }
                    },1000);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                setViewsEnable(true);
                if (!activity.isFinishing()&&dialog != null) dialog.dismiss();
                ToastUtils.showError("网络异常，请确保网络畅通");
            }
        });

    }

    private void setViewsEnable(boolean enable) {
        label_view.setEnabled(enable);
        et_info.setEnabled(enable);
        et_contacts.setEnabled(enable);
        rl_card.setEnabled(enable);
        rl_id.setEnabled(enable);
        btn.setEnabled(enable);
    }

    @Override
    public void onClick(View view) {
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
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");//相片类型
//        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        Picker.from(this)
                .count(1)
                .enableCamera(false)
                .singleChoice()
                .setEngine(new ImageLoaderEngine())
                .forResult(REQUEST_CODE_PICK_IMAGE);
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
            switch (requestCode) {
                case REQUEST_CODE_PICK_IMAGE:
//                    Uri uri = data.getData();
//                    if (uri != null) {
//                        toCropActivity(uri);
//                    } else {
//                        Util.makeToast("抱歉，从相册获取图片失败");
//                    }
                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                    if (mSelected ==null) return;
                    if (mSelected.size()==0) return;
                    toCropActivity(mSelected.get(0));
                    break;
                case REQUEST_CODE_CAPTURE_CAMERA:
                    if (imageUri != null) {
                        toCropActivity(imageUri);
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

    @Override
    protected void onDestroy() {
        certificateStatusActivity = null;
        if (bitmap_id != null) bitmap_id.recycle();
        if (bitmap_card != null) bitmap_card.recycle();
        super.onDestroy();
    }
}
