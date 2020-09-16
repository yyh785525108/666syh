package com.tchy.syh.book.book_detail;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.book_detail.audio.BookDetailAudioFragment;
import com.tchy.syh.book.book_detail.image.BookDetailImageFragment;
import com.tchy.syh.book.book_detail.video.BookDetailVideoFragment;
import com.tchy.syh.custom.share.ShareDialogBlurView;
import com.tchy.syh.databinding.BookDetailBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseActivity;

public class BookVideoPlayerActivity extends BaseActivity<BookDetailBinding, BookDetailVm> {
    BookDetailVideoFragment videoFragment;
    BookDetailAudioFragment audioFragment;
    BookDetailImageFragment imageFragment;
    public String book_id;
    Disposable d;

    ShareDialogBlurView view;

//    public void showShareView(){
//        if(view!=null){
//            view.setVisibility(View.VISIBLE);
//        }
//    }
//    public void closeShareView(){
//        if(view!=null){
//            view.setVisibility(View.INVISIBLE);
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CommentDialog dialog=new CommentDialog();
//        dialog.show(getSupportFragmentManager(),"dashang");

//        d=RxBus.getDefault().toObservable(DashangDialog.DashangBean.class).subscribe(v->{
//            Log.d("sort", "onCreate: "+v);
//            ToastUtils.showShort("支付"+v.money+"元");
//        });

//        shareView = getLayoutInflater().inflate(R.layout.share_dialog, null);
//        shareView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));

//        shareView.setVisibility(View.GONE);

//        view=new ShareDialogBlurView(this);
//        ((ViewGroup)binding.getRoot()).addView(view);
        Log.d("sort", "onCreate: ");
        List<Fragment> list = new ArrayList<>();
        videoFragment = new BookDetailVideoFragment();
        imageFragment = new BookDetailImageFragment();
        audioFragment = new BookDetailAudioFragment();
        list.add(videoFragment);
        list.add(imageFragment);
        list.add(audioFragment);
        this.binding.vp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return list.get(position);

            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        this.binding.vp.setScanScroll(false);
        this.binding.vp.setOffscreenPageLimit(3);
        this.binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (videoFragment.getPlayer() != null) {
//                    if(videoFragment.getPlayer().isInPlayingState())
//                        videoFragment.getPlayer().onVideoPause();
//                }
//                audioFragment.mediaPause();

                videoFragment.pausePlayer();

                audioFragment.mediaPause();

                if(position>0){
                    binding.bottombar.up.setVisibility(View.VISIBLE);
                }else{
                    binding.bottombar.up.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
//        if (orientationUtils != null) {
//            orientationUtils.backToProtVideo();
//        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }


        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (videoFragment != null) {
//            videoFragment.releasePlayer();
//
//        }
////        audioFragment.mediaRelease();
////        if(d!=null){
////             if (d != null) {                                     d.dispose();                                 }
////        }
//        if(audioFragment!=null)
//            audioFragment.mediaRelease();

    }



    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.book_detail;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BookDetailVm initViewModel() {
        book_id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        return new BookDetailVm(this, book_id, name);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

//        viewModel.isshareShow.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                if(viewModel.isshareShow.get()){
//                   showShareView();
//                }else{
//                    closeShareView();
//                }
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoFragment!= null) {
            videoFragment.pausePlayer();
        }
      if(audioFragment!=null)
        audioFragment.mediaPause();
    }


}
