package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

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
    private SVProgressHUD dialog;

    @Override
    protected void getIntentData() {
        target_id = getIntent().getStringExtra("target_id");
        type = getIntent().getStringExtra("type");
        if (target_id == null || type == null) {
            Toast.makeText(ReportActivity.this, "暂不可举报", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public ReportActivity() {
        super(R.layout.activity_report);
    }

    @Override
    protected void initView() {
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImgVisible(false);
        titleLayout.setTitle(R.string.report, getResources().getColor(R.color.black333333));
        titleLayout.setRightTv(R.string.cancel, getResources().getColor(R.color.black333333), ReportActivity.this);
        dialog = new SVProgressHUD(ReportActivity.this);
    }

    @OnClick({R.id.activity_report_sexual_violence, R.id.activity_report_steal_picture, R.id.activity_report_advertising_cheat})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_report_sexual_violence:
                dialog.show();
                DataPaser.report(target_id, type, 1 + "", handler);
                break;
            case R.id.activity_report_steal_picture:
                dialog.show();
                DataPaser.report(target_id, type, 2 + "", handler);
                break;
            case R.id.activity_report_advertising_cheat:
                dialog.show();
                DataPaser.report(target_id, type, 3 + "", handler);
                break;
            case R.id.title_continue:
                onBackPressed();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.REPORT:
                    dialog.dismiss();
                    NetBean netBean = (NetBean) msg.obj;
                    if (netBean.isSuccess()) {
                        reportLinear.setVisibility(View.GONE);
                        successTv.setVisibility(View.VISIBLE);
                        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), ReportActivity.this);
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
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
