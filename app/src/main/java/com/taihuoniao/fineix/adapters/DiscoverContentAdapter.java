package com.taihuoniao.fineix.adapters;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DiscoverBean;
import com.taihuoniao.fineix.beans.DiscoverIndexBean;
import com.taihuoniao.fineix.home.GoToNextUtils;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.product.GoodsListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJCategoryActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by lilin on 2017/2/17.
 */

public class DiscoverContentAdapter extends CommonBaseAdapter implements PlatformActionListener {
    private DiscoverBean discoverBean;
    private List<DiscoverIndexBean> titles;

    public DiscoverContentAdapter(Activity activity, DiscoverBean discoverBean, List<DiscoverIndexBean> titles) {
        super(titles.subList(1, titles.size()), activity);
        this.discoverBean = discoverBean;
        this.titles = titles;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = View.inflate(activity, R.layout.item_discover_category, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.gvStick.setVisibility(View.GONE);
        holder.gvCategory.setVisibility(View.GONE);
        holder.view.setVisibility(View.GONE);
        holder.gvStick.setVisibility(View.GONE);
        holder.gvCategory.setVisibility(View.GONE);
        switch (position) {
            case 0: //分类
                holder.tvTitle.setText(titles.get(position + 1).indexName);
                holder.gvCategory.setVisibility(View.VISIBLE);
                holder.gvCategory.setAdapter(new DiscoverGVCategoryAdapter(discoverBean.pro_category, activity));
                holder.gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(activity, GoodsListActivity.class);
                        intent.putExtra("id", discoverBean.pro_category.get(i)._id);
                        intent.putExtra("name", discoverBean.pro_category.get(i).title);
                        activity.startActivity(intent);
                    }
                });
                break;
            case 1: //地盘
                holder.tvTitle.setText(titles.get(position + 1).indexName);
                holder.gvStick.setVisibility(View.VISIBLE);
                holder.gvStick.setAdapter(new DiscoverZoneRecommendAdapter(discoverBean.scene.stick, activity));
                holder.gvStick.setOnItemClickListener(new AdapterView.OnItemClickListener() { //banner
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DiscoverBean.SceneBean.StickBeanX beanXX = discoverBean.scene.stick.get(i);
                        GoToNextUtils.goToIntent(activity, Integer.valueOf(beanXX.type), beanXX.web_url);
                    }
                });
                break;
            case 2: //情境
                holder.tvTitle.setText(titles.get(position + 1).indexName);
                holder.gvStick.setVisibility(View.VISIBLE);
                holder.gvStick.setAdapter(new DiscoverQJRecommendAdapter(discoverBean.sight.stick, activity));
                holder.gvStick.setOnItemClickListener(new AdapterView.OnItemClickListener() { //banner
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DiscoverBean.SightBean.StickBeanXX beanXX = discoverBean.sight.stick.get(i);
                        GoToNextUtils.goToIntent(activity, Integer.valueOf(beanXX.type), beanXX.web_url);
                    }
                });
                holder.gvCategory.setVisibility(View.VISIBLE); //情境分类
                holder.gvCategory.setAdapter(new DiscoverQJCategoryAdapter(discoverBean.sight.category, activity));
                holder.gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(activity, QJCategoryActivity.class);
                        intent.putExtra("id", discoverBean.sight.category.get(i)._id);
                        intent.putExtra("name", discoverBean.sight.category.get(i).title);
                        activity.startActivity(intent);
                    }
                });
                break;
            case 3: //品牌
                holder.tvTitle.setText(titles.get(position + 1).indexName);
                holder.gvCategory.setVisibility(View.VISIBLE);
                holder.gvCategory.setAdapter(new DiscoverBrandCategoryAdapter(discoverBean.brand, activity));
                holder.gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(activity, BrandDetailActivity.class);
                        intent.putExtra("id", discoverBean.brand.get(i)._id);
                        activity.startActivity(intent);
                    }
                });
                break;
            case 4://好货集合
                holder.tvTitle.setText(titles.get(position + 1).indexName);
                holder.gvStick.setVisibility(View.VISIBLE);
                holder.gvStick.setAdapter(new DiscoverZJRecommendAdapter(discoverBean.product_subject, activity));
                holder.gvStick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        GoToNextUtils.goToIntent(activity, Integer.valueOf(discoverBean.product_subject.get(i).type), discoverBean.product_subject.get(i).web_url);
                    }
                });
                break;
            case 5: //发现好友
                holder.view.setVisibility(View.VISIBLE);
                holder.tvTitle.setText(titles.get(position + 1).indexName);
                holder.gvCategory.setVisibility(View.VISIBLE);
                int[] imgs = {R.mipmap.share_wechat, R.mipmap.share_weibo, R.mipmap.share_user};
                String[] strs = {activity.getString(R.string.invite_wechat), activity.getString(R.string.link_weibo), activity.getString(R.string.link_contact)};
                ArrayList<DiscoverBean.UsersBean> list = new ArrayList<>();
                DiscoverBean.UsersBean usersBean;
                for (int i = 0; i < imgs.length; i++) {
                    usersBean = new DiscoverBean.UsersBean();
                    usersBean.pic = imgs[i];
                    usersBean.nickname = strs[i];
                    list.add(usersBean);
                }
                holder.gvCategory.setAdapter(new DiscoverFreindsCategoryAdapter(list, activity));
                holder.gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Platform.ShareParams params;
                        switch (i) {
                            case 0:
                                params = new Platform.ShareParams();
                                params.setShareType(Platform.SHARE_TEXT);
                                params.setTitle(activity.getString(R.string.app_name));
                                params.setText(activity.getString(R.string.share_txt));
                                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                wechat.setPlatformActionListener(DiscoverContentAdapter.this);
                                wechat.share(params);
                                break;
                            case 1:
                                params = new Platform.ShareParams();
                                params.setText(activity.getString(R.string.share_txt));
                                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                                weibo.setPlatformActionListener(DiscoverContentAdapter.this); // 设置分享事件回调
                                weibo.share(params);
                                break;
                            case 2:
                                Uri sms = Uri.parse("smsto:");
                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, sms);
                                sendIntent.putExtra("sms_body", activity.getString(R.string.share_txt));
                                activity.startActivity(sendIntent);
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            default:
                break;
        }
        return convertView;
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.showSuccess("分享成功");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.showInfo("您取消了分享");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.showError("对不起，分享出错");
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_coupon)
        ImageView ivCoupon;
        @Bind(R.id.gv_stick)
        CustomGridView gvStick;
        @Bind(R.id.gv_category)
        CustomGridView gvCategory;
        @Bind(R.id.view)
        View view;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
