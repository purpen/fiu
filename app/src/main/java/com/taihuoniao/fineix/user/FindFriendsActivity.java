package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FindFriendAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.FindFriendData;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.fragments.SearchUserFragment;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomSubItemLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author lilin
 *         created at 2016/4/26 16:06
 */
public class FindFriendsActivity extends BaseActivity<FindFriendData.User> implements PlatformActionListener, View.OnClickListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;

    CustomSubItemLayout item_wx;
    CustomSubItemLayout item_sina;
    CustomSubItemLayout item_contacts;
    @Bind(R.id.pull_lv)
    PullToRefreshListView pull_lv;

    private int curPage = 1;
    private static final String PAGE_SIZE = "10";
    private static final String SORT = "1";  //随机排序
    private static final String HAS_SCENE = "1";
    private FindFriendAdapter adapter;
    private List<FindFriendData.User> mList = new ArrayList();
    private ListView lv;

    private WaittingDialog dialog;

    public FindFriendsActivity() {
        super(R.layout.activity_find_freinds);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "发现好友");
        dialog = new WaittingDialog(this);
        custom_head.setHeadShopShow(true);
        custom_head.getShopImg().setImageResource(R.mipmap.scan);
        View view = Util.inflateView(R.layout.headview_findfriend, null);
        item_wx = ButterKnife.findById(view, R.id.item_wx);
        item_wx.setImg(R.mipmap.wechat);
        item_wx.setTitle("邀请微信好友");
        item_wx.setSubTitle("分享给好友");

        item_sina = ButterKnife.findById(view, R.id.item_sina);
        item_sina.setImg(R.mipmap.sina);
        item_sina.setTitle("连接微博");
        item_sina.setSubTitle("分享给微博好友");

        item_contacts = ButterKnife.findById(view, R.id.item_contacts);
        item_contacts.setImg(R.mipmap.icon_head);
        item_contacts.setTitle("连接通讯录");
        item_contacts.setSubTitle("关注你认识的好友");
        lv = pull_lv.getRefreshableView();
//        lv.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.dp05));
        pull_lv.getRefreshableView().addHeaderView(view);
        pull_lv.getRefreshableView().setHeaderDividersEnabled(false);
        pull_lv.setPullToRefreshEnabled(false);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void installListener() {
        pull_lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                isLoadMore = true;
//                requestNet();
            }
        });
        item_wx.setOnClickListener(this);
        item_sina.setOnClickListener(this);
        item_contacts.setOnClickListener(this);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //TODO 跳转个人中心//场景详情
//                Util.makeToast(i+"");
//            }
//        });
    }

    @Override
    protected void requestNet() {
        String sight_count = "5";
        ClientDiscoverAPI.findFriends(String.valueOf(curPage), PAGE_SIZE, sight_count, SORT, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null) {
                    if (curPage == 1) dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getSceneList", responseInfo.result);
                HttpResponse<FindFriendData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<FindFriendData>>() {
                });
                if (response.isSuccess()) {
                    FindFriendData data = response.getData();
                    List list = data.users;
                    refreshUI(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI(List<FindFriendData.User> list) {
        if (list == null) return;
        if (list.size() == 0) {
//            boolean isLoadMore = false;
//            if (isLoadMore) {
//                Util.makeToast("没有更多数据哦！");
//            } else {
//                Util.makeToast("暂无数据！");
//            }
            return;
        }

        curPage++;

//        Iterator<FindFriendData.User> iterator = list.iterator();
//        while (iterator.hasNext()){
//            FindFriendData.User user = iterator.next();
//            if (user.is_love==FansAdapter.LOVE){
//                iterator.remove();
//            }
//        }

        if (adapter == null) {
            mList.addAll(list);
            adapter = new FindFriendAdapter(mList, activity);
            lv.setAdapter(adapter);
        } else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }

        if (pull_lv != null)
            pull_lv.onRefreshComplete();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    ToastUtils.showError("对不起，分享出错");
                    break;
                case 2:
                    ToastUtils.showInfo("您取消了分享");
                    break;
                case 1:
                    ToastUtils.showSuccess("分享成功");
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        Platform.ShareParams params;
        switch (view.getId()) {
            case R.id.item_wx:
                params = new Platform.ShareParams();
                params.setShareType(Platform.SHARE_TEXT);
                params.setTitle(getResources().getString(R.string.app_name));
                params.setText(getResources().getString(R.string.share_txt));
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.share(params);
                break;
            case R.id.item_sina:
                params = new Platform.ShareParams();
                params.setText(getResources().getString(R.string.share_txt));
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(this); // 设置分享事件回调
                weibo.share(params);
                break;
            case R.id.item_contacts:
                Uri sms = Uri.parse("smsto:");
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, sms);
                sendIntent.putExtra("sms_body", getResources().getString(R.string.share_txt));
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
                break;

        }
    }

    @OnClick({R.id.head_view_shop, R.id.rl_search_user})
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.head_view_shop:
                startActivity(new Intent(activity, CaptureActivity.class));
                break;
            case R.id.rl_search_user:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        SearchUserFragment newFragment = SearchUserFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), SearchUserFragment.class.getSimpleName());
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        handler.sendEmptyMessage(3);
    }

}
