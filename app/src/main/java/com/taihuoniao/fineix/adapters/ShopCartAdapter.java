package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ShopCartInventoryItemBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.ShopCarActivity;
import com.taihuoniao.fineix.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by android on 2016/3/3.
 */
public class ShopCartAdapter extends BaseAdapter {
    private final static int STATE_COMPLETE = 0;//完成状态
    private final static int STATE_EDIT = 1;//编辑状态
    private int number = 0, maxNumber = 0, minNumber = 1;
    HashMap<Integer, String> hashMap = new HashMap<>();//装载item上商品加减变化的数目
    private OnTwoClickedListener listener = null;

    public interface OnTwoClickedListener {
        void onLetterCliced(HashMap<Integer, String> hashMap);
    }

    public void setOnTwoClickedListener(OnTwoClickedListener listener) {
        this.listener = listener;
    }


    private LayoutInflater inflater = null;
    private List<Map<String, Object>> list;
    private ShopCarActivity shopCarActivity;
    private Context context;
    private DecimalFormat df = null;
    //    public BitmapUtils bitmapUtils_listview = null;
    private DisplayImageOptions options;

    public ShopCartAdapter(List<Map<String, Object>> list, ShopCarActivity shopCarActivity, Context context) {
        this.shopCarActivity = shopCarActivity;
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) shopCarActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
//        String diskCachePath = StorageUtils.getCacheDirectory(MainApplication.getContext()).getAbsolutePath();
//        bitmapUtils_listview = new BitmapUtils(context, diskCachePath)
//                .configMemoryCacheEnabled(true)
//                .configDefaultCacheExpiry(1024 * 1024 * 4)
//                .configDefaultBitmapMaxSize(300, 300)
//                .configDefaultBitmapConfig(Bitmap.Config.ALPHA_8)
////                .configDefaultLoadingImage(R.mipmap.default_shopcart)
////                .configDefaultLoadFailedImage(R.mipmap.default_shopcart)
//                .configThreadPoolSize(5);
////                .configDefaultImageLoadAnimation(
////                        AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;

        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_shopcart, parent, false);
            mHolder.mCount = (TextView) convertView.findViewById(R.id.tv_count_shopcart_item);
            mHolder.mColor = (TextView) convertView.findViewById(R.id.tv_color_shopcart_item);
            mHolder.mPrice = (TextView) convertView.findViewById(R.id.tv_price_shopcart_item);
            mHolder.mImageGoods = (ImageView) convertView.findViewById(R.id.image_shopcart_item);
            mHolder.mTittle = (TextView) convertView.findViewById(R.id.tv_title_shopcart_item);
            mHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox_shopcart_item);
            mHolder.mRightItem = (LinearLayout) convertView.findViewById(R.id.linear_item_right);
            mHolder.mEditLayout = (LinearLayout) convertView.findViewById(R.id.linear_edit_shopcart_item);
            mHolder.mCompleteLayout = (LinearLayout) convertView.findViewById(R.id.linear_complete_shopcart_item);
            mHolder.mAdd = (TextView) convertView.findViewById(R.id.tv_add_shopcart_item_edit);
            mHolder.mSubtract = (TextView) convertView.findViewById(R.id.tv_subtract_shopcart_item_edit);
            mHolder.mText = (TextView) convertView.findViewById(R.id.tv_counts_shopcart_item_edit);
            mHolder.mText.setTag(position);
            mHolder.mColorEdit = (TextView) convertView.findViewById(R.id.tv_color_shopcart_item_edit);
            mHolder.mCountEdit = (TextView) convertView.findViewById(R.id.tv_count_shopcart_item_edit);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        hashMap.put(position, list.get(position).get("keyCount") + "");
        switch (shopCarActivity.getChange()) {
            case STATE_COMPLETE:
                mHolder.mEditLayout.setVisibility(View.GONE);
                mHolder.mCompleteLayout.setVisibility(View.VISIBLE);
                mHolder.mCount.setText("数量 * " + list.get(position).get("keyCount") + "");
                mHolder.mTittle.setText(list.get(position).get("keyTitle") + "");
                if ((list.get(position).get("keyColor")).equals("null")) {
                    mHolder.mColor.setText("颜色/分类：默认");
                } else {
                    mHolder.mColor.setText("颜色/分类：" + list.get(position).get("keyColor") + "");
                }
                mHolder.mPrice.setText("¥" + list.get(position).get("keyPrice") + "");
                mHolder.mRightItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (shopCarActivity.getChange() == STATE_COMPLETE) {
                            Intent intent = new Intent(shopCarActivity, BuyGoodsDetailsActivity.class);
                            intent.putExtra("id", list.get(position).get("keyProductId") + "");
                            shopCarActivity.startActivity(intent);
                        }
                    }
                });
                break;
            case STATE_EDIT:
                mHolder.positionTag = position;
                mHolder.mEditLayout.setVisibility(View.VISIBLE);
                mHolder.mCompleteLayout.setVisibility(View.GONE);
                mHolder.mCountEdit.setText("数量 * " + list.get(position).get("keyCount") + "");
                mHolder.mText.setTag(position);
                mHolder.mText.setText(list.get(position).get("keyCount") + "");
                if ((list.get(position).get("keyColor")).equals("null")) {
                    mHolder.mColorEdit.setText("颜色/分类：默认");
                } else {
                    mHolder.mColorEdit.setText("颜色/分类：" + list.get(position).get("keyColor") + "");
                }
                inventory();
                final ViewHolder finalMHolder = mHolder;
                mHolder.mSubtract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        LinearLayout linearLayout= (LinearLayout) v.getParent();
