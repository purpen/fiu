package com.taihuoniao.fineix.scene.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AlbumGridAdapter;
import com.taihuoniao.fineix.adapters.AlbumListAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.AlbumBean;
import com.taihuoniao.fineix.beans.PhotoItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.scene.PictureEditActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.ImageCrop.ClipImageLayout;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PHONE_STATE_CODE;

/**
 * Created by taihuoniao on 2016/8/28.
 */
public class PictureFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {
    private static final int REQUEST_CODE = 100;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.container)
    LinearLayout container;
    @Bind(R.id.clip_img)
    ClipImageLayout clipImg;
    @Bind(R.id.arrow_container)
    RelativeLayout arrowContainer;
    @Bind(R.id.arrow_img)
    ImageView arrowImg;
    @Bind(R.id.grid_view)
    GridView gridView;
    @Bind(R.id.list_view)
    ListView listView;
    private AlbumGridAdapter albumGridAdapter;
    private Map<String, AlbumBean> albumList;
    private List<String> albumPaths;
    private boolean isUp = true;// 相册列表显示为false 不显示为ture
    private PhotoItem photoItem;//当前显示的photo


    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_picture, null);
        ButterKnife.bind(this, view);
        titleLayout = (GlobalTitleLayout) view.findViewById(R.id.fragment_photo_titlelayout);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.height = MainApplication.getContext().getScreenHeight() - DensityUtils.dp2px(getActivity(), 90);
        listView.setLayoutParams(layoutParams);
        listView.setTranslationY(layoutParams.height);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) clipImg.getLayoutParams();
        layoutParams1.width = MainApplication.getContext().getScreenWidth();
        layoutParams1.height = layoutParams1.width;
        clipImg.setLayoutParams(layoutParams1);
        return view;
    }

    @Override
    protected void initList() {
        titleLayout.setBackgroundResource(R.color.title_black);
        titleLayout.setBackImgVisible(false);
        titleLayout.setCancelImgVisible(true);
        titleLayout.setArrowImgVisible(true);
        titleLayout.setCancelListener(this);
        titleLayout.setBackListener(this);
        titleLayout.setTitleLinearListener(this);
        titleLayout.setContinueListener(this);
        albumPaths = new ArrayList<>();
        if (AndPermission.hasPermission(activity,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            initAlbumList();
        }else {
            // 申请权限。
            AndPermission.with(this)
                    .requestCode(REQUEST_PHONE_STATE_CODE)
                    .permission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .send();
        }
    }

    private void initAlbumList(){
        albumList = findGalleries(getActivity());
        arrowContainer.setOnTouchListener(this);
        if (albumList.containsKey(MainApplication.systemPhotoPath)) {
            titleLayout.setTitle(R.string.film_album);
            albumGridAdapter = new AlbumGridAdapter(getActivity(), albumList.get(MainApplication.systemPhotoPath).getPhotos());
        } else {
            if (albumList.size() > 0 && albumPaths.size() > 0) {
                titleLayout.setTitle(albumList.get(albumPaths.get(0)).getTitle());
                albumGridAdapter = new AlbumGridAdapter(getActivity(), albumList.get(albumPaths.get(0)).getPhotos());
            }
        }
        AlbumListAdapter albumListAdapter = new AlbumListAdapter(getActivity(), albumPaths, albumList);
        listView.setAdapter(albumListAdapter);
        listView.setOnItemClickListener(this);
        gridView.setAdapter(albumGridAdapter);
        gridView.setOnItemClickListener(this);
        selectImg(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(REQUEST_CODE)
    private void getRequestYes(List<String> grantedPermissions) {
        initAlbumList();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(REQUEST_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this,REQUEST_CODE_SETTING).show();
        }else {
            activity.finish();
        }
    }


    @Override
    protected void requestNet() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_cancel:
                getActivity().finish();
                break;
            case R.id.title_back:
            case R.id.title_linear:
                if (isUp) {
                    isUp = false;
                    ObjectAnimator.ofFloat(listView, "translationY", 0).setDuration(500).start();
                    titleLayout.setArrowImgResource(R.mipmap.arrow_up_white);
                    titleLayout.setContinueTvVisible(false);
                    titleLayout.setBackImgVisible(true);
                    titleLayout.setCancelImgVisible(false);
                } else {
                    isUp = true;
                    ObjectAnimator.ofFloat(listView, "translationY", listView.getMeasuredHeight()).setDuration(500).start();
                    titleLayout.setArrowImgResource(R.mipmap.arrow_down_white);
                    titleLayout.setContinueTvVisible(true);
                    titleLayout.setBackImgVisible(false);
                    titleLayout.setCancelImgVisible(true);
                }
                break;
            case R.id.title_continue:
                if (photoItem == null) {
                    ToastUtils.showError("请选择一张照片");
                    return;
                }
                //剪切完的图片
                MainApplication.cropBitmap = clipImg.clip();
//                ImageUtils.location = ImageUtils.picLocation(photoItem.getImageUri());
                Intent intent = new Intent(activity, PictureEditActivity.class);
                activity.startActivity(intent);
                break;
        }
    }

    public Map<String, AlbumBean> findGalleries(Context context) {
        albumPaths.clear();
        albumPaths.add(MainApplication.systemPhotoPath);
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED};
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,//指定所要查询的字段
                MediaStore.Images.Media.SIZE + ">?",//查询条件
                new String[]{"1"}, //查询条件中问号对应的值
                MediaStore.Images.Media.DATE_ADDED + " desc");
        Map<String, AlbumBean> galleries = new HashMap<>();
        if (cursor == null) {
            return galleries;
        }
        cursor.moveToFirst();
        //文件夹照片
        while (cursor.moveToNext()) {
            String data = cursor.getString(1);
            if (data.lastIndexOf("/") < 1) {
                continue;
            }
            String sub = data.substring(0, data.lastIndexOf("/"));

            if (!galleries.keySet().contains(sub)) {
                String name = sub.substring(sub.lastIndexOf("/") + 1, sub.length());
                if (!albumPaths.contains(sub)) {
                    albumPaths.add(sub);
                }
                galleries.put(sub, new AlbumBean(name, sub, new ArrayList<PhotoItem>()));
            }
            galleries.get(sub).getPhotos().add(new PhotoItem(cursor.getLong(0), data, (long) (cursor.getInt(2)) * 1000));
//            }
        }
        //系统相机照片
        ArrayList<PhotoItem> sysPhotos = FileUtils.findPicsInDir(MainApplication.systemPhotoPath);
        if (!sysPhotos.isEmpty()) {
            galleries.put(MainApplication.systemPhotoPath, new AlbumBean("Camera", MainApplication.systemPhotoPath, sysPhotos));
        } else {
            galleries.remove(MainApplication.systemPhotoPath);
            albumPaths.remove(MainApplication.systemPhotoPath);
        }
        cursor.close();
        return galleries;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent instanceof GridView) {
            selectImg(position);
//            linear.showTop();
            ObjectAnimator.ofFloat(container, "translationY", 0).setDuration(500).start();
        } else if (parent instanceof ListView) {
            titleLayout.setTitle(albumList.get(albumPaths.get(position)).getTitle());
            albumGridAdapter = new AlbumGridAdapter(getActivity(), albumList.get(albumPaths.get(position)).getPhotos());
            gridView.setAdapter(albumGridAdapter);
            selectImg(0);
//            linear.showTop();
            ObjectAnimator.ofFloat(container, "translationY", 0).setDuration(500).start();
            isUp = true;
            ObjectAnimator.ofFloat(listView, "translationY", listView.getMeasuredHeight()).setDuration(500).start();
            titleLayout.setArrowImgResource(R.mipmap.arrow_down_white);
            titleLayout.setContinueTvVisible(true);
            titleLayout.setCancelImgVisible(true);
            titleLayout.setBackImgVisible(false);
        }
    }

    private void selectImg(final int position) {
        if (albumGridAdapter != null) {
            albumGridAdapter.selectImg(position);
            photoItem = (PhotoItem) albumGridAdapter.getItem(position);
            Uri uri = photoItem.getImageUri().startsWith("file:") ? Uri.parse(photoItem
                    .getImageUri()) : Uri.parse("file://" + photoItem.getImageUri());
            GlideUtils.loadBitmap(uri.toString(), new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    clipImg.setImage(resource);
                }
            });
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gridView.smoothScrollToPositionFromTop(position, 0);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        dis(v.getParent());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startP = new PointF(event.getRawX(), event.getRawY());
                return true;
