//package com.taihuoniao.fineix.view;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.TypedArray;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.product.ShopCarActivity;
//
//
///**
// * 可复用标题栏
// * <p/>
// * 添加自定义属性 标题|返回键 slf  ff3366
// */
//public class MyGlobalTitleLayout extends RelativeLayout {
//
//    private ImageView backButton;//标题栏返回按钮
//    private TextView titleText;// 标题栏标题文字
//    private TextView rightButton;//标题栏右侧文字按钮，默认隐藏
//    private ImageView rightImageShareButton;//标题栏右侧分享图标按钮，默认隐藏
//    private ImageView rightImageSetButton;//标题栏右侧设置图标按钮，默认隐藏
//    private ImageView rightImageSearchButton;//标题栏右侧搜索图标按钮，默认显示
//    private ImageView rightImageShopCartButton;//标题栏右侧购物车图标按钮，默认显示
//    private TextView rightImageShopCartCounterButton;//标题栏右侧购物车上的黑色计数图标按钮，默认隐藏
//
//    public MyGlobalTitleLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    private void init(final Context context, AttributeSet attrs) {
//        View view = LayoutInflater.from(context).inflate(
//                //multip为多重的、多样的、许多的之意，在这儿是指该自定义titlebar为可复用的，可多次使用的
//                R.layout.multip_global_title,
//                this,
//                true
//        );
//        String title = null;
//        boolean showBackButton = true;
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GlobalTitleLayout);
//        if (typedArray != null) {
//            title = typedArray.getString(R.styleable.GlobalTitleLayout_global_title_layout_title);
//            if (!TextUtils.isEmpty(title)) {
//                showBackButton = typedArray.getBoolean(R.styleable.GlobalTitleLayout_global_title_layout_show_back, false);
//            }
//        }
//
//
//        if (view != null) {
//            backButton = (ImageView) view.findViewById(R.id.global_title_back);
//            titleText = (TextView) view.findViewById(R.id.global_title_text);
//            rightButton = (TextView) view.findViewById(R.id.global_title_button);
//            rightImageShareButton = (ImageView) view.findViewById(R.id.global_title_image_share);
//            rightImageSetButton = (ImageView) view.findViewById(R.id.global_title_image_set);
//            rightImageSearchButton = (ImageView) view.findViewById(R.id.global_title_image_search);
//            rightImageShopCartButton = (ImageView) view.findViewById(R.id.global_title_image_shopcart);
//            rightImageShopCartCounterButton = (TextView) view.findViewById(R.id.global_title_image_shopcart_counter);
//
//            if (title != null) {
//                setTitle(title);
//            }
//
//            rightImageShopCartButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (context instanceof Activity) {
//                        Activity activity = (Activity) context;
//                        Intent intent;
////                        if ("1".equals(THNApplication.getIsLoginInfo().getIs_login())) {
//                        intent = new Intent(activity, ShopCarActivity.class);
//
////                        } else {
////                            intent=new Intent(activity, OptRegisterLoginActivity.class);
////                        }
//                        activity.startActivity(intent);
//                    }
//                }
//            });
//            rightImageShopCartCounterButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (context instanceof Activity) {
//                        Activity activity = (Activity) context;
//                        Intent intent = new Intent(activity, ShopCarActivity.class);
//                        activity.startActivity(intent);
//                    }
//                }
//            });
//
//            setBackButtonVisibility(showBackButton);
//
//            //为标题栏后退按钮绑定默认监听事件
//            //监听事件默认调用当前Activity 的onBackPressed 方法
//            backButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (context instanceof Activity) {
//                        Activity activity = (Activity) context;
//                        activity.onBackPressed();
//                    }
//                }
//            });
//
//            view.setBackgroundResource(R.color.red);
//        }
//        if (typedArray != null) {
//            typedArray.recycle();
//        }
//
//    }
//
//    /**
//     * 设置标题栏标题文字
//     *
//     * @param title 标题文字
//     */
//    public void setTitle(CharSequence title) {
//
//        if (title != null) {
//            titleText.setText(title);
//        }
//
//    }
//
//    public void setTitleColor(int resid) {
//        titleText.setTextColor(resid);
//    }
//
//    /**
//     * 为右侧文字按钮设置文字并绑定监听器，同时自动显示右侧按钮
//     *
//     * @param buttonText                 右侧按钮文字
//     * @param onRightButtonClickListener 右侧按钮 OnClickListener 监听器
//     */
//    public void setRightButton(
//            CharSequence buttonText,
//            OnClickListener onRightButtonClickListener) {
//        if (onRightButtonClickListener != null && buttonText != null) {
////            rightButton.setVisibility(VISIBLE);
//            rightButton.setText(buttonText);
//            rightButton.setOnClickListener(onRightButtonClickListener);
//        }
//    }
//
//    public void setRightColor(int resid) {
//        rightButton.setTextColor(resid);
//
//    }
//
//    public void setRightButton(CharSequence title) {
//        if (title != null) {
//            rightButton.setText(title);
//        }
//    }
//
//    public void setRightButtomVisible(boolean visible) {
//        if (visible)
//            rightButton.setVisibility(VISIBLE);
//        else
//            rightButton.setVisibility(GONE);
//    }
//
//    /*对如下5个图标按钮的监听事件留到后面做到了搜索、购物车等相应页面的时候再考虑
//    到那时，给它们分别设置监听，可在本类中写，也可到时看情况在别的类中写，监听被点击之后
//    分别可跳转到相应的搜索、购物车等页面*/
//
//    //标题栏右侧搜索图标按钮，默认显示
//    public void setRightSearchButton(boolean visible) {
//        if (visible) {
//            rightImageSearchButton.setVisibility(VISIBLE);
//        } else {
//            rightImageSearchButton.setVisibility(GONE);
//        }
//    }
//
//    /**
//     * 设置是否显示标题栏后退按钮，默认为显示
//     *
//     * @param visible 是否显示标题栏后退按钮
//     */
//    public void setBackButtonVisibility(boolean visible) {
//
//        if (visible) {
//            backButton.setVisibility(VISIBLE);
//        } else {
//            backButton.setVisibility(GONE);
//        }
//    }
//
//    public void setBackImg(int resid) {
//        backButton.setImageResource(resid);
//    }
//
//    //
//    public void setBackButtonListener(OnClickListener listener) {
//        if (listener != null) {
//            backButton.setVisibility(VISIBLE);
//            backButton.setOnClickListener(listener);
//        } else {
//            backButton.setVisibility(GONE);
//        }
//    }
//
//}
