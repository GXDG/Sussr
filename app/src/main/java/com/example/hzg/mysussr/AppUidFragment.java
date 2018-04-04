package com.example.hzg.mysussr;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzg on 2018/4/4 10:28
 * mail:1039766856@qq.com
 * Sussr
 */

public class AppUidFragment extends BaseFragment {
    @Override
    int getLayoutId() {
        return R.layout.fragment_app_uid;
    }

    @Override
    protected void initView() {
        final ArrayList<AppUidBean> dataList = new ArrayList<>();
        ArrayList<String> selectList = new ArrayList<>();
        final UidAdapter adapter = new UidAdapter(mContext, dataList, selectList);
        RecyclerView recyclerView = mView.findViewById(R.id.rv_content);
        final SwipeRefreshLayout refreshLayout = mView.findViewById(R.id.refresh_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        UidViewModel viewModel = ViewModelProviders.of(getActivity()).get(UidViewModel.class);
        viewModel.getUidDataList().observe(this, new Observer<List<AppUidBean>>() {
            @Override
            public void onChanged(@Nullable List<AppUidBean> appUidBeans) {
                Log.d("onChanged", "数据更新");
                dataList.addAll(appUidBeans);
                adapter.notifyDataSetChanged();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.refresh();
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