//                break;
            case MotionEvent.ACTION_MOVE:
                nowP = new PointF(event.getRawX(), event.getRawY());
                if (startP != null && container.getPaddingTop() <= 0 && container.getPaddingTop() >= -MainApplication.getContext().getScreenWidth()) {
                    if (nowP.y < startP.y && startP.y >= titleLayout.getMeasuredHeight() + MainApplication.getContext().getScreenWidth()) {
                        if (container.getPaddingTop() == -MainApplication.getContext().getScreenWidth()) {
                            return true;
                        }
                        container.setPadding(0, (int) (nowP.y - startP.y), 0, 0);
                    } else if (nowP.y > startP.y && startP.y <= titleLayout.getMeasuredHeight() + arrowContainer.getMeasuredHeight()) {
                        if (container.getPaddingTop() == 0) {
                            return true;
                        }
                        container.setPadding(0, (int) (nowP.y - startP.y - MainApplication.getContext().getScreenWidth()), 0, 0);
                    }
                    if (container.getPaddingTop() <= -MainApplication.getContext().getScreenWidth()) {
                        container.setPadding(0, -MainApplication.getContext().getScreenWidth(), 0, 0);
                        return true;
                    } else if (container.getPaddingTop() >= 0) {
                        container.setPadding(0, 0, 0, 0);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (nowP == null || Math.sqrt((startP.x - nowP.x) * (startP.x - nowP.x) + (startP.y - nowP.y) * (startP.y - nowP.y)) < DensityUtils.dp2px(getActivity(), 10)) {
                    //单击
                    if (container.getPaddingTop() == 0) {
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-MainApplication.getContext().getScreenWidth());
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float f = (float) animation.getAnimatedValue();
                                container.setPadding(0, (int) f, 0, 0);
                                arrowImg.setRotation(animation.getAnimatedFraction() * 180);
                            }
                        });
                        valueAnimator.setDuration(500).start();
