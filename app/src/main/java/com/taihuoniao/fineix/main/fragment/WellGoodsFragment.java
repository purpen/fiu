package com.taihuoniao.fineix.main.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinLabelRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.HotLabelBean;
import com.taihuoniao.fineix.beans.RandomImg;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WellGoodsFragment extends BaseFragment implements EditRecyclerAdapter.ItemClick {
    private AbsoluteLayout absoluteLayout;
    private RecyclerView labelRecycler;
    private RecyclerView recyclerView;
    private WaittingDialog dialog;
    //标签列表
    private List<HotLabelBean> hotLabelList;
    private PinLabelRecyclerAdapter pinLabelRecyclerAdapter;
    private int labelPage = 1;
    //品牌列表
    private List<CategoryListBean> list;
    private PinRecyclerAdapter pinRecyclerAdapter;
    private DisplayImageOptions options;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods, null);
        absoluteLayout = (AbsoluteLayout) view.findViewById(R.id.fragment_wellgoods_absolute);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MainApplication.getContext().getScreenWidth(), DensityUtils.dp2px(getActivity(), 157));
        absoluteLayout.setLayoutParams(lp);
        labelRecycler = (RecyclerView) view.findViewById(R.id.fragment_wellgoods_label_recycler);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_wellgoods_recycler);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.hotLabelList(labelPage + "", handler);
        DataPaser.categoryList(1 + "", 1 + "", handler);

    }

    @Override
    protected void initList() {
        hotLabelList = new ArrayList<>();
        labelRecycler.setHasFixedSize(true);
        labelRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        pinLabelRecyclerAdapter = new PinLabelRecyclerAdapter(getActivity(), hotLabelList, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Toast.makeText(getActivity(), "点击条目 = " + postion, Toast.LENGTH_SHORT).show();
            }
        });
        labelRecycler.addItemDecoration(new PinLabelRecyclerAdapter.LabelItemDecoration(getActivity()));
        labelRecycler.setAdapter(pinLabelRecyclerAdapter);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        pinRecyclerAdapter = new PinRecyclerAdapter(getActivity(), list, this);
        recyclerView.setAdapter(pinRecyclerAdapter);
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.HOT_LABEL_LIST:
                    dialog.dismiss();
                    HotLabel netHotLabel = (HotLabel) msg.obj;
                    if (netHotLabel.isSuccess()) {
                        hotLabelList.addAll(netHotLabel.getHotLabelBeanList());
                        pinLabelRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.CATEGORY_LIST:
                    dialog.dismiss();
                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
                    if (netCategoryBean.isSuccess()) {
                        list.addAll(netCategoryBean.getList());
                        pinRecyclerAdapter.notifyDataSetChanged();
                    }
                    addImgToAbsolute();
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void click(int postion) {
        Toast.makeText(getActivity(), "点击条目 = " + postion, Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据里应有图片地址，类型，id，
     * http://frbird.qiniudn.com/scene_product/160413/570de6b63ffca2e3108bc4b3-1-p500x500.jpg
     * http://frbird.qiniudn.com/scene_product/160412/570cbbb93ffca27d078bb969-1-p500x500.jpg
     * http://frbird.qiniudn.com/scene_product/160412/570cb2593ffca268098c293a-1-p500x500.jpg
     * 大 51dp 中 33dp 小 21dp
     */
    private void addImgToAbsolute() {
        Random random = new Random();
        List<RandomImg> randomImgs = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            RandomImg randomImg = new RandomImg();
//            if (i % 3 == 0) {
            randomImg.url = "http://frbird.qiniudn.com/scene_product/160413/570de6b63ffca2e3108bc4b3-1-p500x500.jpg";
//            } else if (i % 3 == 1) {
//                randomImg.url = "http://frbird.qiniudn.com/scene_product/160412/570cbbb93ffca27d078bb969-1-p500x500.jpg";
//            } else {
//                randomImg.url = "http://frbird.qiniudn.com/scene_product/160412/570cb2593ffca268098c293a-1-p500x500.jpg";
//            }
            randomImg.type = random.nextInt(3) + 1;
            if (randomImg.type == 1) {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 21) / 2;
            } else if (randomImg.type == 2) {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 33) / 2;
            } else {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 51) / 2;
            }
            randomImgs.add(randomImg);
        }
        int top = absoluteLayout.getTop();
//        Log.e("<<<", "top = " + top);
        int bottom = absoluteLayout.getBottom();
//        Log.e("<<<", "bottom = " + bottom);
        for (int i = 0; i < randomImgs.size(); i++) {
//            boolean isStop = false;
            RandomImg randomImg = randomImgs.get(i);

            whi:
//            while (!isStop) {
            for (int k = 0; k < 200; k++) {
                int x = random.nextInt(MainApplication.getContext().getScreenWidth());
//                Log.e("<<<", "x = " + x);
                int y = random.nextInt(bottom - top) + top;
//                Log.e("<<<", "y = " + y);
                if (MainApplication.getContext().getScreenWidth() - x < randomImg.radius || x < randomImg.radius ||
                        bottom - y < randomImg.radius || y - top < randomImg.radius) {
                    continue;
                }
//                Log.e("<<<", "radius = " + randomImg.radius);
//                Log.e("<<<", "右边距 = " + (MainApplication.getContext().getScreenWidth() - x) + ",下边距 = "
//                        + (bottom - y) + ",上边距 = " + (y - top));
                for (int j = 0; j < absoluteLayout.getChildCount(); j++) {
                    ImageView img1 = (ImageView) absoluteLayout.getChildAt(j);
                    RandomImg randomImg1 = (RandomImg) img1.getTag();
                    if (randomImg1 == null) {
                        continue;
                    }
                    if (Math.sqrt((randomImg1.x - x) * (randomImg1.x - x) + (randomImg1.y - y) * (randomImg1.y - y)) < randomImg1.radius + randomImg.radius) {
                        continue whi;
                    }
//                    Log.e("<<<", "点距离=" + Math.sqrt((randomImg1.x - x) * (randomImg1.x - x) + (randomImg.y - y) * (randomImg.y - y))
//                            + ",半径和=" + (randomImg1.radius + randomImg.radius));
                }
//                Log.e("<<<", "x = " + x + ",y = " + y);
                randomImg.x = x;
                randomImg.y = y;
//                Log.e("<<<", "img.x=" + randomImg.x);
//                Log.e("<<<", "img.y=" + randomImg.y);
                break;
            }
//            }
            if (randomImg.x == 0 && randomImg.y == 0) {
                continue;
            }
//            Log.e("<<<", "url =" + randomImgs.get(i).url + ",radius=" + randomImg.radius + ",imgx=" + randomImg.x + ",imgy=" + randomImg.y);
            ImageView img = new ImageView(getActivity());
            ImageLoader.getInstance().displayImage(randomImgs.get(i).url, img, options);
            img.setLayoutParams(new AbsoluteLayout.LayoutParams(randomImg.radius * 2, randomImg.radius * 2,
                    randomImg.x - randomImg.radius, randomImg.y - top - randomImg.radius));
//            Log.e("<<<", "图片参数=宽高：" + (randomImg.radius * 2) + ",x:" + randomImg.x + ",y:" + (randomImg.y - top));
            img.setTag(randomImg);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RandomImg randomImg1 = (RandomImg) v.getTag();
                    Toast.makeText(getActivity(), "又点击了", Toast.LENGTH_SHORT).show();
                }
            });
            absoluteLayout.addView(img);
        }
    }
}
