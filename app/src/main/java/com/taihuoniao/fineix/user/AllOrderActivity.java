//package com.taihuoniao.fineix.user;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.base.BaseActivity;
//import com.taihuoniao.fineix.beans.UserInfo;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.view.CustomHeadView;
//
//import java.lang.reflect.Type;
//
//import butterknife.Bind;
//import butterknife.OnClick;
//
///**
// * Created by taihuoniao on 2016/5/10.
// */
//public class AllOrderActivity extends BaseActivity implements View.OnClickListener {
//    @Bind(R.id.custom_head)
//    CustomHeadView custom_head;
//    @Bind(R.id.activity_all_order_myorderrelative)
//    RelativeLayout myOrderRelative;
//    @Bind(R.id.activity_all_order_daifukuanrelative)
//    RelativeLayout daifukuanTv;
//    @Bind(R.id.activity_all_order_daifukuanred)
//    TextView daifukuanRed;
//    @Bind(R.id.activity_all_order_daifahuorelative)
//    RelativeLayout daifahuoTv;
//    @Bind(R.id.activity_all_order_daifahuored)
//    TextView daifahuoRed;
//    @Bind(R.id.activity_all_order_daishouhuorelative)
//    RelativeLayout daishouhuoTv;
//    @Bind(R.id.activity_all_order_daishouhuored)
//    TextView daishouhuoRed;
//    @Bind(R.id.activity_all_order_daipingjiarelative)
//    RelativeLayout daipingjiaTv;
//    @Bind(R.id.activity_all_order_daipingjiared)
//    TextView daipingjiaRed;
//    @Bind(R.id.activity_all_order_tuihuanhuorelative)
//    RelativeLayout tuihuanhuoTv;
//    @Bind(R.id.activity_all_order_jdrelative)
//    RelativeLayout jdRelative;
//    @Bind(R.id.activity_all_order_tbrelative)
//    RelativeLayout tbRelative;
//    @Bind(R.id.activity_all_order_tmrelative)
//    RelativeLayout tmRelative;
//    @Bind(R.id.activity_all_order_ymxrelative)
//    RelativeLayout ymxRelative;
//
//    public AllOrderActivity() {
//        super(R.layout.activity_all_order);
//    }
//
//    @Override
//    protected void initView() {
//        custom_head.setHeadCenterTxtShow(true, "全部订单");
////        myOrderRelative.setOnClickListener(this);
////        daifukuanTv.setOnClickListener(this);
////        daifahuoTv.setOnClickListener(this);
////        daishouhuoTv.setOnClickListener(this);
////        daipingjiaTv.setOnClickListener(this);
////        tuihuanhuoTv.setOnClickListener(this);
//
//    }
//
//    @Override
//    protected void requestNet() {
//
//    }
//
//    @OnClick({R.id.activity_all_order_myorderrelative, R.id.activity_all_order_daifukuanrelative, R.id.activity_all_order_daifahuorelative,
//            R.id.activity_all_order_daishouhuorelative, R.id.activity_all_order_daipingjiarelative, R.id.activity_all_order_tuihuanhuorelative,
//            R.id.activity_all_order_jdrelative, R.id.activity_all_order_tbrelative, R.id.activity_all_order_tmrelative,
//            R.id.activity_all_order_ymxrelative})
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.activity_all_order_myorderrelative:
//                Intent allIntent = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
//                allIntent.putExtra("flag", "0");
//                startActivity(allIntent);
//                break;
//            case R.id.activity_all_order_daifukuanrelative:
//                Intent payIntent = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
//                payIntent.putExtra("optFragmentFlag", "1");
//                startActivity(payIntent);
//                break;
//            case R.id.activity_all_order_daifahuorelative:
//                Intent deliver = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
//                deliver.putExtra("optFragmentFlag", "2");
//                startActivity(deliver);
//                break;
//            case R.id.activity_all_order_daishouhuorelative:
//                Intent receiver = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
//                receiver.putExtra("optFragmentFlag", "3");
//                startActivity(receiver);
//                break;
//            case R.id.activity_all_order_daipingjiarelative:
//                Intent critical = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
//                critical.putExtra("optFragmentFlag", "4");
//                startActivity(critical);
//                break;
//            case R.id.activity_all_order_tuihuanhuorelative:
//                startActivity(new Intent(AllOrderActivity.this, ReturnGoodsActivity.class));
//                break;
//            case R.id.activity_all_order_jdrelative:
////                webView.getSettings().setJavaScriptEnabled(true);
////                webView.loadUrl("http://home.m.jd.com/newAllOrders/newAllOrders.action");
//                ToastUtils.showInfo("正在跳转，请稍等");
//                Uri uri = Uri.parse("http://home.m.jd.com/newAllOrders/newAllOrders.action");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//                break;
//            case R.id.activity_all_order_tbrelative:
//            case R.id.activity_all_order_tmrelative:
////                webView.getSettings().setJavaScriptEnabled(true);
////                webView.loadUrl("https://h5.m.taobao.com/mlapp/olist.html");
//                ToastUtils.showInfo("正在跳转，请稍等");
//                Uri uri1 = Uri.parse("https://h5.m.taobao.com/mlapp/olist.html");
//                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
//                startActivity(intent1);
//                break;
//            case R.id.activity_all_order_ymxrelative:
//                break;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        DataPaser.userInfoParser(handler);
//        ClientDiscoverAPI.userInfoNet(new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                UserInfo userInfo = new UserInfo();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<UserInfo>(){}.getType();
//                    userInfo = gson.fromJson(responseInfo.result,type);
//                }catch (JsonSyntaxException e){
//                    Log.e("<<<用户信息","解析异常="+e.toString());
//                }
//                if (userInfo.isSuccess()) {
////                        Log.e("<<<", "待付款" + userInfo.getOrder_wait_payment() + ",待发货" + userInfo.getOrder_ready_goods()
////                                + ",待收货" + userInfo.getOrder_sended_goods() + ",待评价" + userInfo.getOrder_evaluate());
//                    if (!"0".equals(userInfo.getData().getCounter().getOrder_wait_payment())) {
//                        daifukuanRed.setVisibility(View.VISIBLE);
//                        daifukuanRed.setText(String.format("%s", userInfo.getData().getCounter().getOrder_wait_payment()));
//                    } else {
//                        daifukuanRed.setVisibility(View.INVISIBLE);
//                    }
//                    if (!"0".equals(userInfo.getData().getCounter().getOrder_ready_goods())) {
//                        daifahuoRed.setVisibility(View.VISIBLE);
//                        daifahuoRed.setText(userInfo.getData().getCounter().getOrder_ready_goods());
//                    } else {
//                        daifahuoRed.setVisibility(View.INVISIBLE);
//                    }
//                    if (!"0".equals(userInfo.getData().getCounter().getOrder_sended_goods())) {
//                        daishouhuoRed.setVisibility(View.VISIBLE);
//                        daishouhuoRed.setText(userInfo.getData().getCounter().getOrder_sended_goods());
//                    } else {
//                        daishouhuoRed.setVisibility(View.INVISIBLE);
//                    }
//                    if (!"0".equals(userInfo.getData().getCounter().getOrder_evaluate())) {
//                        daipingjiaRed.setVisibility(View.VISIBLE);
//                        daipingjiaRed.setText(userInfo.getData().getCounter().getOrder_evaluate());
//                    } else {
//                        daipingjiaRed.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                ToastUtils.showError(R.string.net_fail);
//            }
//        });
//    }
//
//}
