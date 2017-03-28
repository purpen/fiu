package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.IsEditorBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ImageCrop.ClipZoomImageView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/9/14.
 */
public class QJPictureActivity extends BaseActivity implements View.OnClickListener {
    private String imgStr;//上个界面传递过来的图片路径
    private boolean isFine, isStick, isCheck;//是否精选推荐屏蔽
    private String id;//情景id
    @Bind(R.id.clip_img)
    ClipZoomImageView clipImg;
    private Bitmap bitmap;
    private WaittingDialog dialog;
    private boolean isEditor;
    //popupWindow
    private View popup_view;
    private PopupWindow popupWindow;
    private TextView jingxuanTv;
    private TextView tuijianTv;
    private TextView pingbiTv;


    public QJPictureActivity() {
        super(R.layout.activity_qj_picture);
    }

    @Override
    protected void getIntentData() {
        imgStr = getIntent().getStringExtra("img");
        isFine = getIntent().getBooleanExtra("fine", false);
        isStick = getIntent().getBooleanExtra("stick", false);
        isCheck = getIntent().getBooleanExtra("check", false);
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        clipImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clipImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPop();
                return true;
            }
        });
        ImageLoader.getInstance().displayImage(imgStr, clipImg, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                bitmap = loadedImage;
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        initPopupWindow();
    }

    private void initPopupWindow() {
        popup_view = View.inflate(activity, R.layout.popup_scene_details_more, null);
        jingxuanTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more1);
        tuijianTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_bianji);
        pingbiTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_shoucang);
        TextView saveTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_jubao);
        TextView cancelTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_cancel);
        pingbiTv.setTextColor(getResources().getColor(R.color.black));
        saveTv.setTextColor(getResources().getColor(R.color.black));
        saveTv.setText("保存图片到本地");
        saveTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
        popupWindow = new PopupWindow(popup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity,
                R.color.nothing));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    @Override
    protected void requestNet() {
        if (!LoginInfo.isUserLogin()) {
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        isEditor();
    }

    private void showPop() {
        if (isEditor) {
            jingxuanTv.setVisibility(View.VISIBLE);
            tuijianTv.setVisibility(View.VISIBLE);
            pingbiTv.setVisibility(View.VISIBLE);
            if (isFine) {
                jingxuanTv.setText("取消精选");
            } else {
                jingxuanTv.setText("精选");
            }
            if (isStick) {
                tuijianTv.setText("取消推荐");
            } else {
                tuijianTv.setText("推荐");
            }
            if (isCheck) {
                pingbiTv.setText("取消屏蔽");
            } else {
                pingbiTv.setText("屏蔽");
            }
            jingxuanTv.setOnClickListener(this);
            tuijianTv.setOnClickListener(this);
            pingbiTv.setOnClickListener(this);
        } else {
            jingxuanTv.setVisibility(View.GONE);
            tuijianTv.setVisibility(View.GONE);
            pingbiTv.setVisibility(View.GONE);
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(popup_view, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_scene_detail_more1:
                popupWindow.dismiss();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                setFine();
                break;
            case R.id.popup_scene_detail_more_bianji:
                popupWindow.dismiss();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                setStick();
                break;
            case R.id.popup_scene_detail_shoucang:
                popupWindow.dismiss();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                setCheck();
                break;
            case R.id.popup_scene_detail_more_jubao:
//                AlertDialog.Builder builder = new AlertDialog.Builder(QJPictureActivity.this);
//                builder.setMessage("保存到本地？");
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                popupWindow.dismiss();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                savePicture();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
                break;
            case R.id.popup_scene_detail_more_cancel:
                popupWindow.dismiss();
                break;
        }
    }

    //保存图片到本地
    private void savePicture() {
        if (bitmap == null) {
            ImageLoader.getInstance().displayImage(imgStr, clipImg, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    bitmap = loadedImage;
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
//                    保存失败，本地找不到
                    ImageUtils.saveToFile(MainApplication.systemPhotoPath, true, bitmap);
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            ToastUtils.showError("图片保存失败，请重试");
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showSuccess("保存成功");
                    }
                });
            }
        }.start();

    }

    //查看当前用户是否有编辑权限
    private void isEditor() {
        HashMap<String, String> params = ClientDiscoverAPI.getisEditorRequestParams();
        Call httpHandler  = HttpRequest.post(params,URL.USER_IS_EDITOR, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse<IsEditorBean> isEditorBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<IsEditorBean>>() {});
                if (isEditorBeanHttpResponse.isSuccess()) {
                    isEditor = (isEditorBeanHttpResponse.getData().getIs_editor() == 1);
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    //精选或取消精选
    private void setFine() {
        HashMap<String, String> params = ClientDiscoverAPI.getsetFineRequestParams(id, isFine ? "0" : "1");
        Call httpHandler = HttpRequest.post(params,URL.USER_DO_FINE, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                if(netBean.isSuccess()){
                    isFine = !isFine;
                    ToastUtils.showSuccess(netBean.getMessage());
                }else{
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    //推荐或取消推荐
    private void setStick() {
        HashMap<String, String> params = ClientDiscoverAPI.getsetStickRequestParams(id, isStick ? "0" : "1");
        Call httpHandler = HttpRequest.post(params,URL.USER_DO_STICK, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                if(netBean.isSuccess()){
                    isStick = !isStick;
                    ToastUtils.showSuccess(netBean.getMessage());
                }else{
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    //屏蔽或取消屏蔽
    private void setCheck() {
        HashMap<String, String> params = ClientDiscoverAPI.getsetCheckRequestParams(id, isCheck ? "1" : "0");
        Call httpHandler = HttpRequest.post(params,URL.USER_DO_CHECK, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                if(netBean.isSuccess()){
                    isCheck = !isCheck;
                    ToastUtils.showSuccess(netBean.getMessage());
                }else{
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }
}
