//package com.taihuoniao.fineix.scene.fragments;
//
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.FrameLayout;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.AlbumGridAdapter;
//import com.taihuoniao.fineix.adapters.AlbumListAdapter;
//import com.taihuoniao.fineix.base.BaseFragment;
//import com.taihuoniao.fineix.beans.AlbumBean;
//import com.taihuoniao.fineix.beans.PhotoItem;
//import com.taihuoniao.fineix.main.MainApplication;
//import com.taihuoniao.fineix.utils.DensityUtils;
//import com.taihuoniao.fineix.utils.FileUtils;
//import com.taihuoniao.fineix.utils.ImageUtils;
//import com.taihuoniao.fineix.view.GlobalTitleLayout;
//import com.taihuoniao.fineix.view.TopAndBottomLinear;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by taihuoniao on 2016/3/14.
// */
//public class PhotoFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
//    //    private ImageView cancelImg;
////    private LinearLayout titleLinear;
////    private TextView titleTv;
////    private ImageView titleImg;
////    private TextView continueTv;
//    private GlobalTitleLayout titleLayout;
//    //        private ScrollView scrollView;
//    private TopAndBottomLinear linear;
//    private LinearLayout listLinear;
//    private ListView albumListView;
//    private AlbumListAdapter albumListAdapter;
//    private ImageView photoImg;
//    //    private GridViewForScrollView gridView;
//    private GridView gridView;
//    private AlbumGridAdapter albumGridAdapter;
//    private Map<String, AlbumBean> albumList;
//    private List<String> albumPaths;
//    private boolean isUp = true;//相册列表界面的动画控制
//    private PhotoItem photoItem;//当前显示的photo
//
//
//    @Override
//    protected void requestNet() {
//        selectImg(0);
//    }
//
//    @Override
//    protected void initList() {
//        titleLayout.setBackImgVisible(false);
//        titleLayout.setCancelImgVisible(true);
//        titleLayout.setArrowImgVisible(true);
//        titleLayout.setCancelListener(this);
//        titleLayout.setBackListener(this);
//        titleLayout.setTitleLinearListener(this);
//        titleLayout.setContinueListener(this);
//        albumPaths = new ArrayList<String>();
//        albumList = findGalleries(getActivity());
//        if (albumList.containsKey(systemPhotoPath)) {
//            titleLayout.setTitle("胶卷相册");
//            albumGridAdapter = new AlbumGridAdapter(getActivity(), albumList.get(systemPhotoPath).getPhotos());
//        } else {
//            titleLayout.setTitle(albumList.get(albumPaths.get(0)).getTitle());
//            albumGridAdapter = new AlbumGridAdapter(getActivity(), albumList.get(albumPaths.get(0)).getPhotos());
//        }
//        albumListAdapter = new AlbumListAdapter(getActivity(), albumPaths, albumList);
//        albumListView.setAdapter(albumListAdapter);
//        albumListView.setOnItemClickListener(this);
//        gridView.setAdapter(albumGridAdapter);
//        gridView.setOnItemClickListener(this);
//    }
//
//    @Override
//    protected View initView() {
//        View view = View.inflate(getActivity(), R.layout.fragment_photo, null);
//        titleLayout = (GlobalTitleLayout) view.findViewById(R.id.fragment_photo_titlelayout);
////        cancelImg = (ImageView) view.findViewById(R.id.fragment_photo_back);
////        titleLinear = (LinearLayout) view.findViewById(R.id.fragment_photo_titlelinear);
////        titleTv = (TextView) view.findViewById(R.id.fragment_photo_titletv);
////        titleImg = (ImageView) view.findViewById(R.id.fragment_photo_titleimg);
////        continueTv = (TextView) view.findViewById(R.id.fragment_photo_continue);
////        scrollView = (TopAndBottomScrollView) view.findViewById(R.id.fragment_photo_scrollview);
//        linear = (TopAndBottomLinear) view.findViewById(R.id.fragment_photo_linear);
//        listLinear = (LinearLayout) view.findViewById(R.id.fragment_photo_listlinear);
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) listLinear.getLayoutParams();
//        lp.topMargin = MainApplication.getContext().getScreenHeight();
//        lp.height = MainApplication.getContext().getScreenHeight() - DensityUtils.dp2px(getActivity(), 50);
//        albumListView = (ListView) view.findViewById(R.id.fragment_photo_albmlist);
////        photoImg = (ImageView) view.findViewById(R.id.fragment_photo_photoimg);
////        gridView = (GridViewForScrollView) view.findViewById(R.id.fragment_photo_gridview);
//        addViewToLinear();
//        return view;
//    }
//
//    private void addViewToLinear() {
//        photoImg = new ImageView(getActivity());
//        photoImg.setLayoutParams(new LinearLayout.LayoutParams(MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenWidth()));
//        photoImg.setImageResource(R.mipmap.ic_launcher);
//        photoImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        linear.addToToplinear(photoImg);
//        gridView = new GridView(getActivity());
//        gridView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        gridView.setCacheColorHint(0);
//        gridView.setHorizontalSpacing(DensityUtils.dp2px(getActivity(), 2));
//        gridView.setVerticalSpacing(DensityUtils.dp2px(getActivity(), 2));
//        gridView.setNumColumns(4);
//        gridView.setSelector(R.color.nothing);
//        gridView.setPadding(0, DensityUtils.dp2px(getActivity(), 2), 0, DensityUtils.dp2px(getActivity(), 2));
//        linear.addToBottomlinear(gridView);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.title_cancel:
//                getActivity().finish();
//                break;
//            case R.id.title_back:
//            case R.id.title_linear:
//                if (isUp) {
//                    isUp = false;
//                    ObjectAnimator.ofFloat(listLinear, "translationY", DensityUtils.dp2px(getActivity(), 50) - MainApplication.getContext().getScreenHeight()).setDuration(500).start();
//                    titleLayout.setArrowImgResource(R.mipmap.arrow_up_white);
//                    titleLayout.setContinueTvVisible(false);
//                    titleLayout.setBackImgVisible(true);
//                    titleLayout.setCancelImgVisible(false);
//                } else {
//                    isUp = true;
//                    ObjectAnimator.ofFloat(listLinear, "translationY", listLinear.getMeasuredHeight() - DensityUtils.dp2px(getActivity(), 50)).setDuration(500).start();
//                    titleLayout.setArrowImgResource(R.mipmap.arrow_down_white);
//                    titleLayout.setContinueTvVisible(true);
//                    titleLayout.setBackImgVisible(false);
//                    titleLayout.setCancelImgVisible(true);
//                }
//                break;
//            case R.id.title_continue:
//                ImageUtils.processPhotoItem(getActivity(), photoItem);
//                break;
//        }
//    }
//
//    public Map<String, AlbumBean> findGalleries(Context context) {
//        albumPaths.clear();
//        albumPaths.add(systemPhotoPath);
//        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
//                MediaStore.Images.Media.DATE_ADDED};
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,//指定所要查询的字段
//                MediaStore.Images.Media.SIZE + ">?",//查询条件
//                new String[]{"100000"}, //查询条件中问号对应的值
//                MediaStore.Images.Media.DATE_ADDED + " desc");
//
//        cursor.moveToFirst();
//        //文件夹照片
//        Map<String, AlbumBean> galleries = new HashMap<String, AlbumBean>();
//        while (cursor.moveToNext()) {
//            String data = cursor.getString(1);
//            if (data.lastIndexOf("/") < 1) {
//                continue;
//            }
//            String sub = data.substring(0, data.lastIndexOf("/"));
//
//            if (!galleries.keySet().contains(sub)) {
//                String name = sub.substring(sub.lastIndexOf("/") + 1, sub.length());
//                if (!albumPaths.contains(sub)) {
//                    albumPaths.add(sub);
//                }
//                galleries.put(sub, new AlbumBean(name, sub, new ArrayList<PhotoItem>()));
//            }
//
//            galleries.get(sub).getPhotos().add(new PhotoItem(data, (long) (cursor.getInt(2)) * 1000));
//        }
//        //系统相机照片
//        ArrayList<PhotoItem> sysPhotos = FileUtils.findPicsInDir(systemPhotoPath);
//        if (!sysPhotos.isEmpty()) {
//            galleries.put(systemPhotoPath, new AlbumBean("胶卷相册", systemPhotoPath, sysPhotos));
//        } else {
//            galleries.remove(systemPhotoPath);
//            albumPaths.remove(systemPhotoPath);
//        }
//        return galleries;
//    }
//
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (parent instanceof GridView) {
//            selectImg(position);
//            linear.showTop();
//        } else if (parent instanceof ListView) {
//            titleLayout.setTitle(albumList.get(albumPaths.get(position)).getTitle());
//            albumGridAdapter = new AlbumGridAdapter(getActivity(), albumList.get(albumPaths.get(position)).getPhotos());
//            gridView.setAdapter(albumGridAdapter);
//            selectImg(0);
//            linear.showTop();
//            isUp = true;
//            ObjectAnimator.ofFloat(listLinear, "translationY", listLinear.getMeasuredHeight() - DensityUtils.dp2px(getActivity(), 50)).setDuration(500).start();
//            titleLayout.setArrowImgResource(R.mipmap.arrow_down_white);
//            titleLayout.setContinueTvVisible(true);
//            titleLayout.setCancelImgVisible(true);
//            titleLayout.setBackImgVisible(false);
//        }
//    }
//
//    private void selectImg(int position) {
//        albumGridAdapter.selectImg(position);
//        photoItem = (PhotoItem) albumGridAdapter.getItem(position);
//        Uri uri = photoItem.getImageUri().startsWith("file:") ? Uri.parse(photoItem
//                .getImageUri()) : Uri.parse("file://" + photoItem.getImageUri());
//        photoImg.setImageBitmap(ImageUtils.decodeBitmapWithSize(uri.getPath(), MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenWidth(), true));
//        gridView.smoothScrollToPositionFromTop(position, 0);
//    }
//
//}
