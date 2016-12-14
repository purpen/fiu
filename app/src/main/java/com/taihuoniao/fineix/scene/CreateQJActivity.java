package com.taihuoniao.fineix.scene;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CreateQJBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.map.MapSearchAddressActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.Base64Utils;
import com.taihuoniao.fineix.utils.EffectUtil;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListener;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/15.
 * 创建情景/编辑情景
 */
public class CreateQJActivity extends BaseActivity implements View.OnClickListener {
    //编辑情境时传过来的情境详情
    private SceneList.DataBean.RowsBean qjBean;
    private String qjId;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.background_img)
    ImageView backgroundImg;
    @Bind(R.id.qj_title_tv)
    TextView qjTitleTv;
    @Bind(R.id.qj_title_tv2)
    TextView qjTitleTv2;
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.des_tv)
    EditText desTv;
    @Bind(R.id.location_relative)
    RelativeLayout locationRelative;
    @Bind(R.id.delete_address)
    ImageView deleteAddressImg;
    @Bind(R.id.location_tv)
    EditText locationTv;
    @Bind(R.id.city_tv)
    TextView cityTv;
    private WaittingDialog dialog;
    private float pointWidth;//LabelView动画点的宽高
    private float labelMargin;//LabelView动画点左间距
    private StringBuilder tags;//用来存储标签

    public CreateQJActivity() {
        super(0);
    }

    @Override
    protected void setContenttView() {
        View activityView = View.inflate(this, R.layout.activity_create_qj, null);
        setContentView(activityView);
    }

    @Override
    protected void initView() {
        titleLayout.setBackgroundResource(R.color.title_black);
        titleLayout.setTitle(R.string.create_qingjing, getResources().getColor(R.color.white));
        titleLayout.setRightTv(R.string.publish, getResources().getColor(R.color.yellow_bd8913), this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) container.getLayoutParams();
        layoutParams.width = MainApplication.getContext().getScreenWidth();
        layoutParams.height = layoutParams.width;
        container.setLayoutParams(layoutParams);
        pointWidth = getResources().getDimension(R.dimen.label_point_width);
        labelMargin = getResources().getDimension(R.dimen.label_point_margin);
        dialog = new WaittingDialog(this);
        tags = new StringBuilder();
    }

    private boolean isDown;

    @Override
    protected void initList() {
        locationTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteAddressImg.setVisibility(View.VISIBLE);
                } else {
                    deleteAddressImg.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        qjTitleTv.setOnClickListener(this);
        qjTitleTv2.setOnClickListener(this);
        desTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isDown = true;
                        Log.e("<<<", "down,x=" + event.getX() + ",y=" + event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isDown && event.getX() >= 0 && event.getY() >= 0 && event.getX() <= desTv.getMeasuredWidth()
                                && event.getY() <= desTv.getMeasuredHeight()) {
                            onClick(desTv);
                            isDown = false;
                        }
                        Log.e("<<<", "up,x=" + event.getX() + ",y=" + event.getY() + ",height=" + desTv.getMeasuredHeight()
                                + ",width=" + desTv.getMeasuredWidth());
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.e("<<<", "cancel");
                        break;
                }
                return false;
            }
        });
        locationRelative.setOnClickListener(this);
        deleteAddressImg.setOnClickListener(this);
        if (getIntent().hasExtra(PictureEditActivity.class.getSimpleName())) {
            //设置背景图片
            backgroundImg.setImageBitmap(MainApplication.editBitmap);
            //添加标签
            if (MainApplication.tagInfoList == null) {
                return;
            }
            for (final TagItem tagItem : MainApplication.tagInfoList) {
                final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                final LabelView labelView = new LabelView(this);
                labelView.init(tagItem);
                labelView.setLayoutParams(layoutParams);
                if (tagItem.getLoc() == 2) {
                    labelView.nameTv.setBackgroundResource(R.drawable.label_left);
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                    layoutParams1.leftMargin = (int) labelMargin;
                    labelView.pointContainer.setLayoutParams(layoutParams1);
                }
                labelView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tagItem.getLoc() == 2) {
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                            lp.leftMargin = (int) (tagItem.getX() * MainApplication.getContext().getScreenWidth() - labelMargin - pointWidth / 2);
                            lp.topMargin = (int) (tagItem.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + pointWidth / 2);
                            Log.e("<<<", "getX=" + tagItem.getX() + ",screenWidth=" + MainApplication.getContext().getScreenWidth() +
                                    ",labelMargin=" + labelMargin + ",pointWidth/2=" + pointWidth / 2 + ",leftMargin=" + lp.leftMargin);
                            Log.e("<<<", "getY=" + tagItem.getY() + ",labelView.height=" + labelView.getMeasuredHeight() + ",topMargin=" + lp.topMargin);
                            labelView.setLayoutParams(lp);
                        } else {
                            labelView.nameTv.setBackgroundResource(R.drawable.label_right);
                            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                            layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - pointWidth - labelMargin);
                            labelView.pointContainer.setLayoutParams(layoutParams1);
                            Log.e("<<<", "nameTv.width=" + labelView.nameTv.getMeasuredWidth() + ",pointWidth=" + pointWidth + ",labelMargin=" + labelMargin + ",point.leftMargin=" + layoutParams1.leftMargin);
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                            lp.leftMargin = (int) (tagItem.getX() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredWidth() + labelMargin + pointWidth / 2);
                            lp.topMargin = (int) (tagItem.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + pointWidth / 2);
                            Log.e("<<<", "getX=" + tagItem.getX() + ",screenWidth=" + MainApplication.getContext().getScreenWidth() + ",labelWidth=" + labelView.getMeasuredWidth() +
                                    ",labelMargin=" + labelMargin + ",pointWidth/2=" + pointWidth / 2 + ",leftMargin=" + lp.leftMargin);
                            Log.e("<<<", "getY=" + tagItem.getY() + ",labelHeight=" + labelView.getMeasuredHeight() + ",topMargin=" + lp.topMargin);
                            labelView.setLayoutParams(lp);
                        }
                    }
                });
                container.addView(labelView);
                labelView.wave();
            }
        } else if (getIntent().hasExtra(IndexFragment.class.getSimpleName())) {
            titleLayout.setCancelImgVisible(true);
            qjBean = (SceneList.DataBean.RowsBean) getIntent().getSerializableExtra(IndexFragment.class.getSimpleName());
            qjId = qjBean.get_id();
            titleLayout.setTitle(R.string.bianji_qingjing);
            titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.yellow_bd8913), this);
            ImageLoader.getInstance().loadImage(qjBean.getCover_url(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    MainApplication.editBitmap = bitmap;
                    backgroundImg.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
            //添加商品标签
            for (final SceneList.DataBean.RowsBean.ProductBean productBean : qjBean.getProduct()) {
                final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                final LabelView labelView = new LabelView(activity);
                labelView.nameTv.setText(productBean.getTitle());
                labelView.setLayoutParams(layoutParams);
                if (productBean.getLoc() == 2) {
                    labelView.nameTv.setBackgroundResource(R.drawable.label_left);
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                    layoutParams1.leftMargin = (int) labelView.labelMargin;
                    labelView.pointContainer.setLayoutParams(layoutParams1);
                }
                labelView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (productBean.getLoc() == 2) {
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                            lp.leftMargin = (int) (productBean.getX() * MainApplication.getContext().getScreenWidth() - labelView.labelMargin - labelView.pointWidth / 2);
                            lp.topMargin = (int) (productBean.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                            labelView.setLayoutParams(lp);
                        } else {
                            labelView.nameTv.setBackgroundResource(R.drawable.label_right);
                            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                            layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - labelView.pointWidth - labelView.labelMargin);
                            labelView.pointContainer.setLayoutParams(layoutParams1);
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                            lp.leftMargin = (int) (productBean.getX() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredWidth() + labelView.labelMargin + labelView.pointWidth / 2);
                            lp.topMargin = (int) (productBean.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                            labelView.setLayoutParams(lp);
                        }
                    }
                });
                container.addView(labelView);
                labelView.wave();
            }
            titleIntent = qjBean.getTitle();
            SceneTitleSetUtils.setTitle(qjTitleTv, qjTitleTv2, titleIntent);
            desIntent = qjBean.getDes();
            tags = new StringBuilder();
            int sta = 0;
            SpannableString spannableStringBuilder = new SpannableString(desIntent);
            while (desIntent.substring(sta).contains("#")) {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow_bd8913));
                sta = desIntent.indexOf("#", sta);
                if (desIntent.substring(sta).contains(" ")) {
                    int en = desIntent.indexOf(" ", sta);
                    tags.append(",").append(desIntent.substring(sta + 1, en));
                    spannableStringBuilder.setSpan(foregroundColorSpan, sta, en, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sta = en;
                } else {
                    tags.append(",").append(desIntent.substring(sta + 1));
                    spannableStringBuilder.setSpan(foregroundColorSpan, sta, desIntent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
            if (tags.length() > 0) {
                tags.deleteCharAt(0);
            }
            desTv.setText(spannableStringBuilder);
            locationTv.setText(qjBean.getAddress());
            city = qjBean.getCity();
            if (TextUtils.isEmpty(city)) {
                cityTv.setVisibility(View.GONE);
            } else {
                cityTv.setText(qjBean.getCity());
                cityTv.setVisibility(View.VISIBLE);
            }
            try {
                lat = qjBean.getLocation().getCoordinates().get(1) + "";
                lng = qjBean.getLocation().getCoordinates().get(0) + "";
            } catch (Exception e) {
                lat = null;
                lng = null;
            }
        }
    }


    @Override
    protected void requestNet() {
    }

    private Thread thread;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_address:
                locationTv.setText("");
                cityTv.setText("");
                cityTv.setVisibility(View.GONE);
                city = null;
                lat = null;
                lng = null;
                break;
            case R.id.location_relative:
                Intent intent = new Intent(this, MapSearchAddressActivity.class);
                startActivityForResult(intent, DataConstants.REQUESTCODE_CREATESCENE_BDSEARCH);
                break;
            case R.id.des_tv:
            case R.id.qj_title_tv:
            case R.id.qj_title_tv2:
                Log.e("<<<", "点击desTv");
                Intent intent1 = new Intent(CreateQJActivity.this, AddEnvirActivity.class);
                if (qjTitleTv2.getVisibility() == View.VISIBLE) {
                    intent1.putExtra("title", qjTitleTv2.getText().toString() + qjTitleTv.getText().toString());
                } else {
                    intent1.putExtra("title", qjTitleTv.getText().toString());
                }
                intent1.putExtra("des", desTv.getText().toString());
                startActivityForResult(intent1, 1);
                overridePendingTransition(R.anim.bottom_to_up, R.anim.abc_fade_out);
                break;
            case R.id.title_continue:
                long now = System.currentTimeMillis();
                if (now - lastClick < 1000) {
                    lastClick = now;
                    return;
                }
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(this, OptRegisterLoginActivity.class));
                    return;
                }
                Log.e("<<<", "已登录");
                if (MainApplication.editBitmap == null) {
                    ToastUtils.showError("图片信息错误，请返回重试");
                    return;
                }
                Log.e("<<<", "图片不为空");
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("<<<", "显示对话框");
                        String title = titleIntent;
                        Log.e("<<<", "初始化名称");
                        String des = desIntent;
                        Log.e("<<<", "初始化描述");
                        StringBuilder products = new StringBuilder();
                        if (MainApplication.tagInfoList != null && MainApplication.tagInfoList.size() > 0) {
                            products.append("[");
                            for (int i = 0; i < MainApplication.tagInfoList.size(); i++) {
                                products.append(MainApplication.tagInfoList.get(i).toString());
                                if (i != MainApplication.tagInfoList.size() - 1) {
                                    products.append(",");
                                }
                            }
                            products.append("]");
                        }
                        Log.e("<<<", "初始化产品信息");
                        String address = null;
                        if (!TextUtils.isEmpty(locationTv.getText())) {
                            address = locationTv.getText().toString();
                        }
                        Log.e("<<<", "初始化地址");
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        ToastUtils.showError("图片过大");
                                    }
                                });
                                return;
                            }
                            MainApplication.editBitmap.compress(Bitmap.CompressFormat.JPEG, sapleSize, stream);
                            Log.e("<<<", "压缩" + sapleSize + ",大小=" + stream.size());
                        } while (stream.size() > MainApplication.MAXPIC);//最大上传图片不得超过512K
                        Log.e("<<<", "压缩图片");
                        String tmp = Base64Utils.encodeLines(stream.toByteArray());
                        Log.e("<<<", "初始化活动标签");
                        createQJ(qjId, title, des, null, tags.length() > 0 ? tags.toString() : null, products.toString(), address, city, tmp, lat, lng, MainApplication.subjectId);
                    }
                });
                thread.start();
                break;
        }
    }

    private void blurActivity() {
        try {
            MainApplication.blurBitmap = blur(myShot(CreateQJActivity.this), 25f);
        } catch (Exception e) {
            MainApplication.blurBitmap = myShot(CreateQJActivity.this);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            blurActivity();
        }
    }

    private long lastClick = 0L;//上次点击的时间。避免多次点击重复上传

    private void createQJ(String id, String title, String des, final String scene_id, String tags,
                          String products, String address, String city, String tmp, String lat, String lng,
                          String subject_ids) {
        Log.e("<<<", "开始上传");
        HttpHandler<String> httpHandler = ClientDiscoverAPI.createScene(id, title, des, scene_id, tags, products, address, city,
                tmp, lat, lng, subject_ids, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(final ResponseInfo<String> responseInfo) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                CreateQJBean createQJBean = new CreateQJBean();
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<CreateQJBean>() {
                                    }.getType();
                                    createQJBean = gson.fromJson(responseInfo.result, type);
                                } catch (JsonSyntaxException e) {
                                    Log.e("<<<", "解析异常");
                                }
                                if (createQJBean.isSuccess()) {
                                    MainApplication.cropBitmap = null;
                                    MainApplication.editBitmap = null;
                                    MainApplication.blurBitmap = null;
                                    MainApplication.subjectId = null;
                                    EffectUtil.clear();
                                    if (qjBean == null) {
                                        ToastUtils.showSuccess("创建成功");
                                        Intent intent1 = new Intent(DataConstants.BroadFind);
                                        intent1.putExtra("id", createQJBean.getData().getId());
                                        sendBroadcast(intent1);
                                        Intent intent = new Intent(CreateQJActivity.this, MainActivity.class);
                                        intent.putExtra(FindFragment.class.getSimpleName(), false);
                                        startActivity(intent);
                                    } else {
                                        ToastUtils.showSuccess("修改成功");
//                                        Intent intent= new Intent(DataConstants.BroadRefreshQJ);
//                                        sendBroadcast(intent);
                                        onBackPressed();
                                    }
                                } else {
                                    ToastUtils.showError(createQJBean.getMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(final HttpException error, final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Log.e("<<<失败", error.toString() + "," + msg);
                                ToastUtils.showError(R.string.net_fail);
                            }
                        });
                    }
                });
        addNet(httpHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if (view instanceof LabelView) {
                LabelView labelView = (LabelView) view;
                labelView.stopAnim();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if (view instanceof LabelView) {
                LabelView labelView = (LabelView) view;
                labelView.wave();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra(PictureEditActivity.class.getSimpleName())) {
            new DefaultDialog(CreateQJActivity.this, App.getString(R.string.hint_dialog_goBack_title), App.getStringArray(R.array.text_dialog_button3), new IDialogListener() {
                @Override
                public void click(View view, int index) {
                    if (index == 0) {
                        dialog.dismiss();
                        EffectUtil.clear();
                        MainApplication.blurBitmap = null;
                        MainApplication.cropBitmap = null;
                        MainApplication.editBitmap = null;
                        MainApplication.subjectId = null;
                        startActivity(new Intent(CreateQJActivity.this, MainActivity.class));
                    } else if (index == 1) {
                        CreateQJActivity.this.finish();
                    }
                }
            });
//            AlertDialog.Builder builder = new AlertDialog.Builder(CreateQJActivity.this);
//            builder.setMessage("返回上一步？");
//            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    CreateQJActivity.this.finish();
//                }
//            });
//            builder.setNegativeButton("取消创建", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    EffectUtil.clear();
//                    MainApplication.blurBitmap = null;
//                    MainApplication.cropBitmap = null;
//                    MainApplication.editBitmap = null;
//                    MainApplication.subjectId = null;
//                    startActivity(new Intent(CreateQJActivity.this, MainActivity.class));
//                }
//            });
//            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (thread != null && thread.isAlive()) {
            thread.stop();
        }
        super.onDestroy();
    }

    private String titleIntent;
    private String desIntent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case 2:
                    titleIntent = data.getStringExtra("title");
                    desIntent = data.getStringExtra("des");
                    if (titleIntent != null) {
                        SceneTitleSetUtils.setTitle(qjTitleTv, qjTitleTv2, titleIntent);
                    }
                    if (desIntent != null) {
                        tags = new StringBuilder();
                        int sta = 0;
                        SpannableString spannableStringBuilder = new SpannableString(desIntent);
                        while (desIntent.substring(sta).contains("#")) {
                            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow_bd8913));
                            sta = desIntent.indexOf("#", sta);
                            if (desIntent.substring(sta).contains(" ")) {
                                int en = desIntent.indexOf(" ", sta);
                                tags.append(",").append(desIntent.substring(sta + 1, en));
                                spannableStringBuilder.setSpan(foregroundColorSpan, sta, en, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                sta = en;
                            } else {
                                tags.append(",").append(desIntent.substring(sta + 1));
                                spannableStringBuilder.setSpan(foregroundColorSpan, sta, desIntent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                break;
                            }
                        }
                        if (tags.length() > 0) {
                            tags.deleteCharAt(0);
                        }
                        desTv.setText(spannableStringBuilder);
                    }
                    blurActivity();
                    break;
                case DataConstants.RESULTCODE_CREATESCENE_BDSEARCH:
                    PoiInfo poiInfo = data.getParcelableExtra(PoiInfo.class.getSimpleName());
                    city = data.getStringExtra("city");
                    if (poiInfo != null) {
                        cityTv.setText(city);
                        cityTv.setVisibility(View.VISIBLE);
                        lng = poiInfo.location.longitude + "";
                        lat = poiInfo.location.latitude + "";
                        locationTv.setText(poiInfo.name);
                    }
                    break;
            }
        }
    }

    private String lat, lng;
    private String city;


    public Bitmap myShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();
        // 获取屏幕宽和高
        int widths = MainApplication.getContext().getScreenWidth();
        int heights = MainApplication.getContext().getScreenHeight();
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                0, widths, heights);
        // 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap blur(Bitmap bkg, float radius) throws Exception {
        Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, 0, 0, null);
        RenderScript rs = RenderScript.create(this);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur;
        blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        rs.destroy();
        return overlay;
    }
}