//                        int p= (int) linearLayout.getChildAt(1).getTag();
//number= Integer.parseInt(list.get(p).get("keyCount")+"");
                        number = Integer.parseInt(finalMHolder.mText.getText().toString());
                        if (number <= 1) {
                            number = 1;
//                            Toast.makeText(shopCarActivity, "我这么可爱，您一个都不买真的好吗", Toast.LENGTH_SHORT).show();
                        } else {
                            number--;
                        }
                        finalMHolder.mText.setText(number + "");
                        hashMap.put(position, number + "");
                        listener.onLetterCliced(hashMap);
                    }
                });
                mHolder.mAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = Integer.parseInt(finalMHolder.mText.getText().toString());
                        maxNumber = Integer.parseInt(mInventoryList.get(position).getQuantity());
                        if (maxNumber == 0)
                            return;
                        if (number >= maxNumber) {
                            number = maxNumber;
//                            Toast.makeText(shopCarActivity, "货不够了，再加程序猿就要疯掉了", Toast.LENGTH_SHORT).show();
                        } else {
                            number++;
                        }
                        finalMHolder.mText.setText(number + "");
                        hashMap.put(position, number + "");
                        listener.onLetterCliced(hashMap);
                    }
                });
                break;
        }
        mHolder.mCheckBox.setChecked((Boolean) list.get(position).get("status"));
        ImageLoader.getInstance().displayImage(list.get(position).get("keyImage") + "", mHolder.mImageGoods);
//        bitmapUtils_listview.display(mHolder.mImageGoods, list.get(position).get("keyImage") + "");
        listener.onLetterCliced(hashMap);

        return convertView;
    }

    public class ViewHolder {
        public ImageView mImageGoods;
        TextView mTittle;
        TextView mPrice;
        TextView mCount;
        TextView mColor;
        public LinearLayout mRightItem;
        public CheckBox mCheckBox;
        private LinearLayout mCompleteLayout, mEditLayout;
        private TextView mAdd, mSubtract, mText, mColorEdit, mCountEdit;
        private int positionTag;
    }

    // 重新加载Listview，即刷新
    public void reloadListView() {
        notifyDataSetChanged();
    }

    // 删除一批数据
    public void removeItems(Collection<? extends Map<String, Object>> data) {
        list.removeAll(data);
        if (hashMap != null) {
            hashMap.clear();
        }
        notifyDataSetChanged();
    }

    List<ShopCartInventoryItemBean> mInventoryList = new ArrayList<>();

    //    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case DataConstants.PASER_SHOPCART_INVENTORY_ITEM:
//                    if (msg.obj != null) {
//                        if (msg.obj instanceof List) {
//                            mInventoryList.clear();
//                            mInventoryList.addAll((Collection<? extends ShopCartInventoryItemBean>) msg.obj);
//                        }
//                    }
//                    break;
//            }
//        }
//    };
    private void inventory() {
        ClientDiscoverAPI.shopcartInventoryNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                List<ShopCartInventoryItemBean> list = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject jsonObj = obj.getJSONObject("data");
                    JSONArray jsonArray = jsonObj.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ShopCartInventoryItemBean bean = new ShopCartInventoryItemBean();
                        bean.setN(jsonObject.optString("n"));
                        bean.setProduct_id(jsonObject.optString("product_id"));
                        bean.setQuantity(jsonObject.optString("quantity"));
                        bean.setTarget_id(jsonObject.optString("target_id"));
                        bean.setType(jsonObject.optString("type"));
                        list.add(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mInventoryList.clear();
                mInventoryList.addAll(list);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showError("网络错误");
            }
        });
    }
}
