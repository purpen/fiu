package com.dodola.bubblecloud;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dodola on 15/7/23.
 */
public class BubbleCloudView<T extends Adapter> extends AdapterView<T> {

    private T mAdapter;

    private int lastX;
    private int lastY;
    private int deltaX;
    private int deltaY;
    private int scrollMoveX;
    private int scrollMoveY;
    private int scrollX;
    private int scrollY;


    private int mTouchState = TOUCH_STATE_RESTING;
    private static final int TOUCH_STATE_RESTING = 0;
    private static final int TOUCH_STATE_CLICK = 1;
    private static final int TOUCH_STATE_SCROLL = 2;
    private static final int INVALID_INDEX = -1;
    private static final int TOUCH_SCROLL_THRESHOLD = 10;

    private Rect mRect;
    private int screenW;
    private int screenH;
    private int centerW;
    private int centerH;
    private ArrayList<Integer[]> hexCube;
    private ArrayList<XY> hexCubeOrtho = new ArrayList<>();
    private ArrayList<Rrad> hexCubePolar = new ArrayList<>();
    private ArrayList<Rdr> hexCubeSphere = new ArrayList<>();
    private int sphereR;
    private int hexR;
    private int itemSize;
    private int edgeSize;
    private float animAlpha = 1;
    int scrollRangeX = 30;
    int scrollRangeY = 10;

    private void startTouch(final MotionEvent event) {
        lastX = (int) event.getX();
        lastY = (int) event.getY();
        deltaX = 0;
        deltaY = 0;
        scrollMoveX += deltaX;
        scrollMoveY += deltaY;
        scrollX = scrollMoveX;
        scrollY = scrollMoveY;
        mTouchState = TOUCH_STATE_CLICK;
    }

