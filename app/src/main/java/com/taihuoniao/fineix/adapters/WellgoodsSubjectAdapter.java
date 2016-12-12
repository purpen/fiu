package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.user.ActivityDetailActivity;
import com.taihuoniao.fineix.user.ArticalDetailActivity;
import com.taihuoniao.fineix.user.NewProductDetailActivity;
import com.taihuoniao.fineix.user.SalePromotionDetailActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class WellgoodsSubjectAdapter extends BaseAdapter {
    private Activity activity;
    private List<SubjectListBean.DataBean.RowsBean> list;

    public WellgoodsSubjectAdapter(Activity activity, List<SubjectListBean.DataBean.RowsBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = View.inflate(activity, R.layout.item_wellgoods_subject, null);
            holder = new ViewHolder(convertView);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.subjectImg.getLayoutParams();
            layoutParams.width = MainApplication.getContext().getScreenWidth();
            layoutParams.height = layoutParams.width * 422 / 750;
            holder.subjectImg.setLayoutParams(layoutParams);
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == list.size() - 1) {
            holder.container.setPadding(0, 0, 0, DensityUtils.dp2px(activity, 50));
        } else {
            holder.container.setPadding(0, 0, 0, 0);
        }
//        ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.subjectImg);
        GlideUtils.displayImage(list.get(position).getCover_url(), holder.subjectImg);

        holder.subjectName.setText(list.get(position).getTitle());
        holder.subjectName2.setText(list.get(position).getShort_title());
        holder.subjectImg.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Intent intent = new Intent();
                                                     switch (list.get(position).getType()) {
                                                         case 1: //文章详情
                                                             intent = new Intent(activity, ArticalDetailActivity.class);
                                                             intent.putExtra(ArticalDetailActivity.class.getSimpleName(), list.get(position).get_id());
//                                                             activity.startActivity(intent);
                                                             break;
                                                         case 2: //活动详情
                                                             intent = new Intent(activity, ActivityDetailActivity.class);
                                                             intent.putExtra(ActivityDetailActivity.class.getSimpleName(), list.get(position).get_id());
//                                                             activity.startActivity(intent);
                                                             break;
                                                         case 4: //新品
                                                             intent = new Intent(activity, NewProductDetailActivity.class);
                                                             intent.putExtra(NewProductDetailActivity.class.getSimpleName(), list.get(position).get_id());
//                                                             activity.startActivity(intent);
                                                             break;
                                                         case 3: //促销
                                                         case 5://好货
                                                             intent = new Intent(activity, SalePromotionDetailActivity.class);
                                                             intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), list.get(position).get_id());
//                                                             activity.startActivity(intent);
                                                             break;
                                                     }
                                                     activity.startActivity(intent);
                                                 }

                                             }

        );
        holder.recyclerView.setAdapter(new RecyclerAdapter(activity, list.get(position).getProducts(), new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", list.get(position).getProducts().get(postion).get_id());
                activity.startActivity(intent);
            }
        }

        ));
//        holder.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                if (parent.getChildAdapterPosition(view) == 0) {
//                    outRect.left = DensityUtils.dp2px(parent.getContext(), 15);
//                    outRect.top = DensityUtils.dp2px(parent.getContext(), 10);
//                } else if (parent.getChildAdapterPosition(view) == list.get(position).getProducts().size() - 1) {
//                    outRect.left = DensityUtils.dp2px(activity, 10);
//                    outRect.top = DensityUtils.dp2px(activity, 10);
//                    outRect.right = DensityUtils.dp2px(activity, 15);
//                } else {
//                    outRect.top = DensityUtils.dp2px(activity, 10);
//                    outRect.left = DensityUtils.dp2px(activity, 10);
//                }
//            }
//        });
        return convertView;
    }

    static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VH> {

        private Activity activity;
        private List<SubjectListBean.DataBean.RowsBean.ProductsBean> list;
        private EditRecyclerAdapter.ItemClick itemClick;

        public RecyclerAdapter(Activity activity, List<SubjectListBean.DataBean.RowsBean.ProductsBean> list, EditRecyclerAdapter.ItemClick itemClick) {
            this.activity = activity;
            this.list = list;
            this.itemClick = itemClick;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(activity, R.layout.item_wellgoods_product, null);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.click(holder.getAdapterPosition());
                }
            });
//            ImageLoader.getInstance().displayImage(list.get(position).getBanner_url(), holder.backgroundImg);
            GlideUtils.displayImage(list.get(position).getBanner_url(), holder.backgroundImg);

            holder.name.setText(list.get(position).getTitle());
            holder.price.setText("¥" + list.get(position).getSale_price());
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            @Bind(R.id.background_img)
            ImageView backgroundImg;
            @Bind(R.id.name)
            TextView name;
            @Bind(R.id.price)
            TextView price;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
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
        @Bind(R.id.recycler_view)
        RecyclerView recyclerView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
