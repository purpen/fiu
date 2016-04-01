package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class CommonBaseAdapter<T> extends BaseAdapter {
	protected final String TAG=getClass().getSimpleName();
	protected ArrayList<T> list;
	protected Activity activity;
	public CommonBaseAdapter(ArrayList<T> list,Activity activity){
		this.list=list;
		this.activity=activity;
	}
	@Override
	public int getCount() {
		if (list==null){
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
