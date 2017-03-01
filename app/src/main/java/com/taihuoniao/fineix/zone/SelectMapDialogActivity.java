package com.taihuoniao.fineix.zone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.zxing.RGBLuminanceSource;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.DividerLine;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by lilin on 2017/02/22.
 * 选择导航地图
 */

public class SelectMapDialogActivity extends BaseActivity {
    public static final String GAO_DE="com.autonavi.minimap";
    public static final String BAI_DU ="com.baidu.BaiduMap";
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private ZoneDetailBean zoneDetailBean;
    public SelectMapDialogActivity() {
        super(R.layout.activity_select_map);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent!=null){
            zoneDetailBean = intent.getParcelableExtra(TAG);
//            destination = intent.getStringExtra(TAG);
        }

    }

    @Override
    protected void initView() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
        initData();
    }

    private void initData() {
        ArrayList<String> list = new ArrayList<>();
        List<String> packageNames = Util.getPackageNames(this);
        for (String str:packageNames){
            if (TextUtils.equals(str,BAI_DU)){
                list.add(getString(R.string.baidu_map));
                continue;
            }

            if (TextUtils.equals(str,GAO_DE)){
                list.add(getString(R.string.gaode_map));
                continue;
            }
        }
        if (list.size()==0) ToastUtils.showInfo("请先安装百度或高德地图以使用导航功能");
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        TextAdapter adapter = new TextAdapter(activity,list);
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(getResources().getDimensionPixelSize(R.dimen.dp05));
        dividerLine.setColor(getResources().getColor(R.color.color_eee));
        recyclerView.addItemDecoration(dividerLine);

        adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view instanceof TextView){
                    CharSequence text = ((TextView) view).getText();
                    if (TextUtils.equals(text,getString(R.string.baidu_map))){
                        String from = zoneDetailBean.location.myLocation.longitude+","+zoneDetailBean.location.myLocation.latitude;
                        String to=zoneDetailBean.location.coordinates.get(1)+","+zoneDetailBean.location.coordinates.get(0);
                        //调用百度地图
                        String url="baidumap://map/direction?origin=latlng:"+from+"|name:我的位置&destination="+to+"|name:"+zoneDetailBean.title+"&mode=transit&sy=3&index=0&target=1";                        toExternalMapAPP(url);
                        toExternalMapAPP(url);
                    }

                    if (TextUtils.equals(text,getString(R.string.gaode_map))){
                        String from = zoneDetailBean.location.myLocation.longitude+","+zoneDetailBean.location.myLocation.latitude;
                        String to = zoneDetailBean.location.coordinates.get(0)+","+zoneDetailBean.location.coordinates.get(1);
                        //调用高德地图
                        String url="http://uri.amap.com/navigation?from="+from+",我的位置&to="+to+","+zoneDetailBean.title+"&coordinate=gaode&policy=1&src=mypage&callnative=1";
                        toExternalMapAPP(url);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    //跳转外部地图APP
    private void toExternalMapAPP(String url) {
        Intent i= new Intent();
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    static class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
        private Activity activity;
        private List<String> list;
        private boolean clickable = true;

        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(TextAdapter.OnItemClickListener itemClickListener) {
            this.mOnItemClickListener = itemClickListener;
        }

        public TextAdapter(Activity activity,List<String> list) {
            this.activity= activity;
            this.list = list;
        }

        @Override
        public TextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, activity.getResources().getDimensionPixelSize(R.dimen.dp45));
            TextView textView = new TextView(activity);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setTextColor(activity.getResources().getColor(R.color.color_222));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            return new TextAdapter.ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(final TextAdapter.ViewHolder holder, int position) {
            holder.mTextView.setText(list.get(position));
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickable) {
                            mOnItemClickListener.onItemClick(holder.mTextView, holder.getAdapterPosition());
                        }
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mOnItemClickListener.onItemLongClick(holder.mTextView, holder.getAdapterPosition());
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setClickable(boolean clickable) {
            this.clickable = clickable;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            public ViewHolder(TextView view){
                super(view);
                this.mTextView = view;
            }
        }
    }
}
