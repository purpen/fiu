package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;


/**
 * Created by Stephen on 2016/12/20 14:47
 * Email: 895745843@qq.com
 */
public class EditRecyclerAjustAdapter extends RecyclerView.Adapter<EditRecyclerAjustAdapter.VH> {
    private int[] imgs = new int[]{R.mipmap.icon_ajust_00,R.mipmap.icon_ajust_01,R.mipmap.icon_ajust_02,R.mipmap.icon_ajust_03,R.mipmap.icon_ajust_04,R.mipmap.icon_ajust_05};
    private String[] strs = new String[]{"还原", "亮度","对比度","饱和度","锐度","色温"};

    private Context context;
    private ItemClick itemClick;

    public EditRecyclerAjustAdapter(Context context) {
        this.context = context;
    }

    public EditRecyclerAjustAdapter(Context context, ItemClick itemClick) {
        this.context = context;
        this.itemClick = itemClick;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_edit_recycler_adjust, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null) {
                    itemClick.click(position, strs[position]);
                }
            }
        });
        holder.imageView.setImageResource(imgs[position]);
        holder.textView.setText(strs[position]);
    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }

    public class VH extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public VH(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_edit_recycler_filterimg);
            textView = (TextView) itemView.findViewById(R.id.item_edit_recycler_textview);
        }
    }

    public interface ItemClick {
        void click(int postion, String filterName);
    }
}
