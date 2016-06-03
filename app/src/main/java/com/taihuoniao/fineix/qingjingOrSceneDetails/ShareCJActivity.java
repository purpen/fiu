package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.ShareCJRecyclerAdapter;
import com.taihuoniao.fineix.beans.ShareDemoBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ShareCJUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class ShareCJActivity extends BaseActivity implements EditRecyclerAdapter.ItemClick, View.OnClickListener, PlatformActionListener {
    //上个界面传递过来的场景id
    private String id;
    @Bind(R.id.activity_share_title)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_share_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.activity_share_relative)
    RelativeLayout relativeLayout;
    @Bind(R.id.activity_share_container)
    RelativeLayout container;
    @Bind(R.id.activity_share_img)
    ImageView img;
    //    @Bind(R.id.activity_share_user_headimg)
//    ImageView userHeadImg;
//    @Bind(R.id.activity_share_user_name)
//    TextView userName;
//    @Bind(R.id.activity_share_user_info)
//    TextView userInfo;
//    @Bind(R.id.activity_share_location)
//    TextView locationTv;
//    @Bind(R.id.activity_share_frame)
//    FrameLayout frameLayout;
//    @Bind(R.id.activity_share_scene_title)
//    TextView sceneTitle;
//    @Bind(R.id.activity_share_scene_des)
//    TextView desTv;
    //网络请求对话框
    private WaittingDialog dialog;
    private DisplayImageOptions options750_1334, options500_500;
    private int[] shareImgs = {R.mipmap.share1, R.mipmap.share4, R.mipmap.share5, R.mipmap.share7};
    private List<ShareDemoBean> shareList;
    private ShareCJRecyclerAdapter shareCJRecyclerAdapter;
    private SceneDetails netScene;

    public ShareCJActivity() {
        super(R.layout.activity_sharecj);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        if (id == null) {
            ToastUtils.showError("数据异常");
//            dialog.showErrorWithStatus("数据异常");
            finish();
        }
    }

    @Override
    protected void initView() {
        titleLayout.setTitleVisible(false);
        titleLayout.setShareImgVisible(true, this);
        titleLayout.setBackImgVisible(false);
        titleLayout.setContinueTvVisible(false);
        titleLayout.setCancelImgVisible(true);
//        titleLayout.setRightTv(R.string.share, getResources().getColor(R.color.white), this);
        dialog = new WaittingDialog(ShareCJActivity.this);
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
        shareList = new ArrayList<>();
        for (int imgId : shareImgs) {
            ShareDemoBean shareDemoBean = new ShareDemoBean(imgId, false);
            shareList.add(shareDemoBean);
        }
        shareCJRecyclerAdapter = new ShareCJRecyclerAdapter(shareList, ShareCJActivity.this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(shareCJRecyclerAdapter);
        img.setOnClickListener(this);
    }

    private View initPop() {
        View view = View.inflate(ShareCJActivity.this, R.layout.share_layout, null);
        GridView gv_share = (GridView) view.findViewById(R.id.gv_share);
        View tv_cancel = view.findViewById(R.id.tv_cancel);
        int[] image = {R.mipmap.wechat, R.mipmap.wechatmoment, R.mipmap.sina, R.mipmap.qqzone};
        String[] name = {"微信好友", "微信朋友圈", "新浪微博", "QQ空间",};
        List<HashMap<String, Object>> shareList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("text", name[i]);
            shareList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(ShareCJActivity.this, shareList, R.layout.share_item_layout, new String[]{"image", "text"}, new int[]{R.id.iv_plat_logo, R.id.tv_plat_name});
        gv_share.setAdapter(adapter);
        gv_share.setOnItemClickListener(itemClicklistener);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
            }
        });
        return view;
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.sceneDetails(id, handler);
    }

    private Bitmap loadImg = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case DataConstants.SCENE_DETAILS:
                    SceneDetails netScene = (SceneDetails) msg.obj;
                    if (netScene.isSuccess()) {
                        ShareCJActivity.this.netScene = netScene;
                        setImgParams();
//                        ImageSize imageSize = new ImageSize(MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenWidth() * 16 / 9);
                        ImageLoader.getInstance().loadImage(netScene.getCover_url(), options750_1334, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                loadImg = loadedImage;
                                img.setImageBitmap(loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
//                        ImageLoader.getInstance().displayImage(netScene.getCover_url(), img, options750_1334);
                        recyclerView.setVisibility(View.VISIBLE);
                        click(0);
                    } else {
                        ToastUtils.showError(netScene.getMessage());
//                        new SVProgressHUD(ShareCJActivity.this).showErrorWithStatus(netScene.getMessage());
//                        Toast.makeText(ShareCJActivity.this, netScene.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    ToastUtils.showError("网络错误，请重试");
//                    dialog.showErrorWithStatus("网络错误，请重试");
//                    Toast.makeText(ShareCJActivity.this, "网络错误,请重试", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 3:
                    dialog.dismiss();
                    ToastUtils.showError("对不起，分享出错");
//                    dialog.showErrorWithStatus("对不起，分享出错");
                    break;
                case 2:
                    dialog.dismiss();
                    ToastUtils.showInfo("您取消了分享");
//                    dialog.showErrorWithStatus("您取消了分享");
                    break;
                case 1:
                    dialog.dismiss();
                    ToastUtils.showSuccess("分享成功");
//                    dialog.showSuccessWithStatus("分享成功");
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    private int imgWidth = 0;

    //动态设置container和imgview的宽高
    private void setImgParams() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) img.getLayoutParams();
        layoutParams.height = relativeLayout.getHeight();
        layoutParams.width = layoutParams.height * 9 / 16;
        imgWidth = layoutParams.width;
        img.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams cLp = (RelativeLayout.LayoutParams) container.getLayoutParams();
        cLp.height = layoutParams.height;
        cLp.width = layoutParams.width;
        container.setLayoutParams(cLp);
    }

    @Override
    public void click(int postion) {
        //切换下面的线显示
        for (int i = 0; i < shareList.size(); i++) {
            if (i == postion) {
                shareList.get(postion).setIsSelect(true);
            } else {
                shareList.get(i).setIsSelect(false);
            }
        }
        shareCJRecyclerAdapter.notifyDataSetChanged();
        //改变分享的样式
        selectShareStyle(postion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_share_img:
                if (netScene == null) {
                    requestNet();
                    return;
                }
//                dialog.show();
                Bitmap bit = Bitmap.createBitmap(MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bit);//创建空图片变成画布
//                img.draw(canvas);
                container.draw(canvas);
                canvas.save();
                MainApplication.shareBitmap = bit;
                Intent intent = new Intent(ShareCJActivity.this, ShareCJSelectActivity.class);
                intent.putExtra("scene", netScene);
//                dialog.dismiss();
                startActivityForResult(intent, 1);
                break;
            case R.id.title_share:
                if (netScene.getOid() != null) {
                    DataPaser.commitShareCJ(netScene.getOid(), handler);
                }
                PopupWindowUtil.show(ShareCJActivity.this, initPop());
                break;
        }
    }

    //    private Bitmap bit1;
    private View view;

    //动态改变分享的样式
    private void selectShareStyle(int position) {
        if (netScene == null) {
            dialog.show();
            DataPaser.sceneDetails(id, handler);
            return;
        }
        if (view != null) {
            container.removeView(view);
        }
        double width = MainApplication.getContext().getScreenWidth();
        double bi = ((double) imgWidth / width);
        setImgParams();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
        int padding = DensityUtils.dp2px(ShareCJActivity.this, 10);
        if (position == 2 || position == 3) {
            lp.width = (int) (lp.width * 0.92);
            lp.height = (int) (lp.height * 0.72);

            container.setPadding(padding, padding, padding, padding);
        } else {
            container.setPadding(0, 0, 0, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lp.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else {
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(lp.width, lp.height);
            rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp = rlp;
        }
        if (position == 3) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        view = ShareCJUtils.selectStyle(container, position, netScene, bi);
        if (position == 0 || position == 1) {
            view.setPadding(padding, padding, padding, padding);
        }
        container.addView(view);
        currentPosition = position;
    }

    private int currentPosition = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case 2:
                netScene = (SceneDetails) data.getSerializableExtra("scene");
                click(currentPosition);
                break;
        }
    }

    private AdapterView.OnItemClickListener itemClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Platform.ShareParams params = null;
            String imgPath = MainApplication.systemPhotoPath + File.separator + "fiu" + System.currentTimeMillis() + ".png";
            Bitmap bitmap = Bitmap.createBitmap(container.getWidth(), container.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);//创建空图片变成画布
            container.draw(canvas);//绘制画布上
            canvas.save();
            boolean isSuccess = FileUtils.bitmapToFile(bitmap, imgPath);
//            relative.setDrawingCacheEnabled(false);
//            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, relative.getMeasuredWidth(), relative.getMeasuredHeight());

            if (!isSuccess) {
                dialog.dismiss();
                ToastUtils.showError("图片保存失败");
//                dialog.showErrorWithStatus("图片保存失败");
                return;
            }
            switch (position) {
                case 3:
                    //qqzong
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.setPlatformActionListener(ShareCJActivity.this); // 设置分享事件回调
                    qzone.share(params);
                    break;
                case 2:
                    //sina
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    weibo.setPlatformActionListener(ShareCJActivity.this); // 设置分享事件回调
                    weibo.share(params);
                    break;
                case 0:
                    //wechat
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(ShareCJActivity.this);
                    wechat.share(params);
                    break;
                case 1:
                    //wechatmoment
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(ShareCJActivity.this);
                    wechatMoments.share(params);
                    break;
            }
        }
    };


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        handler.sendEmptyMessage(3);
    }
}
