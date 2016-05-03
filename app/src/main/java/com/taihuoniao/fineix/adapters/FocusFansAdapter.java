package com.taihuoniao.fineix.adapters;
import android.app.Activity;
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
import com.taihuoniao.fineix.beans.FocusFansItem;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.user.FocusFansActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.io.FileOutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/4/22 19:00
 */
public class FocusFansAdapter extends CommonBaseAdapter<FocusFansItem> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private String pageType;
    private static final int TYPE1=1; //单向关注
    private static final int TYPE2=2; //互向关注
    public FocusFansAdapter(List<FocusFansItem> list, Activity activity,String pageType) {
        super(list, activity);
        this.imageLoader=ImageLoader.getInstance();
        this.pageType=pageType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FocusFansItem item = list.get(position);
        ViewHolder holder=null;
        if (convertView==null){
            convertView= Util.inflateView(activity,R.layout.item_focus_fans,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        imageLoader.displayImage(item.follows.avatar_url,holder.riv,options);
        holder.tv_name.setText(item.follows.nickname);
        holder.tv_desc.setText(item.follows.summary);

        if (TextUtils.equals(FocusFansActivity.FOCUS_TYPE,pageType)){
            holder.btn.setText("已关注");
            holder.btn.setTextColor(activity.getResources().getColor(android.R.color.white));
            holder.btn.setBackgroundResource(R.drawable.border_radius5_pressed);
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//取消关注
                    View view1 = Util.inflateView(activity, R.layout.popup_focus_fans, null);
                    RoundedImageView riv = (RoundedImageView) view1.findViewById(R.id.riv);
                    TextView tv_take_photo = (TextView) view1.findViewById(R.id.tv_take_photo);
                    TextView tv_album = (TextView) view1.findViewById(R.id.tv_album);
                    TextView tv_cancel = (TextView) view1.findViewById(R.id.tv_cancel);
                    ImageLoader.getInstance().displayImage(item.follows.avatar_url,riv,options);
                    tv_take_photo.setText("停止关注"+item.follows.nickname+"?");
                    tv_album.setText("停止关注");
                    tv_album.setOnClickListener(FocusFansAdapter.this);
                    tv_album.setTag(item);
                    tv_cancel.setOnClickListener(FocusFansAdapter.this);
                    PopupWindowUtil.show(activity,view1);
                }
            });
        }

        if (TextUtils.equals(FocusFansActivity.FANS_TYPE,pageType)){
            switch (item.type){
                case TYPE1:  //仅当粉丝关注我
                    holder.btn.setText("关注");
                    holder.btn.setTextColor(activity.getResources().getColor(R.color.color_333));
                    holder.btn.setBackgroundResource(R.drawable.border_radius5);
                    holder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View view1 = Util.inflateView(activity, R.layout.popup_focus_fans, null);
                            RoundedImageView riv = (RoundedImageView) view1.findViewById(R.id.riv);
                            TextView tv_take_photo = (TextView) view1.findViewById(R.id.tv_take_photo);
                            TextView tv_album = (TextView) view1.findViewById(R.id.tv_album);
                            TextView tv_cancel = (TextView) view1.findViewById(R.id.tv_cancel);
                            ImageLoader.getInstance().displayImage(item.follows.avatar_url,riv,options);
                            tv_take_photo.setText("开始关注"+item.follows.nickname+"?");
                            tv_album.setText("开始关注");
                            tv_album.setOnClickListener(FocusFansAdapter.this);
                            tv_album.setTag(item);
                            tv_cancel.setOnClickListener(FocusFansAdapter.this);
                            PopupWindowUtil.show(activity,view1);
                        }
                    });
                    break;
                case TYPE2: //互粉
                    holder.btn.setText("已关注");
                    holder.btn.setTextColor(activity.getResources().getColor(android.R.color.white));
                    holder.btn.setBackgroundResource(R.drawable.border_radius5_pressed);
                    holder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View view1 = Util.inflateView(activity, R.layout.popup_focus_fans, null);
                            RoundedImageView riv = (RoundedImageView) view1.findViewById(R.id.riv);
                            TextView tv_take_photo = (TextView) view1.findViewById(R.id.tv_take_photo);
                            TextView tv_album = (TextView) view1.findViewById(R.id.tv_album);
                            TextView tv_cancel = (TextView) view1.findViewById(R.id.tv_cancel);
                            ImageLoader.getInstance().displayImage(item.follows.avatar_url,riv,options);
                            tv_take_photo.setText("停止关注"+item.follows.nickname+"?");
                            tv_album.setText("停止关注");
                            tv_album.setOnClickListener(FocusFansAdapter.this);
                            tv_album.setTag(item);
                            tv_cancel.setOnClickListener(FocusFansAdapter.this);
                            PopupWindowUtil.show(activity,view1);
                        }
                    });
                    break;
            }
        }
        return convertView;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                PopupWindowUtil.dismiss();
                break;
            case R.id.tv_album:
                view.setEnabled(false);
                if (TextUtils.equals(FocusFansActivity.FOCUS_TYPE,pageType)){ //关注列表的取消关注操作
                    final FocusFansItem item =(FocusFansItem)view.getTag();
                    if (item==null) return;
                    ClientDiscoverAPI.cancelFocusOperate(item.follows.user_id+"", new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            view.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            if (responseInfo==null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()){
                                list.remove(item);
                                notifyDataSetChanged();
                                Util.makeToast(response.getMessage());
                                return;
                            }

                            Util.makeToast(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            view.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            Util.makeToast(s);
                        }
                    });
                }

                if (TextUtils.equals(FocusFansActivity.FANS_TYPE,pageType)){
                    final FocusFansItem item =(FocusFansItem)view.getTag();
                    if (item==null) return;
                    switch (item.type){
                        case TYPE1:  //关注粉丝操作
                            ClientDiscoverAPI.focusOperate(item.follows.user_id+"", new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    view.setEnabled(true);
                                    PopupWindowUtil.dismiss();
                                    if (responseInfo==null) return;
                                    if (TextUtils.isEmpty(responseInfo.result)) return;
                                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                                    if (response.isSuccess()){
                                        item.type=TYPE2;
                                        notifyDataSetChanged();
                                        Util.makeToast(response.getMessage());
                                        return;
                                    }
                                    Util.makeToast(response.getMessage());

                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    view.setEnabled(true);
                                    PopupWindowUtil.dismiss();
                                    Util.makeToast(s);
                                }
                            });
                            break;
                        case TYPE2:   //取消关注粉丝操作
                            ClientDiscoverAPI.cancelFocusOperate(item.follows.user_id+"", new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    view.setEnabled(true);
                                    PopupWindowUtil.dismiss();
                                    if (responseInfo==null) return;
                                    if (TextUtils.isEmpty(responseInfo.result)) return;
                                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                                    if (response.isSuccess()){
                                        item.type=TYPE1;
                                        notifyDataSetChanged();
                                        Util.makeToast(response.getMessage());
                                        return;
                                    }
                                    Util.makeToast(response.getMessage());
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    view.setEnabled(true);
                                    PopupWindowUtil.dismiss();
                                    Util.makeToast(s);
                                }
                            });
                            break;
                    }
                }

                break;
        }
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
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