//                        ObjectAnimator.ofFloat(container, "translationY", -MainApplication.getContext().getScreenWidth()).setDuration(500).start();
                        startP = null;
                        nowP = null;
                        return true;
                    } else if (container.getPaddingTop() == -MainApplication.getContext().getScreenWidth()) {
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-MainApplication.getContext().getScreenWidth(), 0);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float f = (float) animation.getAnimatedValue();
                                container.setPadding(0, (int) f, 0, 0);
                                arrowImg.setRotation((1 - animation.getAnimatedFraction()) * 180);
                            }
                        });
                        valueAnimator.setDuration(500).start();
                        startP = null;
                        nowP = null;
                        return true;
                    }
                } else {
                    if (container.getPaddingTop() == -MainApplication.getContext().getScreenWidth()) {
                        arrowImg.setRotation(180);
                        startP = null;
                        nowP = null;
                        return true;
                    }
                    if (container.getPaddingTop() == 0) {
                        arrowImg.setRotation(0);
                        startP = null;
                        nowP = null;
                        return true;
                    }
                    //滑动
                    if (container.getPaddingTop() > -MainApplication.getContext().getScreenWidth() &&
                            container.getPaddingTop() <= -MainApplication.getContext().getScreenWidth() / 2) {
                        ValueAnimator valueAnimator = ValueAnimator.ofInt(container.getPaddingTop(), -MainApplication.getContext().getScreenWidth());
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int f = (int) animation.getAnimatedValue();
                                container.setPadding(0, f, 0, 0);
                                arrowImg.setRotation(animation.getAnimatedFraction() * 180);
                            }
                        });
                        valueAnimator.setDuration(((long) (MainApplication.getContext().getScreenWidth()) + container.getPaddingTop()) * 500 / MainApplication.getContext().getScreenWidth())
                                .start();
                        startP = null;
                        nowP = null;
                        return true;
                    } else if (container.getPaddingTop() < 0 && container.getPaddingTop() > -MainApplication.getContext().getScreenWidth() / 2) {
                        ValueAnimator valueAnimator = ValueAnimator.ofInt(container.getPaddingTop(), 0);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int f = (int) animation.getAnimatedValue();
                                container.setPadding(0, f, 0, 0);
                                arrowImg.setRotation((1f - animation.getAnimatedFraction()) * 180);
                            }
                        });
                        valueAnimator.setDuration((long) (-container.getPaddingTop() * 500 / MainApplication.getContext().getScreenWidth()))
                                .start();
//                        ObjectAnimator.ofFloat(container, "translationY", 0)
//                                .setDuration((long) (-container.getTranslationY() * 500 / MainApplication.getContext().getScreenWidth()))
//                                .start();
                        startP = null;
                        nowP = null;
                        return true;
                    }
                }

                break;
        }
        return false;
    }

    private PointF startP, nowP;

    private void dis(ViewParent viewParent) {
        viewParent.requestDisallowInterceptTouchEvent(true);
        if (viewParent.getParent() != null) {
            dis(viewParent.getParent());
        }
    }

}
