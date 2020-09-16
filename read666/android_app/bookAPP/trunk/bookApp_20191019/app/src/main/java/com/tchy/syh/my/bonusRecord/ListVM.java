package com.tchy.syh.my.bonusRecord;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.my.ApiService;
import com.tchy.syh.orders.entity.OrderResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ListVM extends BaseViewModel {

    public ObservableList<ListItemVM> listItems = new ObservableArrayList<>();

    public final ItemBinding itemBinding = ItemBinding.of(BR.vm,R.layout.my_records_item);

    public ListVM(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        getList(true);

    }
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isRefreshFinish.set(false);
            pageNum = 0;
            //重新请求
            getList(true);

        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinish.set(false);
            pageNum++;
            getList(false);
        }
    });
    private int pageNum = 0;
    public ObservableBoolean isRefreshFinish = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);
    public void getList(boolean isRefresh) {
        HashMap<String, Object> params = new HashMap();
        params.put("type","integral");
        params.put("access_token", SPUtils.getInstance().getString("token"));
        params.put("page", this.pageNum);
        Observable<OrderResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .order_list(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(res -> {


            if (res.getStatus() != 1) {
                return;

            }
            if (isRefresh)
                listItems.clear();

            if(res.getData().getList()!=null){
                Observable.fromIterable(res.getData().getList()).subscribe(bean -> {
                            listItems.add(new ListItemVM(context, bean));
                        }
                );
            }
//            Gson gson=new Gson();
//            OrderResp.DataBean.ListBean bean=gson.fromJson("  {\n" +
//                    "                \"id\":668,\n" +
//                    "                \"order_sn\":\"18911100601212\",\n" +
//                    "                \"goods_name\":\"把信送给加西亚\",\n" +
//                    "                \"thumb\":\"http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg\",\n" +
//                    "                \"num\":1,\n" +
//                    "                \"goods_jefen\":0,\n" +
//                    "                \"goods_amount\":58,\n" +
//                    "                \"amount\":39.98,\n" +
//                    "                \"jifen\":0,\n" +
//                    "                \"time\":1536631561,\n" +
//                    "                \"pay_status\":\"0\",\n" +
//                    "                \"pay_status_str\":\"未付款\",\n" +
//                    "                \"shipping_status\":\"0\",\n" +
//                    "                \"shipping_status_str\":\"未发货\",\n" +
//                    "                \"desc\":\"企业家，政府机关和员工的职业精神读本！\"\n" +
//                    "            }", OrderResp.DataBean.ListBean.class);
//            listItems.add(new ListItemVM(context, bean));
//            listItems.add(new ListItemVM(context, bean));
//            listItems.add(new ListItemVM(context, bean));
            if (isRefresh)
                this.isRefreshFinish.set(true);
            else
                this.isLoadMoreFinish.set(true);



        },e -> {
            if (isRefresh) {
                this.isRefreshFinish.set(true);
            } else
                this.isLoadMoreFinish.set(true);
            e.printStackTrace();
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
