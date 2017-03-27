package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 亮点详情
 */
public class LightSpotDetailActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHeadView;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.high_light)
    TextView highLight;
    private ZoneDetailBean zoneDetailBean;
    public LightSpotDetailActivity() {
        super(R.layout.activity_light_spot);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ZoneDetailActivity.class.getSimpleName())) {
            zoneDetailBean = intent.getParcelableExtra(ZoneDetailActivity.class.getSimpleName());
        }

    }

    @OnClick({R.id.tv_head_right})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_head_right:
                Intent intent = new Intent(activity,ZoneEditBrightActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initView() {
        highLight.getPaint().setFakeBoldText(true);
        if (zoneDetailBean==null) return;
        customHeadView.setHeadCenterTxtShow(true,zoneDetailBean.title);
        setBrightSpots(zoneDetailBean.bright_spot);
    }

    private void setBrightSpots(List<String> brightSpot) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.dp195));
        int size = getResources().getDimensionPixelSize(R.dimen.dp10);
        params.bottomMargin = size;
        imageParams.bottomMargin = size;
        for (String str : brightSpot) {
            if (!str.contains(Constants.SEPERATOR)) continue;
            String[] split = str.split(Constants.SEPERATOR);
            if (TextUtils.equals(split[0], Constants.TEXT_TYPE)) {
                TextView textView = new TextView(activity);
                textView.setLayoutParams(params);
                textView.setText(split[1]);
                textView.setLineSpacing(1,1.2f);
                textView.setTextColor(getResources().getColor(R.color.color_222));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                llContainer.addView(textView);
            } else if (TextUtils.equals(split[0], Constants.IMAGE_TYPE)) {
                ImageView imageView = new ImageView(activity);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(imageParams);
                GlideUtils.displayImageFadein(split[1], imageView);
                llContainer.addView(imageView);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
