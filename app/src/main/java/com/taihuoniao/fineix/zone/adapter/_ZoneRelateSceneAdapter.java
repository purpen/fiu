package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneLoveBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.MapNearByCJActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJPictureActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ClickImageView;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lilin on 2017/2/21.
 */

public class _ZoneRelateSceneAdapter extends RecyclerView.Adapter<_ZoneRelateSceneAdapter.ViewHolder> {

    private Activity activity;
    private List<SceneList.DataBean.RowsBean> list;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public _ZoneRelateSceneAdapter(Activity activity, List list) {
        this.activity = activity;
        this.list = list;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    @Override
    public _ZoneRelateSceneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_relate_zone, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final _ZoneRelateSceneAdapter.ViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                    return false;
                }
            });
        }

        ViewGroup.LayoutParams params = holder.container.getLayoutParams();
        params.width = Util.getScreenWidth();
        params.height= params.width;
        holder.container.setLayoutParams(params);

        GlideUtils.displayImageFadein(list.get(position).getCover_url(), holder.qjImg);
        GlideUtils.displayImage(list.get(position).getUser_info().getAvatar_url(), holder.headImg);
        holder.labelContainer.setVisibility(View.VISIBLE);

        //设置情景标题
        SceneTitleSetUtils.setTitle(holder.qjTitleTv, holder.qjTitleTv2, list.get(position).getTitle());
        //添加商品标签
        List<SceneList.DataBean.RowsBean.ProductBean> productBeanList = list.get(position).getProduct();
        if (productBeanList != null && compareble(productBeanList, (List<SceneList.DataBean.RowsBean.ProductBean>) holder.labelContainer.getTag(R.id.label_container))) {
            holder.labelContainer.setTag(R.id.label_container, productBeanList);
        } else {
            stopAnimate(holder); //停止商品动画 移除所有标签
            method1(position, holder);
            addGoodsTag(position,holder); //添加商品标签
            holder.labelContainer.setTag(R.id.label_container, productBeanList);
        }

        //跳转个人中心
        holder.headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserCenterActivity.class);
                long l = Long.valueOf(list.get(position).getUser_info().getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                v.getContext().startActivity(intent);
            }
        });
        //跳转情景地图
        holder.mapLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                跳转到地图界面，查看附近的情境
                String address = list.get(position).getAddress();
                LatLng ll = new LatLng(list.get(position).getLocation().getCoordinates().get(1), list.get(position).getLocation().getCoordinates().get(0));
                Intent intent2 = new Intent(activity, MapNearByCJActivity.class);
                intent2.putExtra("address", address);
                intent2.putExtra(MapNearByCJActivity.class.getSimpleName(), ll);
                activity.startActivity(intent2);
            }
        });

        //点赞或取消点赞
        holder.loveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (list.get(position).getIs_love() == 1) {
                        holder.loveContainer.setEnabled(false);
                        cancelLoveQJ(position, list.get(position).get_id(), holder);
                    } else {
                        holder.loveContainer.setEnabled(false);
                        loveQJ(position, list.get(position).get_id(), holder);
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.IndexFragment;
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //取消点赞
    private void cancelLoveQJ(final int position, String id, final _ZoneRelateSceneAdapter.ViewHolder holder) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getcancelLoveQJRequestParams(id);
        HttpRequest.post(requestParams, URL.CANCEL_LOVE_SCENE, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                holder.loveContainer.setEnabled(false);
            }

            @Override
            public void onSuccess(String json) {
                holder.loveContainer.setEnabled(true);
                SceneLoveBean sceneLoveBean = new SceneLoveBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneLoveBean>() {
                    }.getType();
                    sceneLoveBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常");
                }
                if (sceneLoveBean.isSuccess()) {
                    holder.loveImg.setImageResource(R.mipmap.index_love);
                    holder.loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    list.get(position).setIs_love(0);
                    list.get(position).setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                holder.loveContainer.setEnabled(true);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //点赞情景
    private void loveQJ(final int position, String id, final _ZoneRelateSceneAdapter.ViewHolder holder) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getloveQJRequestParams(id);
        HttpRequest.post(requestParams, URL.LOVE_SCENE, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                holder.loveContainer.setEnabled(false);
            }

            @Override
            public void onSuccess(String json) {
                holder.loveContainer.setEnabled(true);
                SceneLoveBean sceneLoveBean = new SceneLoveBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneLoveBean>() {
                    }.getType();
                    sceneLoveBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常");
                }
                if (sceneLoveBean.isSuccess()) {
                    holder.loveImg.setImageResource(R.mipmap.index_has_love);
                    holder.loveCount.setText(sceneLoveBean.getData().getLove_count() + "");
                    list.get(position).setIs_love(1);
                    list.get(position).setLove_count(sceneLoveBean.getData().getLove_count() + "");
                } else {
                    ToastUtils.showError(sceneLoveBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                holder.loveContainer.setEnabled(true);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }


    /**
     * 添加商品标签
     * @param position
     * @param holder
     */
    private void addGoodsTag(int position,_ZoneRelateSceneAdapter.ViewHolder holder) {
        for (final SceneList.DataBean.RowsBean.ProductBean productBean : list.get(position).getProduct()) {
            final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final LabelView labelView = new LabelView(activity);
            labelView.nameTv.setText(productBean.getTitle());
            labelView.setLayoutParams(layoutParams);
            if (productBean.getLoc() == 2) {
                labelView.nameTv.setBackgroundResource(R.drawable.label_left);
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                layoutParams1.leftMargin = (int) labelView.labelMargin;
                labelView.pointContainer.setLayoutParams(layoutParams1);
            }
            labelView.post(new Runnable() {
                @Override
                public void run() {
                    if (productBean.getLoc() == 2) {
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.getX() * MainApplication.getContext().getScreenWidth() - labelView.labelMargin - labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    } else {
                        labelView.nameTv.setBackgroundResource(R.drawable.label_right);
                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) labelView.pointContainer.getLayoutParams();
                        layoutParams1.leftMargin = (int) (labelView.nameTv.getMeasuredWidth() - labelView.pointWidth - labelView.labelMargin);
                        labelView.pointContainer.setLayoutParams(layoutParams1);
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) labelView.getLayoutParams();
                        lp.leftMargin = (int) (productBean.getX() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredWidth() + labelView.labelMargin + labelView.pointWidth / 2);
                        lp.topMargin = (int) (productBean.getY() * MainApplication.getContext().getScreenWidth() - labelView.getMeasuredHeight() + labelView.pointWidth / 2);
                        labelView.setLayoutParams(lp);
                    }
                }
            });
            holder.labelContainer.addView(labelView);
//            Log.e("<<<", "开启动画" + holder.qjTitleTv.getText() + ",现在位置=" + position);
            labelView.wave();
            labelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                    intent.putExtra("id", productBean.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }


    private void stopAnimate(_ZoneRelateSceneAdapter.ViewHolder holder) {
        //停止商品动画
        for (int i = 0; i < holder.labelContainer.getChildCount(); i++) {
            LabelView view = (LabelView) holder.labelContainer.getChildAt(i);
            view.stopAnim();
        }
        //移除所有标签
        holder.labelContainer.removeAllViews();
    }


    private void method1(final int position, final _ZoneRelateSceneAdapter.ViewHolder holder) {
        if (list.get(position).getUser_info().getIs_expert() == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
        } else {
            holder.vImg.setVisibility(View.GONE);
        }
        holder.userNameTv.setText(list.get(position).getUser_info().getNickname());
        holder.publishTime.setText(list.get(position).getCreated_at());
        if (TextUtils.isEmpty(list.get(position).getAddress())) {
            holder.locationImg.setVisibility(View.GONE);
            holder.locationTv.setVisibility(View.GONE);
        } else {
            holder.locationTv.setText(list.get(position).getCity() + " " + list.get(position).getAddress());
            holder.locationImg.setVisibility(View.VISIBLE);
            holder.locationTv.setVisibility(View.VISIBLE);
        }
        holder.qjImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QJPictureActivity.class);
                intent.putExtra("img", list.get(position).getCover_url());
                intent.putExtra("fine", list.get(position).getFine() == 1);
                intent.putExtra("stick", list.get(position).getStick() == 1);
                intent.putExtra("check", list.get(position).getIs_check() == 0);
                intent.putExtra("id",list.get(position).get_id());
                activity.startActivity(intent);
            }
        });
        holder.viewCount.setText(list.get(position).getView_count());
        holder.loveCount.setText(list.get(position).getLove_count());
        if (list.get(position).getIs_love() == 1) {
            holder.loveImg.setImageResource(R.mipmap.index_has_love);
        } else {
            holder.loveImg.setImageResource(R.mipmap.index_love);
        }
        SpannableString spannableStringBuilder = SceneTitleSetUtils.setDes(list.get(position).getDes(), activity);
        holder.qjDesTv.setText(spannableStringBuilder);
        holder.qjDesTv.setMovementMethod(LinkMovementMethod.getInstance());
        holder.qjDesTv.setMaxLines(3);
        holder.qjDesTv.post(new Runnable() {
            @Override
            public void run() {
                if (holder.qjDesTv.getLineCount() > 3) {
                    Layout layout = holder.qjDesTv.getLayout();
                    String str = list.get(position).getDes().substring(layout.getLineStart(0), layout.getLineEnd(2) - 1) + "…";
                    holder.qjDesTv.setText(SceneTitleSetUtils.setDes(str, activity));
                }
            }
        });
        holder.commentList.setAdapter(new _ZoneRelateSceneAdapter.IndexCommentAdapter(list.get(position).getComments()));
        if (list.get(position).getComment_count() > 0) {
            holder.moreComment.setText("查看所有" + list.get(position).getComment_count() + "条评论");
            holder.moreComment.setVisibility(View.GONE);
        } else {
            holder.moreComment.setVisibility(View.GONE);
        }
    }

    static class IndexCommentAdapter extends BaseAdapter {
        private List<SceneList.DataBean.RowsBean.CommentsBean> commentList;

        public IndexCommentAdapter(List<SceneList.DataBean.RowsBean.CommentsBean> commentList) {
            this.commentList = commentList;
        }

        @Override
        public int getCount() {
            if (commentList == null) {
                return 0;
            }
            if (commentList.size() <= 2) {
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
            ZoneRelateSceneAdapter.IndexCommentAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_index_comment, null);
                holder = new ZoneRelateSceneAdapter.IndexCommentAdapter.ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ZoneRelateSceneAdapter.IndexCommentAdapter.ViewHolder) convertView.getTag();
            }
