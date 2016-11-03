package com.taihuoniao.fineix.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.AddressListBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.user.AddAddressActivity;
import com.taihuoniao.fineix.user.SelectAddressActivity;

import java.util.List;

/**
 * Created by taihuoniao on 2016/2/2.
 */
public class SelectAddressListViewAdapter extends BaseAdapter {
    private int mScreentWidth;
    private View view;
    private SelectAddressActivity activity;
    private List<AddressListBean.RowsEntity> list;
    private String addressId;
    public SelectAddressListViewAdapter(SelectAddressActivity activity, List<AddressListBean.RowsEntity> list, int mScreentWidth,String addressId) {
        this.activity = activity;
        this.list = list;
        this.mScreentWidth = mScreentWidth;
        this.addressId=addressId;
    }

    public void setAddressId(String addressId){
        this.addressId = addressId;
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
        final AddressListBean.RowsEntity item = list.get(position);
        ViewHolder hold;
        if (convertView == null) {
            hold = new ViewHolder();
            convertView = View.inflate(activity, R.layout.item_address, null);
            hold.isSelectImg = (ImageView) convertView.findViewById(R.id.item_address_checkimg);
            hold.editImg = (ImageView) convertView.findViewById(R.id.item_address_editimg);
            hold.linearLayout = (LinearLayout) convertView.findViewById(R.id.item_address_linear);
            hold.llBox = (LinearLayout) convertView.findViewById(R.id.ll_box);
            hold.nameTv = (TextView) convertView.findViewById(R.id.item_address_nametv);
            hold.isDefaultTv = (TextView) convertView.findViewById(R.id.item_address_isdefaulttv);
            hold.addressTv = (TextView) convertView.findViewById(R.id.item_address_addresstv);
            hold.detailsAddressTv = (TextView) convertView.findViewById(R.id.item_address_details_addresstv);
            hold.phoneTv = (TextView) convertView.findViewById(R.id.item_address_phonetv);
            //为实现item的滑动删除效果
            hold.scrollView = (HorizontalScrollView) convertView.findViewById(R.id.item_address_scrollview);
            hold.deleteTv = (TextView) convertView.findViewById(R.id.item_address_deletetv);
            hold.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.item_address_relative);
            ViewGroup.LayoutParams lp = hold.relativeLayout.getLayoutParams();
            lp.width = mScreentWidth;

            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
        hold.nameTv.setText(item.name);
        if (TextUtils.equals("0",item.is_default)){
            hold.isDefaultTv.setVisibility(View.GONE);
        }else if(TextUtils.equals("1",item.is_default)){
            hold.isDefaultTv.setVisibility(View.VISIBLE);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(item.province);
        builder.append(" ");
        builder.append(item.city);
        builder.append(" ");
        builder.append(item.county);
        builder.append(" ");
        builder.append(item.town);
        hold.addressTv.setText(builder.toString());
        hold.detailsAddressTv.setText(item.address);
        hold.phoneTv.setText(item.phone);

        if (item.isSelected || TextUtils.equals(item._id,addressId)) {
            hold.isSelectImg.setImageResource(R.mipmap.checked);
        } else {
            hold.isSelectImg.setImageResource(R.mipmap.check);
        }

        hold.llBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AddressListBean.RowsEntity row:list){
                    if (row.isSelected){
                        row.isSelected=false;
                    }else {
                        if (TextUtils.equals(item._id,row._id)){
                            row.isSelected=true;
                        }else {
                            row.isSelected=false;
                        }
                    }
                }
                if (!TextUtils.equals(item._id,addressId)){
                    addressId="";
                }
                notifyDataSetChanged();
            }
        });
//        hold.isSelectImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (item.isSelected) {
//                    item.isSelected=false;
//                } else {
//                    for (int i = 0; i < list.size(); i++) {
//                        if (i == position) {
//                            item.isSelected=true;
//                        } else {
//                            item.isSelected=false;
//                        }
//                    }
//                }
//                notifyDataSetChanged();
//            }
//        });
        hold.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddAddressActivity.class);
                intent.putExtra("addressBean",item);
                activity.startActivityForResult(intent, DataConstants.REQUESTCODE_EDITADDRESS);
            }
        });
//        hold.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (item.isSelected) {
//                    item.isSelected=false;
//                } else {
//                    for (int i = 0; i < list.size(); i++) {
//                        if (i == position) {
//                            item.isSelected=true;
//                        } else {
//                            item.isSelected=false;
//                        }
//                    }
//                }
//                notifyDataSetChanged();
//            }
//        });

        //实现item的侧滑删除
        hold.deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除某收货地址
                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("确认删除此收货地址吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.deleteAddress(item._id);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        // 设置监听事件
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (view != null) {
                            ViewHolder viewHolder1 = (ViewHolder) view.getTag();
                            viewHolder1.scrollView.smoothScrollTo(0, 0);
                        }
                    case MotionEvent.ACTION_UP:
                        // 获得ViewHolder
                        ViewHolder viewHolder = (ViewHolder) v.getTag();
                        view = v;
                        // 获得HorizontalScrollView滑动的水平方向值.
                        int scrollX = viewHolder.scrollView.getScrollX();

                        // 获得操作区域的长度
                        int actionW = viewHolder.deleteTv.getWidth();

                        // 注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
                        // 如果水平方向的移动值<操作区域的长度的一半,就复原
                        if (scrollX < actionW / 2) {
                            viewHolder.scrollView.smoothScrollTo(0, 0);
                        } else// 否则的话显示操作区域
                        {
                            viewHolder.scrollView.smoothScrollTo(actionW, 0);
                        }
                        return true;
                }
                return false;
            }
        });

        // 这里防止删除一条item后,ListView处于操作状态,直接还原
        if (hold.scrollView.getScrollX() != 0) {
            hold.scrollView.scrollTo(0, 0);
        }
        return convertView;
    }



    private class ViewHolder {
        private ImageView isSelectImg;
        private ImageView editImg;
        private LinearLayout linearLayout;
        private LinearLayout llBox;
        private TextView nameTv, isDefaultTv, addressTv, detailsAddressTv, phoneTv;
        //为实现item的滑动删除效果
        private HorizontalScrollView scrollView;
        private TextView deleteTv;
        private RelativeLayout relativeLayout;
    }

}
