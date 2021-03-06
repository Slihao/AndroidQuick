package com.sdwfqin.quickseed.ui;

import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.BaseLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.blankj.utilcode.util.ConvertUtils;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.adapter.StrokeCardAdapter;
import com.sdwfqin.quickseed.adapter.StrokeHistoryAdapter;
import com.sdwfqin.quickseed.adapter.StrokeTitleAdapter;
import com.sdwfqin.quickseed.adapter.StrokeOrderAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * VLayoutSample
 * <p>
 *
 * @author 张钦
 * @date 2019-06-28
 */
public class VLayoutSampleActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    private VirtualLayoutManager mVirtualLayoutManager;
    private DelegateAdapter mDelegateAdapter;
    private LinkedList<DelegateAdapter.Adapter> mAdapters;
    private StrokeCardAdapter mStrokeCardAdapter;
    private StrokeHistoryAdapter mStrokeHistoryAdapter;
    private StrokeOrderAdapter mStrokeOrderAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_vlayout_sample;
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("VLayoutSample");
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(v -> finish());

        mVirtualLayoutManager = new VirtualLayoutManager(mContext);
        mDelegateAdapter = new DelegateAdapter(mVirtualLayoutManager, false);

        mRvList.setLayoutManager(mVirtualLayoutManager);
        mRvList.setAdapter(mDelegateAdapter);

        mAdapters = new LinkedList<>();

        initHeader();
        initHistoryTitle();
        initHistoryList();
        initOrderTitle();
        initOrderList();

        mDelegateAdapter.setAdapters(mAdapters);

        initData();
    }

    private void initHeader() {

        mVirtualLayoutManager.setLayoutViewFactory(ImageView::new);

        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        int dp15 = ConvertUtils.dp2px(15);
        linearLayoutHelper.setPadding(dp15, dp15, dp15, dp15);
        linearLayoutHelper.setDividerHeight(dp15);
        linearLayoutHelper.setBgColor(R.color.frame_gray_background_color);

        BaseLayoutHelper.DefaultLayoutViewHelper defaultLayoutViewHelper =
                new BaseLayoutHelper.DefaultLayoutViewHelper((layoutView, baseLayoutHelper) -> {
                    ImageView iv_bg = (ImageView) layoutView;
                    iv_bg.setImageResource(R.mipmap.ic_launcher);
                }, (layoutView, baseLayoutHelper) -> {

                });

        linearLayoutHelper.setLayoutViewHelper(defaultLayoutViewHelper);

        mStrokeCardAdapter = new StrokeCardAdapter(linearLayoutHelper, null);

        mStrokeCardAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        mAdapters.add(mStrokeCardAdapter);
    }

    private void initHistoryTitle() {

        List<String> title = new ArrayList<>();
        title.add("历史行程");

        StrokeTitleAdapter strokeTitleAdapter =
                new StrokeTitleAdapter(
                        new LinearLayoutHelper(),
                        title);

        mAdapters.add(strokeTitleAdapter);

    }

    private void initHistoryList() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        int dp15 = ConvertUtils.dp2px(15);
        linearLayoutHelper.setPadding(dp15, dp15, dp15, dp15);
        linearLayoutHelper.setDividerHeight(dp15);
        linearLayoutHelper.setBgColor(getResources().getColor(R.color.frame_gray_background_color));
        mStrokeHistoryAdapter = new StrokeHistoryAdapter(linearLayoutHelper, null);

        mAdapters.add(mStrokeHistoryAdapter);
    }

    private void initOrderTitle() {
        List<String> title = new ArrayList<>();
        title.add("推荐订单");

        StrokeTitleAdapter strokeTitleAdapter =
                new StrokeTitleAdapter(
                        new LinearLayoutHelper(),
                        title);

        mAdapters.add(strokeTitleAdapter);
    }

    private void initOrderList() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        mStrokeOrderAdapter = new StrokeOrderAdapter(linearLayoutHelper, null);

        mAdapters.add(mStrokeOrderAdapter);
    }

    private void initData() {

        List<String> todoTestBeans = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            todoTestBeans.add("" + i);
        }

        mStrokeCardAdapter.setNewData(todoTestBeans);
        mStrokeHistoryAdapter.setNewData(todoTestBeans);
        mStrokeOrderAdapter.setNewData(todoTestBeans);
    }
}
