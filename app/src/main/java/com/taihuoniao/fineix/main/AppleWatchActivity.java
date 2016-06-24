package com.taihuoniao.fineix.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dodola.bubblecloud.BubbleCloudView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.FiuUserListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/6/24.
 */
public class AppleWatchActivity extends BaseActivity {
    @Bind(R.id.bubble)
    BubbleCloudView bubbleCloudView;

    public AppleWatchActivity() {
        super(R.layout.aaaaaaaa);
    }

    @Override
    protected void initView() {
        final WaittingDialog dialog = new WaittingDialog(this);
//        FileManagerImageLoader.prepare(this.getApplication());
        dialog.show();
        ClientDiscoverAPI.fiuUserList(1 + "", 60 + "", null, null, 1 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                FiuUserListBean fiuUserListBean = new FiuUserListBean();
                try {
                    Gson gson = new Gson();
                    Type type1 = new TypeToken<FiuUserListBean>() {
                    }.getType();
                    fiuUserListBean = gson.fromJson(responseInfo.result, type1);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                FiuUserListBean netUser = fiuUserListBean;
                if (netUser.isSuccess()) {
                    MyAdapter adapter = new MyAdapter(AppleWatchActivity.this, netUser.getData().getUsers());
                    bubbleCloudView.setAdapter(adapter);
                } else {
                    ToastUtils.showError(netUser.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
            }
        });

    }


    private static class MyAdapter extends ArrayAdapter<FiuUserListBean.FiuUserItem> {
        private List<FiuUserListBean.FiuUserItem> list;

        public MyAdapter(final Context context, final List<FiuUserListBean.FiuUserItem> contacts) {
            super(context, 0, contacts);
            list = contacts;
        }

        @Override
        public int getCount() {
//            Log.e("<<<appleWatch界面","list.size="+list.size());
            return list == null ? 0 : list.size();
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_apple_view, null);
            }
            final RoundedImageView itemRound = (RoundedImageView) view.findViewById(R.id.item_round);
//            FileManagerImageLoader.getInstance().addTask(getItem(position).getMedium_avatar_url(), itemRound, null, 48, 48, false);
            ImageLoader.getInstance().displayImage(getItem(position).getMedium_avatar_url(),itemRound);
            return view;
        }
    }
}
