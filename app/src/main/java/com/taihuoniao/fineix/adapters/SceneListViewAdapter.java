package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.LoveSceneBean;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.MainApplication;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/19.
 */
public class SceneListViewAdapter extends BaseAdapter {
    private Context context;
    private List<SceneListBean> list;
    private List<LoveSceneBean.LoveSceneItem> loveList;
    private DisplayImageOptions options;

    public SceneListViewAdapter(Context context, List<SceneListBean> list, List<LoveSceneBean.LoveSceneItem> loveList) {
        this.context = context;
        this.list = list;
        this.loveList = loveList;
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else if (loveList != null) {
            return loveList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else if (loveList != null) {
            return loveList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_scenelist, null);
            holder = new ViewHolder();
            holder.backgroundImg = (ImageView) convertView.findViewById(R.id.item_scenelist_backgroundimg);
            holder.userHeadImg = (ImageView) convertView.findViewById(R.id.item_scenelist_user_headimg);
            holder.userName = (TextView) convertView.findViewById(R.id.item_scenelist_user_name);
            holder.userInfo = (TextView) convertView.findViewById(R.id.item_scenelist_user_info);
            holder.viewCount = (TextView) convertView.findViewById(R.id.item_scenelist_view_count);
            holder.loveCount = (TextView) convertView.findViewById(R.id.item_scenelist_love_count);
            holder.sceneTitle = (TextView) convertView.findViewById(R.id.item_scenelist_scene_title);
            holder.suoshuQingjing = (TextView) convertView.findViewById(R.id.item_scenelist_suoshuqingjing);
            holder.location = (TextView) convertView.findViewById(R.id.item_scenelist_location);
            holder.time = (TextView) convertView.findViewById(R.id.item_scenelist_time);
            ViewGroup.LayoutParams lp = holder.backgroundImg.getLayoutParams();
            lp.width = MainApplication.getContext().getScreenWidth();
            lp.height = lp.width * 16 / 9;
            holder.backgroundImg.setLayoutParams(lp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg);
            //数据为空
            ImageLoader.getInstance().displayImage(list.get(position).getUser_info().getAvatar_url(), holder.userHeadImg, options);
            holder.userName.setText(list.get(position).getUser_info().getNickname());
            if (list.get(position).getUser_info().getSummary().equals("null")) {
                holder.userInfo.setText(list.get(position).getUser_info().getUser_rank());
            } else {
                holder.userInfo.setText(String.format("%s | %s", list.get(position).getUser_info().getUser_rank(), list.get(position).getUser_info().getSummary()));
            }
            holder.viewCount.setText(list.get(position).getView_count());
            holder.loveCount.setText(list.get(position).getLove_count());
            holder.sceneTitle.setText(list.get(position).getTitle());
            holder.suoshuQingjing.setText(list.get(position).getScene_title());
            holder.location.setText(list.get(position).getAddress());
            holder.time.setText(list.get(position).getCreated_at());
        } else if (loveList != null) {
            ImageLoader.getInstance().displayImage(loveList.get(position).getCover_url(), holder.backgroundImg);
//            Log.e("<<<", "用户头像url=" + loveList.get(position).getUser_info().getAvatar_ur());
            ImageLoader.getInstance().displayImage(loveList.get(position).getUser_info().getAvatar_ur(), holder.userHeadImg, options);
            holder.userName.setText(loveList.get(position).getUser_info().getNickname());
            if (loveList.get(position).getUser_info().getSummary() == null || loveList.get(position).getUser_info().getSummary().equals("null")) {
                holder.userInfo.setText(loveList.get(position).getUser_info().getUser_rank());
            } else {
                holder.userInfo.setText(String.format("%s | %s", loveList.get(position).getUser_info().getUser_rank(), loveList.get(position).getUser_info().getSummary()));
            }
            holder.viewCount.setText(loveList.get(position).getView_count());
            holder.loveCount.setText(loveList.get(position).getLove_count());
            holder.sceneTitle.setText(loveList.get(position).getTitle());
            holder.suoshuQingjing.setText(loveList.get(position).getScene_title());
            holder.location.setText(loveList.get(position).getAddress());
            holder.time.setText(loveList.get(position).getCreated_at());
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView backgroundImg;
        ImageView userHeadImg;
        TextView userName;
        TextView userInfo;
        TextView viewCount;
        TextView loveCount;
        TextView sceneTitle;
        TextView suoshuQingjing;
        TextView location;
        TextView time;
    }
}
