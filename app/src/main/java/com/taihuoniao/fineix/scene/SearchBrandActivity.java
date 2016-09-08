package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchBrandAdapter;
import com.taihuoniao.fineix.adapters.SearchProductAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.AddBrandBean;
import com.taihuoniao.fineix.beans.AddProductBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/15.
 */
public class SearchBrandActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //上个界面传递过来的品牌名称
    private String brandIntent;
    private String brandIdIntent;
    private String productIntent;
    @Bind(R.id.brand_name)
    TextView brandName;
    @Bind(R.id.search_cancel)
    TextView searchCancel;
    @Bind(R.id.search_delete)
    ImageView searchDelete;
    @Bind(R.id.search_edit_text)
    EditText searchEditText;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    @Bind(R.id.add_brand_relative)
    RelativeLayout addBrandRelative;
    @Bind(R.id.add_brand_name)
    TextView addBrandName;
    @Bind(R.id.add_product_relative)
    RelativeLayout addProductRelative;
    @Bind(R.id.add_product_name)
    TextView addProductName;
    private ListView listView;
    private List<SearchBean.Data.SearchItem> brandList;
    private SearchBrandAdapter searchBrandAdapter;
    private String currentInBrand;//正在输入的品牌名称
    private WaittingDialog dialog;
    private boolean onlyProduct;//是否只搜索产品

    public SearchBrandActivity() {
        super(R.layout.activity_search_brand);
    }

    @Override
    protected void getIntentData() {
        if (getIntent().hasExtra(PictureEditActivity.class.getSimpleName())) {
            brandIntent = getIntent().getStringExtra("brand");
        } else {
            brandIntent = getIntent().getStringExtra("brand");
            brandIdIntent = getIntent().getStringExtra("brandId");
            productIntent = getIntent().getStringExtra("product");
        }
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        listView = pullRefreshView.getRefreshableView();
        listView.setDividerHeight(0);
        listView.setDivider(null);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void initList() {
        addProductRelative.setOnClickListener(this);
        addBrandRelative.setOnClickListener(this);
        brandList = new ArrayList<>();
        searchBrandAdapter = new SearchBrandAdapter(brandList);
        listView.setAdapter(searchBrandAdapter);
        listView.setOnItemClickListener(this);
        searchCancel.setOnClickListener(this);
        searchDelete.setOnClickListener(this);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchEditText.getText().toString().length() > 0) {
                    searchDelete.setVisibility(View.VISIBLE);
                    if (onlyProduct) {
                        searchProduct(s.toString(), null);
                        return;
                    }
                    if (brandName.getText().length() > 0) {
                        searchProduct(s.toString(), cuurentBrandId);
                    } else {
                        currentInBrand = s.toString();
                        searchBrand(s.toString());
                    }
                } else {
                    searchDelete.setVisibility(View.GONE);
                    addBrandRelative.setVisibility(View.GONE);
                    addProductRelative.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6) {
                    if (SearchBrandActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchBrandActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    if (onlyProduct) {
                        if (searchEditText.getText().toString().length() > 0) {
                            addProduct(searchEditText.getText().toString(), null);
                            return false;
                        }
                        finish();
                        return false;
                    }
                    //如果选择品牌，则返回品牌，如果没有品牌。则不返回
                    if (brandName.getText().toString().length() > 0) {
                        if (searchEditText.getText().toString().length() > 0) {
                            addProduct(searchEditText.getText().toString(), cuurentBrandId);
                            return false;
                        }
                        Intent intent = new Intent();
                        String brand = brandName.getText().toString();
                        intent.putExtra("brand", brand);
                        intent.putExtra("brandId", cuurentBrandId);
                        setResult(1, intent);
                    }
                    finish();
                }
                return false;
            }
        });
        pullRefreshView.setPullToRefreshEnabled(false);
        if (getIntent().hasExtra(PictureEditActivity.class.getSimpleName())) {
            if (brandIntent != null) {
                searchEditText.setText(brandIntent);
            }
        } else {
            if (brandIntent != null) {
                if (brandIdIntent == null) {
                    searchEditText.setHint("请输入产品名称");
                    searchEditText.setText("");
                    brandName.setText(brandIntent);
                    addBrandRelative.setVisibility(View.GONE);
                    isProduct = true;
                } else {
                    searchEditText.setHint("请输入产品名称");
                    searchEditText.setText("");
                    brandName.setText(brandIntent);
                    cuurentBrandId = brandIdIntent;
                    currentBrandName = brandIntent;
                    addBrandRelative.setVisibility(View.GONE);
                    searchProduct(null, brandIdIntent);
                    isProduct = true;
                }
            } else {
                onlyProduct = true;
                addBrandRelative.setVisibility(View.GONE);
                searchEditText.setHint("请输入产品名称");
                Log.e("<<<", "接收产品名称=" + productIntent);
                searchEditText.setText(productIntent);
            }
        }
    }

    private boolean isProduct;//现在搜索的是品牌还是商品
