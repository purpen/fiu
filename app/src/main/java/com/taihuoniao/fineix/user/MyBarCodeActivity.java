package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.QrCodeUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.taihuoniao.fineix.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 *         created at 2016/4/26 14:09
 */
public class MyBarCodeActivity extends BaseActivity implements PlatformActionListener, View.OnClickListener {
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
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.rl_box)
    RelativeLayout rl_box;
    private String url;
    private String nickName;
    private String sex;
    private ArrayList<String> areas;
    private WaittingDialog svProgressHUD;
    public MyBarCodeActivity() {
        super(R.layout.activity_bar_code);
    }


    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MyBarCodeActivity.class.getSimpleName())) {
            Bundle bundle = intent.getBundleExtra(MyBarCodeActivity.class.getSimpleName());
            url = bundle.getString("url");
            nickName = bundle.getString("nickName");
            sex = bundle.getString("sex");
            areas = (ArrayList<String>) bundle.getSerializable("areas");
        }else {
            LoginInfo loginInfo = LoginInfo.getLoginInfo();
            if (loginInfo==null) return;
            url=loginInfo.getMedium_avatar_url();
            nickName=loginInfo.getNickname();
            sex=loginInfo.getSex();
            areas=loginInfo.areas;
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "二维码");
        svProgressHUD=new WaittingDialog(this);
        custom_head.setRightImgBtnShow(true);
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.getInstance().displayImage(url, riv);
        }

        if (!TextUtils.isEmpty(nickName)) {
            tv_name.setText(nickName);
        }
        if (TextUtils.equals(sex, "1")) {
            tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.male, 0);
        } else {
            tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.female, 0);
        }
        if (areas != null && areas.size() == 2) {
            tv_address.setText(String.format("%s %s", areas.get(0), areas.get(1)));
        }

        try {
            bitmap_2code = QrCodeUtils.Create2DCode(String.format(//13为用户类型
                    "http://m.taihuoniao.com/guide/appload?infoType=%s&infoId=%s", 13, LoginInfo.getUserId()));
            iv_bar_code.setImageBitmap(bitmap_2code);
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.ib_right)
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.ib_right:
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_share_code));
                break;
        }
    }

    private View initPopView(int layout) {
        View view = Util.inflateView(this, layout, null);
        GridView gv = (GridView) view.findViewById(R.id.gv);
        View iv_take_photo = view.findViewById(R.id.tv_take_photo);
        View iv_take_album = view.findViewById(R.id.tv_album);
        View iv_close = view.findViewById(R.id.tv_cancel);
        int[] image = {R.mipmap.wechat, R.mipmap.wechatmoment, R.mipmap.sina, R.mipmap.qqzone,};
        String[] name = {"微信好友", "微信朋友圈", "新浪微博", "QQ空间"};
        List<HashMap<String, Object>> shareList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("text", name[i]);
            shareList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(activity, shareList, R.layout.share_item_layout, new String[]{"image", "text"}, new int[]{R.id.iv_plat_logo, R.id.tv_plat_name});
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(itemClicklistener);
        iv_take_photo.setOnClickListener(this);
        iv_take_album.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        return view;
    }

    private AdapterView.OnItemClickListener itemClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String title = "有Fiu的生活，才够意思，快点扫码加我吧！查看个人主页>>http://m.taihuoniao.com/guide/fiu?infoType=13&infoId=" + LoginInfo.getUserId();
            String titleUrl = "http://m.taihuoniao.com/guide/fiu?infoType=13&infoId=" + LoginInfo.getUserId();
            String imgPath = FileUtils.getSavePath(getPackageName()) + "/share.png";
            FileUtils.bitmapToFile(ImageUtils.convertViewToBitmap(rl_box), imgPath);
            Platform.ShareParams params = null;
            switch (i) { //微信好友
                case 0:
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
//                    params.setTitle(title);
//                    params.setTitleUrl(titleUrl);
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(MyBarCodeActivity.this);
                    wechat.share(params);
                    break;
                case 1:  //微信朋友圈
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
//                    params.setTitle(title);
//                    params.setTitleUrl(titleUrl);
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(MyBarCodeActivity.this);
                    wechatMoments.share(params);
                    break;
                case 2: //新浪微博
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
//                    params.setText(title);
//                    params.setTitleUrl(titleUrl);
                    params.setImagePath(imgPath);
                    Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    weibo.setPlatformActionListener(MyBarCodeActivity.this); // 设置分享事件回调
                    weibo.share(params);
                    break;
                case 3:  //qq空间
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
//                    params.setTitle("有Fiu的生活");
//                    params.setText(title);
//                    params.setTitleUrl(titleUrl); // 标题的超链接
//                    params.setSite("");
//                    params.setSiteUrl("http://www.taihuoniao.com/");
                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.setPlatformActionListener(MyBarCodeActivity.this); // 设置分享事件回调
                    qzone.share(params);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.tv_take_photo:
                if (FileUtils.bitmapToFile(bitmap_2code, FileUtils.getSavePath(getPackageName()) + "/bar_code.png")) {
                    ToastUtils.showSuccess("二维码已保存到" + getPackageName() + "文件夹下");
//                    svProgressHUD.showSuccessWithStatus("二维码已保存到"+getPackageName()+"文件夹下");
                } else {
                    ToastUtils.showError("SD卡不可写，二维码保存失败");
//                    svProgressHUD.showErrorWithStatus("SD卡不可写，二维码保存失败");
                }
                PopupWindowUtil.dismiss();
                break;
            case R.id.tv_album:
                startActivity(new Intent(activity, CaptureActivity.class));
                PopupWindowUtil.dismiss();
                break;
            case R.id.tv_cancel:
            default:
                PopupWindowUtil.dismiss();
                break;
        }
    }

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    ToastUtils.showError("对不起，分享出错");
//                    svProgressHUD.showErrorWithStatus("对不起，分享出错");
                    break;
                case 2:
//                    Util.makeToast("您取消了分享");
                    break;
                case 1:
                    ToastUtils.showSuccess("分享成功");
//                    svProgressHUD.showSuccessWithStatus("分享成功");
                    break;
            }
        }
    };
}
