package com.taihuoniao.fineix.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.taihuoniao.fineix.view.imageViewTouch.ImageViewTouch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 03-24 12:00:38.420  26411-26411/com.taihuoniao.fineix I/ImageViewTouchBase﹕ onDown
 * 03-24 12:00:38.425  26411-26411/com.taihuoniao.fineix I/myhighlightview﹕ setMode: 64
 * 03-24 12:00:38.430  26411-26411/com.taihuoniao.fineix I/ImageViewTouchBase﹕ onSingleTapUp
 * 03-24 12:00:38.430  26411-26411/com.taihuoniao.fineix I/ImageViewTouchBase﹕ onUp
 * 03-24 12:00:38.430  26411-26411/com.taihuoniao.fineix I/myhighlightview﹕ setMode: 1
 */

public class MyImageViewTouch extends ImageViewTouch {
    //也就是说点击贴纸前两个方法肯定会进入，后两个方法只会进一个
    public interface OnDrawableEventListener {
        //贴纸的焦点切换回调,只要点击就会进入
        void onFocusChange(MyHighlightView newFocus, MyHighlightView oldFocus);

        //按下时的回调，点击就会进入，在焦点切换之后
        void onDown(MyHighlightView view);

        //移动时才会进入，在按下之后
        void onMove(MyHighlightView view);

        //单击但是没有移动时会进入，在按下之后
        void onClick(MyHighlightView view);

        //标签的点击事件处理
        void onClick(LabelView label);

//        //移动标签后手指抬起事件
//        void onUp(LabelView labelView);
    }

    //删除的时候会出错
    private List<MyHighlightView> mOverlayViews = new CopyOnWriteArrayList<>();

    private MyHighlightView mOverlayView;

    private OnDrawableEventListener mDrawableListener;

    private Paint mDropPaint;

    private Rect mTempRect = new Rect();

    private boolean mScaleWithContent = false;

    private List<LabelView> labels = new ArrayList<>();
    //当前被点击的标签
    private LabelView currentLabel;
    //标签被点击的处与基本坐标的距离
    private float labelX, labelY, downLabelX, downLabelY;

    /************************[BEGIN]贴纸处理**********************/
    /**
     * 用于感知label被点击了
     */
    //贴纸在上面进行操作
    public void setCurrentLabel(LabelView label, float eventRawX, float eventRawY) {
        if (labels.contains(label)) {
            currentLabel = label;
            int[] location = new int[2];
            label.getLocationOnScreen(location);
            labelX = eventRawX - location[0];
            labelY = eventRawY - location[1];

            downLabelX = eventRawX;
            downLabelY = eventRawY;
        } else if (label == null) {
            currentLabel = null;
        }
    }

    public void addLabel(LabelView label) {
        labels.add(label);
    }

    public void removeLabel(LabelView label) {
        currentLabel = null;
        labels.remove(label);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (currentLabel != null) {
            currentLabel.updateLocation((int) (event.getX() - labelX),
                    (int) (event.getY() - labelY));
            currentLabel.invalidate();
        }
        if (currentLabel != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:// 手指离开时
                case MotionEvent.ACTION_CANCEL:

                    float upX = event.getRawX();
                    float upY = event.getRawY();
                    double distance = Math.sqrt(Math.abs(upX - downLabelX)
                            * Math.abs(upX - downLabelX)
                            + Math.abs(upY - downLabelY)
                            * Math.abs(upY - downLabelY));//两点之间的距离
                    if (distance < 15) { // 距离较小，当作click事件来处理
                        if (mDrawableListener != null) {
                            mDrawableListener.onClick(currentLabel);
                        }
                    }
                    currentLabel = null;
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    /************************
     * [END]贴纸处理
     **********************/

    public MyImageViewTouch(Context context) {
        super(context);
        //setScrollEnabled(false);
    }

    public MyImageViewTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setScrollEnabled(false);
    }

    public MyImageViewTouch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //setScrollEnabled(false);
    }

