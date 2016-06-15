package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneDetailsBean;
import com.taihuoniao.fineix.beans.ShareCJRecyclerAdapter;
import com.taihuoniao.fineix.beans.ShareDemoBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.TestShareUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

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
public class TestShare extends BaseActivity implements EditRecyclerAdapter.ItemClick, View.OnClickListener, PlatformActionListener {
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
    //网络请求对话框
    private WaittingDialog dialog;
    private DisplayImageOptions options750_1334, options500_500;
    private int[] shareImgs = {R.mipmap.share1, R.mipmap.share4, R.mipmap.share5, R.mipmap.share7};
    private List<ShareDemoBean> shareList;
    private ShareCJRecyclerAdapter shareCJRecyclerAdapter;
    private SceneDetailsBean netScene;

    public TestShare() {
        super(R.layout.activity_test_share);
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
        dialog = new WaittingDialog(TestShare.this);
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        shareList = new ArrayList<>();
        for (int imgId : shareImgs) {
            ShareDemoBean shareDemoBean = new ShareDemoBean(imgId, false);
            shareList.add(shareDemoBean);
        }
        shareCJRecyclerAdapter = new ShareCJRecyclerAdapter(shareList, TestShare.this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(shareCJRecyclerAdapter);
//        img.setOnClickListener(this);
        container.setOnClickListener(this);
    }

    private View initPop() {
        View view = View.inflate(TestShare.this, R.layout.share_layout, null);
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
        SimpleAdapter adapter = new SimpleAdapter(TestShare.this, shareList, R.layout.share_item_layout, new String[]{"image", "text"}, new int[]{R.id.iv_plat_logo, R.id.tv_plat_name});
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

    private int imgWidth = 0, imgHeight = 0;

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
                    SceneDetailsBean netScene = (SceneDetailsBean) msg.obj;
                    if (netScene.isSuccess()) {
                        TestShare.this.netScene = netScene;
                        setImgParams();
                        recyclerView.setVisibility(View.VISIBLE);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                click(0);
                            }
                        });
//                        click(0);
                    } else {
                        ToastUtils.showError(netScene.getMessage());
                        finish();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    ToastUtils.showError("网络错误，请重试");
                    finish();
                    break;
                case 3:
                    dialog.dismiss();
                    ToastUtils.showError("对不起，分享出错");
                    break;
                case 2:
                    dialog.dismiss();
                    ToastUtils.showInfo("您取消了分享");
                    break;
                case 1:
                    dialog.dismiss();
                    ToastUtils.showSuccess("分享成功");
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


    //动态设置container和imgview的宽高
    private void setImgParams() {
        RelativeLayout.LayoutParams cLp = (RelativeLayout.LayoutParams) container.getLayoutParams();
        cLp.height = relativeLayout.getHeight();
        cLp.width = cLp.height * 9 / 16;
        container.setLayoutParams(cLp);
        ImageLoader.getInstance().loadImage(netScene.getData().getCover_url(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imgWidth = loadedImage.getWidth();
                imgHeight = loadedImage.getHeight();
//                Log.e("<<<图片大小", "width=" + imgWidth + ",height=" + imgHeight);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
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
            case R.id.activity_share_container:
                dialog.show();
                if (netScene == null) {
                    requestNet();
                    return;
                }
                Intent intent = new Intent(TestShare.this, ShareCJSelectActivity.class);
                intent.putExtra("scene", netScene);
                startActivityForResult(intent, 1);
                dialog.dismiss();
                break;
            case R.id.title_share:
                if (imgHeight == 0 || imgWidth == 0) {
                    if (netScene == null) {
                        requestNet();
                    } else {
                        setImgParams();
                    }
                    return;
                }
                if (netScene.getData().getOid() != null) {
                    DataPaser.commitShareCJ(netScene.getData().getOid(), handler);
                }
                PopupWindowUtil.show(TestShare.this, initPop());
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
        double b = (double) container.getWidth() / MainApplication.getContext().getScreenWidth();
        view = TestShareUtils.selectStyle(this, position, netScene, b);
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
                netScene = (SceneDetailsBean) data.getSerializableExtra("scene");
                click(currentPosition);
                break;
        }
    }


    private Bitmap inflateView() {
        int layout = R.layout.view_share_style1;
        switch (currentPosition) {
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
        View view = View.inflate(this, layout, null);
        //启用绘图缓存
        view.setDrawingCacheEnabled(true);
        ImageView img = (ImageView) view.findViewById(R.id.activity_share_img);
        ImageLoader.getInstance().displayImage(netScene.getData().getCover_url(), img, options750_1334);
        RoundedImageView userHeadImg = (RoundedImageView) view.findViewById(R.id.activity_share_user_headimg);
        RelativeLayout userRightRelative = (RelativeLayout) view.findViewById(R.id.activity_share_user_right_relative);
        TextView userName = (TextView) view.findViewById(R.id.activity_share_user_name);
        TextView userInfo = (TextView) view.findViewById(R.id.activity_share_user_info);
        LinearLayout locationLinear = (LinearLayout) view.findViewById(R.id.activity_share_location_linear);
        ImageView locationImg = (ImageView) view.findViewById(R.id.activity_share_location_img);
        TextView locationTv = (TextView) view.findViewById(R.id.activity_share_location);
        ImageView erweima = (ImageView) view.findViewById(R.id.erweima);
        TextView line = (TextView) view.findViewById(R.id.activity_share_scene_line);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.activity_share_frame);
        TextView sceneTitle = (TextView) view.findViewById(R.id.activity_share_scene_title);
        final TextView desTv = (TextView) view.findViewById(R.id.activity_share_scene_des);
        final ImageView addImg = (ImageView) view.findViewById(R.id.activity_share_scene_add_img);
        ImageView fiuImg = (ImageView) view.findViewById(R.id.activity_share_fiu_img);
        TextView fiuTv = (TextView) view.findViewById(R.id.activity_share_fiu_tv);
        if (currentPosition == 2 || currentPosition == 3) {
            userName.setTextColor(getResources().getColor(R.color.black));
            userInfo.setTextColor(getResources().getColor(R.color.black969696));
            locationTv.setTextColor(getResources().getColor(R.color.black969696));
            locationImg.setImageResource(R.mipmap.location_height_22px);
            desTv.setTextColor(getResources().getColor(R.color.black969696));
            line.setTextColor(getResources().getColor(R.color.black969696));
        }
        ImageLoader.getInstance().displayImage(netScene.getData().getUser_info().getAvatar_url(), userHeadImg, options500_500);
        userName.setText(netScene.getData().getUser_info().getNickname());
        userInfo.setText(netScene.getData().getUser_info().getSummary());
        locationTv.setText(netScene.getData().getAddress());
        sceneTitle.setText(netScene.getData().getTitle());
        desTv.setText(netScene.getData().getDes());
        desTv.post(new Runnable() {
            @Override
            public void run() {
                if (desTv.getLineCount() > 2) {
                    addImg.setVisibility(View.VISIBLE);
                } else {
                    addImg.setVisibility(View.INVISIBLE);
                }
            }
        });
        SceneTitleSetUtils.setTitle(sceneTitle, frameLayout, 42, 21, 1);

        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
        view.measure(View.MeasureSpec.makeMeasureSpec(imgWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(imgHeight, View.MeasureSpec.EXACTLY));
        //这个方法也非常重要，设置布局的尺寸和位置
        view.layout(0, 0, imgWidth, imgHeight);
        Bitmap bitmap = view.getDrawingCache();
        //获得绘图缓存中的Bitmap
        view.buildDrawingCache();
        return bitmap;
    }

    private AdapterView.OnItemClickListener itemClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Platform.ShareParams params = null;
            String imgPath = MainApplication.getContext().getCacheDirPath() + File.separator + "fiu" + ".png";
            dialog.show();
//            Bitmap bitmap = Bitmap.createBitmap(container.getWidth(), container.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);//创建空图片变成画布
//            container.draw(canvas);//绘制画布上
//            canvas.save();
            Bitmap returnedBitmap = inflateView();
            if (returnedBitmap == null) {
                dialog.dismiss();
                ToastUtils.showError("获取图片失败,请重试");
                imgWidth = MainApplication.getContext().getScreenWidth();
                imgHeight = imgWidth * 16 / 9;
                return;
            }
//            try {
//                ImageUtils.saveToFile(imgPath, false, returnedBitmap);
//            } catch (IOException e) {
//                dialog.dismiss();
//                ToastUtils.showError("图片保存失败");
//                return;
//            }
            boolean isSuccess = FileUtils.bitmapToFile(returnedBitmap, imgPath);
            if (!isSuccess) {
                dialog.dismiss();
                ToastUtils.showError("图片保存失败");
                return;
            }
            switch (position) {
                case 3:
                    //qqzong
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.setPlatformActionListener(TestShare.this); // 设置分享事件回调
                    qzone.share(params);
                    break;
                case 2:
                    //sina
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    weibo.setPlatformActionListener(TestShare.this); // 设置分享事件回调
                    weibo.share(params);
                    break;
                case 0:
                    //wechat
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(TestShare.this);
                    wechat.share(params);
                    break;
                case 1:
                    //wechatmoment
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(TestShare.this);
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
