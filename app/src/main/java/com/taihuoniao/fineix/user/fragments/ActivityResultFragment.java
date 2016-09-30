package com.taihuoniao.fineix.user.fragments;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
//public class ActivityResultFragment extends MyBaseFragment {
//    @Bind(R.id.pull_lv)
//    PullToRefreshListView pullLv;
//    private ArrayList<ActivityPrizeData.SightsBean> mList;
//    private ActivityResultAdapter adapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            mList = bundle.getParcelableArrayList("list");
//        }
//        super.onCreate(savedInstanceState);
//    }
//
//    public static ActivityResultFragment newInstance(ArrayList<ActivityPrizeData.SightsBean> list) {
//        ActivityResultFragment fragment = new ActivityResultFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("list", list);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater,
//                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.setFragmentLayout(R.layout.fragment_article);
//        super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, view);
//        return view;
//    }
//
//    @Override
//    protected void initViews() {
//        pullLv.setMode(PullToRefreshBase.Mode.DISABLED);
//    }
//
//    @Override
//    protected void installListener() {
//
//    }
//
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        if (mList == null || mList.size() == 0) return;
//        adapter = new ActivityResultAdapter(mList, activity);
//        pullLv.setAdapter(adapter);
//    }
//}