    protected void panBy(double dx, double dy) {
        RectF rect = getBitmapRect();
        mScrollRect.set((float) dx, (float) dy, 0, 0);
        updateRect(rect, mScrollRect);
        //postTranslate(mScrollRect.left, mScrollRect.top);
        center(true, true);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyle) {
        super.init(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledDoubleTapSlop();
        mGestureDetector.setIsLongpressEnabled(false);
    }

    /**
     * How overlay content will be scaled/moved
     * when zomming/panning the base image
     *
     * @param value true if content will scale according to the image
     */
    public void setScaleWithContent(boolean value) {
        mScaleWithContent = value;
    }

    public boolean getScaleWithContent() {
        return mScaleWithContent;
    }

    /**
     * If true, when the user tap outside the drawable overlay and
     * there is only one active overlay selection is not changed.
     *
     * @param value the new force single selection
     */
    public void setForceSingleSelection(boolean value) {
        boolean mForceSingleSelection = value;
    }

    public void setOnDrawableEventListener(OnDrawableEventListener listener) {
        mDrawableListener = listener;
    }

    @Override
    public void setImageDrawable(android.graphics.drawable.Drawable drawable,
                                 Matrix initial_matrix, float min_zoom, float max_zoom) {
        super.setImageDrawable(drawable, initial_matrix, min_zoom, max_zoom);
    }

    @Override
    protected void onLayoutChanged(int left, int top, int right, int bottom) {
        super.onLayoutChanged(left, top, right, bottom);

        if (getDrawable() != null) {

            for (MyHighlightView view : mOverlayViews) {
                view.getMatrix().set(getImageMatrix());
                view.invalidate();
            }
        }
    }

    @Override
    public void postTranslate(float deltaX, float deltaY) {
        super.postTranslate(deltaX, deltaY);

        for (MyHighlightView view : mOverlayViews) {
            if (getScale() != 1) {
                float[] mvalues = new float[9];
                getImageMatrix().getValues(mvalues);
                final float scale = mvalues[Matrix.MSCALE_X];

                if (!mScaleWithContent)
                    view.getCropRectF().offset(-deltaX / scale, -deltaY / scale);
            }

            view.getMatrix().set(getImageMatrix());
            view.invalidate();
        }
    }

    @Override
    protected void postScale(float scale, float centerX, float centerY) {

        if (mOverlayViews.size() > 0) {
            Iterator<MyHighlightView> iterator = mOverlayViews.iterator();

            Matrix oldMatrix = new Matrix(getImageViewMatrix());
            super.postScale(scale, centerX, centerY);

            while (iterator.hasNext()) {
                MyHighlightView view = iterator.next();

                if (!mScaleWithContent) {
                    RectF cropRect = view.getCropRectF();
                    RectF rect1 = view.getDisplayRect(oldMatrix, view.getCropRectF());
                    RectF rect2 = view.getDisplayRect(getImageViewMatrix(), view.getCropRectF());

                    float[] mvalues = new float[9];
                    getImageViewMatrix().getValues(mvalues);
                    final float currentScale = mvalues[Matrix.MSCALE_X];

                    cropRect.offset((rect1.left - rect2.left) / currentScale,
                            (rect1.top - rect2.top) / currentScale);
                    cropRect.right += -(rect2.width() - rect1.width()) / currentScale;
                    cropRect.bottom += -(rect2.height() - rect1.height()) / currentScale;

                    view.getMatrix().set(getImageMatrix());
                    view.getCropRectF().set(cropRect);
                } else {
                    view.getMatrix().set(getImageMatrix());
                }
                view.invalidate();
            }
        } else {
            super.postScale(scale, centerX, centerY);
        }
    }

    private void ensureVisible(MyHighlightView hv, float deltaX, float deltaY) {
        RectF r = hv.getDrawRect();
        int panDeltaX1 = 0, panDeltaX2 = 0;
        int panDeltaY1 = 0, panDeltaY2 = 0;

        if (deltaX > 0)
            panDeltaX1 = (int) Math.max(0, getLeft() - r.left);
        if (deltaX < 0)
            panDeltaX2 = (int) Math.min(0, getRight() - r.right);

        if (deltaY > 0)
            panDeltaY1 = (int) Math.max(0, getTop() - r.top);

        if (deltaY < 0)
            panDeltaY2 = (int) Math.min(0, getBottom() - r.bottom);

        int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
        int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy(panDeltaX, panDeltaY);
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {

        // iterate the items and post a single tap event to the selected item
        for (MyHighlightView view : mOverlayViews) {
            if (view.isSelected()) {
                view.onSingleTapConfirmed(e.getX(), e.getY());
                postInvalidate();
            }
        }
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i(LOG_TAG, "onDown");

        mScrollStarted = false;
        mLastMotionScrollX = e.getX();
        mLastMotionScrollY = e.getY();

        // return the item being clicked
        MyHighlightView newSelection = checkSelection(e);
        MyHighlightView realNewSelection = newSelection;

//        if (newSelection == null && mOverlayViews.size() == 1 && mForceSingleSelection) {
//            // force a selection if none is selected, when force single selection is
//            // turned on
//            newSelection = mOverlayViews.get(0);
//        }

        setSelectedHighlightView(newSelection);

        if (realNewSelection != null && mScaleWithContent) {
            RectF displayRect = realNewSelection.getDisplayRect(realNewSelection.getMatrix(),
                    realNewSelection.getCropRectF());
            boolean invalidSize = realNewSelection.getContent().validateSize(displayRect);

            Log.d(LOG_TAG, "invalidSize: " + invalidSize);

            if (!invalidSize) {
                Log.w(LOG_TAG, "drawable too small!!!");

                float minW = realNewSelection.getContent().getMinWidth();
                float minH = realNewSelection.getContent().getMinHeight();

                Log.d(LOG_TAG, "minW: " + minW);
                Log.d(LOG_TAG, "minH: " + minH);

                float minSize = Math.min(minW, minH) * 1.1f;

                Log.d(LOG_TAG, "minSize: " + minSize);

                float minRectSize = Math.min(displayRect.width(), displayRect.height());

                Log.d(LOG_TAG, "minRectSize: " + minRectSize);

                float diff = minSize / minRectSize;

                Log.d(LOG_TAG, "diff: " + diff);

                Log.d(LOG_TAG, "min.size: " + minW + "x" + minH);
                Log.d(LOG_TAG, "cur.size: " + displayRect.width() + "x" + displayRect.height());
                Log.d(LOG_TAG, "zooming to: " + (getScale() * diff));

                zoomTo(getScale() * diff, displayRect.centerX(), displayRect.centerY(),
                        DEFAULT_ANIMATION_DURATION * 1.5f);
                return true;
            }
        }

        if (mOverlayView != null) {
            //通过触摸区域得到Mode
            int edge = mOverlayView.getHit(e.getX(), e.getY());
            if (edge != MyHighlightView.NONE) {
                mOverlayView.setMode((edge == MyHighlightView.MOVE) ? MyHighlightView.MOVE
                        : (edge == MyHighlightView.ROTATE ? MyHighlightView.ROTATE
                        : MyHighlightView.GROW));
                postInvalidate();
                if (mDrawableListener != null) {
                    mDrawableListener.onDown(mOverlayView);
                }
            }
        }

        return super.onDown(e);
    }

    public float getmLastMotionScrollX() {
        return mLastMotionScrollX;
    }

    public float getmLastMotionScrollY() {
        return mLastMotionScrollY;
    }

    @Override
    public boolean onUp(MotionEvent e) {
        Log.i(LOG_TAG, "onUp");

        if (mOverlayView != null) {
            mOverlayView.setMode(MyHighlightView.NONE);
            postInvalidate();
        }
        return super.onUp(e);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(LOG_TAG, "onSingleTapUp");

        if (mOverlayView != null) {

            int edge = mOverlayView.getHit(e.getX(), e.getY());
            if ((edge & MyHighlightView.MOVE) == MyHighlightView.MOVE) {
                if (mDrawableListener != null) {
                    mDrawableListener.onClick(mOverlayView);
                }
                return true;
            }

            mOverlayView.setMode(MyHighlightView.NONE);
            postInvalidate();

            Log.d(LOG_TAG, "selected items: " + mOverlayViews.size());

            if (mOverlayViews.size() != 1) {
                setSelectedHighlightView(null);
            }
        }

        return super.onSingleTapUp(e);
    }

    boolean mScrollStarted;
    float mLastMotionScrollX, mLastMotionScrollY;

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i(LOG_TAG, "onScroll");

        float dx, dy;

        float x = e2.getX();
        float y = e2.getY();

        if (!mScrollStarted) {
            dx = 0;
            dy = 0;
            mScrollStarted = true;
        } else {
            dx = mLastMotionScrollX - x;
            dy = mLastMotionScrollY - y;
        }

        mLastMotionScrollX = x;
        mLastMotionScrollY = y;

        if (mOverlayView != null && mOverlayView.getMode() != MyHighlightView.NONE) {
            mOverlayView.onMouseMove(mOverlayView.getMode(), e2, -dx, -dy);
            postInvalidate();

            if (mDrawableListener != null) {
                mDrawableListener.onMove(mOverlayView);
            }

            if (mOverlayView.getMode() == MyHighlightView.MOVE) {
                if (!mScaleWithContent) {
                    ensureVisible(mOverlayView, distanceX, distanceY);
                }
            }
            return true;
        } else {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(LOG_TAG, "onFling");

        if (mOverlayView != null && mOverlayView.getMode() != MyHighlightView.NONE)
            return false;
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean shouldInvalidateAfter = false;

        for (int i = 0; i < mOverlayViews.size(); i++) {
            canvas.save(Canvas.MATRIX_SAVE_FLAG);

            MyHighlightView current = mOverlayViews.get(i);
            current.draw(canvas);

            // check if we should invalidate again the canvas
            if (!shouldInvalidateAfter) {
                FeatherDrawable content = current.getContent();
                if (content instanceof EditableDrawable) {
                    if (((EditableDrawable) content).isEditing()) {
                        shouldInvalidateAfter = true;
                    }
                }
            }

            canvas.restore();
        }

        if (null != mDropPaint) {
            getDrawingRect(mTempRect);
            canvas.drawRect(mTempRect, mDropPaint);
        }

        if (shouldInvalidateAfter) {
            postInvalidateDelayed(EditableDrawable.CURSOR_BLINK_TIME);
        }
    }

    public void clearOverlays() {
        Log.i(LOG_TAG, "clearOverlays");
        setSelectedHighlightView(null);
        while (mOverlayViews.size() > 0) {
            MyHighlightView hv = mOverlayViews.remove(0);
            hv.dispose();
        }
        mOverlayView = null;
    }

    public boolean addHighlightView(MyHighlightView hv) {
        for (int i = 0; i < mOverlayViews.size(); i++) {
            if (mOverlayViews.get(i).equals(hv))
                return false;
        }
        mOverlayViews.add(hv);
        postInvalidate();

//        if (mOverlayViews.size() == 1) {
//            setSelectedHighlightView(hv);
//        }

        return true;
    }

    public int getHighlightCount() {
        return mOverlayViews.size();
    }

    public MyHighlightView getHighlightViewAt(int index) {
        return mOverlayViews.get(index);
    }

    public boolean removeHightlightView(MyHighlightView view) {
        Log.i(LOG_TAG, "removeHightlightView");
        for (int i = 0; i < mOverlayViews.size(); i++) {
            if (mOverlayViews.get(i).equals(view)) {
                MyHighlightView hv = mOverlayViews.remove(i);
                if (hv.equals(mOverlayView)) {
                    setSelectedHighlightView(null);
                }
                hv.dispose();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onZoomAnimationCompleted(float scale) {
        Log.i(LOG_TAG, "onZoomAnimationCompleted: " + scale);
        super.onZoomAnimationCompleted(scale);

        if (mOverlayView != null) {
            mOverlayView.setMode(MyHighlightView.MOVE);
            postInvalidate();
        }
    }

    public MyHighlightView getSelectedHighlightView() {
        return mOverlayView;
    }

    public void commit(Canvas canvas) {

        MyHighlightView hv;
        for (int i = 0; i < getHighlightCount(); i++) {
            hv = getHighlightViewAt(i);
            FeatherDrawable content = hv.getContent();
            if (content instanceof EditableDrawable) {
                ((EditableDrawable) content).endEdit();
            }

            Matrix rotateMatrix = hv.getCropRotationMatrix();
            Rect rect = hv.getCropRect();

            int saveCount = canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.concat(rotateMatrix);
            content.setBounds(rect);
            content.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    private MyHighlightView checkSelection(MotionEvent e) {
        Iterator<MyHighlightView> iterator = mOverlayViews.iterator();
        MyHighlightView selection = null;
        while (iterator.hasNext()) {
            MyHighlightView view = iterator.next();
            int edge = view.getHit(e.getX(), e.getY());
            if (edge != MyHighlightView.NONE) {
                selection = view;
            }
        }
        return selection;
    }

    public void setSelectedHighlightView(MyHighlightView newView) {

        final MyHighlightView oldView = mOverlayView;

        if (mOverlayView != null && !mOverlayView.equals(newView)) {
            mOverlayView.setSelected(false);
//            Log.e("<<<", "oldView失去焦点");
        }

        if (newView != null) {
            newView.setSelected(true);
//            Log.e("<<<", "newView获得焦点");
        }

        postInvalidate();

        mOverlayView = newView;

        if (mDrawableListener != null) {
            mDrawableListener.onFocusChange(newView, oldView);
        }
    }

}
