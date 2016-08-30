package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.utils.DensityUtils;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/3.
 */
public class GoodsDetailSceneRecyclerAdapter extends RecyclerView.Adapter<GoodsDetailSceneRecyclerAdapter.VH> {
    private Context context;
    private List<ProductAndSceneListBean.ProductAndSceneItem> list;
    private EditRecyclerAdapter.ItemClick itemClick;

    public GoodsDetailSceneRecyclerAdapter(Context context, List<ProductAndSceneListBean.ProductAndSceneItem> list, EditRecyclerAdapter.ItemClick itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_small_scene, null);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.click(holder.getAdapterPosition());
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(DensityUtils.dp2px(context, 300) * 9 / 16, DensityUtils.dp2px(context, 280));
        holder.itemView.setLayoutParams(layoutParams);
        ImageLoader.getInstance().displayImage(list.get(position).getSight().getCover_url(), holder.imageView);
        holder.titleTv.setText(list.get(position).getSight().getTitle());
        holder.addressTv.setText(list.get(position).getSight().getAddress());
        holder.titleTv.post(new Runnable() {
            @Override
            public void run() {
                double leng = holder.titleTv.getText().length();
                int t = 10;
                for (char c : holder.titleTv.getText().toString().toCharArray()) {
                    if (c >= 32 && c <= 126) {
                        leng -= 0.5;
                        t++;
                    }
                }
                if (leng > 10) {
                    Bitmap bitmap = BitmapFactory.decodeResource(holder.titleTv.getResources(), R.mipmap.scene_title_background).copy(Bitmap.Config.ARGB_8888, true);
                    Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap1);
                    canvas.drawColor(Color.argb(0, 0, 0, 0));
                    canvas.clipRect(0, 0, holder.titleTv.getMeasuredWidth(), holder.titleTv.getMeasuredHeight() / 2);
                    canvas.clipRect(0, 0, holder.titleTv.getText().subSequence(t, holder.titleTv.getText().length()).length() * holder.titleTv.getTextSize(), holder.titleTv.getMeasuredHeight(), Region.Op.UNION);
                    Paint paint = new Paint();
                    canvas.drawBitmap(bitmap, 0, 0, paint);
                    canvas.save();
                    canvas.restore();
                    holder.sceneImg.setImageBitmap(bitmap1);
                } else {
                    holder.sceneImg.setImageResource(R.mipmap.scene_title_background);
                }
                LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) holder.frameLayout.getLayoutParams();
                layoutParams1.height = holder.titleTv.getMeasuredHeight();
                layoutParams1.width = holder.titleTv.getMeasuredWidth();
                holder.frameLayout.setLayoutParams(layoutParams1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() > 8 ? 8 : list.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        ImageView imageView;
        FrameLayout frameLayout;
        ImageView sceneImg;
        TextView titleTv;
        TextView addressTv;

        public VH(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_cover);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.item_frame);
            sceneImg = (ImageView) itemView.findViewById(R.id.title_img);
            titleTv = (TextView) itemView.findViewById(R.id.tv_title);
            addressTv = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}
