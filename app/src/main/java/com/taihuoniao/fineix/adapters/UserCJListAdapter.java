package com.taihuoniao.fineix.adapters;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/5/6 14:45
 */
public class UserCJListAdapter extends CommonBaseAdapter<SceneListBean> {
    private ImageLoader imageLoader;
    private DisplayImageOptions options_head;

    public UserCJListAdapter(List<SceneListBean> list, Activity activity) {
        super(list, activity);
        this.imageLoader = ImageLoader.getInstance();
        options_head = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .delayBeforeLoading(0)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SceneListBean item = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = Util.inflateView(activity, R.layout.item_scenelist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(item.getCover_url(), holder.iv_bg, options);
        imageLoader.displayImage(item.getUser_info().getAvatar_url(), holder.iv_avatar, options_head);
        holder.tv_name.setText(item.getUser_info().getNickname());
        holder.tv_info.setText(item.getUser_info().getSummary());
        holder.tv_view_count.setText(item.getView_count());
        holder.tv_love_count.setText(item.getLove_count());
        holder.tv_title.setText(item.getTitle());
        holder.tv_blong.setText(item.getScene_title());
        holder.tv_location.setText(item.getAddress());
        holder.time.setText(item.getCreated_at());
        double leng = holder.tv_title.getText().length();
        for (char c : holder.tv_title.getText().toString().toCharArray()) {
            if (c >= 32 && c <= 126) {
                leng -= 0.5;
            }
        }
        int l = 0;
        if (leng * 10 % 10 != 0) {
            l = 1 + (int) leng;
        } else {
            l = (int) leng;
        }
//            遍历所有字符判断是否含有英文字符。有的话算半个
        if (l < 8) {
            holder.tv_title.setTextSize(40);
        } else {
            holder.tv_title.setTextSize(20);
        }
//        动态改变宽高
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.frameLayout.getLayoutParams();
        if (l * holder.tv_title.getTextSize() < DensityUtils.dp2px(activity, 300)) {
            lp.width = (int) (holder.tv_title.getTextSize() * l);
        } else {
            lp.width = DensityUtils.dp2px(activity, 300);
        }
        if (holder.tv_title.getTextSize() < DensityUtils.sp2px(activity, 30) && lp.width <= DensityUtils.dp2px(activity, 300)) {
            lp.height = DensityUtils.dp2px(activity, 28);
        } else {
            lp.height = DensityUtils.dp2px(activity, 55);
        }
        holder.frameLayout.setLayoutParams(lp);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_scenelist_backgroundimg)
        ImageView iv_bg;
        @Bind(R.id.item_scenelist_user_headimg)
        RoundedImageView iv_avatar;
        @Bind(R.id.item_scenelist_user_name)
        TextView tv_name;

        @Bind(R.id.item_scenelist_user_info)
        TextView tv_info;

        @Bind(R.id.item_scenelist_view_count)
        TextView tv_view_count;
        @Bind(R.id.item_scenelist_frame)
        FrameLayout frameLayout;
        @Bind(R.id.item_scenelist_love_count)
        TextView tv_love_count;
        @Bind(R.id.item_scenelist_scene_title)
        TextView tv_title;
        @Bind(R.id.item_scenelist_suoshuqingjing)
        TextView tv_blong;

        @Bind(R.id.item_scenelist_location)
        TextView tv_location;

        @Bind(R.id.item_scenelist_time)
        TextView time;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
