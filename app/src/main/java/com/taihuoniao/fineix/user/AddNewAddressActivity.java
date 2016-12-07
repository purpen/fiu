package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.NetBean;
import com.taihuoniao.fineix.beans.AddressListBean;
import com.taihuoniao.fineix.beans.CityBean;
import com.taihuoniao.fineix.beans.ProvinceBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.wheelview.ArrayWheelAdapter;
import com.taihuoniao.fineix.view.wheelview.OnWheelChangedListener;
import com.taihuoniao.fineix.view.wheelview.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/2/2.
 */
public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener, OnWheelChangedListener {
//    @Bind(R.id.head_view)
//    CustomHeadView headView;
    @Bind(R.id.activity_addnewaddress_commitbtn)
    Button commitBtn;
    @Bind(R.id.activity_addnewaddress_name)
    EditText nameEdt;
    @Bind(R.id.activity_addnewaddress_phone)
    EditText phoneEdt;
    @Bind(R.id.activity_addnewaddress_postcode)
    EditText postcodeEdt;
    @Bind(R.id.activity_addnewaddress_province)
    TextView provinceTv;
    @Bind(R.id.activity_addnewaddress_city)
    TextView cityTv;
    @Bind(R.id.activity_addnewaddress_addresslinear)
    LinearLayout addressLinear;
    @Bind(R.id.activity_addnewaddress_addressdetails)
    EditText detailsAddressEdt;
    @Bind(R.id.activity_addnewaddress_isdefault)
    ImageView isDefaultImg;
    //从选择地址界面传递过来的数据
    private AddressListBean.RowsEntity addressBean;
    //界面下控件
    private View activity_view;
    private String provinceName;//选择结束后展示出来的省名称
    private List<ProvinceBean> provinceList;
    private String[] provinceNames;//存放省名称的容器
    protected Map<String, String[]> provinceCities = new HashMap<>();
    //popupwindow下的控件
    private PopupWindow popupWindow;
    private WheelView provinceView;
    private WheelView cityView;
    private String provinceId = "0";
    private String cityId = "0";
    private String countyId = "0";
    private String townId = "0";
    private boolean isdefault = true;//设置此地址是否为默认地址
    private WaittingDialog dialog;

    public AddNewAddressActivity() {
        super(R.layout.activity_addnewaddress);
    }
    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("addressBean")) {
            addressBean = (AddressListBean.RowsEntity) intent.getSerializableExtra("addressBean");
        }
    }
    @Override
    protected void initView() {
//        if (addressBean != null) {
//            headView.setHeadCenterTxtShow(true,R.string.edit_address);
//        } else {
//            headView.setHeadCenterTxtShow(true,R.string.new_consignee_address);
//        }
        dialog = new WaittingDialog(this);
        provinceList = new ArrayList<>();
    }

    @Override
    protected void requestNet() {
        getProvinceData();
    }

    @Override
    protected void installListener() {
        addressLinear.setOnClickListener(this);
        isDefaultImg.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
//        if (addressBean != null) {
//            headView.getHeadRightTV().setText(R.string.delete);
//            headView.getHeadRightTV().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNewAddressActivity.this);
//                    builder.setMessage("确认删除此收货地址吗？");
//                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            if (!AddNewAddressActivity.this.dialog.isShowing()) {
//                                AddNewAddressActivity.this.dialog.show();
//                            }
//                            HttpHandler<String> httpHandler = ClientDiscoverAPI.deleteAddressNet(addressBean.get_id(), new RequestCallBack<String>() {
//                                @Override
//                                public void onSuccess(ResponseInfo<String> responseInfo) {
//                                    AddNewAddressActivity.this.dialog.dismiss();
//                                    NetBean netBean = new NetBean();
//                                    try {
//                                        Gson gson = new Gson();
//                                        Type type = new TypeToken<NetBean>() {
//                                        }.getType();
//                                        netBean = gson.fromJson(responseInfo.result, type);
//                                    } catch (JsonSyntaxException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (netBean.isSuccess()) {
//                                        ToastUtils.showSuccess("删除成功");
//                                        Intent intent = new Intent();
//                                        intent.putExtra("address", 1);
//                                        setResult(DataConstants.RESULTCODE_ADDNEWADDRESS, intent);
//                                        finish();
//                                    } else {
//                                        ToastUtils.showError(netBean.getMessage());
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(HttpException error, String msg) {
//                                    AddNewAddressActivity.this.dialog.dismiss();
//                                    ToastUtils.showError("网络错误");
//                                }
//                            });
//                            addNet(httpHandler);
//                        }
//                    });
//                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.create().show();
//                }
//            });
//            nameEdt.setText(addressBean.getName());
//            phoneEdt.setText(addressBean.getPhone());
//            provinceTv.setText(addressBean.getProvince_name());
//            provinceId = addressBean.getProvince();
//            cityId = addressBean.getCity();
//            cityTv.setText(addressBean.getCity_name());
//
//            detailsAddressEdt.setText(addressBean.getAddress());
//            postcodeEdt.setText(addressBean.getZip());
//            if (addressBean.getIs_default().equals("0")) {
//                isdefault = false;
//                isDefaultImg.setImageResource(R.mipmap.switch_off);
//            } else {
//                isdefault = true;
//                isDefaultImg.setImageResource(R.mipmap.switch_on);
//            }
//        }
    }

    private void getProvinceData() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        HttpHandler<String> httpHandler = ClientDiscoverAPI.getProvinceList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                List<ProvinceBean> list = new ArrayList<>();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    JSONObject data = job.getJSONObject("data");
                    JSONArray rows = data.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject pro = rows.getJSONObject(i);
                        ProvinceBean provinceBean = new ProvinceBean();
                        provinceBean.set_id(pro.optString("_id"));
                        provinceBean.setName(pro.optString("city"));
                        JSONArray cities = pro.getJSONArray("cities");
                        List<CityBean> cityBeanList = new ArrayList<CityBean>();
                        for (int j = 0; j < cities.length(); j++) {
                            JSONObject city = cities.getJSONObject(j);
                            CityBean cityBean = new CityBean();
                            cityBean.set_id(city.optString("_id"));
                            cityBean.setName(city.optString("city"));
                            cityBean.setParent_id(city.optString("parent_id"));
                            cityBeanList.add(cityBean);
                        }
                        provinceBean.setCityList(cityBeanList);
                        list.add(provinceBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                provinceList.clear();
                provinceList.addAll(list);
                initPopupWindow();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
        addNet(httpHandler);
    }


    private void initPopupWindow() {
        WindowManager windowManager = AddNewAddressActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(AddNewAddressActivity.this, R.layout.address_popup, null);
        provinceView = (WheelView) popup_view.findViewById(R.id.id_province);
        cityView = (WheelView) popup_view.findViewById(R.id.id_city);
        Button confirmBtn = (Button) popup_view.findViewById(R.id.btn_confirm);
        provinceView.addChangingListener(this);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (provinceList.get(provinceView.getCurrentItem()).getCityList().size() != 0) {
                    cityTv.setText(provinceList.get(provinceView.getCurrentItem()).getCityList().get(cityView.getCurrentItem()).getName());
                    cityId = provinceList.get(provinceView.getCurrentItem()).getCityList().get(cityView.getCurrentItem()).get_id();
                } else {
                    cityTv.setText("");
                    cityId = "0";
                }
                provinceTv.setText(provinceList.get(provinceView.getCurrentItem()).getName());
                provinceId = provinceList.get(provinceView.getCurrentItem()).get_id();
                popupWindow.dismiss();
            }
        });

        initProvinceDatas();
        provinceView.setAdapter(new ArrayWheelAdapter<>(provinceNames, provinceNames.length));

        // 设置可见条目数量
        provinceView.setVisibleItems(7);
        cityView.setVisibleItems(7);
        updateCities();
        popupWindow = new PopupWindow(popup_view, display.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(AddNewAddressActivity.this,
                R.color.white));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    private void showPopup() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(activity_view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_addnewaddress_addresslinear:
                if (provinceList.size() == 0) {
                    getProvinceData();
                    return;
                }
                showPopup();
                break;
            case R.id.activity_addnewaddress_commitbtn:
                commitAddress();
                break;
            case R.id.activity_addnewaddress_isdefault:
                if (!isdefault) {
                    isDefaultImg.setImageResource(R.mipmap.switch_on);
                    isdefault = true;
                } else {
                    isDefaultImg.setImageResource(R.mipmap.switch_off);
                    isdefault = false;
                }
                break;
        }
    }

    private void commitAddress() {
        if (provinceList.size() == 0) {
            getProvinceData();
            return;
        }
        if (nameEdt.getText().toString().isEmpty()) {
            ToastUtils.showError(R.string.name_is_empty);
            return;
        }
        if (phoneEdt.getText().toString().isEmpty()) {
            ToastUtils.showError(R.string.phone_is_empty);
            return;
        }
        if (detailsAddressEdt.getText().toString().isEmpty()) {
            ToastUtils.showError(R.string.address_details_is_empty);
            return;
        }
        if (postcodeEdt.getText().toString().isEmpty()) {
            ToastUtils.showError(R.string.zip_is_empty);
            return;
        }
        cityId = provinceList.get(provinceView.getCurrentItem()).getCityList().get(cityView.getCurrentItem()).get_id();
        provinceId = provinceList.get(provinceView.getCurrentItem()).get_id();
        //接口 25
        if (!dialog.isShowing()) {
            dialog.show();
        }
        HttpHandler<String> httpHandler = ClientDiscoverAPI.commitAddressNet(addressBean == null ? null : addressBean._id, nameEdt.getText().toString(), phoneEdt.getText().toString(), provinceId, cityId, countyId, townId, detailsAddressEdt.getText().toString(), postcodeEdt.getText().toString(), isdefault ? "1" : "0", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess(netBean.getMessage());
                    Intent intent = new Intent();
                    intent.putExtra("address", 1);
                    setResult(DataConstants.RESULTCODE_ADDNEWADDRESS, intent);
                    finish();
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == provinceView) {
            updateCities();
        }
    }

    //解析省市数据
    private void initProvinceDatas() {
        //*/ 初始化默认选中的省、市、区
        if (provinceList != null && !provinceList.isEmpty()) {
            provinceName = provinceList.get(0).getName();
            List<CityBean> cityList = provinceList.get(0).getCityList();
            if (cityList != null && !cityList.isEmpty()) {
                String cityName = cityList.get(0).getName();
            }
        }
        //*/
        provinceNames = new String[provinceList != null ? provinceList.size() : 0];
        for (int i = 0; i < provinceList.size(); i++) {
            // 遍历所有省的数据
            provinceNames[i] = provinceList.get(i).getName();
            List<CityBean> cityList = provinceList.get(i).getCityList();
            String[] cityNames = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                // 遍历省下面的所有市的数据
                cityNames[j] = cityList.get(j).getName();
            }
            // 省-市的数据，保存到mCitisDatasMap
            provinceCities.put(provinceList.get(i).getName(), cityNames);
        }
    }


    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int currentItem = provinceView.getCurrentItem();
        provinceName = provinceNames[currentItem];
        String[] cities = provinceCities.get(provinceName);

        cityView.setAdapter(new ArrayWheelAdapter<>(cities, cities.length));
//        if (cities == null)
//            DataParser.getCityListParser(provinceList.get(currentItem).get_id(), provinceList);
//        else
        cityView.setCurrentItem(0);
    }


}
