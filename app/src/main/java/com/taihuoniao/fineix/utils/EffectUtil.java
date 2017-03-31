package com.taihuoniao.fineix.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.Addon;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.MyHighlightView;
import com.taihuoniao.fineix.view.MyImageViewTouch;
import com.taihuoniao.fineix.view.StickerDrawable;
import com.taihuoniao.fineix.view.imageViewTouch.ImageViewTouch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by sky on 15/7/6.
 */
public class EffectUtil {

    public static List<Addon> addonList = new ArrayList<>();
    private static List<MyHighlightView> hightlistViews = new CopyOnWriteArrayList<>();

    public static int size() {
        return hightlistViews.size();
    }

    public static void clear() {
        hightlistViews.clear();
    }


    public interface StickerCallback {
        void onRemoveSticker(ImageView imageView);
    }

    //添加商品
    public static MyHighlightView addStickerImage(final ImageViewTouch processImage,
                                                  Context context, Bitmap bit) {
        if (bit == null) {
            return null;
        }
        StickerDrawable drawable = new StickerDrawable(context.getResources(), bit);
        drawable.setAntiAlias(true);
        drawable.setMinSize(30, 30);

        final MyHighlightView hv = new MyHighlightView(processImage, R.style.AppTheme, drawable);
        hv.setPadding(0);
        hv.setOnDeleteClickListener(new MyHighlightView.OnDeleteClickListener() {

            @Override
            public void onDeleteClick() {
                ((MyImageViewTouch) processImage).removeHightlightView(hv);
                hightlistViews.remove(hv);
                processImage.invalidate();
            }
        });

        Matrix mImageMatrix = processImage.getImageViewMatrix();

        int cropWidth, cropHeight;
        int x, y;

        final int width = processImage.getWidth();
        final int height = processImage.getHeight();

        // width/height of the sticker
        cropWidth = (int) drawable.getCurrentWidth();
        cropHeight = (int) drawable.getCurrentHeight();

        final int cropSize = Math.max(cropWidth, cropHeight);
        final int screenSize = Math.min(processImage.getWidth(), processImage.getHeight());
        RectF positionRect = null;
        if (cropSize > screenSize) {
            float ratio;
            float widthRatio = (float) processImage.getWidth() / cropWidth;
            float heightRatio = (float) processImage.getHeight() / cropHeight;

            if (widthRatio < heightRatio) {
                ratio = widthRatio;
            } else {
                ratio = heightRatio;
            }

            cropWidth = (int) ((float) cropWidth * (ratio / 2));
            cropHeight = (int) ((float) cropHeight * (ratio / 2));

            int w = processImage.getWidth();
            int h = processImage.getHeight();
            positionRect = new RectF(w / 2 - cropWidth / 2, h / 2 - cropHeight / 2,
                    w / 2 + cropWidth / 2, h / 2 + cropHeight / 2);

            positionRect.inset((positionRect.width() - cropWidth) / 2,
                    (positionRect.height() - cropHeight) / 2);
        }

        if (positionRect != null) {
            x = (int) positionRect.left;
            y = (int) positionRect.top;

        } else {
            x = (width - cropWidth) / 2;
            y = (height - cropHeight) / 2;
        }

        Matrix matrix = new Matrix(mImageMatrix);
        matrix.invert(matrix);

        float[] pts = new float[]{x, y, x + cropWidth, y + cropHeight};
        MatrixUtils.mapPoints(matrix, pts);

        RectF cropRect = new RectF(pts[0], pts[1], pts[2], pts[3]);
        Rect imageRect = new Rect(0, 0, width, height);

        hv.setup(context, mImageMatrix, imageRect, cropRect, false);

        ((MyImageViewTouch) processImage).addHighlightView(hv);
        ((MyImageViewTouch) processImage).setSelectedHighlightView(hv);
        hightlistViews.add(hv);
        return hv;
    }

    //----添加标签-----
    public static void addLabelEditable(View.OnClickListener deleteListener, MyImageViewTouch mImageView, RelativeLayout gpuRelative,
                                        LabelView label, int left, int top) {
        addLabel(deleteListener, mImageView, gpuRelative, label, left, top);
        addLabel2Overlay(mImageView, label);
    }

    private static void addLabel(View.OnClickListener deleteListener, MyImageViewTouch overlay, RelativeLayout container, LabelView label, int left, int top) {
        label.addTo(deleteListener, overlay, container, left, top);
    }

    public static void removeLabelEditable(MyImageViewTouch overlay, ViewGroup container,
                                           LabelView label) {
        label.stopAnim();
        container.removeView(label);
        overlay.removeLabel(label);
    }

    public static double getStandDis(float realDis, float baseWidth) {
        return realDis / baseWidth;
    }

    /**
     * 使标签在Overlay上可以移动
     *
     * @param overlay
     * @param label
     */
    private static void addLabel2Overlay(final MyImageViewTouch overlay,
                                         final LabelView label) {
        //添加事件，触摸生效
        overlay.addLabel(label);
        label.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 手指按下时
                        overlay.setCurrentLabel(label, event.getRawX(), event.getRawY());
                        return false;
                    default:
                        return false;
                }
            }
        });
    }

    //添加商品
    public static void applyOnSave(Canvas mCanvas, ImageViewTouch processImage) {
        for (MyHighlightView view : hightlistViews) {
            applyOnSave(mCanvas, processImage, view);
        }
    }

    private static void applyOnSave(Canvas mCanvas, ImageViewTouch processImage, MyHighlightView view) {

        if (view != null && view.getContent() instanceof StickerDrawable) {

            final StickerDrawable stickerDrawable = ((StickerDrawable) view.getContent());
            RectF cropRect = view.getCropRectF();
            double bi = (double) mCanvas.getWidth() / MainApplication.getContext().getScreenWidth();
            Rect rect = new Rect((int) (bi * cropRect.left), (int) (bi* cropRect.top), (int) (bi* cropRect.right),
                    (int) (bi*cropRect.bottom));

            Matrix rotateMatrix = view.getCropRotationMatrix(bi);
            Matrix matrix = new Matrix(processImage.getImageMatrix());
            if (!matrix.invert(matrix)) {
            }
            int saveCount = mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
            mCanvas.concat(rotateMatrix);

            stickerDrawable.setDropShadow(false);
            view.getContent().setBounds(rect);
            view.getContent().draw(mCanvas);
            mCanvas.restoreToCount(saveCount);
        }
    }

}
