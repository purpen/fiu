package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.AddressData;
import com.taihuoniao.fineix.beans.AddressListBean;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.fragments.AddressSelectFragment;
import com.taihuoniao.fineix.utils.EmailUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListenerConfirmBack;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author lilin
 *         created at 2016/10/26 14:11
 */
public class AddAddressActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.et_consignee_name)
    EditText etConsigneeName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_zip_code)
    EditText etZipCode;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.et_address_details)
    EditText etAddressDetails;
    @Bind(R.id.ibtn_set_default)
    ImageButton ibtnSetDefault;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    //从选择地址界面传递过来的数据
    private AddressListBean.RowsEntity addressBean;
    private String provinceId = "0";
    private String cityId = "0";
    private String countyId = "0";
    private String townId = "0";
    private boolean isdefault = true;//设置此地址是否为默认地址
    private WaittingDialog dialog;
    private AddressSelectFragment addressSelectFragment;
    private AddressData.RowsEntity curProvince;
    private AddressData.RowsEntity curCity;
    private AddressData.RowsEntity curCounty;
    private AddressData.RowsEntity curTown;
    private boolean isAddressSelected;
    public AddAddressActivity() {
        super(R.layout.activity_add_address);
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
        dialog = new WaittingDialog(this);
        if (addressBean != null) {
            customHead.setHeadCenterTxtShow(true, R.string.edit_address);
            customHead.setHeadRightTxtShow(true, R.string.delete);
            customHead.getHeadRightTV().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DefaultDialog(AddAddressActivity.this, App.getString(R.string.hint_dialog_delete_address_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {

                        @Override
                        public void clickRight() {
                            HashMap<String, String> params = ClientDiscoverAPI.getdeleteAddressNetRequestParams(addressBean._id);
                            Call httpHandler = HttpRequest.post(params, URL.URLSTRING_DELETE_ADDRESS,new GlobalDataCallBack(){
                                @Override
                                public void onStart() {
                                    if (dialog != null && !activity.isFinishing()) dialog.show();
                                }

                                @Override
                                public void onSuccess(String json) {
                                    HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                                    if (netBean.isSuccess()) {
                                        ToastUtils.showSuccess("删除成功");
                                        Intent intent = new Intent();
                                        intent.putExtra("address", 1);
                                        setResult(DataConstants.RESULTCODE_ADDNEWADDRESS, intent);
                                        finish();
                                    } else {
                                        ToastUtils.showError(netBean.getMessage());
                                    }
                                }

                                @Override
                                public void onFailure(String error) {
                                    dialog.dismiss();
                                    ToastUtils.showError(R.string.network_err);
                                }
                            });
                            addNet(httpHandler);
                        }
                    });
                }
            });
            etConsigneeName.setText(addressBean.name);
            etPhone.setText(addressBean.phone);
            String builder = addressBean.province +
                    " " +
                    addressBean.city +
                    " " +
                    addressBean.county +
                    " " +
                    addressBean.town;
            tvAddress.setText(builder);
//            provinceId = addressBean.province_id;
//            cityId = addressBean.city_id;
//            townId = addressBean.town_id;
            etAddressDetails.setText(addressBean.address);
            etZipCode.setText(addressBean.zip);
            if (addressBean.is_default.equals("0")) {
                isdefault = false;
                ibtnSetDefault.setImageResource(R.mipmap.switch_off);
            } else {
                isdefault = true;
                ibtnSetDefault.setImageResource(R.mipmap.switch_on);
            }
        } else {
            customHead.setHeadCenterTxtShow(true, R.string.new_consignee_address);
        }
    }

    @Override
    protected void requestNet() {

    }

    @OnClick({R.id.tv_address, R.id.btn_commit, R.id.ibtn_set_default})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_address:
                if (addressSelectFragment == null) {
                    addressSelectFragment = new AddressSelectFragment();
                    addressSelectFragment.show(activity.getFragmentManager(), AddressSelectFragment.class.getSimpleName());
                    addressSelectFragment.setOnAddressChoosedListener(new AddressSelectFragment.OnAddressChoosedListener() {
                        @Override
                        public void onAddressChoosed(AddressData.RowsEntity province, AddressData.RowsEntity city, AddressData.RowsEntity county, AddressData.RowsEntity town) {
                            StringBuilder builder = new StringBuilder();
                            if (province != null) {
                                builder.append(province.name);
                                builder.append(" ");
                                curProvince = province;
                            }
                            if (city != null) {
                                builder.append(city.name);
                                builder.append(" ");
                                curCity = city;
                            }
                            if (county != null) {
                                builder.append(county.name);
                                builder.append(" ");
                                curCounty = county;
                            }
                            if (town != null) {
                                builder.append(town.name);
                                builder.append(" ");
                                curTown = town;
                            }
                            tvAddress.setText(builder.toString());
                            isAddressSelected=true;
                        }
                    });
                } else {
                    addressSelectFragment.show(activity.getFragmentManager(), AddressSelectFragment.class.getSimpleName());
                }
                break;
            case R.id.ibtn_set_default:
                if (!isdefault) {
                    ibtnSetDefault.setImageResource(R.mipmap.switch_on);
                    isdefault = true;
                } else {
                    ibtnSetDefault.setImageResource(R.mipmap.switch_off);
                    isdefault = false;
                }
                break;
            case R.id.btn_commit:
                commitConsigneeAddress();
                break;
            default:
                break;
        }
    }

    private void commitConsigneeAddress() {
        String consigneeName = etConsigneeName.getText().toString().trim();
        if (TextUtils.isEmpty(consigneeName)) {
            ToastUtils.showInfo(R.string.name_is_empty);
            return;
        }
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showInfo(R.string.phone_is_empty);
            return;
        }

        if (!EmailUtils.isMobileNO(etPhone.getText().toString())) {
            ToastUtils.showInfo(R.string.phone_is_err);
            return;
        }

        if (TextUtils.equals(tvAddress.getText(), getResources().getString(R.string.please_select_address))) {
            ToastUtils.showInfo(R.string.please_select_address);
            return;
        }

        String addressDetail = etAddressDetails.getText().toString().trim();
        if (TextUtils.isEmpty(addressDetail)) {
            ToastUtils.showInfo(R.string.address_details_is_empty);
            return;
        }

        if (isAddressSelected){
            if (curProvince != null) {
                provinceId = String.valueOf(curProvince.oid);
            } else {
                provinceId = "0";
            }
            if (curCity != null) {
                cityId = String.valueOf(curCity.oid);
            } else {
                cityId = "0";
            }
            if (curCounty != null) {
                countyId = String.valueOf(curCounty.oid);
            } else {
                countyId = "0";
            }
            if (curTown != null) {
                townId = String.valueOf(curTown.oid);
            }else {
                townId = "0";
            }
        }else {
            if (addressBean!=null){
                provinceId=addressBean.province_id;
                cityId=addressBean.city_id;
                countyId=addressBean.county_id;
                townId=addressBean.town_id;
            }else {
                ToastUtils.showError(R.string.network_err);
            }
        }
        String is_default;
        if (isdefault) {
            is_default = "1";
        } else {
            is_default = "0";
        }

        String id;
        if (addressBean == null) {
            id = "";
        } else {
            id = addressBean._id;
        }

        HashMap<String, String> params = ClientDiscoverAPI.getcommitAddressNetRequestParams(id, consigneeName, phone, provinceId, cityId, countyId, townId, addressDetail, etZipCode.getText().toString(), is_default);
        HttpRequest.post(params, URL.URLSTRING_NEW_ADDRESS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                LogUtil.e(TAG, json);
                dialog.dismiss();
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
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
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }
}
