package com.taihuoniao.fineix.home.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.main.App;
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
 * Created by Stephen on 2017/2/16 11:16
 * Email: 895745843@qq.com
 */

public class ProductAlbumAdapter extends RecyclerView.Adapter<ProductAlbumAdapter.VH>{
    private Context mContext;
    private List<SubjectListBean.DataBean.RowsBean> mRowsBeens;

    public ProductAlbumAdapter(Context context, List<SubjectListBean.DataBean.RowsBean> list) {
        this.mContext = context;
        this.mRowsBeens = list;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wellgoods_subject, null);
        return new VH(inflate);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                Toast.makeText(App.getContext(), "Click " + adapterPosition + "", Toast.LENGTH_SHORT).show();
            }
        });


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.subjectImg.getLayoutParams();
        layoutParams.width = MainApplication.getContext().getScreenWidth();
        layoutParams.height = layoutParams.width * 422 / 750;
        holder.subjectImg.setLayoutParams(layoutParams);
        if (position == mRowsBeens.size() - 1) {
            holder.container.setPadding(0, 0, 0, DensityUtils.dp2px(mContext, 50));
        } else {
            holder.container.setPadding(0, 0, 0, 0);
        }

        SubjectListBean.DataBean.RowsBean rowsBean = mRowsBeens.get(adapterPosition);
        if (rowsBean == null) {
            return;
        }
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(new RecyclerAdapter((Activity) mContext, rowsBean.getProducts(), new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Intent intent = new Intent(mContext, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", mRowsBeens.get(holder.getAdapterPosition()).getProducts().get(postion).get_id());
                mContext.startActivity(intent);
            }
        }));
        GlideUtils.displayImage(rowsBean.getCover_url(), holder.subjectImg);
        holder.subjectName.setText(rowsBean.getTitle());
        holder.subjectName2.setText(rowsBean.getShort_title());
        holder.subjectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBigImage(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRowsBeens == null ? 0 : mRowsBeens.size();
    }

    class VH extends RecyclerView.ViewHolder {
        private RelativeLayout container;
        private RelativeLayout relative;
        private ImageView subjectImg;
        private TextView subjectName;
        private TextView subjectName2;
        private RecyclerView recyclerView;

        public VH(View itemView) {
            super(itemView);

            container = (RelativeLayout) itemView.findViewById(R.id.container);
            relative = (RelativeLayout) itemView.findViewById(R.id.relative);
            subjectImg = (ImageView) itemView.findViewById(R.id.subject_img);
            subjectName = (TextView) itemView.findViewById(R.id.subject_name);
            subjectName2 = (TextView) itemView.findViewById(R.id.subject_name2);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        }
    }

    static class RecyclerAdapter extends RecyclerView.Adapter<ProductAlbumAdapter.RecyclerAdapter.VH> {

        private Activity activity;
        private List<SubjectListBean.DataBean.RowsBean.ProductsBean> list;
        private EditRecyclerAdapter.ItemClick itemClick;

        public RecyclerAdapter(Activity activity, List<SubjectListBean.DataBean.RowsBean.ProductsBean> list, EditRecyclerAdapter.ItemClick itemClick) {
            this.activity = activity;
            this.list = list;
            this.itemClick = itemClick;
        }

        @Override
        public ProductAlbumAdapter.RecyclerAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_wellgoods_product, parent, false);
            return new ProductAlbumAdapter.RecyclerAdapter.VH(view);
        }

        @Override
        public void onBindViewHolder(final ProductAlbumAdapter.RecyclerAdapter.VH holder, int position) {
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

    private void clickBigImage(int position){
        Intent intent = new Intent();
        switch (mRowsBeens.get(position).getType()) {
            case 1: //文章详情
                intent = new Intent(mContext, ArticalDetailActivity.class);
                intent.putExtra(ArticalDetailActivity.class.getSimpleName(), mRowsBeens.get(position).get_id());
                break;
            case 2: //活动详情
                intent = new Intent(mContext, ActivityDetailActivity.class);
                intent.putExtra(ActivityDetailActivity.class.getSimpleName(), mRowsBeens.get(position).get_id());
                break;
            case 4: //新品
                intent = new Intent(mContext, NewProductDetailActivity.class);
                intent.putExtra(NewProductDetailActivity.class.getSimpleName(), mRowsBeens.get(position).get_id());
                break;
            case 3: //促销
            case 5://好货
                intent = new Intent(mContext, SalePromotionDetailActivity.class);
                intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), mRowsBeens.get(position).get_id());
                break;
        }
        mContext.startActivity(intent);
    }
}
