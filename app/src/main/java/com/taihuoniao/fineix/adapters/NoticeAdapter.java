package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.NoticeBean;
import com.taihuoniao.fineix.beans.NoticeData;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/5/4 19:24
 */
public class NoticeAdapter extends CommonBaseAdapter<NoticeBean.DataBean.RowsBean> {
    private ImageLoader imageLoader;

    public NoticeAdapter(List list, Activity activity) {
        super(list, activity);
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NoticeBean.DataBean.RowsBean item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_notice, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (item.getReaded() == 1) {
            holder.dot.setVisibility(View.INVISIBLE);
        } else {
            holder.dot.setVisibility(View.VISIBLE);
        }
        imageLoader.displayImage(item.getSend_user().getAvatar_url(), holder.riv, options);
        SpannableStringBuilder spannableStringBuilder;
        if (item.getComment_target_obj() != null) {
            spannableStringBuilder = new SpannableStringBuilder(item.getSend_user().getNickname() + item.getInfo() + item.getKind_str() + ":" + item.getTarget_obj().getContent());
        } else {
            spannableStringBuilder = new SpannableStringBuilder(item.getSend_user().getNickname() + item.getInfo() + item.getKind_str());
        }
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(activity.getResources().getColor(R.color.title_black));
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, item.getSend_user().getNickname().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_name.setText(spannableStringBuilder);
        holder.tv_time.setText(item.getCreated_at());
        holder.iv.setVisibility(View.VISIBLE);
        if (item.getComment_target_obj() != null) {
            imageLoader.displayImage(item.getComment_target_obj().getCover_url(), holder.iv);
        } else {
            imageLoader.displayImage(item.getTarget_obj().getCover_url(), holder.iv, options);
        }
        holder.riv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UserCenterActivity.class);
                intent.putExtra(FocusActivity.USER_ID_EXTRA, Long.parseLong(item.getSend_user().get_id()));
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.riv)
        ImageView riv;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_time)
        TextView tv_time;
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.dot)
        TextView dot;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
