package com.taihuoniao.fineix.zone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.zone.ZoneEditCoverDialogActivity;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * @author lilin
 *         created at 2017/3/13 17:26
 */
public class ZoneBrowserCoverFragment extends DialogFragment {
    public static final int REQUEST_COVER_DIALOG = 100;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private ZoneDetailBean zoneDetailBean;
    private SimplePagerAdapter adapter;
    private OnFragmentInteractionListener mListener;
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener listener){
        this.mListener = listener;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        if (getArguments() != null) {
            zoneDetailBean = getArguments().getParcelable(ZoneBrowserCoverFragment.class.getSimpleName());
        }
    }

    public static ZoneBrowserCoverFragment newInstance(ZoneDetailBean zoneDetailBean) {
        ZoneBrowserCoverFragment f = new ZoneBrowserCoverFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ZoneBrowserCoverFragment.class.getSimpleName(), zoneDetailBean);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_zone_browser_cover, container, false);
        ButterKnife.bind(this, v);
        WindowUtils.chenjin(getActivity());
        adapter = new SimplePagerAdapter(zoneDetailBean.n_covers);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(zoneDetailBean.clickPosition);
        return v;
    }

    @OnClick({R.id.ibtn_menu,R.id.ibtn_close})
    void click(final View v){
        switch (v.getId()){
            case R.id.ibtn_menu:
                Intent intent = new Intent(getActivity(), ZoneEditCoverDialogActivity.class);
                intent.putExtra(ZoneEditCoverDialogActivity.class.getSimpleName(),zoneDetailBean);
                intent.putExtra(ZoneEditCoverDialogActivity.class.getName(),viewPager.getCurrentItem());
                startActivityForResult(intent,REQUEST_COVER_DIALOG);
                break;
            case R.id.ibtn_close:
                dismiss();
                break;
            default:
                break;
        }
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    static class SimplePagerAdapter extends PagerAdapter {
        private List<ZoneDetailBean.NcoverBean> mList;
        public SimplePagerAdapter(List<ZoneDetailBean.NcoverBean> list){
            this.mList = list;
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            GlideUtils.displayImageNoFading(mList.get(position).url,imageView);
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode){
            case REQUEST_COVER_DIALOG:
                int i = data.getIntExtra(ZoneEditCoverDialogActivity.class.getSimpleName(), -1);
                if (i<0) return;
                zoneDetailBean.n_covers.remove(i);
                adapter.notifyDataSetChanged();
                if (mListener!=null) mListener.onFragmentInteraction(i);
                break;
            default:
                break;
        }
    }
}
