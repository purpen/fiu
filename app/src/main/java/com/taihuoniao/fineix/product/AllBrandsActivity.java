package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hb.views.PinnedSectionListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CharBrandAdapter;
import com.taihuoniao.fineix.adapters.MineBrandGridAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/7/11.
 */
public class AllBrandsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    @Bind(android.R.id.list)
    PinnedSectionListView listView;
    @Bind(R.id.titlelayout)
    GlobalTitleLayout titlelayout;
    @Bind(R.id.char_list_view)
    ListView charListView;
    //headerView下的控件
    private GridViewForScrollView gridView;
    private LinearLayout allBrandLinear;
    private WaittingDialog dialog;
    private int page = 1;
    private int size = Integer.MAX_VALUE;
    private List<BrandListBean.DataBean.RowsBean> allBrandList;//网络请求返回全部品牌列表
    private List<Item> itemList;

    public AllBrandsActivity() {
        super(R.layout.activity_all_brands);
    }

    @Override
    protected void initView() {
        titlelayout.setBackgroundResource(R.color.white);
        titlelayout.setContinueTvVisible(false);
        titlelayout.setBackImg(R.mipmap.back_black);
        titlelayout.setTitle(R.string.brand, getResources().getColor(R.color.black333333));
        dialog = new WaittingDialog(this);
        listView.setOnItemClickListener(this);
        allBrandList = new ArrayList<>();
        itemList = new ArrayList<>();
        View header = View.inflate(this, R.layout.header_brand_list, null);
        gridView = (GridViewForScrollView) header.findViewById(R.id.grid_view);
        allBrandLinear = (LinearLayout) header.findViewById(R.id.all_brand_ll);
        gridView.setOnItemClickListener(this);
        allBrandLinear.setOnClickListener(this);
        listView.addHeaderView(header);
        charListView.setOnItemClickListener(this);
    }

    @Override
    protected void requestNet() {
        dialog.show();
        brandList(1, 6, null, 1 + "", null);//自营品牌推荐列表
        brandList(page, size, null, null, null);//全部品牌列表
    }

    private void brandList(int page, final int size, String mark, String self_run, String stick) {
        //品牌列表
        ClientDiscoverAPI.brandList(page, size, mark, self_run, stick, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<品牌列表", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                BrandListBean brandListBean = new BrandListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BrandListBean>() {
                    }.getType();
                    brandListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常" + e.toString());
                }
                dialog.dismiss();
                if (brandListBean.isSuccess()) {
                    if (size > 10) {
                        //全部品牌
                        Collections.sort(brandListBean.getData().getRows());
                        allBrandList = brandListBean.getData().getRows();
//                        initializeHeaderAndFooter();
                        initializeAdapter();
                        charListView.setAdapter(new CharBrandAdapter(AllBrandsActivity.this, itemList));
                    } else {
                        //推荐的自营品牌
                        gridView.setAdapter(new MineBrandGridAdapter(AllBrandsActivity.this, brandListBean.getData().getRows(), true, gridView));
                    }
                } else {
                    ToastUtils.showError(brandListBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case android.R.id.list:
                Item item = (Item) listView.getAdapter().getItem(position);
                if (item != null) {
                    if (item.type == Item.ITEM) {
                        Intent intent = new Intent(this, BrandDetailActivity.class);
                        intent.putExtra("id", item.get_id());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.grid_view:
                BrandListBean.DataBean.RowsBean rowsBean = (BrandListBean.DataBean.RowsBean) gridView.getAdapter().getItem(position);
//                ToastUtils.showError("跳转到品牌详情");
                Intent intent = new Intent(this, BrandDetailActivity.class);
                intent.putExtra("id", rowsBean.get_id());
                startActivity(intent);
                break;
            case R.id.char_list_view:
                String str = (String) charListView.getAdapter().getItem(position);
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).type == Item.SECTION) {
                        if (itemList.get(i).text.equals(str)) {
                            listView.setSelection(i + listView.getHeaderViewsCount());
                            break;
                        }
                    }
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_brand_ll:
                startActivity(new Intent(this, MineBrandActivity.class));
                break;
        }
    }

    static class SimpleAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
        private List<BrandListBean.DataBean.RowsBean> brandList;
        private List<Item> itemList;

        public SimpleAdapter(List<BrandListBean.DataBean.RowsBean> brandList, List<Item> itemList) {
            this.brandList = brandList;
            this.itemList = itemList;
            generateDataset(false);
        }

        public void generateDataset(boolean clear) {

            if (clear) {
                itemList.clear();
            }
            int sectionPosition = 0, listPosition = 0;
            if (brandList.size() > 0) {
                Item item = new Item(Item.SECTION, brandList.get(0).getMark().toUpperCase());
                item.sectionPosition = sectionPosition;
                item.listPosition = listPosition++;
                itemList.add(item);
                if (brandList.size() > 1) {
                    for (int i = 1; i < brandList.size(); i++) {
                        if (brandList.get(i).getMark().equals(brandList.get(i - 1).getMark())) {
                            Item item1 = new Item(Item.ITEM, brandList.get(i).getTitle());
                            item1.set_id(brandList.get(i).get_id());
                            item1.setUrl(brandList.get(i).getCover_url());
                            item1.sectionPosition = sectionPosition;
                            item1.listPosition = listPosition++;
                            itemList.add(item1);
                        } else {
                            sectionPosition++;
                            Item item1 = new Item(Item.SECTION, brandList.get(i).getMark().toUpperCase());
                            item1.sectionPosition = sectionPosition;
                            item1.listPosition = listPosition++;
                            itemList.add(item1);
                        }
                    }
                }
            }
        }

        protected void prepareSections(int sectionsNumber) {
        }

        protected void onSectionAdded(Item section, int sectionPosition) {
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Item getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_brand_list1, null);
                holder = new ViewHolder();
                holder.markTv = (TextView) convertView.findViewById(R.id.item_brand_list1_tv);
                holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.item_brand_list_rl);
                holder.brandImg = (RoundedImageView) convertView.findViewById(R.id.item_brand_list_img);
                holder.nameTv = (TextView) convertView.findViewById(R.id.item_brand_list_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (itemList.get(position).type == Item.SECTION) {
                holder.relativeLayout.setVisibility(View.GONE);
                holder.markTv.setVisibility(View.VISIBLE);
                holder.markTv.setText(itemList.get(position).text);
            } else {
                holder.relativeLayout.setVisibility(View.VISIBLE);
                holder.markTv.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(itemList.get(position).getUrl(), holder.brandImg);
                holder.nameTv.setText(itemList.get(position).text);
            }

            return convertView;
        }

        static class ViewHolder {
            TextView markTv;
            RelativeLayout relativeLayout;
            RoundedImageView brandImg;
            TextView nameTv;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == Item.SECTION;
        }

    }

    static class FastScrollAdapter extends SimpleAdapter implements SectionIndexer {

        private Item[] sections;

        public FastScrollAdapter(List<BrandListBean.DataBean.RowsBean> brandList, List<Item> itemList) {
            super(brandList, itemList);
        }


        @Override
        protected void prepareSections(int sectionsNumber) {
            sections = new Item[sectionsNumber];
        }

        @Override
        protected void onSectionAdded(Item section, int sectionPosition) {
            sections[sectionPosition] = section;
        }

        @Override
        public Item[] getSections() {
            return sections;
        }

        @Override
        public int getPositionForSection(int section) {
            if (section >= sections.length) {
                section = sections.length - 1;
            }
            return sections[section].listPosition;
        }

        @Override
        public int getSectionForPosition(int position) {
            if (position >= getCount()) {
                position = getCount() - 1;
            }
            return getItem(position).sectionPosition;
        }

    }

    public static class Item {

        public static final int ITEM = 0;
        public static final int SECTION = 1;

        public final int type;
        public final String text;

        public int sectionPosition;
        public int listPosition;

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }

        private String _id;
        private String url;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return text;
        }

    }

    private boolean hasHeaderAndFooter;
    private boolean isFastScroll;
    private boolean addPadding;
    private boolean isShadowVisible = true;
    private int mDatasetUpdateCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isFastScroll = savedInstanceState.getBoolean("isFastScroll");
            addPadding = savedInstanceState.getBoolean("addPadding");
            isShadowVisible = savedInstanceState.getBoolean("isShadowVisible");
            hasHeaderAndFooter = savedInstanceState.getBoolean("hasHeaderAndFooter");
        }

        initializePadding();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFastScroll", isFastScroll);
        outState.putBoolean("addPadding", addPadding);
        outState.putBoolean("isShadowVisible", isShadowVisible);
        outState.putBoolean("hasHeaderAndFooter", hasHeaderAndFooter);
    }

    private void initializePadding() {
        float density = getResources().getDisplayMetrics().density;
        int padding = addPadding ? (int) (16 * density) : 0;
        listView.setPadding(padding, padding, padding, padding);
    }

    private void initializeAdapter() {
        listView.setFastScrollEnabled(isFastScroll);
        if (isFastScroll) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            listView.setFastScrollAlwaysVisible(true);
//            }
            listView.setAdapter(new FastScrollAdapter(allBrandList, itemList));
        } else {
            listView.setAdapter(new SimpleAdapter(allBrandList, itemList));
        }
    }

}
