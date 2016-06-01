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
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.FindFriendData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/8 17:45
 */
public class FindFriendAdapter extends CommonBaseAdapter<FindFriendData.User>{
    private FindFriendRecycleViewAdapter adapter;
    private ImageLoader imageLoader;
    private SVProgressHUD svProgressHUD;
    public FindFriendAdapter(List list, Activity activity){
        super(list,activity);
        this.imageLoader=ImageLoader.getInstance();
        this.svProgressHUD=new SVProgressHUD(activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FindFriendData.User item = list.get(position);
        ViewHolder holder=null;
        if (convertView==null){
            convertView= Util.inflateView(activity, R.layout.item_find_friend,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(item.medium_avatar_url,holder.riv,options);
        holder.tv_name.setText(item.nickname);
//        if (TextUtils.equals(item.sex,"1")){
//            holder.tv_name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.male,0);
//        }else {
//            holder.tv_name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.female,0);
//        }

        if (item.areas!=null &&item.areas.size()>=2){
            holder.tv_desc.setVisibility(View.VISIBLE);
            holder.tv_desc.setText(String.format("%s %s",item.areas.get(0),item.areas.get(1)));
        }else {
            holder.tv_desc.setVisibility(View.INVISIBLE);
        }
        if (item.is_love==FansAdapter.NOT_LOVE){
            holder.btn.setText("关注");
            holder.btn.setTextColor(activity.getResources().getColor(R.color.color_333));
            holder.btn.setBackgroundResource(R.drawable.border_radius5);
        }else {
            holder.btn.setText("已关注");
            holder.btn.setTextColor(activity.getResources().getColor(android.R.color.white));
            holder.btn.setBackgroundResource(R.drawable.border_radius5_pressed);
        }
        setOnClickListener(holder.btn,item);
        LinearLayoutManager manager = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
        holder.recycler_view.setLayoutManager(manager);
        if (item.scene_sight !=null || item.scene_sight.size()>0){
            adapter=new FindFriendRecycleViewAdapter(activity,item.scene_sight);
            holder.recycler_view.setAdapter(adapter);
            adapter.setmOnItemClickLitener(new FindFriendRecycleViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

//                    Util.makeToast("positon=="+position);
                    Intent intent = new Intent(activity, SceneDetailActivity.class);
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
                                button.setText("已关注");
                                button.setTextColor(activity.getResources().getColor(android.R.color.white));
                                button.setBackgroundResource(R.drawable.border_radius5_pressed);
//                                Util.makeToast(response.getMessage());
                                return;
                            }

                            svProgressHUD.showErrorWithStatus(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            button.setEnabled(true);
                            svProgressHUD.showErrorWithStatus("网络异常,请确认网络畅通");
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
                                button.setText("关注");
                                button.setTextColor(activity.getResources().getColor(R.color.color_333));
                                button.setBackgroundResource(R.drawable.border_radius5);
//                                Util.makeToast(response.getMessage());
                                svProgressHUD.showSuccessWithStatus("已取消关注");
                                return;
                            }

                            svProgressHUD.showErrorWithStatus(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            button.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            svProgressHUD.showErrorWithStatus("网络异常,请确认网络畅通");
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
