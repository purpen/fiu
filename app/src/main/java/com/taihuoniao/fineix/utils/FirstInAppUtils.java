package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;

/**
 * Created by taihuoniao on 2016/5/11.
 */
public class FirstInAppUtils {
    public static final int QING = 11111;
    public static final int JING = 22222;
    public static final int FIU = 33333;
    public static final int PIN = 44444;
    public static final int WO = 55555;
    public static final int CREATE = 66666;//第一次创建
    public static final int ADDURL = 777777;//第一次添加链接
    public static final int ALL = 88888;//第一次进入全部情景

    private static int type = 0;//判断第一次进入的哪个界面
    private static Activity activity;
    private static PopupWindow popupWindow;

    public static void showPop(Activity activity1, int type1, View activity_view) {
        activity = activity1;
        type = type1;
        initPop();
//        WindowManager.LayoutParams params = activity1.getWindow().getAttributes();
//        params.alpha = 0.4f;
//        activity1.getWindow().setAttributes(params);
//        activity1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//这行代码可以使window后的所有东西边暗淡
        popupWindow.showAtLocation(activity_view, Gravity.TOP, 0, 0);
    }


    private static void initPop() {
//        WindowManager windowManager = activity.getWindowManager();
//        final View popView = View.inflate(activity, R.layout.pop_first_in, null);
//        TextView titleTv = (TextView) popView.findViewById(R.id.pop_first_in_title);
//        TextView desTv = (TextView) popView.findViewById(R.id.pop_first_in_des);
//        Button btn = (Button) popView.findViewById(R.id.pop_first_in_btn);
        final View popView = View.inflate(activity, R.layout.popup_first, null);
        final ImageView img = (ImageView) popView.findViewById(R.id.popup_first_img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        switch (type) {
//            case QING:
//                img.setImageResource(R.mipmap.first_in_index);
//                img.setTag(1);
//                img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if ((int) (v.getTag()) == 1) {
//                            img.setImageResource(R.mipmap.first_in_index2);
//                            img.setTag(2);
//                        } else if ((int) (v.getTag()) == 2) {
//                            popupWindow.dismiss();
//                        }
//                    }
//                });
//                break;
//            case JING:
//                img.setImageResource(R.mipmap.first_in_find);
//                img.setTag(9);
//                img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if ((int) (v.getTag()) == 9) {
//                            img.setImageResource(R.mipmap.first_in_fiu);
//                            img.setTag(10);
//                        } else if ((int) (v.getTag()) == 10) {
//                            popupWindow.dismiss();
//                        }
//                    }
//                });
//                break;
            case FIU:
                img.setImageResource(R.mipmap.first_in_index3);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
//            case PIN:
//                img.setImageResource(R.mipmap.first_in_wellgood);
//                img.setTag(4);
//                img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if ((int) (v.getTag()) == 4) {
//                            img.setImageResource(R.mipmap.first_in_wellgood2);
//                            img.setTag(5);
//                        } else if ((int) (v.getTag()) == 5) {
//                            img.setImageResource(R.mipmap.first_in_wellgood3);
//                            img.setTag(6);
//                        } else if ((int) (v.getTag()) == 6) {
//                            popupWindow.dismiss();
//                        }
//                    }
//                });
//                break;
//            case WO:
//                img.setImageResource(R.mipmap.first_in_mine);
//                img.setTag(7);
//                img.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if ((int) (v.getTag()) == 7) {
//                            img.setImageResource(R.mipmap.first_in_mine2);
//                            img.setTag(8);
//                        } else if ((int) (v.getTag()) == 8) {
//                            popupWindow.dismiss();
//                        }
//                    }
//                });
//                break;
            case CREATE:
                img.setImageResource(R.mipmap.first_in_create);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
            case ADDURL:
                img.setImageResource(R.mipmap.first_in_url);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
            case ALL:
                img.setImageResource(R.mipmap.first_in_all);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
        }
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
        popupWindow = new PopupWindow(popView, MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenHeight(), true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
//                params.alpha = 1f;
////                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                activity.getWindow().setAttributes(params);

//            }
//        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity,
                R.color.nothing));
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
    }


}
