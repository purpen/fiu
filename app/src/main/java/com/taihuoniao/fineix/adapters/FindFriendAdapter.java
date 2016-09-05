package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.FindFriendData;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/8 17:45
 */
public class FindFriendAdapter extends CommonBaseAdapter<FindFriendData.User>{
    private ImageLoader imageLoader;
    public FindFriendAdapter(List list, Activity activity){
        super(list,activity);
        this.imageLoader=ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FindFriendData.User item = list.get(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = Util.inflateView(R.layout.item_find_friend, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(item.medium_avatar_url,holder.riv,options);
        holder.tv_name.setText(item.nickname);

        if (!TextUtils.isEmpty(item.summary)){
            holder.tv_desc.setVisibility(View.VISIBLE);
            holder.tv_desc.setText(item.summary);
        }else {
            holder.tv_desc.setVisibility(View.INVISIBLE);
        }
        if (item.is_love==FansAdapter.NOT_LOVE){
            setFocusBtnStyle(holder.btn, activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_white, android.R.color.white, R.drawable.shape_subscribe_theme);
        }else {
            setFocusBtnStyle(holder.btn, activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic, android.R.color.white, R.drawable.border_radius5_pressed);
        }

        setOnClickListener(holder.btn,item);
        LinearLayoutManager manager = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.getScreenWidth() / 3);
        holder.recycler_view.setLayoutParams(params);
        holder.recycler_view.setHasFixedSize(true);
        holder.recycler_view.setLayoutManager(manager);
        if (item.scene_sight !=null || item.scene_sight.size()>0){
            FindFriendRecycleViewAdapter adapter = new FindFriendRecycleViewAdapter(activity, item.scene_sight);
            holder.recycler_view.setAdapter(adapter);
            adapter.setmOnItemClickLitener(new FindFriendRecycleViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(activity, QJDetailActivity.class);
                    LogUtil.e(TAG,item.scene_sight.get(position)._id+"");
                    intent.putExtra("id", item.scene_sight.get(position)._id);//场景ID
                    activity.startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        }

        holder.riv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UserCenterActivity.class);
                intent.putExtra(FocusActivity.USER_ID_EXTRA, item._id);//userID
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    private void setFocusBtnStyle(Button bt_focus, int dimensionPixelSize, int focus, int unfocus_pic, int color, int drawable) {
        bt_focus.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        bt_focus.setText(focus);
        bt_focus.setTextColor(activity.getResources().getColor(color));
        bt_focus.setBackgroundResource(drawable);
        bt_focus.setCompoundDrawablesWithIntrinsicBounds(unfocus_pic, 0, 0, 0);
    }

    private void setOnClickListener(final Button button,final FindFriendData.User item){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                if (item.is_love == FansAdapter.NOT_LOVE){
                    ClientDiscoverAPI.focusOperate(String.valueOf(item._id), new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            button.setEnabled(true);
                            if (responseInfo == null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("focusOperate",responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                item.is_love=FansAdapter.LOVE;
                                setFocusBtnStyle(button, activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic, android.R.color.white, R.drawable.border_radius5_pressed);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            button.setEnabled(true);
                            ToastUtils.showError(R.string.network_err);
                        }
                    });
                }else {
                    ClientDiscoverAPI.cancelFocusOperate(String.valueOf(item._id), new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            button.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            if (responseInfo==null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("cancelFocusOperate",responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()){
                                item.is_love=FansAdapter.NOT_LOVE;
                                setFocusBtnStyle(button, activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_white, android.R.color.white, R.drawable.shape_subscribe_theme);
                                return;
                            }
                            ToastUtils.showError(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            button.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            ToastUtils.showError(R.string.network_err);
                        }
                    });
                }
            }
        });
    }
    static class ViewHolder {
        @Bind(R.id.riv)
        ImageView riv;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.btn)
        Button btn;
        @Bind(R.id.recycler_view)
        RecyclerView recycler_view;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
