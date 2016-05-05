package com.taihuoniao.fineix.user;

import android.text.TextUtils;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;

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
    private static final String COMMENT_TYPE="12";
    public UserCommentsActivity(){
        super(R.layout.activity_user_comments);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"评论");
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.commentsList(String.valueOf(curPage), null, COMMENT_TYPE, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e(TAG,responseInfo.result);


            }
            @Override
            public void onFailure(HttpException e, String s) {
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI() {

    }
}
