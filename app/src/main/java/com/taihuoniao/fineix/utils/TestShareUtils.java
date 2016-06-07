package com.taihuoniao.fineix.utils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class TestShareUtils {
    static Context context;
    private static ImageView img;
    static RoundedImageView userHeadImg;
    static RelativeLayout userRightRelative;
    static TextView userName;
    static TextView userInfo;
    static LinearLayout locationLinear;
    static ImageView locationImg;
    static TextView locationTv;
    static ImageView erweima;
    static TextView line;
    static FrameLayout frameLayout;
    static TextView sceneTitle;
    static TextView desTv;
    static ImageView fiuImg;
    static TextView fiuTv;
    static DisplayImageOptions options500_500,options750_1334;
    static int layout;
    static double bi = 1;

    //根据position动态改变控件的位置
    public static View selectStyle(Context context1, int position, SceneDetails sceneDetails, double b) {
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        context = context1;
        bi = b;
        selectStyle(position);
        View view = View.inflate(context, layout, null);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        img = (ImageView) view.findViewById(R.id.activity_share_img);
        ImageLoader.getInstance().displayImage(sceneDetails.getCover_url(),img,options750_1334);
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
        if (position == 2 || position == 3) {
            setSize(view, position);
        }
        ImageLoader.getInstance().displayImage(sceneDetails.getUser_info().getAvatar_url(), userHeadImg, options500_500);
        userName.setText(sceneDetails.getUser_info().getNickname());
        userInfo.setText(sceneDetails.getUser_info().getSummary());
        locationTv.setText(sceneDetails.getAddress());
        sceneTitle.setText(sceneDetails.getTitle());
        desTv.setText(sceneDetails.getDes());
        selectTitleSize(position);
        return view;
    }

    private static void selectStyle(int position) {
        switch (position) {
            case 1:
                layout = R.layout.view_share_style2;
                break;
            case 2:
                layout = R.layout.view_share_style3;
                break;
            case 3:
                layout = R.layout.view_share_style4;
                break;
            default:
                layout = R.layout.view_share_style1;
                break;
        }
    }

    private static void setSize(View view, int position) {

        userName.setTextColor(context.getResources().getColor(R.color.black));
        userInfo.setTextColor(context.getResources().getColor(R.color.black));
        locationTv.setTextColor(context.getResources().getColor(R.color.black));
        locationImg.setImageResource(R.mipmap.location_height_22px);
        desTv.setTextColor(context.getResources().getColor(R.color.black));
        line.setTextColor(context.getResources().getColor(R.color.black));

    }

    private static void selectTitleSize(int position) {
        SceneTitleSetUtils.setTitle(sceneTitle, frameLayout, 42, 21, bi);
    }


}
