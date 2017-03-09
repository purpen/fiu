package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 地盘的基本信息
 */
public class ZoneBaseInfoActivity extends BaseActivity {
    private static final int REQUEST_MODIFY_AVATAR = 99;
    private static final int REQUEST_MODIFY_TITLE = 100;
    private static final int REQUEST_MODIFY_SUBTITLE = 101;
    private static final int REQUEST_ZONE_CATEGORY = 102;
    private static final int REQUEST_ZONE_TAGS = 103;
    @Bind(R.id.item_zone_avatar)
    CustomItemLayout itemZoneAvatar;
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.item_zone_title)
    CustomItemLayout itemZoneTitle;
    @Bind(R.id.item_sub_title)
    CustomItemLayout itemSubTitle;
    @Bind(R.id.item_zone_category)
    CustomItemLayout itemZoneCategory;
    @Bind(R.id.item_zone_tags)
    CustomItemLayout itemZoneTags;
    private ZoneDetailBean zoneDetailBean;

    public ZoneBaseInfoActivity() {
        super(R.layout.activity_zone_base_info);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            zoneDetailBean = intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, R.string.title_zone_info);
        itemZoneAvatar.setTVStyle(0, R.string.zone_avatar, R.color.color_666);
        itemZoneAvatar.setUserAvatar(null);
        itemZoneTitle.setTVStyle(0, R.string.zone_name, R.color.color_666);
        itemSubTitle.setTVStyle(0, R.string.zone_subtitle, R.color.color_666);
        itemZoneCategory.setTVStyle(0, R.string.zone_category, R.color.color_666);
        itemZoneTags.setTVStyle(0, R.string.zone_tags, R.color.color_666);
        if (zoneDetailBean != null) {
            GlideUtils.displayImage(zoneDetailBean.avatar_url, itemZoneAvatar.getAvatarIV());
            itemZoneCategory.setTvArrowLeftStyle(true, zoneDetailBean.category.title, R.color.color_333);
            itemSubTitle.setTvArrowLeftStyle(true, zoneDetailBean.sub_title, R.color.color_333);
            itemZoneTitle.setTvArrowLeftStyle(true, zoneDetailBean.title, R.color.color_333);
        }
    }

    @OnClick({R.id.item_zone_avatar, R.id.item_zone_title, R.id.item_sub_title, R.id.item_zone_category, R.id.item_zone_tags})
    void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.item_zone_avatar: //地盘头像

                break;
            case R.id.item_zone_title: //地盘标题
                if (zoneDetailBean == null) return;
                intent = new Intent(activity, ZoneEditTitleActivity.class);
                intent.putExtra(ZoneEditTitleActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent, REQUEST_MODIFY_TITLE);
                break;
            case R.id.item_sub_title: //地盘副标题
                if (zoneDetailBean == null) return;
                intent = new Intent(activity, ZoneEditSubtitleActivity.class);
                intent.putExtra(ZoneEditSubtitleActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent, REQUEST_MODIFY_SUBTITLE);
                break;
            case R.id.item_zone_category: //地盘分类
                if (zoneDetailBean == null) return;
                intent = new Intent(activity, ZoneClassifyActivity.class);
                intent.putExtra(ZoneClassifyActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent, REQUEST_ZONE_CATEGORY);
                break;
            case R.id.item_zone_tags: //地盘标签

                break;
            default:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_MODIFY_AVATAR: //修改头像

                break;
            case REQUEST_MODIFY_TITLE: //修改标题
                String zoneName = data.getStringExtra(ZoneEditTitleActivity.class.getSimpleName());
                if (TextUtils.isEmpty(zoneName)) return;
                itemZoneTitle.setTvArrowLeftStyle(true, zoneName, R.color.color_333);
                break;
            case REQUEST_MODIFY_SUBTITLE: //修改副标题
                zoneDetailBean = data.getParcelableExtra(ZoneEditSubtitleActivity.class.getSimpleName());
                if (zoneDetailBean==null) return;
                itemSubTitle.setTvArrowLeftStyle(true,zoneDetailBean.sub_title, R.color.color_333);
                break;
            case REQUEST_ZONE_CATEGORY: //修改地盘分类
                zoneDetailBean = data.getParcelableExtra(ZoneClassifyActivity.class.getSimpleName());
                if (zoneDetailBean == null) return;
                itemZoneCategory.setTvArrowLeftStyle(true, zoneDetailBean.category.title, R.color.color_333);
                break;
            case REQUEST_ZONE_TAGS:  //地盘标签

                break;
            default:
                break;
        }
    }
}
