package com.taihuoniao.fineix.utils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
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
public class ShareCJUtils {
    static Context context;
    //    @Bind(R.id.activity_share_user_headimg)
    static RoundedImageView userHeadImg;
    //    @Bind(R.id.activity_share_user_name)
    static TextView userName;
    //    @Bind(R.id.activity_share_user_info)
    static TextView userInfo;
    //    @Bind(R.id.activity_share_location)
    static TextView locationTv;
    //    @Bind(R.id.activity_share_frame)
    static FrameLayout frameLayout;
    //    @Bind(R.id.activity_share_scene_title)
    static TextView sceneTitle;
    //    @Bind(R.id.activity_share_scene_des)
    static TextView desTv;
    static DisplayImageOptions options500_500;
    static int layout;

    //根据position动态改变控件的位置
    public static View selectStyle(RelativeLayout container, int position, SceneDetails sceneDetails) {
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        context = container.getContext();
        selectStyle(position);
        View view = View.inflate(context, layout, null);
        userHeadImg = (RoundedImageView) view.findViewById(R.id.activity_share_user_headimg);
        userName = (TextView) view.findViewById(R.id.activity_share_user_name);
        userInfo = (TextView) view.findViewById(R.id.activity_share_user_info);
        locationTv = (TextView) view.findViewById(R.id.activity_share_location);
        frameLayout = (FrameLayout) view.findViewById(R.id.activity_share_frame);
        sceneTitle = (TextView) view.findViewById(R.id.activity_share_scene_title);
        desTv = (TextView) view.findViewById(R.id.activity_share_scene_des);
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
            case 2:
            case 3:
                layout = R.layout.item_share_style2;
                break;
            case 4:
                layout = R.layout.item_share_style3;
                break;
            case 5:
            case 6:
                layout = R.layout.item_share_style4;
                break;
            default:
                layout = R.layout.item_share_style1;
                break;
        }
    }

    private static void selectTitleSize(int position) {
        switch (position) {
            case 1:
            case 2:
            case 5:
                SceneTitleSetUtils.setTitle(sceneTitle, frameLayout, 21, 21);
                break;
            default:
                SceneTitleSetUtils.setTitle(sceneTitle, frameLayout, 42, 21);
                break;
        }
    }


}
