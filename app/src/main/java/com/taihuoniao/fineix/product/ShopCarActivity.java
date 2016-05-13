package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShopCartAdapter;
import com.taihuoniao.fineix.beans.CartDoOrder;
import com.taihuoniao.fineix.beans.ShopCart;
import com.taihuoniao.fineix.beans.ShopCartNumber;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.PullRefreshLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCarActivity extends Activity implements View.OnClickListener, PullRefreshLayout.OnPullListener, PullRefreshLayout.OnPullStateListener {
    private ListViewForScrollView mShopCartListView;
    private WaittingDialog mDialog = null;
    private List<ShopCart> mList = new ArrayList<>();
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> list_delete = new ArrayList<Map<String, Object>>();
    HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
    private ShopCartAdapter adapter;
    private int mCarNum = 0;
    private MyGlobalTitleLayout title = null;
    //该变量是为了切换购物车标题右边的“编缉”和“完成”二字所在的button
    private int change = 0;
    Map<String, Object> map;

    private CheckBox mAllCheck;//底部的全选框
    //底部的总价
    private TextView mAllPrice;
    private Double mPayMoneyAll = 0.0;
    private Button mDeleteCalculate;//底部删除和结算相切换的按钮
    private List<CartDoOrder> doOrderList = new ArrayList<>();//购物车结算向确认订单界面传值

    private Button mStroll;//列表为空时，“去逛逛”那个按钮
    private RelativeLayout mEmptyLayout;
    private RelativeLayout mFullLayout;
    private DecimalFormat df = null;
    //下拉刷新
    private PullRefreshLayout mPullLayout;
    private Animation mRotateUpAnimation;
    private Animation mRotateDownAnimation;
    private boolean mInLoading = false;
    private View mProgress;
    private View mActionImage;
    private TextView mActionText;
    private TextView mTimeText;

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.CUSTOM_PULLTOREFRESH_HOME:
                    dataLoadFinished();
                    break;
                case DataConstants.PARSER_SHOP_CART_NUMBER:
                    if (msg.obj != null) {
                        if (msg.obj instanceof ShopCartNumber) {
                            ShopCartNumber numberCart = null;
                            numberCart = (ShopCartNumber) msg.obj;
                            if ("false".equals(numberCart.getSuccess())) {
                                title.setTitle("购物车(0)");
                            } else {
                                title.setTitle("购物车(" + numberCart.getCount() + ")");
                            }
                        }
                    }
                    break;
                case DataConstants.PARSER_SHOP_CART:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            totalList.clear();
                            mList.clear();
                            mList.addAll((List<ShopCart>) msg.obj);
                            for (int i = 0; i < mList.size(); i++) {
                                for (int j = 0; j < mList.get(i).getShopCartItemList().size(); j++) {
                                    map = new HashMap<String, Object>();
                                    map.put("keyImage", mList.get(i).getShopCartItemList().get(j).getCover());
                                    map.put("keyTitle", mList.get(i).getShopCartItemList().get(j).getTitle());
                                    map.put("keyPrice", mList.get(i).getShopCartItemList().get(j).getTotal_price());
                                    map.put("keyColor", mList.get(i).getShopCartItemList().get(j).getSku_mode());
                                    map.put("keyCount", mList.get(i).getShopCartItemList().get(j).getN());
                                    map.put("keyType", mList.get(i).getShopCartItemList().get(j).getType());
                                    map.put("keyTargetId", mList.get(i).getShopCartItemList().get(j).getTarget_id());
                                    map.put("keyProductId", mList.get(i).getShopCartItemList().get(j).getProduct_id());
                                    // 一开始，把所有的checkbox状态设为未勾选
                                    map.put("status", false);
                                    totalList.add(map);
                                }
                            }
                            if (mAllCheck.isChecked()) {
                                mAllCheck.setChecked(false);
                            }
                            mPayMoneyAll = 0.0;//全局的
                            mAllPrice.setText("合计：¥" + df.format(mPayMoneyAll));
                            mDeleteCalculate.setBackgroundColor(Color.parseColor("#e0e0e0"));
                            adapter.notifyDataSetChanged();
                            if (!totalList.isEmpty()) {
                                mEmptyLayout.setVisibility(View.GONE);
                                mFullLayout.setVisibility(View.VISIBLE);
                            } else {
                                mEmptyLayout.setVisibility(View.VISIBLE);
                                mFullLayout.setVisibility(View.GONE);
                            }
                        }
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                    break;
                case DataConstants.PARSER_SHOP_CART_CALCULATE:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            doOrderList.clear();
                            doOrderList.addAll((List<CartDoOrder>) msg.obj);
                            //用户以前加入购物车的商品现在已经下架了，购物车中还显示，这时点确认订单，就给他提示，让他删掉已经下架的商品，否则不能往下走
                            if ("false".equals(doOrderList.get(0).getSuccess())) {
                                if (mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                                Toast.makeText(ShopCarActivity.this, doOrderList.get(0).getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(ShopCarActivity.this, ConfirmOrderActivity.class);
                                intent.putExtra("cartBean", doOrderList.get(0));
                                startActivity(intent);
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_shop_car);
        ActivityUtil.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();
        DataPaser.shopCartParser(mHandler);
        DataPaser.shopCartNumberParser(mHandler);
    }

    private void initView() {
        mDialog = new WaittingDialog(this);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        df = new DecimalFormat("######0.00");
        initPullToRefresh();
        mDeleteCalculate = (Button) findViewById(R.id.bt_delete_calculate_shopcart_item);
        mDeleteCalculate.setOnClickListener(this);
        mAllCheck = (CheckBox) findViewById(R.id.checkbox_choice_all_shopcart_item);
        mAllCheck.setOnClickListener(this);
        mAllPrice = (TextView) findViewById(R.id.tv_totalprice_shopcart_item);
        mEmptyLayout = (RelativeLayout) findViewById(R.id.relative_stroll_shopcart);
        mStroll = (Button) findViewById(R.id.bt_stroll_shopcart_empty);
        mStroll.setOnClickListener(this);
        mFullLayout = (RelativeLayout) findViewById(R.id.relative_full_shopcart);

        mShopCartListView = (ListViewForScrollView) findViewById(R.id.lv_shopcart);
        adapter = new ShopCartAdapter(totalList, ShopCarActivity.this, this);
        mShopCartListView.setAdapter(adapter);
        adapter.setOnTwoClickedListener(new ShopCartAdapter.OnTwoClickedListener() {
            @Override
            public void onLetterCliced(HashMap<Integer, String> hashMap) {
                ShopCarActivity.this.hashMap = hashMap;
            }
        });
        title = (MyGlobalTitleLayout) findViewById(R.id.title_shopcart);
        title.setBackgroundResource(R.color.white);
        title.setTitleColor(getResources().getColor(R.color.black333333));
        title.setBackImg(R.mipmap.back_black);
        title.setRightColor(getResources().getColor(R.color.black333333));
        title.setTitle("购物车（" + mCarNum + "）");
        title.setRightSearchButton(false);
        title.setRightShopCartButton(false);
        title.setRightButton("编缉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change == 0) {
                    mDialog.show();
                    title.setRightButton("完成");
                    mDeleteCalculate.setText("删除");
                    mAllPrice.setVisibility(View.INVISIBLE);
                    mDeleteCalculate.setBackgroundColor(Color.parseColor("#e0e0e0"));
                    DataPaser.shopCartParser(mHandler);
                    mShopCartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            final ShopCartAdapter.ViewHolder viewHolder = (ShopCartAdapter.ViewHolder) view.getTag();
// 让当前checkbox的勾选项变为相反状态，即如被勾则改为勾，反之改为不勾
                            viewHolder.mCheckBox.toggle();
//                            viewHolder.mRightItem.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (change == 0) {
//                                        Intent intent = new Intent(ShopCarActivity.this, GoodsDetailsActivity.class);
//                                        intent.putExtra("id", totalList.get(position).get("keyProductId") + "");
//                                        ShopCarActivity.this.startActivity(intent);
//
//                                    } else {
//                                        // 让当前checkbox的勾选项变为相反状态，即如被勾则改为勾，反之改为不勾
//                                        viewHolder.mCheckBox.toggle();
//                                    }
//                                }
//                            });
                            mAllCheck.setChecked(false);
                            mDeleteCalculate.setBackgroundColor(Color.parseColor("#e0e0e0"));
                            totalList.get(position).put("status", viewHolder.mCheckBox.isChecked());
                            int count = 0;
                            mPayMoneyAll = 0.0;//全局的
                            for (int i = 0; i < totalList.size(); i++) {
                                if (totalList.get(i).get("status").equals(true)) {
                                    count = count + 1;
                                    mDeleteCalculate.setBackgroundResource(R.color.yellow_bd8913);
                                }
                            }
                            if (count == totalList.size()) {
                                mAllCheck.setChecked(true);
                            }
                        }
                    });
                    change = 1;
                    setChange(change);
                } else {
                    StringBuilder addSubtractBuilder = new StringBuilder();
                    addSubtractBuilder.append("[");
                    for (int i = 0; i < ShopCarActivity.this.hashMap.size(); i++) {
                        addSubtractBuilder.append("{\"target_id\":" + totalList.get(i).get("keyTargetId") +
                                ",\"n\":" + ShopCarActivity.this.hashMap.get(i) + ",\"type\":" + 1 + "},");

                    }
                    addSubtractBuilder.append("]");
                    addSubtractBuilder.replace(addSubtractBuilder.length() - 2, addSubtractBuilder.length() - 1, "");
                    String addSubtractArray = addSubtractBuilder.toString();
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                    ClientDiscoverAPI.shopcartAddSubtractNet(addSubtractArray, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            DataPaser.shopCartParser(mHandler);
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {

                        }
                    });
                    title.setRightButton("编缉");
                    mAllPrice.setVisibility(View.VISIBLE);
                    mAllPrice.setText("合计：¥0.00");
                    mDeleteCalculate.setText("结算");
                    mDeleteCalculate.setBackgroundColor(Color.parseColor("#e0e0e0"));
