package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.LoveSceneBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.beans.SubsCjListBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.SceneTitleSetUtils;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

/**
 * Created by taihuoniao on 2016/4/19.
 * 由于加载的图片过大过多。所以采用了滑动时不加载图片的方法
 */
public class SceneListViewAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private SceneListAdapterScrollListener sceneListAdapterScrollListener;//外界用来调用的滑动监听
    private Context context;
    private List<SceneList.DataBean.RowsBean> list;
    private List<LoveSceneBean.LoveSceneItem> loveList;
    private List<SearchBean.Data.SearchItem> searchList;
    private List<SubsCjListBean.SubsCJItem> subsList;
    private DisplayImageOptions options500_500, options750_1334;

    public SceneListViewAdapter(Context context, List<SceneList.DataBean.RowsBean> list, List<LoveSceneBean.LoveSceneItem> loveList,
                                List<SearchBean.Data.SearchItem> searchList, List<SubsCjListBean.SubsCJItem> subsList) {
        this.context = context;
        this.list = list;
        this.loveList = loveList;
        this.searchList = searchList;
        this.subsList = subsList;
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
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
    public void setSceneListAdapterScrollListener(SceneListAdapterScrollListener sceneListAdapterScrollListener) {
        this.sceneListAdapterScrollListener = sceneListAdapterScrollListener;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else if (loveList != null) {
            return loveList.size();
        } else if (searchList != null) {
            return searchList.size();
        } else if (subsList != null) {
            return subsList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else if (loveList != null) {
            return loveList.get(position);
        } else if (searchList != null) {
            return searchList.get(position);
        } else if (subsList != null) {
            return subsList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
//            Log.e("<<<", "创建布局");
            convertView = View.inflate(context, R.layout.item_scenelist, null);
            holder = new ViewHolder();
            holder.container = (RelativeLayout) convertView.findViewById(R.id.item_scenelist_container);
            holder.pointContainer = (RelativeLayout) convertView.findViewById(R.id.item_scenelist_point_container);
            holder.backgroundImg = (ImageView) convertView.findViewById(R.id.item_scenelist_backgroundimg);
            holder.userHeadImg = (RoundedImageView) convertView.findViewById(R.id.item_scenelist_user_headimg);
            holder.vImg = (RoundedImageView) convertView.findViewById(R.id.riv_auth);
            holder.userName = (TextView) convertView.findViewById(R.id.item_scenelist_user_name);
            holder.userInfo = (TextView) convertView.findViewById(R.id.item_scenelist_user_info);
            holder.viewCount = (TextView) convertView.findViewById(R.id.item_scenelist_view_count);
            holder.loveCount = (TextView) convertView.findViewById(R.id.item_scenelist_love_count);
            holder.frameLayout = (FrameLayout) convertView.findViewById(R.id.item_scenelist_frame);
            holder.sceneImg = (ImageView) convertView.findViewById(R.id.item_scenelist_title_img);
            holder.sceneTitle = (TextView) convertView.findViewById(R.id.item_scenelist_scene_title);
            holder.suoshuQingjing = (TextView) convertView.findViewById(R.id.item_scenelist_suoshuqingjing);
            holder.location = (TextView) convertView.findViewById(R.id.item_scenelist_location);
            holder.time = (TextView) convertView.findViewById(R.id.item_scenelist_time);
            holder.container.setLayoutParams(new AbsListView.LayoutParams(
                    MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenWidth() * 16 / 9));
            holder.bottomLinear = (LinearLayout) convertView.findViewById(R.id.item_scenedetails_bottomlinear);
            holder.bottomLinear.setLayoutParams(new RelativeLayout.LayoutParams(
                    MainApplication.getContext().getScreenWidth(), MainApplication.getContext().getScreenWidth() * 8 / 9));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.backgroundImg.getTag() != null && holder.backgroundImg.getTag().toString().equals(list.get(position).getCover_url())) {
//            if (list != null && list.get(position).isStartAnim()) {
//                holder.bottomLinear.setTranslationY(holder.bottomLinear.getMeasuredHeight());
////            new Handler().post(new Runnable() {
////                @Override
////                public void run() {
//                ObjectAnimator.ofFloat(holder.bottomLinear, "translationY", 0).setDuration(600).start();
//                list.get(position).setStartAnim(false);
////                }
////            });
//            }
            return convertView;
        }
        holder.backgroundImg.setImageResource(R.mipmap.default_background_750_1334);
        holder.pointContainer.removeAllViews();
        if (list != null) {
            if (!isScrolling) {
                ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), holder.backgroundImg);
                holder.backgroundImg.setTag(list.get(position).getCover_url());
                if (list.get(position).getProduct() != null && list.get(position).getProduct().size() > 0) {
                    addProductToImg(list.get(position).getProduct(), holder.pointContainer);
                }
            }
            //数据为空
            ImageLoader.getInstance().displayImage(list.get(position).getUser_info().getAvatar_url(), holder.userHeadImg, options500_500);
            holder.userName.setText(list.get(position).getUser_info().getNickname());
            if (list.get(position).getUser_info().getIs_expert() == 1) {
                holder.userInfo.setText(list.get(position).getUser_info().getExpert_label() + " | " + list.get(position).getUser_info().getExpert_info());
                holder.vImg.setVisibility(View.VISIBLE);
            } else {
                holder.userInfo.setText(list.get(position).getUser_info().getSummary().toString());
                holder.vImg.setVisibility(View.GONE);
            }
            holder.viewCount.setText(list.get(position).getView_count());
            holder.loveCount.setText(list.get(position).getLove_count());
            holder.sceneTitle.setText(list.get(position).getTitle());
//            holder.suoshuQingjing.setText(list.get(position).getScene_title());
            holder.location.setText(list.get(position).getAddress());
            holder.time.setText(list.get(position).getCreated_at());
        } else if (loveList != null) {
            if (loveList.get(position).getProduct() != null && loveList.get(position).getProduct().size() > 0) {
                addProductToImg(loveList.get(position).getProduct(), holder.pointContainer);
            }
            ImageLoader.getInstance().displayImage(loveList.get(position).getCover_url(), holder.backgroundImg, options750_1334);
//            Log.e("<<<", "用户头像url=" + loveList.get(position).getUser_info().getAvatar_ur());
            ImageLoader.getInstance().displayImage(loveList.get(position).getUser_info().getAvatar_ur(), holder.userHeadImg, options500_500);
            holder.userName.setText(loveList.get(position).getUser_info().getNickname());
            if (loveList.get(position).getUser_info().getIs_expert() == 1) {
                holder.userInfo.setText(loveList.get(position).getUser_info().getExpert_label() + " | " + loveList.get(position).getUser_info().getExpert_info());
                holder.vImg.setVisibility(View.VISIBLE);
            } else {
                holder.userInfo.setText(loveList.get(position).getUser_info().getSummary());
                holder.vImg.setVisibility(View.GONE);
            }
//            isSpertAndSummary(holder.userInfo, loveList.get(position).getUser_info().getLabel(), loveList.get(position).getUser_info().getSummary());
            holder.viewCount.setText(loveList.get(position).getView_count());
            holder.loveCount.setText(loveList.get(position).getLove_count());
            holder.sceneTitle.setText(loveList.get(position).getTitle());
            holder.suoshuQingjing.setText(loveList.get(position).getScene_title());
            holder.location.setText(loveList.get(position).getAddress());
            holder.time.setText(loveList.get(position).getCreated_at());
        } else if (searchList != null) {
            ImageLoader.getInstance().displayImage(searchList.get(position).getCover_url(), holder.backgroundImg, options750_1334);
//            Log.e("<<<", "用户头像url=" + loveList.get(position).getUser_info().getAvatar_ur());
            ImageLoader.getInstance().displayImage(searchList.get(position).getUser_info().getAvatar_url(), holder.userHeadImg, options500_500);
            holder.userName.setText(searchList.get(position).getUser_info().getNickname());
            if (searchList.get(position).getUser_info().getIs_expert() == 1) {
                holder.userInfo.setText(searchList.get(position).getUser_info().getExpert_label() + " | " + searchList.get(position).getUser_info().getExpert_info());
                holder.vImg.setVisibility(View.VISIBLE);
            } else {
                holder.userInfo.setText(searchList.get(position).getUser_info().getSummary());
                holder.vImg.setVisibility(View.GONE);
            }
//            isSpertAndSummary(holder.userInfo, searchList.get(position).getUser_info().getLabel(), searchList.get(position).getUser_info().getSummary());
            holder.viewCount.setText(searchList.get(position).getView_count());
            holder.loveCount.setText(searchList.get(position).getLove_count());
            holder.sceneTitle.setText(searchList.get(position).getTitle());
            holder.suoshuQingjing.setText(searchList.get(position).getScene_title());
            holder.location.setText(searchList.get(position).getAddress());
            holder.time.setText(searchList.get(position).getCreated_at());
        } else if (subsList != null) {
            if (subsList.get(position).getProduct() != null && subsList.get(position).getProduct().size() > 0) {
                addProductToImg(subsList.get(position).getProduct(), holder.pointContainer);
            }

            ImageLoader.getInstance().displayImage(subsList.get(position).getCover_url(), holder.backgroundImg, options750_1334);
//            Log.e("<<<", "用户头像url=" + loveList.get(position).getUser_info().getAvatar_ur());
            ImageLoader.getInstance().displayImage(subsList.get(position).getUser_info().getAvatar_url(), holder.userHeadImg, options500_500);
            holder.userName.setText(subsList.get(position).getUser_info().getNickname());
            if (subsList.get(position).getUser_info().getIs_expert() == 1) {
                holder.userInfo.setText(subsList.get(position).getUser_info().getExpert_label() + " | " + subsList.get(position).getUser_info().getExpert_info());
                holder.vImg.setVisibility(View.VISIBLE);
            } else {
                holder.userInfo.setText(subsList.get(position).getUser_info().getSummary());
                holder.vImg.setVisibility(View.GONE);
            }
//            isSpertAndSummary(holder.userInfo, subsList.get(position).getUser_info().getLabel(), subsList.get(position).getUser_info().getSummary());
            holder.viewCount.setText(subsList.get(position).getView_count());
            holder.loveCount.setText(subsList.get(position).getLove_count());
            holder.sceneTitle.setText(subsList.get(position).getTitle());
            holder.suoshuQingjing.setText(subsList.get(position).getScene_title());
            holder.location.setText(subsList.get(position).getAddress());
            holder.time.setText(subsList.get(position).getCreated_at());
        }
        SceneTitleSetUtils.setTitle(holder.sceneTitle, holder.frameLayout, holder.sceneImg, 12, 1);
        return convertView;
    }


    private void addProductToImg(List<SceneList.DataBean.RowsBean.ProductBean> productList, RelativeLayout container) {
        for (final SceneList.DataBean.RowsBean.ProductBean product : productList) {
//            Log.e("<<<", productList.toString());
            final LabelView labelView = new LabelView(context);
            TagItem tagItem = new TagItem();
            tagItem.setId(product.getId()+"");
            tagItem.setName(product.getTitle());
//            tagItem.setPrice("¥" + product.getPrice());
            labelView.init(tagItem);
            final RelativeLayout.LayoutParams labelLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            labelView.pointOrAll(false, false);
            labelLp.leftMargin = (int) (product.getX() * MainApplication.getContext().getScreenWidth());
            labelLp.topMargin = (int) (product.getY() * MainApplication.getContext().getScreenWidth() * 16 / 9);
            labelView.setLayoutParams(labelLp);
            container.addView(labelView);
//            labelView.stopAnim();
            labelView.wave();
        }
    }

    private boolean isScrolling;//用来判断listview是否正在滑动的标识

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (sceneListAdapterScrollListener != null) {
            sceneListAdapterScrollListener.onScrollStateChanged(view, scrollState);
        }
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            isScrolling = false;
            SceneListViewAdapter.this.notifyDataSetChanged();
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

    static class ViewHolder {
        RelativeLayout container;
        RelativeLayout pointContainer;
        ImageView backgroundImg;
        RoundedImageView userHeadImg;
        RoundedImageView vImg;
        TextView userName;
        TextView userInfo;
        TextView viewCount;
        TextView loveCount;
        FrameLayout frameLayout;
        ImageView sceneImg;
        TextView sceneTitle;
        TextView suoshuQingjing;
        TextView location;
        TextView time;
        public LinearLayout bottomLinear;
    }

    public interface SceneListAdapterScrollListener extends AbsListView.OnScrollListener {

    }
}
