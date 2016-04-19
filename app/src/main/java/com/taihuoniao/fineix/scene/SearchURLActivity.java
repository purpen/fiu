package com.taihuoniao.fineix.scene;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.JDDetailsBean;
import com.taihuoniao.fineix.beans.TBDetailsBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

/**
 * Created by taihuoniao on 2016/3/24.
 */
public class SearchURLActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的数据，用来判断进入哪个商城
    private int store;
    private String search;//获取上个界面传递过来的搜索商品关键字
    private GlobalTitleLayout titleLayout;
    private WebView webView;
    private RelativeLayout findRelative;
    private Button findBtn;
    private int type;//搜索到的商品属于哪个商城
    private String id;//搜索到的商品id
    private WaittingDialog dialog;
    //popupwindow下的控件
    private PopupWindow popupWindow;
    private ImageView productsImg;
    private EditText nameTv;
    private EditText priceTv;
    private Button confirmBtn;
    private String imagePath;//商品图片路径

    public SearchURLActivity() {
        super(R.layout.activity_search_url);
    }


    @Override
    protected void requestNet() {
        switch (store) {
            case DataConstants.JINGDONG:
//                webView.loadUrl(DataConstants.URL_JINGDONG);
                webView.loadUrl("http://m.jd.com/ware/search.action?sid=b5d5827fb0e5214f99d755c080c52d5b&keyword=" + search + "&catelogyList=");
                break;
            case DataConstants.TAOBAO:
//                webView.loadUrl(DataConstants.URL_TAOBAO);
                webView.loadUrl("http://s.m.taobao.com/h5?event_submit_do_new_search_auction=1&_input_charset=utf-8&topSearch=1&atype=b&searchfrom=1&action=home%3Aredirect_app_action&from=1&sst=1&n=20&buying=buyitnow&q=" + search);
                break;
            case DataConstants.TIANMAO:
//                webView.loadUrl(DataConstants.URL_TIANMAO);
                webView.loadUrl("https://s.m.tmall.com/m/search.htm?q=" + search + "&type=p&tmhkh5=&spm=875.7403452.a2227oh.d100&from=mallfp..m_1_searchbutton");
                break;
            case DataConstants.YAMAXUN:
//                webView.loadUrl(DataConstants.URL_YAMAXUN);
                webView.loadUrl(" http://www.amazon.cn/gp/aw/s/ref=nb_sb_noss?k=" + search);
                break;
        }

    }

    @Override
    protected void initList() {
        store = getIntent().getIntExtra("store", DataConstants.JINGDONG);
        search = getIntent().getStringExtra("search");
        titleLayout.setTitleVisible(false);
        titleLayout.setBackgroundResource(R.drawable.grey_no_corner);
//        titleLayout.setBackgroundColor(getResources().getColor(R.color.white));
        titleLayout.setRightTv(R.string.close, R.color.white, this);
        titleLayout.setBackListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.show();
                Log.e("<<<start>>>", url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                //淘宝搜索界面
                //手环
                //http://s.m.taobao.com/h5?event_submit_do_new_search_auction=1&_input_charset=utf-8&topSearch=1&atype=b&searchfrom=1&action=home%3Aredirect_app_action&from=1&sst=1&n=20&buying=buyitnow&q=%E6%89%8B%E7%8E%AF
                //230 137 139 231 142 175
                //亚马逊
                //https://www.amazon.cn/gp/aw/d/B01DBH4FNQ?psc=1
                //https://www.amazon.cn/gp/aw/d/B01COLHX6Q?psc=1
                //http://www.amazon.cn/gp/aw/d/B00WJSQOQU?ref=mh_cnm_waterfall_p11&pf_rd_p=260963472&pf_rd_s=mobile-11&pf_rd_t=36701&pf_rd_i=mobile&pf_rd_m=A1AJ19PSB66TGU&pf_rd_r=0H4WG8E8ST12T6MG49F7
                //https://www.amazon.cn/b/ref=lp_1791114071_gbsl_ulm_l-1_2192_69a77115?rh=i%3Ababy%2Cn%3A1791114071%2Cn%3A1791114071&ie=UTF8&node=1791114071
                Log.e("<<<finish>>>", url);
                if (url.startsWith("http://item.m.jd.com/product/")) {
                    findRelative.setVisibility(View.VISIBLE);
                    if (url.contains("id")) {
                        id = url.substring(url.indexOf("id=") + 3, url.indexOf("&", url.indexOf("id")));
                    } else if (url.endsWith(".html")) {
                        id = url.substring(url.indexOf("product/") + 8, url.indexOf(".", url.indexOf("product/")));
                    }
                    type = DataConstants.JINGDONG;
                } else if (url.startsWith("http://h5.m.taobao.com/awp/core/detail")) {
                    findRelative.setVisibility(View.VISIBLE);
                    if (url.contains("id")) {
                        id = url.substring(url.indexOf("id") + 3, url.indexOf("&", url.indexOf("id")));
                        type = DataConstants.TAOBAO;
                    }
                } else if (url.startsWith("https://detail.m.tmall.com/item")) {
                    findRelative.setVisibility(View.VISIBLE);
                    if (url.contains("id")) {
                        id = url.substring(url.indexOf("id") + 3, url.indexOf("&", url.indexOf("id")));
                        type = DataConstants.TIANMAO;
                    }
                } else if (url.startsWith("http://www.amazon.cn/gp/aw/d")) {
                    findRelative.setVisibility(View.VISIBLE);
                    if (url.contains("id")) {
                        id = url.substring(url.indexOf("B0"), url.indexOf("?", url.indexOf("B0")));
                        type = DataConstants.YAMAXUN;
                    }
                } else {
                    findRelative.setVisibility(View.GONE);
                    type = -1;
                    id = "-1";
                }
                super.onPageFinished(view, url);
            }
        });

        findBtn.setOnClickListener(this);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                webView.stopLoading();
                cancelNet();
            }
        });
    }

    @Override
    protected void initView() {
//        setContentView(R.layout.activity_search_url);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_search_url_title);
        webView = (WebView) findViewById(R.id.activity_search_url_webview);
        findRelative = (RelativeLayout) findViewById(R.id.activity_search_url_findrelative);
        findBtn = (Button) findViewById(R.id.activity_search_url_find);
        dialog = new WaittingDialog(SearchURLActivity.this);
        initPopupWindow();
    }

    private void initPopupWindow() {
        WindowManager windowManager = SearchURLActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(SearchURLActivity.this, R.layout.pop_find, null);
        productsImg = (ImageView) popup_view.findViewById(R.id.pop_find_img);
        nameTv = (EditText) popup_view.findViewById(R.id.pop_find_name);
        priceTv = (EditText) popup_view.findViewById(R.id.pop_find_price);
        confirmBtn = (Button) popup_view.findViewById(R.id.pop_find_confirm);
        popupWindow = new PopupWindow(popup_view, display.getWidth(), (int) (display.getHeight() * 0.4), true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        confirmBtn.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(SearchURLActivity.this,
                R.color.white));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    private void showPopup() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(webView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_find_confirm:
                String name = nameTv.getText().toString();
                String price = priceTv.getText().toString();
                TagItem tagItem = new TagItem(name, price);
                tagItem.setType(type);
                tagItem.setId(id);
                tagItem.setImagePath(imagePath);
                Intent intent = new Intent();
                intent.putExtra("tagItem", tagItem);
                setResult(DataConstants.RESULTCODE_SEARCHSTORE_SEARCHURL, intent);
                popupWindow.dismiss();
                finish();
                break;
            case R.id.title_continue:
                finish();
                break;
            case R.id.title_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.activity_search_url_find:
                dialog.show();
                switch (type) {
                    case DataConstants.JINGDONG:
                        DataPaser.getJDProductData(handler, id);
                        break;
                    case DataConstants.TAOBAO:
                        DataPaser.getTBProductData(handler, id);
                        break;
                    case DataConstants.TIANMAO:
                        DataPaser.getTBProductData(handler, id);
                        dialog.dismiss();
                        break;
                    case DataConstants.YAMAXUN:
                        Toast.makeText(SearchURLActivity.this, "亚马逊搜索无接口", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                }
                Toast.makeText(SearchURLActivity.this, "商品id = " + id + ",类型 = " + type, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case DataConstants.JINGDONG:
                    JDDetailsBean netJD = (JDDetailsBean) msg.obj;
                    if (!netJD.isSuccess()) {
                        Toast.makeText(SearchURLActivity.this, netJD.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    imagePath = netJD.getImagePath();
                    ImageLoader.getInstance().displayImage(imagePath, productsImg);
                    nameTv.setText(netJD.getName());
                    priceTv.setText(netJD.getSale_price());
                    showPopup();
                    break;
                case DataConstants.TAOBAO:
                    TBDetailsBean netTB = (TBDetailsBean) msg.obj;
                    if (!netTB.isSuccess()) {
                        Toast.makeText(SearchURLActivity.this, netTB.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ImageLoader.getInstance().displayImage(netTB.getPict_url(), productsImg);
                    nameTv.setText(netTB.getTitle());
                    priceTv.setText(netTB.getZk_final_price());
                    showPopup();
                    break;
                case DataConstants.YAMAXUN:
                    break;
                case DataConstants.NET_FAIL:
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
//        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    private void cancelNet() {
        Toast.makeText(SearchURLActivity.this, "取消网络请求未做", Toast.LENGTH_SHORT).show();
    }
}
