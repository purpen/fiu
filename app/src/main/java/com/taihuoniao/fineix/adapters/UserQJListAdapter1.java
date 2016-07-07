package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/5/16 15:44
 */
public class UserQJListAdapter1 extends CommonBaseAdapter<SceneListBean> {
    private ImageLoader imageLoader;

    public UserQJListAdapter1(List<SceneListBean> list, Activity activity) {
        super(list, activity);
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        int size = super.getCount();
        return (size % 2 == 0 ? size / 2 : size / 2 + 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_grid_qj, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SceneListBean left_qj;
        SceneListBean right_qj = null;
        left_qj = list.get(2 * position);

        if (position == list.size() / 2) {
            holder.rl_right.setVisibility(View.INVISIBLE);
        } else {
            holder.rl_right.setVisibility(View.VISIBLE);
            right_qj = list.get(2 * position + 1);
            imageLoader.displayImage(right_qj.getCover_url(), holder.iv_cover_right, options);
            holder.tv_title_right.setText(right_qj.getTitle());
            SceneTitleSetUtils.setTitle(holder.tv_title_right, holder.item_right_frame);
            holder.tv_desc_right.setText(right_qj.getAddress());
        }

        imageLoader.displayImage(left_qj.getCover_url(), holder.iv_cover_left, options);
        holder.tv_title_left.setText(left_qj.getTitle());
        SceneTitleSetUtils.setTitle(holder.tv_title_left, holder.item_left_frame);
        holder.tv_desc_left.setText(left_qj.getAddress());
        holder.rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QingjingDetailActivity.class);
                intent.putExtra("id", left_qj.get_id());
                activity.startActivity(intent);
            }
        });
        final SceneListBean finalRight_qj = right_qj;
        holder.rl_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QingjingDetailActivity.class);
                intent.putExtra("id", finalRight_qj.get_id());
                activity.startActivity(intent);
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.rl_left)
        RelativeLayout rl_left;
        @Bind(R.id.iv_cover_left)
        ImageView iv_cover_left;
        @Bind(R.id.tv_title_left)
        TextView tv_title_left;
        @Bind(R.id.tv_desc_left)
        TextView tv_desc_left;

        @Bind(R.id.item_left_frame)
        FrameLayout item_left_frame;

        @Bind(R.id.item_right_frame)
        FrameLayout item_right_frame;

        @Bind(R.id.rl_right)
        RelativeLayout rl_right;
        @Bind(R.id.iv_cover_right)
        ImageView iv_cover_right;
        @Bind(R.id.tv_title_right)
        TextView tv_title_right;
        @Bind(R.id.tv_desc_right)
        TextView tv_desc_right;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
