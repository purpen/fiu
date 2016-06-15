package com.taihuoniao.fineix.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SceneDetailsBean;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class ShareCJUtils {
    static Context context;
    //    @Bind(R.id.activity_share_user_headimg)
    static RoundedImageView userHeadImg;
    //    @Bind(R.id.activity_share_user_name)
    static RelativeLayout userRightRelative;
    static TextView userName;
    //    @Bind(R.id.activity_share_user_info)
    static TextView userInfo;
    //    @Bind(R.id.activity_share_location)
    static LinearLayout locationLinear;
    static ImageView locationImg;
    static TextView locationTv;
    //    @Bind(R.id.activity_share_frame)
    static ImageView erweima;
    static TextView line;
    static FrameLayout frameLayout;
    //    @Bind(R.id.activity_share_scene_title)
    static TextView sceneTitle;
    //    @Bind(R.id.activity_share_scene_des)
    static TextView desTv;
    static ImageView fiuImg;
    static TextView fiuTv;
    static DisplayImageOptions options500_500;
    static int layout;
    static double bi = 1;

    //根据position动态改变控件的位置
    public static View selectStyle(RelativeLayout container, int position, SceneDetailsBean sceneDetails, double b) {
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        context = container.getContext();
        Log.e("<<<选择样式", "bi=" + bi);
        bi = b;
        selectStyle(position);
        View view = View.inflate(context, layout, null);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        userHeadImg = (RoundedImageView) view.findViewById(R.id.activity_share_user_headimg);
        userRightRelative = (RelativeLayout) view.findViewById(R.id.activity_share_user_right_relative);
        userName = (TextView) view.findViewById(R.id.activity_share_user_name);
        userInfo = (TextView) view.findViewById(R.id.activity_share_user_info);
        locationLinear = (LinearLayout) view.findViewById(R.id.activity_share_location_linear);
        locationImg = (ImageView) view.findViewById(R.id.activity_share_location_img);
        locationTv = (TextView) view.findViewById(R.id.activity_share_location);
        erweima = (ImageView) view.findViewById(R.id.erweima);
        line = (TextView) view.findViewById(R.id.activity_share_scene_line);
        frameLayout = (FrameLayout) view.findViewById(R.id.activity_share_frame);
        sceneTitle = (TextView) view.findViewById(R.id.activity_share_scene_title);
        desTv = (TextView) view.findViewById(R.id.activity_share_scene_des);
        fiuImg = (ImageView) view.findViewById(R.id.activity_share_fiu_img);
        fiuTv = (TextView) view.findViewById(R.id.activity_share_fiu_tv);
        if (position == 2||position==3) {
            setSize(view, position);
        }
        ImageLoader.getInstance().displayImage(sceneDetails.getData().getUser_info().getAvatar_url(), userHeadImg, options500_500);
        userName.setText(sceneDetails.getData().getUser_info().getNickname());
        userInfo.setText(sceneDetails.getData().getUser_info().getSummary());
        locationTv.setText(sceneDetails.getData().getAddress());
        sceneTitle.setText(sceneDetails.getData().getTitle());
        desTv.setText(sceneDetails.getData().getDes());
        selectTitleSize(position);
        return view;
    }

    private static void selectStyle(int position) {
        switch (position) {
            case 1:
                layout = R.layout.item_share_style2;
                break;
            case 2:
                layout = R.layout.item_share_style3;
                break;
            case 3:
                layout = R.layout.item_share_style4;
                break;
            default:
                layout = R.layout.item_share_style1;
                break;
        }
    }

    private static void setSize(View view, int position) {

            userName.setTextColor(context.getResources().getColor(R.color.black));
            userInfo.setTextColor(context.getResources().getColor(R.color.black969696));
            locationTv.setTextColor(context.getResources().getColor(R.color.black969696));
            locationImg.setImageResource(R.mipmap.location_height_22px);
            desTv.setTextColor(context.getResources().getColor(R.color.black969696));
            line.setTextColor(context.getResources().getColor(R.color.black969696));

//        int padding = DensityUtils.dp2px(context, 22);
//        view.setPadding((int) (padding * bi), (int) (padding * bi), (int) (padding * bi), (int) (padding * bi));
//        ViewGroup.LayoutParams headLp = userHeadImg.getLayoutParams();
//        headLp.width = (int) (DensityUtils.dp2px(context, 30) * bi);
//        headLp.height = headLp.width;
//        userHeadImg.setLayoutParams(headLp);
//        LinearLayout.LayoutParams reLp = (LinearLayout.LayoutParams) userRightRelative.getLayoutParams();
//        reLp.height = (int) (DensityUtils.dp2px(context, 30) * bi);
//        reLp.leftMargin = (int) (DensityUtils.dp2px(context, 8) * bi);
//        userRightRelative.setLayoutParams(reLp);
//        userName.setTextSize((float) (DensityUtils.sp2px(context, 11) * bi));
//        userInfo.setTextSize((float) (DensityUtils.sp2px(context, 10) * bi));
//        RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) locationLinear.getLayoutParams();
//        lLp.topMargin = (int) (DensityUtils.dp2px(context, 10) * bi);
//        locationLinear.setLayoutParams(lLp);
//        locationImg.getLayoutParams().height = (int) (DensityUtils.dp2px(context, 11) * bi);
//        LinearLayout.LayoutParams locationLp = (LinearLayout.LayoutParams) locationTv.getLayoutParams();
//        locationLp.leftMargin = (int) (DensityUtils.dp2px(context, 8) * bi);
//        locationTv.setLayoutParams(locationLp);
//        locationTv.setTextSize((float) (DensityUtils.sp2px(context, 9) * bi));
//        erweima.getLayoutParams().width = (int) (DensityUtils.dp2px(context, 62) * bi);
//        erweima.getLayoutParams().height = (int) (DensityUtils.dp2px(context, 62) * bi);
//        RelativeLayout.LayoutParams desLp = (RelativeLayout.LayoutParams) desTv.getLayoutParams();
//        desLp.setMargins(0, (int) (DensityUtils.dp2px(context, 10) * bi), (int) (DensityUtils.dp2px(context, 15) * bi), 0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            desLp.setMarginEnd((int) (DensityUtils.dp2px(context, 15) * bi));
//        }
//        desTv.setLayoutParams(desLp);
//        desTv.setTextSize((float) (DensityUtils.sp2px(context, 10) * bi));
//        RelativeLayout.LayoutParams lineLp = (RelativeLayout.LayoutParams) line.getLayoutParams();
//        lineLp.setMargins(0, (int) (DensityUtils.dp2px(context, 10) * bi), (int) (DensityUtils.dp2px(context, 15) * bi), 0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            lineLp.setMarginEnd((int) (DensityUtils.dp2px(context, 15) * bi));
//        }
//        line.setTextSize((float) (DensityUtils.sp2px(context, 9) * bi));
//        RelativeLayout.LayoutParams frameLp = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
//        frameLp.topMargin = (int) (DensityUtils.dp2px(context, 7) * bi);
//        frameLayout.setLayoutParams(frameLp);
//        fiuImg.getLayoutParams().height = (int) (DensityUtils.dp2px(context, 20) * bi);
//        LinearLayout.LayoutParams fiuLp = (LinearLayout.LayoutParams) fiuTv.getLayoutParams();
//        fiuLp.topMargin = (int) (DensityUtils.dp2px(context, 10) * bi);
//        fiuTv.setLayoutParams(fiuLp);
//        fiuTv.setTextSize((float) (DensityUtils.sp2px(context, 9) * bi));
    }

    private static void selectTitleSize(int position) {
        SceneTitleSetUtils.setTitle(sceneTitle, frameLayout, 42, 21, bi);
//        switch (position) {
//            case 1:
//            case 2:
//            case 5:
//                SceneTitleSetUtils.setTitle(sceneTitle, frameLayout, 21, 21, 1);
//                break;
//            default:
//
//                break;
//        }
    }


}
