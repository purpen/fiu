//package com.taihuoniao.fineix.qingjingOrSceneDetails;
//
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
//import com.taihuoniao.fineix.base.BaseActivity;
//import com.taihuoniao.fineix.view.GlobalTitleLayout;
//import com.taihuoniao.fineix.view.WaittingDialog;
//import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
//import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
//
//import butterknife.Bind;
//
///**
// * Created by taihuoniao on 2016/5/11.
// */
//public class SubsCJListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
//    @Bind(R.id.activity_subs_cjlist_title)
//    GlobalTitleLayout titleLayout;
//    @Bind(R.id.activity_subs_cjlist_pullrefreshview)
//    PullToRefreshListView pullToRefreshView;
//    @Bind(R.id.activity_subs_cjlist_progress)
//    ProgressBar progressBar;
//    private ListView listView;
//    private WaittingDialog dialog;
//    //网络请求返回数据
//    //场景列表
//    private int page = 1;
////    private List<SubsCjListBean.SubsCJItem> list;
//    private SceneListViewAdapter sceneListViewAdapter;
//
//    public SubsCJListActivity() {
//        super(R.layout.activity_subs_cjlist);
//    }
//
//    @Override
//    protected void initView() {
//        titleLayout.setBackgroundResource(R.color.white);
//        titleLayout.setBackImg(R.mipmap.back_black);
//        titleLayout.setTitle(R.string.subs, getResources().getColor(R.color.black333333));
//        listView = pullToRefreshView.getRefreshableView();
//        listView.setDivider(null);
//        listView.setDividerHeight(0);
//        dialog = new WaittingDialog(SubsCJListActivity.this);
//        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                if (!dialog.isShowing()) {
//                    dialog.show();
//                }
//                requestNet();
//            }
//        });
//        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//            @Override
//            public void onLastItemVisible() {
//                progressBar.setVisibility(View.VISIBLE);
//                page++;
//                requestNet();
//            }
//        });
//    }
//
//    @Override
//    protected void initList() {
////        list = new ArrayList<>();
////        sceneListViewAdapter = new SceneListViewAdapter(SubsCJListActivity.this, null, null, null, list);
//        listView.setAdapter(sceneListViewAdapter);
//        listView.setOnItemClickListener(this);
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//    }
//
//    @Override
//    protected void requestNet() {
//
////        ClientDiscoverAPI.subsQJList(page + "", 8 + "", null, new RequestCallBack<String>() {
////            @Override
////            public void onSuccess(ResponseInfo<String> responseInfo) {
////                pullToRefreshView.onRefreshComplete();
////                SubsCjListBean subsCjListBean = new SubsCjListBean();
////                try {
////                    Gson gson = new Gson();
////                    Type type = new TypeToken<SubsCjListBean>() {
////                    }.getType();
////                    subsCjListBean = gson.fromJson(responseInfo.result, type);
////                } catch (JsonSyntaxException e) {
////                    Log.e("<<<", "数据解析异常" + e.toString());
////                }
////                dialog.dismiss();
////                progressBar.setVisibility(View.GONE);
////                if (subsCjListBean.isSuccess()) {
////                    pullToRefreshView.setLoadingTime();
////                    if (page == 1) {
////                        list.clear();
////                        pullToRefreshView.lastSavedFirstVisibleItem = -1;
////                        pullToRefreshView.lastTotalItem = -1;
////                    }
////                    list.addAll(subsCjListBean.getData().getRows());
////                    sceneListViewAdapter.notifyDataSetChanged();
////                }
////            }
////
////            @Override
////            public void onFailure(HttpException error, String msg) {
////                pullToRefreshView.onRefreshComplete();
////            }
////        });
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////        SubsCjListBean.SubsCJItem subsCJItem = (SubsCjListBean.SubsCJItem) listView.getAdapter().getItem(position);
////        if (subsCJItem != null) {
////            Intent intent = new Intent(SubsCJListActivity.this, SceneDetailActivity.class);
////            intent.putExtra("id", subsCJItem.get_id());
////            startActivity(intent);
////        }
//    }
//
//
//}
