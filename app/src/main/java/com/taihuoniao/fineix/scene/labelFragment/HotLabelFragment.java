package com.taihuoniao.fineix.scene.labelFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.UsedAndHotLabelAdapter;
import com.taihuoniao.fineix.beans.Label;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.view.GridViewForScrollView;

/**
 * Created by taihuoniao on 2016/5/17.
 */
public class HotLabelFragment extends Fragment implements AdapterView.OnItemClickListener {
    private int pos;
    private static Label label;

    public static HotLabelFragment newInstance(int position, Label label) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("label", label);
        HotLabelFragment fragment = new HotLabelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pos = getArguments().getInt("position", 0);
        label = (Label) getArguments().getSerializable("label");
        View view = View.inflate(getActivity(), R.layout.view_label_gridview, null);
        GridViewForScrollView gridView = (GridViewForScrollView) view.findViewById(R.id.view_label_gridview_grid);
        gridView.setAdapter(new UsedAndHotLabelAdapter(pos,label));
        gridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(DataConstants.BroadLabelActivity);
        if (label.getUsedLabelList().size() == 0) {
            intent.putExtra("flag", -1);
            intent.putExtra("label", label.getHotLabelList().get(position));
        } else {
            if (pos == 0) {
                intent.putExtra("flag", -2);
                intent.putExtra("label", label.getUsedLabelList().get(position));
            } else {
                intent.putExtra("flag", -1);
                intent.putExtra("label", label.getHotLabelList().get(position));
            }
        }
        getActivity().sendBroadcast(intent);
    }

}
