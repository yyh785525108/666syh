package com.tchy.syh.custom.toolbar;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableField;
import android.os.Bundle;

import com.tchy.syh.messages.MessageFragment;
import com.tchy.syh.search.SearchFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


public class CustomToolbarVM extends BaseViewModel {

    public ObservableField<String> text = new ObservableField<>("");

    public void setLeftObserve(BindingCommand leftObserve) {
        this.leftObserve = leftObserve;
    }

    public void setRightObserve(BindingCommand rightObserve) {
        this.rightObserve = rightObserve;
    }

    public BindingCommand leftObserve;
    public BindingCommand rightObserve;
    public int type;


    public CustomToolbarVM(Context context,int type) {
        super(context);
        this.type=type;
    }

    public BindingCommand onLeftClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(leftObserve!=null)
                leftObserve.execute();
            else{
                ((Activity)context).finish();
            }
        }
    });
    public BindingCommand onSearchClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putString("searchKey", text.get());
            bundle.putInt("type",type);
            startContainerActivity(SearchFragment.class.getCanonicalName(),bundle );
        }
    });
    public BindingCommand onRightClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(rightObserve!=null)
                rightObserve.execute();
            else{
                startContainerActivity(MessageFragment.class.getCanonicalName());
            }
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
