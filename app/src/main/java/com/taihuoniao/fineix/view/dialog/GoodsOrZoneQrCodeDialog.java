package com.taihuoniao.fineix.view.dialog;

import android.Manifest;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.QrCodeUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.zone.bean.ShareH5Url;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PHONE_STATE_CODE;

/**
 * @author lilin
 *         created at 2017/5/5 10:51
 */
public class GoodsOrZoneQrCodeDialog extends DialogFragment implements View.OnClickListener{
    @Bind(R.id.iv_qr_code)
    ImageView ivQrCode;
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.ibtn_close)
    ImageView ibtnClose;
    @Bind(R.id.tv_link)
    TextView tvLink;

    private ShareH5Url shareH5Url;
    private OnCommitClickListener listener;
    private Bitmap qrCode;
    public interface OnCommitClickListener {
        void execute(View v, EditText et);
    }

    public void setOnCommitClickListener(OnCommitClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_goods_zone_qrcode, container);
        if (getArguments()!=null){
            shareH5Url = getArguments().getParcelable(getClass().getSimpleName());
        }
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.execute(v, (EditText) view.findViewById(R.id.et_code));
                }
            }
        });
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.share_logo);
            if (null!=shareH5Url){
                qrCode = QrCodeUtils.create2DCode(logo,shareH5Url.o_url, 800, 800);
                ivQrCode.setImageBitmap(qrCode);
                tvLink.setText(shareH5Url.url);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        btn.setOnClickListener(this);
        ibtnClose.setOnClickListener(this);
        ivQrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (AndPermission.hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    savePic();
                } else {
                    AndPermission.with(getActivity()).requestCode(REQUEST_PHONE_STATE_CODE).permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).send();
                }

                return false;
            }
        });
    }

    private void savePic(){
        String packageName = getActivity().getPackageName();
        if (FileUtils.bitmapToFile(qrCode, FileUtils.getSavePath(packageName) + "/qr_code.png")) {
            ToastUtils.showSuccess("二维码已保存到" + packageName+ "文件夹下");
        } else {
            ToastUtils.showError("SD卡不可写，二维码保存失败");
        }
    }

    @PermissionYes(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusYes(List<String> grantedPermissions) {
        if (grantedPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            savePic();
        }

    }

    @PermissionNo(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn: //复制链接
                if (null==shareH5Url) return;
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("link",shareH5Url.url));
                ToastUtils.showInfo("已复制链接到剪切板");
                break;
            case R.id.ibtn_close:
                dismiss();
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (qrCode!=null) qrCode.recycle();
    }
}
