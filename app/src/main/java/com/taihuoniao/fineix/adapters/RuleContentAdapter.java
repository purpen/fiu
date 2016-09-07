package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;

/**
 * @author lilin
 *         created at 2016/8/29 14:16
 */
public class RuleContentAdapter extends BaseAdapter {
    private SubjectData data;
    private Activity activity;

    //    private TextView tv_html;
//    private WebView webView;
    public RuleContentAdapter(SubjectData data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        final AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = 3 * Util.getScreenHeight(); //3屏高度
//        final View view = Util.inflateView(R.layout.activity_detail_footer, null);
//        webView= (WebView) view.findViewById(R.id.webView);
        final WebView webView = new WebView(MainApplication.getContext());
        webView.setLayoutParams(params);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webView.setWebViewClient(webViewClient);

//        tv_html = (TextView) view.findViewById(R.id.tv_html);
//        tv_html.setMovementMethod(ScrollingMovementMethod.getInstance());
//        new Thread(new VisitWebRunnable(data)).start();
//        Button btn = (Button) view.findViewById(R.id.btn);
//        if (data.evt == 2) {
//            btn.setVisibility(View.GONE);
//        }
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, SelectPhotoOrCameraActivity.class);
//                MainApplication.id = String.valueOf(data._id);
//                activity.startActivity(intent);
//            }
//        });
        webView.loadUrl(data.content_view_url);
//        webView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
////                view.setLayoutParams(params);
//                notifyDataSetChanged();
//            }
//        });
        return webView;
    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtil.e("onPageStarted", view.getMeasuredHeight() + "");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            LogUtil.e("onPageFinished", view.getMeasuredHeight() + "");
//            view.loadUrl("javascript:window.HTMLOUT.getContentHeight(document.getElementByTagName('html')");
//            LogUtil.e("onPageFinishedgetContentHeight", view.getContentHeight() + "");
//            LogUtil.e("onPageFinished", view.getMeasuredHeight() + "");
        }
    };
}
