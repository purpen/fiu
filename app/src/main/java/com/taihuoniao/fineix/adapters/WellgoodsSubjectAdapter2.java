package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.home.GoToNextUtils;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.TypeConversionUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class WellgoodsSubjectAdapter2 extends BaseAdapter {
    private Activity activity;
    private List<SubjectListBean.RowsEntity> list;

    public WellgoodsSubjectAdapter2(Activity activity, List<SubjectListBean.RowsEntity> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : 2;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_wellgoods_subject2, null);
            holder = new ViewHolder(convertView);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.subjectImg.getLayoutParams();
            layoutParams.width = MainApplication.getContext().getScreenWidth();
            layoutParams.height = layoutParams.width * 422 / 750;
            holder.subjectImg.setLayoutParams(layoutParams);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (position == list.size() - 1) {
//            holder.container.setPadding(0, 0, 0, DensityUtils.dp2px(activity, 50));
//        } else {
//            holder.container.setPadding(0, 0, 0, 0);
//        }
        GlideUtils.displayImage(list.get(position).getCover_url(), holder.subjectImg);
        holder.subjectName.setText(list.get(position).getTitle());
        holder.subjectName2.setText(list.get(position).getShort_title());
        holder.subjectImg.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     GoToNextUtils.goNext(activity, TypeConversionUtils.StringConvertInt(list.get(position).getType()), list.get(position).get_id());
                                                 }
                                             }
        );
        switch (TypeConversionUtils.StringConvertInt(list.get(position).getType())) {
            case 2:
                holder.label.setImageResource(R.mipmap.subject_huodong);
                break;
            case 3:
                holder.label.setImageResource(R.mipmap.subject_cuxiao);
                break;
            case 4:
                holder.label.setImageResource(R.mipmap.subject_xinpin);
                break;
            case 1:
                holder.label.setImageResource(R.mipmap.subject_wenzhang);
                break;
            case 5:
                holder.label.setVisibility(View.GONE);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.container)
        RelativeLayout container;
        @Bind(R.id.subject_img)
        ImageView subjectImg;
        @Bind(R.id.subject_name)
        TextView subjectName;
        @Bind(R.id.subject_name2)
        TextView subjectName2;
        @Bind(R.id.label)
        ImageView label;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
