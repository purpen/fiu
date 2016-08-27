package com.taihuoniao.fineix.scene;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.SearchEnvirAdapter;
import com.taihuoniao.fineix.adapters.ShareCJSelectListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.ActiveTagsBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.CreateQJBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.blurview.BlurView;
import com.taihuoniao.fineix.blurview.RenderScriptBlur;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.map.MapSearchAddressActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.Base64Utils;
import com.taihuoniao.fineix.utils.EffectUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/15.
 * 创建情景/编辑情景
 */
public class CreateQJActivity extends BaseActivity implements View.OnClickListener, EditRecyclerAdapter.ItemClick, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.background_img)
    ImageView backgroundImg;
    @Bind(R.id.qj_title_tv)
    EditText qjTitleTv;
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
    private View activityView;
    //选择语境popupwindow
    private PopupWindow selectEnvirPop;
    private StringBuilder tags;//用来存储标签

    public CreateQJActivity() {
        super(0);
    }

    @Override
    protected void setContenttView() {
        activityView = View.inflate(this, R.layout.activity_create_qj, null);
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
        initSelectEnvirPop();
    }

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
        desTv.setOnClickListener(this);
        locationRelative.setOnClickListener(this);
        deleteAddressImg.setOnClickListener(this);
        if (getIntent().hasExtra(PictureEditActivity.class.getSimpleName())) {
            //设置背景图片
            backgroundImg.setImageBitmap(MainApplication.editBitmap);
            //添加标签
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
        }
