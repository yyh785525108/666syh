package com.tchy.syh.daily_advance.daily_home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.custom.share.ShareDialogBlurView;
import com.tchy.syh.daily_advance.entity.DailyAdvanceListResp;
import com.tchy.syh.databinding.DailyCommentDetailBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseActivity;

public class DailyCommentActivity extends BaseActivity<DailyCommentDetailBinding, DailyCommentVm> {
    public String daily_id;
    Disposable d;

    ShareDialogBlurView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("sort", "onCreate: ");
        List<Fragment> list = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }


        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }



    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.daily_comment_detail;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }
    public DailyAdvanceListResp.DataBean.ListBean entity;
    @Override
    public DailyCommentVm initViewModel() {
        daily_id = getIntent().getStringExtra("id");
        entity = getIntent().getParcelableExtra("bean");
        return new DailyCommentVm(this,entity,daily_id);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
