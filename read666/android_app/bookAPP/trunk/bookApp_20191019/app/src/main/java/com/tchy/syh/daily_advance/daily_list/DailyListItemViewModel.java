package com.tchy.syh.daily_advance.daily_list;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.text.TextUtils;
import android.util.Log;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.daily_advance.entity.DailyAdvanceListResp;

import java.util.Date;
import java.util.HashMap;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class DailyListItemViewModel extends BaseViewModel {
    public ObservableInt notifyNum = new ObservableInt();
    private int itemIndex = 0;
    private int pageNum = 0;
    public ObservableBoolean isRefreshFinish = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);


    public ObservableField<String> search = new ObservableField<>("");
    public ObservableList<DailyListItemVm> dailyList = new ObservableArrayList<>();
    public ItemBinding<DailyListItemVm> dailyItemBinding = ItemBinding.of(BR.vm, R.layout.daily_list_item);


    private int index;
    private String id;


    public DailyListItemViewModel(Context context, int index, String id) {
        super(context);
        this.index = index;
        this.id=id;
        Log.d("sort", "VideoPageItemVM: " + this.index);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        Log.d("sort", "onCreate111111111: " + this.index);
        getDailsList(true);

    }


    public void getDailsList(boolean isRefresh) {

        HashMap<String, Object> params = new HashMap();
        if(!TextUtils.isEmpty(id))
            params.put("fid", id);
            params.put("type", 1);
            params.put("orderby", 0);
            params.put("page", this.pageNum);
        DailyAdvanceListResp.DataBean.ListBean bean = new DailyAdvanceListResp.DataBean.ListBean();
        bean.setLove_id("0");
        bean.setContent("1.就分开算哒发涮诶哦啊房东\n" +
                "2.黄金框框考的\n" +
                "3.房东就啃红啊减肥\n" +
                "4.当看骚就爱法抗拒\n" +
                "大森好撒进看三党发窘况就慌撒放假抗冻方案韩风散昂放空间昂扥看森哈看放单撒房堪桑房三单康复撒旦肯喊死扥就红等反董噶发三啊狂三房开三方老撒就发牢骚的" +
                "三垦囧看法开镜框撒就分开三打单发了但方而无踹绕丧夫看懂一" +
                "放松蛋昂法看撒扥好龙大恨啊少翻到撒很塑窗昂法动就卡fsdjfksdaj共打风撒健康肤色的放骚啊解放骚啦反龙啊到反龙爱的嫩姜骚啊来烦死到反卡死淡扫烦死带肯讲赛看");
        bean.setAdd_time(""+new Date().getTime());
        bean.setType(1);
        if (pageNum<=9){
            bean.setImage_num(pageNum);
        }else{
            bean.setImage_num(4);
        }
        bean.setNickname("靠");
        bean.setLovenum(100);
        bean.setId("12");
        bean.setIs_love(1);
        bean.setLearn_rank((10 + 100)+"/365"  );
        dailyList.add(new DailyListItemVm(context, bean));
           /** Observable<DailyAdvanceListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                    .learn(FormdataRequestUtil.getFormDataRequestParams(params))
                    .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });

            observable.subscribe(res -> {

                if (res.getStatus() != 1) {
                    return;

                }
                if(pageNum>50){
                    ToastUtil.toastBottom("没有更多内容了！");
                    return ;
                }
                if(pageNum>0&&res.getData().getList().size()==0){
                    ToastUtil.toastBottom("没有更多内容了！");
                    return ;
                }
                if (isRefresh)
                    dailyList.clear();

                Observable.fromIterable(res.getData().getList()).subscribe(bean -> {
                    dailyList.add(new DailyListItemVm(context, bean));
                        }
                );
            }, e -> {
                e.printStackTrace();
            },()->{});
            */
            if (isRefresh) {
                this.isRefreshFinish.set(true);
            } else
                this.isLoadMoreFinish.set(true);
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isRefreshFinish.set(false);
            pageNum = 0;
            //重新请求
            getDailsList(true);
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinish.set(false);
            pageNum++;
            getDailsList(false);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
