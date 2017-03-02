package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.DiscoverBean;
import com.taihuoniao.fineix.beans.DiscoverIndexBean;
import com.taihuoniao.fineix.home.GoToNextUtils;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.product.GoodsListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJCategoryActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by lilin on 2017/2/17.
 */

public class DiscoverContentAdapter extends BaseAdapter implements PlatformActionListener{
    private static final int COUNT = 6;
    private Activity activity;
    private DiscoverBean discoverBean;
    private List<DiscoverIndexBean> titles;

    public DiscoverContentAdapter(Activity activity, DiscoverBean discoverBean, List<DiscoverIndexBean> titles) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView;
        switch (position) {
            case 0: //推荐
                convertView = View.inflate(activity, R.layout.item_discover_recommend, null);
                textView = ButterKnife.findById(convertView, R.id.tv);
                textView.getPaint().setFakeBoldText(true);
                textView.setText(titles.get(position).indexName);
                ImageView imageView = ButterKnife.findById(convertView, R.id.iv_cover);
                GlideUtils.displayImageFadein(discoverBean.stick.cover_url, imageView);
                ((TextView) ButterKnife.findById(convertView, R.id.tv_title)).setText(discoverBean.stick.title);
                ((TextView) ButterKnife.findById(convertView, R.id.tv_subtitle)).setText(discoverBean.stick.sub_title);
                ButterKnife.findById(convertView,R.id.rl_box).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoToNextUtils.goToIntent(activity, Integer.valueOf(discoverBean.stick.type), discoverBean.stick.web_url);
                    }
                });
                break;
            case 1: //分类
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                textView = ButterKnife.findById(convertView, R.id.tv_title);
                textView.getPaint().setFakeBoldText(true);
                textView.setText(titles.get(position).indexName);
                CustomGridView gv_category = ButterKnife.findById(convertView, R.id.gv_category);
                gv_category.setVisibility(View.VISIBLE);
                gv_category.setAdapter(new DiscoverGVCategoryAdapter(discoverBean.pro_category, activity));
                gv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(activity, GoodsListActivity.class);
                        intent.putExtra("id", discoverBean.pro_category.get(i)._id);
                        intent.putExtra("name", discoverBean.pro_category.get(i).title);
                        activity.startActivity(intent);
                    }
                });
                break;
            case 2: //情境
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                textView = ButterKnife.findById(convertView, R.id.tv_title);
                textView.getPaint().setFakeBoldText(true);
                textView.setText(titles.get(position).indexName);
                CustomGridView qj_gv_stick = ButterKnife.findById(convertView, R.id.gv_stick);
                qj_gv_stick.setVisibility(View.VISIBLE);
                qj_gv_stick.setAdapter(new DiscoverQJRecommendAdapter(discoverBean.sight.stick, activity));
                qj_gv_stick.setOnItemClickListener(new AdapterView.OnItemClickListener() { //banner
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DiscoverBean.SightBean.StickBeanXX beanXX = discoverBean.sight.stick.get(i);
                        LogUtil.e("i===" + i + "get(i)==" + beanXX);
                        GoToNextUtils.goToIntent(activity, Integer.valueOf(discoverBean.sight.stick.get(i).type), discoverBean.sight.stick.get(i).web_url);
                    }
                });
                CustomGridView qj_gv_category = ButterKnife.findById(convertView, R.id.gv_category);
                qj_gv_category.setVisibility(View.VISIBLE); //情境分类
                qj_gv_category.setAdapter(new DiscoverQJCategoryAdapter(discoverBean.sight.category, activity));
                qj_gv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                textView = ButterKnife.findById(convertView, R.id.tv_title);
                textView.getPaint().setFakeBoldText(true);
                textView.setText(titles.get(position).indexName);
                CustomGridView brand_gv_category = ButterKnife.findById(convertView, R.id.gv_category);
                brand_gv_category.setVisibility(View.VISIBLE);
                brand_gv_category.setAdapter(new DiscoverBrandCategoryAdapter(discoverBean.brand, activity));
                brand_gv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(activity, BrandDetailActivity.class);
                        intent.putExtra("id", discoverBean.brand.get(i)._id);
                        activity.startActivity(intent);
                    }
                });
                break;
            case 4://好货集合
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
                textView = ButterKnife.findById(convertView, R.id.tv_title);
                textView.getPaint().setFakeBoldText(true);
                textView.setText(titles.get(position).indexName);
                CustomGridView zj_gv_stick = ButterKnife.findById(convertView, R.id.gv_stick);
                zj_gv_stick.setVisibility(View.VISIBLE);
                zj_gv_stick.setAdapter(new DiscoverQJRecommendAdapter(discoverBean.sight.stick, activity));
                zj_gv_stick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        GoToNextUtils.goToIntent(activity, Integer.valueOf(discoverBean.sight.stick.get(i).type), discoverBean.sight.stick.get(i).web_url);
                    }
                });
                break;
            case 5: //发现好友
                convertView = View.inflate(activity, R.layout.item_discover_category, null);
//                ButterKnife.findById(convertView, R.id.iv_coupon).setVisibility(View.VISIBLE);
                textView = ButterKnife.findById(convertView, R.id.tv_title);
                textView.getPaint().setFakeBoldText(true);
                textView.setText(titles.get(position).indexName);
                CustomGridView friend_gv_category = ButterKnife.findById(convertView, R.id.gv_category);
                friend_gv_category.setVisibility(View.VISIBLE);
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
                friend_gv_category.setAdapter(new DiscoverFreindsCategoryAdapter(list, activity));
                friend_gv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Intent intent = new Intent(activity, UserCenterActivity.class);
//                        intent.putExtra(USER_ID_EXTRA,discoverBean.users.get(i)._id);
//                        activity.startActivity(intent);
                        Platform.ShareParams params;
                        switch (i){
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
                                sendIntent.putExtra("sms_body",activity.getString(R.string.share_txt));
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
}
