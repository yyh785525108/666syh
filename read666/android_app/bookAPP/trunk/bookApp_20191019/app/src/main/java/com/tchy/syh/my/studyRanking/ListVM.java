package com.tchy.syh.my.studyRanking;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.custom.CustomLoadingDialog;
import com.tchy.syh.my.ApiService;
import com.tchy.syh.my.entity.StudyListResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class ListVM extends BaseViewModel {

    public ObservableList<ListItemVM> listItems = new ObservableArrayList<>();


    public final ItemBinding itemBinding = ItemBinding.of(new OnItemBind<Object>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, Object item) {
            itemBinding.set(BR.vm, position == 0 ? R.layout.study_ranking_list_header : R.layout.study_ranking_list_item);
        }
    });

    public ListVM(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据



    }

    // 获得当前周- 周一的日期
    private Date getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        return currentDate.getTime();
    }

    // 获得当前日期与本周一相差的天数
    private int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    // 获得当前周- 周日  的日期
    private Date getPreviousSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        return currentDate.getTime();

    }

    public void paihan_list(CustomLoadingDialog dialog) {


        HashMap<String, Object> params = new HashMap();
        params.put("access_token", SPUtils.getInstance().getString("token"));
        if(type.get()==0){
            params.put("start_time", getCurrentMonday().getTime()/1000);
            params.put("end_time", getPreviousSunday().getTime()/1000);
        }

        Observable<StudyListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .paihan_list(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            dialog.dismiss();
            listItems.add(new ListItemVM(context, v.getData().getList().get(0), v.getData().getList().get(1), v.getData().getList().get(2), v.getData().getMine()));
            Observable.fromIterable(v.getData().getList()).skip(3).subscribe(item ->
                    {
                        listItems.add(new ListItemVM(context, item));
                    }
            );
        }, e -> {
            e.printStackTrace();
        });

    }
    public ObservableInt type=new ObservableInt();
    public BindingCommand changed=new BindingCommand(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer o) {
            listItems.clear();
            type.set(o);
        }
    });
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
