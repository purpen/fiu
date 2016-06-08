package com.taihuoniao.fineix.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class SceneTitleSetUtils {
//    static Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.scene_title_background);

    //用于动态改变场景title的宽和高
    public static void setTitle(TextView sceneTitle, FrameLayout frameLayout, int maxTitleSize, int minTitleSize, double b) {
//        尝试动态改变bimap
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
        Log.e("<<<缩小倍数", b + "");
//            遍历所有字符判断是否含有英文字符。有的话算半个
//        FrameLayout.LayoutParams slp = (FrameLayout.LayoutParams) sceneTitle.getLayoutParams();
        if (leng < 8) {
            sceneTitle.setTextSize((float) (35 * b));
//            slp.topMargin = DensityUtils.dp2px(sceneTitle.getContext(), maxTitleSize == minTitleSize ? -4 : -8);
        } else if (leng < 14) {
            sceneTitle.setTextSize((float) (25 * b));
        } else {
            sceneTitle.setTextSize((float) (20 * b));
//            slp.topMargin = DensityUtils.dp2px(sceneTitle.getContext(), -4);
        }
//        Log.e("<<<测量", "title=" + sceneTitle.getText().toString() + "leng=" + leng + ",l=" + l +
//                ",size=" + sceneTitle.getTextSize() + "width=" + sceneTitle.getMeasuredWidth() + "height=" + sceneTitle.getMeasuredHeight());
//        sceneTitle.setLayoutParams(slp);
//        动态改变宽高
        ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
////        Log.e("<<<最大", DensityUtils.dp2px(sceneTitle.getContext(), 245*bi) + "," + (8 * sceneTitle.getTextSize()));
////        Log.e("<<<动态改变宽高", "scale=" + sceneTitle.getTextScaleX() + ",size=" + sceneTitle.getTextSize() + ",leng=" + leng + ",全部字数宽度=" + (leng * sceneTitle.getTextSize()));
//        if (sceneTitle.getTextSize() > DensityUtils.sp2px(sceneTitle.getContext(), 30)) {
//            lp.width = (int) (leng * sceneTitle.getTextSize());
//            lp.height = DensityUtils.dp2px(sceneTitle.getContext(), 41);
//        } else if (sceneTitle.getTextSize() > DensityUtils.sp2px(sceneTitle.getContext(), 23)) {
//            lp.width = (int) (leng*sceneTitle.getTextSize());
//            lp.height = DensityUtils.dp2px(sceneTitle.getContext(),29);
//        } else {
//            lp.height = DensityUtils.dp2px(sceneTitle.getContext(), 49);
//            lp.width = DensityUtils.dp2px(sceneTitle.getContext(), 259);
//        }
//        float textSize = sceneTitle.getPaint().getTextSize();
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        sceneTitle.measure(spec, spec);
//        Log.e("<<<测量","measureWidth="+sceneTitle.getMeasuredWidth()+",measureHeight="+sceneTitle.getMeasuredHeight());
        lp.width = sceneTitle.getMeasuredWidth();
        lp.height = sceneTitle.getMeasuredHeight();
        frameLayout.setLayoutParams(lp);
    }
}
