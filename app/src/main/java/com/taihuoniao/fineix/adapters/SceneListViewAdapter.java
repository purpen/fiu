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
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.SubsCjListBean;
import com.taihuoniao.fineix.main.MainApplication;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/19.
 */
public class SceneListViewAdapter extends BaseAdapter {
    private Context context;
    private List<SceneListBean> list;
    private List<LoveSceneBean.LoveSceneItem> loveList;
    private List<SearchBean.SearchItem> searchList;
    private List<SubsCjListBean.SubsCJItem> subsList;
    private DisplayImageOptions options;

    public SceneListViewAdapter(Context context, List<SceneListBean> list, List<LoveSceneBean.LoveSceneItem> loveList,
                                List<SearchBean.SearchItem> searchList, List<SubsCjListBean.SubsCJItem> subsList) {
        this.context = context;
        this.list = list;
        this.loveList = loveList;
        this.searchList = searchList;
        this.subsList = subsList;
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
        } else if (searchList != null) {
            return searchList.size();
        } else if (subsList != null) {
            return subsList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else if (loveList != null) {
            return loveList.get(position);
        } else if (searchList != null) {
            return searchList.get(position);
        } else if (subsList != null) {
            return subsList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
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
//            holder.backgroundImg.setTag(list.get(position).getCover_url());
//            holder.backgroundImg.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg);
            //数据为空
            ImageLoader.getInstance().displayImage(list.get(position).getUser_info().getAvatar_url(), holder.userHeadImg, options);
            holder.userName.setText(list.get(position).getUser_info().getNickname());
            isSpertAndSummary(holder.userInfo, list.get(position).getUser_info().getIs_expert(), list.get(position).getUser_info().getSummary());
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
            isSpertAndSummary(holder.userInfo, loveList.get(position).getUser_info().getIs_expert(), loveList.get(position).getUser_info().getSummary());
            holder.viewCount.setText(loveList.get(position).getView_count());
            holder.loveCount.setText(loveList.get(position).getLove_count());
            holder.sceneTitle.setText(loveList.get(position).getTitle());
            holder.suoshuQingjing.setText(loveList.get(position).getScene_title());
            holder.location.setText(loveList.get(position).getAddress());
            holder.time.setText(loveList.get(position).getCreated_at());
        } else if (searchList != null) {
            ImageLoader.getInstance().displayImage(searchList.get(position).getCover_url(), holder.backgroundImg);
//            Log.e("<<<", "用户头像url=" + loveList.get(position).getUser_info().getAvatar_ur());
            ImageLoader.getInstance().displayImage(searchList.get(position).getUser_info().getAvatar_url(), holder.userHeadImg, options);
            holder.userName.setText(searchList.get(position).getUser_info().getNickname());
            isSpertAndSummary(holder.userInfo, searchList.get(position).getUser_info().getIs_expert(), searchList.get(position).getUser_info().getSummary());
            holder.viewCount.setText(searchList.get(position).getView_count());
            holder.loveCount.setText(searchList.get(position).getLove_count());
            holder.sceneTitle.setText(searchList.get(position).getTitle());
            holder.suoshuQingjing.setText(searchList.get(position).getScene_title());
            holder.location.setText(searchList.get(position).getAddress());
            holder.time.setText(searchList.get(position).getCreated_at());
        } else if (subsList != null) {
            ImageLoader.getInstance().displayImage(subsList.get(position).getCover_url(), holder.backgroundImg);
//            Log.e("<<<", "用户头像url=" + loveList.get(position).getUser_info().getAvatar_ur());
            ImageLoader.getInstance().displayImage(subsList.get(position).getUser_info().getAvatar_url(), holder.userHeadImg, options);
            holder.userName.setText(subsList.get(position).getUser_info().getNickname());
            isSpertAndSummary(holder.userInfo, subsList.get(position).getUser_info().getIs_expert(), subsList.get(position).getUser_info().getSummary());
            holder.viewCount.setText(subsList.get(position).getView_count());
            holder.loveCount.setText(subsList.get(position).getLove_count());
            holder.sceneTitle.setText(subsList.get(position).getTitle());
            holder.suoshuQingjing.setText(subsList.get(position).getScene_title());
            holder.location.setText(subsList.get(position).getAddress());
            holder.time.setText(subsList.get(position).getCreated_at());
        }
        if (holder.sceneTitle.getText().length() < 8) {
            holder.sceneTitle.setTextSize(40);
        } else if (holder.sceneTitle.getText().length() >= 13) {
            holder.sceneTitle.setTextSize(20);
        } else {
            holder.sceneTitle.setTextSize(30);
        }
        return convertView;
    }

    private void isSpertAndSummary(TextView userInfo, String isSpert, String summary) {
        if ("1".equals(isSpert) && (summary == null || "null".equals(summary))) {
            userInfo.setText("达人");
        } else if ("1".equals(isSpert)) {
            userInfo.setText(String.format("%s | %s", "达人", summary));
        } else if (summary == null || "null".equals(summary)) {
            userInfo.setText("");
        } else {
            userInfo.setText(summary);
        }
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