//    private boolean isSelfBrand;//品牌是否是自己添加的

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_product_relative:
                if (onlyProduct) {
                    addProduct(addProductName.getText().toString(), null);
                    return;
                }
                addProduct(addProductName.getText().toString(), cuurentBrandId);
                break;
            case R.id.add_brand_relative:
                addBrand(currentInBrand);
                break;
            case R.id.search_cancel:
                if (searchEditText.getText().toString().length() > 0) {
                    searchEditText.setText("");
                } else {
                    finish();
                }
                break;
            case R.id.search_delete:
                searchEditText.setText("");
                break;
        }
    }

    private void addProduct(String title, String brand_id) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.addProduct(title, brand_id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("<<<添加产品", responseInfo.result);
                AddProductBean addProductBean = new AddProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<AddProductBean>() {
                    }.getType();
                    addProductBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (addProductBean.isSuccess()) {
                    String brand = brandName.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("brand", brand);
                    intent.putExtra("brandId", cuurentBrandId);
                    intent.putExtra("product", searchEditText.getText().toString());
                    intent.putExtra("productId", addProductBean.getData().getId());
                    setResult(1, intent);
                    finish();
                } else {
                    ToastUtils.showError(addProductBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private void addBrand(String title) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.addBrand(title, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                AddBrandBean addBrandBean = new AddBrandBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<AddBrandBean>() {
                    }.getType();
                    addBrandBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "添加品牌解析异常" + e.toString());
                }
                if (addBrandBean.isSuccess()) {
                    searchEditText.setHint("请输入产品名称");
                    searchEditText.setText("");
                    brandName.setText(currentInBrand);
                    cuurentBrandId = addBrandBean.getData().getId();
                    currentBrandName = currentInBrand;
                    addBrandRelative.setVisibility(View.GONE);
                    searchProduct(null, cuurentBrandId);
                    isProduct = true;
                    brandList.clear();
                    searchBrandAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(addBrandBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private void searchBrand(final String q) {
        ClientDiscoverAPI.search(q, "13", null,"1","100", "content", null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<搜索品牌", responseInfo.result);
                SearchBean searchBean = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    searchBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (searchBean.isSuccess()) {
                    brandList.clear();
                    brandList.addAll(searchBean.getData().getRows());
                    searchBrandAdapter.notifyDataSetChanged();
                    if (brandList.size() <= 0) {
                        addBrandName.setText(q);
                        addBrandRelative.setVisibility(View.VISIBLE);
                    } else {
                        addBrandRelative.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private List<ProductBean.ProductListItem> productList;
    private SearchProductAdapter searchProductAdapter;

    private void searchProduct(String title, String brand_id) {
        ClientDiscoverAPI.getProductList(title, null, null, brand_id, null, "1", "300", null, null, null, null, "9,16", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<品牌下的产品",responseInfo.result);
                ProductBean productBean = new ProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductBean>() {
                    }.getType();
                    productBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<品牌下的产品","解析异常"+e.toString());
                }
                if (productBean.isSuccess()) {
                    productList = new ArrayList<>();
                    productList.addAll(productBean.getData().getRows());
                    searchProductAdapter = new SearchProductAdapter(currentBrandName, productList);
                    listView.setAdapter(searchProductAdapter);
                    if (searchEditText.getText().length() > 0 && productList.size() <= 0) {
                        addProductRelative.setVisibility(View.VISIBLE);
                        addProductName.setText(searchEditText.getText());
                    } else {
                        addProductRelative.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private String currentBrandName;
    private String cuurentBrandId;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onlyProduct) {
            Intent intent = new Intent();
            intent.putExtra("product", productList.get(position).getTitle());
            intent.putExtra("productId", productList.get(position).get_id());
            intent.putExtra("type", 2);
            setResult(1, intent);
            finish();
            return;
        }
        if (!isProduct) {
            addBrandRelative.setVisibility(View.GONE);
            searchEditText.setHint("请输入产品名称");
            searchEditText.setText("");
            brandName.setText(brandList.get(position).getTitle());
            cuurentBrandId = brandList.get(position).get_id();
            currentBrandName = brandList.get(position).getTitle();
            searchProduct(null, brandList.get(position).get_id());
            isProduct = true;
            brandList.clear();
            searchBrandAdapter.notifyDataSetChanged();
        } else {
            String brand = brandName.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("brand", brand);
            intent.putExtra("brandId", cuurentBrandId);
            intent.putExtra("product", productList.get(position).getTitle());
            intent.putExtra("productId", productList.get(position).get_id());
            intent.putExtra("type", 2);
            setResult(1, intent);
            finish();
        }
    }
}
