package com.taihuoniao.fineix.personal.salesevice;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackBean;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackResultBean;
import com.taihuoniao.fineix.utils.DPUtil;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.ArrayList;
import java.util.List;

public class ChargeBackActivity extends BaseActivity {

    private EditText editText1;
    private TextView editText2;
    private EditText editText3;
    private Button buttonCommit;

    private String rId;
    private String skuId;
    private ChargeBackBean chargeBackBean;

    public ChargeBackActivity() {
        super(R.layout.activity_salesafter_chargeback);
    }

    @Override
    protected void initView() {
        CustomHeadView customHead = (CustomHeadView) findViewById(R.id.custom_head);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (TextView) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        buttonCommit = (Button) findViewById(R.id.button_commit);
        customHead.setHeadCenterTxtShow(true, App.getString(R.string.title_salesAfter_chargeBack));
        WindowUtils.chenjin(this);
    }

    @Override
    protected void installListener() {
        super.installListener();
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        buttonCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText2.getText().toString())) {
                    Toast.makeText(ChargeBackActivity.this, App.getString(R.string.warn_salesAfter_chargeBack_cause_error), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(editText3.getText().toString().trim())) {
                    Toast.makeText(ChargeBackActivity.this, App.getString(R.string.warn_salesAfter_chargeBack_explain_error), Toast.LENGTH_SHORT).show();
                } else {
                    requestChargeBack(editText1.getText().toString(), String.valueOf(refundReasonEntity.getId()), editText3.getText().toString());
                }
            }
        });
    }


    @Override
    protected void requestNet() {
        rId = getIntent().getStringExtra(KEY.R_ID);
        skuId = getIntent().getStringExtra(KEY.SKU_ID);
        requestGetChargeBackInfo();
    }

    private List<ChargeBackBean.RefundReasonEntity> refund_reason;
    private ChargeBackBean.RefundReasonEntity refundReasonEntity;

    private void showPopupWindow(View v){
        if (popupWindow != null) {
            popupWindow.showAsDropDown(v);
            return;
        }

        List<String> list = new ArrayList<>();
        if (chargeBackBean != null) {
            refund_reason = chargeBackBean.getRefund_reason();
            for (int i = 0; i < refund_reason.size(); i++) {
                list.add(refund_reason.get(i).getTitle());
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChargeBackActivity.this, R.layout.simple_list_item_1, R.id.text1, list);
        ListView listView = new ListView(ChargeBackActivity.this);
        listView.setPadding(5,5,5,5);
        listView.setDividerHeight(0);
        listView.setBackgroundResource(R.drawable.background_border_white);
        listView.setAdapter(arrayAdapter);

        int listViewWidth = MainApplication.getContext().getScreenWidth() - DPUtil.dip2px(ChargeBackActivity.this, 30);
        popupWindow = new PopupWindow(listView, listViewWidth , AbsListView.LayoutParams.WRAP_CONTENT,  true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(v);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText2.setText(refund_reason.get(position).getTitle());
                refundReasonEntity = refund_reason.get(position);
                popupWindow.dismiss();
            }
        });
    }

    private void requestGetChargeBackInfo(){
        ClientDiscoverAPI.getChargeBackInfo(rId, skuId, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }
                try {
                    HttpResponse<ChargeBackBean> chargeBackBeanHttpResponse = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ChargeBackBean>>(){});
                    if (chargeBackBeanHttpResponse.isSuccess()) {
                        chargeBackBean = chargeBackBeanHttpResponse.getData();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (chargeBackBean != null) {
                    editText1.setText(String.format("¥%s",chargeBackBean.getRefund_price()));
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void requestChargeBack(String price, String refundReason, String refundContent){
        String refund_type = "1";
        ClientDiscoverAPI.getApplyProductRefund(rId, skuId, refund_type, refundReason, refundContent,
                price, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }

                HttpResponse<ChargeBackResultBean> chargeBackResultBeanHttpResponse = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ChargeBackResultBean>>(){});
                LogUtil.e(TAG, "--------> responseInfo: " + responseInfo.result);

                if (chargeBackResultBeanHttpResponse.isSuccess()) {
                    Toast.makeText(activity, App.getString(R.string.hint_salesAfter_chargeBack_request_success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChargeBackActivity.this, ChargeBackDetailsActivity.class);
                    intent.putExtra(KEY.CHARGEBACK_ID, chargeBackResultBeanHttpResponse.getData().getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private PopupWindow popupWindow;
}
