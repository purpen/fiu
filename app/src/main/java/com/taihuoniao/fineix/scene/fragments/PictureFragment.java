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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.ImageCrop.ClipImageLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/28.
 */
public class PictureFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {
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
    private AlbumListAdapter albumListAdapter;
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
        albumListAdapter = new AlbumListAdapter(getActivity(), albumPaths, albumList);
        listView.setAdapter(albumListAdapter);
        listView.setOnItemClickListener(this);
        gridView.setAdapter(albumGridAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void requestNet() {
        selectImg(0);
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
//            Log.e("<<<图片路径", data);
//            if (data.endsWith(".png") || data.endsWith(".jpg") || data.endsWith(".jpeg")) {
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
            ImageLoader.getInstance().loadImage(uri.toString(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    clipImg.setImage(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
//            ImageLoader.getInstance().displayImage(uri.toString(), photoImg, options500_500);
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
                Log.e("<<<", "action_down=x=" + event.getX() + ",y=" + event.getY());
                startP = new PointF(event.getRawX(), event.getRawY());
                return true;
//                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("<<<", "action_move=x=" + event.getX() + ",y=" + event.getY());
                nowP = new PointF(event.getRawX(), event.getRawY());
                if (startP != null && container.getPaddingTop() <= 0 && container.getPaddingTop() >= -MainApplication.getContext().getScreenWidth()) {
                    if (nowP.y < startP.y && startP.y >= titleLayout.getMeasuredHeight() + MainApplication.getContext().getScreenWidth()) {
                        Log.e("<<<", "nowp<startp");
                        if (container.getPaddingTop() == -MainApplication.getContext().getScreenWidth()) {
                            return true;
                        }
                        container.setPadding(0, (int) (nowP.y - startP.y), 0, 0);
                    } else if (nowP.y > startP.y && startP.y <= titleLayout.getMeasuredHeight() + arrowContainer.getMeasuredHeight()) {
                        Log.e("<<<", "nowp>startp");
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
                Log.e("<<<", "action_up");
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
