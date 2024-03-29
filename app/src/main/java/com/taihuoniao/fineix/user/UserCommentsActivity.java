package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.UserCommentsAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/5/4 19:17
 */
public class UserCommentsActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    private int unread_count;
    private List<CommentsBean.CommentItem> list;
    private static final String pageSize = "9999";
    private static final String COMMENT_TYPE = "12";
    private WaittingDialog dialog;
    private UserCommentsAdapter adapter;

    public UserCommentsActivity() {
        super(R.layout.activity_user_comments);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(getClass().getSimpleName())) {
            unread_count = intent.getIntExtra(getClass().getSimpleName(), 0);
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "评论");
        dialog = new WaittingDialog(this);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void installListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, CommentListActivity.class);
                intent.putExtra("target_id", list.get(i).target_id);
                intent.putExtra("target_user_id", commentsBean.getCurrent_user_id());
                intent.putExtra("type", 12 + "");
                intent.putExtra(UserCommentsActivity.class.getSimpleName(), list.get(i).getUser().getNickname());
                intent.putExtra("reply_id", list.get(i).get_id());
                intent.putExtra("reply_user_id", list.get(i).getUser().get_id());
                startActivity(intent);
            }
        });
    }
    private HttpResponse<CommentsBean> commentsBean = new HttpResponse<>();
    @Override
    protected void requestNet() {
        int curPage = 1;
        HashMap<String, String> params = ClientDiscoverAPI.getmycommentsListRequestParams(String.valueOf(curPage), pageSize, null, LoginInfo.getUserId() + "", COMMENT_TYPE);
        HttpRequest.post(params, URL.MY_COMMENTS_LIST, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (!activity.isFinishing() && dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                    }
                }, DataConstants.DIALOG_DELAY);
                if (TextUtils.isEmpty(json)) return;
                LogUtil.e(TAG, json);
                commentsBean = JsonUtil.json2Bean(json,new TypeToken<HttpResponse<CommentsBean>>(){});
                if (commentsBean.isSuccess() && commentsBean.getData() != null) {
                    list = commentsBean.getData().getRows();
                    refreshUI();
                } else {
                    ToastUtils.showError(commentsBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError("网络异常，请确认网络畅通");
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (list == null) return;
        if (list.size() == 0) {
            return;
        }
        for (int i = 0; i < unread_count; i++) {
            list.get(i).is_unread = true;
        }
        if (adapter == null) {
            adapter = new UserCommentsAdapter(list, activity);
            lv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
