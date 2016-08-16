package com.taihuoniao.fineix.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/10.
 */
public class IndexQJListAdapter extends BaseAdapter {
    private List<SceneList.DataBean.RowsBean> sceneList;//情景列表数据

    public IndexQJListAdapter(List<SceneList.DataBean.RowsBean> sceneList) {
        this.sceneList = sceneList;
    }

    @Override
    public int getCount() {
        return sceneList == null ? 0 : sceneList.size();
    }

    @Override
    public Object getItem(int position) {
        return sceneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_index_qj, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            holder.labelRecycler.setHasFixedSize(true);
            holder.labelRecycler.setLayoutManager(new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(sceneList.get(position).getUser_info().getAvatar_url(), holder.headImg);
        if (sceneList.get(position).getUser_info().getIs_expert() == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
        } else {
            holder.vImg.setVisibility(View.GONE);
        }
        holder.userNameTv.setText(sceneList.get(position).getUser_info().getNickname());
        holder.publishTime.setText(sceneList.get(position).getCreated_at());
        holder.locationTv.setText(sceneList.get(position).getAddress());
        //判断是否已经关注的标识没有
        ImageLoader.getInstance().displayImage(sceneList.get(position).getCover_url(), holder.qjImg);
        holder.viewCount.setText(sceneList.get(position).getView_count());
        holder.loveCount.setText(sceneList.get(position).getLove_count());
        //判断是否已经点赞标识没有
        holder.qjDesTv.setText(sceneList.get(position).getDes());
        holder.labelRecycler.setAdapter(new IndexLabelRecyclerAdapter(sceneList.get(position).getTags()));
        holder.commentList.setAdapter(new IndexCommentAdapter(sceneList.get(position).getComments()));
        holder.moreCommentTv.setText("查看" + sceneList.get(position).getComment_count() + "条评论");
        if (Integer.parseInt(sceneList.get(position).getComment_count()) > 0) {
            holder.moreCommentLinear.setVisibility(View.VISIBLE);
        } else {
            holder.moreCommentLinear.setVisibility(View.GONE);
        }
        holder.qjTitleTv.setText(sceneList.get(position).getTitle());
        holder.qjTitleTv.post(new Runnable() {
            @Override
            public void run() {
                if (holder.qjDesTv.getLineCount() >= 3) {
                    holder.moreDesImg.setVisibility(View.VISIBLE);
                } else {
                    holder.moreDesImg.setVisibility(View.GONE);
                }
                if (holder.qjTitleTv.getLineCount() >= 2) {
                    Layout layout = holder.qjTitleTv.getLayout();
                    StringBuilder SrcStr = new StringBuilder(holder.qjTitleTv.getText().toString());
                    String str0 = SrcStr.subSequence(layout.getLineStart(0), layout.getLineEnd(0)).toString();
                    String str1 = SrcStr.subSequence(layout.getLineStart(1), layout.getLineEnd(1)).toString();
                    holder.qjTitleTv2.setText(str0);
                    holder.qjTitleTv.setText(str1);
                    holder.qjTitleTv2.setVisibility(View.VISIBLE);
                } else {
                    holder.qjTitleTv2.setVisibility(View.GONE);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.head_img)
        RoundedImageView headImg;
        @Bind(R.id.v_img)
        ImageView vImg;
        @Bind(R.id.attention_btn)
        Button attentionBtn;
        @Bind(R.id.user_name_tv)
        TextView userNameTv;
        @Bind(R.id.publish_time)
        TextView publishTime;
        @Bind(R.id.location_tv)
        TextView locationTv;
        @Bind(R.id.qj_img)
        ImageView qjImg;
        @Bind(R.id.qj_title_tv)
        TextView qjTitleTv;
        @Bind(R.id.qj_title_tv2)
        TextView qjTitleTv2;
        @Bind(R.id.view_count)
        TextView viewCount;
        @Bind(R.id.more_img)
        ImageView moreImg;
        @Bind(R.id.share_img)
        ImageView shareImg;
        @Bind(R.id.comment_img)
        ImageView commentImg;
        @Bind(R.id.love_count)
        TextView loveCount;
        @Bind(R.id.love_img)
        ImageView loveImg;
        @Bind(R.id.qj_des_tv)
        TextView qjDesTv;
        @Bind(R.id.more_des_img)
        ImageView moreDesImg;
        @Bind(R.id.label_recycler)
        RecyclerView labelRecycler;
        @Bind(R.id.comment_list)
        ListViewForScrollView commentList;
        @Bind(R.id.more_comment_tv)
        TextView moreCommentTv;
        @Bind(R.id.more_comment_linear)
        LinearLayout moreCommentLinear;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class IndexLabelRecyclerAdapter extends RecyclerView.Adapter<IndexLabelRecyclerAdapter.VH> {
        private List<String> tags;

        public IndexLabelRecyclerAdapter(List<String> tags) {
            this.tags = tags;
        }

        @Override
        public IndexLabelRecyclerAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_index_label, null);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(IndexLabelRecyclerAdapter.VH holder, int position) {
            holder.textView.setText(tags.get(position));
        }

        @Override
        public int getItemCount() {
            return tags == null ? 0 : tags.size();
        }

        public static class VH extends RecyclerView.ViewHolder {
            @Bind(R.id.tv)
            TextView textView;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    static class IndexCommentAdapter extends BaseAdapter {
        private List<SceneList.DataBean.RowsBean.CommentsBean> commentList;

        public IndexCommentAdapter(List<SceneList.DataBean.RowsBean.CommentsBean> commentList) {
            this.commentList = commentList;
        }

        @Override
        public int getCount() {
            if(commentList==null){
                return 0;
            }
            if(commentList.size()<=2){
                return commentList.size();
            }
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_index_comment, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(commentList.get(position).getUser_avatar_url(), holder.headImg);
            SpannableStringBuilder spannableString = new SpannableStringBuilder(commentList.get(position).getUser_nickname() + ": " + commentList.get(position).getContent());
            ForegroundColorSpan backgroundColorSpan = new ForegroundColorSpan(parent.getResources().getColor(R.color.black));
            spannableString.setSpan(backgroundColorSpan, 0, commentList.get(position).getUser_nickname().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(parent.getResources().getColor(R.color.color_666));
            spannableString.setSpan(foregroundColorSpan, commentList.get(position).getUser_nickname().length() + 1, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.contentTv.setText(spannableString);
            return convertView;
        }

        static class ViewHolder {
            @Bind(R.id.head_img)
            RoundedImageView headImg;
            @Bind(R.id.content_tv)
            TextView contentTv;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
