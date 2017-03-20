package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchUserAdapter;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.DataUserSearch;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ItemUserSearch;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/8/18 17:26
 */
public class SearchUserFragment extends DialogFragment {
    @Bind(R.id.ibtn)
    ImageButton ibtn;
    @Bind(R.id.search_view)
    EditText searchView;
    @Bind(R.id.pull_lv)
    PullToRefreshListView pullLv;
    private SearchUserAdapter adapter;
    private int curPage = 1;
    private ArrayList<ItemUserSearch> mList;
    private WaittingDialog dialog;
    private boolean isLoadMore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    public static SearchUserFragment newInstance() {
        SearchUserFragment f = new SearchUserFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        ButterKnife.bind(this, v);
        dialog = new WaittingDialog(getActivity());
        pullLv.setMode(PullToRefreshBase.Mode.DISABLED);
        mList = new ArrayList<>();
        loadData();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        installListeners();
    }

    private void installListeners() {
        pullLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList == null) return;
                Intent intent = new Intent(getActivity(), UserCenterActivity.class);
                ItemUserSearch item = mList.get(position - 1);
                intent.putExtra(FocusActivity.USER_ID_EXTRA, Long.valueOf(item.user_id));
                startActivity(intent);
            }
        });

        pullLv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                loadData();
            }
        });

        searchView.addTextChangedListener(tw);

        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    curPage = 1;
                    isLoadMore = false;
                    mList.clear();
                    loadData();
                }
                return false;
            }
        });
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            String keyWord = cs.toString().trim();
            if (!TextUtils.isEmpty(keyWord)) {
                ibtn.setVisibility(View.VISIBLE);
            } else {
                ibtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    private void loadData() {
        String query = searchView.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            ToastUtils.showInfo("请输入查询关键字");
            return;
        }
        HashMap<String, String> params =ClientDiscoverAPI.getsearchUserRequestParams(query, String.valueOf(curPage));
        HttpRequest.post(params, URL.SEARCH, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (dialog != null) dialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null) dialog.dismiss();
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<DataUserSearch> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<DataUserSearch>>() {
                });
                if (response.isSuccess()) {
                    ArrayList<ItemUserSearch> list = response.getData().rows;
                    refreshUI(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(error);
            }
        });
    }

    private void refreshUI(List list) {
        if (list == null) return;
        if (list.size() == 0) {
            if (isLoadMore) {
                ToastUtils.showInfo("没有更多数据哦");
            } else {
                ToastUtils.showInfo("抱歉,没有搜索到结果");
            }
            return;
        }
        curPage++;
        mList.addAll(list);
        if (adapter == null) {
            adapter = new SearchUserAdapter(mList, getActivity());
            pullLv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ibtn, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn:
                searchView.getText().clear();
                ibtn.setVisibility(View.GONE);
                break;
            case R.id.btn:
                dismiss();
                break;
        }
    }
}
