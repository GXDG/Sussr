package com.example.hzg.mysussr.features.config;

import android.content.Context;

import com.example.hzg.mysussr.R;
import com.example.hzg.mysussr.base.BaseAdapter;
import com.example.hzg.mysussr.databinding.ItemConfigBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hzg on 2018/4/6.
 */

public class ConfigAdapter extends BaseAdapter<ConfigAdapter.Data> {

    public ConfigAdapter(@NotNull Context context, @NotNull List<Data> mData) {
        super(context, mData);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_config;
    }

    @Override
    protected void convert(@NotNull BaseVH holder, int position, Data bean) {
        ItemConfigBinding mBinding = holder.getDataBinding();
        mBinding.tvHeader.setText(bean.data.getKey());
        mBinding.tvContent.setText(bean.data.getValue());
    }

 public static   class Data {
        public Data(KeyBean data) {
            this.data = data;
        }

        public KeyBean data;
    }
}
