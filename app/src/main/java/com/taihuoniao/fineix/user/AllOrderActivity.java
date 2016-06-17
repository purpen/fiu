package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.UserInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by taihuoniao on 2016/5/10.
 */
public class AllOrderActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.activity_all_order_title)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_all_order_myorderrelative)
    RelativeLayout myOrderRelative;
    @Bind(R.id.activity_all_order_daifukuanrelative)
    RelativeLayout daifukuanTv;
    @Bind(R.id.activity_all_order_daifukuanred)
    TextView daifukuanRed;
    @Bind(R.id.activity_all_order_daifahuorelative)
    RelativeLayout daifahuoTv;
    @Bind(R.id.activity_all_order_daifahuored)
    TextView daifahuoRed;
    @Bind(R.id.activity_all_order_daishouhuorelative)
    RelativeLayout daishouhuoTv;
    @Bind(R.id.activity_all_order_daishouhuored)
    TextView daishouhuoRed;
    @Bind(R.id.activity_all_order_daipingjiarelative)
    RelativeLayout daipingjiaTv;
    @Bind(R.id.activity_all_order_daipingjiared)
    TextView daipingjiaRed;
    @Bind(R.id.activity_all_order_tuihuanhuorelative)
    RelativeLayout tuihuanhuoTv;
    @Bind(R.id.activity_all_order_jdrelative)
    RelativeLayout jdRelative;
    @Bind(R.id.activity_all_order_tbrelative)
    RelativeLayout tbRelative;
    @Bind(R.id.activity_all_order_tmrelative)
    RelativeLayout tmRelative;
    @Bind(R.id.activity_all_order_ymxrelative)
    RelativeLayout ymxRelative;

    public AllOrderActivity() {
        super(R.layout.activity_all_order);
    }

    @Override
    protected void initView() {
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setContinueTvVisible(false);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setTitle(R.string.all_order, getResources().getColor(R.color.black333333));
//        myOrderRelative.setOnClickListener(this);
//        daifukuanTv.setOnClickListener(this);
//        daifahuoTv.setOnClickListener(this);
//        daishouhuoTv.setOnClickListener(this);
//        daipingjiaTv.setOnClickListener(this);
//        tuihuanhuoTv.setOnClickListener(this);

    }

    @Override
    protected void requestNet() {

    }

    @OnClick({R.id.activity_all_order_myorderrelative, R.id.activity_all_order_daifukuanrelative, R.id.activity_all_order_daifahuorelative,
            R.id.activity_all_order_daishouhuorelative, R.id.activity_all_order_daipingjiarelative, R.id.activity_all_order_tuihuanhuorelative,
            R.id.activity_all_order_jdrelative, R.id.activity_all_order_tbrelative, R.id.activity_all_order_tmrelative,
            R.id.activity_all_order_ymxrelative})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_all_order_myorderrelative:
                Intent allIntent = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
                allIntent.putExtra("flag", "0");
                startActivity(allIntent);
                break;
            case R.id.activity_all_order_daifukuanrelative:
                Intent payIntent = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
                payIntent.putExtra("optFragmentFlag", "1");
                startActivity(payIntent);
                break;
            case R.id.activity_all_order_daifahuorelative:
                Intent deliver = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
                deliver.putExtra("optFragmentFlag", "2");
                startActivity(deliver);
                break;
            case R.id.activity_all_order_daishouhuorelative:
                Intent receiver = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
                receiver.putExtra("optFragmentFlag", "3");
                startActivity(receiver);
                break;
            case R.id.activity_all_order_daipingjiarelative:
                Intent critical = new Intent(AllOrderActivity.this, ShopOrderListActivity.class);
                critical.putExtra("optFragmentFlag", "4");
                startActivity(critical);
                break;
            case R.id.activity_all_order_tuihuanhuorelative:
                startActivity(new Intent(AllOrderActivity.this, ReturnGoodsActivity.class));
                break;
            case R.id.activity_all_order_jdrelative:
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.loadUrl("http://home.m.jd.com/newAllOrders/newAllOrders.action");
                ToastUtils.showInfo("正在跳转，请稍等");
                Uri uri = Uri.parse("http://home.m.jd.com/newAllOrders/newAllOrders.action");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.activity_all_order_tbrelative:
            case R.id.activity_all_order_tmrelative:
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.loadUrl("https://h5.m.taobao.com/mlapp/olist.html");
                ToastUtils.showInfo("正在跳转，请稍等");
                Uri uri1 = Uri.parse("https://h5.m.taobao.com/mlapp/olist.html");
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                startActivity(intent1);
                break;
            case R.id.activity_all_order_ymxrelative:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        DataPaser.userInfoParser(handler);
        ClientDiscoverAPI.userInfoNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<个人信息", responseInfo.result);
                UserInfo userInfo = null;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject userObj = obj.getJSONObject("data");
                    userInfo = UserInfo.getInstance();
                    userInfo.setSuccess(obj.optBoolean("success"));
                    userInfo.setAccount(userObj.optString("account"));
                    userInfo.setNickname(userObj.optString("nickname"));
                    userInfo.setTrue_nickname(userObj.optString("true_nickname"));
                    userInfo.setSex(userObj.optString("sex"));
                    userInfo.setBirthday(userObj.optString("birthday"));
                    userInfo.setMedium_avatar_url(userObj.optString("medium_avatar_url"));
                    userInfo.setRealname(userObj.optString("realname"));
                    userInfo.setPhone(userObj.optString("phone"));
                    userInfo.setAddress(userObj.optString("address"));
                    userInfo.setZip(userObj.optString("zip"));
                    userInfo.setIm_qq(userObj.optString("im_qq"));
                    userInfo.setWeixin(userObj.optString("weixin"));
                    userInfo.setCompany(userObj.optString("company"));
                    userInfo.setProvince_id(userObj.optString("province_id"));
                    userInfo.setDistrict_id(userObj.optString("district_id"));
                    userInfo.setRank_id(userObj.optString("rank_id"));
                    userInfo.setRank_title(userObj.optString("rank_title"));
                    userInfo.setBird_coin(userObj.optString("bird_coin"));
                    userInfo.set_id(userObj.optString("_id"));
                    JSONObject counterObj = userObj.getJSONObject("counter");
                    userInfo.setOrder_wait_payment(counterObj.optString("order_wait_payment"));
                    userInfo.setOrder_ready_goods(counterObj.optString("order_ready_goods"));
                    userInfo.setOrder_evaluate(counterObj.optString("order_evaluate"));
                    userInfo.setOrder_total_count(counterObj.optString("order_total_count"));
                    userInfo.setOrder_sended_goods(counterObj.optString("order_sended_goods"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (userInfo == null) {
                    return;
                }
                if (userInfo.isSuccess()) {
//                        Log.e("<<<", "待付款" + userInfo.getOrder_wait_payment() + ",待发货" + userInfo.getOrder_ready_goods()
//                                + ",待收货" + userInfo.getOrder_sended_goods() + ",待评价" + userInfo.getOrder_evaluate());
                    if (!"0".equals(userInfo.getOrder_wait_payment())) {
                        daifukuanRed.setVisibility(View.VISIBLE);
                        daifukuanRed.setText(String.format("%s", userInfo.getOrder_wait_payment()));
                    } else {
                        daifukuanRed.setVisibility(View.INVISIBLE);
                    }
                    if (!"0".equals(userInfo.getOrder_ready_goods())) {
                        daifahuoRed.setVisibility(View.VISIBLE);
                        daifahuoRed.setText(userInfo.getOrder_ready_goods());
                    } else {
                        daifahuoRed.setVisibility(View.INVISIBLE);
                    }
                    if (!"0".equals(userInfo.getOrder_sended_goods())) {
                        daishouhuoRed.setVisibility(View.VISIBLE);
                        daishouhuoRed.setText(userInfo.getOrder_sended_goods());
                    } else {
                        daishouhuoRed.setVisibility(View.INVISIBLE);
                    }
                    if (!"0".equals(userInfo.getOrder_evaluate())) {
                        daipingjiaRed.setVisibility(View.VISIBLE);
                        daipingjiaRed.setText(userInfo.getOrder_evaluate());
                    } else {
                        daipingjiaRed.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showError("网络错误");
            }
        });
    }

}
