package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.ExpandAdapter;
import com.taihuoniao.fineix.adapters.UsedLabelAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.ActiveTagsBean;
import com.taihuoniao.fineix.beans.SearchExpandBean;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/8/17.
 */
public class AddLabelActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, EditRecyclerAdapter.ItemClick {
    //上个界面传递过来的活动标签
    private ActiveTagsBean activeTagsBean;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.edit_text)
    EditText editText;
    @Bind(R.id.delete_label)
    ImageView deleteLabel;
    //    @Bind(R.id.relative)
//    RelativeLayout relative;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private WaittingDialog dialog;

    private List<String> usedLabelList;//用过的标签
    private UsedLabelAdapter usedLabelAdapter;
    private List<String> expandList;//模糊搜索标签
    private ExpandAdapter expandAdapter;

    @Override
    protected void getIntentData() {
        activeTagsBean = (ActiveTagsBean) getIntent().getSerializableExtra(CreateQJActivity.class.getSimpleName());
    }

    public AddLabelActivity() {
        super(R.layout.activity_add_label);
    }

    @Override
    protected void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        expandList = new ArrayList<>();
        expandAdapter = new ExpandAdapter(expandList, this);
        recyclerView.setAdapter(expandAdapter);
        dialog = new WaittingDialog(this);
        titleLayout.setTitle(R.string.add_label, getResources().getColor(R.color.white));
//        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.white), this);
        titleLayout.setContinueTvVisible(false);
        deleteLabel.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteLabel.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchExpand(s.toString());
                } else {
                    deleteLabel.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6) {
                    if (TextUtils.isEmpty(editText.getText())) {
                        finish();
                        return false;
                    }
                    String str = editText.getText().toString();
                    if (!editText.getText().toString().startsWith("#")) {
                        str = "#" + str;
                    }
                    if (!editText.getText().toString().endsWith(" ")) {
                        str = str + " ";
                    }
                    Intent intent = new Intent();
                    intent.putExtra(AddLabelActivity.class.getSimpleName(), str);
                    setResult(1, intent);
                    finish();
                }
                return false;
            }
        });
        usedLabelList = new ArrayList<>();
        usedLabelAdapter = new UsedLabelAdapter(activeTagsBean, usedLabelList);
        listView.setAdapter(usedLabelAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        HashMap<String, String> params = ClientDiscoverAPI.getusedLabelListRequestParams();
        Call httpHandler = HttpRequest.post(params, URL.USED_LABEL_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                HttpResponse<UsedLabelBean> usedLabelBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<UsedLabelBean>>(){});
                if (usedLabelBean.isSuccess()) {
                    usedLabelList.clear();
                    usedLabelList.addAll(usedLabelBean.getData().getTags());
                    usedLabelAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_label:
                editText.setText("");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (usedLabelAdapter.getItemViewType(position) == 1) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(AddLabelActivity.class.getSimpleName(), usedLabelAdapter.getItem(position));
        setResult(1, intent);
        finish();
    }

    private void searchExpand(String str) {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsearchExpandRequestParams(str, 20 + "");
       Call httpHandler =  HttpRequest.post(requestParams, URL.SEARCH_EXPANDED, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<SearchExpandBean> searchExpandBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SearchExpandBean>>() { });
                if (searchExpandBean.isSuccess()) {
                    expandList.clear();
                    expandList.addAll(searchExpandBean.getData().getData().getSwords());
                    expandAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
        addNet(httpHandler);
    }

    @Override
    public void click(int postion) {
        Intent intent = new Intent();
        intent.putExtra(AddLabelActivity.class.getSimpleName(), "#" + expandList.get(postion) + " ");
        setResult(1, intent);
        finish();
    }
}
