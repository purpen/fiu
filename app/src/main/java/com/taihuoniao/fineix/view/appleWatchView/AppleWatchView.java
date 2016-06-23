package com.taihuoniao.fineix.view.appleWatchView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.beans.FiuUserListBean;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 16/1/18.
 */
public class AppleWatchView extends View {

    public static final String TAG = "AppleWatchView";

    private ArrayList<AppInfo> mAppInfos;
    private Paint mPaint;
    private GestureDetector mGestureDetector;
    private WatchGestureListener mGestureListener;
    private Activity mActivity;
    private float mLastX;
    private float mLastY;

    //account of line,value can be 3/5/7
    private int mLineAccount;
    private int[] mIconsArrange;
    private int midLine;
    private int mRadius;
    private int mCenterX;
    private int mCenterY;
    private int mWidth;
    private int mHeight;
    //最右边Item的下标
    private int rightItemIndex;
    //最左边Item的下标
    private int leftItemIndex;
    private int topItemIndex;
    private int bottomItemIndex;

    public AppleWatchView(Context context) {
        super(context);
        mActivity = (Activity) context;
        init(context);
    }

    public AppleWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        init(context);
    }

    public AppleWatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        init(context);
    }

    private FiuUserListBean users = null;
    private BrandListBean brands = null;

    private void init(Context context) {
        mAppInfos = new ArrayList<>();
        mIconsArrange = Utils.generateNums(mAppInfos.size());
        mLineAccount = mIconsArrange.length;
        midLine = mLineAccount / 2;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mGestureListener = new WatchGestureListener();
        mGestureDetector = new GestureDetector(context, mGestureListener);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        int midLine = mLineAccount / 2;
        for (int i = 0; i <= midLine; i++) {
            if (i == midLine) {
                rightItemIndex += mIconsArrange[i];
                rightItemIndex--;
            } else {
                leftItemIndex += mIconsArrange[i];
                rightItemIndex += mIconsArrange[i];
            }
        }

        topItemIndex = mIconsArrange[0] / 2;

        for (int i = 0; i < mLineAccount - 1; i++) {
            bottomItemIndex += mIconsArrange[i];
        }

        bottomItemIndex = bottomItemIndex + mIconsArrange[mLineAccount - 1] / 2;
    }

    public void init(Context context, FiuUserListBean users, BrandListBean brands) {
        this.users = users;
        this.brands = brands;
        mAppInfos = new ArrayList<>();

//        PackageManager pm = context.getPackageManager();
//        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        if (users != null) {
            for (FiuUserListBean.FiuUserItem user : users.getData().getUsers()) {
                final AppInfo info = new AppInfo();
                ImageLoader.getInstance().loadImage(user.getMedium_avatar_url(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        info.appIcon = loadedImage;
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
                Intent intent = new Intent(mActivity, UserCenterActivity.class);
                intent.putExtra(FocusActivity.USER_ID_EXTRA, Long.parseLong(user.get_id()));
                info.appIntent = intent;
                mAppInfos.add(info);
            }
        } else if (brands != null) {
            for (BrandListBean.BrandItem brand : brands.getData().getRows()) {
                final AppInfo info = new AppInfo();
                ImageLoader.getInstance().loadImage(brand.getCover_url(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        info.appIcon = loadedImage;
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
                Intent intent = new Intent(mActivity, BrandDetailActivity.class);
                intent.putExtra("id", brand.get_id());
                info.appIntent = intent;
                mAppInfos.add(info);
            }

        }

        mIconsArrange = Utils.generateNums(mAppInfos.size());
        mLineAccount = mIconsArrange.length;
        midLine = mLineAccount / 2;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mGestureListener = new WatchGestureListener();
        mGestureDetector = new GestureDetector(context, mGestureListener);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        int midLine = mLineAccount / 2;
        for (int i = 0; i <= midLine; i++) {
            if (i == midLine) {
                rightItemIndex += mIconsArrange[i];
                rightItemIndex--;
            } else {
                leftItemIndex += mIconsArrange[i];
                rightItemIndex += mIconsArrange[i];
            }
        }

        topItemIndex = mIconsArrange[0] / 2;

        for (int i = 0; i < mLineAccount - 1; i++) {
            bottomItemIndex += mIconsArrange[i];
        }

        bottomItemIndex = bottomItemIndex + mIconsArrange[mLineAccount - 1] / 2;
        new Thread() {
            @Override
            public void run() {
                boolean isStop = false;
                whi:
                while (!isStop) {
                    for (int i = 0; i < mAppInfos.size(); i++) {
                        if (mAppInfos.get(i).appIcon == null) {
                            continue whi;
                        }
                    }
                    isStop = true;
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        mRadius = DensityUtils.dp2px(mActivity, 26);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAppInfos.size() <= 0) {
            return;
        }
        canvas.drawColor(getResources().getColor(R.color.f0f0f0));
        for (int i = 0; i < mLineAccount; i++) {
            drawCircles(i, mIconsArrange[i], canvas);
        }
    }

    private void drawCircles(int line, int account, Canvas canvas) {
        initLocationParam(line, account);
        for (int i = 0; i < account; i++) {
            int index = getIndex(line, i);
            AppInfo info = mAppInfos.get(index);
            mPaint.setShader(info.bitmapShader);
            canvas.drawCircle(info.x, info.y, mRadius * Math.min(info.narrowX, info.narrowY), mPaint);
        }
    }

    private void initLocationParam(int line, int account) {

        int centerY = mCenterY + (line - midLine) * mRadius * 2;
        int midNum = account / 2;
        for (int i = 0; i < account; i++) {
            int centerX;
            if (account % 2 == 0) {
                centerX = mCenterX + (i - midNum) * mRadius * 2 + mRadius;
            } else {
                centerX = mCenterX + (i - midNum) * mRadius * 2;
            }

            int index = getIndex(line, i);
            AppInfo info = mAppInfos.get(index);

//            Drawable icon = info.appIcon;
//            if (icon instanceof BitmapDrawable) {
            Bitmap bitmap;
            if (info.icon == null) {
                bitmap = info.appIcon;
                info.icon = bitmap;
            } else {
                bitmap = info.icon;
            }
            if (bitmap == null) {
                return;
            }

            if (info.bitmapShader == null) {
                if (info.scale <= 0) {
                    info.scale = (mRadius * 2.0f) / bitmap.getWidth();
                }

                Matrix matrix = new Matrix();
                matrix.setScale(info.scale, info.scale);
                matrix.postTranslate(centerX - mRadius, centerY - mRadius);
                BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                bitmapShader.setLocalMatrix(matrix);
                info.bitmapShader = bitmapShader;
            }

            float narrowX;
            float halfWidth = mWidth / 2.0f;
            if ((centerX - getScrollX()) < halfWidth) {
                narrowX = (centerX - getScrollX()) / halfWidth;
            } else {
                int disX = mWidth - centerX + getScrollX();
                narrowX = disX / halfWidth;
            }

            narrowX = narrowX * 0.5f + 0.5f;

            float narrowY;
            float halfHeight = mHeight / 2.0f;
            if ((centerY - getScrollY()) < halfHeight) {
                narrowY = (centerY - getScrollY()) / halfHeight;
            } else {
                int disY = mHeight - centerY + getScrollY();
                narrowY = disY / halfHeight;
            }

            narrowY = narrowY * 0.5f + 0.5f;

            info.x = centerX;
            info.y = centerY;
            info.narrowX = narrowX;
            info.narrowY = narrowY;
            float radius = mRadius * Math.min(info.narrowX, info.narrowY);
            info.content = new RectF(info.x - radius, info.y - radius, info.x + radius, info.y + radius);
//            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAppInfos.size() <= 0) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int disX = (int) (event.getX() - mLastX);
                int disY = (int) (event.getY() - mLastY);

                int scrollX = getScrollX();
                int scrollY = getScrollY();

                if (disX < 0) {
                    if ((mAppInfos.get(rightItemIndex).x - scrollX) <= mCenterX) {
                        return true;
                    }
                } else {
                    if ((mAppInfos.get(leftItemIndex).x - scrollX) >= mCenterX) {
                        return true;
                    }
                }
                if (disY < 0) {
                    if ((mAppInfos.get(bottomItemIndex).y - scrollY) <= mCenterY) {
                        return true;
                    }
                } else {
                    if ((mAppInfos.get(topItemIndex).y - scrollY) >= mCenterY) {
                        return true;
                    }
                }
//                if (disX < 0) {
//                    if ((mAppInfos.get(rightItemIndex).x - scrollX) <= 0) {
//                        return true;
//                    }
//                } else {
//                    if ((mAppInfos.get(leftItemIndex).x - scrollX) >= mWidth) {
//                        return true;
//                    }
//                }
//                if (disY < 0) {
//                    if ((mAppInfos.get(bottomItemIndex).y - scrollY) <= 0) {
//                        return true;
//                    }
//                } else {
//                    if ((mAppInfos.get(topItemIndex).y - scrollY) >= mHeight) {
//                        return true;
//                    }
//                }

                scrollBy(-disX, -disY);

                mLastX = event.getX();
                mLastY = event.getY();
                break;
        }

        mGestureDetector.onTouchEvent(event);

        return true;
    }

    /**
     * 获取第line行，第i个元素在集合中的位置
     *
     * @param line
     * @param i
     * @return
     */
    private int getIndex(int line, int i) {
        int num = 0;
        if (line == 0) {
            num = i;
        } else {
            for (int k = 0; k < line; k++) {
                num += mIconsArrange[k];
            }
            num += i;
        }
        return num;
    }

    class AppInfo {
        RectF content;
        String appName;
        String pkgName;
        Bitmap appIcon;
        Intent appIntent;
        Bitmap icon;
        int x;
        int y;
        float narrowX;
        float narrowY;
        float scale;
        BitmapShader bitmapShader;
    }

    private class WatchGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            float clickX = e.getX() + getScrollX();
            float clickY = e.getY() + getScrollY();
            for (AppInfo info : mAppInfos) {
                if (info == null || info.content == null) {
                    return false;
                }
                if (info.content.contains(clickX, clickY)) {
                    if (info.appIntent != null) {
                        mActivity.startActivity(info.appIntent);
                    }
                    break;
                }
            }
            return true;
        }
    }
}