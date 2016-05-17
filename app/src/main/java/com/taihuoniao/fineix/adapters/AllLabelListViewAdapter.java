package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.FlowLayout;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/11.
 */
public class AllLabelListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<AllLabelBean> allLabelList;
    private int pos;//一级目录角标
    private MoreClick moreClick;//点击更多
    private HotLabelViewPagerAdapter.LabelClick labelClick;
    private AllLabelViewPagerAdapter1 allLabelViewPagerAdapter1;

    public AllLabelListViewAdapter(Context context, List<AllLabelBean> allLabelList, int pos, MoreClick moreClick,
                                   HotLabelViewPagerAdapter.LabelClick labelClick, AllLabelViewPagerAdapter1 allLabelViewPagerAdapter1) {
        this.context = context;
        this.allLabelList = allLabelList;
        this.pos = pos;
        this.moreClick = moreClick;
        this.labelClick = labelClick;
        this.allLabelViewPagerAdapter1 = allLabelViewPagerAdapter1;
    }


    @Override
    public int getCount() {
        return allLabelList.size();
    }

    @Override
    public Object getItem(int position) {
        return allLabelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder hold;
//        ?convertView复用会导致滑过去之后的数据从新加载
        if (convertView == null) {
            hold = new ViewHolder();
            convertView = View.inflate(context, R.layout.view_alllabel_item, null);
            hold.nameTv = (TextView) convertView.findViewById(R.id.view_alllabel_item_name);
            hold.flowLayout = (FlowLayout) convertView.findViewById(R.id.view_alllabel_item_flow);
//            hold.wordWrapView = (WordWrapView) convertView.findViewById(R.id.view_alllabel_item_flow);
            hold.moreRelative = (RelativeLayout) convertView.findViewById(R.id.view_alllabel_item_morerelative);
            hold.moreTv = (TextView) convertView.findViewById(R.id.view_alllabel_item_more);
            hold.moreImg = (ImageView) convertView.findViewById(R.id.view_alllabel_item_moreimg);
            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
//        Log.e("<<<标签", allLabelList.get(position).toString());
//        hold.autoFitLinearLayout.setAdapter(new AllLabelGridViewAdapter(context, allLabelList.get(position)));
        hold.flowLayout.removeAllViews();
        hold.flowLayout.setLine(allLabelList.get(position).getPage());
//        hold.wordWrapView.removeAllViews();
        hold.nameTv.setText(allLabelList.get(position).getTitle_cn());
        for (int i = 0; i < allLabelList.get(position).getChildren().size(); i++) {
//            View v = View.inflate(context, R.layout.view_labellist_item, null);
//            TextView textView = (TextView) v.findViewById(R.id.view_labellist_item_tv);
            TextView textView = new TextView(context);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setBackgroundResource(R.drawable.background_corner_black);
            textView.setGravity(Gravity.CENTER);
            textView.setMinWidth(DensityUtils.dp2px(context, 60));
            int value = DensityUtils.dp2px(context, 8);
            textView.setPadding(value, value, value, value);
            textView.setSingleLine(true);
            textView.setTextSize(12);
            textView.setTextColor(context.getResources().getColor(R.color.black333333));
            textView.setText(allLabelList.get(position).getChildren().get(i).getTitle_cn());
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    labelClick.click(new UsedLabelBean(allLabelList.get(position).getChildren().get(finalI).get_id(), allLabelList.get(position).getChildren().get(finalI).getTitle_cn()));
                }
            });
            hold.flowLayout.addView(textView);
//            hold.wordWrapView.addView(v);
        }
        hold.moreImg.setTag(position);
        hold.moreRelative.setTag(position);
        hold.moreRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hold.flowLayout.getLine() > -1) {
                    hold.moreTv.setText("收起");
                    allLabelList.get(position).setPage(-1);
                    notifyDataSetChanged();
                    moreClick.moreClick(0, 0, 0);
                }else{
                    hold.moreTv.setText("更多");
                    allLabelList.get(position).setPage(2);
                    notifyDataSetChanged();
                    moreClick.moreClick(0,0,0);
                }
            }
        });
       hold.moreImg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (hold.flowLayout.getLine() > -1) {
                   hold.moreTv.setText("收起");
                   allLabelList.get(position).setPage(-1);
                   notifyDataSetChanged();
                   moreClick.moreClick(0, 0, 0);
               }else{
                   hold.moreTv.setText("更多");
                   allLabelList.get(position).setPage(2);
                   notifyDataSetChanged();
                   moreClick.moreClick(0,0,0);
               }
           }
       });
        return convertView;
    }


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        moreClick.moreClick(pos, position, 0);
    }

    public interface MoreClick {
        void moreClick(int position1, int position2, int distance);
    }


    static class ViewHolder {
        private TextView nameTv;
        private FlowLayout flowLayout;
        private RelativeLayout moreRelative;
        private TextView moreTv;
        private ImageView moreImg;
    }

}
