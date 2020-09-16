package com.tchy.syh.book.book_detail.video;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.util.Log;

import com.tchy.syh.book.book_detail.BookDetailVm;
import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.my.VipFragment;
import com.tchy.syh.userAccount.account_management.modify_mobile.ModMobileFragment;
import com.tchy.syh.utils.ToastUtil;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;


public class BookDetailVideoPageVm extends BaseViewModel {
    public ObservableField<BookDetailResp.DataBean> dataBean = new ObservableField<>();
    public ObservableField<String> commentCount=new ObservableField<>("0");
    public BookDetailVideoPageVm(Context context){
        super(context);


    }



    @Override
    public void onCreate() {
        super.onCreate();
        Messenger.getDefault().register(context, "refresh", String.class, new BindingConsumer<String>() {
            @Override
            public void call(String s) {
                isShowVip.set(false);

            }
        });

        if("1".equals(SPUtils.getInstance().getString("is_new"))){
            isShowYiyuan.set(true);
        }
        //请求网络数据
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister(context);
    }


    public void collect(){
//        dataBean.get().getId();

    }
    public BindingCommand backClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ((Activity)context).finish();
        }
    });
    @Override
    public void registerRxBus() {
        super.registerRxBus();
        Messenger.getDefault().register(context, BookDetailVm.BOOK_DETAIL_UPDATE, BookDetailResp.DataBean.class, new BindingConsumer< BookDetailResp.DataBean>() {
            @Override
            public void call(final  BookDetailResp.DataBean bean) {
                //删除选择对话框
                dataBean.set(bean);

                Log.d("sort", "call: "+bean.getTitle());

            }

        });



    }


    public ObservableBoolean isShowVip=new ObservableBoolean(false);
    public ObservableBoolean isShowYiyuan=new ObservableBoolean(false);
    public BindingCommand gotoVIP=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(VipFragment.class.getCanonicalName());
        }
    });

    public BindingCommand extClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(StringUtils.isEmpty(SPUtils.getInstance().getString("mobile"))){
                ToastUtil.toastBottom("请先绑定手机");
                startContainerActivity(ModMobileFragment.class.getCanonicalName());
            }else{
                Messenger.getDefault().sendNoMsg("yiyuanMobileBindingSuccess");
            }
        }
    });


}
