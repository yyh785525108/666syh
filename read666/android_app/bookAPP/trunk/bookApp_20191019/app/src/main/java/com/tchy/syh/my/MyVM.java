package com.tchy.syh.my;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.os.Bundle;

import com.tchy.syh.R;
import com.tchy.syh.base.CustomContainerActivity;
import com.tchy.syh.common.entity.CommonNoDataResp;
import com.tchy.syh.common.CommonViewModel;
import com.tchy.syh.messages.MessageFragment;
import com.tchy.syh.my.entity.MyResp;
import com.tchy.syh.my.feedback.FeedBackFragment;
import com.tchy.syh.my.mission.MissionFragment;
import com.tchy.syh.my.spreadranking.ListFragment;
import com.tchy.syh.settings.SettingsFragment;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.TimeFormatUtil;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class MyVM extends BaseViewModel {

    public ObservableInt notifyNum = new ObservableInt();
    private int itemIndex = 0;
    public ObservableField<String> search = new ObservableField<>("");
    public ObservableInt bannerIndex = new ObservableInt(0);
    public ObservableField<MyResp.DataBean> bean =new ObservableField<>();
    public ObservableField<String> levelLimitDate = new ObservableField<>("");

    public ObservableList<CommonViewModel> bannerList = new ObservableArrayList<>();

    public BindingCommand missionClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            Bundle bundle=new Bundle();
            bundle .putBoolean("isSign",false );
            startContainerActivity(MissionFragment.class.getCanonicalName(),bundle);
        }
    });

    public BindingCommand bonusClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }

            startContainerActivity(com.tchy.syh.my.bonus.ListFragment.class.getCanonicalName());

        }
    });
    public BindingCommand signedClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            sign();
        }
    });
    public BindingCommand shopClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startContainerActivity(com.tchy.syh.shopping.home.ListFragment.class.getCanonicalName());
        }
    });
    public BindingCommand ordersClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startContainerActivity(com.tchy.syh.orders.ListFragment.class.getCanonicalName());
        }
    });
    public BindingCommand messageClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startContainerActivity(MessageFragment.class.getCanonicalName());
        }
    });
    public BindingCommand historyClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            startContainerActivity(com.tchy.syh.fav.collect.ListFragment.class.getCanonicalName());
        }
    });
    public BindingCommand collectClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            startContainerActivity(com.tchy.syh.collect.ListFragment.class.getCanonicalName());
        }
    });

    public BindingCommand spreadClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            startContainerActivity(ListFragment.class.getCanonicalName());

        }
    });
    public BindingCommand recommandClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            startContainerActivity(RecommandBookListFragment.class.getCanonicalName());
        }
    });
    public BindingCommand studyClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            Bundle bundle =new Bundle();
            bundle.putString("fragment", com.tchy.syh.my.studyRanking.ListFragment.class.getCanonicalName());
            startActivity(CustomContainerActivity.class,bundle);

        }
    });
    public BindingCommand helpClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            Bundle bundle =new Bundle();
            bundle.putString("fragment", HelpFragment.class.getCanonicalName());
            startActivity(CustomContainerActivity.class,bundle);
        }
    });
    public BindingCommand vipClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            startContainerActivity(VipFragment.class.getCanonicalName());

        }
    });


   public BindingCommand settingClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            startContainerActivity(SettingsFragment.class.getCanonicalName());

        }
    });

    public BindingCommand avatarClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
            }


        }
    });
    public BindingCommand feedbackClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(bean.get()==null){
                startActivity(LoginActivity.class);
                return;
            }
            startContainerActivity(FeedBackFragment.class.getCanonicalName());

        }
    });
    public MyVM(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        user_info();
        Messenger.getDefault().register(context, "refresh", String.class, new BindingConsumer<String>() {
            @Override
            public void call(String s) {
                user_info();
            }
        });
        Messenger.getDefault().register(context, "myRefresh", new BindingAction() {
            @Override
            public void call() {
                user_info();
            }
        });
    }

    public void user_info(){
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<MyResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .user_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if(v.getStatus()==1){

                bean.set(v.getData());
                if(bean.get().getLevel()>0){
                    levelLimitDate.set(context.getResources().getString(R.string.vip_limit,TimeFormatUtil.format(bean.get().getDeadline())));
                    if(bean.get().getLevel()>=3){
                        levelLimitDate.set(context.getResources().getString(R.string.vip_limit,"永久"));
                    }
                }else{
                    levelLimitDate.set("每年共读66本书");
                }
                SPUtils.getInstance().put("nickname", v.getData().getNickname());
                SPUtils.getInstance().put("avatar", v.getData().getAvatar());
                SPUtils.getInstance().put("birthday", v.getData().getBirthday());
                SPUtils.getInstance().put("sex", v.getData().getSex());
                isSigned.set(v.getData().getIs_qiandao());
                SPUtils.getInstance().put("password", v.getData().getPassword());
                SPUtils.getInstance().put("level", v.getData().getLevel());
                SPUtils.getInstance().put("level_name", v.getData().getLevel_name());
                SPUtils.getInstance().put("deadline", v.getData().getDeadline());
                SPUtils.getInstance().put("parent_name", v.getData().getParent_name());
                SPUtils.getInstance().put("parent_id", v.getData().getParent_id());
                SPUtils.getInstance().put("money", v.getData().getMoney()+"");
                SPUtils.getInstance().put("integral",  v.getData().getIntegral()+"");
                SPUtils.getInstance().put("mobile",  v.getData().getMobile());

                SPUtils.getInstance().put("is_new",  v.getData().getIs_new());

                SPUtils.getInstance().put("fensi", v.getData().getFensi());
                SPUtils.getInstance().put("birthday", v.getData().getBirthday());
                SPUtils.getInstance().put("is_qiandao", v.getData().getIs_qiandao());

            }else{
                bean.set(null);
            }


        },e -> {
            e.printStackTrace();
        });

    }
//    public void getLevelName(){
//        bean.get().setLevel(3);
//        if(bean.get().getLevel()>0){
//            this.levelName.set(context.getString(R.string.vip_level,bean.get().getLevel_name()));
//            if(bean.get().getLevel()>=3){
//                this.levelName.set("永久");
//
//            }
//        }else{
//            this.levelName.set(context.getString(R.string.vip_reg));
//        }
//
//    }

    public void sign(){
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<CommonNoDataResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .qiandao(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if(v.getStatus()==1){
                ToastUtil.toastBottom(v.getInfo());
                isSigned.set(0);
                user_info();
//                Bundle bundle=new Bundle();
//                bundle .putBoolean("isSign",true );
//                startContainerActivity(FeedBackFragment.class.getCanonicalName(),bundle);


            }else{
                isSigned.set(1);
                user_info();
                ToastUtil.toastBottom(v.getInfo());
//                Bundle bundle=new Bundle();
//                bundle .putBoolean("isSign",true );
//                startContainerActivity(FeedBackFragment.class.getCanonicalName(),bundle);
//                ToastUtil.toastBottom(v.getInfo());
            }


        },e -> {
            e.printStackTrace();
        });

    }
    public ObservableInt isSigned=new ObservableInt();
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
