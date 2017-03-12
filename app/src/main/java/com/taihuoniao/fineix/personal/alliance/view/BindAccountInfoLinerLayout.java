package com.taihuoniao.fineix.personal.alliance.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.user.fragments.AddressSelectFragment;
import com.taihuoniao.fineix.utils.StringFormatUtils;

/**
 * Created by Stephen on 2017/3/9 17:28
 * Email: 895745843@qq.com
 */

public class BindAccountInfoLinerLayout extends LinearLayout {
    private ImageView imageViewAllianceWithdrawAccountIcon;
    private TextView textViewAllianceWithdrawAccountDescription;
    private TextView textViewAllianceWithdrawAccountDescription2;
    private TextView textViewDefaultText;
    private TextView textViewSetDefaultBank;

    private boolean selected;

    public BindAccountInfoLinerLayout(Context context) {
        super(context);
        initView(context);
    }

    public BindAccountInfoLinerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BindAccountInfoLinerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_alliance_bind_account_info, this, true);
        imageViewAllianceWithdrawAccountIcon = (ImageView) findViewById(R.id.imageView_alliance_withdraw_account_icon);
        textViewAllianceWithdrawAccountDescription = (TextView) findViewById(R.id.textView_alliance_withdraw_account_description);
        textViewAllianceWithdrawAccountDescription2 = (TextView) findViewById(R.id.textView_alliance_withdraw_account_description2);
        textViewDefaultText = (TextView) findViewById(R.id.textView_default_text);
        textViewSetDefaultBank = (TextView) findViewById(R.id.textView_setDefault_bank);
    }

    public void setSelectStatus(boolean selected) {
        this.selected = selected;
        if (selected) {
            textViewSetDefaultBank.setBackgroundResource(R.mipmap.icon_selector_selected);
        } else {
            textViewSetDefaultBank.setBackgroundResource(R.mipmap.icon_selector_normal);
        }
    }

    public void setInitInfo(int kind, String label, String pay_type_label, String account, boolean iSdefault) {
        if (kind == 1) {
            textViewAllianceWithdrawAccountDescription.setText(pay_type_label);
            imageViewAllianceWithdrawAccountIcon.setImageResource(R.mipmap.icon_account_bank);
            textViewAllianceWithdrawAccountDescription2.setText("储蓄卡(尾号 " + account.substring(account.length() - 4) + ")");
        } else if (kind == 2) {
            textViewAllianceWithdrawAccountDescription.setText(label);
            imageViewAllianceWithdrawAccountIcon.setImageResource(R.mipmap.icon_account_alipay);
            textViewAllianceWithdrawAccountDescription2.setText("支付宝(" + StringFormatUtils.formatAccountNumber(account) + ")");
        }
        if (iSdefault) {
            textViewDefaultText.setText("默认提现账户");
        } else {
            textViewDefaultText.setText("");
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public void setOnChangedListener(final GlobalCallBack onCheckedChangeListener, final int index){
        textViewSetDefaultBank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckedChangeListener.callBack(index);
            }
        });
    }
}
