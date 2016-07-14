package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.view.SlidingFocusImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/5.
 */
public class GoodListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private Activity activity;
    private List<ProductBean.ProductListItem> list;
    private List<SearchBean.SearchItem> searchList;
    private boolean isScrolling;//用来判断listview是否正在滑动的标识
    private SceneListViewAdapter.SceneListAdapterScrollListener sceneListAdapterScrollListener;//外界用来调用的滑动监听

    public GoodListAdapter(Activity activity, List<ProductBean.ProductListItem> list, List<SearchBean.SearchItem> searchList) {
        this.activity = activity;
        this.list = list;
        this.searchList = searchList;
    }

    /**
     * 设置listview用来监听listview的滑动状态
     *
     * @param listView 设置监听滑动状态的listview
     */
    public void setListView(AbsListView listView) {
        listView.setOnScrollListener(this);
    }

    /**
     * 设置滑动监听,是外部可以调用
     *
     * @param sceneListAdapterScrollListener 外部调用用来替换onscrolllisttener的监听
     */
    public void setSceneListAdapterScrollListener(SceneListViewAdapter.SceneListAdapterScrollListener sceneListAdapterScrollListener) {
        this.sceneListAdapterScrollListener = sceneListAdapterScrollListener;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else if (searchList != null) {
            return searchList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else if (searchList != null) {
            return searchList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_good_listview, null);
            holder = new ViewHolder();
            holder.slidingFocusImageView = (SlidingFocusImageView) convertView.findViewById(R.id.sfiv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.priceTv = (TextView) convertView.findViewById(R.id.tv_price);
            holder.img = (ImageView) convertView.findViewById(R.id.img_fiu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.slidingFocusImageView.getTag() != null) {
            int pa = (int) holder.slidingFocusImageView.getTag();
            if (pa == position) {
                return convertView;
            }
        }
        holder.slidingFocusImageView.setUnselectedAlpha(0.6f);
        holder.slidingFocusImageView.setAnimationDuration(500);
        holder.slidingFocusImageView.setFadingEdgeLength(200);
        holder.slidingFocusImageView.setGravity(Gravity.CENTER_VERTICAL);
        final int p = position;
//        holder.slidingFocusImageView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int posi, long id) {
////                pos = position;
//                if(list!=null){
//                    list.get(p).setPos(posi);
//                }else{
//                    searchList.get(p).pos = posi;
//                }
//                ProductSlidingAdapter productSlidingAdapter = (ProductSlidingAdapter) holder.slidingFocusImageView.getTag();
//                productSlidingAdapter.notifyDataSetChanged();
////                Log.e("<<<切换焦点", p + "," + list.get(p).getPos()+",适配器"+productSlidingAdapter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        if (list != null) {
            holder.nameTv.setText(list.get(position).getTitle());
            holder.priceTv.setText(String.format("¥%s", list.get(position).getSale_price()));
            if (list.get(position).getBanner_asset().size() > 0) {
                //设置滑动锁
                if (isScrolling) {
                    holder.slidingFocusImageView.setAdapter(new ProductSlidingAdapter(activity, list.get(position), null, true));
                } else {
                    holder.slidingFocusImageView.setAdapter(new ProductSlidingAdapter(activity, list.get(position), null, false));
                    holder.slidingFocusImageView.setTag(position);
                }
//                Log.e("<<<适配器",productSlidingAdapter1.toString());
                if ((list.get(position).getSights() != null && list.get(position).getSights().size() > 0 && list.get(position).getSights().get(0) != null) || list.get(position).getCover_url() != null) {
                    switch (list.get(position).getBanner_asset().size()) {
                        case 2:
                            holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2);
                            break;
                        case 4:
                            holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2 + 2);
                            break;
                        default:
                            holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2 + 1);
                            break;
                    }
                } else {
                    holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2 - 1);
                }
            }
