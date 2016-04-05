package com.taihuoniao.fineix.scene;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.JDDetailsBean;
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
        titleLayout.setBackgroundColor(getResources().getColor(R.color.white));
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
                        id = url.substring(url.indexOf("id") + 3, url.indexOf("&", url.indexOf("id")));
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
    }

    @Override
    protected void initView() {
//        setContentView(R.layout.activity_search_url);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_search_url_title);
        webView = (WebView) findViewById(R.id.activity_search_url_webview);
        findRelative = (RelativeLayout) findViewById(R.id.activity_search_url_findrelative);
        findBtn = (Button) findViewById(R.id.activity_search_url_find);
        dialog = new WaittingDialog(SearchURLActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
                SearchURLActivity.this.finish();
                break;
            case R.id.title_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    SearchURLActivity.this.finish();
                }
                break;
            case R.id.activity_search_url_find:
                dialog.show();
                switch (type) {
                    case DataConstants.JINGDONG:
                        DataPaser.getJDProductData(handler, id);
                        break;
                    case DataConstants.TAOBAO:
                        break;
                    case DataConstants.TIANMAO:
                        break;
                    case DataConstants.YAMAXUN:
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
            switch (msg.what) {
                case DataConstants.JINGDONG:
                    dialog.dismiss();
                    JDDetailsBean netJD = (JDDetailsBean) msg.obj;
                    if (!netJD.isSuccess()) {
                        Toast.makeText(SearchURLActivity.this, netJD.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;
                case DataConstants.TAOBAO:
                    break;
                case DataConstants.TIANMAO:
                    break;
                case DataConstants.YAMAXUN:
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
