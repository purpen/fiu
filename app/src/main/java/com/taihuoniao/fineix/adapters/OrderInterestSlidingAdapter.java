package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.SlidingFocusImageView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 * created at 2016/5/9 17:55
 */
public class OrderInterestSlidingAdapter extends CommonBaseAdapter<QingJingListBean.QingJingItem>{
    private SlidingFocusImageView sfiv;
    private ProgressBar progressBar;
    public OrderInterestSlidingAdapter(SlidingFocusImageView sfiv, List list, Activity activity, ProgressBar progressBar){
        super(list,activity);
        this.sfiv=sfiv;
        this.progressBar=progressBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QingJingListBean.QingJingItem item=list.get(position);
        ViewHolder holder;
        if (convertView==null) {
            convertView = Util.inflateView(R.layout.item_order_interest, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
            holder.iv.setScaleType(ImageView.ScaleType.FIT_XY);
        }else {
            holder= (ViewHolder)convertView.getTag();
        }

        GlideUtils.displayImage(item.getCover_url(),holder.iv);
        if (sfiv.getSelectedItemPosition()==position){
            convertView.setLayoutParams(new Gallery.LayoutParams(Util.getScreenWidth()- DensityUtils.dp2px(activity,120), ViewGroup.LayoutParams.WRAP_CONTENT));
        }else {
            convertView.setScaleY(0.9f);
            convertView.setScaleX(0.9f);
        }

        holder.tv_title.setText(item.getTitle());
        holder.tv_desc.setText(item.getAddress());
        if (item.isOrdered){
            holder.ibtn.setImageResource(R.mipmap.ordered_qj);
        }else {
            holder.ibtn.setImageResource(R.mipmap.order_qj);
        }
        setOnClickListener(holder.ibtn,item);
        return convertView;
    }

    private void setOnClickListener(final ImageButton itbn, final QingJingListBean.QingJingItem item){
        itbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                RequestParams requestParams = ClientDiscoverAPI.getsubsQingjingRequestParams(item.get_id());
                HttpRequest.post(requestParams, URL.SUBS_QINGJING, new GlobalDataCallBack(){
//                ClientDiscoverAPI.subsQingjing(item.get_id(), new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        if (progressBar!=null) progressBar.setVisibility(View.VISIBLE);
                        view.setEnabled(false);
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        progressBar.setVisibility(View.GONE);
                        view.setEnabled(true);
                        if (responseInfo==null) return;
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                        if (response.isSuccess()){
                            itbn.setImageResource(R.mipmap.ordered_qj);
                            item.isOrdered=true;
                            ToastUtils.showSuccess("订阅成功");
                            return;
                        }
                        ToastUtils.showInfo(response.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        progressBar.setVisibility(View.GONE);
                        view.setEnabled(true);
                        ToastUtils.showError("网络异常，请确认网络畅通");
                    }
                });
            }
        });
    }

    static class ViewHolder{
        @Bind(R.id.iv)
        RoundedImageView iv;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.ibtn)
        ImageButton ibtn;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
