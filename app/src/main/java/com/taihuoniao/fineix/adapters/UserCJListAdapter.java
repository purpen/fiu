//package com.taihuoniao.fineix.adapters;
//
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.beans.SceneListBean;
//import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
//import com.taihuoniao.fineix.utils.Util;
//import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
//
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * @author lilin
// *         created at 2016/5/6 14:45
// */
//public class UserCJListAdapter extends CommonBaseAdapter<SceneListBean> {
//    private ImageLoader imageLoader;
//    private DisplayImageOptions options_head;
//
//    public UserCJListAdapter(List<SceneListBean> list, Activity activity) {
//        super(list, activity);
//        this.imageLoader = ImageLoader.getInstance();
//        options_head = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_background_750_1334)
//                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
//                .showImageOnFail(R.mipmap.default_background_750_1334)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .delayBeforeLoading(0)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final SceneListBean item = list.get(position);
//        final ViewHolder holder;
//        if (convertView == null) {
//            convertView = Util.inflateView(R.layout.item_scenelist, null);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        imageLoader.displayImage(item.getCover_url(), holder.iv_bg, options);
//        imageLoader.displayImage(item.getUser_info().getAvatar_url(), holder.iv_avatar, options_head);
//        if (1 == item.getUser_info().is_expert) {
//            holder.riv_auth.setVisibility(View.VISIBLE);
//            if (!TextUtils.isEmpty(item.getUser_info().expert_info) && !TextUtils.isEmpty(item.getUser_info().expert_label)) {
//                holder.tv_info.setText(String.format("%s | %s", item.getUser_info().expert_label, item.getUser_info().expert_info));
//            }
//        } else {
//            holder.riv_auth.setVisibility(View.GONE);
//            holder.tv_info.setText(item.getUser_info().getSummary());
//        }
//        holder.tv_name.setText(item.getUser_info().getNickname());
//        holder.tv_view_count.setText(item.getView_count());
//        holder.tv_love_count.setText(item.getLove_count());
//        holder.tv_title.setText(item.getTitle());
//        holder.tv_blong.setText(item.getScene_title());
//        holder.tv_location.setText(item.getAddress());
//        holder.time.setText(item.getCreated_at());
//        SceneTitleSetUtils.setTitle(holder.tv_title, holder.frameLayout, holder.sceneImg, 11, -1);
//        return convertView;
//    }
//
//    static class ViewHolder {
//        @Bind(R.id.item_scenelist_backgroundimg)
//        ImageView iv_bg;
//        @Bind(R.id.item_scenelist_user_headimg)
//        RoundedImageView iv_avatar;
//        @Bind(R.id.riv_auth)
//        RoundedImageView riv_auth;
//        @Bind(R.id.item_scenelist_user_name)
//        TextView tv_name;
//        @Bind(R.id.item_scenelist_title_img)
//        ImageView sceneImg;
//        @Bind(R.id.item_scenelist_user_info)
//        TextView tv_info;
//        @Bind(R.id.item_scenelist_view_count)
//        TextView tv_view_count;
//        @Bind(R.id.item_scenelist_frame)
//        FrameLayout frameLayout;
//        @Bind(R.id.item_scenelist_love_count)
//        TextView tv_love_count;
//        @Bind(R.id.item_scenelist_scene_title)
//        TextView tv_title;
//        @Bind(R.id.item_scenelist_suoshuqingjing)
//        TextView tv_blong;
//
//        @Bind(R.id.item_scenelist_location)
//        TextView tv_location;
//
//        @Bind(R.id.item_scenelist_time)
//        TextView time;
//
//
//        public ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}
