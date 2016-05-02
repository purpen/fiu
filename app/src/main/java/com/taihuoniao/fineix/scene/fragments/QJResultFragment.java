package com.taihuoniao.fineix.scene.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ImgTxtItem;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.user.FocusFansActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.Util;

/**
 * @author lilin
 * created at 2016/4/25 18:38
 */
public class QJResultFragment extends MyBaseFragment {
    public QJResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_list);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initViews() {

    }


    private void setOnClickListener(View view, final ImgTxtItem item) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.imgId) {
                    case R.mipmap.taobao:
                        Util.makeToast(activity, "taobao");
                        break;
                    case R.mipmap.tmall:
                        Util.makeToast(activity, "tmall");
                        break;
                    case R.mipmap.jd:
                        Util.makeToast(activity, "jd");
                        break;
                    case R.mipmap.amzon:
                        Util.makeToast(activity, "amzon");
                        break;
                }
            }
        });
    }

//    @OnClick({R.id.item_about_us, R.id.item_feedback, R.id.item_partner, R.id.bt_register,R.id.ll_qj,R.id.ll_cj,R.id.ll_focus,R.id.ll_fans})
    protected void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.ll_qj:
                Util.makeToast("我创建的情景");
                //
                break;
            case R.id.ll_cj:
                Util.makeToast("我创建的场景");
                break;
            case R.id.ll_focus:
                intent= new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(),FocusFansActivity.FOCUS_TYPE);
                startActivity(intent);
                break;
            case R.id.ll_fans:
                intent= new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(),FocusFansActivity.FANS_TYPE);
                startActivity(intent);
                break;
            case R.id.iv_detail:
                Util.makeToast(activity, "个人详情");
                break;
            case R.id.iv_calendar:
                Util.makeToast(activity, "日历");
                break;
            case R.id.item_about_us:
                Util.makeToast(activity, "关于我们");
                break;
            case R.id.item_feedback:
                Util.makeToast(activity, "反馈");
                break;
            case R.id.item_partner:
                Util.makeToast(activity, "合作伙伴");
                break;
            case R.id.bt_register:
                intent = new Intent(getActivity(),
                        OptRegisterLoginActivity.class);

                startActivity(intent);
                break;

        }
    }

    @Override
    protected void installListener() {

    }
}
