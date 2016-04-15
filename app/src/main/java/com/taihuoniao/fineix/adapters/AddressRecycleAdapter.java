package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.taihuoniao.fineix.R;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class AddressRecycleAdapter extends RecyclerView.Adapter<AddressRecycleAdapter.VH> {
    private Context context;
    private List<PoiInfo> poiInfoList;
    private static EditRecyclerAdapter.ItemClick itemClick;

    public AddressRecycleAdapter(Context context, List<PoiInfo> poiInfoList, EditRecyclerAdapter.ItemClick itemClick1) {
        this.context = context;
        this.poiInfoList = poiInfoList;
        itemClick = itemClick1;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_horizontal_address, null);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.textView.setText(poiInfoList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return poiInfoList.size();
    }

   static class VH extends RecyclerView.ViewHolder {
        private TextView textView;

        public VH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_horizontal_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.click(getPosition());
                }
            });
        }
    }
}
