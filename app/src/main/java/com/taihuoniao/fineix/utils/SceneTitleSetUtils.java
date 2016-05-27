package com.taihuoniao.fineix.utils;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class SceneTitleSetUtils {
    //用于动态改变场景title的宽和高
    public static void setTitle(TextView sceneTitle, FrameLayout frameLayout, int maxTitleSize, int minTitleSize,double bi) {
        double leng = sceneTitle.getText().length();
        for (char c : sceneTitle.getText().toString().toCharArray()) {
            if (c >= 32 && c <= 126) {
                leng -= 0.5;
            }
        }
        int l = 0;
        if (leng * 10 % 10 != 0) {
            l = 1 + (int) leng;
        } else {
            l = (int) leng;
        }
//            遍历所有字符判断是否含有英文字符。有的话算半个
        FrameLayout.LayoutParams slp = (FrameLayout.LayoutParams) sceneTitle.getLayoutParams();
        if (l < 8) {
            sceneTitle.setTextSize((float) (maxTitleSize*bi));
            slp.topMargin = DensityUtils.dp2px(sceneTitle.getContext(), maxTitleSize == minTitleSize ? -4*bi : -8*bi);
        } else {
            sceneTitle.setTextSize((float) (minTitleSize*bi));
            slp.topMargin = DensityUtils.dp2px(sceneTitle.getContext(), -4*bi);
        }
        sceneTitle.setLayoutParams(slp);
//        动态改变宽高
        ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
//        Log.e("<<<最大", DensityUtils.dp2px(sceneTitle.getContext(), 245*bi) + "," + (8 * sceneTitle.getTextSize()));
//        Log.e("<<<动态改变宽高", "scale=" + sceneTitle.getTextScaleX() + ",size=" + sceneTitle.getTextSize() + ",l=" + l + ",全部字数宽度=" + (l * sceneTitle.getTextSize()) + ",300dp=" + (DensityUtils.dp2px(sceneTitle.getContext(), 300)));
        if (l <= 12) {
            lp.width = (int) (sceneTitle.getTextSize() * l +l);
        } else {
            lp.width = (int) (sceneTitle.getTextSize() * 12 );
        }
        if (sceneTitle.getTextSize() < DensityUtils.sp2px(sceneTitle.getContext(), (float) (30*bi))) {
            if (l * sceneTitle.getTextSize() <= DensityUtils.dp2px(sceneTitle.getContext(), 245*bi)) {
                lp.height = DensityUtils.dp2px(sceneTitle.getContext(), 24*bi);
            } else {
                lp.height = DensityUtils.dp2px(sceneTitle.getContext(), 47*bi);
            }
        } else {
            lp.height = DensityUtils.dp2px(sceneTitle.getContext(), 45*bi);
        }
        frameLayout.setLayoutParams(lp);
    }
}
