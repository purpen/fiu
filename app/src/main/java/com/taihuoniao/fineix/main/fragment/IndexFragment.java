package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.map.BDSearchAddressActivity;
import com.taihuoniao.fineix.map.GeoCoderDemo;
import com.taihuoniao.fineix.map.HotCitiesActivity;
import com.taihuoniao.fineix.map.POIListActivity;
import com.taihuoniao.fineix.order.OrderListActivity;

import butterknife.OnClick;

public class IndexFragment extends BaseFragment {
    public IndexFragment() {
        // Required empty public constructor
    }
    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_index, null);
        return view;
    }

    @OnClick({R.id.location_btn, R.id.poi_btn,R.id.share_btn,R.id.sliding_tab_btn,R.id.geo})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_btn:
                activity.startActivity(new Intent(activity, HotCitiesActivity.class));
                break;
            case R.id.poi_btn:
                activity.startActivity(new Intent(activity, POIListActivity.class));
                break;
            case R.id.share_btn:
                //TODO 调用所有分享
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(intent.EXTRA_TEXT,"我是分享内容。。。。http://www.baidu.com/");
//                intent.setType("*/*");
//                startActivity(Intent.createChooser(intent, "分享到"));

                //TODO 通过短信分享
                String content="我是分享内容....";
                Uri sms = Uri.parse("smsto:");
                Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, sms);
                //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
                sendIntent.putExtra( "sms_body",content);
                sendIntent.setType("vnd.android-dir/mms-sms" );
                startActivity(sendIntent);
                break;
            case R.id.sliding_tab_btn:
                activity.startActivity(new Intent(activity, OrderListActivity.class));
                break;
            case R.id.geo:
                activity.startActivity(new Intent(activity, BDSearchAddressActivity.class));
                break;
        }
    }

    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
