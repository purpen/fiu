package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by taihuoniao on 2016/4/28.
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的数据
    private String target_id;//关联id
    private String type;//类型 ３、情景；４、场景；５、用户
    @Bind(R.id.activity_report_titlelayout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_report_reportlinear)
    LinearLayout reportLinear;
    @Bind(R.id.activity_report_sexual_violence)
    TextView sexVioTv;
    @Bind(R.id.activity_report_steal_picture)
    TextView stealPicTv;
    @Bind(R.id.activity_report_advertising_cheat)
    TextView adCheatTv;
    @Bind(R.id.activity_report_success)
    TextView successTv;
    private WaittingDialog dialog;

    @Override
    protected void getIntentData() {
        target_id = getIntent().getStringExtra("target_id");
        type = getIntent().getStringExtra("type");
        if (target_id == null || type == null) {
            ToastUtils.showError("暂不可举报");
            finish();
        }
    }

    public ReportActivity() {
        super(R.layout.activity_report);
    }

    @Override
    protected void initView() {
        titleLayout.setBackImgVisible(false);
        titleLayout.setTitle(R.string.report);
        titleLayout.setRightTv(R.string.cancel, getResources().getColor(R.color.white), ReportActivity.this);
        dialog = new WaittingDialog(ReportActivity.this);
        WindowUtils.chenjin(this);
    }

    @OnClick({R.id.activity_report_sexual_violence, R.id.activity_report_steal_picture, R.id.activity_report_advertising_cheat})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_report_sexual_violence:
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                report(target_id, type, 1 + "");
                break;
            case R.id.activity_report_steal_picture:
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                report(target_id, type, 2 + "");
                break;
            case R.id.activity_report_advertising_cheat:
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                report(target_id, type, 3 + "");
                break;
            case R.id.title_continue:
                onBackPressed();
                break;
        }
    }

    private HttpHandler<String> reportHandler;

    private void report(String target_id, String type, String evt) {
        reportHandler = ClientDiscoverAPI.report(target_id, type, evt, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常" + e.toString());
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    reportLinear.setVisibility(View.GONE);
                    successTv.setVisibility(View.VISIBLE);
                    titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.white), ReportActivity.this);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (reportHandler != null)
            reportHandler.cancel();
        super.onDestroy();
    }
    //    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DataConstants.REPORT:
//                    dialog.dismiss();
//                    NetBean netBean = (NetBean) msg.obj;
//                    if (netBean.isSuccess()) {
//                        reportLinear.setVisibility(View.GONE);
//                        successTv.setVisibility(View.VISIBLE);
//                        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), ReportActivity.this);
//                    }
//                    break;
//                case DataConstants.NET_FAIL:
//                    dialog.dismiss();
//                    break;
//            }
//        }
//    };

}
