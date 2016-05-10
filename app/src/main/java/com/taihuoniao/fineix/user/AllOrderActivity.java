package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.UserInfo;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

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
        DataPaser.userInfoParser(handler);
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
                break;
            case R.id.activity_all_order_tbrelative:
                break;
            case R.id.activity_all_order_tmrelative:
                break;
            case R.id.activity_all_order_ymxrelative:
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.PARSER_USER_INFO:
                    UserInfo userInfo = (UserInfo) msg.obj;
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
                    break;
                case DataConstants.NET_FAIL:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
