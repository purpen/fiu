package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.UsableRedPacketAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CheckRedBagUsable;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.RedPacketData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class UsableRedPacketActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.pull_lv)
    PullToRefreshListView pull_lv;
    @Bind(R.id.foot_view)
    LinearLayout foot_view;
    private static final String PAGE_SIZE = "10";
    private int curPage = 1;
    private List<RedPacketData.RedPacketItem> mList = new ArrayList<>();
    private ListView lv;
    public static final String UNUSED = "1";//未使用过红包

    public static final String ALLUSED = "0";//全部

    public static final String UNTIMEOUT = "1";//未过期

    public static final String TIMEOUT = "2";//已过期
    private String mRid;//订单号
    private WaittingDialog mDialog;
    private UsableRedPacketAdapter adapter;
    private int mCurrentScrollState;
    private boolean bIsMoved = false;
    private boolean bIsDown = false;
    private int mDeltaY;
    private float mMotionY;
    public UsableRedPacketActivity() {
        super(R.layout.activity_red_bag);
    }

    public boolean isFirstLoad = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.PARSER_CHECK_REDBAG_USABLE:
                    mDialog.dismiss();
                    if (msg.obj != null) {
                        if (msg.obj instanceof CheckRedBagUsable) {
                            CheckRedBagUsable checkRedBagUsable = (CheckRedBagUsable) msg.obj;
                            if ("1".equals(checkRedBagUsable.getUseful())) {
                                //红包可用
                                Intent intent = new Intent();
                                intent.putExtra("code", checkRedBagUsable.getCode());//红包码
                                intent.putExtra("money", checkRedBagUsable.getCoin_money());//红包金额
                                setResult(DataConstants.RESULTCODE_REDBAG, intent);
                                finish();
                            }
//                          else {
//                                //红包不可用
//                                ToastUtils.showError("这个红包不可用");
//                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "我的红包");
        foot_view.setVisibility(View.VISIBLE);
        lv = pull_lv.getRefreshableView();
        mDialog = new WaittingDialog(this);
        mRid = getIntent().getStringExtra("rid");
//        WindowUtils.chenjin(this);
    }

    @Override
    protected void requestNet() {//请求可用红包
        RequestParams params = ClientDiscoverAPI.getmyRedBagNetRequestParams(String.valueOf(curPage), PAGE_SIZE, UNUSED, UNTIMEOUT);
        HttpRequest.post(params,   URL.MY_BONUS, new GlobalDataCallBack(){
//        ClientDiscoverAPI.myRedBagNet(String.valueOf(curPage), PAGE_SIZE, UNUSED, UNTIMEOUT, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!activity.isFinishing() && mDialog != null&& isFirstLoad) mDialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (mDialog != null) mDialog.dismiss();
                HttpResponse<RedPacketData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<RedPacketData>>() {
                });
                if (response.isSuccess()) {
                    if (isFirstLoad) {
                        isFirstLoad = false;
                    }
                    List<RedPacketData.RedPacketItem> rows = response.getData().rows;
                    refreshUI(rows);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (mDialog != null) mDialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void installListener() {
        foot_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, UnUsableRedPacketActivity.class));
            }
        });

        pull_lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                requestNet();
            }
        });

        pull_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                mList.clear();
                requestNet();
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RedPacketData.RedPacketItem redPacketItem = (RedPacketData.RedPacketItem) lv.getAdapter().getItem(i);
                //获取红包操作
                if (mRid != null) {
                    mDialog.show();
//                                            验证红包是否可用
                    DataPaser.checkRedbagUsableParser(mRid, redPacketItem.code, mHandler);
                }
            }
        });

        pull_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {//firstVisibleItem, visibleItemCount, totalItemCount
                showBottomViewOnBottom(totalItemCount);
            }
        });

        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float y = motionEvent.getY();
                float x = motionEvent.getX();
                int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        action_down(y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mDeltaY = (int) (y - mMotionY);
                        bIsMoved = true;
                        //移动的时候，要移除掉显示bottomView的消息
                        //补齐action_down事件，因为有的时候，action_down 事件没有执行
                        action_down(y);
                        break;
                    case MotionEvent.ACTION_UP:
                        bIsMoved = false;
                        bIsDown = false;
                        //没有动作,则过1000ms后显示foot_view
                        mHandler.postDelayed(showBottomBarRunnable, 1000);
                        if (mDeltaY < 0) { //下滑隐藏
                            hideBottomBar();
                        } else {  //上滑显示
                            showBottomBar();
                        }
                        break;

                }

                return false;
            }
        });
    }

    private void action_down(float y){
        mMotionY = y;
        bIsDown = true;
        mHandler.removeCallbacks(showBottomBarRunnable);
    }

    private void showBottomViewOnBottom(int totalItemCount) {

        if(lv.getLastVisiblePosition() ==   totalItemCount -1){
            showBottomBar();
        }
    }

    private Runnable showBottomBarRunnable = new Runnable() {
        @Override
        public void run() {
            showBottomBar();
        }

    };

    public void showBottomBar() {
        if (foot_view != null && foot_view.getVisibility() == View.GONE) {
            Animation translateAnimation = new TranslateAnimation(foot_view.getLeft(), foot_view.getLeft(),foot_view.getHeight(), 0);
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            foot_view.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    foot_view.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void hideBottomBar() {
        if (foot_view != null && foot_view.getVisibility() == View.VISIBLE) {
            Animation translateAnimation = new TranslateAnimation(foot_view.getLeft(), foot_view.getLeft(), 0, foot_view.getHeight());
            translateAnimation.setDuration(300);
            translateAnimation.setInterpolator(new OvershootInterpolator(0.6f));
            foot_view.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    foot_view.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected void refreshUI(List list) {
        curPage++;
        if (pull_lv != null) {
            pull_lv.onRefreshComplete();
            pull_lv.setLoadingTime();
        }
        if (list == null) return;
        if (list.size() == 0) return;
        mList.addAll(list);
        if (adapter == null) {
            adapter = new UsableRedPacketAdapter(mList, activity);
            lv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
