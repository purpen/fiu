package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.MapSearchAddressActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/15.
 */
public class CreateQJActivity extends BaseActivity implements View.OnClickListener {
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
    TextView desTv;
    @Bind(R.id.location_relative)
    RelativeLayout locationRelative;
    @Bind(R.id.delete_address)
    ImageView deleteAddressImg;
    @Bind(R.id.location_tv)
    TextView locationTv;
    private float pointWidth;//LabelView动画点的宽高
    private float labelMargin;//LabelView动画点左间距
    //选择语境popupwindow
    private PopupWindow selectEnvirPop;
    public CreateQJActivity() {
        super(R.layout.activity_create_qj);
    }

    @Override
    protected void initView() {
        titleLayout.setBackgroundResource(R.color.title_black);
        titleLayout.setTitle(R.string.create_qingjing, getResources().getColor(R.color.white));
        titleLayout.setContinueListener(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) container.getLayoutParams();
        layoutParams.width = MainApplication.getContext().getScreenWidth();
        layoutParams.height = layoutParams.width;
        container.setLayoutParams(layoutParams);
        pointWidth = getResources().getDimension(R.dimen.label_point_width);
        labelMargin = getResources().getDimension(R.dimen.label_point_margin);
        initSelectEnvirPop();
    }

    @Override
    protected void initList() {
        qjTitleTv.setOnClickListener(this);
        qjTitleTv2.setOnClickListener(this);
        desTv.setOnClickListener(this);
        locationRelative.setOnClickListener(this);
        deleteAddressImg.setOnClickListener(this);
        if (getIntent().hasExtra(PictureEditActivity.class.getSimpleName())) {
            backgroundImg.setImageBitmap(MainApplication.editBitmap);//设置背景图片
            for (final TagItem tagItem : MainApplication.tagInfoList) {//添加标签
                Log.e("<<<传递过来", MainApplication.tagInfoList.toString());
                final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                final LabelView labelView = new LabelView(this);
                labelView.init(tagItem);
                labelView.setLayoutParams(layoutParams);
                if (tagItem.getX() < 0.5) {
                    labelView.nameTv.setBackgroundResource(R.drawable.label_left);
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointRelative.getLayoutParams();
                    layoutParams1.leftMargin = (int) labelMargin;
                    labelView.pointRelative.setLayoutParams(layoutParams1);
                }
                labelView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tagItem.getX() < 0.5) {
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                            lp.leftMargin = (int) (tagItem.getX() * MainApplication.getContext().getScreenWidth() - labelMargin - pointWidth / 2);
                            lp.topMargin = (int) (tagItem.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + pointWidth / 2);
                            Log.e("<<<", "getX=" + tagItem.getX() + ",screenWidth=" + MainApplication.getContext().getScreenWidth() +
                                    ",labelMargin=" + labelMargin + ",pointWidth/2=" + pointWidth / 2 + ",leftMargin=" + lp.leftMargin);
                            Log.e("<<<", "getY=" + tagItem.getY() + ",labelView.height=" + labelView.getMeasuredHeight() + ",topMargin=" + lp.topMargin);
                            labelView.setLayoutParams(lp);
                        } else {
                            labelView.nameTv.setBackgroundResource(R.drawable.label_right);
                            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointRelative.getLayoutParams();
                            layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - pointWidth - labelMargin);
                            labelView.pointRelative.setLayoutParams(layoutParams1);
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
    }

    private void initSelectEnvirPop() {
        View view = View.inflate(this,R.layout.activity_share_search,null);
        selectEnvirPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_address:
                locationTv.setText("添加地点");
                longitude = 0;
                latitude = 0;
                deleteAddressImg.setVisibility(View.GONE);
                break;
            case R.id.location_relative:
                Intent intent = new Intent(this, MapSearchAddressActivity.class);
                startActivityForResult(intent, DataConstants.REQUESTCODE_CREATESCENE_BDSEARCH);
                break;
            case R.id.des_tv:
            case R.id.qj_title_tv:
            case R.id.qj_title_tv2:
//                ToastUtils.showError("情景标题");
                showSelectEnvirPop();
                break;
            case R.id.title_continue:
                ToastUtils.showError("创建情景");
                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case DataConstants.RESULTCODE_CREATESCENE_BDSEARCH:
                    PoiInfo poiInfo = data.getParcelableExtra(PoiInfo.class.getSimpleName());
                    String city = data.getStringExtra("city");
                    if (poiInfo != null) {
//                        locationTv.setText(poiInfo.name);
//                        deleteAddress.setVisibility(View.VISIBLE);
//                        cityTv.setText(city);
//                        cityTv.setVisibility(View.VISIBLE);
//                        lng = poiInfo.location.longitude;
//                        lat = poiInfo.location.latitude;
                        locationTv.setText(poiInfo.name);
                        longitude = poiInfo.location.longitude;
                        latitude = poiInfo.location.latitude;
                        deleteAddressImg.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    private double longitude, latitude;
}