//        else if (getIntent().hasExtra(IndexFragment.class.getSimpleName())) {
//            titleLayout.setBackImgVisible(false);
//            titleLayout.setCancelImg(R.mipmap.cancel_white);
//            titleLayout.setTitle(R.string.bianji_qingjing, getResources().getColor(R.color.white));
//            titleLayout.setBackImgVisible(true);
//            sceneBean = (SceneList.DataBean.RowsBean) getIntent().getSerializableExtra(IndexFragment.class.getSimpleName());
//            id = sceneBean.get_id();
//            //设置背景图片
//            ImageLoader.getInstance().displayImage(sceneBean.getCover_url(), backgroundImg, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    MainApplication.editBitmap = loadedImage;
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//
//                }
//            });
//            //添加标签
//            for (final SceneList.DataBean.RowsBean.ProductBean productBean : sceneBean.getProduct()) {
//                final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                final LabelView labelView = new LabelView(this);
//                final TagItem tagItem = new TagItem();
//                tagItem.setId(productBean.getId());
//                tagItem.setName(productBean.getTitle());
//                tagItem.setX(productBean.getX());
//                tagItem.setY(productBean.getY());
//                tagItem.setLoc(productBean.getLoc());
//                labelView.init(tagItem);
//                labelView.setLayoutParams(layoutParams);
//                if (tagItem.getLoc() == 2) {
//                    labelView.nameTv.setBackgroundResource(R.drawable.label_left);
//                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointRelative.getLayoutParams();
//                    layoutParams1.leftMargin = (int) labelMargin;
//                    labelView.pointRelative.setLayoutParams(layoutParams1);
//                }
//                labelView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (tagItem.getLoc() == 2) {
//                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
//                            lp.leftMargin = (int) (tagItem.getX() * MainApplication.getContext().getScreenWidth() - labelMargin - pointWidth / 2);
//                            lp.topMargin = (int) (tagItem.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + pointWidth / 2);
//                            labelView.setLayoutParams(lp);
//                        } else {
//                            labelView.nameTv.setBackgroundResource(R.drawable.label_right);
//                            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointRelative.getLayoutParams();
//                            layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - pointWidth - labelMargin);
//                            labelView.pointRelative.setLayoutParams(layoutParams1);
//                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
//                            lp.leftMargin = (int) (tagItem.getX() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredWidth() + labelMargin + pointWidth / 2);
//                            lp.topMargin = (int) (tagItem.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + pointWidth / 2);
//                            labelView.setLayoutParams(lp);
//                        }
//                    }
//                });
//                container.addView(labelView);
//                labelView.wave();
//            }
//            //设置标题及描述
//            tags = new StringBuilder();
//            qjTitleTv.setText(sceneBean.getTitle());
//            holder.title.setText(sceneBean.getTitle());
//            qjTitleTv.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (qjTitleTv.getLineCount() >= 2) {
//                        Layout layout = qjTitleTv.getLayout();
//                        StringBuilder SrcStr = new StringBuilder(qjTitleTv.getText().toString());
//                        String str0 = SrcStr.subSequence(layout.getLineStart(0), layout.getLineEnd(0)).toString();
//                        String str1 = SrcStr.subSequence(layout.getLineStart(1), layout.getLineEnd(1)).toString();
//                        qjTitleTv2.setText(str0);
//                        qjTitleTv.setText(str1);
//                        qjTitleTv2.setVisibility(View.VISIBLE);
//                    } else {
//                        qjTitleTv2.setVisibility(View.GONE);
//                    }
//                }
//            });
//            holder.des.setText(sceneBean.getDes());
//            int sta = 0;
//            SpannableString spannableStringBuilder = new SpannableString(sceneBean.getDes());
//            while (sceneBean.getDes().substring(sta).contains("#")) {
//                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow_bd8913));
//                sta = sceneBean.getDes().indexOf("#", sta);
//                if (sceneBean.getDes().substring(sta).contains(" ")) {
//                    int en = sceneBean.getDes().indexOf(" ", sta);
//                    tags.append(",").append(sceneBean.getDes().substring(sta + 1, en));
//                    spannableStringBuilder.setSpan(foregroundColorSpan, sta, en, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    sta = en;
//                } else {
//                    tags.append(",").append(sceneBean.getDes().substring(sta + 1));
//                    spannableStringBuilder.setSpan(foregroundColorSpan, sta, sceneBean.getDes().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    break;
//                }
//            }
//            if (tags.length() > 0) {
//                tags.deleteCharAt(0);
//            }
//            desTv.setText(spannableStringBuilder);
//            //设置地理位置
//            city = sceneBean.getCity();
//            cityTv.setText(city);
//            cityTv.setVisibility(View.VISIBLE);
//            lng = sceneBean.getLocation().getCoordinates().get(0) + "";
//            lat = sceneBean.getLocation().getCoordinates().get(1) + "";
//            locationTv.setText(sceneBean.getAddress());
//        }

    }

    private ActiveTagsBean activeTagsBean;//当前活动javabean

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.activeTags(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<活动标签", responseInfo.result);
//                WriteJsonToSD.writeToSD("json",responseInfo.result);
//                activeTagsBean = new ActiveTagsBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ActiveTagsBean>() {
                    }.getType();
                    activeTagsBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常" + e.toString());
                }
                if (activeTagsBean.isSuccess()) {
                    try {
                        holder.goneDemoLabel.setText("#" + activeTagsBean.getData().getItems().get(0).get(0) + " ");
                    } catch (Exception e) {
                        Log.e("<<<", "没有活动");
                    }

                } else {
                    holder.goneDemoLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                holder.goneDemoLabel.setVisibility(View.GONE);
            }
        });
    }

    private Thread thread;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gone_label:
                if (holder.des.hasFocus()) {
                    holder.des.getText().insert(holder.des.getSelectionStart(), holder.goneLabel.getText());
                }
                break;
            case R.id.gone_demo_label:
                if (holder.des.hasFocus()) {
                    holder.des.getText().insert(holder.des.getSelectionStart(), holder.goneDemoLabel.getText());
                }
                break;
            case R.id.delete_title:
                holder.title.setText("");
                break;
            case R.id.delete_des:
                holder.des.setText("");
                break;
            case R.id.search_delete:
                holder.editText.setText("");
                break;

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
                showSelectEnvirPop();
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
                        String title = null;
                        if (!TextUtils.isEmpty(holder.title.getText().toString())) {
                            title = holder.title.getText().toString();
                        }
                        Log.e("<<<", "初始化名称");
                        String des = null;
                        if (!TextUtils.isEmpty(holder.des.getText().toString())) {
                            des = holder.des.getText().toString();
                        }
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
                        String sids = null;
                        StringBuilder sub_ids = new StringBuilder();
                        if (des != null && activeTagsBean != null && activeTagsBean.getData() != null && activeTagsBean.getData().getItems() != null) {
                            for (int i = 0; i < activeTagsBean.getData().getItems().size(); i++) {
                                if (des.contains("#" + activeTagsBean.getData().getItems().get(i).get(0) + " ")) {
                                    sub_ids.append(",").append(activeTagsBean.getData().getItems().get(i).get(1));
                                }
                            }
                            if (sub_ids.length() > 0) {
                                sub_ids.deleteCharAt(0);
                                sids = sub_ids.toString();
                            }
                        }
                        Log.e("<<<", "初始化活动标签");
                        createQJ(null, title, des, null, tags.length() > 0 ? tags.toString() : null, products.toString(), address, city, tmp, lat, lng, sids);
                    }
                });
                thread.start();
                break;
        }
    }

    private long lastClick = 0L;//上次点击的时间。避免多次点击重复上传

    private void createQJ(String id, String title, String des, final String scene_id, String tags,
                          String products, String address, String city, String tmp, String lat, String lng,
                          String subject_ids) {
        Log.e("<<<", "开始上传");
        ClientDiscoverAPI.createScene(id, title, des, scene_id, tags, products, address, city,
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
                                    EffectUtil.clear();
                                    ToastUtils.showSuccess("创建成功");
                                    sendBroadcast(new Intent(DataConstants.BroadFind));
                                    Intent intent = new Intent(CreateQJActivity.this, MainActivity.class);
                                    intent.putExtra(FindFragment.class.getSimpleName(), false);
                                    startActivity(intent);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateQJActivity.this);
            builder.setMessage("返回上一步？");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CreateQJActivity.this.finish();
                }
            });
            builder.setNegativeButton("取消创建", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    EffectUtil.clear();
                    startActivity(new Intent(CreateQJActivity.this, MainActivity.class));
                }
            });
            builder.create().show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case 1:
                    String str = data.getStringExtra(AddLabelActivity.class.getSimpleName());
                    holder.des.getText().insert(holder.des.getSelectionStart(), str);
                    break;
                case DataConstants.RESULTCODE_CREATESCENE_BDSEARCH:
                    PoiInfo poiInfo = data.getParcelableExtra(PoiInfo.class.getSimpleName());
                    city = data.getStringExtra("city");
                    if (poiInfo != null) {
//                        locationTv.setText(poiInfo.name);
//                        deleteAddress.setVisibility(View.VISIBLE);
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

    private List<SearchBean.Data.SearchItem> list = new ArrayList<>();
    private ShareCJSelectListAdapter shareCJSelectListAdapter;
    private int page = 1;//搜索页码
    private String searchStr;
    private String cid;
    private ViewHolder holder;
    //语境分类
    private List<CategoryListBean.CategoryListItem> envirList;
    private SearchEnvirAdapter searchEnvirAdapter;
    private int lastTotalItem = -1;
    private int lastSavedFirstVisibleItem = -1;

    private void setupBlurView() {
        final float radius = 16f;

        final View decorView = getWindow().getDecorView();
        //Activity's root View. Can also be root View of your layout
        final View rootView = decorView.findViewById(android.R.id.content);
        //set background, if your root layout doesn't have one
        final Drawable windowBackground = decorView.getBackground();
        holder.blurView.setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(this, true)) //Preferable algorithm, needs RenderScript support mode enabled
                .blurRadius(radius);
    }

    private void initSelectEnvirPop() {
        View view = View.inflate(this, R.layout.activity_share_search, null);
        holder = new ViewHolder(view);
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("<<<pop改变布局", "left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom + ",oldLeft=" + oldLeft + ",oldTop=" + oldTop
                        + ",oldRight=" + oldRight + ",oldBottom=" + oldBottom);
                if (bottom < oldBottom - MainApplication.getContext().getScreenHeight() / 4) {
                    //显示软键盘
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            holder.goneLinear.setVisibility(View.VISIBLE);
                        }
                    });

                } else if (oldBottom < bottom - MainApplication.getContext().getScreenHeight() / 4) {
                    //软键盘消失
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            holder.goneLinear.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });
        setupBlurView();
        holder.goneLabel.setOnClickListener(this);
        holder.goneDemoLabel.setOnClickListener(this);
        holder.titleLayout.setBackImg(R.mipmap.cancel_white);
        holder.titleLayout.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEnvirPop.dismiss();
            }
        });
        holder.titleLayout.setTitle(R.string.add_envir, getResources().getColor(R.color.white));
        holder.titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.white), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags = new StringBuilder();
                qjTitleTv.setText(holder.title.getText().toString());
                qjTitleTv.post(new Runnable() {
                    @Override
                    public void run() {
                        if (qjTitleTv.getLineCount() >= 2) {
                            Layout layout = qjTitleTv.getLayout();
                            StringBuilder SrcStr = new StringBuilder(qjTitleTv.getText().toString());
                            String str0 = SrcStr.subSequence(layout.getLineStart(0), layout.getLineEnd(0)).toString();
                            String str1 = SrcStr.subSequence(layout.getLineStart(1), layout.getLineEnd(1)).toString();
                            qjTitleTv2.setText(str0);
                            qjTitleTv.setText(str1);
                            qjTitleTv2.setVisibility(View.VISIBLE);
                        } else {
                            qjTitleTv2.setText("");
                            qjTitleTv2.setVisibility(View.GONE);
                        }
                    }
                });
                int sta = 0;
                SpannableString spannableStringBuilder = new SpannableString(holder.des.getText().toString());
                while (holder.des.getText().toString().substring(sta).contains("#")) {
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow_bd8913));
                    sta = holder.des.getText().toString().indexOf("#", sta);
                    if (holder.des.getText().toString().substring(sta).contains(" ")) {
                        int en = holder.des.getText().toString().indexOf(" ", sta);
                        tags.append(",").append(holder.des.getText().toString().substring(sta + 1, en));
                        spannableStringBuilder.setSpan(foregroundColorSpan, sta, en, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sta = en;
                    } else {
                        tags.append(",").append(holder.des.getText().toString().substring(sta + 1));
                        spannableStringBuilder.setSpan(foregroundColorSpan, sta, holder.des.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    }
                }
                if (tags.length() > 0) {
                    tags.deleteCharAt(0);
                }
                desTv.setText(spannableStringBuilder);
                selectEnvirPop.dismiss();
            }
        });
        holder.deleteTitle.setOnClickListener(this);
        holder.deleteDes.setOnClickListener(this);
        holder.title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.length() > 20) {
                        holder.title.setText(s.subSequence(0, 20));
                    }
                    holder.deleteTitle.setVisibility(View.VISIBLE);
                } else {
                    holder.deleteTitle.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.des.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("<<<", "before:s=" + s + ",start=" + start + ",count=" + count + ",after=" + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    holder.deleteDes.setVisibility(View.VISIBLE);
                } else {
                    holder.deleteDes.setVisibility(View.GONE);
                }
                Log.e("<<<", "change:s=" + s + ",start=" + start
                        + ",before=" + before + ",count=" + count);
                if (count == before + 1 && before == 0 && s.toString().charAt(start) == '#') {
                    String s1 = s.toString().substring(0, start);
                    String s2 = "";
                    if (s.toString().length() > start + 1) {
                        s2 = s.toString().substring(start + 1, s.length());
                    }
                    holder.des.setText(s1 + s2);
                    holder.des.setSelection(start);
                    Intent intent = new Intent(CreateQJActivity.this, AddLabelActivity.class);
                    if (activeTagsBean != null) {
                        intent.putExtra(CreateQJActivity.class.getSimpleName(), activeTagsBean);
                    }
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("<<<", "after:s=" + s);
            }
        });
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    holder.searchDelete.setVisibility(View.VISIBLE);
                } else {
                    holder.searchDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.editText.setOnKeyListener(new View.OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    //开始搜索
                    searchStr = holder.editText.getText().toString();
                    if (TextUtils.isEmpty(searchStr)) {
                        ToastUtils.showInfo("请输入搜索关键字");
//                        new SVProgressHUD(ShareSearchActivity.this).showInfoWithStatus("请输入搜索关键字");
                        return false;
                    }
                    page = 1;
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    search();
                }
                return false;
            }
        });
        holder.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        envirList = new ArrayList<>();
        searchEnvirAdapter = new SearchEnvirAdapter(this, envirList, this);
        holder.recyclerView.setAdapter(searchEnvirAdapter);
        shareCJSelectListAdapter = new ShareCJSelectListAdapter(this, list);
        holder.listView.setAdapter(shareCJSelectListAdapter);
        holder.listView.setOnScrollListener(this);
        holder.listView.setOnItemClickListener(this);
        holder.searchDelete.setOnClickListener(this);
        selectEnvirPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        selectEnvirPop.setAnimationStyle(R.style.popupwindow_style);
        selectEnvirPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        selectEnvirPop.setBackgroundDrawable(ContextCompat.getDrawable(this,
                R.color.nothing));
        selectEnvirPop.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        selectEnvirPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    private void showSelectEnvirPop() {
        holder.title.setText(qjTitleTv2.getText().toString() + qjTitleTv.getText().toString());
        holder.des.setText(desTv.getText().toString());
        categoryList();
        lastSavedFirstVisibleItem = -1;
        lastTotalItem = -1;
        selectEnvirPop.showAtLocation(activityView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > 0 && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                ) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            page++;
            holder.progressBar.setVisibility(View.VISIBLE);
            if (holder.editText.getText().toString().length() > 0) {
                search();
            } else {
                envirList();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchBean.Data.SearchItem searchItem = (SearchBean.Data.SearchItem) holder.listView.getAdapter().getItem(position);
        //设置语境
        holder.title.setText(searchItem.getTitle());
        holder.des.setText(searchItem.getDes());
    }


    @Override
    public void click(int postion) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        for (int i = 0; i < envirList.size(); i++) {
            if (i == postion) {
                envirList.get(i).setIsSelect(true);
            } else {
                envirList.get(i).setIsSelect(false);
            }
        }
        searchEnvirAdapter.notifyDataSetChanged();
        list.clear();
        shareCJSelectListAdapter.notifyDataSetChanged();
        cid = envirList.get(postion).get_id();
        page = 1;
        if (holder.editText.getText().toString().length() > 0) {
            search();
        } else {
            envirList();
        }
    }

    private void search() {
        ClientDiscoverAPI.search(holder.editText.getText().toString(), 11 + "", cid, page + "", "content", 0 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                holder.progressBar.setVisibility(View.GONE);
                SearchBean netSearch = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    netSearch = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                        lastTotalItem = -1;
                        lastSavedFirstVisibleItem = -1;
                    }
                    list.addAll(netSearch.getData().getRows());
                    shareCJSelectListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                holder.progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    //分类列表
    private void categoryList() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.categoryList(1 + "", 11 + "", 1 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                dialog.dismiss();
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<分类列表", "数据解析异常" + e.toString());
                }
                if (categoryListBean.isSuccess()) {
                    envirList.clear();
                    envirList.addAll(categoryListBean.getData().getRows());
                    searchEnvirAdapter.notifyDataSetChanged();
                    click(0);
                } else {
                    ToastUtils.showError(categoryListBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //语境列表
    private void envirList() {
        ClientDiscoverAPI.envirList(page + "", 8 + "", 1 + "", cid, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                holder.progressBar.setVisibility(View.GONE);
                SearchBean netSearch = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    netSearch = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                        lastTotalItem = -1;
                        lastSavedFirstVisibleItem = -1;
                    }
                    list.addAll(netSearch.getData().getRows());
                    shareCJSelectListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.blur_view)
        BlurView blurView;
        @Bind(R.id.title_layout)
        GlobalTitleLayout titleLayout;
        @Bind(R.id.delete_title)
        ImageView deleteTitle;
        @Bind(R.id.title)
        EditText title;
        @Bind(R.id.delete_des)
        ImageView deleteDes;
        @Bind(R.id.des)
        EditText des;
        @Bind(R.id.search1)
        ImageView search1;
        @Bind(R.id.search_delete)
        ImageView searchDelete;
        @Bind(R.id.edit_text)
        EditText editText;
        @Bind(R.id.recycler_view)
        RecyclerView recyclerView;
        @Bind(R.id.line)
        TextView line;
        @Bind(R.id.linear1)
        LinearLayout linear1;
        @Bind(R.id.gone_label)
        TextView goneLabel;
        @Bind(R.id.gone_demo_label)
        TextView goneDemoLabel;
        @Bind(R.id.gone_linear)
        RelativeLayout goneLinear;
        @Bind(R.id.list_view)
        ListView listView;
        @Bind(R.id.progress_bar)
        ProgressBar progressBar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