//            holder.slidingFocusImageView.setSelection(0);
            switch (list.get(position).getAttrbute()) {
                case "1":
                    holder.img.setImageResource(R.mipmap.product_fiu);
                    break;
                case "2":
                    holder.img.setImageResource(R.mipmap.product_taobao);
                    break;
                case "3":
                    holder.img.setImageResource(R.mipmap.product_tianmao);
                    break;
                case "4":
                    holder.img.setImageResource(R.mipmap.product_jingdong);
                    break;
                default:
                    holder.img.setVisibility(View.INVISIBLE);
                    break;
            }
            ClickListener clickListener = new ClickListener(activity, list, null, position);
            holder.slidingFocusImageView.setOnItemClickListener(clickListener);
            holder.nameTv.setOnClickListener(clickListener);
            holder.priceTv.setOnClickListener(clickListener);
            holder.img.setOnClickListener(clickListener);
        } else if (searchList != null) {
            holder.nameTv.setText(searchList.get(position).getTitle());
            holder.priceTv.setText(String.format("¥%s", searchList.get(position).getSale_price()));
            if (searchList.get(position).getBanners().size() > 0) {
//                holder.slidingFocusImageView.setAdapter(new SlidingFocusAdapter<String>(holder.slidingFocusImageView, null, searchList.get(position).getBanners(), activity));
                ProductSlidingAdapter productSlidingAdapter1 = new ProductSlidingAdapter(activity, null, searchList.get(position), false);
                holder.slidingFocusImageView.setAdapter(productSlidingAdapter1);
                holder.slidingFocusImageView.setTag(productSlidingAdapter1);
                holder.slidingFocusImageView.setSelection(Integer.MAX_VALUE / 2 - 1);
            }
            switch (searchList.get(position).getAttrbute()) {
                case "1":
                    holder.img.setImageResource(R.mipmap.product_fiu);
                    break;
                case "2":
                    holder.img.setImageResource(R.mipmap.product_taobao);
                    break;
                case "3":
                    holder.img.setImageResource(R.mipmap.product_tianmao);
                    break;
                case "4":
                    holder.img.setImageResource(R.mipmap.product_jingdong);
                    break;
                default:
                    holder.img.setVisibility(View.INVISIBLE);
                    break;
            }
            ClickListener clickListener = new ClickListener(activity, null, searchList, position);
            holder.slidingFocusImageView.setOnItemClickListener(clickListener);
            holder.nameTv.setOnClickListener(clickListener);
            holder.priceTv.setOnClickListener(clickListener);
            holder.img.setOnClickListener(clickListener);
        }
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (sceneListAdapterScrollListener != null) {
            sceneListAdapterScrollListener.onScrollStateChanged(view, scrollState);
        }
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            isScrolling = false;
            notifyDataSetChanged();
        } else {
            isScrolling = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (sceneListAdapterScrollListener != null) {
            sceneListAdapterScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    static class ClickListener implements View.OnClickListener, AdapterView.OnItemClickListener {
        private Activity activity;
        private int position;
        private List<ProductBean.ProductListItem> list;
        private List<SearchBean.SearchItem> searchList;

        public ClickListener(Activity activity, List<ProductBean.ProductListItem> list, List<SearchBean.SearchItem> searchList, int position) {
            this.activity = activity;
            this.list = list;
            this.searchList = searchList;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            if (list != null) {
                intent.putExtra("id", list.get(position).get_id());
            } else if (searchList != null) {
                intent.putExtra("id", searchList.get(position).get_id());
            }
            activity.startActivity(intent);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            if (list != null) {
                if (list.get(this.position).getSights() != null && list.get(this.position).getSights().size() > 0 && list.get(this.position).getSights().get(0) != null) {
                    if (position % (1 + list.get(this.position).getBanner_asset().size()) == list.get(this.position).getBanner_asset().size()) {
                        Intent intent1 = new Intent(activity, SceneDetailActivity.class);
                        intent1.putExtra("id", list.get(this.position).getSights().get(0).getId());
                        activity.startActivity(intent1);
                        return;
                    } else {
                        intent.putExtra("id", list.get(this.position).get_id());
                    }
                } else {
                    intent.putExtra("id", list.get(this.position).get_id());
                }

            } else if (searchList != null) {
                intent.putExtra("id", searchList.get(this.position).get_id());
            }
            activity.startActivity(intent);
        }
    }

    static class ViewHolder {
        SlidingFocusImageView slidingFocusImageView;
        TextView nameTv;
        TextView priceTv;
        ImageView img;
    }
}
