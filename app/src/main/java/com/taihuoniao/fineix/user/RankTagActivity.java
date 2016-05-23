package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.labelview.AutoLabelUI;
import com.taihuoniao.fineix.view.labelview.Label;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/5/22 15:49
 */
public class RankTagActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.et)
    EditText et;
    @Bind(R.id.label_view)
    AutoLabelUI label_view;
    @Bind(R.id.tv_tag)
    TextView tv_tag;
    public RankTagActivity(){
        super(R.layout.activity_rank_tag);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"身份标签");
        String[] stringArray = getResources().getStringArray(R.array.user_tags);
        for (int i=0;i<stringArray.length;i++){
            label_view.addLabel(stringArray[i]);
        }
    }

    @Override
    protected void installListener() {
        label_view.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(Label labelClicked) {
                tv_tag.setVisibility(View.VISIBLE);
                et.setHint("");
                et.setEnabled(false);
                tv_tag.setText(labelClicked.getText());
            }
        });

    }

    @OnClick({R.id.btn,R.id.iv_clear,R.id.et})
    void performClick(View view){
        switch (view.getId()){
            case R.id.btn: //申请官方认证
                startActivity(new Intent(activity,OfficialCertificateActivity.class));
                break;
            case R.id.iv_clear:
                tv_tag.setText("");
                tv_tag.setVisibility(View.GONE);
                et.setEnabled(true);
                et.getText().clear();
                et.setHint(R.string.select_tag_tip);
                break;
        }
    }

}
