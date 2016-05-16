package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.view.GridViewForScrollView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/8.
 */
public class HotLabelViewPagerAdapter extends PagerAdapter implements AdapterView.OnItemClickListener {
    private Context context;
    private List<UsedLabelBean> usedLabelList;
    private List<HotLabel.HotLabelBean> hotLabelList;
//    private HotLaelAdapter hotLabelAdapter;
//    private UsedLabelAdapter usedLabelAdapter;
    private LabelClick labelClick;

    public HotLabelViewPagerAdapter(Context context, List<UsedLabelBean> usedLabelList, List<HotLabel.HotLabelBean> hotLabelList, LabelClick labelClick) {
        this.context = context;
        this.usedLabelList = usedLabelList;
        this.hotLabelList = hotLabelList;

        this.labelClick = labelClick;
    }

    @Override
    public int getCount() {
//        if (usedLabelList.size() == 0 && hotLabelList.size() > 0)
//            return 1;
//        if (usedLabelList.size() > 0 && hotLabelList.size() > 0)
//            return 2;
        if (usedLabelList.size() == 0) {
            return 1;
        }
        return 2;
    }

    @Override
    public void notifyDataSetChanged() {
//        usedLabelAdapter.notifyDataSetChanged();
//        hotLabelAdapter.notifyDataSetChanged();
//        destroyItem(container, 0, object);
//        destroyItem(container, 1, object);
//        instantiateItem(container, 0);
//        instantiateItem(container,1);
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.view_label_gridview, null);
//        if (usedLabelList.size() == 0 && hotLabelList.size() > 0) {
//
//        } else if (usedLabelList.size() > 0 && hotLabelList.size() > 0) {
//
//        }
        GridViewForScrollView gridView = (GridViewForScrollView) view.findViewById(R.id.view_label_gridview_grid);
        gridView.setOnItemClickListener(this);
        if (usedLabelList.size() == 0) {
            gridView.setAdapter(new HotLaelAdapter());
        } else {
            if (position == 0) {
                Log.e("<<<使用过的标签适配", "");
                gridView.setAdapter(new UsedLabelAdapter());
            } else if (position == 1) {
                Log.e("<<<热门标签适配", "");
                gridView.setAdapter(new HotLaelAdapter());
            }
        }
//        Log.e("<<<标签位置", "position=" + position);
        container.addView(view);
        return view;
    }

    private ViewGroup container;
    private Object object;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        this.container = container;
        this.object = object;
        container.removeView(container.getChildAt(position));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        labelClick.click(parent.getAdapter().getItem(position));
    }

    public interface LabelClick {
        void click(Object object);
    }

    private class UsedLabelAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return usedLabelList.size() > 15 ? 15 : usedLabelList.size();
        }

        @Override
        public Object getItem(int position) {
            return usedLabelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.view_labellist_item, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.view_labellist_item_tv);
            textView.setText(usedLabelList.get(position).getTitle_cn());
            Log.e("<<<使用过的标签", usedLabelList.get(position).getTitle_cn());
            return convertView;
        }
    }

    private class HotLaelAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return hotLabelList.size() > 15 ? 15 : hotLabelList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotLabelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.view_labellist_item, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.view_labellist_item_tv);
            textView.setText(hotLabelList.get(position).getTitle_cn());
            Log.e("<<<热门标签", hotLabelList.get(position).getTitle_cn());
            return convertView;
        }
    }
}
