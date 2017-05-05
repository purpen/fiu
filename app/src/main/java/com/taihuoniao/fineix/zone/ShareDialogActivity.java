package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.AboutUsActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.GoodsOrZoneQrCodeDialog;
import com.taihuoniao.fineix.zone.adapter.ShareDialogAdapter;
import com.taihuoniao.fineix.zone.bean.ShareH5Url;
import com.taihuoniao.fineix.zone.bean.ShareItem;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.Arrays;
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

import static com.taihuoniao.fineix.utils.Constants.REQUEST_PHONE_STATE_CODE;

/**
 * Created by lilin on 2017/02/22.
 */

public class ShareDialogActivity extends BaseActivity implements PlatformActionListener {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private ShareH5Url shareH5Url;
    private ZoneDetailBean zoneDetailBean;
    private GoodsOrZoneQrCodeDialog dialog;
    public ShareDialogActivity() {
        super(R.layout.activity_share_dialog);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TAG)) {
            zoneDetailBean = intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        setFinishOnTouchOutside(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
        initData();
    }

    @Override
    protected void requestNet() {
        //3代表地盘
        HttpRequest.post(ClientDiscoverAPI.getH5ShareParams(zoneDetailBean._id,"3",""), URL.SHARE_H5_URL, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<ShareH5Url> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ShareH5Url>>() {
                });
                if (response.isSuccess()) shareH5Url = response.getData();
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @OnClick({R.id.ibtn_close,R.id.textView})
    void onClick(View v){
        switch (v.getId()){
            case R.id.ibtn_close:
                finish();
                break;
            case R.id.textView:
                Intent intent = new Intent(activity, AboutUsActivity.class);
                intent.putExtra(AboutUsActivity.class.getSimpleName(), URL.COMPANY_PARTNER_URL);
                intent.putExtra(AboutUsActivity.class.getName(), getString(R.string.company_partner));
                startActivity(intent);
                break;
            default:
                break;
        }
    }



    private void initData() {
        int[] image = {R.mipmap.share_wechat, R.mipmap.share_moments, R.mipmap.share_weibo, R.mipmap.share_qq,R.mipmap.share_qrcode};
        String[] name = {"微信", "朋友圈", "微博", "QQ","二维码"};
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
                if (shareH5Url==null || zoneDetailBean==null) return;
                Platform.ShareParams params;
                switch (position) {
                    case 0: //微信
                        params = new Platform.ShareParams();
                        params.setShareType(Platform.SHARE_WEBPAGE);
                        params.setTitle(zoneDetailBean.title);
                        params.setText(zoneDetailBean.des);
                        params.setUrl(shareH5Url.url);
                        params.setImageUrl(zoneDetailBean.avatar_url);
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(ShareDialogActivity.this);
                        wechat.share(params);
                        break;
                    case 1: //微信朋友圈
                        params = new Platform.ShareParams();
                        params.setShareType(Platform.SHARE_WEBPAGE);
                        params.setTitle(zoneDetailBean.title);
                        params.setText(zoneDetailBean.des);
                        params.setUrl(shareH5Url.url);
                        params.setImageUrl(zoneDetailBean.avatar_url);
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(ShareDialogActivity.this);
                        wechatMoments.share(params);
                        break;
                    case 2: //新浪微博
                        params = new Platform.ShareParams();
                        params.setText(zoneDetailBean.des+shareH5Url.url);
                        params.setImageUrl(zoneDetailBean.avatar_url);
                        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        weibo.setPlatformActionListener(ShareDialogActivity.this); // 设置分享事件回调
                        weibo.share(params);
                        break;
                    case 3: //QQ
                        params = new Platform.ShareParams();
                        params.setTitle(zoneDetailBean.title);
                        params.setTitleUrl(shareH5Url.url);
                        params.setText(zoneDetailBean.des);
                        params.setImageUrl(zoneDetailBean.avatar_url);
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(ShareDialogActivity.this);
                        qq.share(params);
                        break;
                    case 4:
                        dialog = new GoodsOrZoneQrCodeDialog();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(GoodsOrZoneQrCodeDialog.class.getSimpleName(),shareH5Url);
                        dialog.setArguments(bundle);
                        dialog.show(getFragmentManager(),GoodsOrZoneQrCodeDialog.class.getSimpleName());
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.showInfo(platform.getName()+ " 分享成功啦");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.showInfo(platform.getName() + " 分享取消了");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.showInfo(platform.getName() + " 分享失败啦");
        if (throwable != null) {
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Arrays.asList(permissions).contains("android.permission.WRITE_EXTERNAL_STORAGE")){
            if (null == dialog) return;
            AndPermission.onRequestPermissionsResult(dialog, REQUEST_PHONE_STATE_CODE, permissions, grantResults);
        }
    }

}
