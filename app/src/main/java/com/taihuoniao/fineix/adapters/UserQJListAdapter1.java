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

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/5/16 15:44
 */
public class UserQJListAdapter1 extends CommonBaseAdapter<SceneListBean> {
    private int i = Util.getScreenWidth() - 3 * activity.getResources().getDimensionPixelSize(R.dimen.dp16);
    private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, i / 2, 1);
    public UserQJListAdapter1(List<SceneListBean> list, Activity activity) {
        super(list, activity);
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
            GlideUtils.displayImage(right_qj.getCover_url(), holder.iv_cover_right);
//            if (!TextUtils.isEmpty(left_qj.getTitle())) {
//                holder.tv_title_right.setText(right_qj.getTitle());
//                holder.tv_title_right.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
//            }
            if (!TextUtils.isEmpty(right_qj.getTitle())) {
                holder.tv_title_right.setVisibility(View.VISIBLE);
                holder.tv_title_right1.setVisibility(View.VISIBLE);
                if (right_qj.getTitle().length() <= 10) {
                    holder.tv_title_right.setText("");
                    holder.tv_title_right1.setText(right_qj.getTitle());
                    holder.tv_title_right1.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                    holder.tv_title_right.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
                } else {
                    holder.tv_title_right.setText(right_qj.getTitle().substring(0, 10));
                    holder.tv_title_right1.setText(right_qj.getTitle().substring(10, right_qj.getTitle().length()));
                    holder.tv_title_right1.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                    holder.tv_title_right.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                }
            } else {
                holder.tv_title_right.setVisibility(View.GONE);
                holder.tv_title_right1.setVisibility(View.GONE);
            }
            holder.tv_desc_right.setText(right_qj.getAddress());
        }

        GlideUtils.displayImage(left_qj.getCover_url(), holder.iv_cover_left);
        if (!TextUtils.isEmpty(left_qj.getTitle())) {
            holder.tv_title_left.setVisibility(View.VISIBLE);
            holder.tv_title_left1.setVisibility(View.VISIBLE);
            if (left_qj.getTitle().length() <= 10) {
                holder.tv_title_left.setText("");
                holder.tv_title_left1.setText(left_qj.getTitle());
                holder.tv_title_left1.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                holder.tv_title_left.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
            } else {
                holder.tv_title_left.setText(left_qj.getTitle().substring(0, 10));
                holder.tv_title_left1.setText(left_qj.getTitle().substring(10, left_qj.getTitle().length()));
                holder.tv_title_left1.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
                holder.tv_title_left.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
            }
        } else {
            holder.tv_title_left.setVisibility(View.GONE);
            holder.tv_title_left1.setVisibility(View.GONE);
        }
//        if (!TextUtils.isEmpty(left_qj.getTitle())) {
//            holder.tv_title_left.setText(left_qj.getTitle());
//            holder.tv_title_left.setBackgroundColor(activity.getResources().getColor(R.color.black_touming_80));
//        }
        holder.rl_left.setLayoutParams(params);
        holder.tv_desc_left.setText(left_qj.getAddress());
        holder.rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QJDetailActivity.class);
                intent.putExtra("id", left_qj.get_id());
                activity.startActivity(intent);
            }
        });
        final SceneListBean finalRight_qj = right_qj;
        holder.rl_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QJDetailActivity.class);
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
        @Bind(R.id.tv_title_left1)
        TextView tv_title_left1;

        @Bind(R.id.tv_desc_left)
        TextView tv_desc_left;

        @Bind(R.id.rl_right)
        RelativeLayout rl_right;
        @Bind(R.id.iv_cover_right)
        ImageView iv_cover_right;
        @Bind(R.id.tv_title_right)
        TextView tv_title_right;
        @Bind(R.id.tv_title_right1)
        TextView tv_title_right1;
        @Bind(R.id.tv_desc_right)
        TextView tv_desc_right;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
