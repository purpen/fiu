package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductGridAdapter;
import com.taihuoniao.fineix.adapters.ShopCartAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CartDoOrder;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.ShopCart;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.ConfirmOrderActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/2/14 16:29
 * Email: 895745843@qq.com
 */

public class CartFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.linearLayout_title)
    LinearLayout linearLayout_title;

    private View mRootView;
    private LinearLayout mRelativeLayoutEmptyShopCart;
    private RelativeLayout mRelativeLayoutProductLists;
    private RelativeLayout mRelativeLayoutSettlement;
    private ListViewForScrollView mListViewForScrollViewProductList;
    private WaittingDialog mDialog = null;
    private CheckBox mAllCheck; //底部的全选框
    private TextView mAllPrice; //底部的总价
    private Button mDeleteCalculate; //底部删除和结算相切换的按钮
    private List<ShopCart> mList = new ArrayList<>();
    private List<Map<String, Object>> totalList = new ArrayList<>();
    private List<Map<String, Object>> list_delete = new ArrayList<>();
    private HashMap<Integer, String> integerStringHashMap = new HashMap<>();
    private ShopCartAdapter adapter;

    //该变量是为了切换购物车标题右边的“编缉”和“完成”二字所在的button
    private List<CartDoOrder> doOrderList = new ArrayList<>();//购物车结算向确认订单界面传值
    private int change = 0;
    private Double mPayMoneyAll = 0.0;
    private DecimalFormat df = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_SHOP_CART_NUMBER:
                    break;
                case DataConstants.PARSER_SHOP_CART:
                    method002(msg);
                    break;
                case DataConstants.PARSER_SHOP_CART_CALCULATE:
                    method003(msg);
                    break;
            }
        }
    };

    @Override
    protected void requestNet() {
        getLasteProduct();
    }

    @Override
    protected View initView() {
        mRootView = View.inflate(getActivity(), R.layout.fragment_cart, null);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DataPaser.shopCartParser(mHandler);
        DataPaser.shopCartNumberParser(mHandler);
    }

    protected void initView1(View headerView) {
        df = new DecimalFormat("######0.00");
        mListViewForScrollViewProductList = (ListViewForScrollView) headerView.findViewById(R.id.pull_lv);
        mDeleteCalculate = (Button) headerView.findViewById(R.id.bt_delete_calculate_shopcart_item);
        mDeleteCalculate.setOnClickListener(this);
        mAllCheck = (CheckBox) headerView.findViewById(R.id.checkbox_choice_all_shopcart_item);
        mAllCheck.setOnClickListener(this);
        mAllPrice = (TextView)headerView. findViewById(R.id.tv_totalprice_shopcart_item);
        mRelativeLayoutEmptyShopCart = (LinearLayout) headerView.findViewById(R.id.relative_stroll_shopcart);
        Button mStroll = (Button)headerView. findViewById(R.id.bt_stroll_shopcart_empty);
        mStroll.setOnClickListener(this);
        mRelativeLayoutProductLists = (RelativeLayout)headerView. findViewById(R.id.relative_full_shopcart);
        mRelativeLayoutSettlement = (RelativeLayout)headerView. findViewById(R.id.linear_delete_count_shopcart);
        adapter = new ShopCartAdapter(totalList, getActivity(), change);
        mListViewForScrollViewProductList.setAdapter(adapter);
        adapter.setOnTwoClickedListener(new ShopCartAdapter.OnTwoClickedListener() {
            @Override
            public void onLetterCliced(HashMap<Integer, String> hashMap) {
                integerStringHashMap = hashMap;
            }
        });
        customHead.setHeadCenterTxtShow(true, "购物车");
        customHead.getHeadRightTV().setOnClickListener(this);
        customHead.setHeadRightTxtShow(true,R.string.edit);
        customHead.setHeadGoBackShow(false);
        mListViewForScrollViewProductList.setOnItemClickListener(new IonItemClickLister());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                if (change == 0) {
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    customHead.setHeadRightTxtShow(true, R.string.complete);
                    mDeleteCalculate.setText("删除");
                    mAllPrice.setVisibility(View.INVISIBLE);
                    DataPaser.shopCartParser(mHandler);
                    mListViewForScrollViewProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            final ShopCartAdapter.ViewHolder viewHolder = (ShopCartAdapter.ViewHolder) view.getTag();

                            // 让当前checkbox的勾选项变为相反状态，即如被勾则改为勾，反之改为不勾
                            viewHolder.mCheckBox.toggle();
                            mAllCheck.setChecked(false);
//                            if (position==0) return;
                            totalList.get(position/*-1*/).put("status", viewHolder.mCheckBox.isChecked());
                            int count = 0;
                            mPayMoneyAll = 0.0;//全局的
                            for (int i = 0; i < totalList.size(); i++) {
                                if (totalList.get(i).get("status").equals(true)) {
                                    count = count + 1;
                                }
                            }
                            if (count == totalList.size()) {
                                mAllCheck.setChecked(true);
                            }
                        }
                    });
                    change = 1;
                } else {
                    StringBuilder addSubtractBuilder = new StringBuilder();
                    addSubtractBuilder.append("[");
                    for (int i = 0; i < integerStringHashMap.size(); i++) {
                        // : 2017/1/13 添加推广码 店铺ID
                        addSubtractBuilder.append("{\"target_id\":")
                                .append(totalList.get(i).get("keyTargetId"))
                                .append(",\"n\":").append(integerStringHashMap.get(i))
                                .append(",\"type\":").append(1)
                                .append(",\"storage_id\":").append("\"" + totalList.get(i).get("storage_id") + "\"")
                                .append(",\"referral_code\":").append("\"" + totalList.get(i).get("referral_code") + "\"")
                                .append("},");
                    }
                    addSubtractBuilder.append("]");
                    addSubtractBuilder.replace(addSubtractBuilder.length() - 2, addSubtractBuilder.length() - 1, "");
                    String addSubtractArray = addSubtractBuilder.toString();
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    HashMap<String, String> params = ClientDiscoverAPI.getshopcartAddSubtractNetRequestParams(addSubtractArray);
                    HttpRequest.post(params,  URL.SHOPPINGN_EDIT_CART,  new GlobalDataCallBack(){
                        @Override
                        public void onSuccess(String json) {
                            DataPaser.shopCartParser(mHandler);
                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });
                    customHead.setHeadRightTxtShow(true, R.string.edit);
                    mAllPrice.setVisibility(View.VISIBLE);
                    mAllPrice.setText("¥0.00");
                    mDeleteCalculate.setText("去结算");
                    mListViewForScrollViewProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            ShopCartAdapter.ViewHolder viewHolder = (ShopCartAdapter.ViewHolder) view.getTag();

                            // 让当前checkbox的勾选项变为相反状态，即如被勾则改为勾，反之改为不勾
                            viewHolder.mCheckBox.toggle();
                            mAllCheck.setChecked(false);
//                            if (position==0) return;
                            totalList.get(position/*-1*/).put("status", viewHolder.mCheckBox.isChecked());
                            int count = 0;
                            mPayMoneyAll = 0.0;//全局的
                            Double mPayMoneyAll = 0.0;//局部的
                            for (int i = 0; i < totalList.size(); i++) {
                                if (totalList.get(i).get("status").equals(true)) {
                                    count = count + 1;
                                    mPayMoneyAll = mPayMoneyAll + Double.parseDouble(totalList.get(i).get("keyPrice").toString());
                                }
                            }
                            mAllPrice.setText(String.format("¥%s", df.format(mPayMoneyAll)));
                            if (count == totalList.size()) {
                                mAllCheck.setChecked(true);
                            }
                        }
                    });
                    change = 0;
                }
                break;
            case R.id.bt_stroll_shopcart_empty://购物车为空时，去逛逛
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(WellGoodsFragment.class.getSimpleName(), false);
                startActivity(intent);
                break;
            case R.id.checkbox_choice_all_shopcart_item://全选
                for (int i = 0; i < totalList.size(); i++) {
                    if (mAllCheck.isChecked()) {
                        totalList.get(i).put("status", true);
                        mPayMoneyAll = mPayMoneyAll + Double.parseDouble(totalList.get(i).get("keyPrice").toString());
                    } else {
                        totalList.get(i).put("status", false);
                        mPayMoneyAll = 0.00;
                    }
                }
                mAllPrice.setText(String.format("¥%s", df.format(mPayMoneyAll)));
                adapter.reloadListView();
                break;
            case R.id.bt_delete_calculate_shopcart_item://结算、删除按钮
                //拼接数组作为字符串参数访问网络
                StringBuilder builder = new StringBuilder();
                builder.append("[");
                if (list_delete != null) {
                    list_delete.clear();
                }
                for (int i = 0; i < totalList.size(); i++) {
                    if ((boolean) totalList.get(i).get("status")) {
                        list_delete.add(totalList.get(i));
                        // : 2017/1/13 添加推广码 店铺ID
                        builder.append("{\"target_id\":").append(totalList.get(i).get("keyTargetId"))
                                .append(",\"n\":").append(totalList.get(i).get("keyCount"))
                                .append(",\"type\":").append(totalList.get(i).get("keyType"))
                                .append(",\"referral_code\":").append("\"" + totalList.get(i).get("referral_code") + "\"")
                                .append(",\"storage_id\":").append("\"" + totalList.get(i).get("storage_id") + "\"")
                                .append("},");
                    }
                }
                builder.append("]");
                builder.replace(builder.length() - 2, builder.length() - 1, "");
                String array = builder.toString();
                if (change == 0 && !list_delete.isEmpty()) {
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    DataPaser.shopCartCalculateParser(array, mHandler);
                } else if (change == 1 && !list_delete.isEmpty()) {
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    HashMap<String, String> params = ClientDiscoverAPI.getdeletShopCartNetRequestParams(array);
                    HttpRequest.post(params,  URL.SHOPPING_REMOVE_CART, new GlobalDataCallBack(){
                        @Override
                        public void onSuccess(String json) {
                            ToastUtils.showSuccess("删除成功");
                            if (mAllCheck.isChecked()) {
                                mAllCheck.setChecked(false);
                            }
                            DataPaser.shopCartNumberParser(mHandler);
                            if (totalList.size() == list_delete.size()) {
                                mRelativeLayoutEmptyShopCart.setVisibility(View.VISIBLE);
                                mRelativeLayoutProductLists.setVisibility(View.GONE);
                                mRelativeLayoutSettlement.setVisibility(View.GONE);
                            }
                            adapter.removeItems(list_delete);
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                        }
                    });

                } else {
                    if (!list_delete.isEmpty()) {
                        if (!mDialog.isShowing()) {
                            mDialog.show();
                        }
                        DataPaser.shopCartCalculateParser(array, mHandler);
                    }

                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /********************************* 猜你喜欢 **********************************************/
    private AddProductGridAdapter indexAdapter002;//主题列表适配器
    private List<ProductBean.RowsEntity> productList;
    private List<SearchBean.SearchItem> searchList;
    private int currentPage = 0;

    /**
     * 新品
     */
    private void getLasteProduct(){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, null, null, null, null, String.valueOf(4), null, null, null, "1", null);
        HttpRequest.post(requestParams, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<ProductBean> productBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ProductBean>>() {});
                if (productBean.isSuccess()) {
                    searchList.clear();
                    if (currentPage == 1) {
                        productList.clear();
                    }
                    productList.addAll(productBean.getData().getRows());
                    indexAdapter002.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) { }
        });
    }

    private void method003(Message msg) {
        if (msg.obj != null) {
            if (msg.obj instanceof List) {
                doOrderList.clear();
                doOrderList.addAll((List<CartDoOrder>) msg.obj);

                //用户以前加入购物车的商品现在已经下架了，购物车中还显示，这时点确认订单，就给他提示，让他删掉已经下架的商品，否则不能往下走
                if ("false".equals(doOrderList.get(0).getSuccess())) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    new SVProgressHUD(getActivity()).showErrorWithStatus(doOrderList.get(0).getMessage());
                } else {
                    Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                    intent.putExtra("cartBean", doOrderList.get(0));
                    startActivity(intent);
                }
            }else if(msg.obj instanceof String){
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                ToastUtils.showInfo(msg.obj.toString());
            }
        }
    }

    void method002(Message msg) {
        if (msg.obj != null) {
            if (msg.obj instanceof List) {
                totalList.clear();
                mList.clear();
                mList.addAll((List<ShopCart>) msg.obj);
                for (int i = 0; i < mList.size(); i++) {
                    for (int j = 0; j < mList.get(i).getShopCartItemList().size(); j++) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("keyImage", mList.get(i).getShopCartItemList().get(j).getCover());
                        map.put("keyTitle", mList.get(i).getShopCartItemList().get(j).getTitle());
                        map.put("keyPrice", mList.get(i).getShopCartItemList().get(j).getTotal_price());
                        map.put("keyColor", mList.get(i).getShopCartItemList().get(j).getSku_mode());
                        map.put("keyCount", mList.get(i).getShopCartItemList().get(j).getN());
                        map.put("keyType", mList.get(i).getShopCartItemList().get(j).getType());
                        map.put("keyTargetId", mList.get(i).getShopCartItemList().get(j).getTarget_id());
                        map.put("keyProductId", mList.get(i).getShopCartItemList().get(j).getProduct_id());
                        map.put("isFirstJD",mList.get(i).getShopCartItemList().get(j).isFirstJD);
                        map.put("storage_id",mList.get(i).getShopCartItemList().get(j).getStorage_id());
                        map.put("referral_code",mList.get(i).getShopCartItemList().get(j).getReferral_code());
                        map.put("status", false); // 一开始，把所有的checkbox状态设为未勾选
                        totalList.add(map);
                    }
                }
                if (mAllCheck.isChecked()) {
                    mAllCheck.setChecked(false);
                }
                mPayMoneyAll = 0.0;//全局的
                mAllPrice.setText(String.format("¥%s", df.format(mPayMoneyAll)));
                adapter.notifyDataSetChanged();
                mListViewForScrollViewProductList.requestLayout();
                if (!totalList.isEmpty()) {
                    mRelativeLayoutProductLists.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    mRelativeLayoutSettlement.setVisibility(View.VISIBLE);
                    mRelativeLayoutEmptyShopCart.setVisibility(View.GONE);
                } else {
                    mRelativeLayoutEmptyShopCart.setVisibility(View.VISIBLE);
                    mRelativeLayoutProductLists.setVisibility(View.GONE);
                    mRelativeLayoutSettlement.setVisibility(View.GONE);
                }
            }
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    class IonItemClickLister implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final ShopCartAdapter.ViewHolder viewHolder = (ShopCartAdapter.ViewHolder) view.getTag();
            viewHolder.mCheckBox.toggle(); // 让当前checkbox的勾选项变为相反状态，即如被勾则改为勾，反之改为不勾
            mAllCheck.setChecked(false);
            if (position==0){
                return;
            }
            totalList.get(position-1).put("status", viewHolder.mCheckBox.isChecked());
            int count = 0;
            mPayMoneyAll = 0.00;//全局的
            Double mPayMoneyAll = 0.00;//局部的
            for (int i = 0; i < totalList.size(); i++) {
                if (totalList.get(i).get("status").equals(true)) {
                    count = count + 1;
                    mPayMoneyAll = mPayMoneyAll + Double.parseDouble(totalList.get(i).get("keyPrice").toString());
                }
            }
            mAllPrice.setText(String.format("¥%s", df.format(mPayMoneyAll)));
            if (count == totalList.size()) {
                mAllCheck.setChecked(true);
            }
        }
    }

    /**
     * 猜你喜欢
     */
    private void initView2(View headerView){
        GridViewForScrollView   recyclerView002 = (GridViewForScrollView ) headerView.findViewById(R.id.recyclerView_index_002);
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        indexAdapter002 = new AddProductGridAdapter(getActivity(),productList, searchList);
        recyclerView002.setAdapter(indexAdapter002);
        recyclerView002.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", productList.get(position).get_id());
                parent.getContext().startActivity(intent);
            }
        });
    }

    private void showDialog(){
        mDialog = new WaittingDialog(getActivity());
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    protected void initList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            linearLayout_title.setPadding(0, App.getStatusBarHeight(), 0, 0);
        }
        showDialog();
        initView1(mRootView);
        initView2(mRootView);
    }
}
