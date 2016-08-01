package com.taihuoniao.fineix.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class SceneTitleSetUtils {

    /**
     * 用于动态改变场景title的宽和高
     *
     * @param sceneTitle  设置文字的textview
     * @param frameLayout 背景framelayout
     * @param sceneImg    设置图片的imageView
     * @param num         超过多少字换行
     * @param b           用于在分享时设置当前图片的宽与屏幕宽的比，设置字体大小
     */
    public static void setTitle(final TextView sceneTitle, FrameLayout frameLayout, final ImageView sceneImg, int num, double b) {
        double leng = 0;
//        int t = num;
        int position = -1;//在那一行换行
        double[] lengths = new double[sceneTitle.getText().length()];
        for (int i = 0; i < sceneTitle.getText().length(); i++) {
            char c = sceneTitle.getText().charAt(i);
            if (c >= 32 && c <= 126) {
                leng += 0.5;
//                t++;
            } else {
                leng++;
            }
            if (leng == num) {
                position = i;
            }
            lengths[i] = leng;
        }
        if (leng > num) {
            if (position == -1) {
                for (int i = 1; i < lengths.length; i++) {
                    if (lengths[i] > num && lengths[i - 1] < num) {
                        position = i - 1;
                    }
                }
            }
            String str = sceneTitle.getText().toString().substring(0, position + 1) + "\n" + sceneTitle.getText().toString().substring(position + 1);
            sceneTitle.setText(str);
        }

        if (b > 0) {
            if (leng < 8) {
                sceneTitle.setTextSize((float) (b * 35));
            } else if (leng < 14) {
                sceneTitle.setTextSize((float) (b * 25));
            } else {
                sceneTitle.setTextSize((float) (b * 20));
            }
        }
//        动态改变宽高
        ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        sceneTitle.measure(spec, spec);
        if (leng > num) {
            Bitmap bitmap = BitmapFactory.decodeResource(sceneTitle.getResources(), R.mipmap.scene_title_background).copy(Bitmap.Config.ARGB_8888, true);
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap1);
            canvas.drawColor(Color.argb(0, 0, 0, 0));
            canvas.clipRect(0, 0, sceneTitle.getMeasuredWidth(), sceneTitle.getMeasuredHeight() / 2);
            canvas.clipRect(0, 0, (float) ((lengths[lengths.length - 1] - position - 1) * sceneTitle.getTextSize()),
                    sceneTitle.getMeasuredHeight(), Region.Op.UNION);
            Paint paint = new Paint();
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.save();
            canvas.restore();
            sceneImg.setImageBitmap(bitmap1);
        } else {
            sceneImg.setImageResource(R.mipmap.scene_title_background);
        }
        lp.width = sceneTitle.getMeasuredWidth();
        lp.height = sceneTitle.getMeasuredHeight();
        frameLayout.setLayoutParams(lp);
    }

}
