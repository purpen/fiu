package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.ShopCartInventoryItemBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.ShopCartActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

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
 * @author lilin
 * created at 2016/11/1 15:20
 */
public class ShopCartAdapter extends CommonBaseAdapter<Map<String, Object>> {
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
    private DecimalFormat df = null;
    public ShopCartAdapter(List<Map<String, Object>> list, ShopCartActivity activity, Context context) {
       super(list,activity);
        this.list = list;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_shopcart, parent, false);
            mHolder.tvTips= (TextView) convertView.findViewById(R.id.tv_tips);
            mHolder.mCount = (TextView) convertView.findViewById(R.id.tv_count_shopcart_item);
            mHolder.mColor = (TextView) convertView.findViewById(R.id.tv_color_shopcart_item);
            mHolder.mPrice = (TextView) convertView.findViewById(R.id.tv_price_shopcart_item);
            mHolder.mImageGoods = (RoundedImageView) convertView.findViewById(R.id.image_shopcart_item);
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
        switch (((ShopCartActivity)activity).getChange()) {
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
                        if (((ShopCartActivity)activity).getChange() == STATE_COMPLETE) {
                            Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                            intent.putExtra("id", list.get(position).get("keyProductId") + "");
                            activity.startActivity(intent);
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
                        number = Integer.parseInt(finalMHolder.mText.getText().toString());
                        if (number <= 1) {
                            number = 1;
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
        if ((boolean) list.get(position).get("isFirstJD")){
            mHolder.tvTips.setVisibility(View.VISIBLE);
        }else {
            mHolder.tvTips.setVisibility(View.GONE);
        }
        mHolder.mCheckBox.setChecked((Boolean) list.get(position).get("status"));
        GlideUtils.displayImage(list.get(position).get("keyImage") + "", mHolder.mImageGoods);
        listener.onLetterCliced(hashMap);
        return convertView;
    }

    public class ViewHolder {
        public RoundedImageView mImageGoods;
        TextView mTittle;
        TextView mPrice;
        TextView mCount;
        TextView mColor;
        public TextView tvTips;
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

    private void inventory() {
        HashMap<String, String> params = ClientDiscoverAPI.getshopcartInventoryNetRequestParams();
        HttpRequest.post(params,  URL.SHOPPING_FETCH_CART_PRODUCT_COUUNT, new GlobalDataCallBack(){
//        ClientDiscoverAPI.shopcartInventoryNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                List<ShopCartInventoryItemBean> list = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(json);
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
            public void onFailure(String error) {
                ToastUtils.showError("网络错误");
            }
        });
    }
}