//            ImageLoader.getInstance().displayImage(commentList.get(position).getUser_avatar_url(), holder.headImg);
//            Glide.with(holder.headImg.getContext()).load(commentList.get(position).getUser_avatar_url()).into(holder.headImg);
            GlideUtils.displayImage(commentList.get(position).getUser_avatar_url(), holder.headImg);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.qj_img)
        ImageView qjImg;
        @Bind(R.id.qj_title_tv)
        TextView qjTitleTv;
        @Bind(R.id.qj_title_tv2)
        TextView qjTitleTv2;
        @Bind(R.id.label_container)
        RelativeLayout labelContainer;
        @Bind(R.id.container)
        RelativeLayout container;
        @Bind(R.id.head_img)
        RoundedImageView headImg;
        @Bind(R.id.v_img)
        ImageView vImg;
        @Bind(R.id.relative)
        RelativeLayout relative;
        @Bind(R.id.user_name_tv)
        TextView userNameTv;
        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.publish_time)
        TextView publishTime;
        @Bind(R.id.location_img)
        ImageView locationImg;
        @Bind(R.id.location_tv)
        TextView locationTv;
        @Bind(R.id.map_linear)
        LinearLayout mapLinear;
        @Bind(R.id.qj_des_tv)
        TextView qjDesTv;
        @Bind(R.id.img1)
        ImageView img1;
        @Bind(R.id.view_count)
        TextView viewCount;
        @Bind(R.id.more_img)
        ImageView moreImg;
        @Bind(R.id.share_img)
        ImageView shareImg;
        @Bind(R.id.comment_img)
        ImageView commentImg;
        @Bind(R.id.love_img)
        ClickImageView loveImg;
        @Bind(R.id.love_count)
        TextView loveCount;
        @Bind(R.id.love_container)
        RelativeLayout loveContainer;
        @Bind(R.id.comment_list)
        ListViewForScrollView commentList;
        @Bind(R.id.more_comment)
        TextView moreComment;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    private boolean compareble(List<SceneList.DataBean.RowsBean.ProductBean> list1, List<SceneList.DataBean.RowsBean.ProductBean> list2){
        return list1.equals(list2);
    }
}
