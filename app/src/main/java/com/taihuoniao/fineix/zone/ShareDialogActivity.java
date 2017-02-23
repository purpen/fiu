package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.zone.adapter.ShareDialogAdapter;
import com.taihuoniao.fineix.zone.bean.ShareItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by lilin on 2017/02/22.
 */

public class ShareDialogActivity extends BaseActivity implements PlatformActionListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
//    private SubjectListBean.DataBean.RowsBean.ProductsBean.DataEntity item;

    public ShareDialogActivity() {
        super(R.layout.activity_share_dialog);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
//        if (intent.hasExtra(TAG)) {
//            item = (ProductsBean.DataEntity) intent.getSerializableExtra(TAG);
//        }
    }

    @Override
    protected void initView() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
        initData();
    }

    @OnClick(R.id.ibtn_close)
    void onClick(View v){
        finish();
    }

//    private boolean canRedExternalStorage() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
//        return false;
//    }

    private void initData() {
        int[] image = {R.mipmap.share_wechat, R.mipmap.share_moments, R.mipmap.share_weibo, R.mipmap.share_qq,R.mipmap.copy_link};
        String[] name = {"微信", "朋友圈", "微博", "QQ","复制链接"};
        List<ShareItem> shareList = new ArrayList<>();
        ShareItem shareItem;
        for (int i = 0; i < image.length; i++) {
            shareItem = new ShareItem();
            shareItem.txt = name[i];
            shareItem.pic = image[i];
            shareList.add(shareItem);
        }
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity,5);
        recyclerView.setLayoutManager(gridLayoutManager);
        ShareDialogAdapter adapter = new ShareDialogAdapter(activity, shareList);
        recyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new ShareDialogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Platform.ShareParams params;
                switch (position) {
                    case 0: //微信
                        params = new Platform.ShareParams();
//                        params.setShareType(content.shareType);
//                        params.setUrl(content.titleUrl);
//                        params.setTitle(content.title);
//                        params.setTitleUrl(content.titleUrl);
//                        params.setText(content.shareTxt);
//                        params.setSite(content.site);
//                        params.setSiteUrl(content.siteUrl);
//                        params.setImageUrl(content.imageUrl);
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(ShareDialogActivity.this);
                        wechat.share(params);
                        break;
                    case 1: //微信朋友圈
                        params = new Platform.ShareParams();
//                        params.setShareType(content.shareType);
//                        params.setUrl(content.titleUrl);
//                        params.setTitle(content.title);
//                        params.setTitleUrl(content.titleUrl);
//                        params.setText(content.shareTxt);
//                        params.setSite(content.site);
//                        params.setSiteUrl(content.siteUrl);
//                        params.setImageUrl(content.imageUrl);
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(ShareDialogActivity.this);
                        wechatMoments.share(params);
                        break;
                    case 2: //新浪微博
                        params = new Platform.ShareParams();
//                        params.setTitle(content.title);
//                        params.setTitleUrl(content.titleUrl);
//                        params.setText(content.shareTxt);
//                        params.setSite(content.site);
//                        params.setSiteUrl(content.siteUrl);
//                        params.setImageUrl(content.imageUrl);
//                        params.setUrl(content.url);
                        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        weibo.setPlatformActionListener(ShareDialogActivity.this); // 设置分享事件回调
                        weibo.share(params);
                        break;
                    case 3: //QQ
                        params = new Platform.ShareParams();
//                    params.setTitle(getResources().getString(R.string.share_title_url));
//                    params.setText(getResources().getString(R.string.share_title_url));
//                    params.setTitleUrl("http://www.taihuoniao.com/");
//                    params.setImageUrl(LoginInfo.getHeadPicUrl());
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(ShareDialogActivity.this);
                        qq.share(params);
                        break;
                    default:
                        break;
                }
//                if(canRedExternalStorage()){
//                    share();
//                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.showInfo(platform + " 分享成功啦");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.showInfo(platform + " 分享取消了");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.showInfo(platform + " 分享失败啦");
        if (throwable != null) {
            Log.e("throw", "throw:" + throwable.getMessage());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
