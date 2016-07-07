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
    public static void setTitle(final TextView sceneTitle, FrameLayout frameLayout, int maxTitleSize, int minTitleSize, double b) {
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
//        动态改变宽高
        ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        sceneTitle.measure(spec, spec);
        sceneTitle.post(new Runnable() {
            @Override
            public void run() {
                if(sceneTitle.getLineCount()==2){

                }
            }
        });
//        Log.e("<<<测量","measureWidth="+sceneTitle.getMeasuredWidth()+",measureHeight="+sceneTitle.getMeasuredHeight());
        lp.width = sceneTitle.getMeasuredWidth();
        lp.height = sceneTitle.getMeasuredHeight();
        frameLayout.setLayoutParams(lp);

//        Bitmap bitmap = BitmapFactory.decodeResource(sceneTitle.getResources(), R.mipmap.scene_title_background).copy(Bitmap.Config.ARGB_8888, true);
//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap1);
//        canvas.drawColor(Color.argb(0, 0, 0, 0));
//        canvas.clipRect(0, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
//        canvas.clipRect(0, 0, bitmap.getWidth() / 2, bitmap.getHeight(), Region.Op.UNION);
//        Paint paint = new Paint();
//        canvas.drawBitmap(bitmap, 0, 0, paint);
//        canvas.save();
//        canvas.restore();
//        imageView.setImageBitmap(bitmap1);
    }

    //用于动态改变场景title的宽和高
    public static void setTitle(final TextView sceneTitle, FrameLayout frameLayout) {
//        尝试动态改变bimap
        double leng = sceneTitle.getText().length();
        for (char c : sceneTitle.getText().toString().toCharArray()) {
            if (c >= 32 && c <= 126) {
                leng -= 0.5;
            }
        }
//            遍历所有字符判断是否含有英文字符。有的话算半个
//        FrameLayout.LayoutParams slp = (FrameLayout.LayoutParams) sceneTitle.getLayoutParams();
//        动态改变宽高
        ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        sceneTitle.measure(spec, spec);
        sceneTitle.post(new Runnable() {
            @Override
            public void run() {
                if (sceneTitle.getLineCount() == 2) {

                }
            }
        });
//        Log.e("<<<测量","measureWidth="+sceneTitle.getMeasuredWidth()+",measureHeight="+sceneTitle.getMeasuredHeight());
        lp.width = sceneTitle.getMeasuredWidth();
        lp.height = sceneTitle.getMeasuredHeight();
        frameLayout.setLayoutParams(lp);

//        Bitmap bitmap = BitmapFactory.decodeResource(sceneTitle.getResources(), R.mipmap.scene_title_background).copy(Bitmap.Config.ARGB_8888, true);
//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap1);
//        canvas.drawColor(Color.argb(0, 0, 0, 0));
//        canvas.clipRect(0, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
//        canvas.clipRect(0, 0, bitmap.getWidth() / 2, bitmap.getHeight(), Region.Op.UNION);
//        Paint paint = new Paint();
//        canvas.drawBitmap(bitmap, 0, 0, paint);
//        canvas.save();
//        canvas.restore();
//        imageView.setImageBitmap(bitmap1);
    }
}
