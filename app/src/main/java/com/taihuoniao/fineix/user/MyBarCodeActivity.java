package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.QrCodeUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.taihuoniao.fineix.zxing.activity.CaptureActivity;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author lilin
 * created at 2016/4/26 14:09
 */
public class MyBarCodeActivity extends BaseActivity implements PlatformActionListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.iv_bar_code)
    ImageView iv_bar_code;
    @Bind(R.id.riv)
    RoundedImageView riv;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_address)
    TextView tv_address;
    private Bitmap bitmap_2code;
    public MyBarCodeActivity(){
        super(R.layout.activity_bar_code);
    }
    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"二维码");
        custom_head.setRightImgBtnShow(true);
        LoginInfo loginInfo=LoginInfo.getLoginInfo();
        if (loginInfo!=null){
            ImageLoader.getInstance().displayImage(loginInfo.getMedium_avatar_url(),riv);
            tv_name.setText(loginInfo.getNickname());
            if (TextUtils.equals(loginInfo.getSex(),"1")){
                tv_name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.male,0);
            }else {
                tv_name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.female,0);
            }
            if (loginInfo.areas!=null && loginInfo.areas.size()==2){
                tv_address.setText(String.format("%s %s",loginInfo.areas.get(0),loginInfo.areas.get(1)));
            }
        }

        try {
            bitmap_2code = QrCodeUtils.Create2DCode(String.format(//13为用户类型
                    "http://m.taihuoniao.com/guide/appload?infoType=%s&infoId=%s",13,LoginInfo.getUserId()));
            iv_bar_code.setImageBitmap(bitmap_2code);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ib_right)
    void performClick(View v){
        switch (v.getId()){
            case R.id.ib_right:
                PopupWindowUtil.show(activity,initPopView(R.layout.popup_share_code));
                break;
        }
    }

    private View initPopView(int layout) {
        View view = Util.inflateView(this, layout, null);
        View iv_take_photo = view.findViewById(R.id.tv_take_photo);
        View iv_take_album = view.findViewById(R.id.tv_album);
        View iv_close = view.findViewById(R.id.tv_cancel);
        View ll1 = view.findViewById(R.id.ll1);
        View ll2 = view.findViewById(R.id.ll2);
        View ll3 = view.findViewById(R.id.ll3);
        View ll4 = view.findViewById(R.id.ll4);
        iv_take_photo.setOnClickListener(onClickListener);
        iv_take_album.setOnClickListener(onClickListener);
        iv_close.setOnClickListener(onClickListener);
        ll1.setOnClickListener(onClickListener);
        ll2.setOnClickListener(onClickListener);
        ll3.setOnClickListener(onClickListener);
        ll4.setOnClickListener(onClickListener);
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            Platform.ShareParams params=null;
            switch (v.getId()) {
                case R.id.tv_take_photo:
                    if (FileUtils.bitmapToFile(bitmap_2code,FileUtils.getSavePath("Fiu") + "/bar_code.png")){
                        Util.makeToast("二维码已保存到Fiu文件夹下");
                    }else {
                        Util.makeToast("SD卡不可写，二维码保存失败");
                    }
                    PopupWindowUtil.dismiss();
                    break;
                case R.id.tv_album:
                    startActivity(new Intent(activity,CaptureActivity.class));
                    PopupWindowUtil.dismiss();
                    break;
                case R.id.ll1://微信
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    params.setUrl("Constants.SHARE_URI + detail.goods_id");
                    params.setTitle("detail.goods_name");
                    params.setText("detail.goods_name");
                    params.setImageUrl("Constants.PIC_URI+detail.goods_img");
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(MyBarCodeActivity.this);
                    wechat.share(params);
                    break;
                case R.id.ll2: //微信朋友圈
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_WEBPAGE);
                    params.setUrl("Constants.SHARE_URI + detail.goods_id");
                    params.setTitle("detail.goods_name");
                    params.setText("detail.goods_name");
                    params.setImageUrl("Constants.PIC_URI+detail.goods_img");
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(MyBarCodeActivity.this);
                    wechatMoments.share(params);
                    break;
                case R.id.ll3://新浪微博
                    params = new Platform.ShareParams();
                    params.setTitle("detail.goods_name");
                    params.setText("detail.goods_name");
                    params.setImageUrl("Constants.PIC_URI+detail.goods_img");
                    Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    weibo.setPlatformActionListener(MyBarCodeActivity.this); // 设置分享事件回调
                    weibo.share(params);
                    break;
                case R.id.ll4:
                    params= new Platform.ShareParams();
                    params.setTitle("detail.goods_name");
                    params.setText("detail.goods_name");
                    params.setTitleUrl("Constants.SHARE_URI + detail.goods_id"); // 标题的超链接
                    params.setImageUrl("Constants.PIC_URI+detail.goods_img");
                    params.setSite("");
                    params.setSiteUrl("http://www.dmore.com.cn/");
                    Platform qzone = ShareSDK.getPlatform (QZone.NAME);
                    qzone.setPlatformActionListener(MyBarCodeActivity.this); // 设置分享事件回调
                    qzone.share(params);
                    break;
                case R.id.tv_cancel:
                default:
                    PopupWindowUtil.dismiss();
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

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 3:
                    Util.makeToast("对不起，分享出错");
                    break;
                case 2:
                    Util.makeToast("您取消了分享");
                    break;
                case 1:
                    Util.makeToast("分享成功");
                    break;
            }
        }
    };
}
