package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DiscoverBean;
import com.taihuoniao.fineix.beans.DiscoverIndexBean;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.view.CustomGridView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lilin on 2017/2/17.
 */

public class DiscoverContentAdapter extends BaseAdapter {
    private static final int COUNT = 6;
    private Activity activity;
    private DiscoverBean discoverBean;
    private List<DiscoverIndexBean> titles;
    public DiscoverContentAdapter(Activity activity, DiscoverBean discoverBean, List<DiscoverIndexBean> titles)  {
        this.activity = activity;
        this.discoverBean = discoverBean;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (position) {
            case 0: //推荐
                convertView = View.inflate(activity, R.layout.item_discover_recommend, null);
                ((TextView)ButterKnife.findById(convertView, R.id.tv)).setText(titles.get(position).indexName);
                GlideUtils.displayImageFadein(discoverBean.stick.cover_url, (ImageView) ButterKnife.findById(convertView, R.id.iv_cover));
                ((TextView) ButterKnife.findById(convertView, R.id.tv_title)).setText(discoverBean.stick.sub_title);
                break;
            case 1: //分类
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                ((TextView)ButterKnife.findById(convertView, R.id.tv_title)).setText(titles.get(position).indexName);
                CustomGridView gv_category1 = ButterKnife.findById(convertView, R.id.gv_category);
                gv_category1.setVisibility(View.VISIBLE);
                gv_category1.setAdapter(new DiscoverGVCategoryAdapter(discoverBean.pro_category, activity));
                break;
            case 2: //情境
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                ((TextView)ButterKnife.findById(convertView, R.id.tv_title)).setText(titles.get(position).indexName);
                CustomGridView gv_stick2 = ButterKnife.findById(convertView, R.id.gv_stick);
                gv_stick2.setVisibility(View.VISIBLE);
                gv_stick2.setAdapter(new DiscoverQJRecommendAdapter(discoverBean.sight.stick,activity));

                CustomGridView qj_gv_category = ButterKnife.findById(convertView, R.id.gv_category);
                qj_gv_category.setVisibility(View.VISIBLE);
                qj_gv_category.setAdapter(new DiscoverQJCategoryAdapter(discoverBean.sight.category, activity));
                break;
            case 3: //品牌
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                ((TextView)ButterKnife.findById(convertView, R.id.tv_title)).setText(titles.get(position).indexName);
                CustomGridView brand_gv_category = ButterKnife.findById(convertView, R.id.gv_category);
                brand_gv_category.setVisibility(View.VISIBLE);
                brand_gv_category.setAdapter(new DiscoverBrandCategoryAdapter(discoverBean.brand, activity));
                break;
            case 4://专辑
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                ((TextView)ButterKnife.findById(convertView, R.id.tv_title)).setText(titles.get(position).indexName);
                CustomGridView zj_gv_stick = ButterKnife.findById(convertView, R.id.gv_stick);
                zj_gv_stick.setVisibility(View.VISIBLE);
                zj_gv_stick.setAdapter(new DiscoverQJRecommendAdapter(discoverBean.sight.stick,activity));
                break;
            case 5: //发现好友
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                ((TextView)ButterKnife.findById(convertView, R.id.tv_title)).setText(titles.get(position).indexName);
                CustomGridView friend_gv_category = ButterKnife.findById(convertView, R.id.gv_category);
                friend_gv_category.setVisibility(View.VISIBLE);
                friend_gv_category.setAdapter(new DiscoverFreindsCategoryAdapter(discoverBean.users, activity));
                break;
            default:
                break;
        }
        return convertView;
    }
}
