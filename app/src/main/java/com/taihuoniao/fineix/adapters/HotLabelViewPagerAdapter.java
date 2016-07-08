package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.AdapterView;

import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.Label;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.scene.labelFragment.HotLabelFragment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/8.
 */
public class HotLabelViewPagerAdapter extends FragmentPagerAdapter implements AdapterView.OnItemClickListener {
    private Context context;
    private List<UsedLabelBean> usedLabelList;
    private List<HotLabel.HotLabelBean> hotLabelList;
    private LabelClick labelClick;
    //由于fragment不能传递list数据，所以创建一个javabean把list封装进去再传递
    private Label label;

    public HotLabelViewPagerAdapter(FragmentManager fm, Context context, List<UsedLabelBean> usedLabelList, List<HotLabel.HotLabelBean> hotLabelList, LabelClick labelClick) {
        super(fm);
        this.context = context;
        this.usedLabelList = usedLabelList;
        this.hotLabelList = hotLabelList;
        this.labelClick = labelClick;
        label = new Label(hotLabelList, usedLabelList);
    }

    @Override
    public int getCount() {
        if (usedLabelList.size() == 0) {
            return 1;
        }
        return 2;
    }


    @Override
    public Fragment getItem(int position) {
        return HotLabelFragment.newInstance(position, label);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        labelClick.click(parent.getAdapter().getItem(position));
    }

    public interface LabelClick extends Serializable{
        void click(Object object);
    }
}
