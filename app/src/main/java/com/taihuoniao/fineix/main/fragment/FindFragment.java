package com.taihuoniao.fineix.main.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.PullToRefreshListViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;

public class FindFragment extends BaseFragment {
    private PullToRefreshListViewForScrollView pullToRefreshLayout;
    private WaittingDialog dialog;
    //场景列表
    private int currentPage = 1;//页码
    private double distance = 1000;//距离
    private double lat = 39.915119;//纬度
    private double lng = 116.403963;//经度

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_find, null);
        pullToRefreshLayout = (PullToRefreshListViewForScrollView) view.findViewById(R.id.fragment_find_pullrefresh);
        pullToRefreshLayout.setPullToRefreshEnabled(false);
        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {

            }
        });
        ListView listView = pullToRefreshLayout.getRefreshableView();
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {

    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.getSceneList(currentPage + "", 1 + "", distance + "", lng + "", lat + "", handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.SCENE_LIST:
                    dialog.dismiss();
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}
