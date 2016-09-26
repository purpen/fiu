package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.taihuoniao.fineix.adapters.IndexQJListAdapter;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class SceneTitleSetUtils {
    //设置情景标题
    public static void setTitle(final TextView tv1, final TextView tv2, final String title) {
        if (TextUtils.isEmpty(title)) {
            tv1.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
            return;
        }
        if (title.length() > 10) {
            tv2.setText(title.substring(0, 10));
            tv1.setText(title.substring(10, title.length()));
            tv2.setVisibility(View.VISIBLE);
        } else {
            tv2.setText("");
            tv1.setText(title);
            tv2.setVisibility(View.INVISIBLE);
        }
        tv1.setVisibility(View.VISIBLE);
    }

    //设置描述
    public static SpannableString setDes(String des, Activity activity) {
        int sta = 0;
        final SpannableString spannableStringBuilder = new SpannableString(des);
        while (des.substring(sta).contains("#")) {
            IndexQJListAdapter.TextClick textClick;
            sta = des.indexOf("#", sta);
            if (des.substring(sta).contains(" ")) {
                int en = des.indexOf(" ", sta);
                textClick = new IndexQJListAdapter.TextClick(activity, des.substring(sta + 1, en));
                spannableStringBuilder.setSpan(textClick, sta, en + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sta = en;
            } else {
                textClick = new IndexQJListAdapter.TextClick(activity, des.substring(sta + 1, des.length()));
                spannableStringBuilder.setSpan(textClick, sta, des.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            }
        }
        return spannableStringBuilder;
    }
}
