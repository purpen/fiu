package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.BonusBean;
import com.taihuoniao.fineix.beans.QJDetailBean;
import com.taihuoniao.fineix.beans.ShareCJRecyclerAdapter;
import com.taihuoniao.fineix.beans.ShareDemoBean;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.QrCodeUtils;
import com.taihuoniao.fineix.utils.TestShareUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.bean.ShareH5Url;

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
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class ShareActivity extends BaseActivity implements EditRecyclerAdapter.ItemClick, View.OnClickListener, PlatformActionListener {
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
    private View activity_view;
    //网络请求对话框
    private WaittingDialog dialog;
    private int[] shareImgs = {R.mipmap.share1, R.mipmap.share2, R.mipmap.share3, R.mipmap.share4};
    private List<ShareDemoBean> shareList;
    private ShareCJRecyclerAdapter shareCJRecyclerAdapter;
    private QJDetailBean netScene;
    //分享成功后的popwindow
    private PopupWindow popupWindow;
    private LinearLayout linearLayout;
    private TextView textView, expTv;
    private TestShareUtils testShareUtils;
    private Bitmap mQRCodeBitmap;

    public ShareActivity() {
        super(0);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        if (id == null) {
            ToastUtils.showError("数据异常");
            finish();
        }
    }

    @Override
    protected void setContenttView() {
        activity_view = View.inflate(this, R.layout.activity_test_share, null);
        setContentView(activity_view);
    }

    @Override
    protected void initView() {
        titleLayout.setTitleVisible(false);
        titleLayout.setShareImgVisible(true, this);
        titleLayout.setBackImgVisible(false);
        titleLayout.setContinueTvVisible(false);
        titleLayout.setCancelImgVisible(true);
        titleLayout.setColor(R.color.title_black);
        dialog = new WaittingDialog(this);
        shareList = new ArrayList<>();
        for (int imgId : shareImgs) {
            ShareDemoBean shareDemoBean = new ShareDemoBean(imgId, false);
            shareList.add(shareDemoBean);
        }
        shareCJRecyclerAdapter = new ShareCJRecyclerAdapter(shareList, this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(shareCJRecyclerAdapter);
        initPopupWindow();
        testShareUtils = new TestShareUtils();
    }

    private void initPopupWindow() {
        View popup_view = View.inflate(this, R.layout.pop_share_success, null);
        Button button = (Button) popup_view.findViewById(R.id.pop_share_success_btn);
        linearLayout = (LinearLayout) popup_view.findViewById(R.id.linear);
        textView = (TextView) popup_view.findViewById(R.id.tv1);
        expTv = (TextView) popup_view.findViewById(R.id.exp_tv);
        popupWindow = new PopupWindow(popup_view, DensityUtils.dp2px(this, 300), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popup_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        button.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this,
                R.drawable.corner_white_4dp));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void showPopup() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(activity_view, Gravity.CENTER, 0, 0);
    }

    private View initPop() {
        View view = View.inflate(this, R.layout.share_layout, null);
        GridView gv_share = (GridView) view.findViewById(R.id.gv_share);
        View tv_cancel = view.findViewById(R.id.tv_cancel);
        int[] image = {R.mipmap.wechat, R.mipmap.wechatmoment, R.mipmap.sina, R.mipmap.qqzone};
        String[] name = {"微信好友", "微信朋友圈", "新浪微博", "QQ空间",};
        List<HashMap<String, Object>> shareList = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("image", image[i]);
            map.put("text", name[i]);
            shareList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, shareList, R.layout.share_item_layout, new String[]{"image", "text"}, new int[]{R.id.iv_plat_logo, R.id.tv_plat_name});
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

//    private int imgWidth = 0, imgHeight = 0;

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        requestQRCodeLink();
        sceneDetails(id);
    }

    //动态设置container和imgview的宽高
    private void setImgParams() {
        RelativeLayout.LayoutParams cLp = (RelativeLayout.LayoutParams) container.getLayoutParams();
        cLp.height = relativeLayout.getHeight();
        cLp.width = cLp.height * 9 / 16;
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
            case R.id.pop_share_success_btn:
                popupWindow.dismiss();
                break;
            case R.id.title_share:
//                if (imgHeight == 0 || imgWidth == 0) {
                if (netScene == null) {
                    requestNet();
                    return;
                }
                PopupWindowUtil.show(this, initPop());
                break;
        }
    }

    private View view;

    //动态改变分享的样式
    private void selectShareStyle(int position) {
        if (netScene == null) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
            sceneDetails(id);
            return;
        }
        if (view != null) {
            container.removeView(view);
        }
        double b = (double) container.getWidth() / MainApplication.getContext().getScreenWidth();
        view = testShareUtils.selectStyle(this, position, netScene, b, container.getHeight(), container.getWidth(),mQRCodeBitmap );
        container.addView(view);
        int currentPosition = position;
    }

    private Bitmap inflateView() {
        container.setDrawingCacheEnabled(true);
        Bitmap bitmap = container.getDrawingCache();
        container.buildDrawingCache();
        return bitmap;
    }

    private AdapterView.OnItemClickListener itemClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Platform.ShareParams params;
            String imgPath = MainApplication.getContext().getCacheDirPath() + File.separator + "D3IN" + ".png";
            if (!dialog.isShowing()) {
                dialog.show();
            }
            Bitmap returnedBitmap = inflateView();
            if (returnedBitmap == null) {
                dialog.dismiss();
                System.gc();
                ToastUtils.showError("获取图片失败,请重试");
//                imgWidth = MainApplication.getContext().getScreenWidth();
//                imgHeight = imgWidth * 16 / 9;
                return;
            }
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
                    qzone.setPlatformActionListener(ShareActivity.this); // 设置分享事件回调
                    qzone.share(params);
                    break;
                case 2:
                    //sina
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    weibo.setPlatformActionListener(ShareActivity.this); // 设置分享事件回调
                    weibo.share(params);
                    break;
                case 0:
                    //wechat
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(ShareActivity.this);
                    wechat.share(params);
                    break;
                case 1:
                    //wechatmoment
                    params = new Platform.ShareParams();
                    params.setShareType(Platform.SHARE_IMAGE);
                    params.setImagePath(imgPath);
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(ShareActivity.this);
                    wechatMoments.share(params);
                    break;
            }
            PopupWindowUtil.dismiss();
        }
    };

    private Call bonusHandler;

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> params = ClientDiscoverAPI.getgetBonusRequestParams(2 + "", 1 + "", id);
                bonusHandler =  HttpRequest.post(params,  URL.GET_BONUS, new GlobalDataCallBack(){
                    @Override
                    public void onSuccess(String json) {
                        dialog.dismiss();
                        HttpResponse<BonusBean> bonusBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<BonusBean>>() {});
                        if (bonusBean.isSuccess() && bonusBean.getData().getExp() > 0) {
                            textView.setText("+ " + bonusBean.getData().getExp());
                            expTv.setText(bonusBean.getData().getExp() + "");
                            showPopup();
                        } else {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.GONE);
                            showPopup();
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onCancel(Platform platform, int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                ToastUtils.showInfo("您取消了分享");
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                ToastUtils.showError("对不起，分享出错");
            }
        });
    }

    private Call detailsHandler;

    //情境详情
    private void sceneDetails(String id) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsceneDetailsRequestParams(id);
        detailsHandler = HttpRequest.post(requestParams, URL.SCENE_DETAILS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse<QJDetailBean> qjDetailBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<QJDetailBean>>(){});
                if (qjDetailBeanHttpResponse.isSuccess()) {
                    netScene = qjDetailBeanHttpResponse.getData();
                    setImgParams();
                    recyclerView.setVisibility(View.VISIBLE);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            click(0);
                        }
                    });
                } else {
                    ToastUtils.showError(qjDetailBeanHttpResponse.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (bonusHandler != null)
            bonusHandler.cancel();
        if (detailsHandler != null)
            detailsHandler.cancel();
        super.onDestroy();
    }

    private void requestQRCodeLink(){
        HttpRequest.post(ClientDiscoverAPI.getH5ShareParams(id, "2", null), URL.SHARE_H5_URL, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<ShareH5Url> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ShareH5Url>>() {});
                if (response.isSuccess()) {
                    generateQRCodeImage(response.getData());
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void generateQRCodeImage(ShareH5Url shareH5Url) {
        try {
            Bitmap logo = BitmapFactory.decodeResource(App.getContext().getResources(), R.mipmap.share_logo);
            mQRCodeBitmap = QrCodeUtils.create2DCode(null, shareH5Url.o_url, DensityUtils.dp2px(this, 60), DensityUtils.dp2px(this, 60));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
