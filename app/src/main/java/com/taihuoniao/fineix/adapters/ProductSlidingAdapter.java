//package com.taihuoniao.fineix.adapters;
//
//import android.app.Activity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Gallery;
//import android.widget.ImageView;
//
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.beans.ProductBean;
//import com.taihuoniao.fineix.beans.SearchBean;
//import com.taihuoniao.fineix.utils.Util;
//
///**
// * Created by taihuoniao on 2016/6/17.
// */
//public class ProductSlidingAdapter extends BaseAdapter {
//    private Activity activity;
//    private ProductBean.ProductListItem productListItem;
//    private SearchBean.Data.SearchItem searchItem;
//    private DisplayImageOptions options;
//    private boolean isScrolling;
//
//    public ProductSlidingAdapter(Activity activity, ProductBean.ProductListItem productListItem, SearchBean.Data.SearchItem searchItem, boolean isScrolling) {
//        this.activity = activity;
//        this.productListItem = productListItem;
//        this.searchItem = searchItem;
//        this.isScrolling = isScrolling;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_background_750_1334)
//                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
//                .showImageOnFail(R.mipmap.default_background_750_1334)
//                .cacheInMemory(true)
//                .cacheOnDisk(true).considerExifParams(true)
//                .build();
//    }
//
//    @Override
//    public int getCount() {
//        if (productListItem != null) {
//            if (productListItem.getBanner_asset() != null && productListItem.getBanner_asset().size() > 0) {
//                return Integer.MAX_VALUE;
//            }
//        } else if (searchItem != null) {
//            if (searchItem.getBanners() != null && searchItem.getBanners().size() > 0) {
//                return Integer.MAX_VALUE;
//            }
//        }
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        if (productListItem != null) {
//            if (productListItem.getBanner_asset() != null && productListItem.getBanner_asset().size() > 0) {
//                return productListItem.getBanner_asset().get(position);
//            }
//        } else if (searchItem != null) {
//            if (searchItem.getBanners() != null && searchItem.getBanners().size() > 0) {
//                return searchItem.getBanners().get(position);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            convertView = View.inflate(parent.getContext(), R.layout.view_sliding_focus, null);
//            holder = new ViewHolder();
//            convertView.setTag(holder);
//            holder.left = (ImageView) convertView.findViewById(R.id.view_sliding_focus_left);
//            holder.iv = (ImageView) convertView.findViewById(R.id.view_sliding_focus_img);
////            holder.zhezhaoTv = (TextView) convertView.findViewById(R.id.view_sliding_focus_zhezhao);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        if (productListItem != null) {
////            Log.e("<<<遮罩", position + "," + productListItem.getPos());
////            if (position == productListItem.getPos()) {
////                holder.zhezhaoTv.setVisibility(View.GONE);
////            } else {
////                holder.zhezhaoTv.setVisibility(View.VISIBLE);
////            }
//            if (isScrolling) {
//                holder.iv.setImageResource(R.mipmap.default_background_750_1334);
//            }
//            if (productListItem.getSights() != null && productListItem.getSights().size() > 0 && productListItem.getSights().get(0) != null) {
//                if (position % (1 + productListItem.getBanner_asset().size()) == productListItem.getBanner_asset().size()) {
//                    convertView.setLayoutParams(new Gallery.LayoutParams(activity.getResources().getDimensionPixelSize(R.dimen.dp90), activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//                    if (!isScrolling) {
//                        ImageLoader.getInstance().displayImage(productListItem.getSights().get(0).getCover_url(), holder.iv);
//                    }
//                    holder.left.setVisibility(View.GONE);
//                    return convertView;
//                }
//                if (!isScrolling) {
//                    ImageLoader.getInstance().displayImage(productListItem.getBanner_asset().get(position % (1 + productListItem.getBanner_asset().size())), holder.iv);
//                }
//                switch (productListItem.getBanner_asset().size()) {
//                    case 2:
//                        if (position % 3 == 1) {
//                            holder.left.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.left.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 3:
//                        if (position % 4 == 1) {
//                            holder.left.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.left.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 4:
//                        if (position % 5 == 1 || position % 5 == 3) {
//                            holder.left.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.left.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 5:
//                        if (position % 6 == 1 || position % 6 == 3) {
//                            holder.left.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.left.setVisibility(View.GONE);
//                        }
//                        break;
//                    default:
//                        holder.left.setVisibility(View.GONE);
//                        break;
//                }
//                convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//                return convertView;
//            } else if (productListItem.getCover_url() != null) {
//                if (position % (1 + productListItem.getBanner_asset().size()) == productListItem.getBanner_asset().size()) {
//                    convertView.setLayoutParams(new Gallery.LayoutParams(activity.getResources().getDimensionPixelSize(R.dimen.dp90), activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//                    if (!isScrolling) {
//                        ImageLoader.getInstance().displayImage(productListItem.getCover_url(), holder.iv);
//                    }
//                    holder.left.setVisibility(View.GONE);
//                    return convertView;
//                }
//                if (!isScrolling) {
//                    ImageLoader.getInstance().displayImage(productListItem.getBanner_asset().get(position % (1 + productListItem.getBanner_asset().size())), holder.iv);
//                }
//                switch (productListItem.getBanner_asset().size()) {
//                    case 2:
//                        if (position % 3 == 1) {
//                            holder.left.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.left.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 3:
//                        if (position % 4 == 1) {
//                            holder.left.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.left.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 4:
//                        if (position % 5 == 1 || position % 5 == 3) {
//                            holder.left.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.left.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 5:
//                        if (position % 6 == 1 || position % 6 == 3) {
//                            holder.left.setVisibility(View.VISIBLE);
//                        } else {
//                            holder.left.setVisibility(View.GONE);
//                        }
//                        break;
//                    default:
//                        holder.left.setVisibility(View.GONE);
//                        break;
//                }
//                convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//                return convertView;
//            }
//            if (!isScrolling) {
//                ImageLoader.getInstance().displayImage(productListItem.getBanner_asset().get(position % (productListItem.getBanner_asset().size())), holder.iv);
//            }
//            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//            if (position % 2 == 1) {
//                holder.left.setVisibility(View.VISIBLE);
//            } else {
//                holder.left.setVisibility(View.GONE);
//            }
//            return convertView;
//        } else if (searchItem != null) {
////            if (position == searchItem.pos) {
////                holder.zhezhaoTv.setVisibility(View.GONE);
////            } else {
////                holder.zhezhaoTv.setVisibility(View.VISIBLE);
////            }
//            ImageLoader.getInstance().displayImage(searchItem.getBanners().get(position % (searchItem.getBanners().size())), holder.iv);
//            if (position % 2 == 1) {
//                holder.left.setVisibility(View.VISIBLE);
//            } else {
//                holder.left.setVisibility(View.GONE);
//            }
//            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth() - 200, activity.getResources().getDimensionPixelSize(R.dimen.dp150)));
//            return convertView;
//        }
//        return convertView;
//    }
//
//    static class ViewHolder {
//        ImageView left;
//        ImageView iv;
////        TextView zhezhaoTv;
//    }
//}