//                    DataPaser.shopCartParser(mHandler);
                    mShopCartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            ShopCartAdapter.ViewHolder viewHolder = (ShopCartAdapter.ViewHolder) view.getTag();
                            // 让当前checkbox的勾选项变为相反状态，即如被勾则改为勾，反之改为不勾
                            viewHolder.mCheckBox.toggle();

                            mAllCheck.setChecked(false);
                            mDeleteCalculate.setBackgroundColor(Color.parseColor("#e0e0e0"));
                            totalList.get(position).put("status", viewHolder.mCheckBox.isChecked());
                            int count = 0;
                            mPayMoneyAll = 0.0;//全局的
                            Double mPayMoneyAll = 0.0;//局部的
                            for (int i = 0; i < totalList.size(); i++) {
                                if (totalList.get(i).get("status").equals(true)) {
                                    count = count + 1;
                                    mPayMoneyAll = mPayMoneyAll + Double.parseDouble(totalList.get(i).get("keyPrice").toString());
                                    mDeleteCalculate.setBackgroundResource(R.color.yellow_bd8913);
                                }
                            }
                            mAllPrice.setText("合计：¥" + df.format(mPayMoneyAll));
                            if (count == totalList.size()) {
                                mAllCheck.setChecked(true);
                            }
                        }
                    });
                    change = 0;
                    setChange(change);
                }
            }
        });
        mShopCartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ShopCartAdapter.ViewHolder viewHolder = (ShopCartAdapter.ViewHolder) view.getTag();
                // 让当前checkbox的勾选项变为相反状态，即如被勾则改为勾，反之改为不勾
                viewHolder.mCheckBox.toggle();
                mAllCheck.setChecked(false);
                mDeleteCalculate.setBackgroundColor(Color.parseColor("#e0e0e0"));
                totalList.get(position).put("status", viewHolder.mCheckBox.isChecked());
                int count = 0;
                mPayMoneyAll = 0.00;//全局的
                Double mPayMoneyAll = 0.00;//局部的
                for (int i = 0; i < totalList.size(); i++) {
                    if (totalList.get(i).get("status").equals(true)) {
                        count = count + 1;
                        mPayMoneyAll = mPayMoneyAll + Double.parseDouble(totalList.get(i).get("keyPrice").toString());
                        mDeleteCalculate.setBackgroundResource(R.color.yellow_bd8913);
                    }
                }
                mAllPrice.setText("合计：¥" + df.format(mPayMoneyAll));
                if (count == totalList.size()) {
                    mAllCheck.setChecked(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_stroll_shopcart_empty://购物车为空时，去逛逛
//                onBackPressed();
                Intent broadIntent = new Intent();
                broadIntent.setAction(DataConstants.BroadShopCart);
                sendBroadcast(broadIntent);
                Intent intent = new Intent(ShopCarActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.checkbox_choice_all_shopcart_item://全选
                for (int i = 0; i < totalList.size(); i++) {
                    if (mAllCheck.isChecked()) {
                        //在这change为1是因为在上面切换了“编缉”、“完成”这一按钮后，change在括号内最后一行代码处被赋为了1，
                        // 所以，在这里1所代表的状态即是上面0所代表的状态，反之亦然
                        if (change == 1) {
                            mDeleteCalculate.setBackgroundResource(R.color.yellow_bd8913);
                        } else if (change == 0) {
                            mDeleteCalculate.setBackgroundResource(R.color.yellow_bd8913);
                        } else {
                            mDeleteCalculate.setBackgroundResource(R.color.yellow_bd8913);
                        }

                        totalList.get(i).put("status", true);
                        mPayMoneyAll = mPayMoneyAll + Double.parseDouble(totalList.get(i).get("keyPrice").toString());
                    } else {
                        totalList.get(i).put("status", false);
                        mPayMoneyAll = 0.00;

                        mDeleteCalculate.setBackgroundColor(Color.parseColor("#e0e0e0"));

                    }
                }
                mAllPrice.setText("合计：¥" + df.format(mPayMoneyAll));
                adapter.reloadListView();
                break;
            case R.id.bt_delete_calculate_shopcart_item://结算、删除按钮
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                //拼接数组作为字符串参数访问网络
                StringBuilder builder = new StringBuilder();
                builder.append("[");
                if (list_delete != null) {
                    list_delete.clear();
                }
                for (int i = 0; i < totalList.size(); i++) {
                    if ((boolean) totalList.get(i).get("status")) {
                        list_delete.add(totalList.get(i));
                        builder.append("{\"target_id\":" + totalList.get(i).get("keyTargetId") +
                                ",\"n\":" + totalList.get(i).get("keyCount") + ",\"type\":" + totalList.get(i).get("keyType") + "},");
                    }
                }
                builder.append("]");
                builder.replace(builder.length() - 2, builder.length() - 1, "");
                String array = builder.toString();
                if (change == 0 && !list_delete.isEmpty()) {
                    DataPaser.shopCartCalculateParser(array, mHandler);
                } else if (change == 1 && !list_delete.isEmpty()) {
                    ClientDiscoverAPI.deletShopCartNet(array, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Toast.makeText(ShopCarActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            if (mAllCheck.isChecked()) {
                                mAllCheck.setChecked(false);
                            }
                            mDeleteCalculate.setBackgroundColor(Color.parseColor("#e0e0e0"));
                            DataPaser.shopCartNumberParser(mHandler);
                            if (totalList.size() == list_delete.size()) {
                                mEmptyLayout.setVisibility(View.VISIBLE);
                                mFullLayout.setVisibility(View.GONE);
                            }
                            adapter.removeItems(list_delete);
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                        }
                    });

                } else {
                    if (!list_delete.isEmpty()) {
                        DataPaser.shopCartCalculateParser(array, mHandler);
                    }

                }
                break;
        }
    }

    @Override
    public void onSnapToTop() {
// 下拉后，弹到顶部时，开始刷新数据
        if (!mInLoading) {
            mInLoading = true;
            mPullLayout.setEnableStopInActionView(true);
            mActionImage.clearAnimation();
            mActionImage.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
            mActionText.setText(R.string.note_pull_loading);

            DataPaser.shopCartParser(mHandler);

            mHandler.sendEmptyMessageDelayed(DataConstants.CUSTOM_PULLTOREFRESH_HOME, 1000);
        }
    }

    private void dataLoadFinished() {
        if (mInLoading) {
            mInLoading = false;
            mPullLayout.setEnableStopInActionView(false);
            mPullLayout.hideActionView();
            mActionImage.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);

            if (mPullLayout.isPullOut()) {
                mActionText.setText(R.string.note_pull_refresh);
                mActionImage.clearAnimation();
                mActionImage.startAnimation(mRotateUpAnimation);
            } else {
                mActionText.setText(R.string.note_pull_down);
            }

            mTimeText.setText(getString(R.string.note_update_at,
                    formatDate(new Date(System.currentTimeMillis()))));
        }
    }

    //定义日期格式，照搬即可
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void onPullOut() {
        if (!mInLoading) {
            mActionText.setText(R.string.note_pull_refresh);
            mActionImage.clearAnimation();
            mActionImage.startAnimation(mRotateUpAnimation);
        }
    }

    //照搬
    @Override
    public void onPullIn() {
        if (!mInLoading) {
            mActionText.setText(R.string.note_pull_down);
            mActionImage.clearAnimation();
            mActionImage.startAnimation(mRotateDownAnimation);
        }
    }

    private void initPullToRefresh() {
        mRotateUpAnimation = AnimationUtils.loadAnimation(this,
                R.anim.rotate_up);
        mRotateDownAnimation = AnimationUtils.loadAnimation(this,
                R.anim.rotate_down);
        mPullLayout = (PullRefreshLayout) findViewById(R.id.pull_container);
        mPullLayout.setOnActionPullListener(this);
        mPullLayout.setOnPullStateChangeListener(this);
        mPullLayout.setEnableStopInActionView(true);
        mProgress = findViewById(R.id.progress);
        mActionImage = findViewById(R.id.icon);
        mActionImage.setVisibility(View.VISIBLE);
        mActionText = (TextView) findViewById(R.id.pull_note);
        mActionText.setText("下拉刷新");
        mTimeText = (TextView) findViewById(R.id.refresh_time);
    }
}