    private void endTouch() {
        int veloX = deltaX;
        int veloY = deltaY;
        final int distanceX = veloX * 10;
        final int distanceY = veloY * 10;

        ValueAnimator endAnim = ValueAnimator.ofFloat(0, 1);
        endAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.e("<<<进入几次","进入几次");
                scrollMoveX = scrollX;
                scrollMoveY = scrollY;
                int steps = 16;
                int step = (int) (steps * animation.getAnimatedFraction());
//                Log.e("<<<动画", animation.getAnimatedFraction() + "");
                int inertiaX = (int) (easeOutCubic
                        (step, 0, distanceX, steps) - easeOutCubic((step - 1), 0, distanceX, steps));
                int inertiaY = (int) (easeOutCubic
                        (step, 0, distanceY, steps) - easeOutCubic((step - 1), 0, distanceY, steps));

                scrollX += inertiaX;
                scrollY += inertiaY;
//                for(int i=0;i<getChildCount();i++){
//                    getChildAt(i).getTop()
//                }
                double r = Math.sqrt(((double) getChildCount() - 1) / 3 + 0.25) + 0.5;
                int intR = 0;
                if (r * 10 % 10 != 0) {
                    intR = 1 + (int) r;
                } else {
                    intR = (int) r;
                }
                if (scrollX > (intR - 1) * itemSize) {
                    scrollX -= (scrollX - (intR - 1) * itemSize) / 4;
                } else if (scrollX < (1 - intR) * itemSize) {
                    scrollX -= (scrollX - (1 - intR) * itemSize) / 4;
                }
                if (scrollY > Math.sqrt(3) / 2 * (intR - 1) * itemSize) {
                    scrollY -= (scrollY - (Math.sqrt(3) / 2 * (intR - 1) * itemSize)) / 4;
                } else if (scrollY < Math.sqrt(3) / 2 * (1 - intR) * itemSize) {
                    scrollY -= (scrollY - (Math.sqrt(3) / 2 * (1 - intR) * itemSize)) / 4;
                }
//                if (scrollX > scrollRangeX) {
//                    scrollX -= (scrollX - scrollRangeX) / 4;
//                } else if (scrollX < -scrollRangeX) {
//                    scrollX -= (scrollX + scrollRangeX) / 4;
//                }
//
//                if (scrollY > scrollRangeY) {
//                    scrollY -= (scrollY - scrollRangeY) / 4;
//                } else if (scrollY < -scrollRangeY) {
//                    scrollY -= (scrollY + scrollRangeY) / 4;
//                }
//                Log.e("<<<动画", "scrollX=" + scrollX + ",scrollY=" + scrollY);
                iconMapRefresh(sphereR, hexR, scrollX, scrollY);
                requestLayout();

            }
        });
        endAnim.setDuration(300);
        endAnim.start();

        mTouchState = TOUCH_STATE_RESTING;
    }


    public BubbleCloudView(Context context) {
        super(context);
        init();
    }

    public BubbleCloudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleCloudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public T getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(T adapter) {
        mAdapter = adapter;
        removeAllViewsInLayout();
        requestLayout();
    }

    @Override
    public View getSelectedView() {
        return null;

    }

    @Override
    public void setSelection(int position) {

    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(event);
                return false;

            case MotionEvent.ACTION_MOVE:
                return startScrollIfNeeded(event);

            default:
//                回弹效果，待调整
//                double r = Math.sqrt(((double) getChildCount() - 1) / 3 + 0.25) + 0.5;
//                int intR = 0;
//                if (r * 10 % 10 != 0) {
//                    intR = 1 + (int) r;
//                } else {
//                    intR = (int) r;
//                }
//                if (scrollX > (intR - 1) * itemSize) {
//                    endTouch();
//                } else if (scrollX < (1 - intR) * itemSize) {
//                    endTouch();
//                }
//                if (scrollY > Math.sqrt(3) / 2 * (intR - 1) * itemSize) {
//                    endTouch();
//                } else if (scrollY < Math.sqrt(3) / 2 * (1 - intR) * itemSize) {
//                    endTouch();
//                }
//                endTouch();
                return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double r = Math.sqrt(((double) getChildCount() - 1) / 3 + 0.25) + 0.5;
        int intR = 0;
        if (r * 10 % 10 != 0) {
            intR = 1 + (int) r;
        } else {
            intR = (int) r;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(event);
                break;

            case MotionEvent.ACTION_MOVE:
//                Log.d("BubbleCloudView", "===ActionMove===");

                if (mTouchState == TOUCH_STATE_CLICK) {
                    startScrollIfNeeded(event);
                }
                if (mTouchState == TOUCH_STATE_SCROLL) {
                    scrollContainer((int) event.getX(), (int) event.getY());
//                    Log.d("BubbleCloudView", "===mTouchScroll===");
                    Log.e("<<<appleWatchView滑动", "scrollX=" + scrollX + ",scrollY" + scrollY + ",scrollMoveX=" + scrollMoveX + ",scrollMoveY=" + scrollMoveY +
                            ",scrollRangeX=" + scrollRangeX + ",scrollRangeY=" + scrollRangeY);
                }
//                for (int i = 0; i < getChildCount(); i++) {
//                    if (getChildAt(i).getTop() < mTop) {
//                        mTop = getChildAt(i).getTop();
//                        topIndex = i;
//                    }
//                    if (getChildAt(i).getRight() > mRight) {
//                        mRight = getChildAt(i).getRight();
//                        rightIndex = i;
//                    }
//                    if (getChildAt(i).getBottom() > mBottom) {
//                        mBottom = getChildAt(i).getBottom();
//                        bottomIndex = i;
//                    }
//                    if (getChildAt(i).getLeft() < mLeft) {
//                        mLeft = getChildAt(i).getLeft();
//                        leftIndex = i;
//                    }
//                }
//                Log.e("<<<边界", "top=" + mTop + ",right=" + mRight + ",bottom=" + mBottom + ",left=" + mLeft+",scrollY="+getScrollY()+",scrollX="+getScrollX());

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mTouchState == TOUCH_STATE_CLICK) {
                    clickChildAt((int) event.getX(), (int) event.getY());
                }
                //                Log.e("<<<appleWatchView抬起", "scrollX=" + scrollX + ",scrollY" + scrollY + ",scrollMoveX=" + scrollMoveX + ",scrollMoveY=" + scrollMoveY +
//                        ",scrollRangeX=" + scrollRangeX + ",scrollRangeY=" + scrollRangeY);
//                endTouch();

//                if (scrollX > (intR - 1) * itemSize) {
//                    endTouch();
//                } else if (scrollX < (1 - intR) * itemSize) {
//                    endTouch();
//                }
//                if (scrollY > Math.sqrt(3) / 2 * (intR - 1) * itemSize) {
//                    endTouch();
//                } else if (scrollY < Math.sqrt(3) / 2 * (1 - intR) * itemSize) {
//                    endTouch();
//                }
                break;

            default:
//                endTouch();
//                if (scrollX > (intR - 1) * itemSize) {
//                    endTouch();
//                } else if (scrollX < (1 - intR) * itemSize) {
//                    endTouch();
//                }
//                if (scrollY > Math.sqrt(3) / 2 * (intR - 1) * itemSize) {
//                    endTouch();
//                } else if (scrollY < Math.sqrt(3) / 2 * (1 - intR) * itemSize) {
//                    endTouch();
//                }
                break;
        }
        return true;

    }

    private int mTop = Integer.MAX_VALUE, mLeft = Integer.MAX_VALUE, mRight, mBottom;
    private int topIndex, leftIndex, rightIndex, bottomIndex;

    private boolean startScrollIfNeeded(final MotionEvent event) {
        final int xPos = (int) event.getX();
        final int yPos = (int) event.getY();
        if (xPos < lastX - TOUCH_SCROLL_THRESHOLD
                || xPos > lastX + TOUCH_SCROLL_THRESHOLD
                || yPos < lastY - TOUCH_SCROLL_THRESHOLD
                || yPos > lastY + TOUCH_SCROLL_THRESHOLD) {
            mTouchState = TOUCH_STATE_SCROLL;
            return true;
        }
        return false;
    }

    private void scrollContainer(int x, int y) {


        deltaX = x - lastX;
        deltaY = y - lastY;

        lastX = x;
        lastY = y;

        scrollMoveX += deltaX;
        scrollMoveY += deltaY;

        scrollX = scrollMoveX;
        scrollY = scrollMoveY;


        if (scrollMoveX > scrollRangeX) {
            scrollX = scrollRangeX + (scrollMoveX - scrollRangeX) / 2;
        } else if (scrollX < -scrollRangeX) {
            scrollX = -scrollRangeX + (scrollMoveX + scrollRangeX) / 2;
        }

        if (scrollMoveY > scrollRangeY) {
            scrollY = scrollRangeY + (scrollMoveY - scrollRangeY) / 2;
        } else if (scrollY < -scrollRangeY) {
            scrollY = -scrollRangeY + (scrollMoveY + scrollRangeY) / 2;
        }


        iconMapRefresh(sphereR, hexR,
                scrollX,
                scrollY
        );
        requestLayout();
    }

    private void clickChildAt(final int x, final int y) {
        Log.e("<<<bubble点击", "x=" + x + ",y=" + y);
        final int index = getContainingChildIndex(x, y);
        Log.e("<<<bubble角标", "index=" + index + ",INVALID_INDEX=" + INVALID_INDEX);
        if (index != INVALID_INDEX) {
            final View itemView = getChildAt(index);
            final long id = mAdapter.getItemId(index);
            performItemClick(itemView, index, id);
        }
    }

    private int getContainingChildIndex(final int x, final int y) {
        if (mRect == null) {
            mRect = new Rect();
        }
        for (int index = 0; index < getChildCount(); index++) {
            getChildAt(index).getHitRect(mRect);
            if (mRect.contains(x, y)) {
                return index;
            }
        }
        return INVALID_INDEX;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mAdapter == null) {
            return;
        }

        if (getChildCount() == 0) {
            int position = 0;
            while (position < mAdapter.getCount()) {
                View newBottomChild = mAdapter.getView(position, null, this);
                addAndMeasureChild(newBottomChild);
                position++;
            }
        }

        positionItems();
    }

    private void addAndMeasureChild(View child) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        addViewInLayout(child, -1, params, true);
        child.measure(MeasureSpec.EXACTLY | itemSize, MeasureSpec.EXACTLY | itemSize);
    }

    private void positionItems() {
//        Log.e("<<<AppleWatch界面", "子view个数" + getChildCount());
        try {
            for (int index = 0; index < getChildCount(); index++) {
                View child = getChildAt(index);

                final XY xy = hexCubeOrtho.get(index);

                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int offsetX = centerW - edgeSize;
                int offsetY = centerH - edgeSize;

                child.layout((int) xy.x + offsetX, (int) xy.y + offsetY, (int) xy.x + offsetX + width, (int) xy.y + offsetY + height);
                child.setScaleX(xy.scale);
                child.setScaleY(xy.scale);
                child.setAlpha(animAlpha);
            }
        } catch (IndexOutOfBoundsException e) {
        }

    }

    private void init() {
        this.hexCube = new ArrayList<>();
        for (int i = 0; i < 20; i++)//i表示六边形的边长。大了没事。小了会限制个数
            for (int j = -i; j <= i; j++)
                for (int k = -i; k <= i; k++)
                    for (int l = -i; l <= i; l++)
                        if (Math.abs(j) + Math.abs(k) + Math.abs(l) == i * 2 && j + k + l == 0) {
                            final Integer[] integers = {j, k, l};
                            this.hexCube.add(integers);
                            //这个里面放的是控件的位置信息
                        }
//        this.screenW = getResources().getDimensionPixelSize(R.dimen.screenw);
        this.screenH = getResources().getDimensionPixelSize(R.dimen.screenh);
        this.screenW = getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.dp30);

        this.sphereR = getResources().getDimensionPixelSize(R.dimen.sphereR);
        this.hexR = getResources().getDimensionPixelSize(R.dimen.hexR);//两个圆之间的中心距离
        this.itemSize = getResources().getDimensionPixelSize(R.dimen.item_size);
        this.edgeSize = getResources().getDimensionPixelSize(R.dimen.edge_size);
        this.centerW = this.screenW / 2;
        this.centerH = this.screenH / 2;
        Log.e("<<<bubble控件", "screenW=" + screenW + ",screenH=" + screenH + ",sphereR=" + sphereR + ",hexR=" + hexR + ",itemSize=" + itemSize
                + ",edgeSize=" + edgeSize);
        //是圆与圆之间的距离变大
        iconMapRefresh(sphereR, hexR + 10,
                0,
                0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //开启动画
        startEnterAnim();
    }

    private void iconMapRefresh(float sphereR, float hexR, float scrollX, float scrollY) {
//        Log.d("BubbleCloudView", "sphereR:" + sphereR + ",hexR:" + hexR + ",scrollX:" + scrollX + ",scrollY:" + scrollY);

        hexCubeOrtho.clear();
        hexCubePolar.clear();
        hexCubeSphere.clear();

        for (int i = 0; i < hexCube.size(); i++) {
            final Integer[] integers = this.hexCube.get(i);
//            double[] doubles = hexCube.get(i);
            XY tempxy = new XY();
            tempxy.x = (integers[1] + integers[0] / 2f) * hexR + scrollX;
            tempxy.y = (float) (Math.sqrt(3) / 2 * integers[0] * hexR + scrollY);
            hexCubeOrtho.add(tempxy);
        }

        for (int i = 0; i < hexCubeOrtho.size(); i++) {
            final XY hexCubexy = hexCubeOrtho.get(i);
            hexCubePolar.add(ortho2polar(hexCubexy.x, hexCubexy.y));
        }


        for (int i = 0; i < hexCubePolar.size(); i++) {

            final Rrad rrad = hexCubePolar.get(i);

            float rad = rrad.r / sphereR;
            float r, deepth;
            if (rad < Math.PI / 2) {
                r = rrad.r * swing((float) (rad / (Math.PI / 2)), 1.5f, -0.5f, 1f);
                deepth = easeInOutCubic((float) (rad / (Math.PI / 2)), 1f, -0.5f, 1f);
            } else {
                r = rrad.r;
                deepth = easeInOutCubic(1f, 1f, -0.5f, 1f);
            }

            Rdr rdr = new Rdr();
            rdr.r = r;
            rdr.deepth = deepth;
            rdr.rad = rrad.rad;
            hexCubeSphere.add(rdr);
        }
//        Log.d("BubbleCloudView", "iconMapRefresh:resultMap1====" + hexCubeSphere);

        hexCubeOrtho.clear();
        for (int i = 0; i < hexCubeSphere.size(); i++) {
            final Rdr rdr = hexCubeSphere.get(i);
            hexCubeOrtho.add(polar2ortho(rdr.r, rdr.rad));
        }

        for (int i = 0; i < hexCubeOrtho.size(); i++) {
            final XY xy = hexCubeOrtho.get(i);
            xy.x = Math.round(xy.x * 10) / 10;
            xy.y = (float) (Math.round(xy.y * 10) / 10 * 1.14);
        }


        final float edge = edgeSize;
        //半径范围控制
        for (int i = 0; i < hexCubeOrtho.size(); i++) {
            final XY xy = hexCubeOrtho.get(i);
            final Rdr rdr = hexCubeSphere.get(i);
            if (Math.abs(xy.x) > this.screenW / 2 - edge || Math.abs(xy.y) > this.screenH / 2 - edge) {
                xy.scale = rdr.deepth * 0.4f;
            } else if (Math.abs(xy.x) > this.screenW / 2 - 2 * edge && Math.abs(xy.y) > this.screenH / 2 - 2 * edge) {
                xy.scale = Math.min(rdr.deepth * easeInOutSine(this.screenW / 2 - Math.abs(xy.x) - edge, 0.4f, 0.6f, edge), rdr.deepth * easeInOutSine(this.screenH / 2 - Math.abs(xy.y) - edge, 0.3f, 0.7f, edge));
            } else if (Math.abs(xy.x) > this.screenW / 2 - 2 * edge) {
                xy.scale = rdr.deepth * easeOutSine(this.screenW / 2 - Math.abs(xy.x) - edge, 0.4f, 0.6f, edge);
            } else if (Math.abs(xy.y) > this.screenH / 2 - 2 * edge) {
                xy.scale = rdr.deepth * easeOutSine(this.screenH / 2 - Math.abs(xy.y) - edge, 0.4f, 0.6f, edge);
            } else {
                xy.scale = rdr.deepth;
            }
//            Log.e("<<<scale", "i=" + i + ",scale=" + xy.scale);
        }

        for (int i = 0; i < hexCubeOrtho.size(); i++) {
            final XY xy = hexCubeOrtho.get(i);
            if (xy.x < -this.screenW / 2 + 2 * edge) {
                xy.x += easeInSine(this.screenW / 2 - Math.abs(xy.x) - 2 * edge, 0, 6f, 2 * edge);
            } else if (xy.x > this.screenW / 2 - 2 * edge) {
                xy.x += easeInSine(this.screenW / 2 - Math.abs(xy.x) - 2 * edge, 0, -6f, 2 * edge);
            }
            if (xy.y < -this.screenH / 2 + 2 * edge) {
                xy.y += easeInSine(this.screenH / 2 - Math.abs(xy.y) - 2 * edge, 0, 8f, 2 * edge);
            } else if (xy.y > this.screenH / 2 - 2 * edge) {
                xy.y += easeInSine(this.screenH / 2 - Math.abs(xy.y) - 2 * edge, 0, -8f, 2 * edge);
            }
        }
//        Log.d("BubbleCloudView", "iconMapRefresh:resultMap2====" + hexCubeOrtho);

    }

    private float easeInSine(float t, float b, float c, float d) {
        return (float) (-c * Math.cos(t / d * (Math.PI / 2)) + c + b);
    }

    private float swing(float t, float b, float c, float d) {
        return -c * (t /= d) * (t - 2) + b;
    }

    private float easeInOutCubic(float t, float b, float c, float d) {
        if ((t /= d / 2) < 1)
            return c / 2 * t * t * t + b;
        return c / 2 * ((t -= 2) * t * t + 2) + b;
    }

    private float easeInOutSine(float t, float b, float c, float d) {
        return (float) (-c / 2 * (Math.cos(Math.PI * t / d) - 1) + b);
    }

    private float easeOutSine(float t, float b, float c, float d) {
        return (float) (c * Math.sin(t / d * (Math.PI / 2)) + b);
    }

    private XY polar2ortho(float r, float rad) {
        float x = (float) (r * Math.cos(rad));
        float y = (float) (r * Math.sin(rad));
        XY tempxy = new XY();
        tempxy.x = x;
        tempxy.y = y;
        return tempxy;
    }

    private Rrad ortho2polar(float x, float y) {
        float r = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        float rad = (float) Math.atan2(y, x);
        Rrad temprad = new Rrad();
        temprad.r = r;
        temprad.rad = rad;
        return temprad;
    }

    private void startEnterAnim() {
        ValueAnimator startAnim = ValueAnimator.ofFloat(0, 1);
        startAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float v = easeOutCubic((float) animation.getAnimatedValue() * 36, hexR * 2, -hexR, 36f);
                iconMapRefresh(sphereR, v, 0, 0);
                animAlpha = animation.getAnimatedFraction();
                requestLayout();
            }
        });
        startAnim.setDuration(1000);
        startAnim.start();
    }

    private float easeOutCubic(float t, float b, float c, float d) {
        return c * ((t = t / d - 1) * t * t + 1) + b;
    }

    private class XY {
        //圆的中心点的坐标
        float x, y, scale;

        @Override
        public String toString() {
            return "XY{" +
                    "scale=" + scale +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private class Rrad {
        float r, rad;

        @Override
        public String toString() {
            return "Rrad{" +
                    "r=" + r +
                    ", rad=" + rad +
                    '}';
        }
    }

    private class Rdr {
        float r, deepth, rad;

        @Override
        public String toString() {
            return "Rdr{" +
                    "deepth=" + deepth +
                    ", r=" + r +
                    ", rad=" + rad +
                    '}';
        }
    }


    /**
     * @param num 传入的行数，必须为双数
     * @return 根据传入的数字返回坐标的数组list
     */
    private List<double[]> num2List(int num) {
        List<double[]> list = new ArrayList<>();
        if (num <= 0 || num % 2 != 0) {
            return list;
        }
//        list.addAll(twoLineOver0List());
        int howMany = num / 2;
        for (int i = 0; i < howMany; i++) {
            if (i % 2 == 0) {
                List<double[]> list1 = changeY(twoLineOver0List(), Math.sqrt(3) * i / 2);
                list.addAll(list1);
            } else {
                List<double[]> list1 = changeY(twoLineBelow0List(), -Math.sqrt(3) * (i + 1) / 2);
                list.addAll(list1);
            }
        }
        return list;
    }

    //在纵坐标上加减
    private List<double[]> changeY(List<double[]> list, double dy) {
        for (int i = 0; i < list.size(); i++) {
            double[] doubles = list.get(i);
            doubles[1] += dy;
        }
        return list;
    }

    //获取两行状态下的坐标,纵坐标超过0的情况
    private List<double[]> twoLineOver0List() {
        List<double[]> list = new ArrayList<>();
        list.add(new double[]{0, 0});
        list.add(new double[]{-1, 0});
        list.add(new double[]{1, 0});
        list.add(new double[]{-2, 0});
        list.add(new double[]{2, 0});
        list.add(new double[]{-0.5, Math.sqrt(3) / 2});
        list.add(new double[]{0.5, Math.sqrt(3) / 2});
        list.add(new double[]{-1.5, Math.sqrt(3) / 2});
        list.add(new double[]{1.5, Math.sqrt(3) / 2});
        return list;
    }

    //获取两行状态下的坐标,纵坐标小于0的情况
    private List<double[]> twoLineBelow0List() {
        List<double[]> list = new ArrayList<>();
        list.add(new double[]{-0.5, Math.sqrt(3) / 2});
        list.add(new double[]{0.5, Math.sqrt(3) / 2});
        list.add(new double[]{-1.5, Math.sqrt(3) / 2});
        list.add(new double[]{1.5, Math.sqrt(3) / 2});
        list.add(new double[]{0, 0});
        list.add(new double[]{-1, 0});
        list.add(new double[]{1, 0});
        list.add(new double[]{-2, 0});
        list.add(new double[]{2, 0});
        return list;
    }
}
