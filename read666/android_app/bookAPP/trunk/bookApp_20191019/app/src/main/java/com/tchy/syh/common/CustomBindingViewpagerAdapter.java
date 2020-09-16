package com.tchy.syh.common;

import android.view.ViewGroup;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class CustomBindingViewpagerAdapter<T> extends BindingViewPagerAdapter <T>{
    List items;
    public CustomBindingViewpagerAdapter(List items, ItemBinding itemBinding,PageTitles pageTitles){
        this.items=items;
        this.setItems(items);
        this.setItemBinding(itemBinding);
        this.setPageTitles(pageTitles);

    }
    @Override
    public int getCount() {
        return 10000;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position % items.size());
    }




}
