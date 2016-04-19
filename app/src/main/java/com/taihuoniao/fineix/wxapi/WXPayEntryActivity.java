package com.taihuoniao.fineix.wxapi;
import android.content.Intent;
import android.os.Bundle;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";

    public interface WXPayResultListener {
        void onSuccess();
        void onFailure();
        void onCancel();
    }

    private static WXPayResultListener listener;

    public static void setWXPayResultListener(WXPayResultListener listener){
        WXPayEntryActivity.listener=listener;
    }
    private IWXAPI api;

    public WXPayEntryActivity() {
        super(R.layout.pay_result);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, DataConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.e(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
            String resultTip = "";
            switch (resp.errCode) {
                case 0:
                    resultTip = "支付成功";
                    if (listener!=null){
                        listener.onSuccess();
                    }
                    Util.makeToast(activity, resultTip);
                    break;
                case -1:
                    resultTip = "支付异常";
                    if (listener!=null){
                        listener.onFailure();
                    }
                    Util.makeToast(activity, resultTip);
                    break;
                case -2:
                    resultTip = "您取消了支付";
                    if (listener!=null){
                        listener.onCancel();
                    }
                    Util.makeToast(activity, resultTip);
                    break;
            }
//			builder.setMessage(resultTip);
//			builder.show();
//            startActivity(new Intent(activity, UserOrderListActivity.class));
            finish();
        }
    }
}