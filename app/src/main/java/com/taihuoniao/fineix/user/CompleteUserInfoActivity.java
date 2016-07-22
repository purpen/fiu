package com.taihuoniao.fineix.user;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.album.ImageLoaderEngine;
import com.taihuoniao.fineix.album.Picker;
import com.taihuoniao.fineix.album.PicturePickerUtils;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.LoginCompleteUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.SegmentedGroup;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.taihuoniao.fineix.view.wheelview.StringWheelAdapter;
import com.taihuoniao.fineix.view.wheelview.WheelView;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/9 14:42
 */
public class CompleteUserInfoActivity extends BaseActivity {
    @Bind(R.id.rl_box)
    RelativeLayout rl_box;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.et_nickname)
    EditText et_nickname;
    @Bind(R.id.et_sign)
    EditText et_sign;
    @Bind(R.id.riv)
    RoundedImageView riv;
    @Bind(R.id.radioGroup)
    SegmentedGroup radioGroup;
    @Bind(R.id.tv_sign)
    TextView tv_sign;
    private String gender = SECRET;
    private static final String SECRET = "0";
    private static final String MALE = "1";
    private static final String FEMALE = "2";
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 101;
    public static final Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
    private Bitmap bitmap;
    private String label;
    private WaittingDialog svProgressHUD;
    private int invisibleHeight;
    public CompleteUserInfoActivity() {
        super(R.layout.activity_complete_user_info);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "完善个人资料");
        custom_head.setHeadGoBackShow(false);
        svProgressHUD = new WaittingDialog(this);
        et_nickname.setText(LoginInfo.getLoginInfo().getNickname());
        rl_box.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rl_box.getWindowVisibleDisplayFrame(r);
                int screenHeight = rl_box.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                LogUtil.e("Keyboard Size", "Size:" + heightDifference);
                if (heightDifference > screenHeight / 3) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(rl_box, "translationY", 0, -heightDifference + invisibleHeight);
                    animator.setDuration(300);
                    animator.start();
                } else {
                    invisibleHeight = heightDifference;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(rl_box, "translationY", 0, heightDifference - invisibleHeight);
                    animator.setDuration(300);
                    animator.start();
                }
            }
        });
    }

    @OnClick({R.id.btn, R.id.riv, R.id.tv_sign})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                submitData();
                break;
            case R.id.riv:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar, "上传头像"));
                break;
            case R.id.tv_sign:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_gender_layout, R.string.select_tag, Arrays.asList(getResources().getStringArray(R.array.user_tags))));
                break;
        }
    }

    private View initPopView(int layout, int resId, List<String> list) {
        View view = Util.inflateView(activity, layout, null);
        View tv_cancel_select = view.findViewById(R.id.tv_cancel_select);
        View tv_confirm_select = view.findViewById(R.id.tv_confirm_select);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        WheelView wv_province = (WheelView) view.findViewById(R.id.custom_wv);
        wv_province.setAdapter(new StringWheelAdapter(list));
        wv_province.setVisibleItems(5);
        tv_title.setText(resId);
        setClickListener(tv_cancel_select, resId, wv_province, list);
        setClickListener(tv_confirm_select, resId, wv_province, list);
        return view;
    }

    private void setClickListener(View view, final int id, final WheelView wheelView, final List<String> list) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_cancel_select:
                        PopupWindowUtil.dismiss();
                        break;
                    case R.id.tv_confirm_select:
                        label = list.get(wheelView.getCurrentItem());
                        tv_sign.setText(label);
                        PopupWindowUtil.dismiss();
                        break;
                    default:
                        PopupWindowUtil.dismiss();
                        break;
                }
            }
        });
    }

    private View initPopView(int layout, String title) {
        View view = Util.inflateView(activity, layout, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        View iv_take_photo = view.findViewById(R.id.tv_take_photo);
        View iv_take_album = view.findViewById(R.id.tv_album);
        View iv_close = view.findViewById(R.id.tv_cancel);
        iv_take_photo.setOnClickListener(onClickListener);
        iv_take_album.setOnClickListener(onClickListener);
        iv_close.setOnClickListener(onClickListener);
        return view;
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_take_photo:
                    PopupWindowUtil.dismiss();
                    getImageFromCamera();
                    break;
                case R.id.tv_album:
                    PopupWindowUtil.dismiss();
                    getImageFromAlbum();
                    break;
                case R.id.tv_cancel:
                default:
                    PopupWindowUtil.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void installListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.male:
                        gender = MALE;
                        break;
                    case R.id.female:
                        gender = FEMALE;
                        break;
                    case R.id.secret:
                        gender = SECRET;
                        break;
                }
            }
        });
    }

    private void submitData() {
        String nickname = et_nickname.getText().toString().trim();
        String sign = et_sign.getText().toString().trim();

        if (TextUtils.isEmpty(nickname)) {
            ToastUtils.showInfo("请填写昵称");
            return;
        }
//        if (TextUtils.isEmpty(sign)) {
//            ToastUtils.showError("请填写个性签名");
//            return;
//        }

        ClientDiscoverAPI.updateNickNameSummary(nickname, sign, gender, label, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                if (!activity.isFinishing() && svProgressHUD != null) svProgressHUD.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!activity.isFinishing() && svProgressHUD != null) svProgressHUD.dismiss();
                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);

                if (response.isSuccess()) {
                    LoginCompleteUtils.goFrom(CompleteUserInfoActivity.this);
                    if (OrderInterestQJActivity.instance != null) {
                        OrderInterestQJActivity.instance.finish();
                    }
                    if (!activity.isFinishing() && svProgressHUD != null)
                        ToastUtils.showSuccess(response.getMessage());
                    finish();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (!activity.isFinishing() && svProgressHUD != null) svProgressHUD.dismiss();
                ToastUtils.showError("网络异常，请确认网络畅通");
            }
        });
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
                    if (imageUri != null) {
                        toCropActivity(imageUri);
                    }
                    break;
            }
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
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
        } else {
            ToastUtils.showError("未检测到SD卡");
        }
    }

    private void toCropActivity(Uri uri) {
        ImageCropActivity.setOnClipCompleteListener(new ImageCropActivity.OnClipCompleteListener() {
            @Override
            public void onClipComplete(Bitmap bitmap) {
                CompleteUserInfoActivity.this.bitmap = bitmap;
                riv.setImageBitmap(bitmap);
            }
        });
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.class.getSimpleName(), uri);
        intent.putExtra(ImageCropActivity.class.getName(), TAG);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (bitmap != null) bitmap.recycle();
        super.onDestroy();
    }
}
