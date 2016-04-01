package com.taihuoniao.fineix.provider;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.LogUtil;


/**
 * @author lilin
 * created at 2016/3/30 12:03
 */
public class CartActionProvider extends ActionProvider {
    public final String TAG=getClass().getSimpleName();
    private Context mContext;

    public CartActionProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view =inflater.inflate(R.layout.cart_action_provider_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("onClick",TAG);
            }
        });
        return view;
    }

    public void setNum(int num){
        //设置num
    }
}
