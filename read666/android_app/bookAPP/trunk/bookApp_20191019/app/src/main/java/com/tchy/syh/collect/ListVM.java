package com.tchy.syh.collect;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.entity.LearnCate;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;


public class ListVM extends BaseViewModel {
    public ObservableBoolean isInitFinish=new ObservableBoolean(false);
    public ObservableInt notifyNum = new ObservableInt();
    private int itemIndex = 0;
    public ObservableField<String> search = new ObservableField<>("");
    public ObservableInt pagerIndex = new ObservableInt(0);

    public ObservableList<BaseViewModel> pageList = new ObservableArrayList<>();
    public ItemBinding<BaseViewModel> listPageBinding = ItemBinding.of(new OnItemBind<BaseViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, BaseViewModel item) {
            if(item instanceof VideoPageItemVM){
                itemBinding.set(BR.vm, R.layout.collect_video_list_pager_item_frag);

            }else if(item instanceof AudioPageItemVM){
                itemBinding.set(BR.vm, R.layout.collect_audio_list_pager_item_frag);

            }else if(item instanceof ArticlePageItemVM){
                itemBinding.set(BR.vm, R.layout.collect_article_list_pager_item_frag);

            }

        }
    });

    public String []titles=new String []{"视频","音频","文章"};
    public final BindingViewPagerAdapter.PageTitles<BaseViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<BaseViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, BaseViewModel item) {
            return titles[position];
        }
    };

    public ListVM(Context context) {
        super(context);
    }

    public ObservableList<LearnCate.DataBean> categoryList = new ObservableArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据

        pageList.add(new VideoPageItemVM(context));
        pageList.add(new AudioPageItemVM(context));
        pageList.add(new ArticlePageItemVM(context));

//        getCategory();



    }

    Disposable d = null;




    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
