package com.taihuoniao.fineix.user;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.AuthData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/5/31 0:45
 */
public class CertificateStatusActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.iv_label)
    ImageView iv_label;
    @Bind(R.id.tv_info)
    TextView tv_info;
    @Bind(R.id.rl_certificating)
    RelativeLayout rl_certificating;
    @Bind(R.id.rl_no_pass)
    RelativeLayout rl_no_pass;
    @Bind(R.id.ll_edit_pass)
    LinearLayout ll_edit_pass;
    @Bind(R.id.ll_first_auth)
    LinearLayout ll_first_auth;
    private AuthData authData;
    private HashMap<String,Integer> labelMap=new HashMap<>();
    private SVProgressHUD dialog;
    public static CertificateStatusActivity instance;
    public static final int[] labelsImg={R.mipmap.dana,R.mipmap.hj,R.mipmap.xingshe,R.mipmap.yishufan,R.mipmap.shouyiren,R.mipmap.renlaifeng,R.mipmap.shuhui,R.mipmap.buyer};
    public CertificateStatusActivity(){
        super(R.layout.activity_certificate_status);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"官方认证");
        instance=this;
        dialog=new SVProgressHUD(this);
        String[] labels = getResources().getStringArray(R.array.official_tags);
        for (int i=0;i<labels.length;i++){
            labelMap.put(labels[i],labelsImg[i]);
        }
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.getAuthStatus(new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog!=null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog!=null) dialog.dismiss();
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getAuthStatus",responseInfo.result);
                HttpResponse<AuthData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<AuthData>>() {});
                if (response.isSuccess()){
                    authData = response.getData();
                    refreshUI();
                    return;
                }else { //首次参加认证
                    ll_first_auth.setVisibility(View.VISIBLE);
                }
//                dialog.showErrorWithStatus(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog!=null) dialog.dismiss();
                dialog.showErrorWithStatus("网络异常，请保持网络畅通");
            }
        });
    }

    @Override
    protected void refreshUI() {//审核状态：0.未审核；1.拒绝；2.通过
       switch (authData.verified){
           case 0:
                rl_certificating.setVisibility(View.VISIBLE);
               break;
           case 1:
                rl_no_pass.setVisibility(View.VISIBLE);
               break;
           case 2:
               ll_edit_pass.setVisibility(View.VISIBLE);
               setPassInfo();
               break;
       }
    }


    private void setPassInfo(){
        if (!TextUtils.isEmpty(authData.label)){
            if (labelMap.containsKey(authData.label)){
                iv_label.setImageResource(labelMap.get(authData.label));
            }
        }

        if (!TextUtils.isEmpty(authData.info)){
            tv_info.setText(authData.info);
        }
    }

    @OnClick({R.id.tv_edit,R.id.btn,R.id.btn_do_auth})
    void performClick(View v){
        switch (v.getId()){
            case R.id.btn_do_auth:
                startActivity(new Intent(activity,OfficialCertificateActivity.class));
                break;
            case R.id.tv_edit: //修改认证信息
            case R.id.btn: //重新认证
                doOfficialCertificate();
                break;
        }
    }

    private void doOfficialCertificate(){
        Intent intent=new Intent(activity,OfficialCertificateActivity.class);
        intent.putExtra(AuthData.class.getSimpleName(),authData);
        startActivity(intent);
    }
}
