package com.tchy.syh.book.book_detail.image;

import android.content.Context;
import androidx.databinding.ObservableField;

import com.tchy.syh.book.book_detail.BookDetailVm;
import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.common.CommonBottomBarVM;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;


public class BookDetailImagePageVm extends BaseViewModel {

    public ObservableField<String> content = new ObservableField<>();
    public String contentValue = "";

    public BookDetailImagePageVm(Context context) {
        super(context);
    }

    public ObservableField<CommonBottomBarVM> bottomVM = new ObservableField<>();


    @Override
    public void registerRxBus() {
        super.registerRxBus();
        Messenger.getDefault().register(context, BookDetailVm.BOOK_DETAIL_UPDATE, BookDetailResp.DataBean.class, new BindingConsumer<BookDetailResp.DataBean>() {
            @Override
            public void call(final BookDetailResp.DataBean bean) {
                //删除选择对话框
//                content.set(bean.getContent_fee());
                bottomVM.set(new CommonBottomBarVM(context, bean, false));
                contentValue = bean.getContent_tuwen();

            }

        });


    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据


    }

    @Override
    public void onDestroy() {
        if (content != null)
            content.set("");
        super.onDestroy();
        Messenger.getDefault().unregister(context);


    }
}
