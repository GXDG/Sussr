package com.example.hzg.mysussr.features.uid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hzg.mysussr.R;
import com.example.hzg.mysussr.base.BaseFragment;
import com.example.hzg.mysussr.features.uid.bean.AppUidBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hzg on 2018/4/4 10:28
 * mail:1039766856@qq.com
 * Sussr
 */

public class AppUidFragment extends BaseFragment {
    private String type;
    UidAdapter adapter;

    public static AppUidFragment newInstance(String type, String content) {
        AppUidFragment fragment = new AppUidFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("content", content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_app_uid;
    }

    @Override
    protected void initView() {

        final ArrayList<AppUidBean> dataList = new ArrayList<>();
        ArrayList<String> selectList = new ArrayList<>();
        if (getArguments() != null) {
            type = getArguments().getString("type");
            String content = getArguments().getString("content");
            if (content != null) {
                selectList.addAll(Arrays.asList(content.split(",")));
            }
        }
        adapter = new UidAdapter(mContext, dataList, selectList);
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
                dataList.clear();
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

    public String getUidString() {
        return adapter.getSelectUidSting();
    }
}
