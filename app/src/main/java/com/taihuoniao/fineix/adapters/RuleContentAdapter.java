package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.utils.Util;

/**
 * @author lilin
 *         created at 2016/8/29 14:16
 */
public class RuleContentAdapter extends BaseAdapter {
    private SubjectData data;
    private Activity activity;

    public RuleContentAdapter(SubjectData data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = Util.inflateView(R.layout.activity_detail_footer, null);
        params.height = outRect.height() - activity.getResources().getDimensionPixelSize(R.dimen.dp300);
        view.setLayoutParams(params);
        TextView tv_rule_content = (TextView) view.findViewById(R.id.tv_rule_content);
        tv_rule_content.setText(data.summary);
        Button btn = (Button) view.findViewById(R.id.btn);
        if (data.evt == 2) {
            btn.setVisibility(View.GONE);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //参加活动
            }
        });
        return view;
    }
}
