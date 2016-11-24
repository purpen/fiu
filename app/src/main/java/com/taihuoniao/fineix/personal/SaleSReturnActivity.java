package com.taihuoniao.fineix.personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.bean.OrderDetailBean;
import com.taihuoniao.fineix.user.returnGoods.ReturnGoodsFragment;
import com.taihuoniao.fineix.utils.DPUtil;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class SalesReturnActivity extends BaseActivity {

    private CustomHeadView customHead;
    private EditText editText1;
    private TextView editText2;
    private EditText editText3;
    private Button buttonCommit;

    private static final String TITLE = "退货";

    public SalesReturnActivity() {
        super(R.layout.activity_salereturn);
    }

    @Override
    protected void initView() {
        customHead = (CustomHeadView) findViewById(R.id.custom_head);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (TextView) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        buttonCommit = (Button) findViewById(R.id.button_commit);
        customHead.setHeadCenterTxtShow(true, TITLE);
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
//                requestChargeBack();
//                Toast.makeText(activity, "提交申请", Toast.LENGTH_SHORT).show();

                requestChargeBack();
            }
        });
    }

    private void showPopupWindow(View v){
        if (popupWindow != null) {
            popupWindow.showAsDropDown(v);
            return;
        }
        List<String> list = new ArrayList<>();
        Collections.addAll(list, chargeBackCause);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesReturnActivity.this, R.layout.simple_list_item_1, R.id.text1, list);
        ListView listView = new ListView(SalesReturnActivity.this);
        listView.setPadding(5,5,5,5);
        listView.setBackgroundResource(R.drawable.background_border_white);
        listView.setAdapter(arrayAdapter);

        popupWindow = new PopupWindow(listView,  DPUtil.dip2px(SalesReturnActivity.this, 166), AbsListView.LayoutParams.WRAP_CONTENT,  true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(v);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText2.setText(chargeBackCause[position]);
                popupWindow.dismiss();
            }
        });
    }

    private void requestChargeBack(){
        String rid = "116112406518";
        String sku_id = "1071045007";
        String refund_type = "0";
        String refund_reason = "不想要了";
        String refund_content = "hvfnadkobvdj";
        String refund_price = "678.00";
        ClientDiscoverAPI.getApplyProductRefund(rid, sku_id, refund_type, refund_reason, refund_content,
                refund_price, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (responseInfo == null) {
                            return;
                        }
                        HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                        LogUtil.e(TAG, "--------> responseInfo: " + responseInfo.result);

                        if (response.isSuccess()) {
                            Toast.makeText(activity, "提交申请成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SalesReturnActivity.this, SalesReturnDetailsActivity.class));
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
    }

    private static final String[] chargeBackCause = {"不喜欢／不想要", "未按约定时间发货", "快递／物流未送到", "快递／物流无跟踪记录"};
    private PopupWindow popupWindow;
}
