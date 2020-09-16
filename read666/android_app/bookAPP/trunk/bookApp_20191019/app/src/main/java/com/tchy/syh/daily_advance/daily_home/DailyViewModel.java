package com.tchy.syh.daily_advance.daily_home;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.os.Bundle;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.daily_advance.daily_list.DailyListItemViewModel;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class DailyViewModel extends BaseViewModel {
    //
    public ObservableBoolean isInitFinish = new ObservableBoolean(false);
    public ObservableInt pagerIndex = new ObservableInt(0);
    public ItemBinding<DailyListItemViewModel> dailyListPagerBinding = ItemBinding.of(BR.vm, R.layout.daily_list_pager_item_frag);
    public List<LearnCate.DataBean> cates=new ArrayList<>();
    public ObservableList<DailyListItemViewModel> dailyList = new ObservableArrayList<>();
    public final BindingViewPagerAdapter.PageTitles<DailyListItemViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<DailyListItemViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, DailyListItemViewModel item) {
            return cates.get(position).getName();
        }
    };
    public DailyViewModel(Context context ) {
        super(context);
        LearnCate.DataBean bean = new LearnCate.DataBean("1","热榜");
        cates.add(bean);
        LearnCate.DataBean bean1 = new LearnCate.DataBean("1","最新");
        cates.add(bean1);
        LearnCate.DataBean bean2 = new LearnCate.DataBean("1","关注");
        cates.add(bean2);

    }
    public void setCates(){
        for (int i = 0; i <this.cates.size() ; i++) {
            LearnCate.DataBean bean= this.cates.get(i);
            DailyListItemViewModel dailyListItemViewModel=new DailyListItemViewModel(context,i,bean.getId());
            dailyList.add(dailyListItemViewModel);
            dailyListItemViewModel.onCreate();

        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        isInitFinish.set(true);
        setCates();

        setCurrentPage();
    }

    public BindingCommand onAddDaily = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            startContainerActivity(AddDailyAdvanceFregment.class.getCanonicalName(),bundle);
        }
    });
//

    private String id="";
    public void setCurrentPage(){
        if(this.id!=null&&this.id.length()>0){
            for (int i = 0; i <cates.size() ; i++) {
                LearnCate.DataBean bean= cates.get(i);
                if(id.equals(bean.getId())){
                    pagerIndex.set(i);
                }

            }
        }else{
            pagerIndex.set(0);
        }

    }


}
