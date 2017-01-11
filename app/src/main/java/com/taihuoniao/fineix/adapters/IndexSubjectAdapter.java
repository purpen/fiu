package com.taihuoniao.fineix.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/21.
 */
public class IndexSubjectAdapter extends RecyclerView.Adapter<IndexSubjectAdapter.VH> {

    private List<SubjectListBean.DataBean.RowsBean> list;
    private EditRecyclerAdapter.ItemClick itemClick;

    public IndexSubjectAdapter(EditRecyclerAdapter.ItemClick itemClick, List<SubjectListBean.DataBean.RowsBean> list) {
        this.itemClick = itemClick;
        this.list = list;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_index_subject, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(holder.getAdapterPosition());
            }
        });
//        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg);
        GlideUtils.displayImage(list.get(position).getCover_url(), holder.backgroundImg);
        holder.title.setText(list.get(position).getTitle());
        switch (list.get(position).getType()) {
            case 2:
                holder.label.setImageResource(R.mipmap.subject_huodong);
                holder.person.setText(list.get(position).getAttend_count() + "人参加");
                holder.person.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.label.setImageResource(R.mipmap.subject_cuxiao);
                holder.person.setVisibility(View.INVISIBLE);
                holder.imageView.setVisibility(View.INVISIBLE);
                break;
            case 4:
                holder.label.setImageResource(R.mipmap.subject_xinpin);
                holder.person.setVisibility(View.INVISIBLE);
                holder.imageView.setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.label.setImageResource(R.mipmap.subject_wenzhang);
                holder.person.setText(list.get(position).getView_count() + "人阅读");
                holder.person.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.label.setVisibility(View.GONE);
                holder.person.setText(list.get(position).getView_count() + "人阅读");
                holder.person.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        @Bind(R.id.background_img)
        ImageView backgroundImg;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.i)
        ImageView imageView;
        @Bind(R.id.person)
        TextView person;
        @Bind(R.id.label)
        ImageView label;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
