package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FindFriendAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.FindFriendData;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.UserCJListData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomSubItemLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author lilin
 * created at 2016/4/26 16:06
 */
public class FindFriendsActivity extends BaseActivity implements PlatformActionListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.item_wx)
    CustomSubItemLayout item_wx;
    @Bind(R.id.item_sina)
    CustomSubItemLayout item_sina;
    @Bind(R.id.item_contacts)
    CustomSubItemLayout item_contacts;
    @Bind(R.id.pull_lv)
    PullToRefreshListView pull_lv;
    private int curPage = 1;
    private boolean isLoadMore = false;
    private static final String PAGE_SIZE = "10";
    private static final String SORT="1";  //随机排序
    private static final String HAS_SCENE="1";
    private FindFriendAdapter adapter;
    private List<FindFriendData.User> mList=new ArrayList();
    private ListView lv;
    private WaittingDialog dialog;
    public FindFriendsActivity(){
        super(R.layout.activity_find_freinds);
    }

    @Override
    protected void initView() {
        dialog=new WaittingDialog(this);
        custom_head.setHeadCenterTxtShow(true,"发现好友");
        custom_head.setHeadShopShow(true);
        custom_head.getShopImg().setImageResource(R.mipmap.scan);
        item_wx.setImg(R.mipmap.wechat);
        item_wx.setTitle("邀请微信好友");
        item_wx.setSubTitle("分享给好友");
        item_sina.setImg(R.mipmap.sina);
        item_sina.setTitle("连接微博");
        item_sina.setSubTitle("分享给微博好友");
        item_contacts.setImg(R.mipmap.icon_head);
        item_contacts.setTitle("连接通讯录");
        item_contacts.setSubTitle("关注你认识的好友");
        lv = pull_lv.getRefreshableView();
        lv.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.dp05));
        pull_lv.setPullToRefreshEnabled(false);
    }

    @Override
    protected void installListener() {
        pull_lv.setOnLastItemVisibleListener(new com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                requestNet();
            }
        });

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
        String sight_count="5";
        ClientDiscoverAPI.findFriends(String.valueOf(curPage), PAGE_SIZE,sight_count,SORT,new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog!=null){
                    if (curPage == 1) dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getSceneList", responseInfo.result);
                HttpResponse<FindFriendData> response = JsonUtil.json2Bean(responseInfo.result,new TypeToken<HttpResponse<FindFriendData>>(){});
                if (response.isSuccess()) {
                    FindFriendData data = response.getData();
                    List list = data.users;
                    refreshUI(list);
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI(List list) {
        if (list == null) return;
        if (list.size() == 0) {
            if (isLoadMore) {
                Util.makeToast("没有更多数据哦！");
            } else {
                Util.makeToast("暂无数据！");
            }
            return;
        }

        curPage++;

        if (adapter == null) {
            mList.addAll(list);
            adapter = new FindFriendAdapter(list,activity);
            lv.setAdapter(adapter);
        } else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }

        if (pull_lv != null)
            pull_lv.onRefreshComplete();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 3:
                    Util.makeToast("对不起，分享出错");
                    break;
                case 2:
                    Util.makeToast("您取消了分享");
                    break;
                case 1:
                    Util.makeToast("分享成功");
                    break;
            }
        }
    };

    @OnClick({R.id.head_view_shop,R.id.item_wx,R.id.item_sina,R.id.item_contacts})
    void onClick(View v){
        Platform.ShareParams params=null;
        switch (v.getId()){
            case R.id.head_view_shop:
                startActivity(new Intent(activity, CaptureActivity.class));
                break;
            case R.id.item_wx:
                //wechat
                params = new Platform.ShareParams();
                params.setShareType(Platform.SHARE_WEBPAGE);
                params.setUrl("http://www.taihuoniao.com");
                params.setTitle("有Fiu的生活，才够意思，快点扫码加我吧！查看个人主页>>http://www.taihuoniao.com/");
                params.setText("有Fiu才有意思");
                params.setImageUrl(LoginInfo.getHeadPicUrl());
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.share(params);
                break;
            case R.id.item_sina:
                //sina
                params = new Platform.ShareParams();
                params.setTitle("有Fiu才有意思！");
                params.setText("有Fiu的生活，才够意思，快点扫码加我吧！查看个人主页>>http://www.taihuoniao.com/");
                params.setImageUrl(LoginInfo.getHeadPicUrl());
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(this); // 设置分享事件回调
                weibo.share(params);
                break;
            case R.id.item_contacts:
                String content="有Fiu的生活，才够意思，快点扫码加我吧！查看个人主页>>http://www.taihuoniao.com/";
                Uri sms = Uri.parse("smsto:");
                Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, sms);
                sendIntent.putExtra( "sms_body",content);
                sendIntent.setType("vnd.android-dir/mms-sms" );
                startActivity(sendIntent);
                break;
        }
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
