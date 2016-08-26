package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
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
    private int i = Util.getScreenWidth() - 3 * activity.getResources().getDimensionPixelSize(R.dimen.dp16);
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, i / 2, 1);
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
        final ViewHolder holder;
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
            holder.rl_right.setLayoutParams(params);
            holder.rl_right.setVisibility(View.VISIBLE);
            right_qj = list.get(2 * position + 1);
            imageLoader.displayImage(right_qj.getCover_url(), holder.iv_cover_right, options);
            if (!TextUtils.isEmpty(left_qj.getTitle())) {
                holder.tv_title_right.setText(right_qj.getTitle());
                holder.tv_title_right.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
            }
            holder.tv_desc_right.setText(right_qj.getAddress());
        }

        imageLoader.displayImage(left_qj.getCover_url(), holder.iv_cover_left, options);
        if (!TextUtils.isEmpty(left_qj.getTitle())) {
            holder.tv_title_left.setText(left_qj.getTitle());
            holder.tv_title_left.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
        }
        holder.rl_left.setLayoutParams(params);
        holder.tv_desc_left.setText(left_qj.getAddress());
        holder.rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SceneDetailActivity.class);
                intent.putExtra("id", left_qj.get_id());
                activity.startActivity(intent);
            }
        });
        final SceneListBean finalRight_qj = right_qj;
        holder.rl_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SceneDetailActivity.class);
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
