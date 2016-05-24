package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/24.
 */
public class ShareCJActivity extends BaseActivity {
    //上个界面传递过来的场景id
    private String id;
    @Bind(R.id.activity_share_title)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_share_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.activity_share_relative)
    RelativeLayout relativeLayout;
    @Bind(R.id.activity_share_img)
    ImageView img;
    //网络请求对话框
    private WaittingDialog dialog;
    private DisplayImageOptions options750_1334, options500_500;

    public ShareCJActivity() {
        super(R.layout.activity_sharecj);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        if (id == null) {
            Toast.makeText(ShareCJActivity.this, "场景id为空", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(ShareCJActivity.this);
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.sceneDetails(id, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case DataConstants.SCENE_DETAILS:

                    break;
                case DataConstants.NET_FAIL:
                    Toast.makeText(ShareCJActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
