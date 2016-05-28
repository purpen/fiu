package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.UserCommentsAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/5/4 19:17
 */
public class UserCommentsActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    private int curPage=1;
    private int unread_count;
    private List<CommentsBean.CommentItem> list;
    private static final String pageSize="9999";
    private static final String COMMENT_TYPE="12";
    private SVProgressHUD dialog;
    private UserCommentsAdapter adapter;
    public UserCommentsActivity(){
        super(R.layout.activity_user_comments);
    }
    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(getClass().getSimpleName())){
            unread_count = intent.getIntExtra(getClass().getSimpleName(),0);
        }
    }
    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"评论");
        dialog=new SVProgressHUD(this);
    }

    @Override
    protected void installListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, SceneDetailActivity.class);
                intent.putExtra("id",list.get(i).target_id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void requestNet() {
        dialog.show();
        ClientDiscoverAPI.mycommentsList(String.valueOf(curPage),pageSize,null, LoginInfo.getUserId()+"", COMMENT_TYPE, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e(TAG,responseInfo.result);
                CommentsBean commentsBean = JsonUtil.fromJson(responseInfo.result,CommentsBean.class);
                if (commentsBean.isSuccess()){
                    if (commentsBean==null) return;
                    if (commentsBean.getData()==null) return;
                    list=commentsBean.getData().getRows();
                    refreshUI();
                }else {
                    Util.makeToast(commentsBean.getMessage());
                }
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
    protected void refreshUI() {
        if (list==null) return;
        if (list.size()==0) {
            Util.makeToast("暂无评论");
            return;
        }

        for (int i=0;i<unread_count;i++){
            list.get(i).is_unread=true;
        }

        if (adapter==null){
            adapter=new UserCommentsAdapter(list,activity);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
}
