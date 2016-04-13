package com.taihuoniao.fineix.adapters;

/**
 * Created by taihuoniao on 2016/4/11.
 */
//public class AllLabelViewPagerAdapter extends PagerAdapter {
//    private Context context;
//    private List<AllLabelBean> allLabelList;
//    private AllLabelListViewAdapter.MoreClick moreClick;
//    private HotLabelViewPagerAdapter.LabelClick labelClick;
//    private AllLabelListViewAdapter adapter1, adapter2, adapter3, adapter;
//    private int maxHeight;
//
//    public AllLabelViewPagerAdapter(Context context, List<AllLabelBean> allLabelList, AllLabelListViewAdapter.MoreClick moreClick, HotLabelViewPagerAdapter.LabelClick labelClick) {
//        this.context = context;
//        this.allLabelList = allLabelList;
//        this.moreClick = moreClick;
//        this.labelClick = labelClick;
//        maxHeight = DensityUtils.dp2px(context, 400);
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return allLabelList.get(position).getTitle_cn();
//    }
//
//
//    @Override
//    public int getCount() {
//        return allLabelList.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        View view = View.inflate(context, R.layout.view_alllabel_listview, null);
//        ListViewForScrollView listView = (ListViewForScrollView) view.findViewById(R.id.view_alllabel_listview_listview);
//        switch (position) {
//            case 0:
//                adapter = new AllLabelListViewAdapter(context, allLabelList.get(position).getChildren(), position, moreClick, labelClick);
//                listView.setAdapter(adapter);
//                break;
//            case 1:
//                adapter1 = new AllLabelListViewAdapter(context, allLabelList.get(position).getChildren(), position, moreClick, labelClick);
//                listView.setAdapter(adapter1);
//                break;
//            case 2:
//                adapter2 = new AllLabelListViewAdapter(context, allLabelList.get(position).getChildren(), position, moreClick, labelClick);
//                listView.setAdapter(adapter2);
//                break;
//            case 3:
//                adapter3 = new AllLabelListViewAdapter(context, allLabelList.get(position).getChildren(), position, moreClick, labelClick);
//                listView.setAdapter(adapter3);
//                break;
//        }
//        container.addView(view);
//        setMaxHeight(listView.getVHeight());
//        return view;
//    }
//
//    private void setMaxHeight(int height) {
//        Log.e("<<<", "height = " + height + ",maxHeight = " + maxHeight);
//        if (height > maxHeight) {
//            maxHeight = height;
//        }
//    }
//
//    public int getMaxHeight() {
//        return maxHeight;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(container.getChildAt(position));
//    }
//
//    @Override
//    public void notifyDataSetChanged() {
//        if (adapter != null)
//            adapter.notifyDataSetChanged();
//        if (adapter1 != null)
//            adapter1.notifyDataSetChanged();
//        if (adapter2 != null)
//            adapter2.notifyDataSetChanged();
//        if (adapter3 != null)
//            adapter3.notifyDataSetChanged();
//        super.notifyDataSetChanged();
//    }
//}
