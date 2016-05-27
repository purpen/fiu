package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneDetails;
import com.taihuoniao.fineix.beans.ShareCJRecyclerAdapter;
import com.taihuoniao.fineix.beans.ShareDemoBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.ShareCJUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class ShareCJActivity extends BaseActivity implements EditRecyclerAdapter.ItemClick, View.OnClickListener {
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
    private int[] shareImgs = {R.mipmap.share1, R.mipmap.share2, R.mipmap.share3, R.mipmap.share4, R.mipmap.share5, R.mipmap.share6, R.mipmap.share7};
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
            Toast.makeText(ShareCJActivity.this, "场景id为空", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void initView() {
        titleLayout.setTitleVisible(false);
        titleLayout.setRightTv(R.string.share, getResources().getColor(R.color.white), this);
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

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.sceneDetails(id, handler);
    }

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
                        ImageLoader.getInstance().displayImage(netScene.getCover_url(), img, options750_1334);
                        recyclerView.setVisibility(View.VISIBLE);
                        click(0);
                    } else {
                        Toast.makeText(ShareCJActivity.this, netScene.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    Toast.makeText(ShareCJActivity.this, "网络错误,请重试", Toast.LENGTH_SHORT).show();
                    finish();
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
                dialog.show();
                Bitmap bit = Bitmap.createBitmap(container.getWidth(), container.getHeight(), Bitmap.Config.ARGB_4444);
                Canvas canvas = new Canvas(bit);//创建空图片变成画布
                container.draw(canvas);
                canvas.save();
                MainApplication.shareBitmap = bit;
                Intent intent = new Intent(ShareCJActivity.this, ShareCJSelectActivity.class);
                intent.putExtra("scene", netScene);
                dialog.dismiss();
                startActivityForResult(intent, 1);
                break;
            case R.id.title_continue:
                if (netScene.getOid() != null) {
                    DataPaser.commitShareCJ(netScene.getOid(), handler);
                }

//                比例0.73472222
//                    引导图只需要白色部分就行。不需要背景色

//                container.removeView(view);
//                img.setImageBitmap(scBit);
                break;
        }
    }

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
        if (position == 4) {
            ViewGroup.LayoutParams lp = img.getLayoutParams();
            lp.width = (int) (lp.width * 0.8);
            lp.height = (int) (lp.height * 0.8);
        } else if (position == 5 || position == 6) {
            ViewGroup.LayoutParams lp = img.getLayoutParams();
            lp.width = (int) (lp.width * 0.8);
            lp.height = (int) (lp.height * 0.8);
        }
        view = ShareCJUtils.selectStyle(container, position, netScene, bi);
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
}
