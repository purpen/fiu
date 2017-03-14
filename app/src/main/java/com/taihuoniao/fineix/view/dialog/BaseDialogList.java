package com.taihuoniao.fineix.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;


public class BaseDialogList {

    private Dialog mTipDlg = null;
    private LayoutInflater mLayoutInflater = null;
    private Context mContext = null;
    /**
     * 根视图
     */
    private View rootView = null;

    public ListView mListView;
    public TextView tv_cancle;
    public ArrayList<String> mList;//
    private SubmitListener mSubmitListener;
    private TextView tv_title;
    private LinearLayout ll_title;

    public interface SubmitListener {
        void submit(int position);
    }


    /**
     * 构造函数
     */
    public BaseDialogList(Context context, SubmitListener submitListener, String title) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mSubmitListener = submitListener;
        initLayout(title, -1);
        initListener();

    }

    /**
     * 构造函数2
     */
    public BaseDialogList(Context context, SubmitListener submitListener, String title, int textColor) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mSubmitListener = submitListener;
        initLayout(title, textColor);
        initListener();

    }

    public void setContent(ArrayList<String> list) {
        this.mList = list;
        mListView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.dialog_baselist_item, mList));
    }


    /**
     * 初始化视图组件
     *
     * @param title
     */
    private void initLayout(String title, int color) {
        rootView = mLayoutInflater.inflate(R.layout.dialog_baselist, null);
        rootView.setBackgroundColor(Color.parseColor("#efeff4"));
        mListView = (ListView) rootView.findViewById(R.id.lv);
        tv_cancle = (TextView) rootView.findViewById(R.id.tv_cancle);
        tv_title = (TextView) rootView.findViewById(R.id.dialog_title_tv);
        ll_title = (LinearLayout) rootView.findViewById(R.id.dialog_title_ll);

        if (!TextUtils.isEmpty(title)) {
            ll_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
            if (color != -1) {
                tv_title.setTextColor(color);
            }
        } else {
            ll_title.setVisibility(View.GONE);
            mListView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSubmitListener.submit(position);
                mTipDlg.dismiss();
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubmitListener.submit(-1);
                dismiss();
            }
        });
        tv_cancle.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
    }

    public void createDlg() {
        if (mTipDlg == null) {
            int width = Util.getScreenWidth();
            mTipDlg = new Dialog(mContext, R.style.AlertDialog);
            mTipDlg.setContentView(rootView);
            mTipDlg.getWindow().setGravity(Gravity.BOTTOM);
            mTipDlg.getWindow().getAttributes().width = width;   // 设置宽度%p
            mTipDlg.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        }

    }

    public void show() {
        createDlg();
        if (mTipDlg != null && !mTipDlg.isShowing()) {
            mTipDlg.show();
        }
    }

    public void dismiss() {
        if (mTipDlg != null && mTipDlg.isShowing()) {
            mTipDlg.dismiss();
        }
    }
}