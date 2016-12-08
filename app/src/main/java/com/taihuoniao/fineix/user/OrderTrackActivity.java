package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.Base2Activity;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.bean.OrderTrackBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

/**
 * Created by Stephen on 2016/12/8 17:45
 * Email: 895745843@qq.com
 */

public class OrderTrackActivity extends BaseActivity {

    public OrderTrackActivity() {
        super(R.layout.activity_order_track);
    }

    @Override
    protected void initView() {
        CustomHeadView customHead = (CustomHeadView) findViewById(R.id.title_order_tracking);
        customHead.setHeadCenterTxtShow(true, App.getString(R.string.title_orderTracking));
        WindowUtils.chenjin(this);

        Intent intent = getIntent();
        String rid = intent.getStringExtra("rid");
        String express_no = intent.getStringExtra("express_no");
        String express_caty = intent.getStringExtra("express_caty");
        requestOrderTrackInfo(rid, express_no, express_caty);
    }


    private void requestOrderTrackInfo(String s1, String s2, String s3) {
        ClientDiscoverAPI.shoppingTracking(s1, s2, s3, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }
                HttpResponse<OrderTrackBean> orderTrackBeanHttpResponse = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<OrderTrackBean>>() {
                });

                TextView textView11111111 = (TextView) findViewById(R.id.textView11111111);
                textView11111111.setText(orderTrackBeanHttpResponse.toString());

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
