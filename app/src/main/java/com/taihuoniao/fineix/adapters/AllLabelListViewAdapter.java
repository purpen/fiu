package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.view.GridViewForScrollView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/11.
 */
public class AllLabelListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
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
            hold.gridView = (GridViewForScrollView) convertView.findViewById(R.id.view_alllabel_item_grid);
            hold.moreTv = (TextView) convertView.findViewById(R.id.view_alllabel_item_more);
            hold.moreImg = (ImageView) convertView.findViewById(R.id.view_alllabel_item_moreimg);
            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
        hold.nameTv.setText(allLabelList.get(position).getTitle_cn());
        final AllLabelGridViewAdapter allLabelGridViewAdapter = new AllLabelGridViewAdapter(context, allLabelList.get(position));
        hold.gridView.setAdapter(allLabelGridViewAdapter);
        hold.gridView.setOnItemClickListener(this);
        hold.gridView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("<<<", "top = " + top + ",bottom = " + bottom +
                        ",oldTop = " + oldTop + ",oldBottom = " + oldBottom);
                if (bottom - top > oldBottom - oldTop && oldBottom != 0) {
                    moreClick.moreClick(pos, position, bottom - top - oldBottom + oldTop);
                }
            }
        });
        //position 二级目录角标
        hold.gridView.setTag(position);
        hold.moreTv.setTag(position);
        hold.moreImg.setTag(position);
        if (allLabelGridViewAdapter.getType() == 1) {
            hold.moreImg.setVisibility(View.VISIBLE);
            hold.moreTv.setVisibility(View.VISIBLE);
        } else {
            hold.moreTv.setVisibility(View.GONE);
            hold.moreImg.setVisibility(View.GONE);
        }
        hold.moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allLabelGridViewAdapter.setPage(1 + allLabelGridViewAdapter.getPage());
                allLabelGridViewAdapter.notifyDataSetChanged();
//                notifyDataSetChanged();
//                hold.gridView.invalidate();
                hold.gridView.requestLayout();
//                allLabelViewPagerAdapter1.notifyDataSetChanged();
                if (allLabelGridViewAdapter.getType() == 1) {
                    hold.moreImg.setVisibility(View.VISIBLE);
                    hold.moreTv.setVisibility(View.VISIBLE);
                } else {
                    hold.moreTv.setVisibility(View.GONE);
                    hold.moreImg.setVisibility(View.GONE);
                }
//                moreClick.moreClick(pos,position);
            }
        });
        hold.moreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allLabelGridViewAdapter.setPage(1 + allLabelGridViewAdapter.getPage());
                allLabelGridViewAdapter.notifyDataSetChanged();
//                hold.gridView.invalidate();
                hold.gridView.requestLayout();
//                notifyDataSetChanged();
//                allLabelViewPagerAdapter1.notifyDataSetChanged();
                if (allLabelGridViewAdapter.getType() == 1) {
                    hold.moreImg.setVisibility(View.VISIBLE);
                    hold.moreTv.setVisibility(View.VISIBLE);
                } else {
                    hold.moreTv.setVisibility(View.GONE);
                    hold.moreImg.setVisibility(View.GONE);
                }
//                moreClick.moreClick(pos,position);
            }
        });
        return convertView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        labelClick.click(allLabelList.get((Integer) parent.getTag()).getChildren().get(position));
    }

    public interface MoreClick {
        void moreClick(int position1, int position2, int distance);
    }


    class ViewHolder {
        private TextView nameTv;
        private GridViewForScrollView gridView;
        private TextView moreTv;
        private ImageView moreImg;
    }

}
